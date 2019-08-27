package nms.stat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;


public class StorePNObj {
	
	
	private String pn ;
	
	private List<KWCount> kws = new ArrayList<StorePNObj.KWCount>();
	
	private Map<String, Integer> kwToCount = new HashMap<String, Integer>();
	
	private int sumCount=0;
	
	public StorePNObj(String pn) {
		super();
		this.pn = pn.trim().toUpperCase();
	}

	public void addKwCount(  String kw , int count ){
		Integer integer = kwToCount.get(kw);
		if( integer == null  ){
			 kwToCount.put(kw, count);
		}else{
			kwToCount.put(kw, count+integer   );
		}
		sumCount += count;
	}
	
	public String getKWCountStr(){
		StringBuilder sb = new StringBuilder();
		Set<Entry<String,Integer>> entrySet = kwToCount.entrySet();
		for(  Entry<String,Integer> en : entrySet  ){
			String key = en.getKey();
			Integer value = en.getValue();
			sb.append(key+"="+ value + "  ");
		}
		return sb.toString();
	}
	
	public int getSumCount(){
		return sumCount;
	}
	
	
	public String getPn() {
		return pn;
	}

	public void setPn(String pn) {
		this.pn = pn;
	}

	public List<KWCount> getKws() {
		return kws;
	}

	public void setKws(List<KWCount> kws) {
		this.kws = kws;
	}

	public Map<String, Integer> getKwToCount() {
		return kwToCount;
	}

	public void setKwToCount(Map<String, Integer> kwToCount) {
		this.kwToCount = kwToCount;
	}



	static class KWCount{
		
		private String kw ;
		private int count =0 ;
		
		public KWCount(String kw, int count) {
			super();
			this.kw = kw;
			this.count = count;
		}
		public String getKw() {
			return kw;
		}
		public void setKw(String kw) {
			this.kw = kw;
		}
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
		}
		
		
	}
	
}
