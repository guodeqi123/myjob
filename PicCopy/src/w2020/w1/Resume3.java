package w2020.w1;


import java.util.HashMap;
import java.util.Map;

public class Resume3 {

    public static int startRow = 1;
    public static Map<String,String> proNameToPos = new HashMap<String, String>();
    static {
        /*
        1  2  3  4  5  6  7  8  9  10
        A  B  C  D  E  F  G  H  I  J
        K  L  M  N  O  P  Q  R  S  T
        U  V  W  X  Y  Z
        */
        proNameToPos.put("name",     2 + ",4");
        proNameToPos.put("birthDay", 2 + ",6");
        proNameToPos.put("gender",   2 + ",8");
        proNameToPos.put("age",      2 + ",10");

        proNameToPos.put("nativePlace",   "3,4");
        proNameToPos.put("eduBackgroud",  "3,6");
        proNameToPos.put("eduDegree",     "3,8");
        proNameToPos.put("school",        "3,10");

        proNameToPos.put("graTime",   "4,4");
        proNameToPos.put("major",     "4,6");
        proNameToPos.put("stature",   "4,8");
        proNameToPos.put("phoneNo",   "4,10");

        proNameToPos.put("hsInTime",     "6,3");
        proNameToPos.put("hsOutTime",    "6,4");
        proNameToPos.put("hsBackgroup",  "6,5");
        proNameToPos.put("hsSchool",     "6,6");
        proNameToPos.put("hsMajor",      "6,7");

        proNameToPos.put("rccInTime",       "7,3");
        proNameToPos.put("rccOutTime",      "7,4");
        proNameToPos.put("rccBackgroup",    "7,5");
        proNameToPos.put("rccSchool",       "7,6");
        proNameToPos.put("rccMajor",        "7,7");
        proNameToPos.put("rccFullTimeFlag", "7,8");
        proNameToPos.put("rccUpFlag",       "7,9");
        proNameToPos.put("rccBath",         "7,10");

        proNameToPos.put("msInTime",         "8,3");
        proNameToPos.put("msOutTime",        "8,4");
        proNameToPos.put("msBackgroup",      "8,5");
        proNameToPos.put("msSchool",         "8,6");
        proNameToPos.put("msMajor",          "8,7");
        proNameToPos.put("msFullTimeFlag",   "8,8");

        proNameToPos.put("wish1",       "11,5");
        proNameToPos.put("wish2",       "11,9");

        proNameToPos.put("flag1",   "13,10");

        proNameToPos.put("flag2",   "14,10");

        proNameToPos.put("flag3",   "15,10");

        proNameToPos.put("flag4",   "16,10");

        proNameToPos.put("flag5",   "17,10");


    }

    private String idNo = "";    //�������
    private String name;//	����    2,D
    private String birthDay;//	��������  2,F
    private String gender;//�Ա�   2,H
    private String age; //����   2,J

    private String nativePlace;//����   3,D
    //�����ѧ��
    private String eduBackgroud;//ѧ��   3,F
    private String eduDegree;//ѧλ     3,H
    private String school;//ѧУ       3,J

    private String graTime;//��ҵʱ��  4,D
    private String major;//רҵ       4,F
    private String stature;//���    4,H
    private String phoneNo;//�ֻ���  4,J



    //����
    private String hsInTime; //��ѧʱ��    6,c
    private String hsOutTime;//��ҵʱ��    6,d
    private String hsBackgroup;//ѧ��     6,e
    private String hsSchool;//ѧУ        6,f
    private String hsMajor;//רҵ        6,g


    //����
    private String rccInTime;//��ѧʱ��   7,c
    private String rccOutTime;//��ҵʱ��   7,d
    private String rccBackgroup;//ѧ��   7,e
    private String rccSchool;//ѧУ     7,f
    private String rccMajor;//רҵ     7,g
    private String rccFullTimeFlag;//�Ƿ�ȫ����   7,h
    private String rccUpFlag;//�Ƿ�ר����    7,I
    private String rccBath;//¼ȡ����       7,J


    //�о���
    private String msInTime;//��ѧʱ��     8,c
    private String msOutTime;//��ҵʱ��    8,d
    private String msBackgroup;//ѧ��     8,e
    private String msSchool;//ѧУ       8,f
    private String msMajor;//רҵ       8, g
    private String msFullTimeFlag;//�Ƿ�ȫ����   8 , H

    private String wish1; //��Ըһ       //11  , EF
    private String wish2;//��Ը2        //11 , IJ


    //�����ľ�ҵ�ص���ԸΪ֣���з��У��Ƿ�ͬ����������������硢�ױڡ���������������⡢����Ͽ�ȸ������ߵ�����
     private String flag1;  // 13,j
    //�����ľ�ҵ�ص���ԸΪ�����������Ƿ�ͬ�����������Ͽ������
    private String flag2; // 14 ,j
    //�����ľ�ҵ�ص���ԸΪ�з��У��Ƿ�ͬ��������õ���������֧�У�
    private String flag3;  //15 ,j
    //����㱨����־Ը�������㣬�Ƿ�ͬ����ȫʡ��Χ�ڽ��е�����
    private String flag4;   //16,j
    //ͬ���������
    private String flag5;  //17 ,J
	public static int getStartRow() {
		return startRow;
	}
	public static void setStartRow(int startRow) {
		Resume3.startRow = startRow;
	}
	public static Map<String, String> getProNameToPos() {
		return proNameToPos;
	}
	public static void setProNameToPos(Map<String, String> proNameToPos) {
		Resume3.proNameToPos = proNameToPos;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBirthDay() {
		return birthDay;
	}
	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getNativePlace() {
		return nativePlace;
	}
	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}
	public String getEduBackgroud() {
		return eduBackgroud;
	}
	public void setEduBackgroud(String eduBackgroud) {
		this.eduBackgroud = eduBackgroud;
	}
	public String getEduDegree() {
		return eduDegree;
	}
	public void setEduDegree(String eduDegree) {
		this.eduDegree = eduDegree;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getGraTime() {
		return graTime;
	}
	public void setGraTime(String graTime) {
		this.graTime = graTime;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getStature() {
		return stature;
	}
	public void setStature(String stature) {
		this.stature = stature;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getHsInTime() {
		return hsInTime;
	}
	public void setHsInTime(String hsInTime) {
		this.hsInTime = hsInTime;
	}
	public String getHsOutTime() {
		return hsOutTime;
	}
	public void setHsOutTime(String hsOutTime) {
		this.hsOutTime = hsOutTime;
	}
	public String getHsBackgroup() {
		return hsBackgroup;
	}
	public void setHsBackgroup(String hsBackgroup) {
		this.hsBackgroup = hsBackgroup;
	}
	public String getHsSchool() {
		return hsSchool;
	}
	public void setHsSchool(String hsSchool) {
		this.hsSchool = hsSchool;
	}
	public String getHsMajor() {
		return hsMajor;
	}
	public void setHsMajor(String hsMajor) {
		this.hsMajor = hsMajor;
	}
	public String getRccInTime() {
		return rccInTime;
	}
	public void setRccInTime(String rccInTime) {
		this.rccInTime = rccInTime;
	}
	public String getRccOutTime() {
		return rccOutTime;
	}
	public void setRccOutTime(String rccOutTime) {
		this.rccOutTime = rccOutTime;
	}
	public String getRccBackgroup() {
		return rccBackgroup;
	}
	public void setRccBackgroup(String rccBackgroup) {
		this.rccBackgroup = rccBackgroup;
	}
	public String getRccSchool() {
		return rccSchool;
	}
	public void setRccSchool(String rccSchool) {
		this.rccSchool = rccSchool;
	}
	public String getRccMajor() {
		return rccMajor;
	}
	public void setRccMajor(String rccMajor) {
		this.rccMajor = rccMajor;
	}
	public String getRccFullTimeFlag() {
		return rccFullTimeFlag;
	}
	public void setRccFullTimeFlag(String rccFullTimeFlag) {
		this.rccFullTimeFlag = rccFullTimeFlag;
	}
	public String getRccUpFlag() {
		return rccUpFlag;
	}
	public void setRccUpFlag(String rccUpFlag) {
		this.rccUpFlag = rccUpFlag;
	}
	public String getRccBath() {
		return rccBath;
	}
	public void setRccBath(String rccBath) {
		this.rccBath = rccBath;
	}
	public String getMsInTime() {
		return msInTime;
	}
	public void setMsInTime(String msInTime) {
		this.msInTime = msInTime;
	}
	public String getMsOutTime() {
		return msOutTime;
	}
	public void setMsOutTime(String msOutTime) {
		this.msOutTime = msOutTime;
	}
	public String getMsBackgroup() {
		return msBackgroup;
	}
	public void setMsBackgroup(String msBackgroup) {
		this.msBackgroup = msBackgroup;
	}
	public String getMsSchool() {
		return msSchool;
	}
	public void setMsSchool(String msSchool) {
		this.msSchool = msSchool;
	}
	public String getMsMajor() {
		return msMajor;
	}
	public void setMsMajor(String msMajor) {
		this.msMajor = msMajor;
	}
	public String getMsFullTimeFlag() {
		return msFullTimeFlag;
	}
	public void setMsFullTimeFlag(String msFullTimeFlag) {
		this.msFullTimeFlag = msFullTimeFlag;
	}
	public String getWish1() {
		return wish1;
	}
	public void setWish1(String wish1) {
		this.wish1 = wish1;
	}
	public String getWish2() {
		return wish2;
	}
	public void setWish2(String wish2) {
		this.wish2 = wish2;
	}
	public String getFlag1() {
		return flag1;
	}
	public void setFlag1(String flag1) {
		this.flag1 = flag1;
	}
	public String getFlag2() {
		return flag2;
	}
	public void setFlag2(String flag2) {
		this.flag2 = flag2;
	}
	public String getFlag3() {
		return flag3;
	}
	public void setFlag3(String flag3) {
		this.flag3 = flag3;
	}
	public String getFlag4() {
		return flag4;
	}
	public void setFlag4(String flag4) {
		this.flag4 = flag4;
	}
	public String getFlag5() {
		return flag5;
	}
	public void setFlag5(String flag5) {
		this.flag5 = flag5;
	}






}




