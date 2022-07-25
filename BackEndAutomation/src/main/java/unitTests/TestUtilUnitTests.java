package unitTests;

import org.junit.Test;
import utils.FileUtils;
import utils.TestUtils;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestUtilUnitTests {
	TestUtils testUtil = new TestUtils();
	FileUtils file = new FileUtils();
	
	@Test
	public void Test_TestUtils() throws IOException {
		
		List<String> expectedResponse =  new ArrayList<String>();
		expectedResponse.add("Trials3");
		expectedResponse.add("Trials5");
		
		List<String> actualRespone = testUtil.getValuesForGivenKey(file.read("/inputPayloads/creatMultipleUsersPayload.json"), "username");
		
		assertEquals(actualRespone, expectedResponse);
	}
}
