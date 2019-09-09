package Scripts;

import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import CommonFunctions.CommonFunction;
import CommonFunctions.ProcessorSheet;
import CommonFunctions.Screenshot;
import ObjectRepository.OR_LoginPage;
import ObjectRepository.OR_Medication;

public class Login {

	static boolean iStatus = false;
	static Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

	public static void MTM_Login(WebDriver driver, String parameters, ExtentReports extent) throws Exception {

		PropertyConfigurator.configure("Log4j.properties");// logger file

		OR_LoginPage loginpage = new OR_LoginPage(driver);

		String[] columnVal = parameters.split(",");// Getting values(parametrarized values)
		String testcaseName = columnVal[0];
		switch (columnVal[1])// columnVal[1] is Case/column D from spread sheet
		{
		// Call Methods corresponding to Scenarios
		case "Verify Login":

			iStatus = false;
			boolean status = false;
			System.out.println();
//			ExtentReportTest.ExtentResults(driver);

			status = CommonFunction.WaitforObject(driver, loginpage.LoginLink(), status, "Inspire Link");

			// Reporting Results
			ExtentTest test = extent.createTest(testcaseName, "Verify Inpsire Link");
			if (status == true) {
				iStatus = true;
				test.log(Status.PASS, "Inspire Link displays");
				logger.info("Inspire link Found");
			} else {
				Screenshot.TakeScreenshots(driver);
				test.log(Status.FAIL, "Inspire Link DID NOT display");
				logger.info("Inspire link NOT Found... Exiting script execution");
				System.exit(0);// If Home page is not present, then exit
			}

			ProcessorSheet.WriteResults(iStatus);// Write iStatus results

			break;

		case "Member Search":
			iStatus = false;
			status = false;

			try {
				loginpage.LoginLink().click();// Inspire link
				loginpage.LoginLink().click();
			} catch (Exception e) {
			}

			status = CommonFunction.WaitforObject(driver, loginpage.loginPageMemberIDTextBox(), status,
					"MemberID Textbox Field");// Home page "Home" link

			if (status == true) {
				logger.info("MemberID text field displayed");
				loginpage.loginPageMemberIDTextBox().sendKeys(columnVal[2]);// login page member id text box
				Thread.sleep(1000);
				loginpage.loginPageFindButton().click();// Login page Find button

				CommonFunction.WaitUntilDisappear(driver, loginpage.AjaxIcon(), "Ajax Spinner");
			}

			// Home page Home link
			status = CommonFunction.WaitforObject(driver, loginpage.linkHome(), status, "Inspire Link");

			// Reporting Results
			test = extent.createTest(testcaseName, "Verify Member Home Page");
			if (status == true) {
				Thread.sleep(1000);
				OR_Medication med = new OR_Medication(driver);

				if (med.attentionOK().isDisplayed())// Attention OK Button on home page startup
					med.attentionOK().click();

				iStatus = true;
				test.log(Status.PASS, "Member Home Page displays");
				logger.info("Member Home Page displays");
			} else {
				Screenshot.TakeScreenshots(driver);
				test.log(Status.FAIL, "Member Home Page DID NOT display");
				logger.info("Home page NOT displayed... Exiting script execution");
				System.exit(0);// If Home page is not present, then exit
			}

			ProcessorSheet.WriteResults(iStatus);// Write iStatus results
			break;
		}
	}

}
