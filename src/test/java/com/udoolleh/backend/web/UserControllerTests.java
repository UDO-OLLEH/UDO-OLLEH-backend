package com.udoolleh.backend.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udoolleh.backend.entity.User;
import com.udoolleh.backend.provider.security.JwtAuthTokenProvider;
import com.udoolleh.backend.provider.service.UserService;
import com.udoolleh.backend.repository.UserRepository;
import com.udoolleh.backend.utils.SHA256Util;
import com.udoolleh.backend.web.dto.RequestUser;
import com.udoolleh.backend.web.dto.ResponseUser;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.coyote.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
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
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtAuthTokenProvider jwtAuthTokenProvider;

    private ResponseUser.Token token;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

        token = ResponseUser.Token.builder()
                .accessToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZSI6IlJPTEVfVVNFUiIsImV4cCI6MTY3MTc3NDI2NH0.b-02-QeknnbtWV1lrtOdXEYD9xYLLIQ3G0vIy_U8_-8")
                .refreshToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZSI6IlJPTEVfVVNFUiIsImV4cCI6MTcwMzMxMDE0NH0.hR7O2VmeX2NAfgGcxdQxBy554m8ze3Va1p5D_VUh68g")
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
        doNothing().when(userService).register(dto);

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
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("패스워드")
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
        RequestUser.LoginDto loginDto = RequestUser.LoginDto.builder()
                .email("test")
                .password("password")
                .build();

        given(userService.login(loginDto)).willReturn(Optional.ofNullable(token));

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
    @Test
    void logoutUserTest() throws Exception{
        doNothing().when(userService).logout("test");

        mockMvc.perform(RestDocumentationRequestBuilders
                .post("/logout")
                .header("x-auth-token", token.getAccessToken())
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

    @DisplayName("액세스 토큰 갱신 성공 테스트 - 상태코드 : 200")
    @Transactional
    @Test
    void refreshTokenTest() throws Exception {
        ResponseUser.Token newToken = ResponseUser.Token.builder()
                .accessToken("New Access Token")
                .refreshToken(token.getRefreshToken())
                .build();
        given(userService.refreshToken(token.getRefreshToken()))
                .willReturn(Optional.ofNullable(newToken));

        Map<String, String> refreshToken = Map.of("refreshToken", token.getRefreshToken());
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
        RequestUser.UpdateUserDto updateUserDto = RequestUser.UpdateUserDto.builder()
                .nickname("newNickname")
                .password("password")
                .build();
        doNothing().when(userService).updateUser("test", updateUserDto);
        mockMvc.perform(RestDocumentationRequestBuilders
                .put("/user")
                .header("x-auth-token", token.getAccessToken())
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
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test2.png",
                "image/png", "test data".getBytes());
        doNothing().when(userService).uploadUserImage("test", mockMultipartFile);

        mockMvc.perform(RestDocumentationRequestBuilders
                .multipart("/user/image")
                .file(mockMultipartFile)
                .header("x-auth-token", token.getAccessToken())
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

    @DisplayName("유저 정보 조회 - 상태코드 : 200")
    @Transactional
    @Test
    void getUserInfoTest() throws Exception{
        ResponseUser.UserDto userDto = ResponseUser.UserDto.builder()
                .profileImage("https://www.google.com/url?sa=i&url=https%3A%2F%2Fpixabay.com%2Fko%2Fimages%2Fsearch%2Furl%2F&psig=AOvVaw2Kc0QFHfJJxEZtD_UxzWMD&ust=1671882640838000&source=images&cd=vfe&ved=0CBAQjRxqFwoTCLCV9ajWj_wCFQAAAAAdAAAAABAE")
                .nickname("우도 사랑")
                .build();

        given(userService.getUserInfo(any())).willReturn(userDto);

        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/user")
                .header("x-auth-token", token.getAccessToken())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("user-get", // 문서 조각 디렉토리 명
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
                                        fieldWithPath("list.nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("list.profileImage").type(JsonFieldType.STRING).description("프로필 이미지 url")
                                )
                        )
                )
        ;
    }


}
