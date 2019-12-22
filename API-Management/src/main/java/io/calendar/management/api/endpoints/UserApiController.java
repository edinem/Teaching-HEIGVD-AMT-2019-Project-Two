package io.calendar.management.api.endpoints;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import io.calendar.management.api.UserApi;
import io.calendar.management.api.model.User;
import io.calendar.management.entities.UserEntity;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import io.calendar.management.repositories.UserRepository;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-07-26T19:36:34.802Z")

@Controller
public class UserApiController implements UserApi {

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<Object> createUser(@ApiParam(value = "", required = true) @Valid @RequestBody User user) {
        UserEntity newUserEntity = toUserEntity(user);
        userRepository.save(newUserEntity);
        String email = newUserEntity.getEmail();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(newUserEntity.getEmail()).toUri();

        return ResponseEntity.created(location).build();
    }

    public ResponseEntity<Object> updateUser(@ApiParam(value = "", required = true) @Valid @RequestBody User user) {
        UserEntity newUserEntity = toUserEntity(user);
        userRepository.save(newUserEntity);
        String email = newUserEntity.getEmail();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(newUserEntity.getEmail()).toUri();

        return ResponseEntity.created(location).build();
    }


    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = new ArrayList<>();
        for (UserEntity userEntity : userRepository.findAll()) {
            users.add(toUser(userEntity));
        }

        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<Object> getUser(@ApiParam(value = "",required=true) @PathVariable("userId") String userId) {

        Optional<UserEntity> userRetrieved = userRepository.findById(userId);
        if(userRetrieved.isEmpty()){
            return ResponseEntity.badRequest().body("ID not found");
        }else{

            User user = toUser(userRetrieved.get());
            return ResponseEntity.ok(user);
        }
    }

    @Override
    public ResponseEntity<Object> deleteUser(@ApiParam(value = "",required=true) @PathVariable("userId") String userId) {
        Optional<UserEntity> userRetrieved = userRepository.findById(userId);
        if(userRetrieved.isEmpty()){
            return ResponseEntity.badRequest().body("User doesn't exist");
        }else{
            userRepository.delete(userRetrieved.get());
            Optional<UserEntity> userRetrievedAfterDelete = userRepository.findById(userId);
            if(userRetrievedAfterDelete.isEmpty()){
                return ResponseEntity.ok().body("User deleted");
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
            }
        }
    }

    private UserEntity toUserEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setEmail(user.getEmail());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setPassword(user.getPassword());

        return entity;
    }

    private User toUser(UserEntity entity) {
        User user = new User();
        user.setEmail(entity.getEmail());
        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());
        user.setPassword(entity.getPassword());

        return user;
    }


}
