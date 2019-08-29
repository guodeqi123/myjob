package nms.stat;

import java.io.Serializable;

public class U8PNObj implements Serializable  {

	private String pn;
	private int newValue;
	private int oldValue;
	private U8PNObj(){
		
	}
	
	public U8PNObj(String pn) {
		super();
		this.pn = pn;
	}

	public String getPn() {
		return pn;
	}
	public void setPn(String pn) {
		this.pn = pn;
	}
	public int getNewValue() {
		return newValue;
	}
	public void setNewValue(int newValue) {
		this.newValue = newValue;
	}
	public void addNewValue(int v) {
		this.newValue += v;
	}
	public int getOldValue() {
		return oldValue;
	}
	public void setOldValue(int oldValue) {
		this.oldValue = oldValue;
	}
	public void addOldValue(int v) {
		this.oldValue += v;
	}
	
	public int getIncrement(){
		return newValue - oldValue;
	} 
	
}	
