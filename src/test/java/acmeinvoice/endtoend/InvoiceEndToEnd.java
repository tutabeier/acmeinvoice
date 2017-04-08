package acmeinvoice.endtoend;

import acmeinvoice.Application;
import acmeinvoice.model.Address;
import acmeinvoice.model.Customer;
import acmeinvoice.model.Invoice;
import acmeinvoice.repository.InvoiceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static com.jayway.restassured.http.ContentType.JSON;
import static groovy.json.JsonOutput.toJson;
import static org.assertj.core.util.Lists.newArrayList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@TestPropertySource("/test.properties")
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest(webEnvironment = DEFINED_PORT)
public class InvoiceEndToEnd {
    @Value("${server.port}")
    private int serverPort;
    private Invoice invoice;
    private Customer customer;
    private Address address;

    @Before
    public void setUp() {
        RestAssured.port = serverPort;
        customer = new Customer();
        customer.setName("Lucas Falk Beier");
        address = new Address();
        address.setCity("Porto Alegre");
        address.setCountry("Brazil");
        address.setState("Rio Grande do Sul");
        address.setCustomer(customer);
        invoice = new Invoice();
        invoice.setCustomer(customer);
        invoice.setAddress(address);
    }

    @Test
    public void shouldSaveAndFetchAddress() throws JsonProcessingException {
        int customerId =
            given().
                    body(toJson(customer)).
                    contentType(JSON).
            when().
                    post("/v1.0/customers").
            then().
                    statusCode(CREATED.value()).
            extract().
                    path("id");
        customer.setId((long) customerId);

        int addressId =
            given().
                    body(toJson(address)).
                    contentType(JSON).
            when().
                    post("/v1.0/address").
            then().
                    statusCode(CREATED.value()).
            extract().
                    path("id");
        address.setId((long) addressId);

        given()
            .body(toJson(invoice))
            .contentType(JSON)
        .when()
            .post("/v1.0/invoices/")
        .then()
            .statusCode(CREATED.value())
            .body("[0].name", Matchers.is("Person One"))
            .body("[1].name", Matchers.is("Person Two"));
    }
}
