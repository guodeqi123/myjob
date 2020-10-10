package w2020.t7;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import w2020.FUtil;


public class InitData7 {
	
	
	public static Set<String> coll1 = new HashSet<String>();
	public static Set<String> coll2 = new HashSet<String>();
	
	public static void init(){
		initSchool();
		
		initMajor();
		
	}
	
	
	
	public static LinkedHashMap<String, String>  typeToMajor = new LinkedHashMap<String, String>();
	
	
	private static void initMajor() {
		String path = "conf/t7/aa.txt";
		List<String> loadTxt = FUtil.loadTxt(path);
		String currentValue = null;
		for( String aa :  loadTxt ){
			if(  aa.startsWith("*") ){
				currentValue = aa.substring(1).trim();
			}else{
				typeToMajor.put(aa.trim(), currentValue);
			}
		}
		System.out.println(   typeToMajor.size()  );
		
	}





	private static void initSchool() {
		String[] split = aa1.split("、");
		for(String a : split){
			coll1.add(a);
		}
		split = aa2.split("、");
		for(String a : split){
			coll1.add(a);
		}
		
		split = aa3.split("、");
		for(String a : split){
			coll2.add(a);
		}
		System.out.println("双一流数量："  + coll1.size() +" ,  "+  coll2.size()    );
	}

	// 一流大学建设高校42所 A类36所 B类6所
	static String aa1 = "北京大学、中国人民大学、清华大学、北京航空航天大学、北京理工大学、中国农业大学、北京师范大学、中央民族大学、南开大学、天津大学、大连理工大学、吉林大学、哈尔滨工业大学、复旦大学、同济大学、上海交通大学、华东师范大学、南京大学、东南大学、浙江大学、中国科学技术大学、厦门大学、山东大学、中国海洋大学、武汉大学、华中科技大学、中南大学、中山大学、华南理工大学、四川大学、重庆大学、电子科技大学、西安交通大学、西北工业大学、兰州大学、国防科技大学";
	static String aa2 = "东北大学、郑州大学、湖南大学、云南大学、西北农林科技大学、新疆大学";
	
	// 一流学科建设高校95所
	static String aa3 = "北京交通大学、北京工业大学、北京科技大学、北京化工大学、北京邮电大学、北京林业大学、北京协和医学院、北京中医药大学、首都师范大学、北京外国语大学、中国传媒大学、中央财经大学、对外经济贸易大学、外交学院、中国人民公安大学、北京体育大学、中央音乐学院、中国音乐学院、中央美术学院、中央戏剧学院、中国政法大学、天津工业大学、天津医科大学、天津中医药大学、华北电力大学、河北工业大学、太原理工大学、内蒙古大学、辽宁大学、大连海事大学、延边大学、"
			+ "东北师范大学、哈尔滨工程大学、东北农业大学、东北林业大学、华东理工大学、东华大学、上海海洋大学、上海中医药大学、上海外国语大学、上海财经大学、上海体育学院、上海音乐学院、上海大学、"
			+ "苏州大学、南京航空航天大学、南京理工大学、中国矿业大学、南京邮电大学、河海大学、江南大学、南京林业大学、南京信息工程大学、南京农业大学、南京中医药大学、中国药科大学、南京师范大学、中国美术学院、安徽大学、合肥工业大学、福州大学、南昌大学、河南大学、中国地质大学、武汉理工大学、华中农业大学、华中师范大学、中南财经政法大学、湖南师范大学、暨南大学、广州中医药大学、华南师范大学、海南大学、广西大学、西南交通大学、西南石油大学、成都理工大学、四川农业大学、"
			+ "成都中医药大学、西南大学、西南财经大学、贵州大学、西藏大学、西北大学、西安电子科技大学、长安大学、陕西师范大学、青海大学、宁夏大学、石河子大学、中国石油大学、宁波大学、中国科学院大学、第二军医大学、第四军医大学";

}
