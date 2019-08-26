package nms.newstat;

import java.util.HashSet;
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
	
	public Set<String> kwSet = new HashSet<String>();
	
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
		this.pn = pn;
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

	public Set<String> getKwSet() {
		return kwSet;
	}

	public void setKwSet(Set<String> kwSet) {
		this.kwSet = kwSet;
	}
	
	public void addKw(String kw ) {
		this.kwSet.add(kw);
	}
	
	
	
	
}
