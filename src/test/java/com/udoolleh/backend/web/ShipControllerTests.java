package com.udoolleh.backend.web;

import com.udoolleh.backend.entity.Harbor;
import com.udoolleh.backend.entity.HarborTimetable;
import com.udoolleh.backend.entity.ShipFare;
import com.udoolleh.backend.provider.service.BoardService;
import com.udoolleh.backend.provider.service.ShipService;
import com.udoolleh.backend.provider.service.UserService;
import com.udoolleh.backend.repository.BoardRepository;
import com.udoolleh.backend.repository.HarborRepository;
import com.udoolleh.backend.repository.HarborTimetableRepository;
import com.udoolleh.backend.repository.ShipFareRepository;
import com.udoolleh.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestPartFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
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
    private ShipService shipService;

    @Autowired
    private HarborRepository harborRepository;

    @Autowired
    private HarborTimetableRepository harborTimetableRepository;

    @Autowired
    private ShipFareRepository shipFareRepository;

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

    @DisplayName("선착장 조회 성공 테스트 - 상태코드 : 200")
    @Transactional
    @Test
    void getHarborTest() throws Exception {
        Harbor harbor = Harbor.builder()
                .harborName("성산항")
                .build();
        harborRepository.save(harbor);
        harbor = Harbor.builder()
                .harborName("종달항")
                .build();
        harborRepository.save(harbor);

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
    @Transactional
    @Test
    void getHarborTimetalbeTest() throws Exception {
        Harbor harbor = Harbor.builder()
                .harborName("성산항")
                .build();
        harbor = harborRepository.save(harbor);
        HarborTimetable harborTimetable = HarborTimetable.builder()
                .harbor(harbor)
                .destination("천진항")
                .period("3 ~ 6월")
                .operatingTime("07:30 ~ 17:30")
                .build();
        harborTimetableRepository.save(harborTimetable);

        harborTimetable = HarborTimetable.builder()
                .harbor(harbor)
                .destination("천진항")
                .period("7 ~ 10월")
                .operatingTime("07:30 ~ 17:00")
                .build();
        harborTimetableRepository.save(harborTimetable);

        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/harbor/{id}/timetable/{destination}", harbor.getId(), harborTimetable.getDestination())
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

    @DisplayName("선착장 시간표 조회 성공 테스트 - 상태코드 : 200")
    @Transactional
    @Test
    void getHarborShipFare() throws Exception {
        Harbor harbor = Harbor.builder()
                .harborName("성산항")
                .build();
        harbor = harborRepository.save(harbor);
        ShipFare shipFare = ShipFare.builder()
                .harbor(harbor)
                .ageGroup("중학생")
                .leaveIsland(2000)
                .enterIsland(5000)
                .roundTrip(8000)
                .build();
        shipFareRepository.save(shipFare);
        harbor.addShipFare(shipFare);

        shipFare = ShipFare.builder()
                .harbor(harbor)
                .ageGroup("성인")
                .leaveIsland(5500)
                .enterIsland(6000)
                .roundTrip(12000)
                .build();
        shipFareRepository.save(shipFare);
        harbor.addShipFare(shipFare);

        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/harbor/{id}/ship-fare", harbor.getId())
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

}
