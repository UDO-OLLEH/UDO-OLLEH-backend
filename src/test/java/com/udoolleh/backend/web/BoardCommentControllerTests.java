package com.udoolleh.backend.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udoolleh.backend.entity.Board;
import com.udoolleh.backend.entity.BoardComment;
import com.udoolleh.backend.entity.User;
import com.udoolleh.backend.exception.errors.LoginFailedException;
import com.udoolleh.backend.provider.service.UserService;
import com.udoolleh.backend.repository.BoardCommentRepository;
import com.udoolleh.backend.repository.BoardRepository;
import com.udoolleh.backend.repository.UserRepository;
import com.udoolleh.backend.utils.SHA256Util;
import com.udoolleh.backend.web.dto.RequestBoard;
import com.udoolleh.backend.web.dto.RequestBoardComment;
import com.udoolleh.backend.web.dto.RequestUser;
import com.udoolleh.backend.web.dto.ResponseUser;
import org.junit.jupiter.api.BeforeEach;
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
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import org.springframework.restdocs.payload.JsonFieldType;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@ExtendWith(RestDocumentationExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class BoardCommentControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardCommentRepository boardCommentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private User user;

    private Board board;

    private String accessToken;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

        String salt = SHA256Util.generateSalt();
        user = User.builder()
                .email("email")
                .nickname("nick")
                .password(SHA256Util.getEncrypt("1234", salt))
                .salt(salt)
                .profile("profile_url")
                .build();
        user = userRepository.save(user);

        ResponseUser.Token token = userService.login(RequestUser.LoginDto.builder()
                .email("email")
                .password("1234")
                .build()).orElseThrow(() -> new LoginFailedException());
        accessToken = token.getAccessToken();

        board = Board.builder()
                .title("제목")
                .context("내용")
                .user(user)
                .build();
        board = boardRepository.save(board);

    }

    @Test
    @Transactional
    void registerBoardCommentTest() throws Exception {
        RequestBoardComment.RegisterBoardCommentDto dto = RequestBoardComment.RegisterBoardCommentDto
                .builder()
                .boardId(board.getId())
                .context("context")
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders
                .post("/board/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .header("x-auth-token", accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo(document("boardComment-post",
                        preprocessRequest(modifyUris()
                                .scheme("http")
                                .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                .removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("x-auth-token").description("액세스 토큰")),
                        requestFields(
                                fieldWithPath("boardId").type(JsonFieldType.STRING).description("게시판 아이디"),
                                fieldWithPath("context").type(JsonFieldType.STRING).description("댓글 내용")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("api response 고유 아이디 값"),
                                fieldWithPath("dateTime").type(JsonFieldType.STRING).description("response 응답 시간"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("list").type(JsonFieldType.NULL).description("응답 데이터")
                        )
                ));
    }

    @Test
    @Transactional
    void updateBoardCommentTest() throws Exception {
        BoardComment comment = BoardComment.builder()
                .board(board)
                .user(user)
                .context("context")
                .build();
        comment = boardCommentRepository.save(comment);

        RequestBoardComment.UpdateBoardCommentDto dto = RequestBoardComment.UpdateBoardCommentDto
                .builder()
                .commentId(comment.getId())
                .context("newContextf")
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders
                .put("/board/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .header("x-auth-token", accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo(document("boardComment-put",
                        preprocessRequest(modifyUris()
                                .scheme("http")
                                .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                .removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("x-auth-token").description("액세스 토큰")),
                        requestFields(
                                fieldWithPath("commentId").type(JsonFieldType.STRING).description("댓글 아이디"),
                                fieldWithPath("context").type(JsonFieldType.STRING).description("댓글 수정할 내용")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("api response 고유 아이디 값"),
                                fieldWithPath("dateTime").type(JsonFieldType.STRING).description("response 응답 시간"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("list").type(JsonFieldType.NULL).description("응답 데이터")
                        )
                ));
    }

    @Test
    @Transactional
    void getBoardCommentTest() throws Exception {
        BoardComment comment = BoardComment.builder()
                .board(board)
                .user(user)
                .context("내용")
                .build();
        comment = boardCommentRepository.save(comment);

        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/board/{id}/comment", board.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-auth-token", accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo(document("boardComment-get",
                        preprocessRequest(modifyUris()
                                .scheme("http")
                                .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                .removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("x-auth-token").description("액세스 토큰")),
                        pathParameters(parameterWithName("id").description("게시판 아이디")),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("api response 고유 아이디 값"),
                                fieldWithPath("dateTime").type(JsonFieldType.STRING).description("response 응답 시간"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("list.[].id").type(JsonFieldType.STRING).description("댓글 아이디"),
                                fieldWithPath("list.[].context").type(JsonFieldType.STRING).description("댓글 내용"),
                                fieldWithPath("list.[].nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("list.[].email").type(JsonFieldType.STRING).description("유저 이메일"),
                                fieldWithPath("list.[].profile").type(JsonFieldType.STRING).description("유저 프로필"),
                                fieldWithPath("list.[].createAt").type(JsonFieldType.STRING).description("댓글 작성 시간")
                        )
                ));
    }

    @Test
    @Transactional
    void deleteBoardCommentTest() throws Exception {
        BoardComment comment = BoardComment.builder()
                .board(board)
                .user(user)
                .context("내용")
                .build();
        comment = boardCommentRepository.save(comment);

        mockMvc.perform(RestDocumentationRequestBuilders
                .delete("/board/comment/{id}", comment.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-auth-token", accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo(document("boardComment-delete",
                        preprocessRequest(modifyUris()
                                .scheme("http")
                                .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                .removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("x-auth-token").description("액세스 토큰")),
                        pathParameters(parameterWithName("id").description("댓글 아이디")),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("api response 고유 아이디 값"),
                                fieldWithPath("dateTime").type(JsonFieldType.STRING).description("response 응답 시간"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("list").type(JsonFieldType.NULL).description("응답 데이터")
                        )
                ));
    }
}
