package com.udoolleh.backend.web;

import com.udoolleh.backend.core.type.PlaceType;
import com.udoolleh.backend.entity.Menu;
import com.udoolleh.backend.entity.Restaurant;
import com.udoolleh.backend.provider.service.MenuService;
import com.udoolleh.backend.provider.service.RestaurantService;
import com.udoolleh.backend.repository.MenuRepository;
import com.udoolleh.backend.repository.RestaurantRepository;
import com.udoolleh.backend.web.dto.RequestRestaurant;
import com.udoolleh.backend.web.dto.ResponseRestaurant;
import javax.transaction.Transactional;
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
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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
    private MenuRepository menuRepository;

    @Autowired
    private MenuService menuService;

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

    @DisplayName("식당 페이징으로 조회 성공 - 200")
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
        restaurant = Restaurant.builder()
                .name("해녀식당")
                .category("카테고리")
                .address("20222")
                .placeType(PlaceType.RESTAURANT)
                .yCoordinate("123.45")
                .xCoordinate("123.45")
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
                                        fieldWithPath("list.content.[].id").type(JsonFieldType.STRING).description("아이디"),
                                        fieldWithPath("list.content.[].name").type(JsonFieldType.STRING).description("이름"),
                                        fieldWithPath("list.content.[].placeType").type(JsonFieldType.STRING).description("식당/카페"),
                                        fieldWithPath("list.content.[].category").type(JsonFieldType.STRING).description("카테고리"),
                                        fieldWithPath("list.content.[].address").type(JsonFieldType.STRING).description("주소"),
                                        fieldWithPath("list.content.[].imagesUrl").type(JsonFieldType.ARRAY).description("이미지 주소"),
                                        fieldWithPath("list.content.[].totalGrade").type(JsonFieldType.NUMBER).description("별점"),
                                        fieldWithPath("list.content.[].xcoordinate").type(JsonFieldType.STRING).description("x좌표"),
                                        fieldWithPath("list.content.[].ycoordinate").type(JsonFieldType.STRING).description("y좌표"),

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
    @DisplayName("식당 메뉴 조회 성공 - 200")
    @Transactional
    @Test
    void getRestaurantMenuTest() throws Exception {
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
        restaurant = restaurantRepository.save(restaurant);
        Menu menu = Menu.builder()
                .description("메뉴 설명")
                .restaurant(restaurant)
                .name("메뉴 이름")
                .price(12000)
                .build();
        menu = menuRepository.save(menu);
        restaurant.addMenu(menu);
        menu.updatePhoto("photoUrl");
        //when
        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/restaurant/{id}/menu", restaurant.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("restaurantMenu-get", // 문서 조각 디렉토리 명
                                preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                        .removePort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("맛집 아이디")),
                                responseFields( // response 필드 정보 입력
                                        fieldWithPath("id").type(JsonFieldType.STRING).description("응답 아이디"),
                                        fieldWithPath("dateTime").type(JsonFieldType.STRING).description("응답 시간"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
                                        fieldWithPath("list.[].name").type(JsonFieldType.STRING).description("메뉴 이름"),
                                        fieldWithPath("list.[].photo").type(JsonFieldType.STRING).description("메뉴 사진 url"),
                                        fieldWithPath("list.[].price").type(JsonFieldType.NUMBER).description("가격"),
                                        fieldWithPath("list.[].description").type(JsonFieldType.STRING).description("설명")
                                )
                        )
                )
        ;
    }


}
