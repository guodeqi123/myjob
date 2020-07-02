package nms.t1023;

import java.io.Serializable;

import nms.newstat.Convertor2;

public class OrderStatusBean implements Serializable{
	
	
	String oNum;
	String rNum;
	String pnStr;
	String typeDesc;
	Double sumNum  ;
	Double fiNum ;
	Double unFiNum ;
	
	
	public OrderStatusBean(String oNum, String rNum, String pnStr,
			String typeDesc, Double sumNum, Double fiNum, Double unFiNum) {
		super();
		this.oNum = oNum;
		this.rNum = rNum;
		this.pnStr = pnStr;
		this.typeDesc = typeDesc;
		this.sumNum = sumNum;
		this.fiNum = fiNum;
		this.unFiNum = unFiNum;
	}
	public String getoNum() {
		return oNum;
	}
	public void setoNum(String oNum) {
		this.oNum = oNum;
	}
	public String getrNum() {
		return rNum;
	}
	public void setrNum(String rNum) {
		this.rNum = rNum;
	}
	public String getPnStr() {
		return pnStr;
	}
	public void setPnStr(String pnStr) {
		this.pnStr = pnStr;
	}
	public String getTypeDesc() {
		return typeDesc;
	}
	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
	public Double getSumNum() {
		return sumNum;
	}
	public void setSumNum(Double sumNum) {
		this.sumNum = sumNum;
	}
	public Double getFiNum() {
		return fiNum;
	}
	public void setFiNum(Double fiNum) {
		this.fiNum = fiNum;
	}
	public Double getUnFiNum() {
		return unFiNum;
	}
	public void setUnFiNum(Double unFiNum) {
		this.unFiNum = unFiNum;
	}
	
	public static String getKey( String oStr ,String rStr ){
		return oStr +"-"+ rStr;
	}
	
	public void checkSelf() {
		
		if( sumNum != (fiNum +unFiNum ) ){
			throw new RuntimeException(  "已完成未完成与总数不匹配 oNum:" + oNum + " rNum:" + rNum);
		}
		
	}
	
}
