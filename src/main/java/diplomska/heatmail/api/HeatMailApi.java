package diplomska.heatmail.api;

import diplomska.heatmail.dto.DateDto;
import diplomska.heatmail.dto.HeatMailDashboardDto;
import diplomska.heatmail.dto.HeatMailDto;
import diplomska.heatmail.dto.RegisterUserDto;
import diplomska.heatmail.model.HeatMail;
import diplomska.heatmail.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/heatmail")
public interface HeatMailApi {

    @RequestMapping(value = "/sendMail",
            produces = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<Void> sendMail(@RequestBody DateDto dateDto);

    @RequestMapping(value="/insertMail",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<Void> insertMail(@RequestBody List<HeatMailDto> heatMailDtoList);

    @RequestMapping(value = "/getMailDashboard",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<List<HeatMailDashboardDto>> getMailDashboard(@RequestBody DateDto dateDto);
}
