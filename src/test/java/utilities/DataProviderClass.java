package utilities;

import base.BaseTest;
import org.testng.annotations.DataProvider;
import java.io.IOException;

public class DataProviderClass extends BaseTest {

    private static ExcelHandler handler = new ExcelHandler();

    @DataProvider(name="addCustomer")
    public static Object[][] getDataFromDataprovider()throws IOException {
        return handler.readExcel(configProperties.getProperty("filename"),configProperties.getProperty("sheetname.customerdata"));
    }

    @DataProvider(name="openAccount")
    public Object[][] getDataFromOpenAccountSheet() throws IOException {
        return handler.readExcel(configProperties.getProperty("filename"),configProperties.getProperty("sheetname.openAcount"));
    }
}
