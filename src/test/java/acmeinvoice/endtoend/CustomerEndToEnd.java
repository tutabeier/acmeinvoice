package acmeinvoice.endtoend;

import acmeinvoice.Application;
import acmeinvoice.model.Address;
import acmeinvoice.model.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.restassured.RestAssured;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static acmeinvoice.common.JsonUtil.toJson;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.assertj.core.util.Lists.newArrayList;
import static org.hamcrest.Matchers.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@TestPropertySource("/test.properties")
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest(webEnvironment = DEFINED_PORT)
public class CustomerEndToEnd {
    @Value("${server.port}")
    private int serverPort;
    private Customer customerOne;
    private Customer customerTwo;

    @Before
    public void setUp() {
        RestAssured.port = serverPort;

        customerOne = new Customer();
        customerOne.setName("Person One");
        Address addressOne = new Address();
        addressOne.setCity("Porto Alegre");
        addressOne.setState("Rio Grande do Sul");
        addressOne.setCountry("Brazil");
        Address addressTwo = new Address();
        addressTwo.setCity("São Paulo");
        addressTwo.setState("São Paulo");
        addressTwo.setCountry("Brazil");
        addressTwo.setCustomer(customerOne);


        customerTwo = new Customer();
        customerTwo.setName("Person Two");
        Address addressThree = new Address();
        addressThree.setCity("Rio de Janeiro");
        addressThree.setState("Rio de Janeiro");
        addressThree.setCountry("Brazil");
        addressThree.setCustomer(customerTwo);
    }

    @Test
    @Ignore(value = "Flaky test")
    public void shouldSaveAndFetchCustomer() throws JsonProcessingException {
        given()
            .body(toJson(customerOne))
            .contentType(JSON)
        .when()
            .post("/v1.0/customers")
        .then()
            .statusCode(CREATED.value())
            .body("name", is("Person One"));

        given()
            .body(toJson(customerTwo))
            .contentType(JSON)
        .when()
            .post("/v1.0/customers")
        .then()
            .statusCode(CREATED.value())
            .body("name", is("Person Two"));

        when()
            .get("/v1.0/customers")
        .then()
            .statusCode(OK.value())
            .body("[0].name", is("Person One"))
            .body("[1].name", is("Person Two"));
    }
}
