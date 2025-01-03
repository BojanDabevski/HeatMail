package diplomska.heatmail.api;

import diplomska.heatmail.model.User;
import diplomska.heatmail.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Controller
public class UserApiController implements UserApi{

    private final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<User> getUser() {
        User currentUser = userService.getUserFromToken();

        return ResponseEntity.ok(currentUser);
    }

    @Override
    public ResponseEntity<List<User>> allUsers() {
        List <User> users = userService.allUsers();

        return ResponseEntity.ok(users);
    }
}
