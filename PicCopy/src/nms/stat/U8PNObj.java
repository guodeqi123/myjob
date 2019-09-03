package nms.stat;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nms.newstat.tonc2.LoadNCKWInfo;
import nms.newstat.tonc34.LoadU8NCKwMap.U8kwPn_NcKbKw;

public class U8PNObj implements Serializable  {

	private String pn;
	private double newValue;  // 总数量
	
	private double oldValue;
	
	private Map<String,  Double> kbToCount = new HashMap<String, Double>(); 
	
	private U8PNObj(){
		
	}
	
	public U8PNObj(String pn) {
		super();
		this.pn = pn.toUpperCase().trim();
	}

	public String getPn() {
		return pn;
	}
	public void setPn(String pn) {
		this.pn = pn;
	}
	public double getNewValue() {
		return newValue;
	}
	public void setNewValue(double newValue) {
		this.newValue = newValue;
	}
	public void addNewValue(double v) {
		this.newValue += v;
	}
	public double getOldValue() {
		return oldValue;
	}
	public void setOldValue(int oldValue) {
		this.oldValue = oldValue;
	}
	public void addOldValue(double v) {
		this.oldValue += v;
	}
	
	public double getIncrement(){
		return newValue - oldValue;
	}

	public Map<String, Double> getKbToCount() {
		return kbToCount;
	}

	public void setKbToCount(Map<String, Double> kbToCount) {
		this.kbToCount = kbToCount;
	}
	
	public Double calcSum(){
		double ret = 0;
		Collection<Double> values = kbToCount.values();
		for( Double d : values ){
			ret +=d;
		}
		return ret;
	}

	public U8kwPn_NcKbKw getKWForVSN( String u8kb) {
		
		String NCKB = LoadNCKWInfo.kbU8ToNC.get(u8kb);
		
		String u8AndScankwNone = "NONE";
		U8kwPn_NcKbKw kwFor30 = new U8kwPn_NcKbKw(pn, u8AndScankwNone, u8kb, u8AndScankwNone, NCKB, "66", "期初-差异");
		U8kwPn_NcKbKw kwFor31 = new U8kwPn_NcKbKw(pn, u8AndScankwNone, u8kb, u8AndScankwNone, NCKB, "66", "期初-差异");
		U8kwPn_NcKbKw kwFor86 = new U8kwPn_NcKbKw(pn, u8AndScankwNone, u8kb, u8AndScankwNone, NCKB, "66", "期初-差异");
		
		Map<String, U8kwPn_NcKbKw> aa = new HashMap<String, U8kwPn_NcKbKw>();
		aa.put("30", kwFor30);
		aa.put("31", kwFor31);
		aa.put("86", kwFor86);
		
		U8kwPn_NcKbKw u8kwPn_NcKbKw = aa.get(u8kb);
		
		return u8kwPn_NcKbKw;
	}
	
}	
