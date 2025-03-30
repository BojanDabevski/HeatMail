package diplomska.heatmail.api;

import diplomska.heatmail.dto.LoginResponseDto;
import diplomska.heatmail.dto.LoginUserDto;
import diplomska.heatmail.dto.RegisterUserDto;
import diplomska.heatmail.model.User;
import diplomska.heatmail.service.AuthenticationService;
import diplomska.heatmail.service.UserService;
import diplomska.heatmail.service.impl.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class AuthenticationApiController implements AuthenticationApi{

    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    private final UserService userService;

    public AuthenticationApiController(JwtService jwtService, AuthenticationService authenticationService, UserService userService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @Override
    public ResponseEntity<User> register(RegisterUserDto registerUserDto) throws Exception {
        if (registerUserDto.getEmail().equals("") && registerUserDto.getPassword().equals("") && registerUserDto.getFullName().equals("")) {
            throw new Exception("Email, Password and Full Name are required.");
        } else if (!registerUserDto.getEmail().equals("") && registerUserDto.getPassword().equals("") && registerUserDto.getFullName().equals("")) {
            throw new Exception("Password and Full Name are required.");
        } else if (registerUserDto.getEmail().equals("") && !registerUserDto.getPassword().equals("") && registerUserDto.getFullName().equals("")) {
            throw new Exception("Email and Full Name are required.");
        } else if (registerUserDto.getEmail().equals("") && registerUserDto.getPassword().equals("") && !registerUserDto.getFullName().equals("")) {
            throw new Exception("Email and Password are required.");
        } else if (!registerUserDto.getEmail().equals("") && !registerUserDto.getPassword().equals("") && registerUserDto.getFullName().equals("")) {
            throw new Exception("Full Name is required.");
        } else if (!registerUserDto.getEmail().equals("") && registerUserDto.getPassword().equals("") && !registerUserDto.getFullName().equals("")) {
            throw new Exception("Password is required.");
        } else if (registerUserDto.getEmail().equals("") && !registerUserDto.getPassword().equals("") && !registerUserDto.getFullName().equals("")) {
            throw new Exception("Email is required.");
        } else if (!registerUserDto.getEmail().contains("@")) {
            throw new Exception("Please enter a valid email address.");
        } else if (!userService.checkIfUserExists(registerUserDto.getEmail())) {
            User registeredUser = authenticationService.signup(registerUserDto);
            return ResponseEntity.ok(registeredUser);
        } else {
           throw new Exception("This email is already in use. Try registering with different email.");
        }
    }

    @Override
    public ResponseEntity<LoginResponseDto> login(LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponseDto loginResponse =  LoginResponseDto.builder()
                .token(jwtToken)
                .build();

        return ResponseEntity.ok(loginResponse);
    }

}
