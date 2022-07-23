package stepDefs;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.json.JSONArray;
import org.json.JSONObject;

import constants.HTTPCodeConstants;
import constants.TestConstants;
import constants.EndpointConstants;
import io.cucumber.java8.En;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.FileUtils;
import utils.RestSingletonUtils;

public class Petstore_UserOperations implements En {

	FileUtils file = new FileUtils();
	String updatedUserName;
	RestSingletonUtils getRest;
	List<String> multipleUsersCreated;

	public Petstore_UserOperations() throws IOException {

		RequestSpecification request = RestSingletonUtils.getRestInstance();
		request.contentType("application/json");

		Given("I Create single {string}", (String createUserFilePath) -> {
			System.out.println("I Create multiple {string}");

			String bodystr = file.read(createUserFilePath);

			request.body(bodystr);

			System.out.println(EndpointConstants.HOST + EndpointConstants.POST_CREATEUSERS_ENDPOINT);

			Response response = request.post(EndpointConstants.POST_CREATEUSERS_ENDPOINT);
			System.out.println("response of createUsers" + response.getBody().asString());

			assertEquals(response.getStatusCode(), HTTPCodeConstants.STATUS_CODE_OK);

		});

		When("I Create multiple {string}", (String createMultipleUsersFilePath) -> {
			String bodystr = file.read(createMultipleUsersFilePath);

			request.body(bodystr);

			System.out.println(getValuesForGivenKey(bodystr, "username"));

			multipleUsersCreated = getValuesForGivenKey(bodystr, "username");

			System.out.println(EndpointConstants.HOST + EndpointConstants.POST_CREATEUSERS_ENDPOINT);

			Response response = request.post(EndpointConstants.POST_CREATEUSERS_ENDPOINT);
			System.out.println("response of createUsers" + response.getBody().asString());

			assertEquals(response.getStatusCode(), HTTPCodeConstants.STATUS_CODE_OK);
		});

		When("I update {string} with {string}", (String userToBeUpdated, String userDetailsFilePath) -> {
			System.out.println(">>>>>>>>>>>>> Starting Update User");

			userToBeUpdated = new JSONObject(file.read(userDetailsFilePath)).getString("username");
			// request.body(file.read(userDetailsFilePath));
			System.out.println("Put call > " + EndpointConstants.HOST + EndpointConstants.PUT_UPDATEUSERS_ENDPOINT
					+ userToBeUpdated);
			Response response = request.body(file.read(userDetailsFilePath))
					.put(EndpointConstants.PUT_UPDATEUSERS_ENDPOINT + userToBeUpdated);

			updatedUserName = new JSONObject(file.read(userDetailsFilePath)).getString("username");

			System.out.println(updatedUserName);

			// store updated user name in context to be used by next steps

			System.out.println("response of updateUsers" + response.getBody().asString());

			assertEquals(response.getStatusCode(), HTTPCodeConstants.STATUS_CODE_OK);
		});

		Then("I should get updated user data", () -> {

			System.out.println(">>>>>>>>>>>>> Starting Get User");
			System.out.println(
					"Get call > " + EndpointConstants.HOST + EndpointConstants.GET_USERS_ENDPOINT + updatedUserName);

			Response response = request.get(EndpointConstants.GET_USERS_ENDPOINT + updatedUserName);
			System.out.println("response of getUsers" + response.getBody().asString());

			// assertEquals(response.getStatusCode(), HTTPCodeConstants.STATUS_CODE_OK);

			System.out.println(">>>>>>>>>>>>> Starting Delete User");
			System.out.println("Delete call > " + EndpointConstants.HOST + EndpointConstants.DELETE_USERS_ENDPOINT
					+ updatedUserName);
			response = request.get(EndpointConstants.DELETE_USERS_ENDPOINT + updatedUserName);
			System.out.println("response of deleteUsers" + response.getBody().asString());
		});

		Then("all users can be fetched successfully", () -> {

			Response response;

			for (String userName : multipleUsersCreated) {
				System.out.println(userName);
				response = request.get(EndpointConstants.GET_USERS_ENDPOINT + userName);
				System.out.println("status code is " + response.getStatusCode());
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
