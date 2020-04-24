package testcases;

import base.BaseTest;
import org.openqa.selenium.Alert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.DataProviderClass;
import utilities.TestUtils;

public class OpenAccountTest extends BaseTest {

    private TestUtils testUtils = new TestUtils();

    @Test(dataProvider = "openAccount", dataProviderClass = DataProviderClass.class,
            priority = 3, description = "Creating accounts for existing customers")
    public void openAccountForExistingCustomers(String name, String currency, String accountNumber){
        click("btnLoginManager_XPATH");
        wait.until(ExpectedConditions.elementToBeClickable(super.getSelectorType("tabOpenAccount_XPATH")));
        click("tabOpenAccount_XPATH");
        selectFromDropdown("selectCustomer_XPATH",name);
        selectFromDropdown("selectCurrency_XPATH",currency);
        click("btnSubmit_XPATH");
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        String alertMessage = alert.getText();
        alert.accept();
        Assert.assertTrue(alertMessage.contains("successfully"));
        click("btnHome_XPATH");
        log.info(alertMessage);
        testUtils.updateDataSheet(configProperties.getProperty("filename"),configProperties.getProperty("sheetname.openAcount"),
                getAccountNumber(alertMessage),name,currency,2,log);
    }

    private String getAccountNumber(String message){
        String replace = message.replace("Account created successfully with account Number :", "");
        return replace;
    }
}
