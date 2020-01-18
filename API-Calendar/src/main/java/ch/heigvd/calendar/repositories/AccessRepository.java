package ch.heigvd.calendar.repositories;

import ch.heigvd.calendar.entities.AccessEntity;
import ch.heigvd.calendar.entities.AccessIdentity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AccessRepository extends PagingAndSortingRepository<AccessEntity, AccessIdentity> {
    List<AccessEntity> findById_Calendar_Id(long id);
    AccessEntity findById_Calendar_IdAndId_User_Email(long id, String email);
    Page<AccessEntity> findById_User_Email(String email, Pageable pageable);
}
