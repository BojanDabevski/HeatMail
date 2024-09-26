package diplomska.heatmail.service;

import diplomska.heatmail.model.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {
    List<User> allUsers();
}
