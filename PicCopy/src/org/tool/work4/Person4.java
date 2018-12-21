package org.tool.work4;

public class Person4 {
	
	
	public static String GENDER_MALE = "ÄÐ";
	public static String GENDER_FEMALE = "Å®";
	
	private String rowNum;
	String name= null; // "£º"+"Guodeqi";
	String gender= null; // "£º"+"ÄÐ";
	String idCard = null; //"£º"+"123456789";
	String organization = null;//"£º"+"XXX¹«Ë¾";
	String examRoomNum = null; 
	String posNum = null; 
	
	public Person4() {}
	
	

	public String getRowNum() {
		return rowNum;
	}

	public void setRowNum(String rowNum) {
		this.rowNum = rowNum;
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

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getExamRoomNum() {
		return examRoomNum;
	}
	public void setExamRoomNum(String examRoomNum) {
		this.examRoomNum = examRoomNum;
	}

	public String getPosNum() {
		return posNum;
	}
	public void setPosNum(String posNum) {
		this.posNum = posNum;
	}



	@Override
	public String toString() {
		return "Person4 [rowNum=" + rowNum + ", name=" + name + ", gender="
				+ gender + ", idCard=" + idCard + ", organization="
				+ organization + ", examRoomNum=" + examRoomNum + ", posNum="
				+ posNum + "]";
	}

	
}
