package nms.t1101;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ProdSubOrder implements Serializable{
	
	private String oNum;
	private String rNum;
	private String parentPN;
	
	private String subRNum;
	private String subPN;
	
	private Double u8SubNeedCount ;
	private Double u8SubHaveCount ;
	
	private String secProductFlag;
	
	
	//calc
	private Double u8ProdCount ;
	private Double u8DoneCount ;
	private Double ncParentProdCount ;
	
	private Double  ncSubNeedCount;
	private Double ncSubHaveCount ;
	
	private 	Double cc;
	private 	Double dd;
	
	private String remark;

	public ProdSubOrder(){}
	
	public ProdSubOrder(String oNum, String rNum, String parentPN,
			String subPN, Double u8SubNeedCount, Double u8SubHaveCount) {
		this.oNum = oNum;
		this.rNum = rNum;
		this.parentPN = parentPN;
		this.subPN = subPN;
		this.u8SubNeedCount = u8SubNeedCount;
		this.u8SubHaveCount = u8SubHaveCount;
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

	public String getParentPN() {
		return parentPN;
	}

	public void setParentPN(String parentPN) {
		this.parentPN = parentPN;
	}

	public String getSubPN() {
		return subPN;
	}

	public void setSubPN(String subPN) {
		this.subPN = subPN;
	}

	public Double getU8SubNeedCount() {
		return u8SubNeedCount;
	}

	public void setU8SubNeedCount(Double u8SubNeedCount) {
		this.u8SubNeedCount = u8SubNeedCount;
	}

	public Double getU8SubHaveCount() {
		return u8SubHaveCount;
	}

	public void setU8SubHaveCount(Double u8SubHaveCount) {
		this.u8SubHaveCount = u8SubHaveCount;
	}

	public Double getU8ProdCount() {
		return u8ProdCount;
	}

	public void setU8ProdCount(Double u8ProdCount) {
		this.u8ProdCount = u8ProdCount;
	}

	public Double getU8DoneCount() {
		return u8DoneCount;
	}

	public void setU8DoneCount(Double u8DoneCount) {
		this.u8DoneCount = u8DoneCount;
	}

	public Double getNcParentProdCount() {
		return ncParentProdCount;
	}

	public void setNcParentProdCount(Double ncParentProdCount) {
		this.ncParentProdCount = ncParentProdCount;
	}

	public Double getNcSubNeedCount() {
		return ncSubNeedCount;
	}

	public void setNcSubNeedCount(Double ncSubNeedCount) {
		this.ncSubNeedCount = ncSubNeedCount;
	}

	public Double getNcSubHaveCount() {
		return ncSubHaveCount;
	}

	public void setNcSubHaveCount(Double ncSubHaveCount) {
		this.ncSubHaveCount = ncSubHaveCount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getSubRNum() {
		return subRNum;
	}

	public void setSubRNum(String subRNum) {
		this.subRNum = subRNum;
	}

	public String getSecProductFlag() {
		return secProductFlag;
	}

	public void setSecProductFlag(String secProductFlag) {
		this.secProductFlag = secProductFlag;
	}

	public Double getCc() {
		return cc;
	}

	public void setCc(Double cc) {
		this.cc = cc;
	}

	public Double getDd() {
		return dd;
	}

	public void setDd(Double dd) {
		this.dd = dd;
	}

	public String getKey( ){
		return oNum +"-"+  rNum;
	} 
	

}	
