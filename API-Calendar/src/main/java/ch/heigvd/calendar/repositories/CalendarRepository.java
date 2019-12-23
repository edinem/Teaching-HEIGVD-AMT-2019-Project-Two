package ch.heigvd.calendar.repositories;

import ch.heigvd.calendar.entities.CalendarEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Daniel Oliveira Paiva on 16/12/19.
 */
public interface CalendarRepository extends CrudRepository<CalendarEntity, Long>{

}
