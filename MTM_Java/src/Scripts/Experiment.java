package Scripts;

import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.reporters.jq.Main;

public class Experiment extends Main {

	public static void TestExperiment(WebDriver driver) throws Exception {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			js.executeScript("alert('Environment Selected is Prod');");
		} catch (Exception e) {
			System.out.println(e);
		}

		Thread.sleep(1000);

		try {
			driver.findElement(By.name("user_name")).sendKeys("GENERIC216");
			driver.findElement(By.name("user_password")).sendKeys("e#81qtq6");
			driver.findElement(By.name("pin")).sendKeys("HUMANA");
			driver.findElement(By.name("B5")).click();
			// Thread.sleep(5000);
			// driver.findElement(By.name("NEW ORDER(Ctrl+Z)")).click();
			// Thread.sleep(5000);
			Set<String> ids = driver.getWindowHandles();// Getting all the available window handles, here it gives 2
														// window handle ids
			Iterator<String> it = ids.iterator();// Setting iterator to navigate to windows of our choice
			String parentid = it.next();

			String childid = it.next();// Jump or navigate twice to reach the Pop up Window, because it is the second
										// one.
			driver.switchTo().window(childid); // Switching driver control to the Pop up window.

			driver.close(); // Close the Pop up window.
			driver.switchTo().window(parentid); // Grabbing control back to ePost main page.

		} catch (Exception e) {
		}

		// driver.findElement(By.name("B5")).click();

		;

	}
}
