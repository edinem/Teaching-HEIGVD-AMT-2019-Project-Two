package ch.heigvd.calendar.management.cucumber.bdd.stepdefs;

import ch.heigvd.calendar.api.model.Calendar;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import io.cucumber.datatable.DataTable;

import java.util.List;

public class CalendarStepdefs extends AbstractSteps implements En {
    private String calendarUrl = "/api/app/calendars";

    @When("user request calendars")
    public void userRequestCalendars() {
        executeGet(calendarUrl, token);
    }

    @Given("user send calendar name")
    public void userSendCalendarName(DataTable calendarDT) {
        testContext().reset();
        List<Calendar> calendarList = calendarDT.asList(Calendar.class);
        testContext().setPayload(calendarList.get(0));
    }

    @When("user create a new calendar")
    public void userCreateANewCalendar() {
        executePost(calendarUrl, token);
    }
}
