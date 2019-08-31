package nms.newstat;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nms.Convertor;
import nms.RowData;
import nms.stat.SheetObj;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LoadPnInfos {
	
	public static Map<String ,Boolean> pnInSNManage = new HashMap<String , Boolean>();
	public static Map<String ,String> pnToUnit = new HashMap<String , String>();
	
	public static void main(String[] args) {
		
		loadPNStatus();
		
	}

	public static void loadPNStatus( ) {
		
		Workbook wb = null;
		Sheet sheet;
		try {
			File file = new File( FPath.PNStatuspath);
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
			e.printStackTrace();
		}

		Row row = null;
		sheet = wb.getSheetAt(0);
		String sheetName = sheet.getSheetName();
		int rownum = sheet.getLastRowNum();
		for(  int i=1 ; i<=rownum  ; i++ ){
			 try {
				row = sheet.getRow(i);
				 if( row ==null ){
					 continue;
				 }
				 Cell cellPn = row.getCell( 1 );
				 Cell cellUnitNum = row.getCell( 5 );
				 Cell cellUnit = row.getCell( 6 );
				 Cell cellStatus = row.getCell( 7 );
				 
				 
				 String pnStr = Convertor.getCellValue(cellPn);
				 String status = Convertor.getCellValue(cellStatus);
				 
				 String unitNum = Convertor.getCellValue(cellUnitNum);
				 String unitStr = Convertor.getCellValue(cellUnit);
				 pnToUnit.put(pnStr, unitNum );
				 
				 String PNtrim = pnStr.toUpperCase().trim();
				 if(  status!=null && status.toUpperCase().equals("Y")  ){
					 pnInSNManage.put(  PNtrim , true );
				 }else{
					 pnInSNManage.put(  PNtrim , false );
				 }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		System.out.println(  "  启用NC管理的 物料编码个数  :"+pnInSNManage.size() );
		
	}
	
}
