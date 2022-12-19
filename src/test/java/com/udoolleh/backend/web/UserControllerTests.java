package com.udoolleh.backend.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udoolleh.backend.entity.User;
import com.udoolleh.backend.provider.service.UserService;
import com.udoolleh.backend.repository.UserRepository;
import com.udoolleh.backend.utils.SHA256Util;
import com.udoolleh.backend.web.dto.RequestUser;
import com.udoolleh.backend.web.dto.ResponseUser;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("회원가입 성공 테스트 - 상태코드 : 200")
    @Transactional
    @Test
    void registerUserTest() throws Exception {
        RequestUser.RegisterUserDto dto = RequestUser.RegisterUserDto.builder()
                .email("email")
                .nickname("nickname")
                .password("password")
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders
                .post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("user-post", // 문서 조각 디렉토리 명
                                preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                        .removePort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("email"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("nickname"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("password")
                                        ),
                                responseFields( // response 필드 정보 입력
                                        fieldWithPath("id").type(JsonFieldType.STRING).description("응답 아이디"),
                                        fieldWithPath("dateTime").type(JsonFieldType.STRING).description("응답 시간"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
                                        fieldWithPath("list").type(JsonFieldType.NULL).description("반환 리스트")
                                )
                        )
                )
        ;
    }
    @DisplayName("로그인 성공 테스트 - 상태코드 : 200")
    @Transactional
    @Test
    void loginUserTest() throws Exception{
        User user = User.builder()
                .email("test")
                .salt("salt")
                .nickname("testNickname")
                .password(SHA256Util.getEncrypt("password","salt"))
                .build();
        userRepository.save(user);

        RequestUser.LoginDto loginDto = RequestUser.LoginDto.builder()
                .email("test")
                .password("password")
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders
                .post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("userLogin-post", // 문서 조각 디렉토리 명
                                preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                        .removePort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("email"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("password")
                                ),
                                responseFields( // response 필드 정보 입력
                                        fieldWithPath("id").type(JsonFieldType.STRING).description("응답 아이디"),
                                        fieldWithPath("dateTime").type(JsonFieldType.STRING).description("응답 시간"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
                                        fieldWithPath("list.accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                                        fieldWithPath("list.refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰")
                                )
                        )
                )
        ;
    }

    @DisplayName("로그아웃 성공 테스트 - 상태코드 : 200")
    @Transactional
    @Test
    void logoutUserTest() throws Exception{
        User user = User.builder()
                .email("test")
                .salt("salt")
                .nickname("testNickname")
                .password(SHA256Util.getEncrypt("password","salt"))
                .build();
        userRepository.save(user);

        RequestUser.LoginDto loginDto = RequestUser.LoginDto.builder()
                .email("test")
                .password("password")
                .build();
        ResponseUser.Token token = userService.login(loginDto).orElse(null);

        mockMvc.perform(RestDocumentationRequestBuilders
                .post("/logout")
                .header("x-auth-token", token.getRefreshToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("userLogout-post", // 문서 조각 디렉토리 명
                                preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                        .removePort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("x-auth-token").description("리프레시 토큰")),
                                responseFields( // response 필드 정보 입력
                                        fieldWithPath("id").type(JsonFieldType.STRING).description("응답 아이디"),
                                        fieldWithPath("dateTime").type(JsonFieldType.STRING).description("응답 시간"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
                                        fieldWithPath("list").type(JsonFieldType.NULL).description("반환 리스트")
                                )
                        )
                )
        ;
    }

    @DisplayName("액세스 토큰 갱신 성공 테스트 - 상태코드 : 200")
    @Transactional
    @Test
    void refreshTokenTest() throws Exception {
        User user = User.builder()
                .email("test")
                .salt("salt")
                .nickname("testNickname")
                .password(SHA256Util.getEncrypt("password","salt"))
                .build();
        userRepository.save(user);

        RequestUser.LoginDto loginDto = RequestUser.LoginDto.builder()
                .email("test")
                .password("password")
                .build();
        ResponseUser.Token token = userService.login(loginDto).orElse(null);
        Map<String, String> refreshToken = new HashMap<>();
        refreshToken.put("refreshToken", token.getRefreshToken());

        mockMvc.perform(RestDocumentationRequestBuilders
                .post("/refreshToken")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshToken)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("updateAccessToken-post", // 문서 조각 디렉토리 명
                                preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                        .removePort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰")),
                                responseFields( // response 필드 정보 입력
                                        fieldWithPath("id").type(JsonFieldType.STRING).description("응답 아이디"),
                                        fieldWithPath("dateTime").type(JsonFieldType.STRING).description("응답 시간"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
                                        fieldWithPath("list.accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                                        fieldWithPath("list.refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰")
                                )
                        )
                )
        ;
    }

    @DisplayName("유저 프로필 수정 - 상태코드 : 200")
    @Transactional
    @Test
    void updateUserInfoTest() throws Exception {
        User user = User.builder()
                .email("test")
                .salt("salt")
                .nickname("testNickname")
                .password(SHA256Util.getEncrypt("password","salt"))
                .build();
        userRepository.save(user);

        RequestUser.LoginDto loginDto = RequestUser.LoginDto.builder()
                .email("test")
                .password("password")
                .build();
        ResponseUser.Token token = userService.login(loginDto).orElse(null);

        RequestUser.UpdateUserDto updateUserDto = RequestUser.UpdateUserDto.builder()
                .nickname("newNickname")
                .password("password")
                .build();
        mockMvc.perform(RestDocumentationRequestBuilders
                .put("/user")
                .header("x-auth-token", token.getRefreshToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("updateUserInfo-put", // 문서 조각 디렉토리 명
                                preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                        .removePort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("x-auth-token").description("액세스 토큰")),
                                requestFields(
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("변경하고자 하는 닉네임"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("변경하고자 하는 패스워드")),
                                responseFields( // response 필드 정보 입력
                                        fieldWithPath("id").type(JsonFieldType.STRING).description("응답 아이디"),
                                        fieldWithPath("dateTime").type(JsonFieldType.STRING).description("응답 시간"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
                                        fieldWithPath("list").type(JsonFieldType.NULL).description("반환 리스트")
                                )
                        )
                )
        ;
    }

    @DisplayName("유저 이미지 업로드 - 상태코드 : 200")
    @Transactional
    @Test
    void uploadUserImageTest() throws Exception {
        User user = User.builder()
                .email("test")
                .salt("salt")
                .nickname("testNickname")
                .password(SHA256Util.getEncrypt("password","salt"))
                .build();
        userRepository.save(user);

        RequestUser.LoginDto loginDto = RequestUser.LoginDto.builder()
                .email("test")
                .password("password")
                .build();
        ResponseUser.Token token = userService.login(loginDto).orElse(null);

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test2.png",
                "image/png", "test data".getBytes());

        mockMvc.perform(RestDocumentationRequestBuilders
                .multipart("/user/image")
                .file(mockMultipartFile)
                .header("x-auth-token", token.getRefreshToken())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("uploadUserImage-post", // 문서 조각 디렉토리 명
                                preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                        .removePort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("x-auth-token").description("액세스 토큰")),
                                requestParts(
                                        partWithName("file").description("사용자 프로필 사진")),
                                responseFields( // response 필드 정보 입력
                                        fieldWithPath("id").type(JsonFieldType.STRING).description("응답 아이디"),
                                        fieldWithPath("dateTime").type(JsonFieldType.STRING).description("응답 시간"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
                                        fieldWithPath("list").type(JsonFieldType.NULL).description("반환 리스트")
                                )
                        )
                )
        ;
    }


}
