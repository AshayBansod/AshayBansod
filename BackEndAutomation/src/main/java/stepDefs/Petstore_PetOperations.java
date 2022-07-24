package stepDefs;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;

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
import utils.TestUtils;

public class Petstore_PetOperations implements En {

	FileUtils file = new FileUtils();
	String createdPetId;
	PetResponse petResponse;
	TestUtils testUtils = new TestUtils();
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public Petstore_PetOperations() throws IOException {

		RequestSpecification request = RestSingletonUtils.getRestInstance();
		request.contentType("application/json");
		Gson gson = new Gson();

		Given("I Create {string}", (String createPetFilePath) -> {
			LOGGER.log(Level.INFO, "Creating pet record");
			request.body(file.read(createPetFilePath));
			Response response = request.post(EndpointConstants.POST_CREATEPETS_ENDPOINT);
			petResponse = gson.fromJson(response.getBody().asString(), PetResponse.class);
			LOGGER.log(Level.INFO, "Pet is created with pet id - " + petResponse.getId());
			assertEquals(response.getStatusCode(), HTTPCodeConstants.STATUS_CODE_OK);
		});

		When("I update {string} of pets", (String petDetailsForUpdate) -> {

			LOGGER.log(Level.INFO, "Starting to update pet");
			request.body(file.read(petDetailsForUpdate));
			Response response = request.put(EndpointConstants.PUT_UPDATEPETS_ENDPOINT);
			petResponse = response.getBody().as(PetResponse.class);
			assertEquals(response.getStatusCode(), HTTPCodeConstants.STATUS_CODE_OK);
		});

		Then("I should get updated pet data by {string}", (String petStatus) -> {
			LOGGER.log(Level.INFO, "Fetching all pets with status - " + petStatus
					+ " and verifying if created/updated pet is in fetched list");
			Response response = request.queryParam("status", petStatus)
					.get(EndpointConstants.GET_PETSBYSTATUS_ENDPOINT);
			assertEquals(testUtils.ifContainsLongValue(response, "id", petResponse.id), TestConstants.BOOLEAN_TRUE);
		});

		After("@cleanUpPets", () -> {
			LOGGER.log(Level.INFO, "Test data clean-up for Pets user created/updated - " + petResponse.getId());
			Response response = request.delete(EndpointConstants.DELETE_PETSBYID_ENDPOINT + petResponse.getId());
			assertEquals(response.getStatusCode(), HTTPCodeConstants.STATUS_CODE_OK);
		});
	}

	/*
	 * public boolean ifContainsLongValue(Response response, String key, long
	 * toBeSearched) { JsonPath jsonPath = response.jsonPath(); List<Long>
	 * petIdByState = jsonPath.getList(key); return
	 * petIdByState.contains(toBeSearched); }
	 */

}
