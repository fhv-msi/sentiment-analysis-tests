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

import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.*;

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
        System.setProperty("webdriver.gecko.driver", "C:\\Users\\Michael\\Desktop\\geckodriver.exe");
        driver = new FirefoxDriver();

        // prevent errors if we start from a sleeping heroku instance
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
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
        driver.findElement(By.id("loginBtn")).click();
    }

    @When("^Analyze the test '(.*?)'$")
    public void analyzeText(String text) {
        WebElement textField = driver.findElement(By.id("analyzeText"));
        textField.sendKeys(text);
        driver.findElement(By.id("analyzeBtn")).click();
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

    @When("^I press logout$")
    public void logout(){
        driver.findElement(By.id("logoutLink")).click();
        driver.findElement(By.id("logoutBtn")).click();
        assertFalse(driver.findElements(By.id("logo")).isEmpty());
    }
}
