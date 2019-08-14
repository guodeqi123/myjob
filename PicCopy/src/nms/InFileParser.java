package nms;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import nms.seqparser.QrCodeParser;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class InFileParser {
	private String srcFilePath;
	private String srcFileName;

	public InFileParser(String sss) {
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
	
	public static final 	String c1  = ":";
	public static final 	String c2= "：";
	
	public List<InOutObj> parseIn() {
		List<InOutObj> ret = new ArrayList<InOutObj>();

		Workbook wb = null;
		Sheet sheet;
		try {
			File file = new File(srcFilePath);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 获取excel sheet总数
		int sheetNumbers = wb.getNumberOfSheets();

		Row row = null;
		for (int sn = 0; sn < sheetNumbers; sn++) {
			sheet = wb.getSheetAt(sn);
			String sheetName = sheet.getSheetName();
			
			int rownum = sheet.getLastRowNum();
			String cUsePn = null;
			List<InOutObj> oneKW = new ArrayList<InOutObj>();
			
			for (int i = 1; i <= rownum; i++) {
				row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				Cell cellPn = row.getCell(0);
				Cell cellSn = row.getCell(1);
				Cell cellkw = row.getCell(2);
				String pnStr = Convertor.getCellValue(cellPn);
				String snStr = Convertor.getCellValue(cellSn);
				String kwStr = Convertor.getCellValue(cellkw);
				
				if(  !StringUtils.isEmpty(pnStr) ){
					cUsePn = pnStr;
				}
				
				List<String> parse = QrCodeParser.parse(snStr);
				int snsize = parse.size();
				for (int sc = 0; sc < snsize; sc++) {
					String tmpSn = parse.get(sc);
					InOutObj aa = new InOutObj();
					aa.setIsIn(true);
					aa.setPn(cUsePn);
					aa.setSn(tmpSn);
					aa.setDateStr(sheetName);
					oneKW.add(aa);
				}
				if(  !StringUtils.isEmpty(kwStr)  ){
					String[] split = kwStr.split(c1);
					if( split.length == 1 ){
						split = kwStr.split(c2);
					}
					String cUseKW = split[0];
					String count = split[1];
					for(InOutObj ttt :   oneKW ){
						ttt.setKw(cUseKW);
					}
					ret.addAll(oneKW);
					try {
						int parseInt = Integer.parseInt(count);
						if(  parseInt!= oneKW.size()){
							System.out.println( sheetName + " 数量不准确 "+   kwStr + " , 统计数据 ："+ oneKW.size()  );
						}
					} catch ( Exception e) {
						if( e instanceof NumberFormatException ) {
							System.err.println( sheetName + " 数量不准确::"+   kwStr + " , 统计数据 ："+ oneKW.size()  );
						}else{
							e.printStackTrace();
						}
					}
					oneKW.clear();
				}
			}
		}
		return ret;
	}
	
	public static void main(String[] args) {
		String ff0812 = "D:/ForBdcom/0812/in.xlsx";
		String ff0813 = "D:/ForBdcom/0813/in0813.xlsx";
		
		InFileParser inFileParser = new InFileParser( ff0813 );
		List<InOutObj> parseIn = inFileParser.parseIn();
		
//		for(   InOutObj aa  : parseIn ){
//			System.out.println(  "======" + aa.getPn() +" , "+ aa.getSn()+" , " + aa.getKw() +" , "+ aa.getDateStr() );
//		}
		
		System.out.println(    parseIn.size() );
		
	}
	
}
