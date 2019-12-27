package ch.heigvd.calendar.entities;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class AccessIdentity implements Serializable {

    @ManyToOne
    @JoinColumn(name = "calendar_id")
    private CalendarEntity calendar;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public AccessIdentity(){

    }

    public AccessIdentity(CalendarEntity calendar, UserEntity user){
        this.calendar = calendar;
        this.user = user;
    }

    public UserEntity getUser() {
        return user;
    }

    public CalendarEntity getCalendar() {
        return calendar;
    }

    public void setCalendar(CalendarEntity calendar) {
        this.calendar = calendar;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
