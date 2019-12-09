package ch.heigvd.calendar.repositories;

import ch.heigvd.calendar.entities.CalendarEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Olivier Liechti on 26/07/17.
 */
public interface CalendarRepository extends CrudRepository<CalendarEntity, Long>{

}
