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
	public static String resumeFile = "��һ��.xls";
	public static String resultFileName = "01.xls";
	
	public static Map<String, Integer> edumapping = new HashMap<String, Integer>();
	
	public static List<String> falseRows = new ArrayList<String>();
	
	
	public static void main(String[] args) throws Exception {
		
		ProfessionInfo.loadMappingInfo();
		initBasic();
		
		List<PersonInfo> persons = readExcel();
		
//		String idCard = "���֤|41082219880208896X";
//		String idCard2 = "���֤|411002199403151016";
//		System.out.println(  parseIdCardToAge(1, idCard , ""));
//		System.out.println(  parseIdCardToAge(2, idCard2,""));
		
	}

	
	private static void initBasic() {
		////����  ר��   ��ѧ����   ˶ʿ�о���   
		edumapping.put("����",  PostInfo.EDU_senior_high_school );
		edumapping.put("ר��",  PostInfo.EDU_junior_college );
		edumapping.put("��ѧ����",  PostInfo.EDU_regular_college );
		edumapping.put("˶ʿ�о���",  PostInfo.EDU_MASTER );

	}




	private static List<PersonInfo> readExcel() throws Exception {
		String fpath =dirPath +  fileSep +   resumeFile  ;
		
		InputStream is = new FileInputStream(fpath);
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		HSSFSheet sheetAt0 = hssfWorkbook.getSheetAt(0);
		
		// ��0��Ϊ��������
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
				//1 Ͷ��ְλ -> ��λ   �ж��Ƿ�����Ҫ��
				String deliverPost = getValue( hssfRow.getCell(175) );
				posts.add(deliverPost);
				
				// 2ѧ�� -> ���ѧ��
				String tmpEdu = getValue( hssfRow.getCell(8) );
				edus.add(tmpEdu);
				Integer eduInt = edumapping.get(tmpEdu);
				
				//3רҵ -> ��ѧרҵ
				String tmpProfession  = getValue( hssfRow.getCell(9) );
				String tmpSchool  = getValue( hssfRow.getCell(11) );
				pros.add(tmpProfession);
				
				//4���� ->��������
				String idCard = getValue( hssfRow.getCell(14) );
				String yearofBirth = getValue( hssfRow.getCell(4) );
				int age = parseIdCardToAge(rowNum , idCard , yearofBirth);
				if( age<0 ){
					System.err.println( "ROW AGE WRONG ::: "  + rowNum); 
					continue;
				}
				
				//5�Ա� ��->�Ա�
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
					tmpCell.setCellValue( "��" );
				}else{
					tmpCell.setCellValue( "��" );
					tmpCell2.setCellValue(personInfo.getMsg());
					falseRows.add( "FFFF :: Row num :: " +  rowNum + ",       רҵ: " + tmpProfession 
							+ ",        Ͷ�ݸ�λ:" + deliverPost + ",      �Ա�,����:" + tmpGender + " ,    "+age + ",       ѧ��:" +  tmpEdu  + ",  MSG:: " + personInfo.getMsg() );
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
		//"���֤|41082219880208896X";
		idCard = idCard.trim();
		String birthDay = "";
		if( yearofBirth!=null && yearofBirth.startsWith("'") ){
			yearofBirth = yearofBirth.substring(1);
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		if( idCard.startsWith("���֤|") ){
			birthDay = idCard.substring(4).trim();
			birthDay = birthDay.substring(6 , 14);
		}else if( idCard.startsWith("����|") ){
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
	    // ����
	    Calendar born = Calendar.getInstance();
	    born.setTime(parse);
	    // ���� = ��ǰ�� - ������
	    int age = curr.get(Calendar.YEAR) - born.get(Calendar.YEAR);
	    if (age <= 0) {
	        return 0;
	    }
	    // �����ǰ�·�С�ڳ����·�: age-1
	    // �����ǰ�·ݵ��ڳ����·�, �ҵ�ǰ��С�ڳ�����: age-1
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
