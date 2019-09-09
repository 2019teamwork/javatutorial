package CommonFunctions;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Comparator;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;

public class CommonFunction {
	static Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
	@SuppressWarnings("unused")
	public static boolean WaitforObject(WebDriver driver, WebElement element, boolean status, String objName) {
		// Waiting 1 minute for an element to be present on the page, checking for its presence once every 2 seconds.
		PropertyConfigurator.configure("Log4j.properties");// logger file

		status = false;
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(60))
					.pollingEvery(Duration.ofSeconds(2)).ignoring(NoSuchElementException.class, TimeoutException.class);
			System.out.println("WAITING FOR OBJECT UPTO 1 MINUTE" + "\r");

			WebElement foo = wait.until(new Function<WebDriver, WebElement>() {
				@Override
				public WebElement apply(WebDriver driver) {
					WebElement elem = element;

					try {
						if (elem.isDisplayed()) {
							// Function is not ignoring NoSuchElementException, there is no way to return
							// the value outside try .. catch block. Using a notepad status.txt to store
							// values to take value outside the "apply" built in method
							ProcessorSheet.WriteToNotepad("\\status.txt", "true");
							return elem;
						} else {
							System.out.println("Waiting for Object");
							return null;
						}
					} catch (Exception e) {
						System.out.println("... Waiting for Object........");
						return null;
					}
				}
			});

			String readStatus = "";
			try {
				logger.info("	....... Object (" + objName + ") Found ........" + "\r");
				// If the value is true from status.txt, then assign boolean true to status
				readStatus = ProcessorSheet.ReadNotepad("\\status.txt", readStatus);
				if (readStatus.contains("true")) {
					status = true;
				}
			} catch (Exception e) {
				System.out.println("	.......Waiting for Object (" + objName + ") ........");
			}
		} catch (Exception e) {
			logger.error("Object NOT Found.......Object NOT Found........Object NOT Found........Object NOT Found");
		}
		return status;
	}

	public static void DeleteFolderContents(String folderPath) {

//		File file = new File("C:\\Users\\txm9414\\AppData\\Local\\Temp");
//		for (File file : folder.listFiles()) {
//			if (file.isDirectory())
//				DeleteAllContents();
//			file.delete();
//		}

		try {
			Files.walk(Paths.get(folderPath.toString())).sorted(Comparator.reverseOrder()).map(Path::toFile)
					.filter(item -> !item.getPath().equals(folderPath.toString())).forEach(File::delete);
		} catch (IOException e) {

		}
	}

	public static void WaitUntilDisappear(WebDriver driver, WebElement element, String objName) {
		try {
			Thread.sleep(3000);
			System.out.println("		............WAITING FOR " + objName + " TO DISAPPEAR............" + "\r");
			WebDriverWait wait = new WebDriverWait(driver, 3000);
			wait.until(ExpectedConditions.invisibilityOf(element));
		} catch (InterruptedException e) {
		}
	}

	public static String AddDate() {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date date = new Date();

		return dateFormat.format(date);
	}

	public static boolean WaitforObject_Enable(WebElement element, boolean status) {
	int count = 0;
		status = false;
		while (!element.isEnabled() && count <= 3) {
		try {
			Thread.sleep(2000);
				count++;
		} catch (InterruptedException e) {}
	}
		if (element.isEnabled()) {
			status = true;
		}
		return status;
}
}
