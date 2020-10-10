package w2020.t6;

import java.util.List;

public class RowInfo {

	private int rowNum;
	private String postJob ; //FE
	
	private int eduBack;  //L   学历    1 高中  2专科  3大学本科   4硕士研究生
	private String major;  //M
	private int birthDay;//R
	
	private String idCard ;
	
	//年龄	学历	专业
    //  符合 or   不符合
	public static final String yes = "符合";
	public static final String no = "不符合";
	
	private String flagAge;  //C
	private String flagEdu; //D
	private String flagMajor;//E
	
	
	public RowInfo(int i) {
		this.rowNum = i;
	}
	
	public int getRowNum() {
		return rowNum;
	}
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
	public String getPostJob() {
		return postJob;
	}
	public void setPostJob(String postJob) {
		this.postJob = postJob;
	}
	public int getEduBack() {
		return eduBack;
	}
	public void setEduBack(int eduBack) {
		this.eduBack = eduBack;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public int getBirthDay() {
		return birthDay;
	}
	public void setBirthDay(int birthDay) {
		this.birthDay = birthDay;
	}
	public String getFlagAge() {
		return flagAge;
	}
	public void setFlagAge(String flagAge) {
		this.flagAge = flagAge;
	}
	public String getFlagEdu() {
		return flagEdu;
	}
	public void setFlagEdu(String flagEdu) {
		this.flagEdu = flagEdu;
	}
	public String getFlagMajor() {
		return flagMajor;
	}
	public void setFlagMajor(String flagMajor) {
		this.flagMajor = flagMajor;
	}
	
	public void calcFlag(){
		JobBean jobBean = InitData.nameToJob.get(postJob);
		
		int minBirth = jobBean.getMinBirth();
		int maxBirth = jobBean.getMaxBirth();
		this.setFlagAge("AGE"+yes);
		if(  minBirth != -1  ){
			if(  birthDay<minBirth || birthDay>maxBirth ){
				this.setFlagAge("AGE"+no);
			}
		}
		
		int minEdu = jobBean.getEdu();
		this.setFlagEdu("XL"+yes);
		if(  this.eduBack<minEdu  ){
			this.setFlagEdu("XL"+no);
		}
		
		this.setFlagMajor("ZY"+no);
		List<String> majors = jobBean.getMajors();
		for(  String sss :  majors  ){
			List<String> noList = InitData.majorToNotlist.get(sss);
			List<String> yesList = InitData.majorToList.get(sss);
			boolean continueFlag = false;
			for(  String oneNo :  noList ){
				if( majorCompare(oneNo , major)  ){
					continueFlag = true;
					break;
				}
			}
			if(  continueFlag ){
				continue; 
			}
			
			boolean breakFlag = false;
			for(  String oneYes :  yesList ){
				if( majorCompare(oneYes , major)  ){
					this.setFlagMajor("ZY"+yes);
					breakFlag = true;
					break;
				}
			}
			if(   breakFlag ){
				break;
			}
		}
		
		if( this.getFlagMajor().contains( no )    ){
			System.out.println(   "确认专业:"  + major + "=>" +postJob+":"+  majors );
		}
		
	}

	private boolean majorCompare(String src, String major2) {
		
		
		if(   src.equals(major2)  ){
			return true;
		}
		if(   src.contains(major2) || major2.contains(src ) ){
			return true;
		}
		
		return false;
	}

	public void setEduBack(String eduBack2) {
		// 1 高中  2专科  3大学本科   4硕士研究生
		if( "高中".equals(eduBack2)){
			eduBack =1 ;
		}else if( "专科".equals(eduBack2)){
			eduBack =2 ;
		}else if( "大学本科".equals(eduBack2)){
			eduBack =3 ;
		}else if( "硕士研究生".equals(eduBack2)){
			eduBack =4 ;
		}
	}

	public void setIdCard(String idCard) {
		try {
			this.idCard = idCard;
			//身份证|410185199206124524
			String tt = idCard.substring(idCard.indexOf("|") + 1 );
			String birthStr = tt.substring(6, 14);
			birthDay = Integer.parseInt(birthStr);
		} catch ( Exception e) {
			System.out.println( (rowNum+1) + " ，身份证异常："   + idCard ) ;
		}
	}
	
	public static void main(String[] args) {
		
		String idCard = "身份证|410185199206124524";
		String tt = idCard.substring( idCard.indexOf("|") + 1 );
		String birthStr = tt.substring(6, 14);
		int birthDay = Integer.parseInt(birthStr);
		System.out.println( birthDay );
		
	}
	
}
