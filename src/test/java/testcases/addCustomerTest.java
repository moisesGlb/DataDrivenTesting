package testcases;

import base.BaseTest;
import org.openqa.selenium.Alert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.DataProviderClass;
import utilities.TestUtils;

public class addCustomerTest extends BaseTest {

    private TestUtils testUtils = new TestUtils();

    @Test(dataProvider = "addCustomer", dataProviderClass = DataProviderClass.class,
            priority = 2, description = "Creating different types of customers")
    public void addCustomerTest(String name, String lastName, String postCode,String id) {
        click("btnLoginManager_XPATH");
        wait.until(ExpectedConditions.elementToBeClickable(super.getSelectorType("tabAddCustomer_XPATH")));
        click("tabAddCustomer_XPATH");
        if (driver.findElement(super.getSelectorType("inputFirstName_XPATH")).isDisplayed()) {
            type("inputFirstName_XPATH", name);
            type("inputLastName_XPATH", lastName);
            type("inputPostCode_XPATH", postCode);
            click("btnAddCustomer_XPATH");
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertMessage = alert.getText();
            alert.accept();
            Assert.assertTrue(validateAlertMessage(alertMessage), "The customer that you are trying to register " +
                    "could not be created: " + alertMessage);
            log.info(alertMessage);
            click("btnHome_XPATH");
            testUtils.updateDataSheet(configProperties.getProperty("filename"),configProperties.getProperty("sheetname.customerdata"),
                    getId(alertMessage),name,lastName,3,log);

        } else {
            String errorMessage = "The form to add a customer could not be found";
            log.info(errorMessage);
            Assert.fail(errorMessage);
        }
    }

    @Test(priority = 99, description = "Fail test to verify screenshot feature")
    public void screenshotValidationAmeo() {
        driver.findElement(super.getSelectorType("btnLoginClient_XPATH")).click();
        Assert.fail("Validate Screenshot feature papilo");
    }

    private boolean validateAlertMessage(String message) {
        return message.contains("successfully") && message.contains("id");
    }

    private String getId(String message) {
        String ret = message.replace("Customer added successfully with customer id :", "");
        System.out.println(ret);
        return ret;
    }
}
