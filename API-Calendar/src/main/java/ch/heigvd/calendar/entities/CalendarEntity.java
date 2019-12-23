package ch.heigvd.calendar.entities;

import javax.persistence.*;
import java.io.Serializable;

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
}
