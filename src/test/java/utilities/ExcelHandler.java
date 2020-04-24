package utilities;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class ExcelHandler {

    private TestUtils testUtils = new TestUtils();

    public Object[][] readExcel(String fileName, String sheetName) throws IOException {
        Workbook workbook = getWorkBook(fileName);
        Sheet sheet = workbook.getSheet(sheetName);
        int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
        Row row = sheet.getRow(0);
        Object[][] data = new Object[rowCount][row.getLastCellNum()];
        for (int i = 1; i <= rowCount; i++) {
            row = sheet.getRow(i);
            for (int j = 0; j < row.getLastCellNum(); j++) {
                data[i - 1][j] = getCellData(row.getCell(j).getCellType(), row, j);
            }
        }
        return data;
    }

    public boolean executeTest(String sheetName, String testName) throws IOException {
        Workbook workbook = getWorkBook("testData.xlsx");
        Sheet sheet = workbook.getSheet(sheetName);
        int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
        Row row;
        for (int i = 1; i <= rowCount; i++) {
            row = sheet.getRow(i);
            //check the name of the test
            if (getCellData(row.getCell(0).getCellType(), row, 0).equalsIgnoreCase(testName)){
                //validate if it's approved to be executed or not
                if (getCellData(row.getCell(1).getCellType(), row, 1).equalsIgnoreCase("y")){
                    return true;
                }else return false;
            }
        }
        return false;
    }

    public void updateExcel(String fileName, String sheetName, String dataToWrite, String validation1, String validation2, int cellNum) throws IOException {
        if (cellNum > 1) {
            String[] path = {"src", "test", "resources", "excel", fileName};
            File file = new File(testUtils.generateFullPath(path));
            Workbook workbook = getWorkBook(fileName);
            Sheet sheet = workbook.getSheet(sheetName);
            Row row;
            int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
            for (int i = 0; i < rowCount + 1; i++) {
                row = sheet.getRow(i);
                if (row.getCell(0).getStringCellValue().equalsIgnoreCase(validation1) &&
                        row.getCell(1).getStringCellValue().equalsIgnoreCase(validation2)) {
                    row.getCell(cellNum).setCellValue(dataToWrite);
                    break;
                }
            }
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            outputStream.close();
        } else {
            //TODO log the error
        }
    }

    private Workbook getWorkBook(String fileName) throws IOException {
        String[] path = {"src", "test", "resources", "excel", fileName};
        Workbook workbook = null;
        File file = new File(testUtils.generateFullPath(path));
        FileInputStream inputStream = new FileInputStream(file);
        String fileExtensionName = fileName.substring(fileName.indexOf("."));
        if (fileExtensionName.equals(".xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        }
        if (fileExtensionName.equals(".xls")) {
            workbook = new HSSFWorkbook(inputStream);
        }
        inputStream.close();
        return workbook;
    }

    private String getCellData(CellType cellType, Row row, int index) {
        String data = "";
        switch (cellType) {
            case NUMERIC:
                data = String.valueOf(row.getCell(index).getNumericCellValue());
                break;
            case STRING:
                data = removeSemiConlon(row.getCell(index).getStringCellValue());
                break;
        }
        return data;
    }

    private String removeSemiConlon(String word2verify) {
        String str;
        if (!word2verify.isEmpty() && word2verify.contains("\"")) {
            str = word2verify.replace("\"", "");
            return str;
        } else {
            if (word2verify.contains("'")) {
                str = word2verify.replace("'", "");
                return str;
            }
        }
        return word2verify;
    }
}
