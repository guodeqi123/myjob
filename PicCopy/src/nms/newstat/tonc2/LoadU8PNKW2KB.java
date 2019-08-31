package nms.newstat.tonc2;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import nms.RowData;
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

public class LoadU8PNKW2KB {
	
	public final static String U8KWMappingFile = "D:/ForBdcom/0stat1/Data0831/onlyWuWeiLeiBie.xlsx";
	
	public static Map<String , String> kwpnTOkb = new HashMap<String, String>();
	
	public static void load() {
		
		Workbook wb = null;
		try {
			File file = new File( U8KWMappingFile );
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
		Sheet sheet = wb.getSheetAt(0);
		int lastRowNum = sheet.getLastRowNum();
		Row oneRow = null;
		int cc1 = 0;
		for(  int i=1 ; i<=lastRowNum ; i++ ){
			oneRow = sheet.getRow(i);
			if(  oneRow == null ){
			 	continue;
			}
			Cell kwCell = oneRow.getCell(0);
			Cell pnCell = oneRow.getCell(1);
			Cell kbCell = oneRow.getCell(2);
			
			String kwStr = Convertor2.getCellValue(kwCell);
			String pnStr = Convertor2.getCellValue(pnCell);
			String kbStr = Convertor2.getCellValue(kbCell);
			
			kwStr = kwStr.toUpperCase().trim();
			pnStr = pnStr.toUpperCase().trim();
			String  key =  kwStr + FPath.sep1 + pnStr;
			
			if( kwpnTOkb.containsKey( key)  ){
//				System.out.println(  key  );
			}
			kwpnTOkb.put(  key , kbStr);
			if(  !kwStr.startsWith( FPath.KW )  ){
				kwpnTOkb.put(   FPath.KW + kwStr + FPath.sep1 + pnStr, kbStr);
			}
		}
		
		System.out.println( "LoadU8PNKW2KB 库位_PN TO 库别 , 映射个数:" +  kwpnTOkb.size() ); 
	}
	
	public static String getU8KbByRowData(  RowData snInfo , String nckczz ){
		
		String pn = snInfo.getMaterialNum();
		String kw = snInfo.getPosNum();
		String key = kw + FPath.sep1 + pn;
		
		String retKB = kwpnTOkb.get(key);
		
		return retKB;
	}
	
	public static void main(String[] args) {
		
		load();
		
	}
	
}
