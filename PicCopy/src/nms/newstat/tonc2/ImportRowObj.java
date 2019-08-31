package nms.newstat.tonc2;

import java.io.Serializable;

public class ImportRowObj implements Serializable{
	
	private int titleNum;
	//* 行号	* 物料编码	* 单位	* 数量	* 入库日期	货位	序列号	序列号单位
	private int rowNum;
	
	private String pn;
	
	private String unit;
	
	private double count;
	
	private String dateStr = "2019-08-31";
	
	private String nckw = "";
	
	private String sn ="";
	
	private String snUnit  ="";
	
	public ImportRowObj(){
		
	}
	
	public ImportRowObj(int titleNum, int rowNum, String pn, String unit,double count, String dateStr, String nckw) {
		this.titleNum = titleNum;
		this.rowNum = rowNum;
		this.pn = pn;
		this.unit = unit;
		this.count = count;
		this.dateStr = dateStr;
		this.nckw = nckw;
	}


	public int getTitleNum() {
		return titleNum;
	}

	public void setTitleNum(int titleNum) {
		this.titleNum = titleNum;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public String getPn() {
		return pn;
	}

	public void setPn(String pn) {
		this.pn = pn;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public String getNckw() {
		return nckw;
	}

	public void setNckw(String nckw) {
		this.nckw = nckw;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getSnUnit() {
		return snUnit;
	}

	public void setSnUnit(String snUnit) {
		this.snUnit = snUnit;
	}
	
}
