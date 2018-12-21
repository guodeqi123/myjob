package org.tool.work3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Work3Main {
	
	
	public static String dirPath = "D:/GG/3/result";
	public static String fileSep = "/";
	public static String resumeFile = "第一批.xls";
	public static String resultFileName = "01.xls";
	
	public static Map<String, Integer> edumapping = new HashMap<String, Integer>();
	
	public static List<String> falseRows = new ArrayList<String>();
	
	
	public static void main(String[] args) throws Exception {
		
		ProfessionInfo.loadMappingInfo();
		initBasic();
		
		List<PersonInfo> persons = readExcel();
		
//		String idCard = "身份证|41082219880208896X";
//		String idCard2 = "身份证|411002199403151016";
//		System.out.println(  parseIdCardToAge(1, idCard , ""));
//		System.out.println(  parseIdCardToAge(2, idCard2,""));
		
	}

	
	private static void initBasic() {
		////高中  专科   大学本科   硕士研究生   
		edumapping.put("高中",  PostInfo.EDU_senior_high_school );
		edumapping.put("专科",  PostInfo.EDU_junior_college );
		edumapping.put("大学本科",  PostInfo.EDU_regular_college );
		edumapping.put("硕士研究生",  PostInfo.EDU_MASTER );

	}




	private static List<PersonInfo> readExcel() throws Exception {
		String fpath =dirPath +  fileSep +   resumeFile  ;
		
		InputStream is = new FileInputStream(fpath);
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		HSSFSheet sheetAt0 = hssfWorkbook.getSheetAt(0);
		
		// 第0行为列名称行
		HSSFRow columnRow = sheetAt0.getRow(0);
		int columns = columnRow.getPhysicalNumberOfCells();
		System.out.println(    "Person count :: " + sheetAt0.getLastRowNum() + " Cols count ::  " +columns    );
		List<PersonInfo> pers = new ArrayList<PersonInfo>();
		
		Set<String> posts = new LinkedHashSet<String>();
		Set<String> edus = new LinkedHashSet<String>();
		Set<String> pros = new LinkedHashSet<String>();
		Set<String> ages = new LinkedHashSet<String>();
		Set<String> genders = new LinkedHashSet<String>();
		
		for (int rowNum = 1; rowNum <= sheetAt0.getLastRowNum(); rowNum++) {
			HSSFRow hssfRow = sheetAt0.getRow(rowNum);
			
			if (hssfRow != null) {
				//1 投递职位 -> 岗位   判断是否满足要求
				String deliverPost = getValue( hssfRow.getCell(175) );
				posts.add(deliverPost);
				
				// 2学历 -> 最高学历
				String tmpEdu = getValue( hssfRow.getCell(8) );
				edus.add(tmpEdu);
				Integer eduInt = edumapping.get(tmpEdu);
				
				//3专业 -> 所学专业
				String tmpProfession  = getValue( hssfRow.getCell(9) );
				String tmpSchool  = getValue( hssfRow.getCell(11) );
				pros.add(tmpProfession);
				
				//4年龄 ->出生日期
				String idCard = getValue( hssfRow.getCell(14) );
				String yearofBirth = getValue( hssfRow.getCell(4) );
				int age = parseIdCardToAge(rowNum , idCard , yearofBirth);
				if( age<0 ){
					System.err.println( "ROW AGE WRONG ::: "  + rowNum); 
					continue;
				}
				
				//5性别 —->性别
				String tmpGender = getValue( hssfRow.getCell(3) );
				genders.add(tmpGender);
				
				String  tmpIdentity = getValue( hssfRow.getCell(13) );
				
				if( eduInt==PostInfo.EDU_senior_high_school ){
//					System.out.println(" GZZZZ :::::::::: " +  rowNum  + "    " + tmpProfession  +"   "+ tmpIdentity);
				}
				
				
				PersonInfo personInfo = new PersonInfo( rowNum , deliverPost , eduInt , tmpProfession , age ,tmpGender ,tmpIdentity );
				personInfo.setSchool(tmpSchool);
				PostInfo postInfo = ProfessionInfo.postMapping.get(deliverPost);
				
		
				boolean match = postInfo.isMatch(personInfo);
				
				HSSFCell tmpCell = hssfRow.createCell(181);
				HSSFCell tmpCell2 = hssfRow.createCell(182);
				if(match){
					tmpCell.setCellValue( "是" );
				}else{
					tmpCell.setCellValue( "否" );
					tmpCell2.setCellValue(personInfo.getMsg());
					falseRows.add( "FFFF :: Row num :: " +  rowNum + ",       专业: " + tmpProfession 
							+ ",        投递岗位:" + deliverPost + ",      性别,年龄:" + tmpGender + " ,    "+age + ",       学历:" +  tmpEdu  + ",  MSG:: " + personInfo.getMsg() );
				}
			}
		}
		
		
		printCollection(posts);
		printCollection(edus);
		printProfession(pros);
		printCollection(genders);
		
		printCollection(falseRows);
		
		
		String excelFilePath = dirPath + fileSep + resultFileName;
		File file = new File(excelFilePath);
		file.createNewFile();
		FileOutputStream fout = new FileOutputStream(file);
		hssfWorkbook.write(fout);
		
		return pers;
	}


	private static int parseIdCardToAge( int rowNum ,String idCard, String yearofBirth) throws ParseException {
		//"身份证|41082219880208896X";
		idCard = idCard.trim();
		String birthDay = "";
		if( yearofBirth!=null && yearofBirth.startsWith("'") ){
			yearofBirth = yearofBirth.substring(1);
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		if( idCard.startsWith("身份证|") ){
			birthDay = idCard.substring(4).trim();
			birthDay = birthDay.substring(6 , 14);
		}else if( idCard.startsWith("其他|") ){
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			birthDay = yearofBirth;
//			System.err.println( " ROW NUM :: "  + rowNum + "  IDCARD ::   "  +idCard ) ;
		}else{
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			birthDay = yearofBirth;
//			System.err.println( " ROW NUM :: "  + rowNum + "  IDCARD ::   "  +idCard ) ;
		}
		
		
		Date parse = simpleDateFormat.parse(birthDay);
//		System.out.println( string );
//		System.out.println(  substring  );
//		System.out.println( parse );
		
		Calendar curr = Calendar.getInstance();
	    // 生日
	    Calendar born = Calendar.getInstance();
	    born.setTime(parse);
	    // 年龄 = 当前年 - 出生年
	    int age = curr.get(Calendar.YEAR) - born.get(Calendar.YEAR);
	    if (age <= 0) {
	        return 0;
	    }
	    // 如果当前月份小于出生月份: age-1
	    // 如果当前月份等于出生月份, 且当前日小于出生日: age-1
	    int currMonth = curr.get(Calendar.MONTH);
	    int currDay = curr.get(Calendar.DAY_OF_MONTH);
	    int bornMonth = born.get(Calendar.MONTH);
	    int bornDay = born.get(Calendar.DAY_OF_MONTH);
	    if ((currMonth < bornMonth) || (currMonth == bornMonth && currDay <= bornDay)) {
	        age--;
	    }
	    return age < 0 ? 0 : age;
	}




	private static void printCollection( Collection<String> set ) {
		System.out.println(  " Begin : SIZE:::  " + set.size() );
		for(  String str : set ){
			System.out.println(  str );
		}
		System.out.println( " ------------------------------------ ");
	}
	
	private static void printProfession( Collection<String> set ) {
		System.out.println(  " Profession SIZE:::  " + set.size() );
		for(  String str : set ){
			Collection<DomainBean> values = ProfessionInfo.domainBeanMapping.values();
			String a = "";
			for(  DomainBean bean  : values ){
				boolean match = bean.isMatch(str);
				if( match ){
					 a = a+ bean.getName() + " ;  ";
				}
			}
			System.out.println(  str  + "==============" + a  );
		}
		System.out.println( " ------------------------------------ ");
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
