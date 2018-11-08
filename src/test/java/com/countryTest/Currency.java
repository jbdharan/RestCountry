package com.countryTest;

import common.Endpoint;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;

public class Currency {

    @Test
    public void testStatusCode(){
        given()
                .pathParam("code", "GBP")
                .when()
                .get("https://restcountries.eu/rest/v2/currency/{code}")
                .then().statusCode(200);

    }

    @Test
    public void testIncorrectStatusCode() {
        given().
                get(Endpoint.GET_CURRENCY_invalid).then().statusCode(404);
    }

    @Test
    public void validateCurrency(){
        given()
                .pathParam("code", "GBP")
                .when()
                .get("https://restcountries.eu/rest/v2/currency/{code}")
                .then()
                .body("currencies",hasItems("British pound"));

    }



}
