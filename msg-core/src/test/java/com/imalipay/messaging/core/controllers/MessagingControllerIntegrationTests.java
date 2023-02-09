package com.imalipay.messaging.core.controllers;

import com.zaxxer.hikari.HikariDataSource;
import static java.util.Collections.singletonMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imalipay.messaging.core.MessagingCore;
import com.imalipay.messaging.core.configs.JsonUtil;
import com.imalipay.messaging.core.models.Messages;
import com.imalipay.messaging.core.models.Routes;
import com.imalipay.messaging.core.models.Vendors;
import com.imalipay.messaging.core.repositories.MessagesRepositories;
import com.imalipay.messaging.core.repositories.RoutesRepository;
import com.imalipay.messaging.core.services.MessagingService;
import com.imalipay.messaging.core.services.RoutesService;
import com.imalipay.messaging.library.dtos.DeliveryMode;
import com.imalipay.messaging.library.dtos.EmailDTO;
import com.imalipay.messaging.library.dtos.MessageStatus;
import com.imalipay.messaging.library.dtos.SendEmailDTO;

import static com.imalipay.messaging.library.dtos.DeliveryMode.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.*;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.utility.DockerImageName;


import javax.sql.DataSource;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
//@Testable
//@AutoConfigureMockMvc
//@Transactional
//@ActiveProfiles("test")
////@RunWith(SpringRunner.class)
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class MessagingControllerIntegrationTests {
    @Autowired
    private MessagingService messagingService;

    @Autowired
    private RoutesService routesService;

    @Autowired
    private MockMvc mvc;

    @MockBean
    MessagingController messagingController;

    @Autowired
    RoutesRepository routesRepository;

    @MockBean
    MessagesRepositories messagesRepositories;

    // Containers declared as static fields will be shared between test methods.
    @Container
    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:13.7")
                    .withDatabaseName("core_v1")
                    .withUsername("root")
                    .withPassword("lipuka")
                    .withInitScript("init.sql")
                    // Container can have tmpfs mounts for storing data in host memory.
                    // This is useful if you want to speed up your database tests.
                    .withTmpFs(singletonMap("/var/lib/postgresql/data", "rw"));

    private static DataSource datasource;

    @Container
    public static GenericContainer<?> redis =
            new GenericContainer<>(DockerImageName.parse("redis:6.2-alpine")).withExposedPorts(6379);

    @BeforeAll
    public static void setUp(){
        postgresContainer.withReuse(true);
        postgresContainer.withInitScript("src/test/resources/init.sql");
        postgresContainer.start();
        redis.start();
    }

    @DynamicPropertySource
    public static void overrideProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", postgresContainer::getDriverClassName);
        log.info("The values are {} \n \n", registry.toString());
    }

    @Test
    @Sql("/init.sql")
    public void instantiateRoutes() {

        //GMAIL
        Routes r1 = Routes.builder()
                .vendor(Vendors.builder().vendorCode("GMAIL").vendorName("GMAIL").createdAt(ZonedDateTime.now()).build())
                .countryCode("KE")
                .createdAt(ZonedDateTime.now())
                .deliveryMode(EMAIL)
                .build();

        log.info("GMAIL {}", r1);
//        var result = routesRepository.save(r1);
//        log.info("result for r1 {}", result);
    }

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            log.info("\n\nthe datasource is {}", postgresContainer.getJdbcUrl());
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgresContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgresContainer.getUsername(),
                    "spring.datasource.password=" + postgresContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

//    @Test
//    public  void addRoutes() throws Exception {
//        //UWAZII
//        Routes routes = new Routes();
//        routes.setRouteId(1);
//        Vendors vendors = new Vendors("UWAZII", "UWAZII", ZonedDateTime.now());
//        routes.setVendor(vendors);
//        routes.setCountryCode("KE");
//        routes.setDeliveryMode(DeliveryMode.SMS);
//        var res = routesService.findRoute(SMS, "KE");
//        log.debug("searched for path {}", res);
//
//
//        //GMAIL
//        Routes routeGMAIL = new Routes();
//        routeGMAIL.setRouteId(2);
//        Vendors vendorGMAIL = new Vendors("GMAIL", "GMAIL", ZonedDateTime.now());
//        routeGMAIL.setVendor(vendorGMAIL);
//        routeGMAIL.setCountryCode("KE");
//        routeGMAIL.setDeliveryMode(EMAIL);
//    }
//
//    @Test
//    @Sql("/init.sql")
//    public  void  testSendEmail() throws Exception {
//        ObjectMapper mapper = new ObjectMapper();
//
//        EmailDTO emailDTO = new EmailDTO();
//        emailDTO.setEmail("nelly.nakhero@imalipay.com");
//        emailDTO.setName("Nelly");
//
//        log.debug(" DTO {}", emailDTO);
//
//        SendEmailDTO message = SendEmailDTO.builder()
//                .subject("Core platform email tests, body=this is a test email with attachment")
//                .to(List.of(emailDTO))
//                .body("this is a test email with attachment")
//                .description("testing new email function")
//                .build() ;
//        String countryCode = "KE";
//
//        log.trace("search for route for message");
//        Routes route = routesService.findRoute(EMAIL, countryCode);
//        log.trace("route found {}", route);
//
//        Messages messageEntity = Messages.builder()
//                .messageId(UUID.randomUUID())
//                .body(message.getBody())
//                .countryCode(countryCode)
//                .source("no_reply@imalipay.com")
//                .recipient(mapper.writer().writeValueAsString(message.getTo()))
//                .route(route)
//                .status(MessageStatus.CREATED)
//                .createdAt(ZonedDateTime.now())
//                .description(message.getDescription())
//                .build();
//    }

//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    MessagingController messagingController;
//


//    @MockBean
//    MessagesRepositories messagesRepositories;
//
//    @Autowired
//    RoutesService routesService;
//
//    @Autowired
//    private  MessagingService messagingService;
//
//
//
//    @Before
//    public void setUp() throws Exception {
//    }
//
//
//    @Test
//    @Sql("/init.sql")
//    public void whenPostMail_thenCreateMail() throws Exception {
//        ObjectMapper mapper = new ObjectMapper();
//
//        EmailDTO emailDTO = new EmailDTO();
//        emailDTO.setEmail("nelly.nakhero@imalipay.com");
//        emailDTO.setName("Nelly");
//
//        log.trace(" DTO {}", emailDTO);
//
//        SendEmailDTO message = SendEmailDTO.builder()
//                .subject("Core platform email tests, body=this is a test email with attachment")
//                .to(List.of(emailDTO))
//                .body("this is a test email with attachment")
//                .description("testing new email function")
//                .build() ;
//        String countryCode = "KE";
//
//        log.trace("search for route for message");
//        Routes route = routesService.findRoute(EMAIL, countryCode);
//        log.trace("route found {}", route);
//
//        Messages messageEntity = Messages.builder()
//                .messageId(UUID.randomUUID())
//                .body(message.getBody())
//                .countryCode(countryCode)
//                .source("no_reply@imalipay.com")
//                .recipient(mapper.writer().writeValueAsString(message.getTo()))
//                .route(route)
//                .status(MessageStatus.CREATED)
//                .createdAt(ZonedDateTime.now())
//                .description(message.getDescription())
//                .build();
//
//        // log to database record keeping
//        log.info("saving message entity {}", messageEntity);
//        var res = messagesRepositories.save(messageEntity);
//        log.info("response {}", res);
//
//
//        given(messagesRepositories.save(Mockito.any())).willReturn(res);
//
//        mvc
//                .perform(post("/ke/send/delivery-mode/email")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(JsonUtil.toJson((message))))
//                .andExpect(status().
//                        isOk());
//
//        verify(messagesRepositories, times(1)).save(messageEntity);
////        verify(messagesRepositories,  times(1)).save(messageEntity);
//        reset(messagesRepositories);
//    }
//
//
//    @Test
//    public void givenDeliveryModes_whenGetDeliveryModes_thenReturnJsonArray() throws Exception {
//
//        List<DeliveryMode> allDeliveryModes = Arrays.asList(EMAIL, SMS, WHATSAPP, IN_APP, BOT, SLACK, USSD);
//
//        log.trace("All Delivery Modes {} {}", allDeliveryModes, allDeliveryModes.get(1) != null);
//
//        given(Arrays.asList(DeliveryMode.values())).willReturn(allDeliveryModes);
//
//        mvc
//                .perform(get("/ke/delivery-mode").
//                        contentType(MediaType.APPLICATION_JSON)).
//                andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(7)))
//                .andExpect(jsonPath("$[0]", is(EMAIL)))
//                .andExpect(jsonPath("$[1]", is(SMS))
//                );
//
//    }

//    @Test
//    @Sql("/init.sql")
//    public void instantiateRoutes() {
//
//        //GMAIL
//        Routes r1 = Routes.builder()
//                .vendor(Vendors.builder().vendorCode("GMAIL").vendorName("GMAIL").createdAt(ZonedDateTime.now()).build())
//                .countryCode("KE")
//                .createdAt(ZonedDateTime.now())
//                .deliveryMode(EMAIL)
//                .build();
//
//        log.info("GMAIL {}", r1);
//        var result = routesRepository.save(r1);
////        var rr1 = routesRepository.save(r1);
//        log.info("result for r1 {}", result);
//
//        //UWAZII
//        Routes r2 = Routes.builder()
//                .vendor(Vendors.builder().vendorCode("UWAZII").createdAt(ZonedDateTime.now()).build())
//                .deliveryMode(SMS)
//                .countryCode("KE")
//                .build();
//        routesRepository.save(r2);
//
//        //GUPSHUP
//        Routes r3 = Routes.builder()
//                .vendor(Vendors.builder().vendorCode("GUPSHUP").createdAt(ZonedDateTime.now()).build())
//                .deliveryMode(WHATSAPP)
//                .countryCode("KE")
//                .build();
//        routesRepository.save(r3);
//
//        //FIREBASE
//        Routes r4 = Routes.builder()
//                .vendor(Vendors.builder().vendorCode("FIREBASE").createdAt(ZonedDateTime.now()).build())
//                .deliveryMode(IN_APP)
//                .countryCode("KE")
//                .build();
//        routesRepository.save(r4);
//
//        //INFOBIP
//        Routes r5 = Routes.builder()
//                .vendor(Vendors.builder().vendorCode("INFOBIP").createdAt(ZonedDateTime.now()).build())
//                .deliveryMode(SMS)
//                .countryCode("NG")
//                .build();
//        routesRepository.save(r5);
//
//        //GMAIL
//        Routes r6 = Routes.builder()
//                .vendor(Vendors.builder().vendorCode("GMAIL").createdAt(ZonedDateTime.now()).build())
//                .deliveryMode(EMAIL)
//                .countryCode("NG")
//                .build();
//        routesRepository.save(r6);
//
//        //GUPSHUP
//        Routes r7 = Routes.builder()
//                .vendor(Vendors.builder().vendorCode("GUPSHUP").createdAt(ZonedDateTime.now()).build())
//                .deliveryMode(WHATSAPP)
//                .countryCode("NG")
//                .build();
//        routesRepository.save(r7);
//
//        //FIREBASE
//        Vendors v8 = Vendors.builder()
//                .vendorCode("FIREBASE")
//                .build();
//        Routes r8 = Routes.builder()
//                .vendor(Vendors.builder().vendorCode("FIREBASE").createdAt(ZonedDateTime.now()).build())
//                .deliveryMode(IN_APP)
//                .countryCode("NG")
//                .build();
//        routesRepository.save(r8);
//
//        log.info("list of routes {}", routesRepository.findAll());


//    }

    @AfterAll
    public static void tearDown(){
        postgresContainer.stop();
        redis.stop();
    }
}




