package ch.heigvd.calendar.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "user_has_calendar")
public class AccessEntity implements Serializable {

    @EmbeddedId
    private AccessIdentity id;

    private String role;

    public AccessEntity(){

    }

    public AccessEntity(AccessIdentity accessIdentity, String role) {
        this.id = accessIdentity;
        this.role = role;
    }

    public AccessIdentity getId() {
        return id;
    }

    public void setId(AccessIdentity id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccessEntity)) return false;
        AccessEntity that = (AccessEntity) o;
        return Objects.equals(id.getCalendar().getId(), that.id.getCalendar().getId()) &&
                Objects.equals(id.getUser().getEmail(), that.id.getUser().getEmail()) &&
                Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id.getUser().getEmail(), id.getCalendar().getId(), role);
    }
}
