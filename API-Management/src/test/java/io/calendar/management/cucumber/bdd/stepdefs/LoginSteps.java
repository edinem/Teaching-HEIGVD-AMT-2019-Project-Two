package io.calendar.management.cucumber.bdd.stepdefs;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import io.calendar.management.api.model.User;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;

import java.util.List;

import cucumber.api.java8.En;

public class LoginSteps extends AbstractSteps implements En {
    private String loginUrl = "/api/management/user/authentication/";

    public LoginSteps() {
        Given("user send his informations", (DataTable userDt) -> {
            // initial request, reset context
            testContext().reset();

            List<User> userList = userDt.asList(User.class);
            super.testContext().setPayload(userList.get(0));
        });
        When("user login {string}", (String testContext) -> executePost(loginUrl));
        Then("the login {string}", (String expectedResult) -> {
            Response response = testContext().getResponse();

            switch (expectedResult) {
                case "IS SUCCESSFUL":
                    assertThat(response.statusCode()).isIn(200, 201);
                    break;
                case "FAILS":
                    assertThat(response.statusCode()).isBetween(400, 504);
                    break;
                default:
                    fail("Unexpected error");
            }
        });
    }
}