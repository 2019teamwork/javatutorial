package ObjectRepository;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class OR_LoginPage {

	WebDriver driver;

	public OR_LoginPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	// By InpsireLink = By.linkText("Inspire");
	@FindBy(linkText = "Inspire") // Inspire link
	WebElement InpsireLink;

	@FindBy(name = "memberIdTextBox") // login page member id text box
	WebElement memberIdTextBox;

	@FindBy(id = "SearchMembersBtn") // Login page Find button
	WebElement SearchMembersBtn;

	@FindBy(linkText = "Home") // Home page "Home" link
	WebElement Home;

	@FindBy(id = "ProgressImage") // Ajax
	WebElement Ajax;

	// **********************************************************************************

	public WebElement LoginLink() {
		// return driver.findElement(InpsireLink);
		return InpsireLink;
	}

	public WebElement loginPageMemberIDTextBox() {
		return memberIdTextBox;
	}

	public WebElement loginPageFindButton() {
		return SearchMembersBtn;
	}

	public WebElement linkHome() {
		return Home;
	}

	public WebElement AjaxIcon() {
		return Ajax;
	}

}
