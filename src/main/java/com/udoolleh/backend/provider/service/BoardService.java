package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.service.BoardServiceInterface;
import com.udoolleh.backend.entity.Board;
import com.udoolleh.backend.entity.Likes;
import com.udoolleh.backend.entity.User;
import com.udoolleh.backend.exception.errors.CustomJwtRuntimeException;
import com.udoolleh.backend.exception.errors.NotFoundBoardException;
import com.udoolleh.backend.exception.errors.NotFoundUserException;
import com.udoolleh.backend.repository.BoardRepository;
import com.udoolleh.backend.repository.LikeRepository;
import com.udoolleh.backend.repository.UserRepository;
import com.udoolleh.backend.web.dto.RequestBoard;
import com.udoolleh.backend.web.dto.ResponseBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BoardService implements BoardServiceInterface {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final S3Service s3Service;
    private final LikeRepository likeRepository;
    //게시글 전체 조회
    @Transactional(readOnly = true)
    @Override
    public Page<ResponseBoard.listBoardDto> boardList(String userEmail, Pageable pageable) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new CustomJwtRuntimeException();
        }

        Page<Board> board = boardRepository.findAll(pageable);
        return board.map(ResponseBoard.listBoardDto::of);

    }

    @Transactional(readOnly = true)
    @Override
    public ResponseBoard.detailBoardDto boardDetail(String userEmail, String boardId) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new CustomJwtRuntimeException();
        }

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NotFoundBoardException());

        return new ResponseBoard.detailBoardDto(board);
    }


    //게시글 등록 API
    @Override
    @Transactional
    public void registerPosts(MultipartFile file, String userEmail, RequestBoard.registerDto postDto) { //postDto = title, context + category 추후 추가
        User user = userRepository.findByEmail(userEmail);

        if (user == null) {
            throw new CustomJwtRuntimeException();
        }

        //Board board = boardRepository.findByTitleContext(postDto.getTitle(), postDto.getContext);
        //if (board == null) { ~ }
        //빌더 패턴으로 게시글 작성

        Board board = Board.builder()
                .title(postDto.getTitle())
                .context(postDto.getContext())
                .user(user)
                .build();

        board = boardRepository.save(board);
        user.addBoard(board);

        if (Optional.ofNullable(file).isPresent()) {
            String url = "";
            try {
                url = s3Service.upload(file, "board");
                board.updatePhoto(url);
            } catch (IOException e) {
                System.out.println("s3 등록 실패");
            }
        }
    }

    @Override
    @Transactional
    public void modifyPosts(MultipartFile file, String userEmail, String boardId, RequestBoard.updatesDto modifyDto) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new CustomJwtRuntimeException();
        }
        Board board = boardRepository.findByUserAndId(user, boardId);
        if (board == null) {
            throw new NotFoundBoardException();
        }
        if(Optional.ofNullable(board.getPhoto()).isPresent()){
            s3Service.deleteFile(board.getPhoto());
        }

        if(Optional.ofNullable(file).isPresent()){
            String url= "";
            try{
                url = s3Service.upload(file, "board");
                board.updatePhoto(url);
            }catch (IOException e){
                System.out.println("s3 등록 실패");
            }
        }
        board.modifyPosts(modifyDto.getTitle(), modifyDto.getContext());
    }

    @Override
    @Transactional
    public void deletePosts(String userEmail, String boardId) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new CustomJwtRuntimeException();
        }
        Board board = boardRepository.findByUserAndId(user, boardId);
        if (board == null) {
            throw new NotFoundBoardException();
        }

        if(Optional.ofNullable(board.getPhoto()).isPresent()){
            s3Service.deleteFile(board.getPhoto());
        }

        user.getBoardList().remove(board);
        boardRepository.deleteById(boardId);
    }

    @Override
    @Transactional
    public Page<ResponseBoard.getLikeBoardDto> getLikeBoard(String email, Pageable pageable){
        List<Board> boardList = new ArrayList<>();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundUserException();
        }
        List<Likes> likeList = likeRepository.findByUser(user);
        for(Likes item : likeList){
            boardList.add(item.getBoard());
        }
        Page<Board> response = new PageImpl<>(boardList);
        return response.map(ResponseBoard.getLikeBoardDto::of);
    }
}
