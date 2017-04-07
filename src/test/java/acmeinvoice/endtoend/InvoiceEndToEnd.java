package acmeinvoice.endtoend;

import acmeinvoice.Application;
import acmeinvoice.model.Address;
import acmeinvoice.model.Customer;
import acmeinvoice.model.Invoice;
import acmeinvoice.repository.InvoiceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.restassured.RestAssured;
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

    @Before
    public void setUp() {
//        RestAssured.port = serverPort;
//        Address address = new Address();
//        address.setCity("Porto Alegre");
//        address.setCountry("Brazil");
//        address.setNumber("600");
//        Customer customer = new Customer();
//        customer.setName("Lucas Falk Beier");
////        customer.setAddresses(newArrayList(address));
//        invoice = new Invoice();
//        invoice.setCustomer(customer);
//        invoice.setAddress(address);
    }

    @Test
    public void shouldSaveAndFetchAddress() throws JsonProcessingException {
//        given()
//                .body(toJson(invoice))
//                .contentType(JSON)
//                .when()
//                .post("/v1.0/customers")
//                .then()
//                .statusCode(CREATED.value());
//
//        when()
//                .get("/v1.0/customers")
//                .then()
//                .statusCode(OK.value());
    }
}
