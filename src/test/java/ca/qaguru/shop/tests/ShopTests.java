package ca.qaguru.shop.tests;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;

import static org.hamcrest.Matchers.containsString;


public class ShopTests {
    private final String baseUri = "http://localhost:8090";
    private final String basePath = "/api/v1/products";
    private RequestSpecification requestSpecification;

    @BeforeTest
    public void setup(){
        requestSpecification = new RequestSpecBuilder()
                .log(LogDetail.ALL)
                .setBaseUri(baseUri)
                .setBasePath(basePath)
                .setContentType(ContentType.JSON)
                .build();
    }
    @Test
    public void saveNewProduct(){
        RestAssured
                .given()
                    .spec(requestSpecification)
                    .body(new File("src/test/resources/product.json"))
                    .auth().basic("maria","maria123")
                .when()
                    .post()
                .then()
                    .log().all()
                    .assertThat().statusCode(HttpStatus.SC_CREATED)
                    .assertThat().header("Location", containsString("/api/v1/products/"));
    }

}
