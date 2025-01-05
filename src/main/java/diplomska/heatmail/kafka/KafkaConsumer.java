package diplomska.heatmail.kafka;

import com.google.gson.Gson;
import diplomska.heatmail.dto.MailDto;
import diplomska.heatmail.service.HeatMailService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;

@Component
public class KafkaConsumer {

    private final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    private final Gson gson;
    private final HeatMailService heatMailService;

    public KafkaConsumer(Gson gson, HeatMailService heatMailService) {
        this.gson = gson;
        this.heatMailService = heatMailService;
    }

    @KafkaListener(topics = "${spring.kafka.consumer.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void receiveJson(String event) throws MessagingException {
       MailDto mailMessage = gson.fromJson(event, MailDto.class);
       heatMailService.sendEmail(mailMessage);
    }
}
