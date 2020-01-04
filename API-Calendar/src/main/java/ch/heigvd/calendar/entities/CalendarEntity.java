package ch.heigvd.calendar.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Daniel Oliveira Paiva on 16/12/19.
 */
@Entity
@Table(name = "calendar")
public class CalendarEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY,
                cascade = { CascadeType.PERSIST, CascadeType.MERGE},
                mappedBy = "id.calendar",
                orphanRemoval = true)
    private Set<AccessEntity> accesses = new HashSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<AccessEntity> getUsers() {
        return accesses;
    }

    public void setUsers(Set<AccessEntity> users) {
        this.accesses = users;
    }
}
