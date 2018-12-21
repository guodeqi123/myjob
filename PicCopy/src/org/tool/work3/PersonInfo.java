package org.tool.work3;

public class PersonInfo {
	
	
	public static String GENDER_MALE = "ÄÐ";
	public static String GENDER_FEMALE = "Å®";
	
	private int rowNum;
	private String deliverPost;
	private int edu;
	private String profession;
	private int age;
	private String gender;
	private String identity;
	private String school;
	
	
	private String msg;
	
	public PersonInfo() {}
	
	public PersonInfo(int rowNum, String deliverPost, int edu,
			String profession, int age, String gender , String identity) {
		super();
		this.rowNum = rowNum;
		this.deliverPost = deliverPost;
		this.edu = edu;
		this.profession = profession;
		this.age = age;
		this.gender = gender;
		this.identity = identity;
	}
	
	public int getRowNum() {
		return rowNum;
	}
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
	public String getDeliverPost() {
		return deliverPost;
	}
	public void setDeliverPost(String deliverPost) {
		this.deliverPost = deliverPost;
	}
	public int getEdu() {
		return edu;
	}
	public void setEdu(int edu) {
		this.edu = edu;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}
	
	
	
	
}
