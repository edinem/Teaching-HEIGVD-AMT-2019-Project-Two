package io.calendar.management.service;

import io.calendar.management.entities.UserEntity;
import io.calendar.management.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> userRetrived = userRepository.findById(email);
        UserEntity user = null;
        if (userRetrived.isEmpty()) {
            user = this.save(email);
        } else {
            user = userRetrived.get();
        }
        return new org.springframework.security.core.userdetails.User(email, "",
                new ArrayList<>());
    }

    private UserEntity save(String email) {
        UserEntity newUser = new UserEntity();
        newUser.setEmail(email);
        return userRepository.save(newUser);
    }

}