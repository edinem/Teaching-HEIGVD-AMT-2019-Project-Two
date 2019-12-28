package ch.heigvd.calendar.management.cucumber.bdd.stepdefs;

import ch.heigvd.calendar.api.model.Access;
import ch.heigvd.calendar.enums.Role;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import static org.junit.jupiter.api.Assertions.fail;

public class AccessStepdefs extends AbstractSteps implements En {
    private String calendarUrl = "/api/app/calendars";
    private Integer calendarId = 0;
    private String accessUrl = "/api/app/access";
    private String roleURL = accessUrl + "/roles";
    private String email = "";
    private String userURL = "/api/app/users";

    @When("user request roles")
    public void userRequestRoles() {
        executeGet(roleURL, token);
    }

    @Given("An accessible calendar")
    public void anAccessibleCalendar() {
        executeGet(calendarUrl, token);
        Response response = testContext().getResponse();
        String calendars = response.getBody().print();

        try {
            JSONArray myObject = new JSONArray(calendars);
            if(myObject.length() > 0) {
                JSONObject jsonObject = myObject.getJSONObject(0);
                Integer id = (Integer) jsonObject.get("id");
                calendarId = id;
            } else {
                fail("User has no calendar to access");
            }
        }catch (Exception e){
            e.printStackTrace();
            fail("Error when getting calendar");
        }
    }


    @When("user request a calendar permission")
    public void userRequestACalendarPermission() {
        executeGet(accessUrl+"/"+calendarId, token);
    }

    @And("an user other than in the token")
    public void anUserOtherThanInTheToken() {
        executeGet(userURL, token);
        Response response = testContext().getResponse();
        String calendars = response.getBody().print();

        try {
            JSONArray myObject = new JSONArray(calendars);
            for(int i = 0; i < myObject.length(); ++i){
                JSONObject jsonObject = myObject.getJSONObject(i);
                String userEmail = jsonObject.getString("email");
                if(!userEmail.equals("test@test.com")){
                    email = userEmail;
                    break;
                }
            }
            if(email.equals("")) {
                fail("There is only one user in DB");
            }
        }catch (Exception e){
            e.printStackTrace();
            fail("Error when getting users");
        }
    }

    @When("user add a permission")
    public void userAddAPermission() {
        Access access = new Access();
        access.setUser(email);
        access.setCalendar(calendarId);
        access.setRole(Role.EDITOR.name());
        testContext().setPayload(access);
        executePost(accessUrl,token);
    }

    @Given("an viewer role calendar")
    public void anViewerRoleCalendar() {

    }
}
