package qa.automation.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.springframework.context.annotation.Scope;
import qa.automation.config.Config;
import qa.automation.reporting.Reporter;

@Scope("prototype")
public class LoginPage extends CommonPage {
    private final Locator usernameInput;
    private final Locator passwordInput;
    private final Locator submitButton;
    private final Locator loginButton;
    private final Locator loggedInUsr;

    public LoginPage(Page page) {
        super(page);
        this.usernameInput = page.locator("input[type=email]");
        this.passwordInput = page.locator("input[type=password]");
        this.submitButton = page.locator("input[type=submit]");
        this.loginButton = getLocByTypeAndText("button", "Login");
        this.loggedInUsr = page.locator("[id='menu-username']");
    }

    public void openPageAndLogIn(String usr, String url) {
        launch(url);
        clickIfVisible(loginButton);
        wait(usernameInput);
        fill(usernameInput, usr);
        click(submitButton);
        hardwait(1);
        wait(page);
        wait(passwordInput);
        fill(passwordInput, Config.getPassword(usr));
        click(submitButton);
        click(submitButton);
        clickIfVisible(loginButton);
        if (page.locator("[id=passwordError]").isVisible()) {
            Reporter.info("Handling password error..");
            wait(passwordInput);
            fill(passwordInput, Config.getPassword(usr));
            click(submitButton);
            click(submitButton);
            wait(page);
        }
        wait(loggedInUsr);
        Reporter.info("Logged in with user: " + getText(loggedInUsr));
        wait(page);
    }

}
