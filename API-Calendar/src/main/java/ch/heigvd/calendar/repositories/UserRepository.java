package ch.heigvd.calendar.repositories;

import ch.heigvd.calendar.entities.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Daniel Oliveira Paiva on 16/12/19.
 */
public interface UserRepository extends PagingAndSortingRepository<UserEntity, String> {

}
