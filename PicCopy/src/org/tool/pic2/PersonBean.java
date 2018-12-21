package org.tool.pic2;

public class PersonBean {
	
	private int seq = 0;
	
	private String name ;
	private String idcard; 
	
	private String examAddress;
	private String examRoom;
	private String examNum;
	private String siteNum;
	
	private String gender;
	
	private String pic ;
	
	private String phone;
	private String area;
	
	
	public PersonBean(){}
	
	public PersonBean(String name, String idcard, String examAddress,
			String examRoom, String gender, String pic) {
		super();
		this.name = name;
		this.idcard = idcard;
		this.examAddress = examAddress;
		this.examRoom = examRoom;
		this.gender = gender;
		this.pic = pic;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getExamAddress() {
		return examAddress;
	}

	public void setExamAddress(String examAddress) {
		this.examAddress = examAddress;
	}

	public String getExamRoom() {
		return examRoom;
	}

	public void setExamRoom(String examRoom) {
		this.examRoom = examRoom;
	}

	public String getExamNum() {
		return examNum;
	}

	public void setExamNum(String examNum) {
		this.examNum = examNum;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getSiteNum() {
		return siteNum;
	}

	public void setSiteNum(String siteNum) {
		this.siteNum = siteNum;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	@Override
	public String toString() {
		return "PersonBean [seq=" + seq + ", name=" + name + ", idcard="
				+ idcard + ", 考试地点=" + examAddress + ", 考场="
				+ examRoom + ", 准考证号=" + examNum + ", gender=" + gender+ ", site=" + siteNum
				+ ", pic=" + pic + "]";
	}

	
	public void reNewExamNum() {
		String prifix = "1";
		String res = prifix;
		
		if( examRoom.length() == 3 ){
			res = res + examRoom;
		}else if( examRoom.length() == 2){
			res = res + "0" + examRoom;
		}else if( examRoom.length() == 1){
			res = res + "00" + examRoom;
		}
		
		if( siteNum.length() == 2 ){
			res = res + siteNum;
		}else if( siteNum.length() == 1){
			res = res + "0" + siteNum;
		}
		
		examNum = res;
		
	}
	
	
}
