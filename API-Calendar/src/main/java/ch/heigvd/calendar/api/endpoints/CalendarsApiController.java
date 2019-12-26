package ch.heigvd.calendar.api.endpoints;

import ch.heigvd.calendar.api.CalendarsApi;
import ch.heigvd.calendar.api.model.Calendar;
import ch.heigvd.calendar.api.model.InlineObject;
import ch.heigvd.calendar.entities.CalendarEntity;
import ch.heigvd.calendar.repositories.CalendarRepository;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-07-26T19:36:34.802Z")

@Controller
public class CalendarsApiController implements CalendarsApi {

    @Autowired
    CalendarRepository calendarRepository;

    @Override
    public ResponseEntity<Object> createCalendar(@ApiParam(value = "", required = true) @Valid @RequestBody InlineObject calendar) {
        CalendarEntity newCalendarEntity = new CalendarEntity();
        newCalendarEntity.setName(calendar.getName());
        calendarRepository.save(newCalendarEntity);
        Long id = newCalendarEntity.getId();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(newCalendarEntity.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<List<Calendar>> getCalendars() {
        List<Calendar> calendars = new ArrayList<>();
        for (CalendarEntity fruitEntity : calendarRepository.findAll()) {
            calendars.add(toCalendar(fruitEntity));
        }

        return ResponseEntity.ok(calendars);
    }

    @Override
    public ResponseEntity<Void> editCalendar(@ApiParam(value = "", required = true) @Valid @RequestBody Calendar calendar) {
       CalendarEntity editedCalendarEntity = toCalendarEntity(calendar);
       calendarRepository.save(editedCalendarEntity);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(editedCalendarEntity.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<Object> getCalendarById(@ApiParam(value = "",required=true) @PathVariable("calendarId") Integer calendarId) {
        Optional<CalendarEntity> calendarRetrivied = calendarRepository.findById(calendarId.longValue());
        if(calendarRetrivied.isEmpty()) {
            return ResponseEntity.badRequest().body("ID not found");
        } else {
            return ResponseEntity.ok(calendarRetrivied);
        }
    }

    @Override
    public ResponseEntity<Object> deleteCalendar(@ApiParam(value = "",required=true) @PathVariable("calendarId") Integer calendarId) {
        Optional<CalendarEntity> calendarRetrieved = calendarRepository.findById(calendarId.longValue());
        if(calendarRetrieved.isEmpty()){
            return ResponseEntity.badRequest().body("User doesn't exist");
        }else{
            calendarRepository.delete(calendarRetrieved.get());
            Optional<CalendarEntity> userRetrievedAfterDelete = calendarRepository.findById(calendarId.longValue());
            if(userRetrievedAfterDelete.isEmpty()){
                return ResponseEntity.ok().body("User deleted");
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
            }
        }
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
