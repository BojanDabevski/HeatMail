package diplomska.heatmail.api;

import diplomska.heatmail.dto.LoginResponseDto;
import diplomska.heatmail.dto.LoginUserDto;
import diplomska.heatmail.dto.RegisterUserDto;
import diplomska.heatmail.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping(value = "/auth")
public interface AuthenticationApi {
    @RequestMapping(value = "/signup",
            produces = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto);

    @RequestMapping(value = "/login",
            produces = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<LoginResponseDto> login(@RequestBody LoginUserDto loginUserDto);
}
