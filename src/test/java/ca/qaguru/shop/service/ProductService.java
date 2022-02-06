package ca.qaguru.shop.service;

import ca.qaguru.shop.models.Product;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;

public class ProductService {
    private final String baseUri = "http://localhost:8090";
    private final String basePath = "/api/v1/products";
    private RequestSpecification requestSpecification;

    public ProductService() {
        requestSpecification = new RequestSpecBuilder()
                .log(LogDetail.ALL)
                .setBaseUri(baseUri)
                .setBasePath(basePath)
                .setContentType(ContentType.JSON)
                .build();
    }

    public String saveNewRecord(Product product){
        String location = RestAssured
                .given()
                    .spec(requestSpecification)
                    .body(product)
                    .auth().basic("maria","maria123")
                .when()
                    .post()
                .then()
                    .log().all()
                    .assertThat().statusCode(HttpStatus.SC_CREATED)
                    .assertThat().header("Location", containsString("/api/v1/products/"))
                    .extract().header("Location");

        String id = location.substring("/api/v1/products/".length());
        return id;
    }
    public void findRecordById(String id, Product expProduct,int sc){
         ExtractableResponse response = RestAssured
                .given()
                    .spec(requestSpecification)
                    .auth().basic("maria","maria123")
                .when()
                    .get("/"+id)
                .then()
                    .log().all()
                    .assertThat().statusCode(sc)
                    .extract();
        if(sc == HttpStatus.SC_OK) {
            Product actProduct = response.body().as(Product.class);
                    Assert.assertEquals(actProduct, expProduct, "Incorrect Response");
        }
    }

    public void findAllProducts(List<Product> expProducts) {
        List<Product> actProducts = Arrays.asList(RestAssured
                .given()
                    .spec(requestSpecification)
                    .auth().basic("maria","maria123")
                .when()
                    .get()
                .then()
                    .log().all()
                    .assertThat().statusCode(HttpStatus.SC_OK)
                    .extract().body().as(Product[].class));
        for(Product expProduct: expProducts ){
            Assert.assertTrue(actProducts.contains(expProduct), "Product not found");
        }
    }

    public void updateRecord(String id, Product product) {
        RestAssured
                .given()
                    .spec(requestSpecification)
                    .body(product)
                    .auth().basic("maria","maria123")
                .when()
                    .put("/"+id)
                .then()
                .log().all()
                .assertThat().statusCode(HttpStatus.SC_NO_CONTENT);
    }

    public void deleteRecordById(String id) {
        RestAssured
                .given()
                    .spec(requestSpecification)
                    .auth().basic("maria","maria123")
                .when()
                    .delete("/"+id)
                .then()
                 .log().all()
                    .assertThat().statusCode(HttpStatus.SC_NO_CONTENT);
    }
}
