package diplomska.heatmail.kafka;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    @Value("${spring.kafka.producer.topic}")
    private String heatMailTopicName;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMailMessage(String msg){
        kafkaTemplate.send(heatMailTopicName, msg);
    }
}
