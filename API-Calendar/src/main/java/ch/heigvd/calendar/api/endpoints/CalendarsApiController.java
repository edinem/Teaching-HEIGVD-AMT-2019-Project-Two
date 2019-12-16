package ch.heigvd.calendar.api.endpoints;

import ch.heigvd.calendar.api.CalendarsApi;
import ch.heigvd.calendar.api.model.Calendar;
import ch.heigvd.calendar.entities.CalendarEntity;
import ch.heigvd.calendar.repositories.CalendarRepository;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-07-26T19:36:34.802Z")

@Controller
public class CalendarsApiController implements CalendarsApi {

    @Autowired
    CalendarRepository calendarRepository;

    public ResponseEntity<Object> createFruit(@ApiParam(value = "", required = true) @Valid @RequestBody Calendar calendar) {
        CalendarEntity newCalendarEntity = toCalendarEntity(calendar);
        calendarRepository.save(newCalendarEntity);
        Long id = newCalendarEntity.getId();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(newCalendarEntity.getId()).toUri();

        return ResponseEntity.created(location).build();
    }


    public ResponseEntity<List<Calendar>> getFruits() {
        List<Calendar> calendars = new ArrayList<>();
        for (CalendarEntity fruitEntity : calendarRepository.findAll()) {
            calendars.add(toCalendar(fruitEntity));
        }
        /*
        Fruit staticFruit = new Fruit();
        staticFruit.setColour("red");Calen
        staticFruit.setKind("banana");
        staticFruit.setSize("medium");
        List<Fruit> fruits = new ArrayList<>();
        fruits.add(staticFruit);
        */
        return ResponseEntity.ok(calendars);
    }


    private CalendarEntity toCalendarEntity(Calendar fruit) {
        CalendarEntity entity = new CalendarEntity();
        entity.setName(fruit.getName());
        return entity;
    }

    private Calendar toCalendar(CalendarEntity entity) {
        Calendar calendar = new Calendar();
        calendar.setName(entity.getName());
        return calendar;
    }

}
