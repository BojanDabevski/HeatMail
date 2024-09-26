package diplomska.heatmail.api;

import diplomska.heatmail.dto.RegisterUserDto;
import diplomska.heatmail.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@RequestMapping(value = "/users")
public interface UserApi {
    @RequestMapping(value = "/me",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<User> getUser();

    @RequestMapping(value = "/allUsers",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<List<User>> allUsers();
}
