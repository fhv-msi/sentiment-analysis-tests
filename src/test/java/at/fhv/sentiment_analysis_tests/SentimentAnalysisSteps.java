package at.fhv.sentiment_analysis_tests;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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

    @When("^Analyze the text '(.*?)'$")
    public void analyzeText(String text) {
        WebElement textField = driver.findElement(By.id("analyzeText"));
        textField.clear();
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
    public void logout() {
        driver.findElement(By.id("logoutLink")).click();

        // wait until popup is visible
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement logoutBtn = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("logoutBtn")));

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
        WebElement textCell = driver.findElement(By.xpath("//table/tbody/tr[" + row  + "]/td[1]"));
        WebElement sentimentCell = driver.findElement(By.xpath("//table/tbody/tr[" + row + "]/td[2]"));

        assertEquals(text, textCell.getText());
        String classes = sentimentCell.getAttribute("class");
        if ("happy".equals(sentiment)) {
            assertTrue(classes.contains("smile"));
        } else if ("unhappy".equals(sentiment)) {
            assertTrue(classes.contains("frown"));
        } else {
            fail();
        }
    }
}
