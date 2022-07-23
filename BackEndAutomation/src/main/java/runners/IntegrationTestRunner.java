package runners;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/main/resources/features", glue = { "stepDefs" }, tags = {
		"@sanity" }, dryRun = false, plugin = { "pretty", "html:target/cucumber-reports" }, monochrome = true)
public class IntegrationTestRunner {

}
