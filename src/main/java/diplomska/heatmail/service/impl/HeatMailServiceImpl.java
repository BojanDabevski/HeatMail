package diplomska.heatmail.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diplomska.heatmail.dto.HeatMailDto;
import diplomska.heatmail.dto.MailDto;
import diplomska.heatmail.kafka.KafkaProducer;
import diplomska.heatmail.model.HeatMail;
import diplomska.heatmail.model.User;
import diplomska.heatmail.model.enums.HeatMailStatusEnum;
import diplomska.heatmail.repository.HeatMailRepository;
import diplomska.heatmail.service.HeatMailService;
import diplomska.heatmail.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;
import java.util.UUID;

@Service
public class HeatMailServiceImpl implements HeatMailService {

    private final JavaMailSender mailSender;
    private final UserService userService;

    private final HeatMailRepository heatMailRepository;

    private final KafkaProducer kafkaProducer;

    private final Gson gson = new GsonBuilder().create();


    @Autowired
    public HeatMailServiceImpl(JavaMailSender mailSender, UserService userService, HeatMailRepository heatMailRepository, KafkaProducer kafkaProducer) {
        this.mailSender = mailSender;
        this.userService = userService;
        this.heatMailRepository = heatMailRepository;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void sendEmail(MailDto mailDto) {
       SimpleMailMessage message = new SimpleMailMessage();
       message.setTo(mailDto.getTo());
       message.setSubject(mailDto.getTitle());
       message.setText(mailDto.getBody());
        try {
            mailSender.send(message);
            heatMailRepository.updateStatusById(HeatMailStatusEnum.FINISHED, mailDto.getId());
        } catch (Exception e) {
            heatMailRepository.updateStatusById(HeatMailStatusEnum.FAILED, mailDto.getId());
        }
    }

    @Override
    public void sendEmailForMonth(String month, String year) {
        User user = userService.getUserFromToken();
        List<HeatMail> heatMailList = heatMailRepository.findUsersImportedHeatMailForMonthAndYear(user.getId(), month, year);
        heatMailList.addAll(heatMailRepository.findUsersFailedHeatMailForMonthAndYear(user.getId(), month, year));

        for (HeatMail heatMail : heatMailList) {
            MailDto mailDto = new MailDto();
            mailDto.setId(heatMail.getId());
            mailDto.setTo(heatMail.getMail_receiver());
            mailDto.setTitle(heatMail.getMail_title());
            mailDto.setBody(heatMail.getMail_body());

            try {
                kafkaProducer.sendMailMessage(gson.toJson(mailDto));
                heatMailRepository.updateStatusById(HeatMailStatusEnum.MAIL_IN_PROCESS, heatMail.getId());
            } catch (Exception e) {
                heatMailRepository.updateStatusById(HeatMailStatusEnum.FAILED, heatMail.getId());
            }
        }
    }

    @Override
    public void saveHeatMail(List<HeatMailDto> heatMailDtoList) {
        for (HeatMailDto heatMailDto : heatMailDtoList) {
            HeatMail heatMail = mapHeatMailDtoToHeatMail(heatMailDto);
            heatMailRepository.save(heatMail);
        }
    }

    @Override
    public HeatMail mapHeatMailDtoToHeatMail(HeatMailDto heatMailDto) {
        HeatMail heatMail = HeatMail.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .month(heatMailDto.getMonth())
                .year(heatMailDto.getYear())
                .user(userService.getUserFromToken())
                .status(HeatMailStatusEnum.IMPORTED)
                .mail_body(heatMailDto.getMail_body())
                .mail_title(heatMailDto.getMail_title())
                .mail_receiver(heatMailDto.getMail_receiver())
                .build();
        return heatMail;
    }
}
