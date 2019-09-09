package CommonFunctions;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcel {
	static File projFolder = new File(System.getProperty("user.dir"));// Project folder

	static XSSFWorkbook wb;
	static XSSFSheet sheet1;

	public ReadExcel(XSSFWorkbook wb) throws Exception {
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(
				projFolder + "\\Config.properites");/*- Path of test case name from Config */
		prop.load(fis);

		try {
			String testCasePath = projFolder + "\\src" + "\\TestCaseFiles";
			String testCaseName = prop.getProperty("testcaseName");

			File src = new File(testCasePath + "\\" + testCaseName + ".xlsx");
			fis = new FileInputStream(src);

			wb = new XSSFWorkbook(fis);
			sheet1 = wb.getSheetAt(0);// Get the first sheet
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		int rowCount = sheet1.getLastRowNum();
		System.out.println(rowCount);

		String data0 = sheet1.getRow(0).getCell(1).getStringCellValue();
		System.out.println(data0);

//		try {
//			wb.close();
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
	}

}
