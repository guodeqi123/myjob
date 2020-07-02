package nms.t1028;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nms.newstat.Convertor2;
import nms.newstat.FPath;
import nms.newstat.StatRow;
import nms.newstat.tonc2.InventedSerialNumberUtil;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CreateImport {

	
	private static final String path1 = "D:/ForBdcom/1028/序列号生成.xlsx";
	
	public static Map<String, Integer> pnToCnt = new HashMap<String, Integer>();
	
	public static void loadSrc(){
		Workbook wb = FPath.getWB(path1);
		Sheet sheetAt = wb.getSheetAt(0);
		int lastRowNum = sheetAt.getLastRowNum();
		Row oneRow = null;
		for (int i = 1; i <= lastRowNum; i++) {
			oneRow = sheetAt.getRow(i);
			if (oneRow == null) {
				continue;
			}
			
			String pn = Convertor2.getCellValue( oneRow.getCell( 0 ) );
			String cnt = Convertor2.getCellValue( oneRow.getCell( 1 ) );
			
			Integer integer = pnToCnt.get(pn);
			int parseInt = Integer.parseInt(cnt);
			if( integer == null  ){
				pnToCnt.put(  pn,  parseInt    );
			}else{
				pnToCnt.put(  pn,    parseInt + integer   );
			}
		}
		
		System.out.println( pnToCnt.size()   );
		
	}
	
	private static final String  sndateStr = "19A28";
	private static final String  ncBdcomKCZZ = "01";
	
	public static void writeFile(  ){
		String[] title= { "出入库单据号","库存组织","仓库","物料编码","货位","序列号"  };
		
		XSSFWorkbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet( "sheet1" );
		Row row = sheet.createRow((int) 0);
		CellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		for( int i=0; i<title.length  ; i++ ){
			Cell cell = row.createCell((short) i);
			cell.setCellValue(title[i]);
			cell.setCellStyle(style);
		}
		
		int rcount = 1;
		
		Set<Entry<String,Integer>> entrySet = pnToCnt.entrySet();
		
		
		for(Entry<String,Integer> en : entrySet ){
			String tmpPN = en.getKey();
			Integer tcnt = en.getValue();
			
			for( int i=0; i<tcnt ;i++  ){
				Row row2 = sheet.createRow( rcount++ );
				Cell cell2 = null;
				
				cell2 = row2.createCell(0);
				cell2.setCellStyle(style);
				cell2.setCellValue( "");
				
				cell2 = row2.createCell(1);
				cell2.setCellStyle(style);
				cell2.setCellValue( "1");
				
				cell2 = row2.createCell(2);
				cell2.setCellStyle(style);
				cell2.setCellValue( "成品库");
				
				cell2 = row2.createCell(3);
				cell2.setCellStyle(style);
				cell2.setCellValue( tmpPN );
				
				cell2 = row2.createCell(4);
				cell2.setCellStyle(style);
				cell2.setCellValue( "SNINIT" );
				
				cell2 = row2.createCell(5);
				cell2.setCellStyle(style);
				
				String vSN = InventedSerialNumberUtil.getInventedSerialNumber( ncBdcomKCZZ , "10", "SNINIT", sndateStr);
				
				cell2.setCellValue( vSN );
			}
			
		}
		
		FileOutputStream fout = null;
		try {
			File file = new File("res/ff1028.xlsx");
			if( file.exists()){
				file.delete();
			}
			file.createNewFile();
			fout = new FileOutputStream(file);
			wb.write(fout);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static void writeFile( String pn , int count ){
		String[] title= { "出入库单据号","库存组织","仓库","物料编码","货位","序列号"  };
		
		XSSFWorkbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet( "sheet1" );
		Row row = sheet.createRow((int) 0);
		CellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		for( int i=0; i<title.length  ; i++ ){
			Cell cell = row.createCell((short) i);
			cell.setCellValue(title[i]);
			cell.setCellStyle(style);
		}
		
		int rcount = 1;
		
		Set<Entry<String,Integer>> entrySet = pnToCnt.entrySet();
		
		for( int i=0; i<count ;i++  ){
			Row row2 = sheet.createRow( rcount++ );
			Cell cell2 = null;
			
			cell2 = row2.createCell(0);
			cell2.setCellStyle(style);
			cell2.setCellValue( "");
			
			cell2 = row2.createCell(1);
			cell2.setCellStyle(style);
			cell2.setCellValue( "1");
			
			cell2 = row2.createCell(2);
			cell2.setCellStyle(style);
			cell2.setCellValue( "成品库");
			
			cell2 = row2.createCell(3);
			cell2.setCellStyle(style);
			cell2.setCellValue( pn );
			
			cell2 = row2.createCell(4);
			cell2.setCellStyle(style);
			cell2.setCellValue( "SNINIT" );
			
			cell2 = row2.createCell(5);
			cell2.setCellStyle(style);
			
			String vSN = InventedSerialNumberUtil.getInventedSerialNumber( ncBdcomKCZZ , "10", "SNINIT", sndateStr);
			
			cell2.setCellValue( vSN );
		}
			
		FileOutputStream fout = null;
		try {
			File file = new File("res/PN_"+pn+".xlsx");
			if( file.exists()){
				file.delete();
			}
			file.createNewFile();
			fout = new FileOutputStream(file);
			wb.write(fout);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void createFiles(){
		Set<Entry<String,Integer>> entrySet = pnToCnt.entrySet();
		
		for(Entry<String,Integer> en : entrySet ){
			String tmpPN = en.getKey();
			Integer tcnt = en.getValue();
			
			writeFile(tmpPN,  tcnt);
		}
		
	}
	
	public static void main(String[] args) {
			
		loadSrc();
//		createFiles();
		
//		writeFile();
		
	}
	
	
}
