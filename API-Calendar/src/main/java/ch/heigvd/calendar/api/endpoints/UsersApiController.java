package ch.heigvd.calendar.api.endpoints;

import ch.heigvd.calendar.api.UsersApi;
import ch.heigvd.calendar.api.model.User;
import ch.heigvd.calendar.entities.UserEntity;
import ch.heigvd.calendar.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UsersApiController implements UsersApi {

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<List<User>> getUsers(@Min(0) @Valid Integer offset, @Min(1) @Max(50) @Valid Integer limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<UserEntity> users = userRepository.findAll(pageable);
        List<User> userList = new ArrayList<>();
        for(UserEntity user: users.getContent()){
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
