package nms.newstat;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import nms.KWObj;
import nms.newstat.pnc.PNCompareObj;

public class LoadPNCompare {
	
	
	public static Map<String, PNCompareObj> PNCompareMapU8Exist = new HashMap<String, PNCompareObj>();
	public static Map<String, PNCompareObj> PNCompareMapU8NotExist = new HashMap<String, PNCompareObj>();
	
	
	public static void load(){
		
		try {
			File file = new File(FPath.PNComparePath);
			InputStream input = new FileInputStream(file);
			String fileExt = file.getName().substring( file.getName().lastIndexOf(".") + 1);
			Workbook wb = null;
			Sheet sheet = null;
			// 根据后缀判断excel 2003 or 2007+
			if ("xls".equals(fileExt)) {
				wb = (HSSFWorkbook) WorkbookFactory.create(input);
			} else {
				wb = new XSSFWorkbook(input);
			}
			// 获取excel sheet总数
			int sheetNumbers = wb.getNumberOfSheets();

			sheet = wb.getSheetAt( 0 );
			String sheetName = sheet.getSheetName();
			Row row = null;
			int rownum = sheet.getLastRowNum();
			for (int i = 1; i <= rownum; i++) {
				row = sheet.getRow(i);
				Cell cellSrcPn = row.getCell(1);
				Cell cellEnable = row.getCell(2);
				Cell celltoPn = row.getCell(5);
				
				String srcPn = Convertor2.getCellValue(cellSrcPn);
				String enable = Convertor2.getCellValue(cellEnable);
				String toPn = Convertor2.getCellValue(celltoPn);
				
				if(  !StringUtils.isEmpty(toPn)  ){
					srcPn = srcPn.toUpperCase().trim();
				}
				
				if( !StringUtils.isEmpty(toPn) ){
					toPn = toPn.trim().toUpperCase();
					if( toPn.endsWith("_###") ){
						toPn = toPn.split("_")[0];
					}
				}
				
				boolean u8Exist = true;
				if( "N".equals(enable.toUpperCase().trim()) ){
					u8Exist = false;
					PNCompareObj pnCompareObj = new PNCompareObj(srcPn, toPn, u8Exist);
					PNCompareMapU8NotExist.put( srcPn , pnCompareObj );
//					System.out.println( "@@@@=" + srcPn);
				}else{
					PNCompareObj pnCompareObj = new PNCompareObj(srcPn, toPn, u8Exist);
					PNCompareMapU8Exist.put( srcPn , pnCompareObj );
				}
				
			}
			
		} catch (Exception e) {
			System.err.println(   "LoadPNCompare:::" + e.getMessage()    );
		}  
		
	}
	
	public static void main(String[] args) {
		
		load();
		
		System.out.println(  "不在U8系统的物料编码个数：：" +PNCompareMapU8NotExist.size()  );
		System.out.println( "在U8系统的物料编码个数：："+ PNCompareMapU8Exist.size()  );
		
	}
	
	
}
