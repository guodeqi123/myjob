package org.tool.pic2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Checking {
		
	public static String fileSep = "/";
	public static String prodDir = "D:/GG/2/prod/"+MainClass.resultDir;
	
	public static String picSuffix = ".jpg";
	
	public static 	Set <String> examNums = new HashSet<String>();
    public static	int picMaxCounter = 0;
	
	public static void main(String[] args)  throws Exception {
//		getRowNum("D:/GG/2/prod/tempalte.xls");
		
		
		File file = new File(prodDir);
		File[] listFiles = file.listFiles();
		int zipCounter = 1;
		for( int i=0 ;i<listFiles.length ; i++ ){
			if( !listFiles[i].isDirectory() ){
				continue;
			}
			String fileName = MainClass.toFile;
			String dirName = "zip" + zipCounter++ ; 
			String dirPath  = prodDir + fileSep + dirName ;
			String filePath  = prodDir + fileSep + dirName+  fileSep + fileName;
			File dirFile = new File(dirPath);
			
			List<PersonBean> pers = null;
			pers = parseExcel(filePath);
			
			Set<String > pics = loadPic(dirPath);
			System.out.println(  dirName + " Person Count :: " + pers.size() + " Pic count :: " + pics.size() );
			int counter = 1;
			for(PersonBean  pp  :  pers  ){
				String tmpPic = pp.getPic();
				if( pics.contains(tmpPic) ){
					boolean remove = pics.remove(tmpPic);
				}else{
					System.err.println( dirName + " PIC ERROR  not exist::: " + tmpPic    + "  ,"+ counter++  );
				}
			}
		}
		
		
		System.out.println( "examNums count ::: " +  examNums.size()  );
		System.out.println( examNums.iterator().next()  );
		
		System.out.println("PIC SIZE ERR:: " +  picMaxCounter  );
		
	}

	private static Set<String> loadPic(String dirPath) {
		Set<String> pics = new HashSet<String>();
		
		File file = new File(dirPath);
		File[] listfs = file.listFiles();
		for( int i=0; i<listfs.length ;i++ ){
			String name = listfs[i].getName();
			if( name.endsWith( MainClass.picSuffix) ){
				pics.add(name);
			}
			
			long fileSize = listfs[i].length(); // byte  
			//kB , MB
			long KBSize = fileSize/1024;
			long MBSize =  KBSize/1024;
			if( fileSize > MainClass.SIZE_2M ){
				System.err.println(  name + "  size : "   +fileSize +"Byte" + " , "+ KBSize + "KB" + " , "+ MBSize  +"MB");
				picMaxCounter++;
			}
			
			
		}
		
		return pics;
	}

	private static List<PersonBean> parseExcel(String filePath) throws Exception {
		
		
		InputStream is = new FileInputStream(filePath);
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		HSSFSheet sheetAt0 = hssfWorkbook.getSheetAt(0);
		
		// 第0行为列名称行
		HSSFRow columnRow = sheetAt0.getRow(0);
		int columns = columnRow.getPhysicalNumberOfCells();
		
		List<PersonBean> pers = new ArrayList<PersonBean>();
//		System.out.println(    filePath  + "  row num :: "   + sheetAt0.getLastRowNum() );
		for (int rowNum = 1; rowNum <= sheetAt0.getLastRowNum(); rowNum++) {
			HSSFRow hssfRow = sheetAt0.getRow(rowNum);
			if( rowNum>400 ){
				int a = 1;
			}
			if (hssfRow != null) {
				String pic = null;
				String name  = null;
				HSSFCell cell = null;
				HSSFCell cellName = null;
				try {
					cellName  = hssfRow.getCell(0);
					cell = hssfRow.getCell(17);
					HSSFCell examNum = hssfRow.getCell(4);
					if( cell==null   || cellName == null){
						break;
					}
					name = getValue(cellName ) ;
					pic = getValue(cell ) ;
					if( name.trim().length()==0   || pic.trim().length()==0 ){
						continue;
					}
					examNums.add( getValue(examNum  )  );
				} catch (Exception e) {
					System.err.println(    filePath + "  " + rowNum + " :: row ::: " +   name + "   " +   cell);
					e.printStackTrace(); 
				}
				
				PersonBean personBean = new PersonBean();
				personBean.setPic(pic);
				pers.add(personBean);
			}
		}
		return pers;
	
		
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
	
	
	public static void getRowNum(String  filePath  ) throws Exception{
		InputStream is = new FileInputStream(filePath);
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		HSSFSheet sheetAt0 = hssfWorkbook.getSheetAt(0);
		
		System.out.println( sheetAt0.getLastRowNum()  );
		
//		String src = "D:/GG/2/prod/照片汇总/04平顶山（253）/郏县（26）/许春丽2523.png";
//		String dst = "D:/111.jpg";
//		convertPNG2JPG(src, dst);
				
//		String aa = "102.程贺龙 4534.jpg";
//		String aa2 = replaceBlank(aa);
//		System.out.println(aa2 );
//		aa2 = handleTwoDot(aa2);
//		System.out.println(aa2);
		
	}
	
}
