package nms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nms.seqparser.AccessDBParser;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Convertor {
	

	private static List<RowData> parseExcel2() throws InvalidFormatException, IOException {
		File file = new File( srcPath );
		InputStream input = new FileInputStream(file);
		String fileExt = file.getName().substring(file.getName().lastIndexOf(".") + 1);
		Workbook wb = null;
		Sheet sheet = null;
		// 根据后缀判断excel 2003 or 2007+
		if ( "xls".equals(fileExt) ) {
			wb = (HSSFWorkbook) WorkbookFactory.create(input);
		} else {
			wb = new XSSFWorkbook(input);
		}
		// 获取excel sheet总数
		int sheetNumbers = wb.getNumberOfSheets();
		
		sheet = wb.getSheetAt(0);
		List<RowData> srcRows = null;
		 getRows2(sheet);
		 
		 return null;
	}

	public static String path  = "D:/ForBdcom/toRes/";
//	public static String srcPath1  = "D:/ForBdcom/aaa.xls";
	public static String srcPath2  = path + "atest0809.xlsx";
	
	public static String srcPath  = srcPath2 ;
	
	public static void main(String[] args) throws InvalidFormatException, IOException {
		
		AccessDBParser.doInit();
		parseExcel2();
		writeSuccess2();
		writeError2();

	}
 
	public static int kwCol = 0;
	public static int pnCol = 1;
	public static int snCol = 2;
	public static int countCol = 3;
	
	public static int startRow = 1;
	
	public static List<KWObj> kwErr = new ArrayList<KWObj>();
	public static List<KWObj> kwAllSuccess = new ArrayList<KWObj>();
	
	private static void getRows2(Sheet sheet) {
		
		Row row = null;
		int rownum = sheet.getPhysicalNumberOfRows();
		row = sheet.getRow(0);
		
		KWObj ckwObj = null;
		String cUsePN = "";
		 for (int i = startRow; i<=rownum; i++ ) {
			 row = sheet.getRow(i);
			 if( row ==null ){
				 System.out.println( "行为空  " + i   + " 总行数 " + rownum );
				 continue;
			 }
			 Cell cellKW = row.getCell( kwCol );
			 Cell cellPn = row.getCell( pnCol );
			 Cell cellSn = row.getCell( snCol );
			 Cell cellCount = row.getCell( countCol );
			 
			 String kwStr = getCellValue(cellKW  );
			 String pnStr = getCellValue(cellPn );
			 String snStr = getCellValue(cellSn);
			 String countStr = getCellValue( cellCount )  ;
			 
			 boolean isKWEmpty = StringUtils.isEmpty(kwStr);
			 if(   !isKWEmpty   ){
				 System.out.println(  "进入下一个库位" + kwStr  + " ,  行号：" + (i +1 ) );
				 //进入下一个库位, 重置PN
				 cUsePN = ""; 
				 if(  ckwObj == null){
					 ckwObj = new KWObj(kwStr);
				 } else {
					try {
						ckwObj.checkSelf();
						kwAllSuccess.add(ckwObj);
					} catch (Exception e) {
						ckwObj.setMsg(e.getMessage() );
						kwErr.add(ckwObj);
					}
					ckwObj = new KWObj(kwStr);
				 }
			 }
			 boolean isPNEmpty = StringUtils.isEmpty(pnStr);
			 if(   !isPNEmpty   ){
				 cUsePN = pnStr;
			 }
			 if(  !StringUtils.isEmpty(countStr)  ){
				 int count = (int) Double.parseDouble(countStr);
				 ckwObj.addCount(count);
			 }
			 RowData rowData = new RowData( );
			 rowData.setMaterialNum(cUsePN);
			 rowData.setSnsStr(snStr);
			 ckwObj.addRow(rowData);
		 }
		 
		 try {
				ckwObj.checkSelf();
				kwAllSuccess.add(ckwObj);
			} catch (Exception e) {
				ckwObj.setMsg(e.getMessage() );
				kwErr.add(ckwObj);
			}
	}
	
	private static void writeSuccess2() {
		
		String toFile = path + "success"+getDateStr()+".xlsx";
		String[] title= { "库位号"  , "物料编码" , "序列号"  };
		
		XSSFWorkbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("sheet1");
		Row row = sheet.createRow((int) 0);
		CellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		for( int i=0; i<title.length  ; i++ ){
			Cell cell = row.createCell((short) i);
			cell.setCellValue(title[i]);
			cell.setCellStyle(style);
		}
		
		int counter3 = 1;
		int kwCount = kwAllSuccess.size();
		for(  int kwi = 0 ; kwi<kwCount ; kwi++ ){
			KWObj kwObj = kwAllSuccess.get(kwi);
			
			List<RowData> datas = kwObj.getRowSnList();
			int size = datas.size();
			for( int i=0; i<size ;i++ ){
				RowData rowData = datas.get(i);
				Row row2 = sheet.createRow( counter3++ );
				Cell cell2 = null;
				
				cell2 = row2.createCell(0);
				cell2.setCellValue( rowData.getPosNum() );
				cell2 = row2.createCell(1);
				cell2.setCellValue(  rowData.getMaterialNum() );
				
				cell2 = row2.createCell(2);
//				XSSFCellStyle cstyle = wb.createCellStyle();
//				XSSFDataFormat fmt = wb.createDataFormat();
//				style.setDataFormat(fmt.getFormat("@"));
				cell2.setCellType(Cell.CELL_TYPE_STRING);
				cell2.setCellValue( rowData.getSnsStr());
			}
			
		}
		
		writeToFile(toFile, wb);
	}

	private static void writeError2() {
		String toFile = path + "error"+getDateStr()+".xlsx";
		String[] title= { "库位号"  ,"备注" };
		
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("sheet1");
		Row row = sheet.createRow((int) 0);
		CellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		for( int i=0; i<title.length  ; i++ ){
			Cell cell = row.createCell((short) i);
			cell.setCellValue(title[i]);
			cell.setCellStyle(style);
		}
		int counter3 = 1;
		int kwCount = kwErr.size();
		for(  int kwi = 0 ; kwi<kwCount ; kwi++ ){
			KWObj kwObj = kwErr.get(kwi);
			Row row2 = sheet.createRow( counter3++ );
			Cell cell2 = null;
			cell2 = row2.createCell(0);
			cell2.setCellValue( kwObj.getKwNum() );
			cell2 = row2.createCell(1);
			cell2.setCellValue(  kwObj.getMsg());
		}
		
		writeToFile(toFile, wb);
	}

	private static String getDateStr() {
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd_HH-mm");
		String format = sdf.format(date);
		return format;
	}

	public static void writeToFile(String fname ,  Workbook wb){
		FileOutputStream fout = null;
		try {
			File excelFile = new File( fname );
			fout = new FileOutputStream(excelFile);
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
	
	//========================
	public static String getCellValue( Cell oneCell  ){
		String ret = "";
		if( oneCell == null ){
			return ret;
		}
		int cellType = oneCell.getCellType();
		if( oneCell.CELL_TYPE_NUMERIC == cellType ){
			double numericCellValue = oneCell.getNumericCellValue();
			BigDecimal bd = new BigDecimal(numericCellValue);
			String plainString = bd.toPlainString();
			if( plainString.endsWith(".0") ){
				plainString = plainString.substring( 0 , plainString.length() - 2);
			}
			ret = plainString ;
		}else if(  oneCell.CELL_TYPE_STRING == cellType  ){
			ret = oneCell.getStringCellValue();
		}else{
			ret = oneCell.getStringCellValue();
		}
		
		return ret ;
	}

	private static void writeToNewExcel(List<RowData> parseExcel) {
		
		List<RowData> successDatas = new ArrayList<RowData>();
		List<RowData> errorDatas = new ArrayList<RowData>();
		
		int size = parseExcel.size();
		for(  int i=0; i<size ; i++  ){
			RowData rowData = parseExcel.get(i);
			boolean parseRes = rowData.parseSNsToList();
			String pNum = rowData.getPosNum();
			String mNum = rowData.getMaterialNum();
			if( parseRes  ){
				//解析成功
				List<String> snList = rowData.getSnList();
				int snSize = snList.size();
				for(   int index=0; index<snSize ; index++  ){
					String tmpSn = snList.get(index);
					
					System.out.println(  pNum + " , " + mNum   + " , " +  tmpSn  +"||"  );
					RowData rd = new RowData(pNum, mNum, tmpSn, "1");
					successDatas.add(rd);
				}
			}else{
				System.err.println( "解析异常:: " +  pNum + " , " + mNum   + " , " +  rowData.getSnsStr() +"||"  );
				errorDatas.add(rowData);
			}
		}
		
		writeSuccess( successDatas );
		writeError( errorDatas );
	}


	private static void writeError(List<RowData> datas) {
		String toFile = path + "error.xlsx";
		String[] title= { "库位号"  , "物料编码" , "序列号" ,"备注" };
		
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("sheet1");
		Row row = sheet.createRow((int) 0);
		CellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		for( int i=0; i<title.length  ; i++ ){
			Cell cell = row.createCell((short) i);
			cell.setCellValue(title[i]);
			cell.setCellStyle(style);
		}
		
		int counter3 = 1;
		int size = datas.size();
		for( int i=0; i<size ;i++ ){
			RowData rowData = datas.get(i);
			Row row2 = sheet.createRow( counter3++ );
			Cell cell2 = null;
			
			cell2 = row2.createCell(0);
			cell2.setCellValue( rowData.getPosNum() );
			cell2 = row2.createCell(1);
			cell2.setCellValue(  rowData.getMaterialNum() );
			cell2 = row2.createCell(2);
			cell2.setCellValue( rowData.getSnsStr() );
			cell2 = row2.createCell(3);
			cell2.setCellValue( rowData.getMsg() );
		}
		
		FileOutputStream fout = null;
		try {
			File excelFile = new File( toFile );
			fout = new FileOutputStream(excelFile);
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

	private static void writeSuccess(List<RowData> datas) {
		String toFile = path + "success.xlsx";
		String[] title= { "库位号"  , "物料编码" , "序列号"  };
		
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("sheet1");
		Row row = sheet.createRow((int) 0);
		CellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		for( int i=0; i<title.length  ; i++ ){
			Cell cell = row.createCell((short) i);
			cell.setCellValue(title[i]);
			cell.setCellStyle(style);
		}
		
		int counter3 = 1;
		int size = datas.size();
		for( int i=0; i<size ;i++ ){
			RowData rowData = datas.get(i);
			Row row2 = sheet.createRow( counter3++ );
			Cell cell2 = null;
			
			cell2 = row2.createCell(0);
			cell2.setCellValue( rowData.getPosNum() );
			cell2 = row2.createCell(1);
			cell2.setCellValue(  rowData.getMaterialNum() );
			cell2 = row2.createCell(2);
			cell2.setCellValue( rowData.getSnsStr() );
		}
		
		FileOutputStream fout = null;
		try {
			File excelFile = new File( toFile );
			fout = new FileOutputStream(excelFile);
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
	


	private static List<RowData> parseExcel() throws InvalidFormatException, IOException {
		File file = new File( srcPath );
		InputStream input = new FileInputStream(file);
		String fileExt = file.getName().substring(file.getName().lastIndexOf(".") + 1);
		Workbook wb = null;
		Sheet sheet = null;
		// 根据后缀判断excel 2003 or 2007+
		if ( "xls".equals(fileExt) ) {
			wb = (HSSFWorkbook) WorkbookFactory.create(input);
		} else {
			wb = new XSSFWorkbook(input);
		}
		// 获取excel sheet总数
//		int sheetNumbers = wb.getNumberOfSheets();
		
		sheet = wb.getSheetAt(0);
		List<RowData> srcRows = null;
		return srcRows = getRows(sheet);
	}
	
	private static List<RowData> getRows(Sheet sheet) {
		
		List<RowData> aaa = new ArrayList<RowData>();
		
		Row row = null;
		int rownum = sheet.getPhysicalNumberOfRows();
		row = sheet.getRow(0);
		 for (int i = 2; i<=rownum; i++ ) {
			 row = sheet.getRow(i);
			 Cell cellPos = row.getCell(1);
			 Cell cellMa = row.getCell(2);
			 Cell cellSn = row.getCell(3);
			 Cell cellCount = row.getCell(4);
			 
			 String pNum = getCellValue(cellPos  );
			 String mNum = getCellValue(cellMa );
			 String snStr = getCellValue(cellSn);
			 String countStr = getCellValue( cellCount )  ;
			 
//			 System.out.println(   pNum + " ,  "+  mNum+ " ,  "+ snStr + " ,  "+  countStr    );
//			 System.out.println( "~~~~~~~~~" );
			 
			 RowData rowData = new RowData(pNum, mNum, snStr , countStr);
			 aaa.add(rowData);
		 }
		 
		return aaa;
	}
}	
