package io.calendar.management.cucumber.bdd.stepdefs;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


import io.calendar.management.api.model.User;
import io.calendar.management.repositories.UserRepository;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import cucumber.api.java8.En;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class TestSteps extends AbstractSteps implements En {
    private String loginURL = "/api/management/users/authentication/";
    private String baseURL = "/api/management/users";

    @Autowired
    private UserRepository userRepository;

    private Map<String, String> token = null;

    private String tokenUsertest = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QudGVzdCIsImV4cCI6MTY0MTk5NjQ1OSwiaWF0IjoxNTc4OTI0NDU5fQ._fRuHlJoEwvuXKWYO2lqsU_AoqgIB3wfKcqqLKiRUAkkddY78WmbaennVySY9sIAD12eSs0vWKg37W11RRlBWw";
    private String userId = "";


    public TestSteps() {
        /** Login **/
        Given("user send his informations", (DataTable userDt) -> {
            // initial request, reset context
            testContext().reset();

            List<User> userList = userDt.asList(User.class);
            super.testContext().setPayload(userList.get(0));
        });
        When("user login {string}", (String testContext) -> executePost(loginURL));
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

        /** Register **/
        Given("user send his informations to register", (DataTable userDt) ->{
            testContext().reset();
            List<User> userList = userDt.asList(User.class);
            super.testContext().setPayload(userList.get(0));
        });
        When("user register {string}",(String testContext) -> executePost(baseURL));
        Then("the registration {string}", (String expectedResult)  -> {
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

        /** General **/

        When("non-authenticated user try to get user list", () -> executeGet(baseURL, ""));

        When("authenticated user try to get user list", () -> {
            executeGet(baseURL, tokenUsertest);
        });

        When("authenticated user try to delete an account", () -> {
            executeDelete(baseURL + "/" + userId, tokenUsertest);
        });

        When("^authenticated user try to get user information$", () -> {
            executeGet(baseURL + "/" + userId, tokenUsertest);
        });

        When("non-authenticated user try to get user information", () -> {
            executeGet(baseURL + "/" + userId, "");
        });

        When("authenticated user try to update an account", () -> {
            executePut(baseURL, token);
        });

        Given("the user id to get", (DataTable dataTable) -> {
            testContext().reset();
            List<String> userList = dataTable.asList(String.class);
            this.userId = userList.get(1);
        });

        Given("the user id to delete", (DataTable dataTable) -> {
            testContext().reset();
            List<String> userList = dataTable.asList(String.class);
            this.userId = userList.get(1);
        });

        Given("the user informations to update", (DataTable dataTable) -> {
            testContext().reset();
            List<User> userList = dataTable.asList(User.class);
            super.testContext().setPayload(userList.get(0));
        });




        Then("the authenticated action {string}", (String expectedResult) ->{
            Response response = testContext().getResponse();

            switch (expectedResult) {
                case "IS SUCCESSFUL":
                    assertThat(response.statusCode()).isIn(200, 201);
                    break;
                case "FAILS":
                    assertThat(response.statusCode()).isIn(401);
                    break;
                default:
                    fail("Unexpected error");
            }
        });
    }
}