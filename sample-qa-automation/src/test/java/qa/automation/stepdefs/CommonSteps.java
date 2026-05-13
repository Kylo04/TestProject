package qa.automation.stepdefs;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import qa.automation.config.Config;
import qa.automation.playwright.PlaywrightSession;
import qa.automation.reporting.Reporter;

public class CommonSteps {

    public CommonSteps(PlaywrightSession session) {
    }

    /* =======================
       Background steps
       ======================= */

    @Given("environment is ready")
    public void environment_is_ready() {
        try {
            Page page = PlaywrightSession.page();
            page.navigate(Config.baseUrl(), new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
            Reporter.pass("url has been launched");
        }catch (Exception e){Reporter.fail("Couldn't launch url: "+e.getMessage());}
    }

    @And("user is authenticated")
    public void user_is_authenticated() {
        Reporter.pass("User authenticated successfully");
    }

    /* =======================
       Scenario A1
       ======================= */

    @When("I perform a normal operation")
    public void i_perform_a_normal_operation() {
        Reporter.info("Normal operation executed");
    }

    @Then("the operation should pass")
    public void the_operation_should_pass() {
        Reporter.pass("Operation passed as expected");
    }

    /* =======================
       Scenario Outline A2
       ======================= */

    @When("I perform operation for {string}")
    public void i_perform_operation_for_type(String type) {
        Reporter.info("Executing operation for type: " + type);
    }

    @Then("the result should {string}")
    public void the_result_should_be(String result) {

        switch (result.toLowerCase()) {
            case "pass" -> {
                Reporter.pass("Result validated as PASS");
            }
            case "soft" -> {
                Reporter.softFail("Soft validation issue detected", true);
            }
            default -> {
                Reporter.fail("Unsupported result type: " + result, true);
                // execution stops here
            }
        }
    }

    @When("I perform a failing operation")
    public void iPerformAFailingOperation() {
        Reporter.info("Failing operation executed");
        Reporter.pass("Operation passed as expected");
    }

    @Then("the system should show a warning")
    public void theSystemShouldHardFail() {
        Reporter.info("System should hard fail");
        Reporter.pass("Operation passed as expected");
        Reporter.warn("System should hard fail", true);
    }

    @Then("the system should skip the test")
    public void theSystemShouldskip() {
        Reporter.info("System should hard fail");
        Reporter.pass("Operation passed as expected");
        Reporter.skip("System should skip here", true);
    }

    @And("check next test step continues or not")
    public void checkNextTestStepContinuesOrNot() {
        Reporter.info("Next test step continues or not");
    }

    @And("further some steps")
    public void furtherSomeSteps() {
        Reporter.info("Further some steps");
    }
}