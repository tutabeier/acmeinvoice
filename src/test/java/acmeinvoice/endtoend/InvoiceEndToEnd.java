package acmeinvoice.endtoend;

import acmeinvoice.Application;
import acmeinvoice.model.Address;
import acmeinvoice.model.Customer;
import acmeinvoice.model.InvoiceResponse;
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
import static java.time.LocalDate.parse;
import static org.hamcrest.Matchers.is;
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
    private InvoiceResponse invoiceResponseOne;
    private InvoiceResponse invoiceResponseTwo;
    private InvoiceResponse invoiceResponseThree;
    private Customer customer;
    private Address addressOne;
    private Address addressTwo;

    @Before
    public void setUp() {
        RestAssured.port = serverPort;
        customer = new Customer();
        customer.setName("Lucas Falk Beier");
        addressOne = new Address();
        addressOne.setCity("Porto Alegre");
        addressOne.setState("Rio Grande do Sul");
        addressOne.setCountry("Brazil");
        addressOne.setCustomer(customer);
        addressTwo = new Address();
        addressTwo.setCity("São Paulo");
        addressTwo.setState("São Paulo");
        addressTwo.setCountry("Brazil");
        addressTwo.setCustomer(customer);
        invoiceResponseOne = new InvoiceResponse(0L, 0L, 0L, "AdvancePayment",
                "Voorschot", parse("2015-02-13"), parse("2015-02-20"), "157005888",
                parse("2015-03-01"), parse("2015-04-01"), 165.29f,34.71f);
        invoiceResponseTwo = new InvoiceResponse(0L, 0L, 0L, "AdvancePayment",
                "Voorschot", parse("2014-11-13"), parse("2014-11-20"), "1429564",
                parse("2014-12-01"), parse("2015-01-01"), 165.29f,34.71f);
        invoiceResponseThree = new InvoiceResponse(0L, 0L, 0L, "AdvancePayment",
                "Voorschot", parse("2014-11-13"), parse("2014-11-20"), "1429565",
                parse("2014-12-01"), parse("2015-01-01"), 165.29f,34.71f);
    }

    @Test
    public void shouldFindInvoiceByCustomerId() throws JsonProcessingException {
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

        int addressIdOne =
                given().
                    body(toJson(addressOne)).
                    contentType(JSON).
                when().
                    post("/v1.0/address").
                then().
                    statusCode(CREATED.value()).
                extract().
                    path("id");

        int addressIdTwo =
                given().
                    body(toJson(addressOne)).
                    contentType(JSON).
                when().
                    post("/v1.0/address").
                then().
                    statusCode(CREATED.value()).
                extract().
                    path("id");

        invoiceResponseOne.setCustomerId(customerId);
        invoiceResponseOne.setAddressId(addressIdOne);
        invoiceResponseTwo.setCustomerId(customerId);
        invoiceResponseTwo.setAddressId(addressIdOne);
        invoiceResponseThree.setCustomerId(customerId);
        invoiceResponseThree.setAddressId(addressIdTwo);

        given()
            .body(toJson(invoiceResponseOne))
            .contentType(JSON)
        .when()
            .post("/v1.0/invoices/")
        .then()
            .statusCode(CREATED.value());

        given()
            .body(toJson(invoiceResponseTwo))
            .contentType(JSON)
        .when()
            .post("/v1.0/invoices/")
        .then()
            .statusCode(CREATED.value());

        given()
            .body(toJson(invoiceResponseThree))
            .contentType(JSON)
        .when()
            .post("/v1.0/invoices/")
        .then()
            .statusCode(CREATED.value());

        when()
            .get("/v1.0/invoices/?customerId=" + customerId)
        .then()
            .statusCode(OK.value())
            .body("[0].invoiceNumber", is("157005888"))
            .body("[1].invoiceNumber", is("1429564"))
            .body("[2].invoiceNumber", is("1429565"))
            .body("list.size()", is(3));
    }

    @Test
    public void shouldFindInvoiceByCustomerIdAndAddressId() throws JsonProcessingException {
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

        int addressIdOne =
            given().
                    body(toJson(addressOne)).
                    contentType(JSON).
            when().
                    post("/v1.0/address").
            then().
                    statusCode(CREATED.value()).
            extract().
                    path("id");

        int addressIdTwo =
            given().
                    body(toJson(addressOne)).
                    contentType(JSON).
            when().
                    post("/v1.0/address").
            then().
                    statusCode(CREATED.value()).
            extract().
                    path("id");

        invoiceResponseOne.setCustomerId(customerId);
        invoiceResponseOne.setAddressId(addressIdOne);
        invoiceResponseTwo.setCustomerId(customerId);
        invoiceResponseTwo.setAddressId(addressIdOne);
        invoiceResponseThree.setCustomerId(customerId);
        invoiceResponseThree.setAddressId(addressIdTwo);

        given()
            .body(toJson(invoiceResponseOne))
            .contentType(JSON)
        .when()
            .post("/v1.0/invoices/")
        .then()
            .statusCode(CREATED.value());

        given()
            .body(toJson(invoiceResponseTwo))
            .contentType(JSON)
        .when()
            .post("/v1.0/invoices/")
        .then()
            .statusCode(CREATED.value());

        given()
                .body(toJson(invoiceResponseThree))
                .contentType(JSON)
        .when()
                .post("/v1.0/invoices/")
        .then()
            .statusCode(CREATED.value());

        when()
            .get("/v1.0/invoices/?customerId=" + customerId + "&addressId=" +  addressIdOne)
        .then()
            .statusCode(OK.value())
            .body("[0].invoiceNumber", is("157005888"))
            .body("[1].invoiceNumber", is("1429564"))
            .body("list.size()", is(2));
    }

    @Test
    public void shouldSaveInvoice() throws Exception {
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

        int addressId =
                given().
                        body(toJson(addressOne)).
                        contentType(JSON).
                when().
                        post("/v1.0/address").
                then().
                        statusCode(CREATED.value()).
                extract().
                        path("id");

        invoiceResponseOne.setCustomerId(customerId);
        invoiceResponseOne.setAddressId(addressId);

        given()
                .body(toJson(invoiceResponseOne))
                .contentType(JSON)
        .when()
                .post("/v1.0/invoices/")
        .then()
                .statusCode(CREATED.value())
                .body("customerId", is(customerId))
                .body("addressId", is(addressId))
                .body("invoiceType", is(invoiceResponseOne.getInvoiceType()))
                .body("invoiceTypeLocalized", is(invoiceResponseOne.getInvoiceTypeLocalized()))
                .body("invoiceDate", is(invoiceResponseOne.getInvoiceDate().toString()))
                .body("paymentDueDate", is(invoiceResponseOne.getPaymentDueDate().toString()))
                .body("invoiceNumber", is(invoiceResponseOne.getInvoiceNumber()))
                .body("startDate", is(invoiceResponseOne.getStartDate().toString()))
                .body("endDate", is(invoiceResponseOne.getEndDate().toString()))
                .body("periodDescription", is(invoiceResponseOne.getPeriodDescription()))
                .body("amount", is(invoiceResponseOne.getAmount()))
                .body("vatAmount", is(invoiceResponseOne.getVatAmount()))
                .body("totalAmount", is(invoiceResponseOne.getTotalAmount()));


    }
}
