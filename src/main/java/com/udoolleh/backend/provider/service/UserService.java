package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.security.role.Role;
import com.udoolleh.backend.core.service.UserServiceInterface;
import com.udoolleh.backend.entity.User;
import com.udoolleh.backend.exception.errors.*;
import com.udoolleh.backend.provider.security.JwtAuthToken;
import com.udoolleh.backend.provider.security.JwtAuthTokenProvider;
import com.udoolleh.backend.repository.UserRepository;
import com.udoolleh.backend.utils.SHA256Util;
import com.udoolleh.backend.web.dto.RequestUser;
import com.udoolleh.backend.web.dto.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;
    private final JwtAuthTokenProvider jwtAuthTokenProvider;
    private final S3Service s3Service;

    //회원가입 API
    @Transactional
    @Override
    public void register(RequestUser.registerDto registerDto) {
        User user = userRepository.findByEmail(registerDto.getEmail());
        if (user != null) {
            throw new RegisterFailedException();
        }
        user = userRepository.findByNickname(registerDto.getNickname());
        if(user != null){//닉네임 중복시
            throw new UserNicknameDuplicatedException();
        }
        String salt = SHA256Util.generateSalt();

        //SHA256으로 솔트와 암호화
        String encryptedPassword = SHA256Util.getEncrypt(registerDto.getPassword(), salt);

        //DB에 기입
        user = User.builder()
                .email(registerDto.getEmail())
                .password(encryptedPassword)
                .nickname(registerDto.getNickname())
                .salt(salt)
                .build();
        userRepository.save(user);
    }

    //로그인 API
    @Transactional
    @Override
    public Optional<ResponseUser.Login> login(RequestUser.loginDto loginDto) {

        User user = userRepository.findByEmail(loginDto.getEmail());
        if (user == null) {
            throw new LoginFailedException();
        }
        String salt = user.getSalt();
        String encryptedPassword = SHA256Util.getEncrypt(loginDto.getPassword(), salt);

        user = userRepository.findByEmailAndPassword(loginDto.getEmail(), encryptedPassword);
        ResponseUser.Login login = null;

        if (user != null) {
            String refreshToken = createRefreshToken((user.getEmail()));
            login = ResponseUser.Login.builder()
                    .accessToken(createAccessToken(user.getEmail()))
                    .refreshToken(refreshToken)
                    .build();

            user.changeRefreshToken(refreshToken);
        } else {
            throw new LoginFailedException();
        }
        return Optional.ofNullable(login);
    }

    @Transactional
    @Override
    public Optional<ResponseUser.Token> refreshToken(String token) {
        if(token == null || token.equals("null")) {
            throw new CustomJwtRuntimeException();
        }
        //db에서 리프레시 토큰으로 어드민을 꺼낸다.
        User user = userRepository.findByRefreshToken(token);
        //없으면 실패
        if(user == null) {
            throw new CustomJwtRuntimeException();
        }
        //디비에 있는 토큰이랑 다르면 실패, 즉 현재는 한 기기에만 로그인 가능, 여러 기기에 하려면 엔티티를 별도로 생성해야할듯
        if(!user.getRefreshToken().equals(token)) {
            throw new CustomJwtRuntimeException();
        }
        //토큰 유효 검증
        JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token);
        if(!jwtAuthToken.validate() || !jwtAuthToken.getData().get("role").equals(Role.USER.getCode())) {

            return Optional.empty();
        }

        String id = String.valueOf(jwtAuthToken.getData().get("id"));
        String accessToken = createAccessToken(id); // 액세스토큰 재발급

        ResponseUser.Token newToken = ResponseUser.Token.builder()
                .accessToken(accessToken)
                .refreshToken(token)
                .build();
        return Optional.ofNullable(newToken);
    }


    @Override
    public String createAccessToken(String id) {
        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant()); // 토큰은 2분만 유지되도록 설정, 2분 후 refresh token
        JwtAuthToken accessToken = jwtAuthTokenProvider.createAuthToken(id, Role.USER.getCode(), expiredDate);
        return accessToken.getToken();
    }


    @Override
    public String createRefreshToken(String id) {
        Date expiredDate = Date.from(LocalDateTime.now().plusYears(1).atZone(ZoneId.systemDefault()).toInstant()); // refresh토큰은 유효기간이 1년
        JwtAuthToken refreshToken = jwtAuthTokenProvider.createAuthToken(id, Role.USER.getCode(), expiredDate);
        return refreshToken.getToken();
    }

    @Override
    @Transactional
    public void updateUser(String email, MultipartFile file, RequestUser.updateDto requestDto){
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new NotFoundUserException();
        }
        User user1 = userRepository.findByNickname(requestDto.getNickname());
        if(user1 != null){//닉네임 중복시
            throw new UserNicknameDuplicatedException();
        }
        if(!Optional.ofNullable(user.getProfile()).isEmpty()){//프로필 사진이 이미 s3에 등록 됐을 경우 삭제 후 업로드
            s3Service.deleteFile(user.getProfile());
        }
        //프로필 사진 s3에 등록
        String url="";
        try {
            url = s3Service.upload(file, "user");
        }catch (IOException e){
            System.out.println("s3 등록 실패");
        }

        //salt생성
        String salt = SHA256Util.generateSalt();
        //비밀번호 암호화
        String encryptedPassword = SHA256Util.getEncrypt(requestDto.getPassword(),salt);
        //유저 정보 수정
        user.changeUserInfo(encryptedPassword,requestDto.getNickname(),salt,url);

    }
}
