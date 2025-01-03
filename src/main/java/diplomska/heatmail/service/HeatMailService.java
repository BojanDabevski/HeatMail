package diplomska.heatmail.service;

import diplomska.heatmail.dto.HeatMailDto;
import diplomska.heatmail.model.HeatMail;

import java.util.List;

public interface HeatMailService {
    void sendEmail(String to, String subject, String body);

    void sendEmailForMonth(String month, String year);

    void saveHeatMail(List<HeatMailDto> heatMailDto);

    HeatMail mapHeatMailDtoToHeatMail(HeatMailDto heatMailDto);
}
