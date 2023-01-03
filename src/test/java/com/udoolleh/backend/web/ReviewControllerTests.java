package com.udoolleh.backend.web;

import com.udoolleh.backend.entity.Ads;
import com.udoolleh.backend.entity.Restaurant;
import com.udoolleh.backend.entity.Review;
import com.udoolleh.backend.entity.User;
import com.udoolleh.backend.exception.errors.LoginFailedException;
import com.udoolleh.backend.provider.service.UserService;
import com.udoolleh.backend.repository.AdsRepository;
import com.udoolleh.backend.repository.RestaurantRepository;
import com.udoolleh.backend.repository.ReviewRepository;
import com.udoolleh.backend.repository.UserRepository;
import com.udoolleh.backend.utils.SHA256Util;
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
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Map;

@ExtendWith(RestDocumentationExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class ReviewControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private User user;

    private Restaurant restaurant;

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
                .build();
        user = userRepository.save(user);

        ResponseUser.Token token = userService.login(RequestUser.LoginDto.builder()
                .email("email")
                .password("1234")
                .build()).orElseThrow(() -> new LoginFailedException());
        accessToken = token.getAccessToken();

        restaurant = Restaurant.builder()
                .name("restaurant")
                .build();
        restaurant = restaurantRepository.save(restaurant);
    }

    @Test
    @Transactional
    void registerReviewTest() throws Exception {
        MockMultipartFile mockMultipartfile = new MockMultipartFile("file", "test2.png",
                "image/png", "test data".getBytes());
        MockMultipartFile requestDto = new MockMultipartFile("requestDto", "",
                "application/json", "{\"restaurantName\": \"restaurant\",\"context\": \"context\",\"grade\": 3.0}".getBytes());

        mockMvc.perform(RestDocumentationRequestBuilders
                .multipart("/restaurant/review")
                .file(mockMultipartfile)
                .file(requestDto)
                .header("x-auth-token", accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo(document("review-post",
                        preprocessRequest(modifyUris()
                                .scheme("http")
                                .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                .removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("x-auth-token").description("액세스 토큰")),
                        requestParts(partWithName("file").description("사진 파일"),
                                partWithName("requestDto").description("리뷰 정보")),
                        requestPartFields("requestDto",
                                fieldWithPath("restaurantName").type(JsonFieldType.STRING).description("맛집 이름"),
                                fieldWithPath("context").type(JsonFieldType.STRING).description("내용"),
                                fieldWithPath("grade").type(JsonFieldType.NUMBER).description("별점")
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
    void updateReviewTest() throws Exception {
        Review review = Review.builder()
                .context("context")
                .grade(3.0)
                .user(user)
                .restaurant(restaurant)
                .build();
        review = reviewRepository.save(review);

        MockMultipartFile mockMultipartfile = new MockMultipartFile("file", "test2.png",
                "image/png", "test data".getBytes());
        MockMultipartFile requestDto = new MockMultipartFile("requestDto", "",
                "application/json", "{\"context\": \"context\",\"grade\": 3.0}".getBytes());

        mockMvc.perform(RestDocumentationRequestBuilders
                .multipart("/restaurant/review/{id}", review.getId())
                .file(mockMultipartfile)
                .file(requestDto)
                .header("x-auth-token", accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo(document("review-update-post",
                        preprocessRequest(modifyUris()
                                .scheme("http")
                                .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                .removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("id").description("리뷰 아이디")),
                        requestHeaders(headerWithName("x-auth-token").description("액세스 토큰")),
                        requestParts(partWithName("file").description("사진 파일"),
                                partWithName("requestDto").description("리뷰 내용")),
                        requestPartFields("requestDto",
                                fieldWithPath("context").type(JsonFieldType.STRING).description("내용"),
                                fieldWithPath("grade").type(JsonFieldType.NUMBER).description("별점")
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
    void getReviewTest() throws Exception {
        //given
        Review review = Review.builder()
                .restaurant(restaurant)
                .context("hello")
                .user(user)
                .photo("photo_url")
                .build();
        review = reviewRepository.save(review);

        //when
        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/restaurant/{name}/review", "restaurant")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("review-get", // 문서 조각 디렉토리 명
                                preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                        .removePort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("name").description("맛집 아이디")),
                                responseFields( // response 필드 정보 입력
                                        fieldWithPath("id").type(JsonFieldType.STRING).description("응답 아이디"),
                                        fieldWithPath("dateTime").type(JsonFieldType.STRING).description("응답 시간"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
                                        fieldWithPath("list.[].reviewId").type(JsonFieldType.STRING).description("리뷰 아이디"),
                                        fieldWithPath("list.[].nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("list.[].email").type(JsonFieldType.STRING).description("유저 이메일"),
                                        fieldWithPath("list.[].context").type(JsonFieldType.STRING).description("내용"),
                                        fieldWithPath("list.[].photo").type(JsonFieldType.STRING).description("사진"),
                                        fieldWithPath("list.[].grade").type(JsonFieldType.NUMBER).description("평점")
                                )
                        )
                )
        ;
    }

    @Test
    @Transactional
    void deleteReviewTest() throws Exception {
        Review review = Review.builder()
                .restaurant(restaurant)
                .context("내용")
                .user(user)
                .photo("photo_url")
                .build();
        review = reviewRepository.save(review);

        mockMvc.perform(RestDocumentationRequestBuilders
                .delete("/restaurant/review/{id}", review.getId())
                .header("x-auth-token", accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo(document("review-delete",
                        preprocessRequest(modifyUris()
                                .scheme("http")
                                .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                .removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("x-auth-token").description("액세스 토큰")),
                        pathParameters(parameterWithName("id").description("리뷰 아이디")),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("api response 고유 아이디 값"),
                                fieldWithPath("dateTime").type(JsonFieldType.STRING).description("response 응답 시간"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("list").type(JsonFieldType.NULL).description("응답 데이터")
                        )
                ));
    }
}
