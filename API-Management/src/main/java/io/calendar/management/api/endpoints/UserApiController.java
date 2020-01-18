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
/**
 * Controller of the User API
 */
@Controller
public class UserApiController implements UsersApi {

    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtTokenUtil jwtUtil;

    /**
     * Méthode permettant de créer un utilisateur
     * @param user Objet User qui contient les informations de l'utilisateur que l'on souhaite créé.
     * @return Retourne une réponse HTTP.
     */
    public ResponseEntity<Object> createUser(@ApiParam(value = "", required = true) @Valid @RequestBody User user) {

        //On verifie si le mail est pas déjà pris
        Optional<UserEntity> userRetrieved = userRepository.findById(user.getEmail());
        if(!userRetrieved.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        //Création du nouvel utilisateur
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

    /**
     * Permet de modifier son compte utilisateur. Nécessite l'authentification.
     * @param user Objet user contenant toutes les informations de l'utilisateur que l'on souhaite mettre à jour.
     * @return Retourne une réponse HTTP.
     */
    public ResponseEntity<Object> updateUser(@ApiParam(value = "", required = true) @Valid @RequestBody User user) {
        UserEntity newUserEntity = toUserEntity(user);
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        //Verification que l'utilisateur souhaitant être modifié est bien l'utilisateur connecté
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

    /**
     * Méthode permettant de récuperer la liste des utilisateurs. Nécessite l'authentification.
     * @return Retourne une réponse HTTP.
     */
    public ResponseEntity<List<SimpleUser>> getAllUsers() {
        List<SimpleUser> users = new ArrayList<>();
        for (UserEntity userEntity : userRepository.findAll()) {
            users.add(toSimpleUser(userEntity));
        }

        return ResponseEntity.ok(users);
    }

    /**
     * Méthode permettant de récuperer les informations (firstName, lastName, email) d'un utilisateur spécifique.
     * Nécessite l'authentification.
     * @param userId l'email de l'utilisateur dont on souhaite avoir les informations.
     * @return Retourne une réponse HTTP.
     */
    @Override
    public ResponseEntity<Object> getUser(@ApiParam(value = "",required=true) @PathVariable("userId") String userId) {

        Optional<UserEntity> userRetrieved = userRepository.findById(userId);
        if (userRetrieved.isPresent()) {
            UserWithoutPassword user = toUserWithoutPassword(userRetrieved.get());
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID not found");
        }
    }

    /**
     * Méthode permettant de supprimer son utilisateur. Nécessite l'authentification.
     * @param userId Email de l'utilisateur que l'on souhaite supprimer.
     * @return Retourne une réponse HTTP.
     */
    @Override
    public ResponseEntity<Object> deleteUser(@ApiParam(value = "",required=true) @PathVariable("userId") String userId) {
        Optional<UserEntity> userRetrieved = userRepository.findById(userId);
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        //Verification que l'utilisateur souhaitant être supprimé est bien l'utilisateur connecté
        if(!userEmail.equals(userId)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("NOT AUTHORIZED");
        }
        if (userRetrieved.isPresent()) {
            userRepository.delete(userRetrieved.get());
            Optional<UserEntity> userRetrievedAfterDelete = userRepository.findById(userId);
            if (userRetrievedAfterDelete.isPresent()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("User deleted");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User doesn't exist");
        }
    }


    /**
     * Méthode permettant de s'authentifier auprès de l'API.
     * @param user Objet LogInUser contenant les identifiants
     * @return Retourne une réponse HTTP avec token.
     */
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

        //On vérifie que les identifiants sont valides.
        if((!userRetrieved.isEmpty()) && (passedPassword.equals(userRetrieved.get().getPassword()))){
            token = jwtUtil.generateToken(user.getEmail());
             c = new HttpCookie("myJwt", token);
            headers.add("Set-Cookie","myJwt=" + token);
            jsonResp.put("token", token);

            return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, c.toString()).body(jsonResp.toJSONString());
        }else{
            jsonResp.put("result", "No user found or credentials are wrong");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(jsonResp.toJSONString());
        }
    }


    /**
     * Méthode permettant de recevoir un mail avec un lien afin de réinitialiser son mot de passe.
     * @param content Email du compte dont on veut réinitialiser le mot de passe.
     * @return Retourne une réponse HTTP.
     */
    @Override
    public ResponseEntity<String> getResetLink(String content) {
        Optional<UserEntity> userRetrieved = userRepository.findById(content);
        String token = null;
        String linkToSend = null;

        try{
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
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }

        return ResponseEntity.status(200).body("Email successfully sent");
    }

    /**
     * Méthode permettant de réinitialiser le mot de passe d'un compte
     * @param content Token permettant la réinitialisation du mot de passe
     * @param user Objet LogInUser contenant l'adresse mail et le nouveau mot de passe.
     * @return Retourne une réponse HTTP.
     */
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

    /**
     * Méthode permettant de passer d'un objet User à UserEntity.
     * @param user Objet user contenant les informations
     * @return Le nouvel objet UserEntity.
     */
    private UserEntity toUserEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setEmail(user.getEmail());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setPassword(user.getPassword());

        return entity;
    }

    /**
     * Méthode permettant de passer d'un objet UserEntity à User
     * @param entity Objet UserEntity contenant les informations.
     * @return Le nouvel objet User
     */
    private User toUser(UserEntity entity) {
        User user = new User();
        user.setEmail(entity.getEmail());
        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());
        user.setPassword(entity.getPassword());

        return user;
    }

    /**
     * Méthode permettant de passer d'un objet UserEntity à SimpleUser
     * @param entity Objet UserEntity contenant les informations.
     * @return Le nouvel objet SimpleUser.
     */
    private SimpleUser toSimpleUser(UserEntity entity) {
        SimpleUser user = new SimpleUser();
        user.setEmail(entity.getEmail());
        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());

        return user;
    }

    /**
     * Méthode permettant de passer d'un objet UserEntity à UserWithoutPassword
     * @param entity Objet UserEntity contenant les informations.
     * @return Le nouvel objet UserWithoutPassword.
     */
    private UserWithoutPassword toUserWithoutPassword(UserEntity entity){
        UserWithoutPassword user = new UserWithoutPassword();
        user.setEmail(entity.getEmail());
        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());

        return user;
    }


}
