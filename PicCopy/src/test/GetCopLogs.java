package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

public class GetCopLogs {
	
	public static Map<String, String> descToLevel = new HashMap<String, String>();
	
	public static  String tmpPath = "D:/ForCop/0815.docx";
	
	public static final String csvSep = "。";

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		descToLevel.put( "Emergency".toLowerCase()  , "0");
		descToLevel.put( "EMER".toLowerCase()  , "0");
		descToLevel.put( "Alert".toLowerCase()  , "1");
		descToLevel.put( "Critical".toLowerCase()  , "2");
		descToLevel.put( "Error".toLowerCase()  , "3");
		descToLevel.put( "ERR".toLowerCase()  , "3");
		descToLevel.put( "Warning".toLowerCase()  , "4");
		descToLevel.put( "Notice".toLowerCase()  , "5");
		descToLevel.put( "Information".toLowerCase()  , "6");
		descToLevel.put( "INFO".toLowerCase()  , "6");
		descToLevel.put( "Debug".toLowerCase()  , "7");
		
		XWPFDocument document = new XWPFDocument(  new FileInputStream( tmpPath  )   );
		List<XWPFTable> tables = document.getTables();
		
		Map<String, List<LogCategory>> aaa = new HashMap<String, List<LogCategory>>();
		
		int size = tables.size();
		for ( int i=1; i<size ;i++ )  {
			XWPFTable table  = tables.get(i);
		    List<XWPFTableRow> rows = table.getRows();
		    int rowSize = rows.size();
		    for (int rowIndex = 1 ;  rowIndex<rowSize ; rowIndex ++) {
		    	XWPFTableRow row = rows.get(rowIndex);
		        List<XWPFTableCell> tableCells = row.getTableCells();
		        int cellsCells = tableCells.size();
		        
		        XWPFTableCell cell1 = tableCells.get(0);
		        XWPFTableCell cell2 = tableCells.get(1);
		        XWPFTableCell cell3 = tableCells.get(2);
		        XWPFTableCell cell4 = tableCells.get(3);
		        XWPFTableCell cell5 = tableCells.get(4);
		        
		        LogCategory lc = new LogCategory( cell1.getText() , cell2.getText() ,cell3.getText() ,cell4.getText() ,cell5.getText() );
		        
		        putInMap(lc , aaa);
		    }
		}
		
		int counter1 = 0;
		int counter2 = 0;
		
		List<String> sqls = new ArrayList<String>();
		List<String> sqls2 = new ArrayList<String>();
		
		Set<Entry<String,List<LogCategory>>> entrySet = aaa.entrySet();
		for(   Entry<String,List<LogCategory>> en : entrySet  ){
			String key = en.getKey();
			List<LogCategory> value = en.getValue();
			int size2 = value.size(); 
			if(  size2 >1  ){
				 for(  int i=0; i<size2   ;i ++   ){
					 LogCategory logCategory = value.get(i);	
					 
					 String lowerCase = logCategory.level.toLowerCase();
					 String levelNum = descToLevel.get(lowerCase);
//					 System.out.println(    "key:" + logCategory.getKey() +  " ,level:" + levelNum + "      desc::" + logCategory.desc);
					 System.out.println( logCategory   );
				 }
				 System.out.println(  "  === "+csvSep+"=== "+csvSep+" === "+csvSep+"==="+csvSep+"===" );
//				 System.out.println();
				 counter2 ++;
				 
				String generalSql = generalSql(value.get(0));
				sqls2.add(generalSql);
			}else{
				counter1 ++;
				String generalSql = generalSql(value.get(0));
				sqls.add(generalSql);
			}
		}
		
		System.out.println(   "only 1 :: " +counter1 +  " , multi :: " + counter2 );
		
		createExcel(aaa);
		System.out.println(  ""  );
		for( String ss : sqls ){
			System.out.println(  ss  );
		}
		System.out.println(  "----------------------"  );
		for( String ss : sqls2 ){
			System.out.println(  ss  );
		}
		
	}
	
	public static int counterId = 1;
	
	public static String generalSql(  LogCategory lc ){
		
		String sql = " INSERT INTO `syslog_category` VALUES ('1', '2', 'mm', 'ee', 'dd', 'rr', 'ss', '0', '0', '0', null, null, null); ";
		
		
		String lowerCase = lc.level.toLowerCase();
		String levelNum = descToLevel.get(lowerCase);
		
//		sql = " INSERT INTO `syslog_category` VALUES ('"+ (counterId++) +"', '"+ levelNum +"', '"+lc.module+"', '"+lc.event+"', '"+lc.desc+"', '"+lc.reason+"', 'NoSuggest', '1', '1', '1', null, null, null); ";
		sql = " INSERT INTO `syslog_category` VALUES ('"+ (counterId++) +"', '"+ levelNum +"', '"+lc.module+"', '"+lc.event+"', 'No Desc', 'No Reason', 'NoSuggest', '1', '1', '1', null, null, null); ";
		
		return sql;
	}
	
	public static String[] title= { "级别"  , "模块" , "事件" , "描述" , "原因" , };
	
	private static void createExcel(Map<String, List<LogCategory>> aaa) {
		
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("sheet1");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		
		for( int i=0; i<title.length  ; i++ ){
			HSSFCell cell = row.createCell((short) i);
			cell.setCellValue(title[i]);
			cell.setCellStyle(style);
		}
		
		int counter3 = 1;
		Set<Entry<String,List<LogCategory>>> entrySet = aaa.entrySet();
		for(   Entry<String,List<LogCategory>> en : entrySet  ){
			String key = en.getKey();
			List<LogCategory> value = en.getValue();
			int size2 = value.size(); 
			if(  size2 >1  ){
				 for(  int i=0; i<size2   ;i ++   ){
					LogCategory logCategory = value.get(i);
					String lowerCase = logCategory.level.toLowerCase();
					String levelNum = descToLevel.get(lowerCase);
					
					HSSFRow row2 = sheet.createRow( counter3++ );
					HSSFCell cell2 = null;
					cell2 = row2.createCell(0);
					cell2.setCellValue(levelNum);
					cell2 = row2.createCell(1);
					cell2.setCellValue(logCategory.module );
					cell2 = row2.createCell(2);
					cell2.setCellValue(logCategory.event );
					cell2 = row2.createCell(3);
					cell2.setCellValue(logCategory.desc );
					cell2 = row2.createCell(4);
					cell2.setCellValue(logCategory.reason );
						
				 }
				 
				HSSFRow row2 = sheet.createRow( counter3++ );
				HSSFCell cell2 = null;
				cell2 = row2.createCell(0);
				cell2.setCellValue( "==="  );
				cell2 = row2.createCell(1);
				cell2.setCellValue( "==="  );
				cell2 = row2.createCell(2);
				cell2.setCellValue( "==="  );
				cell2 = row2.createCell(3);
				cell2.setCellValue( "==="  );
				cell2 = row2.createCell(4);
				cell2.setCellValue( "==="  );
				 
			} 
		}
		
		
		FileOutputStream fout = null;
		try {
			long currentTimeMillis = System.currentTimeMillis();
			File excelFile = new File("D:/ForCop/"+currentTimeMillis + ".xls");
			fout = new FileOutputStream(excelFile);
			wb.write(fout);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fout.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void putInMap(LogCategory lc, Map<String, List<LogCategory>> aaa) {
		String key = lc.getKey();
		
		List<LogCategory> list = aaa.get(key);
		if( list == null ){
			list = new ArrayList<LogCategory>();
			aaa.put( key, list);
		}
		list.add(lc);
	}

	static class LogCategory{
		public String level = "";
		public String module = "";
		public String event = "";
		public String desc = "";
		public String reason = "";
		
		public LogCategory(String level, String module, String event,
				String desc, String reason) {
			super();
			this.level = level;
			this.module = module;
			this.event = event;
			this.desc = desc;
			this.reason = reason;
		}


//		@Override
//		public String toString() {
//			return "LogCategory [level=" + level + ", module=" + module
//					+ ", event=" + event + ", desc=" + desc + ", reason="
//					+ reason + "]";
//		}
		
		@Override
		public String toString() {
			
			String levelNum = descToLevel.get(level.toLowerCase() );
			
			return  levelNum +csvSep + module  +csvSep+ event +csvSep +   desc +csvSep +  reason ;
		}
		
		public String getKey(){
			return module + "_"+event;
		}
		
	}
	
}
