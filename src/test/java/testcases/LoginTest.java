package testcases;

import base.BaseTest;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(priority = 1, description = "Login as Member")
    public void LoginsAsMember() {
        click("btnLoginClient_XPATH");
        wait.until(ExpectedConditions.elementToBeClickable(super.getSelectorType("clientSelector_XPATH")));
        Assert.assertTrue(driver.findElement(super.getSelectorType("clientSelector_XPATH")).isDisplayed());
        click("btnHome_XPATH");
    }

    @Test(priority = 1, description = "Login as Manager")
    public void LoginsAsManager() {
        click("btnLoginManager_XPATH");
        wait.until(ExpectedConditions.elementToBeClickable(super.getSelectorType("tabAddCustomer_XPATH")));
        Assert.assertTrue(driver.findElement(super.getSelectorType("tabAddCustomer_XPATH")).isDisplayed());
        click("btnHome_XPATH");
    }

}
