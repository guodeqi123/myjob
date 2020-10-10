package w2020.t6;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import w2020.FUtil;

public class MainApp {
	
	
	public static String basePath = "D:/GG/t6";
	
	public static String srcFilePath = basePath + "/4484初筛.xlsx" ;
	public static String dstFilePath = basePath + "/res.xls" ;
	
	public static void main(String[] args) {
	
		InitData.init();
		InitData.initJob();
//		System.out.println( InitData.majorToList.size() );
//		System.out.println( InitData.majorToNotlist.size() );
//		Set<Entry<String,JobBean>> entrySet = InitData.nameToJob.entrySet();
//		for(  Entry<String,JobBean> en : entrySet  ){
//			System.out.println(  en.getValue() );
//		}
		
		loadRowInfo();
		
		writeRes();
		
	}

	
	private static void writeRes() {
	    XSSFWorkbook wb = (XSSFWorkbook) FUtil.getWB(  srcFilePath  );
        Sheet wsheet = wb.getSheetAt(0);
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); 
        Sheet rsheet = wb.getSheetAt(0);
        int lastRowNum = rsheet.getLastRowNum();
        for (int i = 1; i <= lastRowNum; i++) {
           Row wrow =  rsheet.getRow(i);
           if(wrow ==null){
               continue;
           }
           String key = ""+i;
           RowInfo rowInfo = rowToBean.get(key);
           
           Cell wcell = wrow.createCell(2);
           wcell.setCellValue( rowInfo.getFlagAge() );
           
           wcell = wrow.createCell(3);
           wcell.setCellValue( rowInfo.getFlagEdu() );
           
           wcell = wrow.createCell(4);
           wcell.setCellValue( rowInfo.getFlagMajor() );

        }
 

        FUtil.writeExcel(wb  , dstFilePath );
	}


	public static Map<String , RowInfo> rowToBean = new HashMap<String, RowInfo>();
	
	private static void loadRowInfo() {
	
		Workbook wb = FUtil.getWB(srcFilePath);
		Sheet rsheet = wb.getSheetAt(0);	
		int lastRowNum = rsheet.getLastRowNum();
		for (int i = 1; i <= lastRowNum; i++) {
			Row row = rsheet.getRow(i);
			if (row == null) {
				continue;
			}
			
			RowInfo rowInfo = new RowInfo( i  );
			
//			private String postJob ; //FE
//			private int eduBack;  //L   学历    1 高中  2专科  3大学本科   4硕士研究生
//			private String major;  //M
//			private int birthDay;//R
			
			Cell postJobCell = row.getCell( 160 ) ;
			String postJob = FUtil.getCellValueByCell(postJobCell);
			
			Cell eduBackCell = row.getCell( 11 ) ;
			String eduBack = FUtil.getCellValueByCell(eduBackCell);
			
			Cell majorCell = row.getCell( 12 ) ;
			String major = FUtil.getCellValueByCell(majorCell);
			
			Cell idCardCell = row.getCell( 17 ) ;
			String idCard = FUtil.getCellValueByCell(idCardCell);
			rowInfo.setPostJob(postJob);
			rowInfo.setEduBack(eduBack);
			rowInfo.setMajor(major);
			rowInfo.setIdCard(idCard);
			rowInfo.calcFlag();
			
			rowToBean.put( rowInfo.getRowNum() + "" , rowInfo);
		}
		
	}
	
}
