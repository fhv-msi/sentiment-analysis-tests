package at.fhv.sentiment_analysis_tests;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * Step implementation for the sentiment analysis UAT tests
 */
public class SentimentAnalysisSteps {

    private WebDriver driver;

    /**
     * Setup the firefow test driver. This needs the environment variable 'webdriver.gecko.driver'
     * with the path to the geckodriver binary
     */
    @Before
    public void before() {
        driver = new FirefoxDriver();
    }

    /**
     * Shutdown the driver
     */
    @After
    public void after() {
        driver.quit();
    }

    @Given("^Open (.*?)$")
    public void openUrl(String url) {
        driver.navigate().to(url);
    }

    @Given("^Login with user '(.*?)'$")
    public void login(String email) {
        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys(email);
        emailField.submit();
    }

    @When("^Analyze the test '(.*?)'$")
    public void analyzeText(String text) {
        WebElement textField = driver.findElement(By.id("text"));
        textField.sendKeys(text);
        textField.submit();
    }

    @Then("^The smiley should be (.*?)$")
    public void checkSentiment(String sentiment) {
        WebElement sentimentItem = driver.findElement(By.id("sentiment"));
        String classes = sentimentItem.getAttribute("class");
        if ("happy".equals(sentiment)) {
            assertTrue(classes.contains("smile"));
        } else if ("unhappy".equals(sentiment)) {
            assertTrue(classes.contains("frown"));
        } else {
            fail();
        }
    }
}
