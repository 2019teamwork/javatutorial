package Scripts;

import java.lang.invoke.MethodHandles;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import CommonFunctions.CommonFunction;
import CommonFunctions.ProcessorSheet;
import CommonFunctions.Screenshot;
import ObjectRepository.OR_Medication;


public class Medication {

	static boolean iStatus = false;
	static Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

	public static void Medication_Main(WebDriver driver, String parameters, ExtentReports extent) throws Exception {

		PropertyConfigurator.configure("Log4j.properties");// logger file

		String[] columnVal = parameters.split(",");// Getting values(parametrarized values)
		String testcaseName = columnVal[0];
		switch (columnVal[1])// columnVal[1] is Case/column D from spread sheet
		{
		// Call Methods corresponding to Scenarios
		case "Verify Add Problem":

			iStatus = false;
			boolean status = false;
			System.out.println();

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

			}

			ProcessorSheet.WriteResults(iStatus);// Write iStatus results

			break;

		case "General":
			iStatus = false;
			status = false;


//			ReviewAllMedication(driver);
			
//			UnReviewAllMedication(driver);

//			AddAllergy(driver);
			RemoveAllergy(driver);

			ProcessorSheet.WriteResults(iStatus);// Write iStatus results
			break;
		}
	}

	public static void ReviewAllMedication(WebDriver driver) {
		boolean status = false;

		OR_Medication review = new OR_Medication(driver);
		WebElement completeMedicines = review.CompleteMedications();// (Complete Medications)
		
		List<WebElement> elements = completeMedicines.findElements(By.className("img_icon_review"));/*-to get the total number of review buttons present(Review Button)*/
		try {
			List<WebElement> unReviewedCount = completeMedicines.findElements(By.className("div_med_review"));// finding the count of UnReviewed buttons present

			for (int a = 0; a <= unReviewedCount.size() - 1; a++) {
				try {
					WebElement relCondition = review.RelatedCondition();// Releated Condition Text field
					WebElement DFU = review.DFU();// DFU text field
					if (relCondition.getText() == "") {
						review.RelatedCondition().click();

						// WebElement relConditionTable = driver.FindElement(By.Id("IndicationsTable"));
						WebElement relConditionTable = review.RelatedConditionTable();// Related Conditions Table
						status = CommonFunction.WaitforObject(driver, relConditionTable, status, "Related Condition Page");// Waiting for Add Related Condition page to display
						if (status == true) {
							WebElement addRelatedConditionTable = review.RelatedConditionTable();// (Related Conditions Table)
							List<WebElement> addRelatedCondition = addRelatedConditionTable.findElements(By.className("table_td_condition"));
							try {
								addRelatedCondition.get(0).click();
								addRelatedCondition.get(0).click();
								Thread.sleep(2000);
							} catch (Exception e) {
							}
						}
					}
					if (DFU.getText() == "") {
						review.DFU().sendKeys("AM");
					}

					try {
						if (review.prescriberName().getAttribute("value").isEmpty())// Prescriber text field
						{
							review.prescriberName().sendKeys("PRESCRIBER TEST");
							Thread.sleep(1000);
						}
						try {
							elements.get(a).click();
							CommonFunction.WaitUntilDisappear(driver, review.AjaxIcon(), "Ajax Spinner");
						} catch (Exception e) {
						}

					} catch (Exception e) {
					}
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
		}
	}

	public static void UnReviewAllMedication(WebDriver driver)
    {
        OR_Medication unReview = new OR_Medication(driver);

        WebElement elementContainer = unReview.CompleteMedications();////Complete Medications

        List<WebElement> elements = elementContainer.findElements(By.className("img_icon_review"));//to get the total number of review buttons present
        try
        {
            int x = 0;
            List<WebElement> reviewdCount = elementContainer.findElements(By.className("div_med_reviewed"));//finding the count of Reviewed buttons present
            x = elements.size() - reviewdCount.size();//getting the start point of Reviewed buttons

            for (int a = x; a <= elements.size() - 1; a++)
            {
                try
                {
                    elements.get(a).click();
					CommonFunction.WaitUntilDisappear(driver, unReview.AjaxIcon(), "Ajax Spinner");
                    elements.get(a).click();
                    reviewdCount = elementContainer.findElements(By.className("div_med_reviewed"));
                }
                catch (Exception e) {}
            }
        }
        catch (Exception e) {}
    }

	public static void AddAllergy(WebDriver driver) throws Exception {

		boolean status = false;

		OR_Medication addAllergy = new OR_Medication(driver);

		CheckAddAllergyIcon(driver);

		Thread.sleep(1000);
			List<WebElement> elements = addAllergy.EditAllergyIcon;// (Add Delete edit allergy icon)
			try {
				status = CommonFunction.WaitforObject_Enable(elements.get(2), status);
				
			if (status == true) {// Proceed only if Add Allergy button is enabled
					elements.get(2).click();
					WebElement allergy = addAllergy.AddAllergy;// Allergy text field
					status = CommonFunction.WaitforObject(driver, allergy, status, "Add allergy page");
					if (status == true)
					{
						try {
						addAllergy.AddAllergy.sendKeys("Allergy Test" + CommonFunction.AddDate());// Allergy text field
						addAllergy.AddReaction.sendKeys("Reaction Test" + CommonFunction.AddDate());// Add Reaction text
																									// field
							Thread.sleep(1000);
						addAllergy.AddAllergyBTN.click();// Add Allergy Button
							Thread.sleep(1000);
						addAllergy.CancelAllergyBTN.click();// Cancel Allergy Button
							}catch (Exception e) {}
					}
				
				}
				else {logger.info("Allergy Edit button is NOT enabled");}
			}
			catch (Exception e) {}
	}

	public static void RemoveAllergy(WebDriver driver) {
		CheckAddAllergyIcon(driver);

	}

	public static void CheckAddAllergyIcon(WebDriver driver) {
		try {
			OR_Medication addAllergy = new OR_Medication(driver);
			if (addAllergy.CheckboxWithAllergy().isSelected()) {
				if (addAllergy.CheckboxEmptyAllergy().isDisplayed())
				addAllergy.CheckboxEmptyAllergy().click();
				else
					addAllergy.CheckboxWithAllergy.click();
			}
		} catch (Exception e) {
		}
	}
}
