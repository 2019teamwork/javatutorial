package CommonFunctions;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import com.google.common.io.Files;

public class Screenshot{
	
	public static void TakeScreenshots(WebDriver driver) throws Exception {
		File projFolder = new File(System.getProperty("user.dir"));// Project folder;
		
		String readTempResults = "";
		readTempResults = ProcessorSheet.ReadNotepad("\\TempResults.txt", readTempResults);/*-Reading values already present in TempResults.txt*/
		
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(projFolder + "\\Config.properites");// To get the Test case folder name
		prop.load(fis);
		
		String folderName = prop.getProperty("testcaseName");
		
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		String dest = projFolder + "\\TestCaseFiles\\" + folderName + "\\Results\\" + readTempResults + ".png";

		Files.copy(scrFile, new File(dest));
	}

}
