package at.fhv.sentiment_analysis_tests;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"json:target/cucumber.json"} )
public class RunSentimentAnalysisTest {
}
