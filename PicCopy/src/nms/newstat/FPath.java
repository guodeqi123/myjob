package nms.newstat;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FPath {
	
	//  XKSWI-SWI2021C           	CBNNN-JHJ0151A

	
	public static final String sep1= "_";
	public static final String KW = "KW";
	
	public final static String scanbasePath = "D:/ForBdcom/0stat1/ɨ�����п�λ��Ϣ";
	
	//0831
	public final static String outDir = "D:/ForBdcom/0stat1/����ɨ���¼";
	
	//0831
	public final static 	String indir1 = "D:/ForBdcom/0stat1/���ɨ���¼";
	public final static 	String indir2 = "D:/ForBdcom/0stat1/���ɨ���¼/nf";
	
	
	//0831
	public final static String PNStatuspath = "D:/ForBdcom/0stat1/Data0901/���ϵ���20190901_1.xlsx";
	
	//0831
	public final static String storePath  = "D:/ForBdcom/0stat1/Data0831/����̨��0831�����˻��ܣ�(1).xlsx";
	
	//0830
	public final static String u8PathNew  = "D:/ForBdcom/0stat1/Data0830/u8data.xlsx";
	public final static String u8PathOld  = "D:/ForBdcom/0stat1/u8/�ִ�����ѯ_2019.08.29.xlsx";
	
	//0828   ���ϱ�����۸����
	public final static String u8PNToPrice  = "D:/ForBdcom/0stat1/u8/���ϵ���/�ִ�����___0190828.9.41.xlsx";
	
	//0830
	public final static String SNUSEPN  = "D:/ForBdcom/0stat1/Data0830/�ظ���SN��Ӧ�����ϱ���(2).xlsx";
	
	//0901 �������ṩ�� u8kw_pn => u8kb 
	public final static String U8KWMappingFile = "D:/ForBdcom/0stat1/Data0831/onlyWuWeiLeiBie.xlsx";
	
	//0830 ÿ��PN����
	public final static String PNComparePath = "D:/ForBdcom/0stat1/���ϵ����ȶ�.xls";
	//0829ÿ��PN����
	public static String fDir = "D:/ForBdcom/0stat1/Data0829/";
	public static String[][] PNAmendFiles =new String[][] {
		//sheet num , startrow , srcpncol , topncol
		{ fDir+"�������ϱ���ͳ��0827�޸�.xlsx" , "0", "1","0","1"  }, 
		{ fDir+"���ϱ��벻��U8�д���0828�޸�ȷ��.xlsx" , "0", "1","0","1"  }, 
	};
	
	
	public static String u8DataDir = "D:/ForBdcom/0stat1/u8/0830/";
	public static String[][] u8DataPath = new String[][]{
		// fname ,  sheet num ,   startrow ,							�ֿ����col ,   pn col ,  �ִ����� col  ,   �������� col   , 
		{u8DataDir+"����ͨ�ſƼ��ִ�����ѯ.xlsx" , "0", "1",       "0", "1" , "2" ,"10" } , 
		{u8DataDir+"����ͨ���ִ�����ѯ.xlsx" , "0", "1",             "0", "1" , "3" ,"4" } , 
		{u8DataDir+"������Ϣ�Ƽ��ִ�����ѯ.xlsx" , "0", "2",      "0", "1" , "2" ,"10" } , 
		{u8DataDir+"̩��ͨѶ�ִ���.xlsx" , "0",  "1",                 "0", "1" , "2" ,"10" }, 
		{u8DataDir+"Ѹ���ִ�����ѯ.xlsx" , "0", "1",                 "0", "1" , "2" ,"10" } ,
	};
	
	
	public static boolean isSNRight(String sn){
		
		String regex = "[\u4e00-\u9fa5]";
		StringBuilder sb  = new StringBuilder();// ����һ���б����ڴ���ҵ�������
		Pattern pattern = Pattern.compile(regex);// ����ģʽ����ģ�ߣ�
		Matcher matcher = pattern.matcher(sn); // ƥ����
		while (matcher.find()) { // ƥ�������ҵ���һ��
			sb.append(matcher.group() );
		}
		 String str = sb.toString();
		if(StringUtils.isEmpty(str) ){
			return true;
		}else{
			System.out.println( "SN���ں��֣�����" +  str );
			return false;
		}
	}
	
	
	public static Workbook getWB(String fpath){
		Workbook wb = null;
		try {
			File file = new File( fpath );
			InputStream input = new FileInputStream(file);
			String fileExt = file.getName().substring( file.getName().lastIndexOf(".") + 1);
			if ("xls".equals(fileExt)) {
				wb = (HSSFWorkbook) WorkbookFactory.create(input);
			} else {
				wb = new XSSFWorkbook(input);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wb;
	}
	
	public static void main(String[] args) {
		
		
	}
	
	
}
