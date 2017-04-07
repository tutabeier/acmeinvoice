package acmeinvoice.endtoend;

import acmeinvoice.Application;
import acmeinvoice.model.Address;
import acmeinvoice.model.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.restassured.RestAssured;
import org.junit.Before;
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

        Address addressOne = new Address();
        addressOne.setCity("Porto Alegre");
        addressOne.setState("Rio Grande do Sul");
        addressOne.setCountry("Brazil");
        Address addressTwo = new Address();
        addressTwo.setCity("São Paulo");
        addressTwo.setState("São Paulo");
        addressTwo.setCountry("Brazil");
        customerOne = new Customer();
        customerOne.setName("Person One");
        customerOne.setAddresses(newArrayList(addressOne, addressTwo));


        Address addressThree = new Address();
        addressThree.setCity("Rio de Janeiro");
        addressThree.setState("Rio de Janeiro");
        addressThree.setCountry("Brazil");
        customerTwo = new Customer();
        customerTwo.setName("Person Two");
        customerTwo.setAddresses(newArrayList(addressThree));
    }

    @Test
    public void shouldSaveAndFetchCustomer() throws JsonProcessingException {
        given()
            .body(toJson(customerOne))
            .contentType(JSON)
        .when()
            .post("/v1.0/customers")
        .then()
            .statusCode(CREATED.value())
            .body("name", is("Person One"))
            .body("addresses[0].city", is("Porto Alegre"))
            .body("addresses[0].state", is("Rio Grande do Sul"))
            .body("addresses[0].country", is("Brazil"))
            .body("addresses[1].city", is("São Paulo"))
            .body("addresses[1].state", is("São Paulo"))
            .body("addresses[1].country", is("Brazil"));

        given()
            .body(toJson(customerTwo))
            .contentType(JSON)
        .when()
            .post("/v1.0/customers")
        .then()
            .statusCode(CREATED.value())
            .body("name", is("Person Two"))
            .body("addresses[0].city", is("Rio de Janeiro"))
            .body("addresses[0].state", is("Rio de Janeiro"))
            .body("addresses[0].country", is("Brazil"));

        when()
            .get("/v1.0/customers")
        .then()
            .statusCode(OK.value())
            .body("[0].name", is("Person One"))
            .body("[0].addresses[0].city", is("Porto Alegre"))
            .body("[0].addresses[0].state", is("Rio Grande do Sul"))
            .body("[0].addresses[0].country", is("Brazil"))
            .body("[0].addresses[1].city", is("São Paulo"))
            .body("[0].addresses[1].state", is("São Paulo"))
            .body("[0].addresses[1].country", is("Brazil"))
            .body("[1].name", is("Person Two"))
            .body("[1].addresses[0].city", is("Rio de Janeiro"))
            .body("[1].addresses[0].state", is("Rio de Janeiro"))
            .body("[1].addresses[0].country", is("Brazil"));
    }
}
