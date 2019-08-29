package nms.newstat.pnc;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nms.newstat.Convertor2;
import nms.newstat.FPath;
import nms.stat.StorePNObj;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 加载 修正后的物料编码映射
 * @author user
 *
 */
public class LoadPNAmend {
	
	public  static Map<String, String> srcPNToPN = new HashMap<String, String>();
	
	
	public static void  load(){
		
		for(  String[] oneSheet : FPath.PNAmendFiles  ){
			String fname = oneSheet[0];
			int sheetNum = Integer.parseInt( oneSheet[1] );
			int startrow = Integer.parseInt( oneSheet[2] );
			int srcPnCol = Integer.parseInt( oneSheet[3] );
			int toPnCol = Integer.parseInt( oneSheet[4] );
			doParse(fname ,startrow, sheetNum ,srcPnCol ,toPnCol );
		}
	}

	private static void doParse(String fname, int startrow , int sheetNum, int srcPnCol, int toPnCol) {
		
		Workbook wb = null;
		try {
			File file = new File( fname );
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
		Sheet sheet = wb.getSheetAt(sheetNum);
		int lastRowNum = sheet.getLastRowNum();
		Row oneRow = null;
		int cc1 = 0;
		for(  int i=startrow ; i<=lastRowNum ; i++ ){
			oneRow = sheet.getRow(i);
			Cell srcpncell = oneRow.getCell(srcPnCol);
			Cell topnCell = oneRow.getCell(toPnCol);
			String srcpn = Convertor2.getCellValue(srcpncell);
			String topn = Convertor2.getCellValue(topnCell);
			
			if( StringUtils.isEmpty(srcpn ) || StringUtils.isEmpty(topn ) || "1".equals(topn) ){
				continue;
			}
			
			srcPNToPN.put( srcpn.trim().toUpperCase() ,  topn.trim().toUpperCase()  );
		}
		
	}
	
	
	public static void main(String[] args) {
		
		load();
	}
	
}
