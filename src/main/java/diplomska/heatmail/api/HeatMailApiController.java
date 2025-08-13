package diplomska.heatmail.api;

import diplomska.heatmail.dto.*;
import diplomska.heatmail.model.HeatMailAttachment;
import diplomska.heatmail.service.HeatMailService;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class HeatMailApiController implements HeatMailApi{

    private final HeatMailService heatMailService;

    public HeatMailApiController(HeatMailService heatMailService) {
        this.heatMailService = heatMailService;
    }

    @Override
    public ResponseEntity<Void> sendMail(DateDto dateDto) {
        try {
            heatMailService.sendEmailForMonth(dateDto.getMonth(), dateDto.getYear());
            return new ResponseEntity<Void>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<Void> deleteMail(DateDto dateDto) {
        try {
            heatMailService.deleteMailForMonthAndYearForUser(dateDto.getMonth(), dateDto.getYear());
            return new ResponseEntity<Void>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> sendSpecificMail(MailDto mailDto) throws MessagingException {
        heatMailService.sendMultipartMail(mailDto);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> insertMail(List<HeatMailDto> heatMailDtoList) {
        heatMailService.saveHeatMail(heatMailDtoList);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> insertMailAttachment(List<HeatMailAttachmentDto> heatMailAttachmentDtoList) throws Exception {
        if (heatMailService.checkIfAttachmentExistsForUser(heatMailAttachmentDtoList.get(0))) {
            throw new Exception("Attachment already exists for this user.");
        }
        heatMailService.saveHeatMailAttachments(heatMailAttachmentDtoList);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<HeatMailDashboardDto>> getMailDashboard(DateDto dateDto) {
        return ResponseEntity.ok(heatMailService.getMailDashboard(dateDto.getMonth(), dateDto.getYear()));
    }

    @Override
    public ResponseEntity<List<HeatMailAttachmentDto>> getMailAttachmentDashboard() {
        return ResponseEntity.ok(heatMailService.getHeatMailAttachments());
    }

    @Override
    public ResponseEntity<Void> deleteMailAttachment(HeatMailAttachmentDto heatMailAttachmentDto) {
        try {
            heatMailService.deleteMailAttachment(heatMailAttachmentDto);
            return new ResponseEntity<Void>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<HeatMailStatisticsDto>> getMailStatistics(DateDto dateDto) {
        return ResponseEntity.ok(heatMailService.getMailStatistics(dateDto.getMonth(), dateDto.getYear()));
    }

    @Override
    public ResponseEntity<HeatMailStatisticsDto> getAvailableMailToSendCount(DateDto dateDto) {
        return ResponseEntity.ok(heatMailService.getAvailableMailToSendCount(dateDto.getMonth(), dateDto.getYear()));
    }


}
