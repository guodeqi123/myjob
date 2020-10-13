package w2020.t7;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import w2020.FUtil;
import w2020.IRowToBean;

public class Person7 implements IRowToBean{
	
	public static final String degree0 = "高中";
	public static final String degree1 = "专科";
	public static final String degree2 = "本科";
	public static final String degree3 = "硕士";
	public static final String degree4 = "博士";
	
	private int row ;
	private String idCard ;  //I   8 
	
	private String postCompany; //Q  16     报考单位
	private String edu;//最高学历学历 W 22
	private String collage; //最高学历院校  V  21
	private String degree;//最高学历学位 X  23    (本科  硕士  博士)
	//1.社会统一招聘考生须具有国家教育行政机关承认的普通高等院校大学本科及以上学历，并取得相应学位
	//2 其中，报考郑州农商银行（上街支行除外）的， 须具有国家教育行政机关认可的“双一流”建设高校大学本科及以上学
	//历或国家教育行政机关认可的普通高等院校硕士研究生及以上学历，并取得相应学位
	
	private String name;  //F 5
	private String gender;//G  6
	private String birth;//H  7       
	
	private String graduateYear ;  //AA  26          暂时忽略<= 2020-10-30
	
	public  static final String getType1 = "贫困生";
	public  static final String getType2 = "其他专业";
	public  static final String getType3 = "信息科技类专业";
	private String getType;	//招聘类别  T  19   (  贫困生 / 其他专业 / 信息科技类专业 )
	private String major;//专业  Y   24
	//2社会统一招聘中报考信息科技专业的考生，所学专业需为电子信息类、计算机类专业
	//3社会统一招聘中其他专业考生所学专业需为马克思主义理论类、哲学、经济学、法学、教育学、文学、理学、工学、农学、管理学等相关专业
	//4社会统一招聘中具有普通高等院校硕士研究生及以上学历的考生，不受上述第3条中招聘专业限制。但如报考信息科技专业，需符合第2条专业限制
	
	private int age ;  //calc
	//1贫困家庭大学生专项招聘中，大专、本科学历年龄在27周岁以下（1993年1月1日后出生）；硕士研究生及以上学历年龄在30周岁以下（1990年1月1日后出生）。
	//2.社会统一招聘中，本科学历年龄在26周岁以下（1994年1月1日后出生）；硕士研究生及以上学历年龄在28周岁以下（1992年1月1日后出生）
	
	private int seqNum;  //calc
	private String passFlag;  //calc
	public  static final String yes = "是";
	public  static final String no = "否";
	
	private String reason = "*";
	
	public void calcPass(){
		age = FUtil.calcAge( birth);
		passFlag = yes;
		
		//校验年龄
		if( getType1.equals(getType)  ){
			if(  degree2.equals(degree) && age > 27 ){
				passFlag = no;
			}
			if(  (degree3.equals(degree) || degree4.equals(degree)  ) && age > 30 ){
				passFlag = no;
			}
		}else{
			if(  degree2.equals(degree) && age > 26 ){
				passFlag = no;
			}
			if(  (degree3.equals(degree) || degree4.equals(degree) ) && age > 28 ){
				passFlag = no;
			}
		}
		
		
		if( no.equals(passFlag  )  ){
			reason = "年龄不符";
			return ;
		}
		
		//校验学历学校
		if( degree1.equals(degree) || StringUtils.isEmpty(degree) ){
			passFlag = no;
		}
		if( postCompany.contains("郑州农商银行") && !postCompany.contains("上街")  ){
			if(  !degree3.equals(degree) && !degree4.equals(degree)  ){
				if(  !InitData7.coll1.contains(collage) && !InitData7.coll2.contains(collage) ) {
					passFlag = no;
				}
			}
		}
		if( no.equals(passFlag  )  ){
			reason = "学历学校不符";
			return ;
		}
		
		//校验专业
		if(  getType3.equals(getType) ){
			String majorType = getMajorType(major);
			if( !"电子信息类".equals(majorType) && !"计算机类".equals(majorType) ){
				passFlag=no;
			}
		}
		if(  getType2.equals(getType) && degree2.equals(degree)   ){
			String majorType = getMajorType(major);
			if( StringUtils.isEmpty( majorType) ){
				passFlag=no;
			}
		}
		if( no.equals(passFlag  )  ){
			reason = "专业不符";
			return ;
		}
	}
	
	public String getMajorType(String major2) {
		
		Set<Entry<String,String>> entrySet = InitData7.typeToMajor.entrySet();
		for(  Entry<String,String> en : entrySet   ){
			String key = en.getKey();
			String majorType = en.getValue();
			
			if(   key.equals(major2)  ){
				return majorType;
			}
			
			String tt = major2;
			if(  tt.endsWith("专业")  ){
				tt = tt.substring(0 , tt.length()-2);
			}
			if(  key.equals(tt)  ){
				return majorType;
			}
		}
		
		
		return null;
	}




	public static void main(String[] args) {
		String aaaa = "1991-12-05";
		System.out.println(  FUtil.calcAge(aaaa) );
	}


	private static Map<String , Integer > proToCol = new HashMap<String, Integer>();
	static{
		proToCol.put("idCard", 8);
		proToCol.put("postCompany", 16 );
		proToCol.put("edu", 22);
		proToCol.put("collage", 21);
		proToCol.put("degree", 23);
		
		proToCol.put("name", 5);
		proToCol.put("gender", 6);
		proToCol.put("birth", 7);
		
		proToCol.put("graduateYear", 26);
		
		proToCol.put("getType", 19);
		proToCol.put("major", 24);
	}
	
	@Override
	public Map<String, Integer> getProToCol() {
		return proToCol;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getPostCompany() {
		return postCompany;
	}

	public void setPostCompany(String postCompany) {
		this.postCompany = postCompany;
	}

	public String getEdu() {
		return edu;
	}

	public void setEdu(String edu) {
		this.edu = edu;
	}

	public String getCollage() {
		return collage;
	}

	public void setCollage(String collage) {
		this.collage = collage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getGraduateYear() {
		return graduateYear;
	}

	public void setGraduateYear(String graduateYear) {
		this.graduateYear = graduateYear;
	}

	public String getPassFlag() {
		return passFlag;
	}

	public void setPassFlag(String passFlag) {
		this.passFlag = passFlag;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getGetType() {
		return getType;
	}

	public void setGetType(String getType) {
		this.getType = getType;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	
}
