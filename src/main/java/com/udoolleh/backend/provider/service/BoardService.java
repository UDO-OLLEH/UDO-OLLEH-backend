package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.service.BoardServiceInterface;
import com.udoolleh.backend.entity.Board;
import com.udoolleh.backend.entity.Likes;
import com.udoolleh.backend.entity.User;
import com.udoolleh.backend.exception.errors.CustomJwtRuntimeException;
import com.udoolleh.backend.exception.errors.LikesDuplicatedException;
import com.udoolleh.backend.exception.errors.NotFoundBoardException;
import com.udoolleh.backend.exception.errors.NotFoundUserException;
import com.udoolleh.backend.exception.errors.NotFoundLikesException;

import com.udoolleh.backend.repository.BoardRepository;
import com.udoolleh.backend.repository.LikesRepository;
import com.udoolleh.backend.repository.UserRepository;

import com.udoolleh.backend.web.dto.RequestBoard;
import com.udoolleh.backend.web.dto.ResponseBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;
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

    private final LikesRepository likesRepository;


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
    public ResponseBoard.detailBoardDto boardDetail(String userEmail, String id) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new CustomJwtRuntimeException();
        }
        Optional<Board> optionalBoard = boardRepository.findById(id);
        Board board = optionalBoard.orElseThrow(() -> new NotFoundBoardException());

        return ResponseBoard.detailBoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .context(board.getContext())
                .photo(board.getPhoto())
                .createAt(board.getCreateAt())
                .nickname(board.getUser().getNickname())
                .countLikes(board.getCountLikes())
                .build();
    }

    @Override
    @Transactional
    public void updateVisit(String userEmail, String id) {

        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new CustomJwtRuntimeException();
        }
        Board board = boardRepository.findById(id).get();
        if (board == null) {
            throw new NotFoundBoardException();
        }

        long countVisit = board.getCountVisit() + 1;
        board.updateVisit(countVisit);

    }

    //게시글 등록 API
    @Override
    @Transactional
    public void registerPosts(MultipartFile file, String userEmail, RequestBoard.registerDto postDto) { //postDto = title, context + category 추후 추가
        User user = userRepository.findByEmail(userEmail);

        if (user == null) {
            throw new CustomJwtRuntimeException();
        }

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
    public void modifyPosts(MultipartFile file, String userEmail, String id, RequestBoard.updatesDto modifyDto) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new CustomJwtRuntimeException();
        }
        Board board = boardRepository.findByUserAndId(user, id);
        if (board == null) {
            throw new NotFoundBoardException();
        }
        if (Optional.ofNullable(board.getPhoto()).isPresent()) {
            s3Service.deleteFile(board.getPhoto());
        }

        if (Optional.ofNullable(file).isPresent()) {
            String url = "";
            try {
                url = s3Service.upload(file, "board");
                board.updatePhoto(url);
            } catch (IOException e) {
                System.out.println("s3 등록 실패");
            }
        }
        board.modifyPosts(modifyDto.getTitle(), modifyDto.getContext());
    }

    @Override
    @Transactional
    public void deletePosts(String userEmail, String id) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new CustomJwtRuntimeException();
        }
        Board board = boardRepository.findByUserAndId(user, id);
        if (board == null) {
            throw new NotFoundBoardException();
        }

        if (Optional.ofNullable(board.getPhoto()).isPresent()) {
            s3Service.deleteFile(board.getPhoto());
        }

        user.getBoardList().remove(board);
        boardRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateLikes(String userEmail, String id) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new CustomJwtRuntimeException();
        }
        Board board = boardRepository.findById(id).get();
        if (board == null) {
            throw new NotFoundBoardException();
        }
        likesRepository.findByUserAndBoard(user, board).ifPresent(duplicate -> {
            throw new LikesDuplicatedException();
        });

        long countLikes = board.getCountLikes() + 1;
        board.updateLikes(countLikes);

        likesRepository.save(
                Likes.builder()
                        .user(user)
                        .board(board)
                        .build());
    }

    @Override
    @Transactional
    public void deleteLikes(String userEmail, String likesId, String id) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new CustomJwtRuntimeException();
        }
        Board board = boardRepository.findById(id).get();
        if (board == null) {
            throw new NotFoundBoardException();
        }
        likesRepository.findByUserAndBoard(user, board).orElseThrow(
                () -> new NotFoundLikesException());

        long countLikes = board.getCountLikes() - 1;
        board.updateLikes(countLikes);
        likesRepository.deleteById(likesId);
       }

    @Override
    @Transactional
    public Page<ResponseBoard.listBoardDto> getMyBoard(String email, Pageable pageable){
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new CustomJwtRuntimeException();
        }
        Page<Board> boardList = boardRepository.findByUser(user, pageable);

        return boardList.map(ResponseBoard.listBoardDto::of);
    }
    
    @Override
    @Transactional
    public Page<ResponseBoard.getLikeBoardDto> getLikeBoard(String email, Pageable pageable){
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new CustomJwtRuntimeException();
        }

        Page<Board> response = boardRepository.findLikeBoard(user, pageable);
        return response.map(ResponseBoard.getLikeBoardDto::of);
    }
}
