package nms.t1101;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProdOrder implements Serializable{
	
	private String oNum;
	private String rNum;
	
	private String key;
	private String pn;
	
	Double sumCount ;
	Double doneCount ;
	
	private List<ProdSubOrder>  subInfos = new ArrayList<ProdSubOrder>();
	
	public ProdOrder(String key, String pn,  Double sumCount, Double doneCount) {
		this.key = key;
		this.pn = pn;
		this.sumCount = sumCount;
		this.doneCount = doneCount;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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

	public String getPn() {
		return pn;
	}

	public void setPn(String pn) {
		this.pn = pn;
	}

	public Double getSumCount() {
		return sumCount;
	}

	public void setSumCount(Double sumCount) {
		this.sumCount = sumCount;
	}

	public Double getDoneCount() {
		return doneCount;
	}

	public void setDoneCount(Double doneCount) {
		this.doneCount = doneCount;
	}

	public List<ProdSubOrder> getSubInfos() {
		return subInfos;
	}

	public void setSubInfos(List<ProdSubOrder> subInfos) {
		this.subInfos = subInfos;
	}
	
	public void addSubRow( ProdSubOrder subOrder){
		subInfos.add( subOrder  );
	}

}	
