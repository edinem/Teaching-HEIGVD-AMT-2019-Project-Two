package io.calendar.management.api.endpoints;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.calendar.management.api.UserApi;
import io.calendar.management.api.model.User;
import io.calendar.management.entities.UserEntity;
import io.swagger.annotations.ApiParam;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import io.calendar.management.repositories.UserRepository;

import java.net.HttpCookie;
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
        if (userRetrieved.isPresent()) {
            User user = toUser(userRetrieved.get());
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.badRequest().body("ID not found");
        }
    }

    @Override
    public ResponseEntity<Object> deleteUser(@ApiParam(value = "",required=true) @PathVariable("userId") String userId) {
        Optional<UserEntity> userRetrieved = userRepository.findById(userId);
        if (userRetrieved.isPresent()) {
            userRepository.delete(userRetrieved.get());
            Optional<UserEntity> userRetrievedAfterDelete = userRepository.findById(userId);
            if (userRetrievedAfterDelete.isPresent()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
            } else {
                return ResponseEntity.ok().body("User deleted");
            }
        } else {
            return ResponseEntity.badRequest().body("User doesn't exist");
        }
    }

    @Override
    public ResponseEntity<String> authenticateUser(@ApiParam(value = "", required = true) @Valid @RequestBody User user) {
        Algorithm algorithm = Algorithm.HMAC256("secret");
        Optional<UserEntity> userRetrieved = userRepository.findById(user.getEmail());
        String token = null;
        HttpCookie c = null;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        JSONObject jsonResp = new JSONObject();
        System.out.println("DEBUg" + userRetrieved);
        if((!userRetrieved.isEmpty()) && (userRetrieved.get().getPassword().equals(user.getPassword()))){
            token = JWT.create()
                    .withIssuer("auth0").withClaim("email", user.getEmail()).sign(algorithm);
             c = new HttpCookie("myJwt", token);
            headers.add("Set-Cookie","myJwt=" + token);

            jsonResp.put("result", token);
            return ResponseEntity.status(200).header(HttpHeaders.SET_COOKIE, c.toString()).body(jsonResp.toJSONString());
        }else{
            jsonResp.put("result", "No user found or credentials are wrong");
            return ResponseEntity.status(400).headers(headers).body(jsonResp.toJSONString());
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
