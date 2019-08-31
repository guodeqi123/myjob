package nms.newstat.tonc2;

import java.io.Serializable;

public class ImportTitleObj implements Serializable{

	private int titleNum;
	//* 库存组织	* 单据日期	* 仓库	* 出入库类型
	private String ncKczz;
	private String dateStr = "2019-08-31" ;
	private String nckb;
	private String inoutType = "期初余额";
	
	public ImportTitleObj(){
		
	}

	public ImportTitleObj(int titleNum, String ncKczz, String dateStr, String nckb) {
		this.titleNum = titleNum;
		this.ncKczz = ncKczz;
		this.dateStr = dateStr;
		this.nckb = nckb;
	}



	public int getTitleNum() {
		return titleNum;
	}

	public void setTitleNum(int titleNum) {
		this.titleNum = titleNum;
	}

	public String getNcKczz() {
		return ncKczz;
	}

	public void setNcKczz(String ncKczz) {
		this.ncKczz = ncKczz;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public String getNckb() {
		return nckb;
	}

	public void setNckb(String nckb) {
		this.nckb = nckb;
	}

	public String getInoutType() {
		return inoutType;
	}

	public void setInoutType(String inoutType) {
		this.inoutType = inoutType;
	}
	
	
	
}
