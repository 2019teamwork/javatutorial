package CommonFunctions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.IntStream;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ProcessorSheet {

	// File(System.getProperty("user.dir")).getParentFile().toString();
	static File notepadPath = new File(System.getProperty("user.dir"));
	static String results = "";

	public static void WriteToNotepad(String notepadName, String val) {
		try {

			FileWriter fw = new FileWriter(notepadPath + notepadName);
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write(val + "\r");
			bw.newLine();

			bw.close();
		} catch (Exception e) {

		}
	}

	public static void WriteResults(boolean iStatus) {
		String readProcessor = "";
		try {
			readProcessor = ReadNotepad("\\Processor.txt",
					readProcessor);/*-Reading values already present in Processor.txt*/
		} catch (Exception e) {
		}

		String readTempResults = "";
		try {
			readTempResults = ReadNotepad("\\TempResults.txt",
					readTempResults);/*-Reading values already present in TempResults.txt*/
			readTempResults = readTempResults.replace("Failed Test - ", "");
		} catch (Exception e) {
		}

		if (iStatus == true) {
			results = "PASS";
			System.out.println("\r" + readTempResults + "   ---- >>>  PASSED" + "\r");
			// AutoClose.SelfMessageBox(readTempResults + " ---- >>> PASSED", "Test//
			// Result");

		} else {
			results = "FAIL";
			System.out.println("\r" + readTempResults + "   ---- >>>  FAILED ---- >>>" + "\r");
		}

		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date date = new Date();

		// Writing the pass or fail results back to Processor.txt along with the old
		// contents
		WriteToNotepad("\\Processor.txt",
				readProcessor + "\r" + readTempResults + "," + dateFormat.format(date) + "," + results + "&");

	}

	public static String ReadNotepad(String notepadName, String val) throws Exception {
		FileReader fr = new FileReader(notepadPath + notepadName);

		BufferedReader br = new BufferedReader(fr);
		String x = "";
		while ((x = br.readLine()) != null) {
			val = val + x;
		}
		br.close();
		return val;
	}

	@SuppressWarnings({})
	public static void WriteFinalResultsToExcel(String sShots_Path) throws Exception {

		@SuppressWarnings("resource")
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = (XSSFSheet) wb.createSheet("Results");

		// Font color for green
		CellStyle green = wb.createCellStyle();
		Font font = wb.createFont();
		font.setColor(HSSFColor.HSSFColorPredefined.GREEN.getIndex());
		font.setBold(true);
		green.setFont(font);

		// Font color for red
		CellStyle red = wb.createCellStyle();
		Font font1 = wb.createFont();
		font1.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
		font1.setBold(true);
		red.setFont(font1);

		// Titles
		Row row = sheet.createRow(0);
		Cell cell = row.createCell(0);

		CellStyle grey = wb.createCellStyle();
		Font font3 = wb.createFont();
		font3.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
//		grey.setFillPattern(FillPatternType.FINE_DOTS);
//		grey.setFillBackgroundColor(IndexedColors.BLACK1.getIndex());
		font3.setBold(true);
		grey.setFont(font3);

		row.createCell(0).setCellValue("Test Case");
		row.createCell(1).setCellValue("Steps");
		row.createCell(2).setCellValue("Date & Time");
		row.createCell(3).setCellValue("RESULT");

		for (int i = 0; i < row.getLastCellNum(); i++) {// For each cell in the row
			row.getCell(i).setCellStyle(grey);// Set the style
		}

		FileOutputStream fileOut = null;
		fileOut = new FileOutputStream(sShots_Path + "\\TestResults.xlsx");

		String readProcessor = "";

		readProcessor = ReadNotepad("\\Processor.txt",
				readProcessor);/*-Reading values already present in Processor.txt*/

		if (readProcessor != "") {

			String[] val = readProcessor.split("&");

			int arraySize = val.length;
			String[] cellVal;

			for (int count = 0; count <= arraySize - 1; count++) {

				sheet = (XSSFSheet) wb.getSheet("Results");
				row = sheet.createRow(count + 1);

				cellVal = val[count].split(",");
				for (int cells = 0; cells <= 3; cells++) {
					row.createCell(cells).setCellValue(cellVal[cells]);
					if (cellVal[cells].toString().trim().equals("PASS")) {
						cell = row.createCell(3);
						row.createCell(cells).setCellValue(cellVal[cells]);
						cell.setCellStyle(green);

					} else if (cellVal[cells].toString().trim().equals("FAIL")) {
						cell = row.createCell(3);
						row.createCell(cells).setCellValue(cellVal[cells]);
						cell.setCellStyle(red);
					}
				}
			}

		}

		XSSFSheet sheet1 = (XSSFSheet) wb.getSheet("Results");
//		for (int colCount = 0; colCount <= 4; colCount++) {
//			sheet.autoSizeColumn(colCount);
//		}
		IntStream.range(0, 4).forEach((columnIndex) -> sheet1.autoSizeColumn(columnIndex));
		wb.write(fileOut);

		fileOut.close();
	}
}
