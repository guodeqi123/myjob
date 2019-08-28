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
import nms.newstat.Convertor2;
import nms.newstat.FPath;
import nms.newstat.LoadPnInfos;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
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
		{ fDir2+"store2.xlsx" , "0,0,2,5"  }, 
	};
	
	public static void main2(String[] args) {
		//加载启用SN 管理的PN
		LoadPnInfos.loadPNStatus();
		Set<String> pns =  LoadPnInfos.pnInSNManage;
		PnCountLoader.u8pnToCount = load( PnCountLoader.u8SrcFileInfo );
		System.out.println( u8pnToCount.size() );
		
//		PnCountLoader.storepnToCount = load( PnCountLoader.srcFileInfo );
		
//		int sum = 0; 
//		Set<Entry<String,Integer>> entrySet = storepnToCount.entrySet();
//		for(Entry<String,Integer> en : entrySet   ){
//			Integer value = en.getValue();
//			String upperCase = en.getKey().toUpperCase();
//			if( pns.contains( upperCase ) ){
//				sum += value;
//			}
//			System.out.println( upperCase + "   ,   " +   value );
//		}
//		System.out.println(  "总数：" + sum  );
		
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

	
	
	public static Map<String, StorePNObj> pnToKWCount = new HashMap<String, StorePNObj>();
	public static void loadStoreData() {
		int pnCol = 2;
		int kwCol = 4;
		int countCol = 5;
		int startRow = 3;
		
		Workbook wb = null;
		try {
			File file = new File(FPath.storePath);
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
		Sheet sheet0 = wb.getSheetAt(0);
		int lastRowNum = sheet0.getLastRowNum();
		Row oneRow = null;
		int cc1 = 0;
		for(  int i=startRow ; i<=lastRowNum ; i++ ){
			oneRow = sheet0.getRow(i);
			Cell pncell = oneRow.getCell(pnCol);
			Cell kwcell = oneRow.getCell(kwCol);
			Cell countcell = oneRow.getCell(countCol);
			String pnStr = Convertor2.getCellValue(pncell);
			String kwStr = Convertor2.getCellValue(kwcell);
			String countStr = Convertor2.getCellValue(countcell);
			if( StringUtils.isEmpty(pnStr) ){
				continue;
			}
			pnStr = pnStr.trim().toUpperCase();
			if( "12O".equals(countStr) ){
				countStr = "120";
			}	else if( "I".equals(countStr) ){
				countStr = "1";
			}else if( StringUtils.isEmpty(countStr) ){
				countStr = "0";
			}
			int iCount = Integer.parseInt(countStr);
			StorePNObj storePNObj = pnToKWCount.get(pnStr);
			if(  storePNObj==null ){
				storePNObj = new StorePNObj(pnStr);
				pnToKWCount.put(pnStr , storePNObj ) ;
			}
			cc1++;
			storePNObj.addKwCount(kwStr, iCount);;
		}
		
		int rows = 0;
		Set<Entry<String,StorePNObj>> entrySet = pnToKWCount.entrySet();
		for(  Entry<String,StorePNObj> en : entrySet ){
			String key = en.getKey();
			StorePNObj value = en.getValue();
//			System.out.println(   "加载自盘数据:" +  key + " , " + value.getSumCount() + " , " + value.getKWCountStr() );
		}
		System.out.println(  "加载自盘数据共行::" +  cc1);
	}


	public static Map<String, U8PNObj> pnToU8Obj = new HashMap<String, U8PNObj>();
	public static void loadU8Data() {
		loadFile( FPath.u8PathNew , 1 );
		if( !StringUtils.isEmpty(FPath.u8PathOld) ){
			loadFile( FPath.u8PathOld  ,2 );
		}
	}
	
	
	private static void loadFile(String fPath , int isnew) {
		
		Workbook wb = null;
		try {
			File file = new File(fPath);
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
		Sheet sheet0 = wb.getSheetAt(0);
		int lastRowNum = sheet0.getLastRowNum();
		Row oneRow = null;
		
		int startrow = 3;
		int pncol = 1;
		int countcol = 4;
		int cc1 = 0 ;
		for(  int i=startrow ; i<lastRowNum ; i++  ){
			oneRow = sheet0.getRow(i);
			Cell pncell = oneRow.getCell(pncol);
			Cell countcell = oneRow.getCell(countcol);
			String pnStr = Convertor2.getCellValue(pncell);
			String countStr = Convertor2.getCellValue(countcell);
			if(  StringUtils.isEmpty(pnStr) ){
				continue;
			}
			pnStr = pnStr.trim().toUpperCase();
			if( StringUtils.isEmpty(countStr) ){
				countStr = "0";
			}
			U8PNObj u8pnObj = pnToU8Obj.get(pnStr);
			if( u8pnObj ==null){
				u8pnObj = new U8PNObj(pnStr);
				pnToU8Obj.put( pnStr , u8pnObj);
			}
			
			int parseInt = Integer.parseInt(countStr);
			if( 1==isnew ){
				u8pnObj.addNewValue(parseInt);
			}else{
				u8pnObj.addOldValue(parseInt);
			}
			cc1++;
		}
		
		System.out.println( "加载U8数据行："+fPath + " , "+cc1);
		
	}


	public static void main(String[] args) {
		
		loadStoreData();
		loadU8Data();
		
	}
	
}
