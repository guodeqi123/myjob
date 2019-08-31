package nms.newstat.tonc;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import nms.newstat.Convertor2;
import nms.newstat.FPath;
import nms.newstat.LoadPnInfos;
import nms.stat.U8PNObj;

public class AllU8DataCheck {
	
	public static Map<String, Object> u8InfoMap = new HashMap<String, Object>();
	
	public static void load() {
		
		for(String[] fileInfo : FPath.u8DataPath){
			// fname ,  sheet num ,   仓库编码col ,   pn col ,  现存数量 col  ,   可用数量 col   , 
			parseFile(fileInfo);
		}
		System.out.println(  "AllU8DataCheck :U8总PN " + u8InfoMap.size()  );
	}
	
	private static void parseFile(String[] fileInfo) {
		// fname ,  sheet num ,   仓库编码col ,   pn col ,  现存数量 col  ,   可用数量 col   , 
		String fpath = fileInfo[0];
		
		Workbook wb = null;
		try {
			File file = new File(fpath);
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
		int sheetNum = Integer.parseInt(fileInfo[1] );
		int startRow = Integer.parseInt(fileInfo[2]) ;
		
		int storeNumCol = Integer.parseInt(fileInfo[3]) ;
		int pnCol = Integer.parseInt(fileInfo[4]);
		int existNumCol = Integer.parseInt(fileInfo[5]);
		int countCol = Integer.parseInt(fileInfo[6]);
		
		Sheet sheet0 = wb.getSheetAt(sheetNum);
		int lastRowNum = sheet0.getLastRowNum();
		Row oneRow = null;
		
		for(  int i=startRow ; i<lastRowNum ; i++  ){
			oneRow = sheet0.getRow(i);
			Cell storeNumCell = oneRow.getCell(storeNumCol);
			Cell pnCell = oneRow.getCell(pnCol);
			Cell existCountCell = oneRow.getCell(existNumCol);
			Cell countCell = oneRow.getCell(countCol);

			String pnStr = Convertor2.getCellValue(pnCell);
			String existCountStr = Convertor2.getCellValue(existCountCell);
			String countStr = Convertor2.getCellValue(countCell);
			if(  StringUtils.isEmpty(pnStr) ){
				continue;
			}
			if( "现存数量".equals(existCountStr)  ){
				continue;
			}
			if(  StringUtils.isEmpty(existCountStr)  ){
				existCountStr = "0";
			}
			double count = 0;
			try {
				count = Double.parseDouble(existCountStr);
				pnStr = pnStr.trim().toUpperCase();
				
//				if( "CBSROU-ROU0676C".equals(pnStr) ){
//					System.out.println( fpath + "  row : " +  (i+1));	
//				}
//				if( "SMEOU-ROU0548C".equals(pnStr) ){
//					System.out.println( fpath + "  row : " +  (i+1));	
//				}
				
				if( count > 0 ){
					u8InfoMap.put(pnStr, new Object());
				}
			} catch (Exception e) {
				System.err.println(  fpath );
				e.printStackTrace();
			}
	
		}
		
	}

	public static void main(String[] args) {
		
		LoadPnInfos.loadPNStatus();
		load();
		
		int count = 0;
		Set<Entry<String,Object>> entrySet = u8InfoMap.entrySet();
		for(  Entry<String,Object> en : entrySet  ){
			String key = en.getKey();
			if( !LoadPnInfos.pnInSNManage.containsKey(key) ){
				System.out.println( key   );
				count++;
			}
		}
		System.out.println(  "U8不在NC中的总个数"  + count);
		
	}
	
}
