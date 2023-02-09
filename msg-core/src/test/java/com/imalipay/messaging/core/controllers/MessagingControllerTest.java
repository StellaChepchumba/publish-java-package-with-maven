package com.imalipay.messaging.core.controllers;

import com.imalipay.messaging.core.services.MessagingService;
import com.imalipay.messaging.core.services.OtpService;
import com.imalipay.messaging.library.dtos.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@Slf4j
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
//@WebMvcTest(MessagingController.class)
public class MessagingControllerTest extends MessagingCoreTests {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private MessagingController messagingController;


//    @Before
//    public void setup() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//    }

    @Test
    @DisplayName("Unit test fo search messages")
    void searchMessages() {
        Pageable page = Pageable
                .unpaged();

        assertFalse(messagingController.searechMessages(
                page,
                "254769234447",
                "ImaliPay",
                null,
                null,
                null,
                null,
                null,
                null,
                "KE").getPageable().isPaged());
    }

    @Test
    @DisplayName("Unit tests for getUssdOtpCode")
    void getUssdOtpCode() throws Exception {
        String uri = "/ng/messaging/otp/ussd";


    }

//    @Test
//    @Sql("/init.sql")
//    public void  whenEmailBodyIsProvided_thenRetrieveTheCorrectDeliveryMode() throws Exception {
//        EmailDTO emailDTO = new EmailDTO();
//        emailDTO.setEmail("nelly.nakhero@imalipay.com");
//        emailDTO.setName("Nelly");
//
//        SendEmailDTO message = new SendEmailDTO();
//        message.setSubject("Core platform email tests, body=this is a test email with attachment");
//        message.setTo(Arrays.asList(emailDTO) );
//        message.setBody("this is a test email with attachment");
//        message.setDescription("testing new email function");
//        String countryCode = "KE";
//
//        Mockito.when(messagingController.sendEmail(message, countryCode).getStatusMessage()).thenReturn("email is being processed");
//
//        String test = messagingController.sendEmail(message, countryCode).getStatusMessage();
//
//        Assert.assertEquals("email is being processed", test);
//
//    }
//    @Test
//    @DisplayName("Delivery modes unit tests")
//    public  void getDeliveryModesUnitTest() throws  Exception {
//        mockMvc.perform(
//                get("/ke/delivery-mode"))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }

//    @Autowired
//    MessagingController messagingController;
//
////    @Test
////    void sendmail () throws Exception {
////        EmailDTO emailDTO = new EmailDTO();
////        emailDTO.setEmail("nelly.nakhero@imalipay.com");
////        emailDTO.setName("Nelly");
////
////        SendEmailDTO message = new SendEmailDTO();
////        message.setSubject("Core platform email tests, body=this is a test email with attachment");
////        message.setTo(Arrays.asList(emailDTO) );
////        message.setBody("this is a test email with attachment");
////        message.setDescription("testing new email function");
////        String countryCode = "KE";
////        var res = messagingController.sendEmail(message, countryCode);
////        assertEquals(
////                StatusCodes.SUCCESS,
////                res.getStatusCode());
////    }
////
////    @SneakyThrows
////    @Test
////    void sendMessage() {
////        SendMessageDTO message = SendMessageDTO.builder()
////                .from("ImaliPay")
////                .to("254705881346")
////                .body("")
////                .build();
////
////
////        assertEquals(
////                StatusCodes.SUCCESS,
////                messagingController.sendMessage(message, "sms", "KE").getStatusCode());
////    }
//
//    @SneakyThrows
//    @Test
//    void sendBatchMessage() {
//        ArrayList<String> tos = new ArrayList<>();
//        tos.add("254705881346");
//        tos.add("254705881346");
//
//        SendBatchMessageDTO messageDTO = SendBatchMessageDTO.builder()
//                .body("this is a test push message from nelly")
//                .from("Imalipay")
//                .to(tos)
//                .scheduledSendTime(ZonedDateTime.now())
//                .build();
//
//        assertEquals(
//                StatusCodes.SUCCESS,
//                messagingController.sendBatchMessages(messageDTO, "sms", "KE").getStatusCode());
//    }
//
//    void  sendEventNotification() {
//
//    }
//
//    void  sendEventMessage() {
//
//    }
//
//    @Test
//    void getDeliveryModes() {
//        var res = messagingController.getDeliveryModes("KE");
//        assertEquals(StatusCodes.SUCCESS, res.getStatusCode());
//        assertFalse(res.getData().isEmpty());
//        assertNotNull(res.getData());
//    }
//
//    @Test
//    void searchMessages() {
//        Pageable page = Pageable
//                .unpaged();
//
//        assertFalse(messagingController.searechMessages(
//                page,
//                "254769234447",
//                "ImaliPay",
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                "KE").getPageable().isPaged());
//    }
//
//    void  getUssdOtpCode(){
//
//    }
}
