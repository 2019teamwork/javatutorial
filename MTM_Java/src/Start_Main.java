import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import CommonFunctions.CommonFunction;
import CommonFunctions.ProcessorSheet;

public class Start_Main {

	public static WebDriver driver = null;

	static Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

	@Test
	public static void Environment() throws Exception {

		// --------------------------------------------------------------------------

		// --------------------------------------------------------------------------

		PropertyConfigurator.configure("Log4j.properties");

		Runtime.getRuntime().exec("taskkill //F //PID  chromedriver.exe");// Kill chromedriver.exe process which is not
																			// released from memory

		ProcessorSheet.WriteToNotepad("\\Processor.txt", "");// Clear contents in Processor notepad
		ProcessorSheet.WriteToNotepad("\\TempResults.txt", "");// Clear contents in TempResults notepad
		ProcessorSheet.WriteToNotepad("\\log\\testlog.log", "");// Clear contents in testlog.log notepad

		File projFolder = Globals.projFolder;// Project folder;

		// Clearing Temp files created by Eclipse and also by the Screen shot capture process
		CommonFunction.DeleteFolderContents(Globals.userFolder + "\\AppData\\Local\\Temp");

		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(projFolder + "\\Config.properites");// Data definitions in Config.properites
		prop.load(fis);

		Arrays.stream(new File(projFolder + "\\TestCaseFiles" + "\\" + prop.getProperty("testcaseName") + "\\Results")
				.listFiles()).forEach(File::delete);// Deleting all files under Results folder of corresponding Test case

		String appEnv = "";
		String env = prop.getProperty("env");// Getting QA or PROD from from DataParameter.properties file
		if (env.equalsIgnoreCase("qa")) {
			env = "urlQA";
			appEnv = prop.getProperty("appQA_URL");
			logger.info("QA Environment is Selected");
		} else {
			env = "urlProd";
			appEnv = prop.getProperty("appQA_PROD");
		}

		projFolder = projFolder.getParentFile();// Workspace folder

		// Select corresponding browser defined in DataParameter.properties file
		String browser = prop.getProperty("browser");

		if (browser.equalsIgnoreCase("ie")) {
			logger.info("Opening IE Browser");
			System.setProperty("webdriver.ie.driver", projFolder + "\\WebDrivers" + "\\IEDriverServer.exe");// IEDriverServer.exe

			driver = new InternetExplorerDriver(); // Open IE browser

		} else if (browser.equalsIgnoreCase("chrome")) {
			logger.info("Opening Chrome Browser");

			/*- Comment openNewBrowser line in Config.properties after you open browser with specific port while writing codes. This will use the same browser instance */
			if (prop.getProperty("openNewBrowser") != null) {
				OpenBrowserSelectedPort();// Open Chrome browser at port 9222
			}

			System.setProperty("webdriver.chrome.driver", projFolder + "\\WebDrivers" + "\\chromedriver.exe");// ChromeDriverServer.exe
			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
			driver = new ChromeDriver(options);

//			Log4jUtil.logMessage("Chrome browser Opened");
//			driver = new ChromeDriver();
		}

		/*- Comment openNewBrowser line in Config.properties after you open browser with specific port while writing codes. This will use the same browser instance */
		@SuppressWarnings("unused")
		boolean prodChecked = false;
		if(prop.getProperty("browser").equalsIgnoreCase("chrome")) {
			if (prop.getProperty("openNewBrowser") != null) {
				SelectEnvironment(env, appEnv, prop);
				prodChecked = true;
			}
		}
		else
			SelectEnvironment(env, appEnv, prop);

		// Double checking if environment is qa, Specific to my application MTM
		if (prop.getProperty("env").equalsIgnoreCase("qa") && prop.getProperty("browser").equalsIgnoreCase("chrome")) {
				if (prop.getProperty("application_Name").equalsIgnoreCase("mtm")) {
						if (!driver.getCurrentUrl().startsWith("https://qa-")) {
							ProdAlert();
							System.out.println("\r");
							logger.info("ENVIRONMENT DEFINED IN CONFIG.PROPERTIES IS QA, BUT CURRENT URL IS PROD --- EXITING SCRIPT EXECUTION");
							System.out.println("\r");
							driver.close();
							driver.quit();
							System.exit(0);
						}
					}
				}
			else {
				if (prodChecked = false) {
					ProdAlert();
				}
			}

		Processor.MainProcessor(); // Transfer control to Processor

	}

	public static void ProdAlert() throws InterruptedException {
		System.out.println("******************** PROD Environment is Selected ********************");

		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			js.executeScript("alert('Environment Selected is PROD, Click OK to CONFIRM');");
			WebDriverWait wait = new WebDriverWait(driver, 0);

			while (wait.until(ExpectedConditions.alertIsPresent()) != null) {
				System.out.println("Please confirm PROD environment");
				Thread.sleep(2000);
			}

		} catch (Exception e) {
		}
		logger.info("PROD ENVIRONMENT CONFIRMED");
	}

	public static void SelectEnvironment(String env, String appEnv, Properties prop) throws InterruptedException {
		if (env == "urlProd")// Wait for browser to open to inject Java script
			ProdAlert();

		if (appEnv != null) {
			driver.get(appEnv);// Open the URL defined in DataParameter.properties file.
		} else
			/*- Some application has to navigate through Humana page only. Open QA or PROD Humana environment.*/
			driver.get(prop.getProperty(env));
	}

	public static void OpenBrowserSelectedPort() throws IOException, InterruptedException {

		// To open Chrome browser from command prompt
		String path = "start chrome.exe --remote-debugging-port=9222 --user-data-dir=" + "\"C:\\selenium\\Automation\"";
		path = path.replace("\\", "\\");// this substitute double backslashes with single backslash

		Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"" + path + "");
		Thread.sleep(2000);
		logger.info("Chrome browser with Specific port is opened");

		try {
			Runtime.getRuntime().exec("taskkill /f /im cmd.exe");// kill the command prompt
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
