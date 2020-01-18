package io.calendar.management.api.endpoints;

import com.google.common.hash.Hashing;
import com.sun.mail.iap.Response;
import io.calendar.management.api.UsersApi;
import io.calendar.management.api.model.*;
import io.calendar.management.api.util.JwtTokenUtil;
import io.calendar.management.api.util.SmtpUtil;
import io.calendar.management.entities.UserEntity;
import io.swagger.annotations.ApiParam;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import io.calendar.management.repositories.UserRepository;

import java.net.HttpCookie;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.Date;
import java.util.concurrent.TimeUnit;


@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-07-26T19:36:34.802Z")

@Controller
public class UserApiController implements UsersApi {

    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtTokenUtil jwtUtil;

    public ResponseEntity<Object> createUser(@ApiParam(value = "", required = true) @Valid @RequestBody User user) {
        //On verifie que le mail est pas déjà pris
        Optional<UserEntity> userRetrieved = userRepository.findById(user.getEmail());
        if(!userRetrieved.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        UserEntity newUserEntity = toUserEntity(user);
        newUserEntity.setPassword( Hashing.sha256()
                .hashString(user.getPassword(), StandardCharsets.UTF_8)
                .toString());
        newUserEntity.setToken("");
        userRepository.save(newUserEntity);
        String email = newUserEntity.getEmail();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(newUserEntity.getEmail()).toUri();

        return ResponseEntity.created(location).build();
    }

    public ResponseEntity<Object> updateUser(@ApiParam(value = "", required = true) @Valid @RequestBody User user) {
        UserEntity newUserEntity = toUserEntity(user);
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if(newUserEntity.getEmail().equals(userEmail)){
            userRepository.save(newUserEntity);
            String email = newUserEntity.getEmail();

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(newUserEntity.getEmail()).toUri();

            return ResponseEntity.created(location).build();
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    public ResponseEntity<List<SimpleUser>> getAllUsers() {
        List<SimpleUser> users = new ArrayList<>();
        for (UserEntity userEntity : userRepository.findAll()) {
            users.add(toSimpleUser(userEntity));
        }

        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<Object> getUser(@ApiParam(value = "",required=true) @PathVariable("userId") String userId) {

        Optional<UserEntity> userRetrieved = userRepository.findById(userId);
        if (userRetrieved.isPresent()) {
            UserWithoutPassword user = toUserWithoutPassword(userRetrieved.get());
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.badRequest().body("ID not found");
        }
    }

    @Override
    public ResponseEntity<Object> deleteUser(@ApiParam(value = "",required=true) @PathVariable("userId") String userId) {
        Optional<UserEntity> userRetrieved = userRepository.findById(userId);
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!userEmail.equals(userId)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User doesn't exist");
        }
        if (userRetrieved.isPresent()) {
            userRepository.delete(userRetrieved.get());
            Optional<UserEntity> userRetrievedAfterDelete = userRepository.findById(userId);
            if (userRetrievedAfterDelete.isPresent()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
            } else {
                return ResponseEntity.ok().body("User deleted");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User doesn't exist");
        }
    }

    @Override
    public ResponseEntity<String> authenticateUser(@ApiParam(value = "", required = true) @Valid @RequestBody LogInUser user) {
        Optional<UserEntity> userRetrieved = userRepository.findById(user.getEmail());
        String token = null;
        HttpCookie c = null;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        JSONObject jsonResp = new JSONObject();
        String passedPassword =  Hashing.sha256()
                .hashString(user.getPassword(), StandardCharsets.UTF_8)
                .toString();

        if((!userRetrieved.isEmpty()) && (passedPassword.equals(userRetrieved.get().getPassword()))){
            token = jwtUtil.generateToken(user.getEmail());
             c = new HttpCookie("myJwt", token);
            headers.add("Set-Cookie","myJwt=" + token);

            jsonResp.put("token", token);
            return ResponseEntity.status(200).header(HttpHeaders.SET_COOKIE, c.toString()).body(jsonResp.toJSONString());
        }else{
            jsonResp.put("result", "No user found or credentials are wrong");
            return ResponseEntity.status(400).headers(headers).body(jsonResp.toJSONString());
        }
    }

    @Override
    public ResponseEntity<String> getResetLink(String content) {
        Optional<UserEntity> userRetrieved = userRepository.findById(content);
        String token = null;
        String linkToSend = null;

        if((!userRetrieved.isEmpty())){
            byte[] array = new byte[7];
            new Random().nextBytes(array);
            token = new String(array, Charset.forName("UTF-8"));
            token = Base64.getEncoder().encodeToString(token.getBytes());
            token = token.replace("/","");
            token = token.replace("+", "");

            userRetrieved.get().setToken(token);
            userRetrieved.get().setTtlToken(new Date(System.currentTimeMillis()+1440*60*1000).toString());
            userRepository.save(userRetrieved.get());
            linkToSend = "http://localhost/api/management/users/password/" + token;
            String contentMail = "Please send a POST request to this address with your username and your new password : " + linkToSend;
            SmtpUtil.sendEmail(userRetrieved.get().getEmail(), contentMail);

        }


        return ResponseEntity.status(200).body("Email successfully sent");
    }

    @Override
    public ResponseEntity<String> resetPassword(String content, @Valid LogInUser user) {

        Optional<UserEntity> userRetrieved = userRepository.findById(user.getEmail());
        if(!userRetrieved.isEmpty()){
            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date d = new java.util.Date();
            String tmp =  myFormat.format(d);
            try {
                java.util.Date tmp1 = myFormat.parse(tmp);
                java.util.Date tmp2 = myFormat.parse(userRetrieved.get().getTtlToken());
                long diff = tmp2.getTime() - tmp1.getTime();
                long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                if (content.equals(userRetrieved.get().getToken()) && (days >= 0)) {
                    userRetrieved.get().setPassword( Hashing.sha256()
                            .hashString(user.getPassword(), StandardCharsets.UTF_8)
                            .toString());
                    userRetrieved.get().setToken("");
                    userRepository.save(userRetrieved.get());
                    return ResponseEntity.status(200).body("Password changed");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return ResponseEntity.status(500).body("Internal server errorr");
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

    private SimpleUser toSimpleUser(UserEntity entity) {
        SimpleUser user = new SimpleUser();
        user.setEmail(entity.getEmail());
        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());

        return user;
    }

    private UserWithoutPassword toUserWithoutPassword(UserEntity entity){
        UserWithoutPassword user = new UserWithoutPassword();
        user.setEmail(entity.getEmail());
        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());

        return user;
    }


}
