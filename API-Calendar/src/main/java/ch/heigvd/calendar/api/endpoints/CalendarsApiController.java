package ch.heigvd.calendar.api.endpoints;

import ch.heigvd.calendar.api.CalendarsApi;
import ch.heigvd.calendar.api.model.Calendar;
import ch.heigvd.calendar.api.model.InlineObject;
import ch.heigvd.calendar.entities.AccessEntity;
import ch.heigvd.calendar.entities.AccessIdentity;
import ch.heigvd.calendar.entities.CalendarEntity;
import ch.heigvd.calendar.entities.UserEntity;
import ch.heigvd.calendar.enums.Role;
import ch.heigvd.calendar.repositories.AccessRepository;
import ch.heigvd.calendar.repositories.CalendarRepository;
import ch.heigvd.calendar.repositories.UserRepository;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-07-26T19:36:34.802Z")

@Controller
public class CalendarsApiController implements CalendarsApi {

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccessRepository accessRepository;

    @Override
    public ResponseEntity<Object> createCalendar(@ApiParam(value = "", required = true) @Valid @RequestBody InlineObject calendar) {
        CalendarEntity newCalendarEntity = new CalendarEntity();
        newCalendarEntity.setName(calendar.getName());
        Long id = newCalendarEntity.getId();
        // associe le calendrier à l'utilisateur
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findById(userEmail).get();
        calendarRepository.save(newCalendarEntity);
        AccessEntity access = new AccessEntity(new AccessIdentity(newCalendarEntity, user), Role.OWNER.name());
        accessRepository.save(access);
        user.getCalendars().add(access);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(newCalendarEntity.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    public ResponseEntity<List<Calendar>> getCalendars(@Min(0) @Valid Integer offset, @Min(1) @Max(50) @Valid Integer limit) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Pageable pageable = PageRequest.of(offset, limit);
        Page<AccessEntity> accesses = accessRepository.findById_User_Email(userEmail, pageable);
        List<Calendar> calendars = new ArrayList<>();
        for (AccessEntity access : accesses.getContent()) {
            calendars.add(toCalendar(access.getId().getCalendar()));
        }

        return ResponseEntity.ok(calendars);
    }

    @Override
    public ResponseEntity<Void> editCalendar(@ApiParam(value = "", required = true) @Valid @RequestBody Calendar calendar) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<CalendarEntity> dbCalendar = calendarRepository.findById(calendar.getId().longValue());
        if(dbCalendar.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<AccessEntity> accessEntity = accessRepository.findById(new AccessIdentity(toCalendarEntity(calendar), new UserEntity(userEmail)));
        if (accessEntity.isEmpty() && accessEntity.get().getRole().equals(Role.VIEWER.name())) {
            return ResponseEntity.status(403).build();
        }

        CalendarEntity calendarEntity = dbCalendar.get();
        calendarEntity.setName(calendar.getName());
        calendarRepository.save(calendarEntity);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(calendarEntity.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<Object> getCalendarById(@ApiParam(value = "",required=true) @PathVariable("calendarId") Integer calendarId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<CalendarEntity> calendarRetrievied = calendarRepository.findById(calendarId.longValue());
        if(calendarRetrievied.isEmpty()) {
            return ResponseEntity.badRequest().body("ID not found");
        }

        if (hasRight(userEmail, calendarRetrievied.get()) == null) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(toCalendar(calendarRetrievied.get()));
    }

    @Override
    public ResponseEntity<Object> deleteCalendar(@ApiParam(value = "",required=true) @PathVariable("calendarId") Integer calendarId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<CalendarEntity> calendarRetrievied = calendarRepository.findById(calendarId.longValue());
        if(calendarRetrievied.isEmpty()) {
            return ResponseEntity.badRequest().body("ID not found");
        }

        String role = hasRight(userEmail, calendarRetrievied.get());
        if (role != null && !role.equals(Role.OWNER.name())) {
            return ResponseEntity.status(403).build();
        }

        List<AccessEntity> accessEntities = accessRepository.findById_Calendar_Id(calendarId);
        CalendarEntity calendarEntity = calendarRetrievied.get();
        for(AccessEntity access : accessEntities) {
            access.getId().getUser().getCalendars().remove(access);
            calendarEntity.getUsers().remove(access);
            accessRepository.delete(access);
        }
        calendarRepository.delete(calendarEntity);
        return ResponseEntity.ok().body("Calendar deleted");
    }

    private String hasRight(String email, CalendarEntity calendar){
        for(AccessEntity access: calendar.getUsers()){
            if(access.getId().getUser().getEmail().equals(email)){
                return access.getRole();
            }
        }
        return null;
    }

    private CalendarEntity toCalendarEntity(Calendar calendar) {
        CalendarEntity entity = new CalendarEntity();
        entity.setId(calendar.getId());
        entity.setName(calendar.getName());
        return entity;
    }

    private Calendar toCalendar(CalendarEntity entity) {
        Calendar calendar = new Calendar();
        calendar.setId((int)entity.getId());
        calendar.setName(entity.getName());
        return calendar;
    }

}