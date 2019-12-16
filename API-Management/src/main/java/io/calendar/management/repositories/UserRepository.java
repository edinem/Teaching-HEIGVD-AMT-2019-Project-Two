package io.calendar.management.repositories;

import io.calendar.management.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;



/**
 * Created by Olivier Liechti on 26/07/17.
 */
public interface UserRepository extends CrudRepository<UserEntity, String>{

}
