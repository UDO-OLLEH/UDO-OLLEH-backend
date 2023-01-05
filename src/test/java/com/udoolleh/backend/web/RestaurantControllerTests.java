package com.udoolleh.backend.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udoolleh.backend.core.type.PlaceType;
import com.udoolleh.backend.entity.Menu;
import com.udoolleh.backend.entity.Restaurant;
import com.udoolleh.backend.entity.Review;
import com.udoolleh.backend.provider.service.AdminAuthenticationService;
import com.udoolleh.backend.provider.service.MenuService;
import com.udoolleh.backend.provider.service.RestaurantService;
import com.udoolleh.backend.repository.MenuRepository;
import com.udoolleh.backend.repository.RestaurantRepository;
import com.udoolleh.backend.web.dto.RequestRestaurant;
import com.udoolleh.backend.web.dto.ResponseMenu;
import com.udoolleh.backend.web.dto.ResponseRestaurant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
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
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
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
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuService menuService;

    @MockBean
    private RestaurantService restaurantService;

    @MockBean
    private AdminAuthenticationService adminAuthenticationService;

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
        String page = "0";
        ResponseRestaurant.RestaurantDto restaurantDto = ResponseRestaurant.RestaurantDto.builder()
                .id("0333091e-a609-4f95-b74a-298015b8b53a")
                .address("제주특별자치도 제주시 우도면 우도해안길 252")
                .category("음식점>간식>아이스크림")
                .imagesUrl(List.of("http://img.tripnbuy.com/photo/instagram_standard_13722307_916099158535191_622597164_n.jpg", "https://www.menupan.com/restaurant/restimg/003/zzmenuimg/d6007032_z.jpg"))
                .placeType(PlaceType.RESTAURANT)
                .name("우도왕자이야기")
                .totalGrade(4.5)
                .xCoordinate("126.943723421797")
                .yCoordinate("33.5027129282042")
                .build();
        ResponseRestaurant.RestaurantDto restaurantCafeDto = ResponseRestaurant.RestaurantDto.builder()
                .id("0333091e-a609-4f95-b74a-298015b8b53a")
                .address("제주특별자치도 제주시 우도면 우도해안길 1132")
                .category("음식점 > 카페 > 생과일전문점 > 리치망고")
                .imagesUrl(List.of("http://img.tripnbuy.com/photo/instagram_standard_13722307_916099158535191_622597164_n.jpg", "https://www.menupan.com/restaurant/restimg/003/zzmenuimg/d6007032_z.jpg"))
                .placeType(PlaceType.CAFE)
                .name("리치망고 우도검멀레점")
                .totalGrade(4.5)
                .xCoordinate("126.967487736596")
                .yCoordinate("33.4977626192462")
                .build();
        //위 객체를 List로 만들고 Page로 변경
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "totalGrade");
        List<ResponseRestaurant.RestaurantDto> responseRestaurants = List.of(restaurantCafeDto, restaurantDto);
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), responseRestaurants.size());
        final Page<ResponseRestaurant.RestaurantDto> restaurantDtoPage = new PageImpl<>(responseRestaurants.subList(start, end), pageable, responseRestaurants.size());

        given(restaurantService.getRestaurant(any())).willReturn(restaurantDtoPage);


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
    @Test
    void getRestaurantMenuTest() throws Exception {
        //given
        List menus = new ArrayList<>();
        ResponseMenu.MenuDto menuDto = ResponseMenu.MenuDto.builder()
                .name("고등어")
                .description("노릇노릇하게 구운 고등어")
                .price(10000)
                .photo("http://img.tripnbuy.com/photo/instagram_standard_13722307_916099158535191_622597164_n.jpg")
                .build();
        menus.add(menuDto);
        menuDto = ResponseMenu.MenuDto.builder()
                .name("갈치")
                .description("부드러운 갈치 구이")
                .price(11000)
                .photo("https://www.menupan.com/restaurant/restimg/003/zzmenuimg/d6007032_z.jpg")
                .build();
        menus.add(menuDto);
        given(menuService.getMenu(any())).willReturn(menus);
        //when
        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/restaurant/{id}/menu", 1)
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

    @DisplayName("식당 등록 메뉴 등록 성공 - 200")
    @Test
    void registerRestaurantMenuTest() throws Exception {
        //given
        String adminAccessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        MockMultipartFile mockMultipartfile = new MockMultipartFile("file", "test2.png",
                "image/png", "test data".getBytes());
        MockMultipartFile requestDto = new MockMultipartFile("requestDto", "",
                "application/json", "{\"restaurantName\": \"해녀촌해산물\",\"name\": \"고등어\", \"price\": 10000, \"description\": \"노릇노릇 고등어 구이\"}".getBytes());

        doNothing().when(menuService).registerMenu(any(), any());
        given(adminAuthenticationService.validAdminToken(any())).willReturn(true);

        mockMvc.perform(RestDocumentationRequestBuilders
                .multipart("/restaurant/menu")
                .file(mockMultipartfile)
                .file(requestDto)
                .header("x-auth-token", adminAccessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo(document("restaurant-menu-post",
                        preprocessRequest(modifyUris()
                                .scheme("http")
                                .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                .removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("x-auth-token").description("어드민 액세스 토큰")),
                        requestParts(partWithName("file").description("사진 파일"),
                                partWithName("requestDto").description("메뉴 등록 내용")),
                        requestPartFields("requestDto",
                                fieldWithPath("restaurantName").type(JsonFieldType.STRING).description("내용"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("메뉴 이름"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("메뉴 가격"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("메뉴 설명")

                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("api response 고유 아이디 값"),
                                fieldWithPath("dateTime").type(JsonFieldType.STRING).description("response 응답 시간"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("list").type(JsonFieldType.NULL).description("응답 데이터")
                        )
                ));
    }
    @DisplayName("식당 사진들 등록 성공 - 200")
    @Test
    void registerRestaurantImagesTest() throws Exception {
        //given
        String adminAccessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        MockMultipartFile mockMultipartfile = new MockMultipartFile("images", "test2.png",
                "image/png", "test data".getBytes());
        MockMultipartFile restaurantName = new MockMultipartFile("restaurantName", "",
                "application/json", "{\"restaurantName\": \"해녀촌해산물\"}".getBytes());

        doNothing().when(restaurantService).registerRestaurantImage(any(),any());
        given(adminAuthenticationService.validAdminToken(any())).willReturn(true);

        mockMvc.perform(RestDocumentationRequestBuilders
                .multipart("/restaurant/images")
                .file(mockMultipartfile)
                .file(mockMultipartfile)
                .file(restaurantName)
                .header("x-auth-token", adminAccessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo(document("restaurant-images-post",
                        preprocessRequest(modifyUris()
                                .scheme("http")
                                .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                .removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("x-auth-token").description("어드민 액세스 토큰")),
                        requestParts(partWithName("images").description("사진 파일들"),
                                partWithName("restaurantName").description("식당 이름")),
                        requestPartFields("restaurantName",
                                fieldWithPath("restaurantName").type(JsonFieldType.STRING).description("식당 이름")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("api response 고유 아이디 값"),
                                fieldWithPath("dateTime").type(JsonFieldType.STRING).description("response 응답 시간"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("list").type(JsonFieldType.NULL).description("응답 데이터")
                        )
                ));
    }

    @DisplayName("이미지를 제외한 맛집 등록 성공 - 200")
    @Test
    void registerRestaurantTest() throws Exception {
        String adminAccessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        RequestRestaurant.RegisterRestaurantDto restaurantDto = RequestRestaurant.RegisterRestaurantDto.builder()
                .address("제주특별자치도 제주시 우도면 우도해안길 86")
                .placeType(PlaceType.RESTAURANT)
                .category("음식점 > 한식 > 해물,생선")
                .name("해녀촌해산물")
                .build();

        doNothing().when(restaurantService).registerRestaurant(any());
        given(adminAuthenticationService.validAdminToken(any())).willReturn(true);

        mockMvc.perform(RestDocumentationRequestBuilders
                .post("/restaurant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(restaurantDto))
                .header("x-auth-token", adminAccessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo(document("restaurant-post",
                        preprocessRequest(modifyUris()
                                .scheme("http")
                                .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                .removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("x-auth-token").description("어드민 액세스 토큰")),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("식당 이름"),
                                fieldWithPath("placeType").type(JsonFieldType.STRING).description("RESTAURANT, CAFE (식당 타입)"),
                                fieldWithPath("category").type(JsonFieldType.STRING).description("카테고리"),
                                fieldWithPath("address").type(JsonFieldType.STRING).description("주소")

                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("api response 고유 아이디 값"),
                                fieldWithPath("dateTime").type(JsonFieldType.STRING).description("response 응답 시간"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("list").type(JsonFieldType.NULL).description("응답 데이터")
                        )
                ));
    }
    @DisplayName("식당 이미지 삭제 성공 - 200")
    @Test
    void deleteRestaurantImagesTest() throws Exception {
        String adminAccessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        doNothing().when(restaurantService).deleteRestaurantImage(any());
        given(adminAuthenticationService.validAdminToken(any())).willReturn(true);

        mockMvc.perform(RestDocumentationRequestBuilders
                .delete("/restaurant/{id}/images", 1)
                .header("x-auth-token", adminAccessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo(document("restaurant-images-delete",
                        preprocessRequest(modifyUris()
                                .scheme("http")
                                .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                .removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("x-auth-token").description("어드민 액세스 토큰")),
                        pathParameters(parameterWithName("id").description("식당 아이디")),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("api response 고유 아이디 값"),
                                fieldWithPath("dateTime").type(JsonFieldType.STRING).description("response 응답 시간"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("list").type(JsonFieldType.NULL).description("응답 데이터")
                        )
                ));
    }

    @DisplayName("메뉴 삭제 성공 - 200")
    @Test
    void deleteRestaurantMenuTest() throws Exception {
        String adminAccessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        doNothing().when(menuService).deleteMenu(any(), any());
        given(adminAuthenticationService.validAdminToken(any())).willReturn(true);

        mockMvc.perform(RestDocumentationRequestBuilders
                .delete("/restaurant/{id}/menu/{name}", 1,"고등어")
                .header("x-auth-token", adminAccessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo(document("restaurant-menu-delete",
                        preprocessRequest(modifyUris()
                                .scheme("http")
                                .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                .removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("x-auth-token").description("어드민 액세스 토큰")),
                        pathParameters(parameterWithName("id").description("식당 아이디"),
                                parameterWithName("name").description("메뉴 이름")),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("api response 고유 아이디 값"),
                                fieldWithPath("dateTime").type(JsonFieldType.STRING).description("response 응답 시간"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("list").type(JsonFieldType.NULL).description("응답 데이터")
                        )
                ));
    }


}
