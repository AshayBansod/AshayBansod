package stepDefs;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import constants.HTTPCodeConstants;
import constants.TestConstants;
import constants.EndpointConstants;
import io.cucumber.java8.En;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.PetResponse;
import utils.FileUtils;
import utils.RestSingletonUtils;

public class Petstore_PetOperations implements En {

	FileUtils file = new FileUtils();
	String createdPetId;
	PetResponse petResponse;

	public Petstore_PetOperations() throws IOException {

		RequestSpecification request = RestSingletonUtils.getRestInstance();
		request.contentType("application/json");

		Given("I Create {string}", (String createPetFilePath) -> {
			System.out.println(">>>>>>>>>>>>> Starting create Pet");
			request.body(file.read(createPetFilePath));

			Response response = request.post(EndpointConstants.POST_CREATEPETS_ENDPOINT);
			System.out.println("response of createPet" + response.getBody().asString());

			petResponse = response.getBody().as(PetResponse.class);

			System.out.println("pet id is " + petResponse.id);

			assertEquals(response.getStatusCode(), HTTPCodeConstants.STATUS_CODE_OK);

		});

		When("I update {string} of pets", (String petDetailsForUpdate) -> {
			System.out.println(">>>>>>>>>>>>> Starting update Pet");
			request.body(file.read(petDetailsForUpdate));
			Response response = request.put(EndpointConstants.PUT_UPDATEPETS_ENDPOINT);
			System.out.println("response of updatePet" + response.getBody().asString());
			petResponse = response.getBody().as(PetResponse.class);
			System.out.println("pet id after update is " + petResponse.id);
			assertEquals(response.getStatusCode(), HTTPCodeConstants.STATUS_CODE_OK);
		});

		Then("I should get updated pet data by {string}", (String petStatus) -> {
			System.out.println(">>>>>>>>>>>>> Starting get pet by status");
			Response response = request.queryParam("status", petStatus)
					.get(EndpointConstants.GET_PETSBYSTATUS_ENDPOINT);

			System.out.println();

			assertEquals(ifContainsLongValue(response, "id", petResponse.id), TestConstants.BOOLEAN_TRUE);
			/*
			 * JsonPath jsonPath = response.jsonPath(); List<Long> petIdByState =
			 * jsonPath.getList("id");
			 * 
			 * System.out.println("===>>>>>> check array by status array >> " +
			 * petIdByState); assertEquals(petIdByState.contains(petResponse.id),
			 * TestConstants.BOOLEAN_TRUE);
			 */
		});
	}

	public boolean ifContainsLongValue(Response response, String key, long toBeSearched) {

		JsonPath jsonPath = response.jsonPath();
		List<Long> petIdByState = jsonPath.getList(key);
		return petIdByState.contains(toBeSearched);
	}

}
