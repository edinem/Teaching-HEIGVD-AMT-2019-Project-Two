package ch.heigvd.calendar.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Daniel Oliveira Paiva on 16/12/19.
 */
@Entity
@Table(name = "user")
public class UserEntity {
    @Id
    private String email;

    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE},mappedBy = "id.user", orphanRemoval = true)
    private Set<AccessEntity> accesses = new HashSet<>();

    public UserEntity(){

    }

    public UserEntity(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<AccessEntity> getCalendars() {
        return accesses;
    }

    public void setCalendars(Set<AccessEntity> calendars) {
        this.accesses = calendars;
    }
}
