package nms.stat;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nms.RowData;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PnCountLoader {
	
	
	public static Map<String, Integer> pnToCount = new HashMap<String, Integer>();
	
	public static String fDir = "D:/ForBdcom/财务盘库统计/";
	public static String[][] fileAndXy =new String[][] {
		//sheet num , startrow , pncol , countcol
		{ fDir+"303186库仓库预盘.xlsx" , "0,1,2,5" , "1,1,0,4", "2,1,1,4",  }, 
		
//		{ fDir+"303186盘点汇总.xlsx" , "0,3,1,4" , "1,3,0,1",  },
//		{ fDir+"30库简包板卡现存量查询.xlsx" , "0,1,1,4" , "1,1,1,4",},
	};
	
	public static void main(String[] args) {
		
		load();
		
		Set<Entry<String,Integer>> entrySet = pnToCount.entrySet();
		for(Entry<String,Integer> en : entrySet   ){
			System.out.println( en.getKey() + "   ,   " + en.getValue()   );
		}
		
		
	}
	
	
	public static void load(){
		for( String[] a :   fileAndXy ){
			ExcelFileObj excelFileObj = new ExcelFileObj(a);
			List<RowData> parse = excelFileObj.parse();
			for(  RowData rd :parse  ){
				
				String pn = rd.getMaterialNum().trim().toUpperCase();
				int  cc = rd.getStatcount();
			
				Integer integer = pnToCount.get(pn);
				if( integer == null ){
					pnToCount.put(pn, cc);
				}else{
					pnToCount.put(pn,  cc + integer );
				}
			}
		}
	}
	
	
}
