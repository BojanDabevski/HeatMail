package diplomska.heatmail.service;

import diplomska.heatmail.dto.HeatMailDto;
import diplomska.heatmail.dto.MailDto;
import diplomska.heatmail.model.HeatMail;
import org.springframework.mail.SimpleMailMessage;

import java.util.List;

public interface HeatMailService {
    void sendEmail(MailDto message);

    void sendEmailForMonth(String month, String year);

    void saveHeatMail(List<HeatMailDto> heatMailDto);

    HeatMail mapHeatMailDtoToHeatMail(HeatMailDto heatMailDto);
}
