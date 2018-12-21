package org.tool.work4;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xwpf.usermodel.IBody;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

public class Work4Main {

	public static String excelPath = "D:/GG/4/src2.xls";
	
	public static String tmpPath = "D:/GG/4/temp2.docx";
	
	public static String resultDir = "D:/GG/4/result";
	
	public static List<Person4> pers = new ArrayList<Person4>();
	
	public static void readExcel() throws Exception{
		
		InputStream is = new FileInputStream(excelPath);
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		HSSFSheet sheetAt0 = hssfWorkbook.getSheetAt(0);
		
		HSSFRow columnRow = sheetAt0.getRow(0);
		int columns = columnRow.getPhysicalNumberOfCells();
		System.out.println(    "Person count :: " + sheetAt0.getLastRowNum() + " Cols count ::  " +columns    );
		
		for (int rowNum = 1; rowNum <= sheetAt0.getLastRowNum(); rowNum++) {
			HSSFRow hssfRow = sheetAt0.getRow(rowNum);
			HSSFCell counterCell = hssfRow.getCell(0);
			HSSFCell nameCell = hssfRow.getCell(2);
			HSSFCell genderCell = hssfRow.getCell(3);
			HSSFCell orgCell = hssfRow.getCell(4);
			HSSFCell idCardCell = hssfRow.getCell(5);
			HSSFCell roomNumCell = hssfRow.getCell(7);
			HSSFCell posNumCell = hssfRow.getCell(8);
			
			Person4 person4 = new Person4();
			person4.setRowNum(getValue(counterCell) );
			person4.setName(getValue(nameCell));
			person4.setGender(getValue(genderCell));
			person4.setOrganization( getValue(orgCell));
			person4.setIdCard( getValue(idCardCell) );
			person4.setExamRoomNum( getValue(roomNumCell) );
			person4.setPosNum( getValue(posNumCell) );
			pers.add(person4);
		}
		int scounter = 0 ;
		int ecounter = 0 ;
		for(Person4 per : pers){
			if(chechPerson(per)){
				System.out.println( per );
				scounter++;
				writeWord(per);
			}else{
				ecounter++;
				System.err.println( per );
			}
		}
		System.out.println(    "success count :: " + scounter   + " error count + "  +ecounter );
	}
	
	private static boolean chechPerson(Person4 per) {
		String name = per.getName();
		String gender = per.getGender();
		String idCard = per.getIdCard();
		String organization = per.getOrganization();
		String examNum = per.getExamRoomNum();
		if(  name==null || name.length()==0 ){
			return false;
		}
		if(  gender==null || gender.length()==0 ){
			return false;
		}
		if(  idCard==null || idCard.length()==0 ){
			return false;
		}
		if(  organization==null || organization.length()==0 ){
			return false;
		}
		if(  examNum==null || examNum.length()==0 ){
			return false;
		}
		return true;
	}

	public static void main(String[] args) throws Exception {
//		testReadWord();
//		writeWord();
		readExcel();
	}
	
	
	public static void writeWord(Person4 per) throws Exception{
		
		XWPFDocument document = new XWPFDocument(  new FileInputStream( tmpPath )   );
		
		List<XWPFTable> tables = document.getTables();
		String name=per.getName();
		String gender=per.getGender();
		String idCard = per.getIdCard();
		String organization =per.getOrganization();
		String examNum = per.getExamRoomNum()+"¿¼³¡"+per.getPosNum()+"ºÅ";
		
		XWPFTable table0 = tables.get(0);
		
		XWPFTableRow nameRow = table0.getRow(2);
		XWPFTableCell cell = nameRow.getCell(1);
		cell.setText(name);
		
		XWPFTableRow genderRow = table0.getRow(3);
		cell = genderRow.getCell(1);
		cell.setText(gender);
		
		XWPFTableRow idCardRow = table0.getRow(4);
		cell = idCardRow.getCell(1);
		cell.setText(idCard);
		
		XWPFTableRow companyRow = table0.getRow(5);
		cell = companyRow.getCell(1);
		cell.setText(organization);
		
		XWPFTableRow examNumRow = table0.getRow(8);
		cell = examNumRow.getCell(1);
		cell.setText(examNum);
		
		String newDirPath = resultDir + "/" + per.getOrganization();
		File dirfile = new File(newDirPath);
		if( !dirfile.exists() ){
			dirfile.mkdirs();
		}
		
		String newFilePath = newDirPath + "/" + per.getName()+"_" + per.getIdCard() + ".docx";
		document.write( new FileOutputStream(newFilePath));
	}
	
	public static void testReadWord() throws Exception{
		XWPFDocument document = new XWPFDocument(  new FileInputStream( tmpPath )   );
		List<XWPFTable> tables = document.getTables();
		for (XWPFTable table : tables) {
		    List<XWPFTableRow> rows = table.getRows();
		    for (XWPFTableRow row : rows) {
		        List<XWPFTableCell> tableCells = row.getTableCells();
		        for (XWPFTableCell cell : tableCells) {
		             String text = cell.getText();
		             
		             System.out.println(  text  );
		        }
		        
		        System.out.println( "///////////////////////" );
		    }
		}
	}
	
	public static String getValue(HSSFCell hssfCell) {
		String res = "";
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			res=  String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			res=  String.valueOf(hssfCell.getNumericCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_STRING) {
			res=  String.valueOf(hssfCell.getStringCellValue());
		}  else {
			hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			res=  String.valueOf(hssfCell.getStringCellValue());
		}
		
		if( res.endsWith(".0") ){
			res = res.substring( 0 ,res.length()-2 );
		}
		return  res;
	}
	
}
