package nms.stat;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import nms.Convertor;
import nms.RowData;

public class ExcelFileObj {
	
	String filePath ;
	
	String fname ;
	
	String fSuffix;
	
	List<SheetObj> sheets = new ArrayList<SheetObj>();
	
	public ExcelFileObj( String[] a  ){
		filePath = a[0];
		
		File file = new File(filePath);
		
		System.out.println( file.exists() );
		
		this.fname = file.getName().substring( 0 , file.getName().lastIndexOf(".") );
		fSuffix = file.getName().substring( file.getName().lastIndexOf(".") + 1);
		
		for( int i=1; i<a.length ;i++  ){
			String tmpa = a[i];
			SheetObj ts = new SheetObj(tmpa);
			sheets.add(ts);
		}
		
	}
	
	public List<RowData> parse(){
		List<RowData> retList = new ArrayList<RowData>();
		Workbook wb = null;
		Sheet sheet;
		try {
			File file = new File(filePath);
			InputStream input = new FileInputStream(file);
			String fileExt = file.getName().substring(
					file.getName().lastIndexOf(".") + 1);
			wb = null;
			sheet = null;
			// 根据后缀判断excel 2003 or 2007+
			if ("xls".equals(fileExt)) {
				wb = (HSSFWorkbook) WorkbookFactory.create(input);
			} else {
				wb = new XSSFWorkbook(input);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Row row = null;
		for(SheetObj sss : sheets){
			int sheetNum = sss.getSheetNum();
			sheet = wb.getSheetAt(sheetNum);
			String sheetName = sheet.getSheetName();
			
			int startRow = sss.getStartRow();
			int pnCol = sss.getPnCol();
			int countCol = sss.getCountCol();
			int rownum = sheet.getLastRowNum();
			for(  int i=startRow ; i<rownum  ; i++ ){
				 try {
					row = sheet.getRow(i);
					 if( row ==null ){
						 continue;
					 }
					 Cell cellPn = row.getCell( pnCol );
					 Cell cellCount = row.getCell( countCol );
					 String pnStr = Convertor.getCellValue(cellPn  );
					 String count = Convertor.getCellValue(cellCount );
					 if( StringUtils.isEmpty(pnStr) ){
						 System.out.println( sheetName + " , rownum ::" + (i+1) + " pn is null");
						 continue;
					 }
					 if( StringUtils.isEmpty(pnStr)  || StringUtils.isEmpty(count) ) {
						 continue;
					 }
					RowData e = new RowData();
					e.setMaterialNum(pnStr);
					e.setStatcount( (int)Double.parseDouble(count) );
					retList.add(e);
				} catch (Exception e) {
					
					if( e instanceof IllegalStateException ){
						System.err.println( "单元格数据错误::"+ sheetName + " , rownum ::" + (i+1) );
					}else{
						System.err.println( "解析有误"+ sheetName + " , rownum ::" + (i+1) );
						e.printStackTrace();
					}
				}
			}
		}
		
		return retList;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getfSuffix() {
		return fSuffix;
	}

	public void setfSuffix(String fSuffix) {
		this.fSuffix = fSuffix;
	}

	public List<SheetObj> getSheets() {
		return sheets;
	}

	public void setSheets(List<SheetObj> sheets) {
		this.sheets = sheets;
	}
	
}

