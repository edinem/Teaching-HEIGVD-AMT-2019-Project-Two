package ch.heigvd.calendar.api.endpoints;

import ch.heigvd.calendar.api.UsersApi;
import ch.heigvd.calendar.api.model.User;
import ch.heigvd.calendar.entities.UserEntity;
import ch.heigvd.calendar.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UsersApiController implements UsersApi {

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<List<User>> getUsers() {
        Iterable<UserEntity> users = userRepository.findAll();
        List<User> userList = new ArrayList<>();
        for(UserEntity user: users){
            userList.add(toUser(user));
        }
        return ResponseEntity.ok(userList);
    }

    private User toUser(UserEntity entity) {
        User user = new User();
        user.setEmail(entity.getEmail());
        return user;
    }
}
