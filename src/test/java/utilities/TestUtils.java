package utilities;

import org.apache.log4j.Logger;

import java.io.IOException;

public class TestUtils {

    private static ExcelHandler handler = new ExcelHandler();

    public boolean isTestRunnable(String testName) throws IOException {
        String sheetName = "testSuite";
        return handler.executeTest(sheetName,testName);
    }

    public String generateFullPath(String[] path) {
        if ("win".equals(getOS())){
            String var = System.getProperty("user.dir")+    getFinalPath(path, "\\");
            return var;
        }else
            return System.getProperty("user.dir") + getFinalPath(path, "/");
    }

    public String getDriverPath(String browser){
        String os = getOS();
        String[] path;
        switch (browser.toLowerCase()){
            case "firefox": path= new String[]{"src","test","resources","executables",os,("win".equals(os) ?  "geckodriver.exe" : "geckodriver" )};
                break;
            case "ie": path= new String[]{"src","test","resources","executables","win","IEDriverServer.exe"};
                break;
            default: path= new String[]{"src","test","resources","executables",os,("win".equals(os) ?  "chromedriver.exe" : "chromedriver" )};
        }
        return generateFullPath(path);
    }

    private String getOS() {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            return "win";
        } else {
            if (System.getProperty("os.name").toLowerCase().contains("mac")){
                return "mac";
            }else return "linux";
        }
    }

    private String getFinalPath(String[] path, String divisor) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < path.length; i++) {
            res.append(divisor).append(path[i]);
        }
        return res.toString();
    }

    public void updateDataSheet(String fileName,String sheetName, String data2insert, String validator1, String validator2, int cellNum ,Logger log){
        try {
            handler.updateExcel(fileName, sheetName, data2insert, validator1, validator2, cellNum);
            log.info("The data: "+data2insert+" was saved on sheet: "+sheetName+", Validated against parameters : "+validator1+" and "+validator2);
        } catch (IOException e) {
            log.info("The Data could not be updated due to IOException: " + e.getMessage());
        } catch (Exception ex) {
            log.info("Unexpected exception: " + ex.getMessage());
        }
    }
}
