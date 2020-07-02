package nms.t1023;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class OrderBean implements Serializable{
	
	private String oNum;
	private String rNum;
	
	private double count;
	
	private String prodNum;
	
	//入库单号 数量
	private Map<String, Double> inStoreToCount = new 	HashMap<String, Double>();
	
	

	public OrderBean(String oNum, String rNum, Double count, String prodNum, String storeNum) {
		this.oNum = oNum;
		this.rNum = rNum;
		this.count = count;
		this.prodNum = prodNum;
		inStoreToCount.put(storeNum, count);
		
	}

	public void addCount( String  inStoreNum , double tcnt , String tprodNum){
		if(  !prodNum.equals(tprodNum) ){
			throw new RuntimeException("产品编码不同 但是 工单号行号相同src:"+ prodNum +" , current:" + tprodNum);
		}
		count +=tcnt;
		inStoreToCount.put(inStoreNum  , tcnt);
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

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}

	public Map<String, Double> getInStoreToCount() {
		return inStoreToCount;
	}

	public void setInStoreToCount(Map<String, Double> inStoreToCount) {
		this.inStoreToCount = inStoreToCount;
	}
	
	public static String getKey(String oStr ,String rStr){
		return oStr +"-"+  rStr;
	} 
	
}	
