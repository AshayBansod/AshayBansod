package utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class TestUtils {

	public List<String> getValuesForGivenKey(String jsonArrayStr, String key) {
		JSONArray jsonArray = new JSONArray(jsonArrayStr);
		return IntStream.range(0, jsonArray.length())
				.mapToObj(index -> ((JSONObject) jsonArray.get(index)).optString(key)).collect(Collectors.toList());
	}

	public boolean ifContainsLongValue(Response response, String key, long toBeSearched) {
		JsonPath jsonPath = response.jsonPath();
		List<Long> petIdByState = jsonPath.getList(key);
		return petIdByState.contains(toBeSearched);
	}
}
