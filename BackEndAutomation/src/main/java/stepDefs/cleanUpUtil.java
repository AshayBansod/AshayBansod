package stepDefs;

import com.google.inject.Inject;

import constants.EndpointConstants;
import io.restassured.response.Response;
import io.cucumber.java8.En;
import io.restassured.specification.RequestSpecification;
import models.UpdateUserRequest;

import utils.RestSingletonUtils;

public class cleanUpUtil implements En {

	@Inject
	UpdateUserRequest updateUserRequest;

	@Inject
	public cleanUpUtil() {

		RequestSpecification request = RestSingletonUtils.getRestInstance();
		request.contentType("application/json");


		After("@cleanUpupdatedUsers", () -> {

			System.out.println(">>>>>>>>>>>>> Starting Delete User");

			System.out.println("value from Pojo >>>> " + updateUserRequest.getUsername());

			System.out.println("Delete call > " + EndpointConstants.HOST + EndpointConstants.DELETE_USERS_ENDPOINT
					+ updateUserRequest.getUsername());
			Response response = request.get(EndpointConstants.DELETE_USERS_ENDPOINT + updateUserRequest.getUsername());
			System.out.println("response of deleteUsers" + response.getBody().asString());
		});

		/*
		 * After("@cleanUpMultipleUsers", () -> { for (String userName :
		 * multipleUsersCreated) { Response response =
		 * request.get(EndpointConstants.DELETE_USERS_ENDPOINT + userName);
		 * System.out.println("status code is " + response.getStatusCode());
		 * assertEquals(response.getStatusCode(), HTTPCodeConstants.STATUS_CODE_OK); }
		 * });
		 */

	}
}
