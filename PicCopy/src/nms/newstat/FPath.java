package nms.newstat;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class FPath {
	
	
	public final static String scanbasePath = "D:/ForBdcom/0stat1/ɨ�����п�λ��Ϣ";
	
	//0830
	public final static String outDir = "D:/ForBdcom/0stat1/����ɨ���¼";
	
	//0830
	public final static 	String indir1 = "D:/ForBdcom/0stat1/���ɨ���¼";
	public final static 	String indir2 = "D:/ForBdcom/0stat1/���ɨ���¼/nf";
	
	
	//0829
	public final static String PNStatuspath = "D:/ForBdcom/0stat1/Data0829/���ϵ��������кŹ���0829.xlsx";
	
	//0830
	public final static String storePath  = "D:/ForBdcom/0stat1/Data0830/����̨��0830_rrr.xlsx";
	
	//0830
	public final static String u8PathNew  = "D:/ForBdcom/0stat1/Data0830/u8data.xlsx";
	public final static String u8PathOld  = "D:/ForBdcom/0stat1/u8/�ִ�����ѯ_2019.08.29.xlsx";
	
	//0828   ���ϱ�����۸����
	public final static String u8PNToPrice  = "D:/ForBdcom/0stat1/u8/���ϵ���/�ִ�����___0190828.9.41.xlsx";
	
	//0830
	public final static String SNUSEPN  = "D:/ForBdcom/0stat1/Data0830/�ظ���SN��Ӧ�����ϱ���(2).xlsx";
	
	
	//0830 ÿ��PN����
	public final static String PNComparePath = "D:/ForBdcom/0stat1/���ϵ����ȶ�.xls";
	//0829ÿ��PN����
	public static String fDir = "D:/ForBdcom/0stat1/Data0829/";
	public static String[][] PNAmendFiles =new String[][] {
		//sheet num , startrow , srcpncol , topncol
		{ fDir+"�������ϱ���ͳ��0827�޸�.xlsx" , "0", "1","0","1"  }, 
		{ fDir+"���ϱ��벻��U8�д���0828�޸�ȷ��.xlsx" , "0", "1","0","1"  }, 
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
	
	public static void main(String[] args) {
		
		String  aaa = "012��3sa��dsax��aete13qs";
		String  aaa2 = "012EXSljS";
		
		System.out.println(   isSNRight(aaa) );
		System.out.println(   isSNRight(aaa2) );
		
	}
	
	
}
