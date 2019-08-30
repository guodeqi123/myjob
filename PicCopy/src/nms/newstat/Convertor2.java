package nms.newstat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nms.KWObj;
import nms.RowData;
import nms.seqparser.AccessDBParser;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Convertor2 {
	
	public static void main(String[] args) throws Exception {
		
	}
	
	public static void doParse( String file  ) throws Exception{
		
		Convertor2 ccc = new Convertor2(file);
		Map<String, List<KWObj> > ret = ccc.parseExcel2( );
		List<KWObj> kwErr = ret.get("error");
		List<KWObj> kwAllSuccess = ret.get("success");
		List<String[]> errPns = ccc.writeSuccess2( kwAllSuccess  , true);
		ccc.writeErrorPns(errPns);
		ccc.writeError2(kwErr);
		
	}
 

	public static int kwCol = 0;
	public static int pnCol = 1;
	public static int snCol = 2;
	public static int countCol = 3;
	public static int startRow = 1;
	
	private String srcFilePath;
	
	private String srcFileName;
	
	private String path = null;
	
	public Convertor2(String sss ){
		this.srcFilePath = sss;
		File file = new File(srcFilePath);
		this.srcFileName = file.getName().substring( 0 , file.getName().lastIndexOf(".") );
		path = file.getParentFile().getAbsolutePath().replace("\\", "/") + "/";
//		System.out.println(  "文件名" +srcFileName +" ,路径: " +  path ); 
	}
	
	public Map<String, List<KWObj> > parseExcel2()  {
		try {
			File file = new File(srcFilePath);
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

			Map<String, List<KWObj>> ret = new HashMap<String, List<KWObj>>();
			
			for( int i=0; i<sheetNumbers ;i++  ){
				sheet = wb.getSheetAt(i);
				
				Map<String, List<KWObj>> resSub = getRows2(sheet);
				
				List<KWObj> kwErrA = resSub.get("error");
				List<KWObj> listE = ret.get("error");
				if(listE ==null){
					ret.put("error", kwErrA);
				}else{
					listE.addAll(kwErrA);
				}
				
				List<KWObj> kwAllSuccessA = resSub.get("success");
				List<KWObj> listS = ret.get("success");
				if(  listS ==null   ){
					ret.put("success", kwAllSuccessA);
				}else{
					listS.addAll(kwAllSuccessA);
				}
				
			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}  
		return null;
	}
	
	private Map<String, List<KWObj> > getRows2(Sheet sheet) {
		Map<String, List<KWObj> >  ret = new HashMap<String, List<KWObj>>();
		List<KWObj> kwErr = new ArrayList<KWObj>();
		List<KWObj> kwAllSuccess = new ArrayList<KWObj>();

		
		Row row = null;
//		int rownum = sheet.getPhysicalNumberOfRows();
		int rownum = sheet.getLastRowNum();
		row = sheet.getRow(0);
		
		KWObj ckwObj = null;
		String cUsePN = "";
		
		String debugKw = "-1";
		 for (int i = 1; i<=rownum; i++ ) {
			 row = sheet.getRow(i);
			 if( row ==null ){
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
			 
//			if( "OT-2200-GP".equals(pnStr)  || "C".equals(pnStr)  ){
//				System.out.println(   );
//			}
			 
			 boolean isKWEmpty = StringUtils.isEmpty(kwStr);
			 if(   !isKWEmpty   ){
//				 System.out.println(  "进入下一个库位" + kwStr  + " ,  行号：" + (i +1 ) );
				 if( debugKw.equals(kwStr) ){
					 System.out.println();
				 }
				 //进入下一个库位, 重置PN
				 cUsePN = ""; 
				 if(  ckwObj == null){
					 ckwObj = new KWObj(kwStr);
					 ckwObj.setSheetRowNum(i+1);
				 } else {
					try {
						ckwObj.checkSelf();
						kwAllSuccess.add(ckwObj);
					} catch (Exception e) {
//						e.printStackTrace();
						if( e instanceof RuntimeException ){
							kwAllSuccess.add(ckwObj);
						}else{
							GatherAll.parseErrorInfo.add( srcFilePath +" , " +srcFileName +" , "+ ckwObj.getKwNum() +" , " +e.getMessage()  );
//							e.printStackTrace();
						}
						ckwObj.setMsg(e.getMessage() );
						kwErr.add(ckwObj);
					}
					ckwObj = new KWObj(kwStr);
					 ckwObj.setSheetRowNum(i+1);
				 }
			 }
			 boolean isPNEmpty = StringUtils.isEmpty(pnStr);
			 if(   !isPNEmpty   ){
				 cUsePN = pnStr.toUpperCase().trim();
				 if( cUsePN.startsWith("PN:" )  ||cUsePN.startsWith("PN"+"：" )   ){
					 cUsePN = cUsePN.substring(3);
				 }
			 }
			 if(  !StringUtils.isEmpty(countStr)  ){
				 if(  countStr.contains(":") ){
					 countStr = countStr.split(":")[0];
				 }
				 if(  countStr.contains("：") ){
					 countStr = countStr.split("：")[0];
				 }
				 if(  countStr.contains("/") ){
					 countStr = countStr.split("/")[0];
				 }
				 int count = 0;
				try {
					if(  "14O".equals(countStr.trim()) ){
						countStr = "140";
					}
					count = (int) Double.parseDouble(countStr.trim());
				} catch (NumberFormatException e) {
					System.out.println( "盘库文件解析数量存在问题::   " +  srcFilePath+" , "+  ckwObj.getKwNum() + " , " +(i+1) + " , " +e.getMessage());
//					e.printStackTrace();
				}
				if( ckwObj == null ){
					continue;
				}
				 ckwObj.addCount(count);
			 }
			 RowData rowData = new RowData( );
			 rowData.setMaterialNum(cUsePN);
			 rowData.setSnsStr(snStr);
			 try {
				 if( ckwObj == null ){
					 continue;
				 }
				if( debugKw.equals(ckwObj.getKwNum()) ){
					 System.out.println( "DEBUG SN ::" + snStr    );
				 }
			} catch (RuntimeException e) {
				System.err.println( sheet.getSheetName() + " , " + (i+1) + " , " + srcFilePath  );
				throw e;
			}
			 ckwObj.addRow(rowData);
		 }
		 
		try {
			if( ckwObj !=null ){
				ckwObj.checkSelf();
				kwAllSuccess.add(ckwObj);
			}
		} catch (Exception e) {
			ckwObj.setMsg(e.getMessage());
			kwErr.add(ckwObj);
			if( e instanceof RuntimeException ){
				kwAllSuccess.add(ckwObj);
			}else{
				GatherAll.parseErrorInfo.add( srcFilePath +" , " +srcFileName +" , "+ ckwObj.getKwNum() +" , " +e.getMessage()  );
//				e.printStackTrace();
			}
		}

		ret.put("success", kwAllSuccess);
		ret.put("error", kwErr);
		return ret;
	}
	
	public List<String[]> writeSuccess2(List<KWObj> kwAllSuccess, boolean isWriteFile) {
		
		List<String[]> ret = new ArrayList<String[]>();
		
		String toFile = path + srcFileName +"_success"+getDateStr()+".xlsx";
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
			
			ret.addAll( kwObj.getErrPns() );
			
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
		
		if( isWriteFile ){
			 
			writeToFile(toFile, wb);
		}
		
		return ret;
	}

	private void writeError2(List<KWObj> kwErr) {
		String toFile = path + srcFileName +"_error"+getDateStr()+".xlsx";
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
			cell2.setCellValue( kwObj.getKwNum()  /*+ "，"+ kwObj.getSheetRowNum() +"行"*/ );
			cell2 = row2.createCell(1);
			cell2.setCellValue(  kwObj.getMsg());
		}
		
		writeToFile(toFile, wb);
	}
	
	private void writeErrorPns(List<String[]> errPns) {
		String toFile = path + srcFileName+"_errorPN"+getDateStr()+".xlsx";
		String[] title= { "库位 "  , "SN"  ,"输入PN" , "DB PN" };
		
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
		int kwCount = errPns.size();
		for(  int kwi = 0 ; kwi<kwCount ; kwi++ ){
			String[] strs = errPns.get(kwi);
			Row row2 = sheet.createRow( counter3++ );
			Cell cell2 = null;
			cell2 = row2.createCell(0);
			cell2.setCellValue( strs[0] );
			cell2 = row2.createCell(1);
			cell2.setCellValue(  strs[1] );
			cell2 = row2.createCell(2);
			cell2.setCellValue(  strs[2] );
			cell2 = row2.createCell(3);
			cell2.setCellValue(  strs[3] );
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
		oneCell.setCellType( Cell.CELL_TYPE_STRING);
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

	public String getSrcFilePath() {
		return srcFilePath;
	}

	public void setSrcFilePath(String srcFilePath) {
		this.srcFilePath = srcFilePath;
	}

	public String getSrcFileName() {
		return srcFileName;
	}

	public void setSrcFileName(String srcFileName) {
		this.srcFileName = srcFileName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	

}	
