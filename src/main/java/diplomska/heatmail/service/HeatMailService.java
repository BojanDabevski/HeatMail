package diplomska.heatmail.service;

import diplomska.heatmail.dto.HeatMailDashboardDto;
import diplomska.heatmail.dto.HeatMailDto;
import diplomska.heatmail.dto.HeatMailStatisticsDto;
import diplomska.heatmail.dto.MailDto;
import diplomska.heatmail.model.HeatMail;
import jakarta.mail.MessagingException;
import java.util.List;

public interface HeatMailService {
    void sendEmail(MailDto message) throws MessagingException;

    void sendEmailForMonth(String month, String year);

    void saveHeatMail(List<HeatMailDto> heatMailDto);

    List<HeatMailDashboardDto> getMailDashboard(String month, String year);

    List<HeatMailStatisticsDto> getMailStatistics(String month, String year);

    HeatMailStatisticsDto getAvailableMailToSendCount(String month, String year);

    HeatMail mapHeatMailDtoToHeatMail(HeatMailDto heatMailDto);

    HeatMailDashboardDto mapHeatMailToHeatMailDashboardDto(HeatMail heatMail);
}
