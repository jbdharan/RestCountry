package com.countryTest;

import com.country.framework.config;
import common.Endpoint;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class country {

	@Test
	public void testStatusCode(){
	given().
	get(Endpoint.GET_COUNTRY).then().statusCode(200);
	}
	
    @Test
	public void testIncorrectStatusCode() {
		given().
				get(Endpoint.GET_COUNTRY_invalid).then().statusCode(404);
	}
	
	@Test
	public void ListAllCountry(){
		/*given().get(Endpoint.GET_COUNTRY).then().statusCode(200);	*/
		RequestSpecification requestSpecification = new config().getRequestSpecification();
		String json = given().get(Endpoint.GET_COUNTRY).then().extract().asString();
		JsonPath jp = new JsonPath(json);
		List<String> list = jp.get("name");
		System.out.println("Country's are -------"+list.get(0));
	}
	
	@Test
	public void validateCountry(){
	    RequestSpecification requestSpecification = new config().getRequestSpecification();
		given().get(Endpoint.GET_COUNTRY).then().statusCode(200).
		and().contentType(ContentType.JSON).
				and().body("name", hasItem("India")).
				and().body("find { d -> d.name == 'India' }.borders", hasItem("CHN"));


	}

	@Test
	public void validateCapital(){

		given()
				.pathParam("country", "GB")
				.when()
				.get("https://restcountries.eu/rest/v2/name/{country}")
				.then()
				.body("capital",contains("London"));
	}

	@Test
	public void validateAll() {

		given()
				.when()
				.get("https://restcountries.eu/rest/v2/all")
				.then()
		        .and().body("name", hasItem("India"))
				.and().body("find { d -> d.name == 'India' }.borders", hasItem("CHN"));

		String name = returnValueByKeys("India", "name");
		String capital = returnValueByKeys("India", "capital");
		String region = returnValueByKeys("India", "region");
		String population = returnValueByKeys("India", "population");
		List borders = returnValuesByKeys("India", "borders");

		System.out.println(name + " " + capital + " " + region + " " + population);
		for (Object border : borders) {
			System.out.print(border + " ");
		}

	}

	private String returnValueByKeys(String primary, String secondary) {
		String path = String.format("find { d -> d.name == '%s' }.%s", primary, secondary);
		Object response = given().contentType(ContentType.JSON)
				.when()
				.get("https://restcountries.eu/rest/v2/all")
				.then()
				.extract().response().body().path(path);
		return String.valueOf(response);
	}

	private ArrayList<String> returnValuesByKeys(String primary, String secondary) {
		String path = String.format("find { d -> d.name == '%s' }.%s", primary, secondary);
		ArrayList<String> response = given().contentType(ContentType.JSON)
				.when()
				.get("https://restcountries.eu/rest/v2/all")
				.then()
				.extract().response().body().path(path);
		return response;
	}

}
