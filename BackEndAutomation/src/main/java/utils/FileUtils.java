package utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils {

	public String read(String filePath) throws IOException {

		InputStream inStream = this.getClass().getResourceAsStream(filePath);
		BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
		StringBuilder stringBuilder = new StringBuilder();

		String eachLine = "";
		while ((eachLine = br.readLine()) != null) {
			stringBuilder.append(eachLine);
		}
		return stringBuilder.toString();
	}
}