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
import nms.newstat.LoadPnInfos;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PnCountLoader {
	
	
	public static Map<String, Integer> u8pnToCount = new HashMap<String, Integer>();
	public static Map<String, Integer> storepnToCount = new HashMap<String, Integer>();
	
	public static String fDir = "D:/ForBdcom/U8DATA/";
	public static String[][] u8SrcFileInfo =new String[][] {
		//sheet num , startrow , pncol , countcol
		{ fDir+"30.XLSX" , "0,1,4,11"  }, 
		{ fDir+"31.XLSX" , "0,1,4,11"  }, 
		{ fDir+"86.XLSX" , "0,1,4,11"   }, 
		
	};
	
	public static String fDir2 = "D:/ForBdcom/U8DATA/";
	public static String[][] srcFileInfo =new String[][] {
		//sheet num , startrow , pncol , countcol
		{ fDir2+"store.xlsx" , "0,0,2,5"  }, 
	};
	
	public static void main(String[] args) {
		//加载启用SN 管理的PN
		LoadPnInfos.loadPNStatus(LoadPnInfos.fpath);
		Set<String> pns =  LoadPnInfos.pnInSNManage;
//		PnCountLoader.u8pnToCount = load( PnCountLoader.u8SrcFileInfo );
		
		
		PnCountLoader.storepnToCount = load( PnCountLoader.srcFileInfo );
		
		int sum = 0; 
		Set<Entry<String,Integer>> entrySet = storepnToCount.entrySet();
		for(Entry<String,Integer> en : entrySet   ){
			Integer value = en.getValue();
			String upperCase = en.getKey().toUpperCase();
			if( pns.contains( upperCase ) ){
				sum += value;
			}
			System.out.println( upperCase + "   ,   " +   value );
		}
		
		System.out.println(  "总数：" + sum  );
		
	}
	
	
	public static Map<String, Integer> load(String[][] fileInfos){
		
		Map<String, Integer> pnToCount = new HashMap<String, Integer>();
		
		for( String[] a :   fileInfos ){
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
		
		return pnToCount;
	}
	
	
}
