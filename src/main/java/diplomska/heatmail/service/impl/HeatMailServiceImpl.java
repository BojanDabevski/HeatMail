package diplomska.heatmail.service.impl;

import diplomska.heatmail.dto.HeatMailDto;
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

    @Autowired
    public HeatMailServiceImpl(JavaMailSender mailSender, UserService userService, HeatMailRepository heatMailRepository) {
        this.mailSender = mailSender;
        this.userService = userService;
        this.heatMailRepository = heatMailRepository;
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    @Override
    public void sendEmailForMonth(String month, String year) {
        User user = userService.getUserFromToken();
        List<HeatMail> heatMailList = heatMailRepository.findUsersImportedHeatMailForMonthAndYear(user.getId(), month, year);

        for (HeatMail heatMail : heatMailList) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(heatMail.getMail_receiver());
            message.setSubject(heatMail.getMail_title());
            message.setText(heatMail.getMail_body());

            try {
                mailSender.send(message);
                heatMailRepository.updateStatusById(HeatMailStatusEnum.MAIL_SENT, heatMail.getId());
            } catch (Exception e) {
                heatMailRepository.updateStatusById(HeatMailStatusEnum.FINISHED, heatMail.getId());
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
