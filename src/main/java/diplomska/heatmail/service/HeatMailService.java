package diplomska.heatmail.service;

import diplomska.heatmail.dto.*;
import diplomska.heatmail.model.HeatMail;
import diplomska.heatmail.model.HeatMailAttachment;
import jakarta.mail.MessagingException;
import java.util.List;

public interface HeatMailService {
    void sendEmail(MailDto message) throws MessagingException;

    void sendMultipartMail(MailDto mailDto) throws MessagingException;

    void sendEmailForMonth(String month, String year);

    void saveHeatMail(List<HeatMailDto> heatMailDto);

    void saveHeatMailAttachments(List<HeatMailAttachmentDto> heatMailAttachmentDtoList);
    List<HeatMailDashboardDto> getMailDashboard(String month, String year);

    List<HeatMailStatisticsDto> getMailStatistics(String month, String year);

    HeatMailStatisticsDto getAvailableMailToSendCount(String month, String year);

    HeatMail mapHeatMailDtoToHeatMail(HeatMailDto heatMailDto);

    HeatMailAttachment mapHeatMailAttachmentDtoToHeatMailAttachment(HeatMailAttachmentDto heatMailAttachmentDto);

    HeatMailDashboardDto mapHeatMailToHeatMailDashboardDto(HeatMail heatMail);

    boolean checkIfAttachmentExistsForUser(HeatMailAttachmentDto heatMailAttachmentDto);
}
