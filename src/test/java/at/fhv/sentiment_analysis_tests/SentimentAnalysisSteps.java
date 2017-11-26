package at.fhv.sentiment_analysis_tests;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
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
    public void before(Scenario scenario) throws Exception {
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability("version", "latest");
        capabilities.setCapability("platform", Platform.LINUX);
        capabilities.setCapability("name", scenario.getName());

        driver = new RemoteWebDriver(
                new URL("http://"+System.getenv("TESTINGBOT_CREDENTIALS")+"@hub.testingbot.com/wd/hub"),
                capabilities);

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

    @When("^Analyze the text '(.*?)'$")
    public void analyzeText(String text) {
        WebElement textField = driver.findElement(By.id("analyzeText"));
        textField.clear();
        textField.sendKeys(text);

        WebDriverWait wait = new WebDriverWait(driver, 10);
        ;
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.id("analyzeBtn")));
        button.click();

    }

    @Then("^The smiley should be (.*?)$")
    // wait until the result has been received
    public void checkSentiment(String sentiment) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("analyzeBtn")));

        WebElement sentimentItem = driver.findElement(By.id("sentiment"));
        verifySentiment(sentimentItem, sentiment);
    }

    @When("^I press logout$")
    public void logout() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement logoutLink = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("logoutLink")));
        logoutLink.click();

        // wait until popup is visible
        WebElement logoutBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("logoutBtn")));
        logoutBtn.click();
    }

    @Then("^I see the login page$")
    public void checkLoginPage() {
        assertFalse(driver.findElements(By.id("logo")).isEmpty());
    }

    @When("^Navigate to history$")
    public void navigateToHistory() {
        driver.findElement(By.linkText("History")).click();
    }

    @Then("^The ([0-9]). row shows the history item with text '(.*?)' and sentiment is '(.*?)'")
    public void checkHistoryItem(int row, String text, String sentiment) {
        WebElement textCell = driver.findElement(By.xpath("//table/tbody/tr[" + row + "]/td[1]"));
        WebElement sentimentIcon = driver.findElement(By.xpath("//table/tbody/tr[" + row + "]/td[2]/i"));

        assertEquals(text, textCell.getText());
        verifySentiment(sentimentIcon, sentiment);
    }

    /**
     * Check if the given icon contains the given sentiment
     *
     * @param sentimentIcon The icon to check
     * @param sentiment     The sentiment which should be set in the icon
     */
    private void verifySentiment(WebElement sentimentIcon, String sentiment) {
        String classes = sentimentIcon.getAttribute("class");
        if ("happy".equals(sentiment)) {
            assertTrue(classes.contains("smile"));
        } else if ("unhappy".equals(sentiment)) {
            assertTrue(classes.contains("frown"));
        } else if ("neutral".equals(sentiment)) {
            assertTrue(classes.contains("meh"));
        } else {
            fail();
        }
    }
}
