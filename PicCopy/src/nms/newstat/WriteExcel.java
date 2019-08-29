package nms.newstat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import nms.RowData;
import nms.newstat.pnc.LoadPNPrice;
import nms.newstat.pnc.PNInfoObj;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WriteExcel {
	
	
	public static void createExcel(  ArrayList<StatRow> resList ,  String fname ){
		
		String[] title= { "PN ", "扫描数量" ,"自盘数量" , "U8数量" , "U8变动数量" ,
				"扫码涉及库位", "自盘涉及库位" , "存货名称" , "单价"  };
		
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
		int size = resList.size();
		for( int i=0; i<size ;i++ ){
			StatRow statRow = resList.get(i);
			Row row2 = sheet.createRow( rcount++ );
			Cell cell2 = null;
			
			cell2 = row2.createCell(0);
			cell2.setCellStyle(style);
			cell2.setCellValue( statRow.getPn() );
			
			cell2 = row2.createCell(1);
			cell2.setCellStyle(style);
			cell2.setCellValue(  statRow.getCountScan() );
			
			cell2 = row2.createCell(2);
			cell2.setCellStyle(style);
			cell2.setCellValue(  statRow.getCountStore() );
			
			cell2 = row2.createCell(3);
			cell2.setCellStyle(style);
			cell2.setCellValue(  statRow.getCountU8() );
			
			cell2 = row2.createCell(4);
			cell2.setCellStyle(style);
			cell2.setCellValue(  statRow.getU8increase() );
			
			String kwStrs = GatherAll.getKWInfo(statRow.getKwCountMap());
			cell2 = row2.createCell(5);
			cell2.setCellStyle(style);
			cell2.setCellValue(  kwStrs );
			
			String kwForStore = statRow.getStoreKwCountStr();
			if( kwForStore ==null ){
				kwForStore = "";
			}
			cell2 = row2.createCell(6);
			cell2.setCellStyle(style);
			cell2.setCellValue(  kwForStore );
			
			PNInfoObj pnInfoObj = LoadPNPrice.pnToObj.get(statRow.getPn());
			String pnName = "";
			double pnPrice = -1;
			if( pnInfoObj != null ){
				pnName = pnInfoObj.getPnName();
				pnPrice = pnInfoObj.getPrice();
			}
			cell2 = row2.createCell(7);
			style.setWrapText(true);
			cell2.setCellStyle(style);
			cell2.setCellValue(  pnName );
			cell2 = row2.createCell(8);
			cell2.setCellStyle(style);
			cell2.setCellValue(  pnPrice );
			
		}
		
		int upvalue = 32;
		sheet.setColumnWidth(0, 170 *upvalue );
		sheet.setColumnWidth(1, 100  *upvalue );
		sheet.setColumnWidth(2, 100 *upvalue);
		sheet.setColumnWidth(3, 100 *upvalue);
		sheet.setColumnWidth(4, 130 *upvalue );
		sheet.setColumnWidth(5, 230*upvalue );
		sheet.setColumnWidth(6, 230*upvalue );
		sheet.setColumnWidth(7, 300*upvalue  );
		sheet.setColumnWidth(8, 90*upvalue );
		
		FileOutputStream fout = null;
		try {
			File file = new File("res/" + fname);
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
	
	
	
}
