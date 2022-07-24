package stepDefs;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.inject.Inject;

import constants.HTTPCodeConstants;
import constants.TestConstants;
import constants.EndpointConstants;
import io.cucumber.java8.En;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.PetResponse;
import models.UpdateUserRequest;
import utils.FileUtils;
import utils.RestSingletonUtils;

public class Petstore_UserOperations implements En {

	@Inject
	UpdateUserRequest updateUserRequest;
	FileUtils file = new FileUtils();
	String updatedUserName;
	RestSingletonUtils getRest;
	List<String> multipleUsersCreated;
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public Petstore_UserOperations() throws IOException {

		RequestSpecification request = RestSingletonUtils.getRestInstance();
		request.contentType("application/json");
		Gson gson = new Gson();

		Given("I Create single {string}", (String createUserFilePath) -> {
			String bodyStr = file.read(createUserFilePath);
			request.body(bodyStr);
			LOGGER.log(Level.INFO, "Creating single user with following details " + bodyStr);
			Response response = request.post(EndpointConstants.POST_CREATEUSERS_ENDPOINT);
			assertEquals(response.getStatusCode(), HTTPCodeConstants.STATUS_CODE_OK);
		});

		When("I Create multiple {string}", (String createMultipleUsersFilePath) -> {
			String bodystr = file.read(createMultipleUsersFilePath);
			request.body(bodystr);
			multipleUsersCreated = getValuesForGivenKey(bodystr, "username");
			LOGGER.log(Level.INFO, "Creating following multiple users " + multipleUsersCreated);
			Response response = request.post(EndpointConstants.POST_CREATEUSERS_ENDPOINT);
			assertEquals(response.getStatusCode(), HTTPCodeConstants.STATUS_CODE_OK);
		});

		When("I update {string} with {string}", (String userToBeUpdated, String userDetailsFilePath) -> {
			LOGGER.log(Level.INFO, "Updating user " + userToBeUpdated);
			userToBeUpdated = new JSONObject(file.read(userDetailsFilePath)).getString("username");
			updateUserRequest = gson.fromJson(file.read(userDetailsFilePath), UpdateUserRequest.class);
			Response response = request.body(file.read(userDetailsFilePath))
					.put(EndpointConstants.PUT_UPDATEUSERS_ENDPOINT + userToBeUpdated);
			updatedUserName = new JSONObject(file.read(userDetailsFilePath)).getString("username");
			LOGGER.log(Level.INFO, "updated user name is " + updatedUserName);
			System.out.println("response of updateUsers" + response.getBody().asString());
			assertEquals(response.getStatusCode(), HTTPCodeConstants.STATUS_CODE_OK);
		});

		Then("I should get updated user data", () -> {
			Response response = request.get(EndpointConstants.GET_USERS_ENDPOINT + updatedUserName);
			LOGGER.log(Level.INFO, "User details after update are " + response.getBody().asString());
			assertEquals(response.getStatusCode(), HTTPCodeConstants.STATUS_CODE_OK);
		});

		Then("all users can be fetched successfully", () -> {

			LOGGER.log(Level.INFO, "Verifying that multiple users are created in system");

			Response response;

			for (String userName : multipleUsersCreated) {
				response = request.get(EndpointConstants.GET_USERS_ENDPOINT + userName);
				assertEquals(response.getStatusCode(), HTTPCodeConstants.STATUS_CODE_OK);
			}
		});

		After("@cleanUpupdatedUsers", () -> {
			LOGGER.log(Level.INFO,
					"Test data clean-up for individual user created/updated " + updatedUserName + " has started");
			Response response = request.get(EndpointConstants.DELETE_USERS_ENDPOINT + updatedUserName);
			assertEquals(response.getStatusCode(), HTTPCodeConstants.STATUS_CODE_OK);
		});

		After("@cleanUpMultipleUsers", () -> {

			LOGGER.log(Level.INFO, "Test data clean-up for multiple user created has started");
			for (String userName : multipleUsersCreated) {
				LOGGER.log(Level.INFO, "Deleting user with username- " + userName);
				Response response = request.get(EndpointConstants.DELETE_USERS_ENDPOINT + userName);
				assertEquals(response.getStatusCode(), HTTPCodeConstants.STATUS_CODE_OK);
			}
		});
	}

	public List<String> getValuesForGivenKey(String jsonArrayStr, String key) {
		JSONArray jsonArray = new JSONArray(jsonArrayStr);
		return IntStream.range(0, jsonArray.length())
				.mapToObj(index -> ((JSONObject) jsonArray.get(index)).optString(key)).collect(Collectors.toList());
	}
}
