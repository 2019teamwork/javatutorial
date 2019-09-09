package ObjectRepository;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class OR_Medication {

	WebDriver driver;

	public OR_Medication(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	// Icons
	@FindBy(className = "img_member_information") // Add Delete edit allergy icon
	public List<WebElement> EditAllergyIcon;



	// **************************************************Buttons************************************************

	@FindBy(id = "closeAlertPopup") // Attention OK Button on home page startup
	public WebElement attentionOKButton;

	// Web Elements
	@FindBy(className = "div_addmedication") // Complete Medications
	public WebElement CompleteMedications;

	@FindBy(id = "ProgressImage") // Ajax Icon
	public WebElement AjaxIcon;

	@FindBy(id = "addAllergyLabel") // Add Allergy Button
	public WebElement AddAllergyBTN;

	@FindBy(id = "cancelAddAllergy") // Cancel Allergy Button
	public WebElement CancelAllergyBTN;

	// ************************************************Text Fields************************************************
	@FindBy(className = "relatedConditionDropDown") // Releated Condition Text field
	public WebElement RelatedCondition;

	@FindBy(className = "DirectionOfUseText") // DFU text field
	public WebElement DFU;

	@FindBy(id = "IndicationsTable") // Related Conditions Table
	public WebElement RelatedConditionTable;

	@FindBy(name = "PrescriberName") // Prescriber text field
	public WebElement prescriberName;

	@FindBy(id = "Allergy") // Allergy text field
	public WebElement AddAllergy;

	@FindBy(id = "Reaction") // Add Reaction text field
	public WebElement AddReaction;

	// *************************************************************************************************
	// CHECK BOXES
	@FindBy(id = "chk_AllergyVerify") // Allergy and Reactions check box without Allergies
	public WebElement CheckboxEmptyAllergy;

	@FindBy(id = "chkAllergyVerify") // Allergy and Reactions check box with Allergies
	public WebElement CheckboxWithAllergy;


	// **********************************************************************************

	// Icons
	public List<WebElement> EditAllergyIcon() {
		return EditAllergyIcon;
	}

	// Buttons
	public WebElement attentionOK() {
		return attentionOKButton;
	}

	// Web Elements
	public WebElement CompleteMedications() {
		return CompleteMedications;
	}

	public WebElement AjaxIcon() {
		return AjaxIcon;
	}

	// Text Fields
	public WebElement RelatedCondition() {
		return RelatedCondition;
	}

	public WebElement DFU() {
		return DFU;
	}

	public WebElement RelatedConditionTable() {
		return RelatedConditionTable;
	}

	public WebElement prescriberName() {
		return prescriberName;
	}

	// CHECK BOXES
	public WebElement CheckboxEmptyAllergy() {
		return CheckboxEmptyAllergy;
	}

	public WebElement CheckboxWithAllergy() {
		return CheckboxWithAllergy;
	}

}
