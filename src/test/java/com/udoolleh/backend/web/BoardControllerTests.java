package com.udoolleh.backend.web;

import com.udoolleh.backend.entity.User;
import com.udoolleh.backend.exception.CustomException;
import com.udoolleh.backend.exception.ErrorCode;
import com.udoolleh.backend.provider.security.JwtAuthTokenProvider;
import com.udoolleh.backend.provider.service.BoardService;
import com.udoolleh.backend.provider.service.UserService;
import com.udoolleh.backend.repository.BoardRepository;
import com.udoolleh.backend.repository.UserRepository;
import com.udoolleh.backend.utils.SHA256Util;
import com.udoolleh.backend.web.dto.RequestBoard;
import com.udoolleh.backend.web.dto.RequestUser;
import com.udoolleh.backend.web.dto.ResponseUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.requestPartFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

@ExtendWith(RestDocumentationExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class BoardControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoardService boardService;

    @MockBean
    private JwtAuthTokenProvider jwtAuthTokenProvider;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("게시판 작성 성공 테스트 - 상태코드 : 200")
    @Test
    void registerBoardTest() throws Exception {
        MockMultipartFile mockMultipartfile = new MockMultipartFile("file", "test2.png",
                "image/png", "test data".getBytes());
        MockMultipartFile requestDto = new MockMultipartFile("requestDto", "",
                "application/json", "{\"title\": \"board title\",\"hashtag\": \"hashtag\",\"context\": \"context\"}".getBytes());

        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZSI6IlJPTEVfVVNFUiIsImV4cCI6MTY3MTc3NDI2NH0.b-02-QeknnbtWV1lrtOdXEYD9xYLLIQ3G0vIy_U8_-8";

        doNothing().when(boardService).registerBoard(mockMultipartfile, "email", RequestBoard.RegisterBoardDto.builder().build());

        mockMvc.perform(RestDocumentationRequestBuilders
                .multipart("/board")
                .file(mockMultipartfile)
                .file(requestDto)
                .header("x-auth-token", accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("board-post", // 문서 조각 디렉토리 명
                                preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                        .removePort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),

                                requestParts(partWithName("file").description("사진 파일"),
                                        partWithName("requestDto").description("title, hashtag, context")),
                                requestPartFields(
                                        "requestDto",
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("게시판 제목"),
                                        fieldWithPath("hashtag").type(JsonFieldType.STRING).description("해시태그"),
                                        fieldWithPath("context").type(JsonFieldType.STRING).description("게시판 내용")
                                ),
                                requestHeaders(
                                        headerWithName("x-auth-token").description("액세스 토큰")),
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

    @DisplayName("게시판 수정 테스트")
    @Test
    void updateBoardTest() throws Exception {
        MockMultipartFile mockMultipartfile = new MockMultipartFile("file", "test2.png",
                "image/png", "test data".getBytes());
        MockMultipartFile updateBoardDto = new MockMultipartFile("updateBoardDto", "",
                "application/json", "{\"title\": \"board title\",\"hashtag\": \"hashtag\",\"context\": \"context\"}".getBytes());

        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZSI6IlJPTEVfVVNFUiIsImV4cCI6MTY3MTc3NDI2NH0.b-02-QeknnbtWV1lrtOdXEYD9xYLLIQ3G0vIy_U8_-8";

        doNothing().when(boardService).updateBoard(mockMultipartfile, "email","id", RequestBoard.UpdateBoardDto.builder().build());

        mockMvc.perform(RestDocumentationRequestBuilders
                .multipart("/board/{id}", "id")
                .file(mockMultipartfile)
                .file(updateBoardDto)
                .header("x-auth-token", accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("board-update-post", // 문서 조각 디렉토리 명
                                preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                        .removePort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("게시판 아이디")),
                                requestParts(partWithName("file").description("사진 파일"),
                                        partWithName("updateBoardDto").description("title, hashtag, context")),
                                requestPartFields(
                                        "updateBoardDto",
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("게시판 제목"),
                                        fieldWithPath("hashtag").type(JsonFieldType.STRING).description("해시태그"),
                                        fieldWithPath("context").type(JsonFieldType.STRING).description("게시판 내용")
                                ),
                                requestHeaders(
                                        headerWithName("x-auth-token").description("액세스 토큰")),
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


    @DisplayName("게시판 삭제 테스트")
    @Test
    void deleteBoardTest() throws Exception {
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZSI6IlJPTEVfVVNFUiIsImV4cCI6MTY3MTc3NDI2NH0.b-02-QeknnbtWV1lrtOdXEYD9xYLLIQ3G0vIy_U8_-8";

        doNothing().when(boardService).deleteBoard("email","id");

        mockMvc.perform(RestDocumentationRequestBuilders
                .delete("/board/{id}", "id")
                .header("x-auth-token", accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("board-delete", // 문서 조각 디렉토리 명
                                preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                        .removePort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("게시판 아이디")),
                                requestHeaders(
                                        headerWithName("x-auth-token").description("액세스 토큰")),
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
