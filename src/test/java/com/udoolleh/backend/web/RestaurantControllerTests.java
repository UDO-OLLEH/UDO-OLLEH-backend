package com.udoolleh.backend.web;

import com.udoolleh.backend.core.type.PlaceType;
import com.udoolleh.backend.entity.Restaurant;
import com.udoolleh.backend.provider.service.RestaurantService;
import com.udoolleh.backend.repository.RestaurantRepository;
import com.udoolleh.backend.web.dto.RequestRestaurant;
import com.udoolleh.backend.web.dto.ResponseRestaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(RestDocumentationExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class RestaurantControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestaurantRepository restaurantRepository;

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

    @Test
    void getRestaurantTest() throws Exception {
        //given
        Restaurant restaurant = Restaurant.builder()
                .name("식당 이름")
                .category("카테고리")
                .address("주소")
                .placeType(PlaceType.RESTAURANT)
                .yCoordinate("123.4")
                .xCoordinate("123.4")
                .totalGrade(0.0)
                .build();
        restaurantRepository.save(restaurant);
        String page = "0";
        //when
        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/restaurant")
                .param("page", page)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("restaurant-get", // 문서 조각 디렉토리 명
                                preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                        .removePort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestParameters(parameterWithName("page").description("페이지")),
                                responseFields( // response 필드 정보 입력
                                        fieldWithPath("id").type(JsonFieldType.STRING).description("응답 아이디"),
                                        fieldWithPath("dateTime").type(JsonFieldType.STRING).description("응답 시간"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
                                        fieldWithPath("list.[].id").type(JsonFieldType.STRING).description("아이디"),
                                        fieldWithPath("list.[].name").type(JsonFieldType.STRING).description("이름"),
                                        fieldWithPath("list.[].placeType").description("식당/카페"),
                                        fieldWithPath("list.[].category").type(JsonFieldType.STRING).description("카테고리"),
                                        fieldWithPath("list.[].address").type(JsonFieldType.STRING).description("주소"),
                                        fieldWithPath("list.[].imagesUrl").type(JsonFieldType.ARRAY).description("이미지 주소"),
                                        fieldWithPath("list.[].totalGrade").type(JsonFieldType.NUMBER).description("별점"),
                                        fieldWithPath("list.[].xcoordinate").type(JsonFieldType.STRING).description("x좌표"),
                                        fieldWithPath("list.[].ycoordinate").type(JsonFieldType.STRING).description("y좌표")
                                )
                        )
                )
        ;
    }
}
