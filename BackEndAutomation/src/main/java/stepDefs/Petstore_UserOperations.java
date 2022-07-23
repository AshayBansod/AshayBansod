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
			// System.out.println("I Create multiple {string}");

			String bodyStr = file.read(createUserFilePath);

			request.body(bodyStr);

			// System.out.println(EndpointConstants.HOST +
			// EndpointConstants.POST_CREATEUSERS_ENDPOINT);

			LOGGER.log(Level.INFO, "Creating single user with following details " + bodyStr);
			Response response = request.post(EndpointConstants.POST_CREATEUSERS_ENDPOINT);
			// System.out.println("response of createUsers" +
			// response.getBody().asString());

			assertEquals(response.getStatusCode(), HTTPCodeConstants.STATUS_CODE_OK);

		});

		When("I Create multiple {string}", (String createMultipleUsersFilePath) -> {
			String bodystr = file.read(createMultipleUsersFilePath);

			request.body(bodystr);

			// System.out.println(getValuesForGivenKey(bodystr, "username"));

			multipleUsersCreated = getValuesForGivenKey(bodystr, "username");

			LOGGER.log(Level.INFO, "Creating following multiple users " + multipleUsersCreated);

			// System.out.println(EndpointConstants.HOST +
			// EndpointConstants.POST_CREATEUSERS_ENDPOINT);

			Response response = request.post(EndpointConstants.POST_CREATEUSERS_ENDPOINT);
			// System.out.println("response of createUsers" +
			// response.getBody().asString());

			assertEquals(response.getStatusCode(), HTTPCodeConstants.STATUS_CODE_OK);
		});

		When("I update {string} with {string}", (String userToBeUpdated, String userDetailsFilePath) -> {
			// System.out.println(">>>>>>>>>>>>> Starting Update User");

			LOGGER.log(Level.INFO, "Updating user " + userToBeUpdated);
			
			userToBeUpdated = new JSONObject(file.read(userDetailsFilePath)).getString("username");

			updateUserRequest = gson.fromJson(file.read(userDetailsFilePath), UpdateUserRequest.class);

			// System.out.println("value username from Pojo >>>> " +
			// updateUserRequest.getUsername());

			// request.body(file.read(userDetailsFilePath));
			// System.out.println("Put call > " + EndpointConstants.HOST +
			// EndpointConstants.PUT_UPDATEUSERS_ENDPOINT
			// + userToBeUpdated);
			Response response = request.body(file.read(userDetailsFilePath))

					.put(EndpointConstants.PUT_UPDATEUSERS_ENDPOINT + userToBeUpdated);

			updatedUserName = new JSONObject(file.read(userDetailsFilePath)).getString("username");

			LOGGER.log(Level.INFO, "updated user name is " + updatedUserName);
			//System.out.println(updatedUserName);

			System.out.println("response of updateUsers" + response.getBody().asString());

			assertEquals(response.getStatusCode(), HTTPCodeConstants.STATUS_CODE_OK);
		});

		Then("I should get updated user data", () -> {

			//System.out.println(">>>>>>>>>>>>> Starting Get User");
			//System.out.println(
			//		"Get call > " + EndpointConstants.HOST + EndpointConstants.GET_USERS_ENDPOINT + updatedUserName);

			Response response = request.get(EndpointConstants.GET_USERS_ENDPOINT + updatedUserName);
			//System.out.println("response of getUsers" + response.getBody().asString());

			LOGGER.log(Level.INFO, "User details after update are " + response.getBody().asString());
			assertEquals(response.getStatusCode(), HTTPCodeConstants.STATUS_CODE_OK);

			/*
			 * System.out.println(">>>>>>>>>>>>> Starting Delete User");
			 * System.out.println("Delete call > " + EndpointConstants.HOST +
			 * EndpointConstants.DELETE_USERS_ENDPOINT + updatedUserName); response =
			 * request.get(EndpointConstants.DELETE_USERS_ENDPOINT + updatedUserName);
			 * System.out.println("response of deleteUsers" +
			 * response.getBody().asString());
			 */
		});

		Then("all users can be fetched successfully", () -> {

			LOGGER.log(Level.INFO, "Verifying that multiple users are created in system");
			
			Response response;

			for (String userName : multipleUsersCreated) {
				//System.out.println(userName);
				response = request.get(EndpointConstants.GET_USERS_ENDPOINT + userName);
				//System.out.println("status code is " + response.getStatusCode());
				assertEquals(response.getStatusCode(), HTTPCodeConstants.STATUS_CODE_OK);

			}
		});

		After("@cleanUpupdatedUsers", () -> {
			LOGGER.log(Level.INFO,
					"Test data clean-up for individual user created/updated " + updatedUserName + " has started");
			//System.out.println("Delete call > " + EndpointConstants.HOST + EndpointConstants.DELETE_USERS_ENDPOINT
			//		+ updatedUserName);
			Response response = request.get(EndpointConstants.DELETE_USERS_ENDPOINT + updatedUserName);
			//System.out.println("response of deleteUsers" + response.getBody().asString());
			assertEquals(response.getStatusCode(), HTTPCodeConstants.STATUS_CODE_OK);
		});

		After("@cleanUpMultipleUsers", () -> {

			LOGGER.log(Level.INFO, "Test data clean-up for multiple user created has started");
			for (String userName : multipleUsersCreated) {
				LOGGER.log(Level.INFO, "Deleting user with username- " + userName);
				Response response = request.get(EndpointConstants.DELETE_USERS_ENDPOINT + userName);
				//System.out.println("status code is " + response.getStatusCode());
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
