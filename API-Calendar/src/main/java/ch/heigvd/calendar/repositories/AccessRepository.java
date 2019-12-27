package ch.heigvd.calendar.repositories;

import ch.heigvd.calendar.entities.AccessEntity;
import ch.heigvd.calendar.entities.AccessIdentity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccessRepository extends CrudRepository<AccessEntity, AccessIdentity> {
    List<AccessEntity> findById_Calendar_Id(long id);
    AccessEntity findById_Calendar_IdAndId_User_Email(long id, String email);
}
