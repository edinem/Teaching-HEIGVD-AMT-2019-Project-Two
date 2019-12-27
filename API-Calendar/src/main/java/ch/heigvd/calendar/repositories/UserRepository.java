package ch.heigvd.calendar.repositories;

import ch.heigvd.calendar.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Daniel Oliveira Paiva on 16/12/19.
 */
public interface UserRepository extends CrudRepository<UserEntity, String>{

}
