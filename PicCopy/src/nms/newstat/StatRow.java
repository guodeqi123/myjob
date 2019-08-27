package nms.newstat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StatRow {
	
	
	public StatRow(String pn){
		this.pn = pn;
	}
	
	public StatRow(){}
	
	public String pn;
	
	public int countU8 = -1;
	public int countStore = -1;
	public int countScan = -1;
	
	public boolean enableSN ;
	
	public Map<String,Integer> kwCountMap = new HashMap<String,Integer>();
	
	public String storeKwCountStr ;
	
	public int u8increase;
	
	public void addScan( int addValue){
		if(  countScan == -1 ){
			countScan = 0 ;
		}
		countScan = countScan +addValue;
	}
	
	public String getPn() {
		return pn;
	}
	public void setPn(String pn) {
		this.pn = pn.toUpperCase().trim();
	}
	public int getCountU8() {
		return countU8;
	}
	public void setCountU8(int countU8) {
		this.countU8 = countU8;
	}
	public int getCountStore() {
		return countStore;
	}
	public void setCountStore(int countStore) {
		this.countStore = countStore;
	}
	public int getCountScan() {
		return countScan;
	}
	public void setCountScan(int countScan) {
		this.countScan = countScan;
	}

	public boolean isEnableSN() {
		return enableSN;
	}

	public void setEnableSN(boolean enableSN) {
		this.enableSN = enableSN;
	}

	
	public void addKw(String kw ) {
		Integer integer = kwCountMap.get(kw);
		
		if( integer == null ){
			kwCountMap.put( kw , 1);
		}else{
			kwCountMap.put( kw , 1 + integer );
		}
		
	}

	public Map<String, Integer> getKwCountMap() {
		return kwCountMap;
	}

	public void setKwCountMap(Map<String, Integer> kwCountMap) {
		this.kwCountMap = kwCountMap;
	}

	public String getStoreKwCountStr() {
		return storeKwCountStr;
	}

	public void setStoreKwCountStr(String storeKwCountStr) {
		this.storeKwCountStr = storeKwCountStr;
	}

	public int getU8increase() {
		return u8increase;
	}

	public void setU8increase(int u8increase) {
		this.u8increase = u8increase;
	}
	
	
	
	
}
