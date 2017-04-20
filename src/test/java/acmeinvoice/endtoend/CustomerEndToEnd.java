package acmeinvoice.endtoend;

import acmeinvoice.Application;
import acmeinvoice.model.Address;
import acmeinvoice.model.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
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
import static acmeinvoice.model.Address.builder;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;
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

        customerOne = Customer.builder().name("Person One").build();
        Address addressOne = builder()
                .city("Porto Alegre")
                .state("Rio Grande do Sul")
                .country("Brazil")
                .customer(customerOne)
                .build();
        Address addressTwo = builder()
                .city("São Paulo")
                .state("São Paulo")
                .country("Brazil")
                .customer(customerOne)
                .build();


        customerOne = Customer.builder().name("Person Two").build();

        Address addressThree = builder()
                .city("Rio de Janeiro")
                .state("Rio de Janeiro")
                .country("Brazil")
                .customer(customerTwo)
                .build();
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
