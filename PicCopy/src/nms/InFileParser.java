package nms;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class InFileParser {
	private String srcFilePath;
	private String srcFileName;

	public InFileParser(String sss) {
		this.srcFilePath = sss;
		File file = new File(srcFilePath);
		this.srcFileName = file.getName().substring(0, file.getName().lastIndexOf("."));
	}

	public String getSrcFilePath() {
		return srcFilePath;
	}

	public void setSrcFilePath(String srcFilePath) {
		this.srcFilePath = srcFilePath;
	}

	public String getSrcFileName() {
		return srcFileName;
	}

	public void setSrcFileName(String srcFileName) {
		this.srcFileName = srcFileName;
	}
	
	public List<InOutObj> parseIn() {
		List<InOutObj> ret = new ArrayList<InOutObj>();

		Workbook wb = null;
		Sheet sheet;
		try {
			File file = new File(srcFilePath);
			InputStream input = new FileInputStream(file);
			String fileExt = file.getName().substring(
					file.getName().lastIndexOf(".") + 1);
			wb = null;
			sheet = null;
			// 根据后缀判断excel 2003 or 2007+
			if ("xls".equals(fileExt)) {
				wb = (HSSFWorkbook) WorkbookFactory.create(input);
			} else {
				wb = new XSSFWorkbook(input);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 获取excel sheet总数
		int sheetNumbers = wb.getNumberOfSheets();

		Row row = null;
		for (int sn = 0; sn <= sheetNumbers; sn++) {
			sheet = wb.getSheetAt(sn);
			String sheetName = sheet.getSheetName();
			int rownum = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i <= rownum; i++) {
				row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				Cell cellPn = row.getCell(0);
				Cell cellSn = row.getCell(1);
				Cell cellkw = row.getCell(2);
				if (cellPn == null || cellSn == null || cellkw == null) {
					continue;
				}
				String pnStr = Convertor.getCellValue(cellPn);
				String snStr = Convertor.getCellValue(cellSn);
				String kwStr = Convertor.getCellValue(cellkw);
				if (pnStr == null || snStr == null || kwStr == null) {
					continue;
				}
				InOutObj aa = new InOutObj();
				aa.setIsIn(true);
				aa.setPn(pnStr);
				aa.setSn(snStr);
				aa.setKw(kwStr);
				aa.setDateStr(sheetName);
				ret.add(aa);
			}
		}

		return ret;
	}
	
}
