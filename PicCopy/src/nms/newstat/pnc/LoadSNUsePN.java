package nms.newstat.pnc;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import nms.newstat.Convertor2;
import nms.newstat.FPath;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LoadSNUsePN {
	
	public static Map<String, String> snToPN = new HashMap<String, String>();
	
	public static void load(){
		
		Workbook wb = null;
		try {
			File file = new File( FPath.SNUSEPN );
			InputStream input = new FileInputStream(file);
			String fileExt = file.getName().substring( file.getName().lastIndexOf(".") + 1);
			wb = null;
			// 根据后缀判断excel 2003 or 2007+
			if ("xls".equals(fileExt)) {
				wb = (HSSFWorkbook) WorkbookFactory.create(input);
			} else {
				wb = new XSSFWorkbook(input);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 获取excel sheet总数
		int sheetNumbers = wb.getNumberOfSheets();
		Sheet sheet = wb.getSheetAt(0);
		int lastRowNum = sheet.getLastRowNum();
		Row oneRow = null;
		int cc1 = 0;
		for(  int i=1 ; i<=lastRowNum ; i++ ){
			oneRow = sheet.getRow(i);
			Cell srcSN = oneRow.getCell(0);
			Cell toUsePN = oneRow.getCell(2);
			String sn = Convertor2.getCellValue(srcSN);
			String topn = Convertor2.getCellValue(toUsePN);
			
			if( StringUtils.isEmpty(sn ) || StringUtils.isEmpty(topn )  ){
				continue;
			}
			snToPN.put(  sn  ,  topn  );
		}
		System.out.println( "LoadSNUsePN ， SN固定PN个数：：  "  + snToPN.size()  );
	}
	
	
	public static void main(String[] args) {
		load();
	}
	
	
}
