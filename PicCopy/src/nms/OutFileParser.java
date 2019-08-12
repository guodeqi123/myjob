package nms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class OutFileParser {
	
	private String srcFilePath;
	private String srcFileName;

	public OutFileParser(String sss) {
		this.srcFilePath = sss;
		File file = new File(srcFilePath);
		this.srcFileName = file.getName().substring(0, file.getName().lastIndexOf("."));
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
	
	public List<InOutObj> parseOut(){
		 List<InOutObj> ret =new ArrayList<InOutObj>();
		
		Workbook wb = null;
		Sheet sheet;
		try {
			File file = new File(srcFilePath);
			InputStream input = new FileInputStream(file);
			String fileExt = file.getName().substring( file.getName().lastIndexOf(".") + 1);
			wb = null;
			sheet = null;
			// 根据后缀判断excel 2003 or 2007+
			if ("xls".equals(fileExt)) {
				wb = (HSSFWorkbook) WorkbookFactory.create(input);
			} else {
				wb = new XSSFWorkbook(input);
			}
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 获取excel sheet总数
		int sheetNumbers = wb.getNumberOfSheets();
		
		Row row = null;
		for (int sn = 0; sn <= sheetNumbers; sn++) {
			sheet = wb.getSheetAt( sn );
			String sheetName = sheet.getSheetName();
			int rownum = sheet.getPhysicalNumberOfRows();
			for(  int i=1; i<=rownum ;i++ ){
				row = sheet.getRow(i);
				 if( row ==null ){
					 continue;
				 }
				 Cell cellNum = row.getCell( 0 );
				 Cell cellSn = row.getCell( 1 );
				 if( cellSn==null ){
					 continue;
				 }
				 String numStr = Convertor.getCellValue(cellNum);
				 String snStr = Convertor.getCellValue(cellSn);
				 if( snStr==null ){
					 continue;
				 }
				 InOutObj aa = new InOutObj();
				 aa.setIsIn(false);
				 aa.setNum(numStr);
				 aa.setSn(snStr);
				 aa.setDateStr(sheetName);
				 ret.add(aa);
			}
		}
		
		return ret;
	}
	
	
}
