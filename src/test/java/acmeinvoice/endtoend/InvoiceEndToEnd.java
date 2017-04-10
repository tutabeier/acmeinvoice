package acmeinvoice.endtoend;

import acmeinvoice.Application;
import acmeinvoice.model.Address;
import acmeinvoice.model.Customer;
import acmeinvoice.model.InvoiceResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import org.junit.Before;
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
import static java.lang.Long.valueOf;
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
    private InvoiceResponse paymentInvoiceForCustomerOneAndAddressOne;
    private InvoiceResponse shopInvoiceForCustomerOneAndAddressTwo;
    private InvoiceResponse paymentInvoiceForCustomerTwoAndAddressThree;
    private InvoiceResponse shopInvoiceForCustomerTwoAndAddressOne;
    private int customeOneId;
    private int customerTwoId;
    private int addressOneId;
    private int addressTwoId;
    private int addressThreeId;


    @Before
    public void setUp() throws JsonProcessingException {
        RestAssured.port = serverPort;

        setupCustomersAndAddresses();

        paymentInvoiceForCustomerOneAndAddressOne = new InvoiceResponse(customeOneId, addressOneId, 0L, "AdvancePayment",
                "Voorschot", parse("2015-02-13"), parse("2015-02-20"), "010101",
                parse("2015-03-01"), parse("2015-04-01"), 165.29f,34.71f);

        shopInvoiceForCustomerOneAndAddressTwo = new InvoiceResponse(customeOneId, addressTwoId, 0L, "ShopPurchase",
                "Voorschot", parse("2014-11-13"), parse("2014-11-20"), "020202",
                parse("2014-12-01"), parse("2015-01-01"), 165.29f,34.71f);

        paymentInvoiceForCustomerTwoAndAddressThree = new InvoiceResponse(customerTwoId, addressThreeId, 0L, "AdvancePayment",
                "Voorschot", parse("2014-11-13"), parse("2014-11-20"), "030303",
                parse("2014-12-01"), parse("2015-01-01"), 165.29f,34.71f);

        shopInvoiceForCustomerTwoAndAddressOne = new InvoiceResponse(customerTwoId, addressOneId, 0L, "ShopPurchase",
                "Voorschot", parse("2014-11-13"), parse("2014-11-20"), "040404",
                parse("2014-12-01"), parse("2015-01-01"), 165.29f,34.71f);
    }

    @Test
    public void shouldSaveAndFindInvoicesCombiningMultipleFilters() throws JsonProcessingException {
        given()
            .body(toJson(paymentInvoiceForCustomerOneAndAddressOne))
            .contentType(JSON)
        .when()
            .post("/v1.0/invoices/")
        .then()
            .statusCode(CREATED.value());

        given()
            .body(toJson(shopInvoiceForCustomerOneAndAddressTwo))
            .contentType(JSON)
        .when()
            .post("/v1.0/invoices/")
        .then()
            .statusCode(CREATED.value());

        given()
            .body(toJson(paymentInvoiceForCustomerTwoAndAddressThree))
            .contentType(JSON)
        .when()
            .post("/v1.0/invoices/")
        .then()
            .statusCode(CREATED.value());

        given()
            .body(toJson(shopInvoiceForCustomerTwoAndAddressOne))
            .contentType(JSON)
        .when()
            .post("/v1.0/invoices/")
        .then()
            .statusCode(CREATED.value());

        when()
            .get("/v1.0/invoices/?customerId=" + customeOneId)
        .then()
            .statusCode(OK.value())
            .body("[0].invoiceNumber", is("010101"))
            .body("[1].invoiceNumber", is("020202"))
            .body("list.size()", is(2));

        given().
            pathParam("customerId", customeOneId).
            pathParam("filter", "shop").
        when().
            get("/v1.0/invoices/?customerId={customerId}&filter={filter}").
        then().
            statusCode(OK.value()).
            body("[0].invoiceNumber", is("020202")).
            body("list.size()", is(1));

        given().
            pathParam("addressId", addressOneId).
        when().
            get("/v1.0/invoices/?addressId={addressId}").
        then().
            statusCode(OK.value()).
            body("[0].invoiceNumber", is("010101")).
            body("[1].invoiceNumber", is("040404")).
            body("list.size()", is(2));

        given().
            pathParam("addressId", addressOneId).
        when().
            get("/v1.0/invoices/?addressId={addressId}").
        then().
            statusCode(OK.value()).
            body("[0].invoiceNumber", is("010101")).
            body("[1].invoiceNumber", is("040404")).
            body("list.size()", is(2));

        given().
            pathParam("customerId", customeOneId).
            pathParam("month", 3).
        when().
            get("/v1.0/invoices/?customerId={customerId}&month={month}").
        then().
            statusCode(OK.value()).
            body("[0].invoiceNumber", is("010101")).
            body("list.size()", is(1));
    }

    private void setupCustomersAndAddresses() throws JsonProcessingException {
        Customer customerOne = new Customer();
        customerOne.setName("Person One");
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

        customeOneId =
                given().
                    body(toJson(customerOne)).
                    contentType(JSON).
                when().
                    post("/v1.0/customers").
                then().
                    statusCode(CREATED.value()).
                extract().
                    path("id");
        customerOne.setId(valueOf(customeOneId));

        addressOneId =
                given().
                    body(toJson(addressOne)).
                    contentType(JSON).
                when().
                    post("/v1.0/address").
                then().
                    statusCode(CREATED.value()).
                extract().
                    path("id");

        addressTwoId =
                given().
                    body(toJson(addressTwo)).
                    contentType(JSON).
                when().
                    post("/v1.0/address").
                then().
                    statusCode(CREATED.value()).
                extract().
                    path("id");

        Customer customerTwo = new Customer();
        customerTwo.setName("Person Two");
        Address addressThree = builder()
                .city("Rio de Janeiro")
                .state("Rio de Janeiro")
                .country("Brazil")
                .customer(customerTwo)
                .build();

        customerTwoId =
                given().
                    body(toJson(customerTwo)).
                    contentType(JSON).
                when().
                    post("/v1.0/customers").
                then().
                    statusCode(CREATED.value()).
                extract().
                    path("id");
        customerTwo.setId(valueOf(customerTwoId));

        addressThreeId =
                given().
                    body(toJson(addressThree)).
                    contentType(JSON).
                when().
                    post("/v1.0/address").
                then().
                    statusCode(CREATED.value()).
                extract().
                    path("id");
    }
}
