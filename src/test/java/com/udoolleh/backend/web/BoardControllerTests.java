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
import com.udoolleh.backend.web.dto.ResponseAds;
import com.udoolleh.backend.web.dto.ResponseBoard;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
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

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
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

        doNothing().when(boardService).updateBoard(mockMultipartfile, "email", "id", RequestBoard.UpdateBoardDto.builder().build());

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

        doNothing().when(boardService).deleteBoard("email", "id");

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

    @DisplayName("게시판 좋아요 테스트")
    @Test
    void likeBoardTest() throws Exception {
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZSI6IlJPTEVfVVNFUiIsImV4cCI6MTY3MTc3NDI2NH0.b-02-QeknnbtWV1lrtOdXEYD9xYLLIQ3G0vIy_U8_-8";

        doNothing().when(boardService).updateLikes("email", "id");

        mockMvc.perform(RestDocumentationRequestBuilders
                .post("/board/{id}/likes", "id")
                .header("x-auth-token", accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("board-like-post", // 문서 조각 디렉토리 명
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

    @DisplayName("게시판 좋아요 취소 테스트")
    @Test
    void deleteLikeBoardTest() throws Exception {
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZSI6IlJPTEVfVVNFUiIsImV4cCI6MTY3MTc3NDI2NH0.b-02-QeknnbtWV1lrtOdXEYD9xYLLIQ3G0vIy_U8_-8";

        doNothing().when(boardService).deleteLikes("email", "likeId", "id");

        mockMvc.perform(RestDocumentationRequestBuilders
                .delete("/board/{id}/likes/{likesId}", "id", "likesId")
                .header("x-auth-token", accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("board-like-delete", // 문서 조각 디렉토리 명
                                preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                        .removePort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("게시판 아이디"),
                                        parameterWithName("likesId").description("좋아요 아이디")),
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

    @Test
    void getBoardListTest() throws Exception {
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZSI6IlJPTEVfVVNFUiIsImV4cCI6MTY3MTc3NDI2NH0.b-02-QeknnbtWV1lrtOdXEYD9xYLLIQ3G0vIy_U8_-8";

        ResponseBoard.BoardListDto boardListDto = ResponseBoard.BoardListDto.builder()
                .id("id")
                .email("email")
                .title("title")
                .context("context")
                .createAt(new Date())
                .countVisit(10L)
                .countLikes(1L)
                .build();
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createAt");
        List<ResponseBoard.BoardListDto> boardListDtos = List.of(boardListDto);
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), boardListDtos.size());
        final Page<ResponseBoard.BoardListDto> boardListDtoPage = new PageImpl<>(boardListDtos.subList(start, end), pageable, boardListDtos.size());

        given(boardService.getBoardList(any(), any())).willReturn(boardListDtoPage);

        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/board/list")
                .header("x-auth-token", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("board-get", // 문서 조각 디렉토리 명
                                preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                        .removePort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("x-auth-token").description("액세스 토큰")),
                                responseFields( // response 필드 정보 입력
                                        fieldWithPath("id").type(JsonFieldType.STRING).description("응답 아이디"),
                                        fieldWithPath("dateTime").type(JsonFieldType.STRING).description("응답 시간"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
                                        fieldWithPath("list.content.[].id").type(JsonFieldType.STRING).description("게시판 아이디"),
                                        fieldWithPath("list.content.[].email").type(JsonFieldType.STRING).description("작성자 아이디"),
                                        fieldWithPath("list.content.[].title").type(JsonFieldType.STRING).description("게시판 제목"),
                                        fieldWithPath("list.content.[].context").type(JsonFieldType.STRING).description("게시판 내용"),
                                        fieldWithPath("list.content.[].createAt").type(JsonFieldType.STRING).description("게시판 작성 일자"),
                                        fieldWithPath("list.content.[].countVisit").type(JsonFieldType.NUMBER).description("게시판 조회수"),
                                        fieldWithPath("list.content.[].countLikes").type(JsonFieldType.NUMBER).description("게시판 좋아요 수"),

                                        fieldWithPath("list.pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 비었는지 여부"),
                                        fieldWithPath("list.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
                                        fieldWithPath("list.pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
                                        fieldWithPath("list.pageable.offset").type(JsonFieldType.NUMBER).description("오프셋"),
                                        fieldWithPath("list.pageable.pageNumber").type(JsonFieldType.NUMBER).description("페이지 번호"),
                                        fieldWithPath("list.pageable.pageSize").type(JsonFieldType.NUMBER).description("페이지 크기"),
                                        fieldWithPath("list.pageable.paged").type(JsonFieldType.BOOLEAN).description("페이지 여부"),
                                        fieldWithPath("list.pageable.unpaged").type(JsonFieldType.BOOLEAN).description("페이지 여부"),

                                        fieldWithPath("list.last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                                        fieldWithPath("list.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 개수"),
                                        fieldWithPath("list.totalElements").type(JsonFieldType.NUMBER).description("전체 요소 개수"),
                                        fieldWithPath("list.size").type(JsonFieldType.NUMBER).description("한 페이지 당 보여지는 요소 개수"),
                                        fieldWithPath("list.number").type(JsonFieldType.NUMBER).description("요소 개수"),
                                        fieldWithPath("list.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 비었는지 여부"),
                                        fieldWithPath("list.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
                                        fieldWithPath("list.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
                                        fieldWithPath("list.first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
                                        fieldWithPath("list.numberOfElements").type(JsonFieldType.NUMBER).description("요소 개수"),
                                        fieldWithPath("list.empty").type(JsonFieldType.BOOLEAN).description("리스트가 비어있는지 여부")
                                )
                        )
                )
        ;
    }

    @Test
    void getBoardTest() throws Exception {
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZSI6IlJPTEVfVVNFUiIsImV4cCI6MTY3MTc3NDI2NH0.b-02-QeknnbtWV1lrtOdXEYD9xYLLIQ3G0vIy_U8_-8";

        ResponseBoard.BoardDto boardDto = ResponseBoard.BoardDto.builder()
                .id("id")
                .email("email")
                .title("title")
                .context("context")
                .photo("photo_url")
                .createAt(new Date())
                .nickname("nickname")
                .countLikes(1L)
                .hashtag("hastag")
                .build();

        given(boardService.getBoardDetail(any(), any())).willReturn(boardDto);

        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/board/{id}", "id")
                .header("x-auth-token", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("board-detail-get", // 문서 조각 디렉토리 명
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
                                        fieldWithPath("list.id").type(JsonFieldType.STRING).description("게시판 아이디"),
                                        fieldWithPath("list.email").type(JsonFieldType.STRING).description("작성자 아이디"),
                                        fieldWithPath("list.title").type(JsonFieldType.STRING).description("게시판 제목"),
                                        fieldWithPath("list..context").type(JsonFieldType.STRING).description("게시판 내용"),
                                        fieldWithPath("list.photo").type(JsonFieldType.STRING).description("사진 url"),
                                        fieldWithPath("list.createAt").type(JsonFieldType.STRING).description("게시판 작성 일자"),
                                        fieldWithPath("list.nickname").type(JsonFieldType.STRING).description("게시판 작성자 닉네임"),
                                        fieldWithPath("list.countLikes").type(JsonFieldType.NUMBER).description("게시판 좋아요 수"),
                                        fieldWithPath("list.hashtag").type(JsonFieldType.STRING).description("게시판 해시태그")
                                )
                        )
                )
        ;
    }

    @Test
    void getMyBoardsTest() throws Exception {
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZSI6IlJPTEVfVVNFUiIsImV4cCI6MTY3MTc3NDI2NH0.b-02-QeknnbtWV1lrtOdXEYD9xYLLIQ3G0vIy_U8_-8";

        ResponseBoard.BoardListDto boardListDto = ResponseBoard.BoardListDto.builder()
                .id("id")
                .email("email")
                .title("title")
                .context("context")
                .createAt(new Date())
                .countVisit(10L)
                .countLikes(1L)
                .build();
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createAt");
        List<ResponseBoard.BoardListDto> boardListDtos = List.of(boardListDto);
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), boardListDtos.size());
        final Page<ResponseBoard.BoardListDto> boardListDtoPage = new PageImpl<>(boardListDtos.subList(start, end), pageable, boardListDtos.size());

        given(boardService.getMyBoard(any(), any())).willReturn(boardListDtoPage);

        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/user/board")
                .header("x-auth-token", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("board-user-get", // 문서 조각 디렉토리 명
                                preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                        .removePort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("x-auth-token").description("액세스 토큰")),
                                responseFields( // response 필드 정보 입력
                                        fieldWithPath("id").type(JsonFieldType.STRING).description("응답 아이디"),
                                        fieldWithPath("dateTime").type(JsonFieldType.STRING).description("응답 시간"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
                                        fieldWithPath("list.content.[].id").type(JsonFieldType.STRING).description("게시판 아이디"),
                                        fieldWithPath("list.content.[].email").type(JsonFieldType.STRING).description("작성자 아이디"),
                                        fieldWithPath("list.content.[].title").type(JsonFieldType.STRING).description("게시판 제목"),
                                        fieldWithPath("list.content.[].context").type(JsonFieldType.STRING).description("게시판 내용"),
                                        fieldWithPath("list.content.[].createAt").type(JsonFieldType.STRING).description("게시판 작성 일자"),
                                        fieldWithPath("list.content.[].countVisit").type(JsonFieldType.NUMBER).description("게시판 조회수"),
                                        fieldWithPath("list.content.[].countLikes").type(JsonFieldType.NUMBER).description("게시판 좋아요 수"),

                                        fieldWithPath("list.pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 비었는지 여부"),
                                        fieldWithPath("list.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
                                        fieldWithPath("list.pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
                                        fieldWithPath("list.pageable.offset").type(JsonFieldType.NUMBER).description("오프셋"),
                                        fieldWithPath("list.pageable.pageNumber").type(JsonFieldType.NUMBER).description("페이지 번호"),
                                        fieldWithPath("list.pageable.pageSize").type(JsonFieldType.NUMBER).description("페이지 크기"),
                                        fieldWithPath("list.pageable.paged").type(JsonFieldType.BOOLEAN).description("페이지 여부"),
                                        fieldWithPath("list.pageable.unpaged").type(JsonFieldType.BOOLEAN).description("페이지 여부"),

                                        fieldWithPath("list.last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                                        fieldWithPath("list.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 개수"),
                                        fieldWithPath("list.totalElements").type(JsonFieldType.NUMBER).description("전체 요소 개수"),
                                        fieldWithPath("list.size").type(JsonFieldType.NUMBER).description("한 페이지 당 보여지는 요소 개수"),
                                        fieldWithPath("list.number").type(JsonFieldType.NUMBER).description("요소 개수"),
                                        fieldWithPath("list.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 비었는지 여부"),
                                        fieldWithPath("list.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
                                        fieldWithPath("list.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
                                        fieldWithPath("list.first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
                                        fieldWithPath("list.numberOfElements").type(JsonFieldType.NUMBER).description("요소 개수"),
                                        fieldWithPath("list.empty").type(JsonFieldType.BOOLEAN).description("리스트가 비어있는지 여부")
                                )
                        )
                )
        ;
    }

    @Test
    void getLikeBoardsTest() throws Exception {
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZSI6IlJPTEVfVVNFUiIsImV4cCI6MTY3MTc3NDI2NH0.b-02-QeknnbtWV1lrtOdXEYD9xYLLIQ3G0vIy_U8_-8";

        ResponseBoard.LikeBoardDto boardListDto = ResponseBoard.LikeBoardDto.builder()
                .id("id")
                .email("email")
                .title("title")
                .context("context")
                .createAt(new Date())
                .build();
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createAt");
        List<ResponseBoard.LikeBoardDto> boardListDtos = List.of(boardListDto);
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), boardListDtos.size());
        final Page<ResponseBoard.LikeBoardDto> boardListDtoPage = new PageImpl<>(boardListDtos.subList(start, end), pageable, boardListDtos.size());

        given(boardService.getLikeBoard(any(), any())).willReturn(boardListDtoPage);

        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/board/like")
                .header("x-auth-token", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("board-like-get", // 문서 조각 디렉토리 명
                                preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                        .removePort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("x-auth-token").description("액세스 토큰")),
                                responseFields( // response 필드 정보 입력
                                        fieldWithPath("id").type(JsonFieldType.STRING).description("응답 아이디"),
                                        fieldWithPath("dateTime").type(JsonFieldType.STRING).description("응답 시간"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
                                        fieldWithPath("list.content.[].id").type(JsonFieldType.STRING).description("게시판 아이디"),
                                        fieldWithPath("list.content.[].email").type(JsonFieldType.STRING).description("작성자 아이디"),
                                        fieldWithPath("list.content.[].title").type(JsonFieldType.STRING).description("게시판 제목"),
                                        fieldWithPath("list.content.[].context").type(JsonFieldType.STRING).description("게시판 내용"),
                                        fieldWithPath("list.content.[].createAt").type(JsonFieldType.STRING).description("게시판 작성 일자"),

                                        fieldWithPath("list.pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 비었는지 여부"),
                                        fieldWithPath("list.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
                                        fieldWithPath("list.pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
                                        fieldWithPath("list.pageable.offset").type(JsonFieldType.NUMBER).description("오프셋"),
                                        fieldWithPath("list.pageable.pageNumber").type(JsonFieldType.NUMBER).description("페이지 번호"),
                                        fieldWithPath("list.pageable.pageSize").type(JsonFieldType.NUMBER).description("페이지 크기"),
                                        fieldWithPath("list.pageable.paged").type(JsonFieldType.BOOLEAN).description("페이지 여부"),
                                        fieldWithPath("list.pageable.unpaged").type(JsonFieldType.BOOLEAN).description("페이지 여부"),

                                        fieldWithPath("list.last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                                        fieldWithPath("list.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 개수"),
                                        fieldWithPath("list.totalElements").type(JsonFieldType.NUMBER).description("전체 요소 개수"),
                                        fieldWithPath("list.size").type(JsonFieldType.NUMBER).description("한 페이지 당 보여지는 요소 개수"),
                                        fieldWithPath("list.number").type(JsonFieldType.NUMBER).description("요소 개수"),
                                        fieldWithPath("list.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 비었는지 여부"),
                                        fieldWithPath("list.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
                                        fieldWithPath("list.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
                                        fieldWithPath("list.first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
                                        fieldWithPath("list.numberOfElements").type(JsonFieldType.NUMBER).description("요소 개수"),
                                        fieldWithPath("list.empty").type(JsonFieldType.BOOLEAN).description("리스트가 비어있는지 여부")
                                )
                        )
                )
        ;
    }
}
