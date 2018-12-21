package org.tool.work3;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProfessionInfo {
	
	// 所学专业  - >  允许匹配岗位ID
	public static Map<String, Set<String>> mapping = new HashMap<String, Set<String>>();
	
	
	public static Map<String, DomainBean > domainBeanMapping = new HashMap<String, DomainBean>();
	
	public static Map<String, PostInfo>  postMapping = new HashMap<String, PostInfo>();
	
	public static  void loadMappingInfo() throws Exception{
		String fName = "ProMapping.txt";
		InputStream resourceAsStream = ProfessionInfo.class.getResourceAsStream(fName);
		BufferedReader br = new BufferedReader( new InputStreamReader(resourceAsStream));
		
		int counter = 0 ;
		String line = null;
		while(  (line=br.readLine())!=null ){
			counter++;
//			System.out.println( line );
			
			String[] split = line.split("=");
			if( split.length>1 ){
				String pro = split[0];
				
				String[] posts = split[1].split(",");
				Set<String> hashSet = new HashSet<String>();
				for( String str :  posts){
					hashSet.add(str.trim());
				}
				mapping.put(pro, hashSet);
			}
			
		}
		
		System.out.println(  mapping.size() +  " .......... over...................." + counter );
		
		
		
//		1.经济金融专业。主要包括宏观经济、金融学、国际金融 金融工程、区域经济学、国民经济学、产业经济学、政治经济学 数量经济学、
//		人口资源环境经济学、金融管理与实务、投资学    不含财政学、国际贸易
//
//		2.会计专业。主要包括财务管理、审计、会计、会计硕士 注册会计师、国际会计。
//
//		3.法律专业。主要包括法学、法律、行政法学、经济法、民法、民商法、知识产权法、法律硕士。
//
//		4.计算机专业。主要包括计算机软件、计算机硬件、计算机应用、电子科学与技术、通信技术、信息安全、计算机科学与技术、通信工程、计算机信息管理、
//		信息与计算科学、软件技术、电子信息工程、网络工程、模式识别与智能系统、计算机数据库 计算机控制。不含管理信息系统、信息管理与信息系统
//
//		5.管理专业。主要包括行政管理、人力资源管理、公共管理劳动与社会保障等
//		6.英语专业。主要包括英语语言文学、英语、翻译专业(英语笔译、英语口译)、商务英语等
//		7.理工科:数学类、电子类、机械类、建筑类、材料类等
//		8.文秘:新闻学、汉语言文(字)学、中国古典文献学、中国现当代文学，中文，中国文学，古典文献，文秘学
				
		String[] strs1 = new String[]{ "经济" , "金融" , "投资"  , "银行" };
		DomainBean bean1 = new DomainBean("经济金融专业", strs1 , new String[]{"财政学" ,  "国际贸易" } );
		
		String[] strs2 = new String[]{ "财务" , "审计" , "会计"  };
		DomainBean bean2 = new DomainBean("会计专业", strs2  );
		
		String[] strs3 = new String[]{ "法学" , "法律" , "经济法"  , "经济法" , "民法" , "民商法" , "知识产权法"   };
		DomainBean bean3 = new DomainBean("法律专业", strs3  );
		
		String[] strs4 = new String[]{ "软件" ,"硬件","计算机","电子科学与技术","通信","信息安全" 
				,  "信息与计算科学" , "电子信息工程" ,"网络","模式识别与智能系统"  , "数据库"  };
		DomainBean bean4 = new DomainBean("计算机专业", strs4  , new String[]{"管理信息系统" , "信息管理与信息系统" });
		
		String[] strs5 = new String[]{ "行政管理" ,"人力资源管理"  , "公共管理"  , "社会保障"   };
		DomainBean bean5 = new DomainBean("管理专业", strs5  );
		
		String[] strs6 = new String[]{ "英语"   , "翻译"  };
		DomainBean bean6 = new DomainBean("英语专业", strs6  );
		
		String[] strs7 = new String[]{ "数学"   , "电子"   , "机械" , "建筑" , "材料" ,"机电"};
		DomainBean bean7 = new DomainBean("理工科", strs7  );
		
		String[] strs8 = new String[]{ "汉语"   , "古典文献"   , "当代文学" , "中文" , "文秘" };
		DomainBean bean8 = new DomainBean("文秘", strs8  );
		

		PostInfo post1 = new PostInfo("1" , "征信岗", PostInfo.EDU_junior_college, 18, 32, null);//经济金融、会计、法律、计算机、管理类相关专业
		post1.addPro(bean1).addPro(bean2).addPro(bean3).addPro(bean4).addPro(bean5).addPro(bean6);
		PostInfo post2 = new PostInfo("2" ,"支付结算", PostInfo.EDU_junior_college, 18, 32, null);//经济金融、会计、计算机类相关专业
		post2.addPro(bean1).addPro(bean2).addPro(bean4).addPro(bean6);
		PostInfo post3 = new PostInfo("3" ,"国库核算", PostInfo.EDU_junior_college, 18, 32, null);//经济金融、会计、计算机类相关专业
		post3.addPro(bean1).addPro(bean2).addPro(bean4).addPro(bean6);
		PostInfo post4 = new PostInfo("4" ,"货币发行", PostInfo.EDU_junior_college, 18, 32, PersonInfo.GENDER_MALE);//经济金融、会计、计算机、理工类相关专业
		post4.addPro(bean1).addPro(bean2).addPro(bean4).addPro(bean7).addPro(bean6);
		PostInfo post5 = new PostInfo("5" ,"文电综合", PostInfo.EDU_junior_college, 18, 32, null);//经济金融、文秘、计算机类相关专业
		post5.addPro(bean1).addPro(bean8).addPro(bean4).addPro(bean6);
		PostInfo post6 = new PostInfo("6" ,"会计核算", PostInfo.EDU_junior_college, 18, 32, null);//经济金融、会计类相关专业
		post6.addPro(bean1).addPro(bean2).addPro(bean6);
		PostInfo post7 = new PostInfo("7" ,"科技岗", PostInfo.EDU_junior_college, 18, 32, null);//计算机类相关专业
		post7.addPro(bean4);
		//部队转业干部、退役士兵和公安院校毕业生
		PostInfo post8 = new PostInfo("8" ,"守库押运", PostInfo.EDU_senior_high_school, 20, 32, PersonInfo.GENDER_MALE);
		
		domainBeanMapping.put(bean1.getName(), bean1);
		domainBeanMapping.put(bean2.getName(), bean2);
		domainBeanMapping.put(bean3.getName(), bean3);
		domainBeanMapping.put(bean4.getName(), bean4);
		domainBeanMapping.put(bean5.getName(), bean5);
		domainBeanMapping.put(bean6.getName(), bean6);
		domainBeanMapping.put(bean7.getName(), bean7);
		domainBeanMapping.put(bean8.getName(), bean8);
		

		//货币发行   会计核算   科技   支付结算   征信   国库核算   文电综合   守库押运
		postMapping.put("支付结算", post2);
		postMapping.put("文电综合", post5);
		postMapping.put("货币发行", post4);
		postMapping.put("会计核算", post6);
		postMapping.put("国库核算", post3);
		postMapping.put("征信", post1);
		postMapping.put("征信岗", post1);
		postMapping.put("守库押运", post8);
		postMapping.put("科技岗", post7);
		postMapping.put("科技", post7);
	}
	
	
	
	
	public static void main(String[] args) throws Exception {
		
		loadMappingInfo();
		
	}
}
