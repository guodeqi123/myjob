package nms.newstat;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
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
	
	public static Set<String> pnInSNManage = new HashSet<String>();
	
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
				 Cell cellStatus = row.getCell( 3 );
				 
				 String pnStr = Convertor.getCellValue(cellPn);
				 String status = Convertor.getCellValue(cellStatus);
				 if(  status!=null && status.toUpperCase().equals("Y")  ){
					 pnInSNManage.add(pnStr.toUpperCase().trim());
				 }else{
					 System.out.println(  status );
				 }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		System.out.println(  "  启用SN管理的 物料编码个数  :"+pnInSNManage.size() );
		
	}
	
}
