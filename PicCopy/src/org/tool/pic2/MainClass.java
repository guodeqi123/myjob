package org.tool.pic2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.tool.piccopy.util.ZipUtils;

public class MainClass {
		
	public static long SIZE_2M = 1 * 1024*1024;
	
	static Map<String, String> picNameToPath = new HashMap<String, String>();
	public static String[] blankChar = new String[]{" "," " , "　"}; 
	
	public static String replaceBlank(String str) {
		String dest = null;
		if (str == null) {
			return dest;
		} else {
			for( int jj =0; jj<blankChar.length ;jj++){
				str = str.replace(blankChar[jj], "");
			}
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
			return dest;
		}
	}
	

	private static String handleTwoDot(String fName) {
		int indexOf = fName.indexOf(".");
		int last = fName.lastIndexOf(".");
		if( indexOf!= last ){
			fName = fName.substring(indexOf + 1);
		}
		fName = fName.replace("-", "");
		fName = fName.replace("+", "");
		fName = fName.replace("_", "");
		return fName;
	}
	
	public static <E> void main(String[] args) throws Exception {
		
		 
//		targetIDCards.add("412728197210291833");
//		targetIDCards.add("412722199101231103");
//		targetIDCards.add("15212319880103515X");
//		targetIDCards.add("411402198712035833");
//		targetIDCards.add("411402198909218246");
//		targetIDCards.add("411424199012020035");
//		targetIDCards.add("41132919881201281X");
//		targetIDCards.add("411323198810046321");
//		targetIDCards.add("411222198808152019");
//		targetIDCards.add("410224199203015026");
//		targetIDCards.add("410922197708010083");
//		targetIDCards.add("41090119900506201X");
//		targetIDCards.add("410782199102160102");
//		targetIDCards.add("410782199004019762");
//		targetIDCards.add("410521199208120519");
//		targetIDCards.add("410322199210050842");
//		targetIDCards.add("410728198612229840");
//		targetIDCards.add("41061119820805752X");
//		targetIDCards.add("410527199002100019");
//		targetIDCards.add("410502198906203545");
//		targetIDCards.add("41040319721029158X");
//		targetIDCards.add("410422197407165420");
//		targetIDCards.add("410482198910081102");
//		targetIDCards.add("410327198312017071");
//		targetIDCards.add("41022319871216001X");
//		targetIDCards.add("410122198904046949");
//		targetIDCards.add("41010519780813282X");
		
		
		targetIDCards.add("411323198602220057");
		
		HashSet<String> hashSet = new HashSet<String>();
		for(  String key : targetIDCards ){
			hashSet.add(key.toLowerCase());
		}
		targetIDCards = hashSet;
		
		System.out.println(    "    ++++++++++  0 " + targetIDCards.size() );
		
		doStat();
		

	}
	
	public static String fileSep = "/";
	public static String prodDir = "D:/GG/2/prod";
	
	public static String picSuffix = ".jpg";
	public static String resultDir = "result_100_3";
	public static  int threshold = 100;// every 500 persion to a file ;
	
	public static Set<String> targetIDCards = new HashSet<String>();
	
	public static void doStat() throws Exception{

		
		String srcDataPath =  prodDir +fileSep + "srcDataNew.xls";
		String pirRootDir =   prodDir +fileSep +"省联社照片New";
		
		List<PersonBean> pers = loadAllPersion(srcDataPath);
		
		loadPic( pirRootDir );
		
		for( PersonBean pp : pers  ){
//			System.out.println(pp );
		}
		
		int counter = 1 ;
		Set<Entry<String,String>> entrySet = picNameToPath.entrySet();
		
		for(  Entry<String,String> en : entrySet ){
//			System.out.println(  counter++  + " || " +  en.getKey() + " || " + en.getValue()    );
		}
		System.out.println(  pers.size()    + " | " + picNameToPath.size()  );
		
		createZipDir(pers);
		
		zipEveryDir();
		
		
	}
	
	
	public static void zipEveryDir() {
		String currentDir = prodDir +fileSep+resultDir;
		
		File file = new File(currentDir);
		File[] listFiles = file.listFiles();
		for( File  toZip : listFiles){
			String srcDirPath = toZip.getAbsolutePath();
			String dstPath = srcDirPath+".zip";
			ZipUtils.zipFile(srcDirPath, dstPath);
		}
		
		
	}


	public static void createZipDir( List<PersonBean> pers ) throws Exception{
		int errorCounter1 = 0;
		int errorCounter2 = 0;
		
		int picSizeMaxCounter = 0;
		
		
		int size = pers.size();
		List<PersonBean> oneBatch = new ArrayList<PersonBean>();
		String dirPrefix = "zip";
		int zipCounter = 1;
		String currentDir = prodDir +fileSep+resultDir+fileSep+ dirPrefix+zipCounter;
		for(  int i=0; i<size ; i++ ){
			PersonBean personBean = pers.get(i);
			if(  (i%threshold==0 && i !=0)  ){
				writeThisBatchToExcel( oneBatch , currentDir  );
				zipCounter ++ ;
				currentDir = prodDir +fileSep+resultDir+fileSep + dirPrefix+zipCounter;
				new File(currentDir).mkdirs();
				oneBatch = new ArrayList<PersonBean>();
			}
			oneBatch.add(personBean);
			//copy pic 
			String tmpPic = personBean.getPic();
			tmpPic = replaceBlank(tmpPic);
			String key = tmpPic.split("\\.")[0];
//			String tmpPicPath = picNameToPath.get(key);
			String tmpPicPath = picNameToPath.remove(key);
			
			personBean.setPic(key+picSuffix);
			if(tmpPicPath == null  ){
				System.err.println("PIC NOT EXIST ::"  + personBean  );
				errorCounter1++;
			} else if( !tmpPicPath.toLowerCase().endsWith( picSuffix ) ){
//				System.err.println("PIC Format ERROR ::"  + personBean  +  "||"+  tmpPicPath );
				errorCounter2++;
				//convert to jpg
				convertPNG2JPG(tmpPicPath, currentDir+fileSep+personBean.getPic());
			}else {
				File tmpFile = new File(tmpPicPath);
				String sPath = tmpFile.getAbsolutePath();
				String name = tmpFile.getName();
				long length = tmpFile.length();
				if( length>SIZE_2M ){
					String resizeDir = prodDir +fileSep + "tmpDir" ;
					// shrink  pic size
					String dPath = resizeDir +fileSep+ name;
					ReduceImgTest.reduceImg(sPath, dPath, 260, 390, null);
					File pressPic = new File(dPath);
					System.out.println(    "Press PIC : " + sPath + " TO PATH ::: " +dPath + " PRESS SIZE " + pressPic.length()/1024+"KB"  );
					picSizeMaxCounter++;
					tmpPicPath = dPath;
				}
				
				copyFileToCurrentDir(tmpPicPath , currentDir  , personBean.getPic());
				
			}
			
			
			if(   i==size-1 ){
				writeThisBatchToExcel( oneBatch , currentDir  );
			}
		}
		
		System.out.println(  "Error Count :: " + errorCounter1 + "  FM ERR :: " +  errorCounter2  + "  PIC REMAIN ::"  + picNameToPath.size() );
		System.out.println(  "Press Pic :: " + picSizeMaxCounter );
		
		Set<Entry<String,String>> entrySet = picNameToPath.entrySet();
		for( Entry<String,String> en : entrySet ){
//			System.out.println(  "Still ::: " + en.getValue()   );
		}
		
	}
	
	public static void convertPNG2JPG(String srcPic , String dstPic){
		try {
			// read image file
			BufferedImage bufferedImage = ImageIO.read(new File(srcPic));
			// create a blank, RGB, same width and height, and a white
			// background
			BufferedImage newBufferedImage = new BufferedImage(
					bufferedImage.getWidth(), bufferedImage.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			// TYPE_INT_RGB:创建一个RBG图像，24位深度，成功将32位图转化成24位
			newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
			// write to jpeg file
			ImageIO.write(newBufferedImage, "jpg", new File(dstPic));
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 
	
	public static void copyFileToCurrentDir(String srcPicPath, String currentDir , String pname) {
		File tmpDir = new File( currentDir );
		if( !tmpDir.exists() ){
			tmpDir.mkdirs();
		}
		File srcFile = new File(srcPicPath);
		File destFile = new File(currentDir +fileSep + pname );
		try {
			FileUtils.copyFile(srcFile, destFile);
		} catch ( Exception e) {
			e.printStackTrace();
		}
	}

	public static String toFile = "智联测评机考候选人信息模板.xls";

	public static void writeThisBatchToExcel(List<PersonBean> oneBatch, String currentDir ) throws Exception {
		
		String templatePath = prodDir + fileSep + "tempalte.xls";
		
		// excute update 
		String excelFilePath = currentDir + fileSep + toFile;
		
		InputStream is = new FileInputStream(templatePath);
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		HSSFSheet sheetAt0 = hssfWorkbook.getSheetAt(0);
		
		int rowCounter = 1;
		int size = oneBatch.size();
		for( int i=0 ; i<size ;i++ ){
			PersonBean cper = oneBatch.get(i);
			HSSFRow tmpRow = sheetAt0.createRow( rowCounter++ );
			
			//姓名 *	身份证号 *	考试地点	考场安排	准考证号	性别 ,	
			//出生日期 	毕业院校	学历 	专业 	现从事行业	 电话	 婚姻状况	户口	现居地	工作年限	员工编号	
			//头像名称
			
			HSSFCell cell1 = tmpRow.createCell(0);
			cell1.setCellValue( cper.getName() );
			HSSFCell cell2 = tmpRow.createCell(1);
			cell2.setCellValue( cper.getIdcard() );
			HSSFCell cell3 = tmpRow.createCell(2);
			cell3.setCellValue( cper.getExamAddress() );
			HSSFCell cell4 = tmpRow.createCell(3);
//			cell4.setCellValue( "第"+cper.getExamRoom() +"考场" );
			cell4.setCellValue( cper.getExamRoom() );
			HSSFCell cell5 = tmpRow.createCell(4);
			cell5.setCellValue( cper.getExamNum() );
			HSSFCell cell6 = tmpRow.createCell(5);
			cell6.setCellValue( cper.getGender() );
			
			HSSFCell cell7 = tmpRow.createCell(6);
			HSSFCell cell8 = tmpRow.createCell(7);
			HSSFCell cell9 = tmpRow.createCell(8);
			HSSFCell cell10 = tmpRow.createCell(9);
			HSSFCell cell11 = tmpRow.createCell(10);
			HSSFCell cell12 = tmpRow.createCell(11);
			HSSFCell cell13 = tmpRow.createCell(12);
			HSSFCell cell14 = tmpRow.createCell(13);
			HSSFCell cell15 = tmpRow.createCell(14);
			HSSFCell cell16 = tmpRow.createCell(15);
			HSSFCell cell17 = tmpRow.createCell(16);
			
			HSSFCell cell18 = tmpRow.createCell(17);
			cell18.setCellValue( cper.getPic() );
			
		}
		
		File file = new File(excelFilePath);
		file.createNewFile();
		FileOutputStream fout = new FileOutputStream(file);
		hssfWorkbook.write(fout);
		
	}


	private static void loadPic(String rootDir) {
		loadImg(rootDir);
	}
	
	
	public static Set<String> testSet = new HashSet<String>();
	
	private static void loadImg(String imgDir) {
		
		File rootDir = new File(imgDir);
		File[] listFiles = rootDir.listFiles();
		for( File ff :   listFiles ){
			String fName = ff.getName();
			
			String absolutePath = ff.getAbsolutePath();
			if( ff.isFile() ){
				fName = replaceBlank(fName);
				fName = handleTwoDot(fName);
				if( isImg(fName.toLowerCase()) ){
					String[] split = fName.split("\\.");
					String fNameWithoutPreffix = split[0];
					String lowerCase = fNameWithoutPreffix.toLowerCase();
					if( picNameToPath.containsKey(lowerCase) ){
						System.err.println(  picNameToPath.get(lowerCase  ) + "::::::::: Repeat PIC ::::::::: " +  fName  +  " | " + absolutePath  );
					}
					picNameToPath.put( lowerCase , absolutePath);
				}
			}else if( ff.isDirectory() ){
				loadImg(absolutePath);
			}
		}
		
	}
	
	public static boolean isImg(String name ){
		String[] arr = new String[]{
				"bmp" , "gif", "jpe" ,"jpeg","png","jpg","png", "dib" ,"jfif", "tif", "tiff" ,"ico"
		};
		for( int i=0; i<arr.length ;i++ ){
			if( name.endsWith(arr[i]) ){
				return true;
			}
		}
		
		return false;
	}


	private static List<PersonBean> loadAllPersion(String xlsPath ) throws Exception{
		InputStream is = new FileInputStream(xlsPath);
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		HSSFSheet sheetAt0 = hssfWorkbook.getSheetAt(0);
		
		// 第0行为列名称行
		HSSFRow columnRow = sheetAt0.getRow(0);
		int columns = columnRow.getPhysicalNumberOfCells();
		
		List<PersonBean> pers = new ArrayList<PersonBean>();
		
		for (int rowNum = 1; rowNum <= sheetAt0.getLastRowNum(); rowNum++) {
			HSSFRow hssfRow = sheetAt0.getRow(rowNum);
			
			if (hssfRow != null) {
				
				String seq = getValue( hssfRow.getCell(0) );
				String area = getValue( hssfRow.getCell(1) );
				String name = getValue( hssfRow.getCell(2) );
				String gender = getValue( hssfRow.getCell(3) );
				String idcard = getValue( hssfRow.getCell(4) );
				String phone = getValue( hssfRow.getCell(5) );
				String pic = getValue( hssfRow.getCell(6) ).toLowerCase().trim();
				String examRoom = getValue( hssfRow.getCell(7) ).toLowerCase();
				String site = getValue( hssfRow.getCell(8) ).toLowerCase();
				String examAddress = getValue( hssfRow.getCell(9) ).toLowerCase();
				
				String lowerCase = idcard.toLowerCase();
				if( targetIDCards.size()>0 && !targetIDCards.contains(lowerCase) ){
					continue;
				}
	
				PersonBean personBean = new PersonBean(name ,idcard , examAddress ,  examRoom  ,gender  ,pic );
				personBean.setSeq(rowNum);
				personBean.setSiteNum(site);
				personBean.setArea(area);
				personBean.setPhone(phone);
				
				personBean.reNewExamNum();
				pers.add(personBean);
				
				if( idcard.length()<18  ){
					System.err.println(  "IDCARD ERR ::: " + personBean  );
				}
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
	
	
	public static void test(){
		String aa = "103772.0";
		
		String substring = aa.substring( 0 ,aa.length()-1 );
		System.out.println(   substring ); 
		
	}
}
