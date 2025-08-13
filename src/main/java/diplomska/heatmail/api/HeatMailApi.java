package diplomska.heatmail.api;

import diplomska.heatmail.dto.*;
import diplomska.heatmail.model.HeatMail;
import diplomska.heatmail.model.HeatMailAttachment;
import diplomska.heatmail.model.User;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/heatmail")
public interface HeatMailApi {

    @RequestMapping(value = "/sendMail",
            produces = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<Void> sendMail(@RequestBody DateDto dateDto);

    @RequestMapping(value = "/deleteMail",
            produces = { "application/json" },
            method = RequestMethod.DELETE)
    ResponseEntity<Void> deleteMail(@RequestBody DateDto dateDto);

    @RequestMapping(value = "/sendSpecificMail",
            produces = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<Void> sendSpecificMail(@RequestBody MailDto dateDto) throws MessagingException;

    @RequestMapping(value="/insertMail",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<Void> insertMail(@RequestBody List<HeatMailDto> heatMailDtoList);

    @RequestMapping(value="/insertMailAttachment",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<Void> insertMailAttachment(@RequestBody List<HeatMailAttachmentDto> heatMailAttachmentDtoList) throws Exception;

    @RequestMapping(value = "/getMailDashboard",
            produces = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<List<HeatMailDashboardDto>> getMailDashboard(@RequestBody DateDto dateDto);

    @RequestMapping(value = "/getMailAttachmentDashboard",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<List<HeatMailAttachmentDto>> getMailAttachmentDashboard();

    @RequestMapping(value = "/deleteMailAttachment",
            produces = { "application/json" },
            method = RequestMethod.DELETE)
    ResponseEntity<Void> deleteMailAttachment(@RequestBody HeatMailAttachmentDto heatMailAttachmentDto);

    @RequestMapping(value = "/getMailStatistics",
            produces = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<List<HeatMailStatisticsDto>> getMailStatistics(@RequestBody DateDto dateDto);

    @RequestMapping(value = "/getAvailableMailToSendCount",
            produces = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<HeatMailStatisticsDto> getAvailableMailToSendCount(@RequestBody DateDto dateDto);
}
