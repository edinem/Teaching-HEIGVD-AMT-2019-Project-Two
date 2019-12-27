package ch.heigvd.calendar.api.endpoints;

import ch.heigvd.calendar.api.AccessApi;
import ch.heigvd.calendar.api.model.Access;
import ch.heigvd.calendar.entities.AccessEntity;
import ch.heigvd.calendar.entities.AccessIdentity;
import ch.heigvd.calendar.entities.UserEntity;
import ch.heigvd.calendar.enums.Role;
import ch.heigvd.calendar.repositories.AccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class AccessApiController implements AccessApi {

    @Autowired
    private AccessRepository accessRepository;

    @Override
    public ResponseEntity<Access> addAccess(@Valid Access access) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        AccessEntity accessEntity = accessRepository.findById_Calendar_IdAndId_User_Email(access.getCalendar().longValue(), userEmail);
        if(accessEntity == null || accessEntity.getRole().equals(Role.VIEWER)){
            return ResponseEntity.status(403).build();
        }
        if(!Role.contains(access.getRole())){
            return ResponseEntity.status(400).build();
        }

        AccessEntity newAccess = new AccessEntity(new AccessIdentity(accessEntity.getId().getCalendar(), new UserEntity(userEmail)), access.getRole());
        accessRepository.save(newAccess);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().build().toUri();

        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<Void> deleteAccess(@Valid Access access) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        AccessEntity accessEntity = accessRepository.findById_Calendar_IdAndId_User_Email(access.getCalendar().longValue(), userEmail);
        if(accessEntity == null || accessEntity.getRole().equals(Role.VIEWER)){
            return ResponseEntity.status(403).build();
        }

        AccessEntity accessDeleted = accessRepository.findById_Calendar_IdAndId_User_Email(access.getCalendar().longValue(), access.getUser());
        accessRepository.delete(accessDeleted);

        accessDeleted = accessRepository.findById_Calendar_IdAndId_User_Email(access.getCalendar().longValue(), access.getUser());
        if(accessDeleted != null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<Access>> getAccess(@Valid Integer calendarId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean hasAccess = false;
        List<Access> accesses = new ArrayList<>();
        List<AccessEntity> accessEntities = accessRepository.findById_Calendar_Id(calendarId.longValue());

        for(AccessEntity access: accessEntities) {
            accesses.add(toAccess(access));
            if(access.getId().getUser().getEmail().equals(userEmail)){
                hasAccess = true;
            }
        }

        if(!hasAccess) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(accesses);
    }

    @Override
    public ResponseEntity<List<String>> getRoles() {
        List<Role> roles = Arrays.asList(Role.values());
        List<String> rolesString = new ArrayList<>();
        for(Role role : roles) {
            rolesString.add(role.name());
        }

        return ResponseEntity.ok(rolesString);
    }

    private Access toAccess(AccessEntity accessEntity) {
        Access access = new Access();
        access.setCalendar((int)accessEntity.getId().getCalendar().getId());
        access.setUser(accessEntity.getId().getUser().getEmail());
        access.setRole(accessEntity.getRole());
        return access;
    }
}
