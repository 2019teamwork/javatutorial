import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import CommonFunctions.ProcessorSheet;
import Scripts.Login;
import Scripts.Medication;

///import JavaClassFiles.Experiment;

public class Processor extends Start_Main {

	public static void MainProcessor() throws Exception {

		File projFolder = Globals.projFolder;// Project folder

		XSSFWorkbook wb = null;
		XSSFSheet sheet1 = null;

		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(projFolder + "\\Config.properites");/*- Path of test case name from Config */
		prop.load(fis);

		// Extent Reports Initialization
		ExtentHtmlReporter htmlReporter;
		ExtentReports extent;

		// specify location of the report
		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/test-output/myReport.html");

		htmlReporter.config().setDocumentTitle("Automation Report");// Title of report
		htmlReporter.config().setReportName("Functional Report");// Name of the report
		htmlReporter.config().setTheme(Theme.DARK);

		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);

		// Passing General information
		extent.setSystemInfo("Hostname", InetAddress.getLocalHost().getHostName());
		extent.setSystemInfo("OS", System.getProperty("os.name"));
		extent.setSystemInfo("Browser", prop.getProperty("browser"));

		// Finding the path of current Test case folder
		String sShots_Path = projFolder + "\\" + "TestCaseFiles" + "\\" + prop.getProperty("testcaseName")
				+ "\\Results";

		try {
			String testCasePath = projFolder + "\\TestCaseFiles" + "\\" + prop.getProperty("testcaseName");// path of
																											// TestCaseFiles
			String testCaseName = prop.getProperty("testcaseName");// Name of test case

			File src = new File(testCasePath + "\\" + testCaseName + ".xlsx");
			fis = new FileInputStream(src);

			wb = new XSSFWorkbook(fis);
			sheet1 = wb.getSheetAt(0);// Get the first sheet
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		int rowCount = sheet1.getLastRowNum();

		for (int iRow = 1; iRow < rowCount + 1; iRow++) {

			Row row = sheet1.getRow(iRow);

			/*-using special formatting to  to get the string value of a numeric cell */
			DataFormatter formatter = new DataFormatter();
			Cell cell = row.getCell(0);
			String str_TestCaseName = formatter.formatCellValue(cell);

			String str_Class_Proc = row.getCell(2).getStringCellValue();
			String str_Case = row.getCell(3).getStringCellValue();
			String str_Scenario = row.getCell(4).getStringCellValue();

			// Create a loop to print cell values in a row
			if (!row.getCell(1).getStringCellValue().equalsIgnoreCase("y")) {
				System.out.println("\r" + "Executing Test Case: " + str_TestCaseName + "\r" + "Step: " + (iRow + 1)
						+ " out of " + rowCount + "\r" + "Class Name: " + str_Class_Proc + " -> " + "Case: " + str_Case
						+ " -> " + str_Scenario + "\r" + "Please Wait........" + "\r");

				/*- storing row number, class, and Scenario in a notepad, so that it can be read for assigning names for failed screenshots */
				String failed_Results_Name = str_TestCaseName + "," + " Failed Test - Step " + iRow + " "
						+ str_Class_Proc + " - " + "Case -- " + str_Case + " - " + str_Scenario;
				ProcessorSheet.WriteToNotepad("\\TempResults.txt",
						failed_Results_Name);/*- This location is also used to obtain the test step name for failed test cases screenshot name */

				/*-Instead of passing individual parameters, collect all values and then split it in corresponding classes */
				String parameters = str_TestCaseName + " -- " + str_Class_Proc + " -> " + str_Scenario;

				/*- Collecting all column values. Parameterization values(Values we want to pass) */
				for (int jCol = 3; jCol < row.getLastCellNum(); jCol++) {
					parameters = parameters + "," + row.getCell(jCol).getStringCellValue();
				}

				switch (str_Class_Proc) {
				case "Login":
					Login.MTM_Login(driver, parameters, extent);
					break;

				case "Medication":
					Medication.Medication_Main(driver, parameters, extent);
					break;

				default:

					break;
				}

			}

		}

		try

		{
			wb.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		System.out.println("Exporting Test Results...... Please Wait" + "\r" + "Results are in => " + sShots_Path);
		ProcessorSheet.WriteFinalResultsToExcel(sShots_Path); // Writing final results from Processor.txt to
																// corresponding Results folder

		extent.flush();
		fis.close();

//		System.out.println("Closing the Browser");
//		System.out.println("Exporting the Results, Please Wait");

//		Thread.sleep(5000);
//		driver.close();
//		driver.quit();

	}

}
