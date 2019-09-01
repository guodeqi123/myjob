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
	
	public final static String scanbasePath = "D:/ForBdcom/0stat1/扫描所有库位信息";
	
	//0831
	public final static String outDir = "D:/ForBdcom/0stat1/出库扫码记录";
	
	//0831
	public final static 	String indir1 = "D:/ForBdcom/0stat1/入库扫码记录";
	public final static 	String indir2 = "D:/ForBdcom/0stat1/入库扫码记录/nf";
	
	
	//0831
	public final static String PNStatuspath = "D:/ForBdcom/0stat1/Data0901/物料档案20190901_1.xlsx";
	
	//0831
	public final static String storePath  = "D:/ForBdcom/0stat1/Data0831/电子台账0831（关账汇总）(1).xlsx";
	
	//0830
	public final static String u8PathNew  = "D:/ForBdcom/0stat1/Data0830/u8data.xlsx";
	public final static String u8PathOld  = "D:/ForBdcom/0stat1/u8/现存量查询_2019.08.29.xlsx";
	
	//0828   物料编码与价格对照
	public final static String u8PNToPrice  = "D:/ForBdcom/0stat1/u8/物料单价/现存量查___0190828.9.41.xlsx";
	
	//0830
	public final static String SNUSEPN  = "D:/ForBdcom/0stat1/Data0830/重复的SN对应的物料编码(2).xlsx";
	
	//0901 张毅龙提供的 u8kw_pn => u8kb 
	public final static String U8KWMappingFile = "D:/ForBdcom/0stat1/Data0831/onlyWuWeiLeiBie.xlsx";
	
	//0830 每日PN修正
	public final static String PNComparePath = "D:/ForBdcom/0stat1/物料档案比对.xls";
	//0829每日PN修正
	public static String fDir = "D:/ForBdcom/0stat1/Data0829/";
	public static String[][] PNAmendFiles =new String[][] {
		//sheet num , startrow , srcpncol , topncol
		{ fDir+"错误物料编码统计0827修改.xlsx" , "0", "1","0","1"  }, 
		{ fDir+"物料编码不在U8中存在0828修改确认.xlsx" , "0", "1","0","1"  }, 
	};
	
	
	public static String u8DataDir = "D:/ForBdcom/0stat1/u8/0830/";
	public static String[][] u8DataPath = new String[][]{
		// fname ,  sheet num ,   startrow ,							仓库编码col ,   pn col ,  现存数量 col  ,   可用数量 col   , 
		{u8DataDir+"博达通信科技现存量查询.xlsx" , "0", "1",       "0", "1" , "2" ,"10" } , 
		{u8DataDir+"博达通信现存量查询.xlsx" , "0", "1",             "0", "1" , "3" ,"4" } , 
		{u8DataDir+"博达信息科技现存量查询.xlsx" , "0", "2",      "0", "1" , "2" ,"10" } , 
		{u8DataDir+"泰砚通讯现存量.xlsx" , "0",  "1",                 "0", "1" , "2" ,"10" }, 
		{u8DataDir+"迅坤现存量查询.xlsx" , "0", "1",                 "0", "1" , "2" ,"10" } ,
	};
	
	
	public static boolean isSNRight(String sn){
		
		String regex = "[\u4e00-\u9fa5]";
		StringBuilder sb  = new StringBuilder();// 定义一个列表用于存放找到的中文
		Pattern pattern = Pattern.compile(regex);// 定义模式，（模具）
		Matcher matcher = pattern.matcher(sn); // 匹配结果
		while (matcher.find()) { // 匹配结果读找到第一个
			sb.append(matcher.group() );
		}
		 String str = sb.toString();
		if(StringUtils.isEmpty(str) ){
			return true;
		}else{
			System.out.println( "SN存在汉字：：：" +  str );
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
