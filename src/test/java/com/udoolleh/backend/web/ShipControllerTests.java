package com.udoolleh.backend.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udoolleh.backend.provider.service.AdminAuthenticationService;
import com.udoolleh.backend.provider.service.ShipService;
import com.udoolleh.backend.web.dto.RequestHarborTimetable;
import com.udoolleh.backend.web.dto.RequestShipFare;
import com.udoolleh.backend.web.dto.ResponseHarbor;
import com.udoolleh.backend.web.dto.ResponseHarborTimetable;
import com.udoolleh.backend.web.dto.ResponseShipFare;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
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
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class ShipControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ShipService shipService;

    @MockBean
    private AdminAuthenticationService adminAuthenticationService;

    private String accessToken;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
        given(adminAuthenticationService.validAdminToken(any())).willReturn(true);
        accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0Iiwibmlja25hbWUiOiJoZWxsby11ZG8ifQ.sRFqCWI3ohcxHHbk969PqdYCFUUe1KEwhndR1b878mo";
    }

    @DisplayName("선착장 조회 성공 테스트 - 상태코드 : 200")
    @Test
    void getHarborTest() throws Exception {
        List<ResponseHarbor.HarborDto> harborDtos = new ArrayList<>();
        ResponseHarbor.HarborDto harborDto = ResponseHarbor.HarborDto.builder()
                .id(2L)
                .name("성산항")
                .build();
        harborDtos.add(harborDto);
        harborDto = ResponseHarbor.HarborDto.builder()
                .id(3L)
                .name("성산항")
                .build();
        harborDtos.add(harborDto);

        given(shipService.getAllHarbors()).willReturn(harborDtos);

        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/harbor")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("harbor-get", // 문서 조각 디렉토리 명
                                preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                        .removePort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields( // response 필드 정보 입력
                                        fieldWithPath("id").type(JsonFieldType.STRING).description("응답 아이디"),
                                        fieldWithPath("dateTime").type(JsonFieldType.STRING).description("응답 시간"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
                                        fieldWithPath("list.[].id").type(JsonFieldType.NUMBER).description("선착장 아이디"),
                                        fieldWithPath("list.[].name").type(JsonFieldType.STRING).description("선착장 이름")
                                )
                        )
                )
        ;
    }

    @DisplayName("선착장 시간표 조회 성공 테스트 - 상태코드 : 200")
    @Test
    void getHarborTimetalbeTest() throws Exception {
        List<ResponseHarborTimetable.TimetableDto> timetableDtos = new ArrayList<>();
        ResponseHarborTimetable.TimetableDto timetableDto = ResponseHarborTimetable.TimetableDto.builder()
                .id(1L)
                .operatingTime("07:30 ~ 17:30")
                .period("1 ~ 2월")
                .build();
        timetableDtos.add(timetableDto);
        timetableDto = ResponseHarborTimetable.TimetableDto.builder()
                .id(2L)
                .operatingTime("07:00 ~ 17:00")
                .period("4 ~ 6월")
                .build();
        timetableDtos.add(timetableDto);
        ResponseHarborTimetable.HarborTimetableDto harborTimetableDto = ResponseHarborTimetable.HarborTimetableDto.builder()
                .destination("하우목동항")
                .timetableDtos(timetableDtos)
                .build();

        given(shipService.getHarborTimetable(any(), any())).willReturn(harborTimetableDto);

        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/harbor/{id}/timetable/{destination}", "3", "성산항")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("harborTimetable-get", // 문서 조각 디렉토리 명
                                preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                        .removePort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("항구 아이디"),
                                        parameterWithName("destination").description("목적지")),
                                responseFields( // response 필드 정보 입력
                                        fieldWithPath("id").type(JsonFieldType.STRING).description("응답 아이디"),
                                        fieldWithPath("dateTime").type(JsonFieldType.STRING).description("응답 시간"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
                                        fieldWithPath("list.destination").type(JsonFieldType.STRING).description("도착지 이름"),
                                        fieldWithPath("list.timetableDtos.[].id").type(JsonFieldType.NUMBER).description("선착장 이름"),
                                        fieldWithPath("list.timetableDtos.[].period").type(JsonFieldType.STRING).description("운영 기간"),
                                        fieldWithPath("list.timetableDtos.[].operatingTime").type(JsonFieldType.STRING).description("운항 시간")
                                )
                        )
                )
        ;
    }

    @DisplayName("선착장 가격표 조회 성공 테스트 - 상태코드 : 200")
    @Test
    void getHarborShipFareTest() throws Exception {
        List<ResponseShipFare.ShipFareDto> shipFareDtos = new ArrayList<>();

        ResponseShipFare.ShipFareDto shipFareDto = ResponseShipFare.ShipFareDto.builder()
                .ageGroup("성인")
                .leaveIsland(7500)
                .enterIsland(10000)
                .id(1L)
                .roundTrip(17000)
                .build();
        shipFareDtos.add(shipFareDto);
        shipFareDto = ResponseShipFare.ShipFareDto.builder()
                .ageGroup("중고등학생")
                .leaveIsland(5500)
                .enterIsland(8000)
                .id(2L)
                .roundTrip(15000)
                .build();
        shipFareDtos.add(shipFareDto);
        ResponseShipFare.HarborShipFareDto harborShipFareDto = ResponseShipFare.HarborShipFareDto.builder()
                .harborName("성산항")
                .shipFareDtos(shipFareDtos)
                .build();

        given(shipService.getShipFare(any())).willReturn(harborShipFareDto);

        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/harbor/{id}/ship-fare", "3")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("shipFare-get", // 문서 조각 디렉토리 명
                                preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                        .removePort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("항구 아이디")),
                                responseFields( // response 필드 정보 입력
                                        fieldWithPath("id").type(JsonFieldType.STRING).description("응답 아이디"),
                                        fieldWithPath("dateTime").type(JsonFieldType.STRING).description("응답 시간"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
                                        fieldWithPath("list.harborName").type(JsonFieldType.STRING).description("항구 이름"),
                                        fieldWithPath("list.shipFareDtos.[].id").type(JsonFieldType.NUMBER).description("아이디"),
                                        fieldWithPath("list.shipFareDtos.[].ageGroup").type(JsonFieldType.STRING).description("나이 그룹"),
                                        fieldWithPath("list.shipFareDtos.[].enterIsland").type(JsonFieldType.NUMBER).description("입도(우도에 들어갈 때) 가격"),
                                        fieldWithPath("list.shipFareDtos.[].leaveIsland").type(JsonFieldType.NUMBER).description("출도(우도에서 나올 때) 가격"),
                                        fieldWithPath("list.shipFareDtos.[].roundTrip").type(JsonFieldType.NUMBER).description("왕복 가격")
                                )
                        )
                )
        ;
    }
    @DisplayName("항구 등록 성공 - 200")
    @Test
    void registerHarborTest() throws Exception {
        doNothing().when(shipService).registerHarbor(any());

        Map<String, String> harborName = Map.of("harborName", "sungsan harbor");

        mockMvc.perform(RestDocumentationRequestBuilders
        .post("/harbor")
                .content(objectMapper.writeValueAsString(harborName))
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-auth-token", accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("harbor-post", // 문서 조각 디렉토리 명
                                preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                        .removePort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("x-auth-token").description("어드민 액세스 토큰")),
                                requestFields(
                                        fieldWithPath("harborName").type(JsonFieldType.STRING).description("항구 이름")
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

    @DisplayName("배 시간 등록 성공 - 200")
    @Test
    void registerHarborTimetableTest() throws Exception {
        doNothing().when(shipService).registerHarborTimetable(any(), any(), any(), any());

        RequestHarborTimetable.RegisterHarborTimetableDto requestDto = RequestHarborTimetable.RegisterHarborTimetableDto.builder()
                .harborName("sungsan harbor")
                .destination("Howumokdong harbor")
                .operatingTime("07:00 ~ 17:00")
                .period("1 ~ 3월")
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders
                .post("/harbor/timetable")
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-auth-token", accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("harbor-timetable-post", // 문서 조각 디렉토리 명
                                preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                        .removePort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("x-auth-token").description("어드민 액세스 토큰")),
                                requestFields(
                                        fieldWithPath("harborName").type(JsonFieldType.STRING).description("출발지"),
                                        fieldWithPath("destination").type(JsonFieldType.STRING).description("목적지"),
                                        fieldWithPath("operatingTime").type(JsonFieldType.STRING).description("운영 시간"),
                                        fieldWithPath("period").type(JsonFieldType.STRING).description("운영 기간")
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

    @DisplayName("배 가격표 등록 성공 - 200")
    @Test
    void registerShipFareTest() throws Exception {
        doNothing().when(shipService).registerShipFare(any());

        RequestShipFare.RegisterShipFareDto registerShipFareDto = RequestShipFare.RegisterShipFareDto.builder()
                .ageGroup("중고등학생")
                .leaveIsland(5500)
                .enterIsland(8000)
                .harborId(2L)
                .roundTrip(15000)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders
                .post("/harbor/ship-fare")
                .content(objectMapper.writeValueAsString(registerShipFareDto))
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-auth-token", accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("harbor-ship-fare-post", // 문서 조각 디렉토리 명
                                preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                        .removePort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("x-auth-token").description("어드민 액세스 토큰")),
                                requestFields(
                                        fieldWithPath("harborId").type(JsonFieldType.NUMBER).description("항구 아이디"),
                                        fieldWithPath("ageGroup").type(JsonFieldType.STRING).description("나이 종류(가격 분류 기준)"),
                                        fieldWithPath("leaveIsland").type(JsonFieldType.NUMBER).description("출도(우도 쪽으로 떠나는 것)"),
                                        fieldWithPath("enterIsland").type(JsonFieldType.NUMBER).description("입도(제주 쪽으로 들어오는 것)"),
                                        fieldWithPath("roundTrip").type(JsonFieldType.NUMBER).description("왕복")
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

    @DisplayName("항구 삭제 성공 - 200")
    @Test
    void deleteHarborTest() throws Exception {
        doNothing().when(shipService).deleteHarbor(any());

        mockMvc.perform(RestDocumentationRequestBuilders
                .delete("/harbor/{id}", "3")
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-auth-token", accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("harbor-delete", // 문서 조각 디렉토리 명
                                preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                        .removePort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("x-auth-token").description("어드민 액세스 토큰")),
                                pathParameters(parameterWithName("id").description("항구 아이디")),
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

    @DisplayName("배 시간 삭제 성공 - 200")
    @Test
    void deleteHarborTimetableTest() throws Exception {
        doNothing().when(shipService).deleteHarborTimetable(any());

        mockMvc.perform(RestDocumentationRequestBuilders
                .delete("/harbor/timetable/{id}", "2")
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-auth-token", accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("harbor-timetable-delete", // 문서 조각 디렉토리 명
                                preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                        .removePort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("x-auth-token").description("어드민 액세스 토큰")),
                                pathParameters(parameterWithName("id").description("항구 시간표 아이디")),
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

    @DisplayName("배 가격표 삭제 성공 - 200")
    @Test
    void deleteShipFareTest() throws Exception {
        doNothing().when(shipService).deleteShipFare(any());

        mockMvc.perform(RestDocumentationRequestBuilders
                .delete("/harbor/ship-fare/{id}", "2")
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-auth-token", accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("harbor-ship-fare-delete", // 문서 조각 디렉토리 명
                                preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                        .removePort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("x-auth-token").description("어드민 액세스 토큰")),
                                pathParameters(parameterWithName("id").description("항구 시간표 아이디")),
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
