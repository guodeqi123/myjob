package nms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nms.seqparser.QrCodeParser;

import org.apache.commons.lang.StringUtils;
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
		for (int sn = 0; sn < sheetNumbers; sn++) {
			sheet = wb.getSheetAt( sn );
			String sheetName = sheet.getSheetName();
			int rownum = sheet.getLastRowNum();
			
			String cUsePn = null;
			String cUseNum = null;
			List<InOutObj> tmpList = new ArrayList<InOutObj>();
			
			for(  int i=1; i<=rownum ;i++ ){
				row = sheet.getRow(i);
				 if( row ==null ){
					 continue;
				 }
				 Cell cellNum = row.getCell( 0 );
				 Cell cellPn = row.getCell( 1 );
				 Cell cellSn = row.getCell( 2 );
				 Cell cellCn = row.getCell( 3 );
				 if( cellSn==null ){
					 continue;
				 }
				 String numStr = Convertor.getCellValue(cellNum);
				 String pnStr = Convertor.getCellValue(cellPn);
				 String snStr = Convertor.getCellValue(cellSn);
				 String countStr = Convertor.getCellValue(cellCn);
				 if( "-1".equals(numStr) ){
					 System.out.println(  );
				 }
				 if(StringUtils.isEmpty(snStr) ){
					 continue;
				 }
				 if(  !StringUtils.isEmpty(numStr) ){
					 cUseNum = numStr;
				 }
				 if(  !StringUtils.isEmpty(pnStr) ){
					 cUsePn = pnStr;
				 }
				 
				 List<String> parse = QrCodeParser.parse(snStr);
				 int snsize = parse.size();
				 if( snsize >1 ){
					 System.out.println();
				 }
				 for(  int sc=0; sc<snsize ;sc++ ){
					 String tmpSn = parse.get(sc);
					 InOutObj aa = new InOutObj();
					 aa.setIsIn(false);
					 aa.setNum(cUseNum);
					 aa.setPn(cUsePn);
					 aa.setSn(tmpSn);
					 aa.setDateStr(sheetName);
					 tmpList.add(aa);
					 ret.add(aa);
				 }
				 
				 if(   !StringUtils.isEmpty(countStr)  ){
					 try {
						 int c = Integer.parseInt(countStr);
						 int size = tmpList.size();
						 if( size!=c ){
							 System.out.println(  sheetName + " , 出库数量不匹配 :" +  cUseNum +" , "+ cUsePn + " ,统计个数 " + size  +" ，填写个数： "+ c  + " , csn : " + parse.get(0) );
						 }
						 tmpList.clear();
					} catch (Exception e) {
						e.printStackTrace();
					}
				 }
				 
			}
		}
		
		return ret;
	}
	
	
	public static void main(String[] args) {
//		String ff0812 = "D:/ForBdcom/0812/out.xlsx";
//		String ff0813 = "D:/ForBdcom/0813/out0813.xlsx";
//		String ff0814 = "D:/ForBdcom/0814/0814out.xlsx";
		String ff0815 = "D:/ForBdcom/0815/0815out.xlsx";
		
		OutFileParser outparser = new OutFileParser( ff0815 );
		List<InOutObj> pOut = outparser.parseOut();
		
//		for(   InOutObj aa  : pOut ){
//			System.out.println(  "======" + aa.getDateStr() + " , " + aa.getNum() +" , "+ aa.getPn()+" , " + aa.getSn()  );
//		}
		
		System.out.println(    pOut.size() );
		
	}
	
	
}
