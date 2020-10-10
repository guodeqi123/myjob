package w2020.t6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class InitData {

	public static final String m1 = "经济金融专业";
	public static final String m2 = "会计专业";
	public static final String m3 = "法律专业";
	public static final String m4 = "计算机专业";
	public static final String m5 = "管理专业";
	public static final String m6 = "英语专业";
	public static final String m7 = "理工科";
	public static final String m8 = "文秘";
	public static final String m9 = "统计类";
	public static final String m10 = "公安";
	
	public static Map<String, List<String> > majorToList = new HashMap();
	public static Map<String, List<String> > majorToNotlist = new HashMap();
	
	
	public static LinkedHashMap<String, JobBean> nameToJob = new LinkedHashMap();
	public static void initJob(){
		initJob1();
		initJob2();
		initJob3();
		initJob4();
		initJob5();
		initJob6();
		initJob7();
		initJob8();
		initJob9();
	}
	
	
	private static void initJob1() {
		JobBean job = new JobBean("征信");
		job.setEdu(2);
		job.addMajor(m1).addMajor(m2).addMajor(m3).addMajor(m4).addMajor(m5);
		job.setMinBirth(19870810);
		job.setMaxBirth(20020809);
		job.setGender(null);
		nameToJob.put(job.getName(), job);
	}
	private static void initJob2() {
		JobBean job = new JobBean("支付结算");
		job.setEdu(2);
		job.addMajor(m1).addMajor(m2).addMajor(m4);
		job.setMinBirth(-1);
		job.setMaxBirth(-1);
		job.setGender(null);
		nameToJob.put(job.getName(), job);
	}
	private static void initJob3() {
		JobBean job = new JobBean("国库核算");
		job.setEdu(2);
		job.addMajor(m1).addMajor(m2).addMajor(m4);
		job.setMinBirth(-1);
		job.setMaxBirth(-1);
		job.setGender(null);
		nameToJob.put(job.getName(), job);
	}
	private static void initJob4() {
		JobBean job = new JobBean("外汇管理");
		job.setEdu(2);
		job.addMajor(m1).addMajor(m3).addMajor(m4);
		job.setMinBirth(-1);
		job.setMaxBirth(-1);
		job.setGender(null);
		nameToJob.put(job.getName(), job);
	}
	private static void initJob5() {
		JobBean job = new JobBean("文电综合");
		job.setEdu(2);
		job.addMajor(m1).addMajor(m8).addMajor(m3).addMajor(m5).addMajor(m4);
		job.setMinBirth(-1);
		job.setMaxBirth(-1);
		job.setGender(null);
		nameToJob.put(job.getName(), job);
	}
	private static void initJob6() {
		JobBean job = new JobBean("会计核算");
		job.setEdu(2);
		job.addMajor(m1).addMajor(m2).addMajor(m9);
		job.setMinBirth(-1);
		job.setMaxBirth(-1);
		job.setGender(null);	
		nameToJob.put(job.getName(), job);
	}
	private static void initJob7() {
		JobBean job = new JobBean("科技");
		job.setEdu(2);
		job.addMajor(m1).addMajor(m4);
		job.setMinBirth(-1);
		job.setMaxBirth(-1);
		job.setGender(null);
		nameToJob.put(job.getName(), job);
	}
	private static void initJob8() {
		JobBean job = new JobBean("货币发行");
		job.setEdu(2);
		job.addMajor(m1).addMajor(m2).addMajor(m4).addMajor(m7);
		job.setMinBirth(-1);
		job.setMaxBirth(-1);
		job.setGender("男");
		nameToJob.put(job.getName(), job);
	}
	private static void initJob9() {
		JobBean job = new JobBean("守库押运");
		job.setEdu(1);
		job.addMajor(m10);
		job.setMinBirth(19870810);
		job.setMaxBirth(20000809);
		job.setGender("男");
		nameToJob.put(job.getName(), job);
	}


	public static void init(  ) {
		initM1();
		initM2();
		initM3();
		initM4();
		initM5();
		initM6();
		initM7();
		initM8();
		initM9();
		initM10();
		
			
	}

	private static void initM10() {
		List<String> list = new ArrayList<String>();
		List<String> listNot = new ArrayList<String>();
		list.add("部队转业干部");
		list.add("退役士兵");
		list.add("公安");
		list.add("体育");
		majorToList.put(m10, list);
		majorToNotlist.put(m10, listNot);	
	}
	
	private static void initM9() {
		List<String> list = new ArrayList<String>();
		List<String> listNot = new ArrayList<String>();
		String str1  = "统计学、统计应用与经济计量分析、经济管理统计、应用数理统计、经济统计与分析、"
				+ "应用统计、概率论与数理统计、计算数学、统计与概算、计量经济学、运筹学、数理统计、信息与计算科学";
		String[] split = str1.split("、");
		for( String s : split   ){
			list.add(s);
		}
		majorToList.put(m9, list);
		majorToNotlist.put(m9, listNot);		
	}

	private static void initM8() {
		List<String> list = new ArrayList<String>();
		List<String> listNot = new ArrayList<String>();
		String str1  = "新闻学、汉语言文学_汉语言文（字）学、中国古典文献学、中国现当代文学、中文、中国文学、古典文献、文秘学";
		String[] split = str1.split("、");
		for( String s : split   ){
			list.add(s);
		}
		majorToList.put(m8, list);
		majorToNotlist.put(m8, listNot);		
		
	}

	private static void initM7() {
		List<String> list = new ArrayList<String>();
		List<String> listNot = new ArrayList<String>();
		//数学类、电子类、机械类、建筑类、材料类
		String str1  = "数学、电子、机械、建筑、材料、工程";
		String[] split = str1.split("、");
		for( String s : split   ){
			list.add(s);
		}
		majorToList.put(m7, list);
		majorToNotlist.put(m7, listNot);		
		
	}

	private static void initM6() {
		List<String> list = new ArrayList<String>();
		List<String> listNot = new ArrayList<String>();
		String str1  = "英语语言文学、英语、翻译专业（英语笔译、英语口译）、商务英语";
		String[] split = str1.split("、");
		for( String s : split   ){
			list.add(s);
		}
		majorToList.put(m6, list);
		majorToNotlist.put(m6, listNot);		
	}

	private static void initM5() {
		
		List<String> list = new ArrayList<String>();
		List<String> listNot = new ArrayList<String>();
		String str1  = "行政管理、人力资源管理、公共管理、劳动与社会保障";
		String[] split = str1.split("、");
		for( String s : split   ){
			list.add(s);
		}
		majorToList.put(m5, list);
		majorToNotlist.put(m5, listNot);		
	}

	private static void initM4() {
		List<String> list = new ArrayList<String>();
		List<String> listNot = new ArrayList<String>();
		String str1  = "计算机软件、计算机硬件、计算机应用、电子科学与技术、通信技术、信息安全、"
				+ "计算机科学与技术、通信工程、计算机信息管理、信息与计算科学、软件技术、电子信息工程、"
				+ "网络工程、模式识别与智能系统、计算机数据库、计算机控制、软件工程、计算机网络技术、计算机";
		String[] split = str1.split("、");
		for( String s : split   ){
			list.add(s);
		}
		listNot.add("管理信息系统");
		listNot.add("信息管理");
		listNot.add("信息系统");
		majorToList.put(m4, list);
		majorToNotlist.put(m4, listNot);		
	}

	private static void initM3() {
		List<String> list = new ArrayList<String>();
		List<String> listNot = new ArrayList<String>();
		String str1  = "法学、法律、行政法学、经济法、民法、民商法、知识产权法、法律硕士";
		String[] split = str1.split("、");
		for( String s : split   ){
			list.add(s);
		}
		majorToList.put(m3, list);
		majorToNotlist.put(m3, listNot);		
		
	}

	private static void initM2() {
		List<String> list = new ArrayList<String>();
		List<String> listNot = new ArrayList<String>();
		String str1  = "财务管理、审计、会计、会计硕士、注册会计师、国际会计";
		String[] split = str1.split("、");
		for( String s : split   ){
			list.add(s);
		}
		majorToList.put(m2, list);
		majorToNotlist.put(m2, listNot);		
	}

	private static void initM1() {
		List<String> list1 = new ArrayList<String>();
		List<String> listNot1 = new ArrayList<String>();
		String str1  = "宏观经济、金融学、国际金融、金融工程、"
				+ "区域经济学、国民经济学、产业经济学、政治经济学、"
				+ "数量经济学、人口资源环境经济学、金融管理与实务、投资学";
		String[] split = str1.split("、");
		for( String s : split   ){
			list1.add(s);
		}
		listNot1.add("财政学");
		listNot1.add("国际贸易");
		majorToList.put(m1, list1);
		majorToNotlist.put(m1, listNot1);		
	}
	
	
	
	
}
