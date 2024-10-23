package diplomska.heatmail.api;

import diplomska.heatmail.service.HeatMailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class HeatMailApiController implements HeatMailApi{

    private final HeatMailService heatMailService;

    public HeatMailApiController(HeatMailService heatMailService) {
        this.heatMailService = heatMailService;
    }

    @Override
    public ResponseEntity<Void> sendMail() {
        heatMailService.sendEmail("bojan.dabevski@live.com","test","test");
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
