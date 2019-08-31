package nms.newstat.tonc2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nms.RowData;
import nms.newstat.Convertor2;
import nms.newstat.FPath;
import nms.stat.PnCountLoader;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LoadNCKWInfo {
	
	public static Map<String, String> kczzU8ToNC = new HashMap<String, String>();
	public static void load( ) {
		
		kczzU8ToNC.put("上海博达数据通信有限公司", "01");
		kczzU8ToNC.put("上海博达通信科技有限公司", "0101");
		kczzU8ToNC.put("上海博达信息技术有限公司", "010101");
		kczzU8ToNC.put("上海泰砚通信技术有限公司", "010102");
		kczzU8ToNC.put("上海迅坤数码科技发展有限公司", "0102");
		
		loadKwInfo();
		loadKczzInfo();
	}
	
	public final static String kwInfoFile = "D:/ForBdcom/0stat1/Data0831/货位导入-0830.xlsx";

	public static Map<String, NCKczzObj> ncKczzMap = new HashMap<String, NCKczzObj>(); 
	public static List<NCKwObj> ncKwList = new ArrayList<NCKwObj>();
	public static Set<String> ncKwSet = new HashSet<String>();
	
	private static void loadKwInfo() {
		
		Workbook wb = FPath.getWB(kwInfoFile);
		
		// 获取excel sheet总数
		Sheet sheet = wb.getSheetAt(0);
		int lastRowNum = sheet.getLastRowNum();
		Row oneRow = null;
		int cc1 = 0;
		for(  int i=2 ; i<=lastRowNum ; i++ ){
			oneRow = sheet.getRow(i);
			if(  oneRow == null ){
			 	continue;
			}
			Cell ncKczzCell = oneRow.getCell(1);
			Cell ncKbCell = oneRow.getCell(2);
			Cell ncKwCell = oneRow.getCell(3);
			Cell u8KwCell = oneRow.getCell(4);
			
			String ncKczzStr = Convertor2.getCellValue(ncKczzCell);
			String ncKbStr = Convertor2.getCellValue(ncKbCell);
			String ncKwStr = Convertor2.getCellValue(ncKwCell);
			String u8KwStr = Convertor2.getCellValue(u8KwCell);
			
			ncKczzStr = ncKczzStr.toUpperCase().trim();
			ncKbStr = ncKbStr.toUpperCase().trim();
			ncKwStr = ncKwStr.toUpperCase().trim();
			u8KwStr = u8KwStr.toUpperCase().trim();
			
			NCKwObj ncKwObj = new NCKwObj();
			ncKwObj.setNcKczz(ncKczzStr);
			ncKwObj.setNcKb(ncKbStr);
			ncKwObj.setNcKw(ncKwStr);
			ncKwObj.setU8Kw(u8KwStr);
			
			
			NCKczzObj ncKczzObj = ncKczzMap.get(ncKczzStr);
			if( ncKczzObj == null  ){
				ncKczzObj = new NCKczzObj(ncKczzStr);
				ncKczzMap.put( ncKczzStr , ncKczzObj ) ;
			}
			Map<String, NCKbObj> nckbToObj = ncKczzObj.getNckbToObj();
			NCKbObj ncKbObj = nckbToObj.get(ncKbStr);
			if(  ncKbObj == null  ){
				ncKbObj = new NCKbObj(ncKbStr);
				nckbToObj.put(ncKbStr, ncKbObj);
			}
			Map<String, NCKwObj> u8kwToObj = ncKbObj.getU8kwToObj();
			u8kwToObj.put(u8KwStr, ncKwObj);
			
			String key =ncKczzStr + FPath.sep1 + ncKbStr + FPath.sep1 + u8KwStr;
//			if( ncKwSet.contains(key) ){
//				System.out.println(  key  );
//			}
			ncKwSet.add( key);
			
			ncKwList.add( ncKwObj );
		}
		
		System.out.println( "loadNCKWInfo.loadKwInfo() :: NC库位数量" + ncKwList.size() + " ,去重： "  + ncKwSet.size() );
		
		NCKczzObj value = ncKczzMap.entrySet().iterator().next().getValue();
		Map<String, NCKbObj> nckbToObj = value.getNckbToObj();
		System.out.println( "loadNCKWInfo.loadKwInfo() :: NC KCZZ数量"   + ncKczzMap.size() +  " , NC库别个数"  + nckbToObj.size() );
		Set<Entry<String,NCKbObj>> entrySet = nckbToObj.entrySet();
		for(   Entry<String,NCKbObj> en :  entrySet){
			String ncTmpKb = en.getKey();
			NCKbObj ncTmpKbObj = en.getValue();
//			System.out.println(    "loadNCKWInfo.loadKwInfo() NC库别含库位数量: "+ ncTmpKb  +  "=" +  ncTmpKbObj.getU8kwToObj().size());
		}
	}

	public final static String kczzInfoFile = "D:/ForBdcom/0stat1/Data0831/仓库对照YCW.xlsx";
	public static Map<String, String> kbU8ToNC = new HashMap<String, String>();//u8  kb = > nc kb
	private static void loadKczzInfo() {
		Workbook wb = FPath.getWB(kczzInfoFile);
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
			Cell u8KbCell = oneRow.getCell(1);
			Cell ncKbCell = oneRow.getCell(3);
			
			String u8KbStr = Convertor2.getCellValue(u8KbCell);
			String ncKbStr = Convertor2.getCellValue(ncKbCell);

			u8KbStr = u8KbStr.toUpperCase().trim();
			ncKbStr = ncKbStr.toUpperCase().trim();
			if( StringUtils.isEmpty( u8KbStr ) ||  StringUtils.isEmpty( ncKbStr ) ){
				continue;
			}
			kbU8ToNC.put(u8KbStr, ncKbStr);
		}
		System.out.println(   "LoadNCKWInfo.loadKczzInfo()::  U8库别映射NC库别个数：" + kbU8ToNC.size()  );
	}

	public static NCKwObj getNCKWByRowData( RowData snInfo , String nckczz ){
		
		String pn = snInfo.getMaterialNum();
		String u8kw = snInfo.getPosNum();
		String u8KB = LoadU8PNKW2KB.getU8KbByRowData(snInfo, nckczz);
		String ncKB = kbU8ToNC.get(u8KB);
		
		NCKczzObj ncKczzObj = ncKczzMap.get(nckczz);
		Map<String, NCKbObj> nckbToObj = ncKczzObj.getNckbToObj();
		NCKbObj ncKbObj = nckbToObj.get(ncKB);
		Map<String, NCKwObj> u8kwToObj = ncKbObj.getU8kwToObj();
		NCKwObj ncKwObj = u8kwToObj.get(u8kw);
		
		if( ncKwObj ==null &&  !u8kw.startsWith( FPath.KW ) ){
			ncKwObj = u8kwToObj.get( FPath.KW + u8kw);
		}
		return ncKwObj;
	}

	public static void main(String[] args) {
		
		// sn  u8kw+pn => u8 kb  ; u8kb => nc kb
		// nckczz => nckb=> map =>  u8kw => nckw 
		LoadU8PNKW2KB.load();
		load( ) ;
		RowData snInfo = new RowData("A5016", "CBNNN-JHJ0173A", "SN");
		NCKwObj nckwByRowData = getNCKWByRowData(snInfo, "01");
		System.out.println(   nckwByRowData  );
		
	}
}	
