package utils;

import constants.EndpointConstants;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class RestSingletonUtils {

	private RestSingletonUtils() {
	};

	static RequestSpecification request;

	public static RequestSpecification getRestInstance() {

		RestAssured.baseURI = EndpointConstants.HOST;

		if (request == null) {
			request = RestAssured.given();
		}
		return request;
	}
}
