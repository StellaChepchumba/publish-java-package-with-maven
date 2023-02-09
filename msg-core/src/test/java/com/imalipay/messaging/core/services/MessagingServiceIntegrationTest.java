package com.imalipay.messaging.core.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imalipay.messaging.core.configs.ApplicationConfigs;
import com.imalipay.messaging.core.models.Messages;
import com.imalipay.messaging.core.models.Routes;
import com.imalipay.messaging.core.repositories.MessageTemplatesRepository;
import com.imalipay.messaging.core.repositories.MessagesRepositories;
import com.imalipay.messaging.library.dtos.EmailDTO;
import com.imalipay.messaging.library.dtos.MessageStatus;
import com.imalipay.messaging.library.dtos.SendEmailDTO;
import freemarker.template.Configuration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.UUID;

import static com.imalipay.messaging.library.dtos.DeliveryMode.EMAIL;

@RunWith(SpringRunner.class)
public class MessagingServiceIntegrationTest {
    private final RabbitTemplate rabbitTemplate = new RabbitTemplate();
    private final MessagesRepositories messagesRepositories = null;
    private final MessageTemplatesRepository messageTemplatesRepository = null;
    private final RoutesService routesService = null;
    private final ApplicationConfigs applicationConfigs = new ApplicationConfigs();
    private final Configuration config = new Configuration();
    private final ObjectMapper mapper = new ObjectMapper();

    @TestConfiguration
    static class MessagingServiceIntegrationTestContextConfiguration {

    }

}
