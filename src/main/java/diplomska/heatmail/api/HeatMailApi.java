package diplomska.heatmail.api;

import diplomska.heatmail.dto.RegisterUserDto;
import diplomska.heatmail.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/heatmail")
public interface HeatMailApi {

    @RequestMapping(value = "/sendMail",
            produces = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<Void> sendMail();

}
