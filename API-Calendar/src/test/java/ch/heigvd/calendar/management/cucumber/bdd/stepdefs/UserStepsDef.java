package ch.heigvd.calendar.management.cucumber.bdd.stepdefs;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class UserStepsDef extends AbstractSteps implements En {
    private String url = "/api/app/users";

    @Given("{string} JWT Token")
    public void aValidJWTToken(String tokenStatus) {
        switch (tokenStatus){
            case "VALID":
                // to replace if expired
                token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiZXhwIjoxNjQwNTk2NTQ2LCJpYXQiOjE1Nzc1MjQ1NDZ9.J7hOkLCP-pjUXi7397Ayy6gbCWnkq-QlUYY9-h2xAZt0kN45JaAlAiCianeKaoSrepJ6fUqfe_y9IvYF-P32tQ";
                break;
            case "INVALID":
                token = "an.invalid.token";
                break;
            default:
                fail("invalid token option given");
        }
    }

    @When("user request users")
    public void userRequestWITHJWTTOKEN() {
        // Get request with JWT token
        executeGet(url, token);
    }

    @Then("request {string}")
    public void request_result(String expectedAnswer) {
        Response response = testContext().getResponse();

        switch (expectedAnswer){
            case "IS SUCCESSFUL":
                assertThat(response.getStatusCode()).isIn(200, 201);
                break;
            case "FAILS":
                assertThat(response.statusCode()).isBetween(400, 504);
                break;
            default:
                fail("Unexpected error");
        }
    }
}
