package w2020.t7;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import w2020.FUtil;

public class MainApp7 {
	
	
	public static String basePath7 = "D:/GG/T7";
	public static String srcPath = "/temp.xlsx";
	
	public static void main(String[] args) {
		
		InitData7.init();
		
		loadSrcData();
		
		writeRes();
		
		
	}
	
	
	private static void writeRes() {
		String srcFilePath = basePath7 + srcPath ;
		Workbook wb = FUtil.getWB(srcFilePath);
		
		Sheet rsheet = wb.getSheetAt(0);	
		int lastRowNum = rsheet.getLastRowNum();
		for (int i = 1; i <= lastRowNum; i++) {
			Row row = rsheet.getRow(i);
			if (row == null) {
				continue;
			}
			String key = "" + i;
			Person7 person7 = personMap.get(key);
			
			Cell wcell = row.createCell(0);
            wcell.setCellValue( person7.getPassFlag() );
            
			wcell = row.createCell(3);
            wcell.setCellValue( person7.getAge()+"" );
            
            wcell = row.createCell(4);
            wcell.setCellValue( person7.getReason() );
			
		}
		
		String fileName = basePath7 + "/res.xlsx";
		FUtil.writeExcel((XSSFWorkbook) wb, fileName);
	}



	public static Map<String, Person7> personMap = new HashMap<String, Person7>();

	private static void loadSrcData() {
		
		String srcFilePath = basePath7 + srcPath ;
		Workbook wb = FUtil.getWB(srcFilePath);
		Sheet rsheet = wb.getSheetAt(0);	
		int lastRowNum = rsheet.getLastRowNum();
		for (int i = 1; i <= lastRowNum; i++) {
			Row row = rsheet.getRow(i);
			if (row == null) {
				continue;
			}
			
			Person7 pp = createPerson(row , i );
			pp.calcPass();
			personMap.put(pp.getRow() +"", pp);
		}
		
	}

	private static Person7 createPerson(Row row , int rn) {
		Person7 pp = new Person7();
		
//		Cell idCardCell = row.getCell( 8 ) ;
//		String idCard = FUtil.getCellValueByCell(idCardCell);
//		pp.setIdCard(idCard);
//		
//		Cell postCompCell = row.getCell( 16 ) ;
//		String postComp = FUtil.getCellValueByCell(postCompCell);
//		pp.setPostCompany( postComp );
//		
//		Cell eduCell = row.getCell( 22 ) ;
//		String edu = FUtil.getCellValueByCell(eduCell);
//		pp.setEdu(edu);
//		
//		Cell collageCell = row.getCell( 21 ) ;
//		String collage = FUtil.getCellValueByCell(collageCell);
//		pp.setCollage(collage);
//		
//		Cell degreeCell = row.getCell( 23 ) ;
//		String degree = FUtil.getCellValueByCell(degreeCell);
//		pp.setDegree( degree );
//		
//		
//		Cell nameCell = row.getCell( 5 ) ;
//		String name = FUtil.getCellValueByCell(nameCell);
//		pp.setName(name);
//		
//		Cell genderCell = row.getCell( 6 ) ;
//		String gender = FUtil.getCellValueByCell(genderCell);
//		pp.setGender(gender);
//		
//		Cell birthCell = row.getCell( 7 ) ;
//		String birth = FUtil.getCellValueByCell(birthCell);
//		pp.setBirth(birth);
//		
//		Cell graduateYearCell = row.getCell( 26 ) ;
//		String graduateYear = FUtil.getCellValueByCell(graduateYearCell);
//		pp.setGraduateYear(graduateYear);
//		
//		Cell getTypeCell = row.getCell( 19 ) ;
//		String getType = FUtil.getCellValueByCell(getTypeCell);
//		pp.setGetType(getType);
//		
//		Cell majorCell = row.getCell( 24 ) ;
//		String major = FUtil.getCellValueByCell(majorCell);
//		pp.setMajor(major);
//		
		
		pp = FUtil.createBean(pp, row);
		
		pp.setRow( rn );
		return pp;
	}
	
	
	
	
	

}
