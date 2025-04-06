package diplomska.heatmail.api;

import diplomska.heatmail.dto.DateDto;
import diplomska.heatmail.dto.HeatMailDashboardDto;
import diplomska.heatmail.dto.HeatMailDto;
import diplomska.heatmail.dto.HeatMailStatisticsDto;
import diplomska.heatmail.model.HeatMail;
import diplomska.heatmail.service.HeatMailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

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
    public ResponseEntity<Void> insertMail(List<HeatMailDto> heatMailDtoList) {
        heatMailService.saveHeatMail(heatMailDtoList);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<HeatMailDashboardDto>> getMailDashboard(DateDto dateDto) {
        return ResponseEntity.ok(heatMailService.getMailDashboard(dateDto.getMonth(), dateDto.getYear()));
    }

    @Override
    public ResponseEntity<List<HeatMailStatisticsDto>> getMailStatistics(DateDto dateDto) {
        return ResponseEntity.ok(heatMailService.getMailStatistics(dateDto.getMonth(), dateDto.getYear()));
    }


}
