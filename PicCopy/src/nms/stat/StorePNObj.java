package nms.stat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import nms.newstat.FPath;
import nms.newstat.tonc34.LoadU8NCKwMap;
import nms.newstat.tonc34.LoadU8NCKwMap.U8kwPn_NcKbKw;


public class StorePNObj  implements Serializable  {
	
	
	private String pn ;
	
	private List<KWCount> kws = new ArrayList<StorePNObj.KWCount>();
	
	private Map<String, Double> kwToCount = new HashMap<String, Double>();
	
	private Map<String, Map<String, KWCount>> kbToKwCntMap = new HashMap<String, Map<String, KWCount>>();
	
	private double sumCount=0;
	
	public StorePNObj(String pn) {
		super();
		this.pn = pn.trim().toUpperCase();
	}

	public void addKwCount(  String u8kw , double count , String u8kb ){
		u8kw = u8kw.toUpperCase().trim();
		Double integer = kwToCount.get(u8kw);
		if( integer == null  ){
			 kwToCount.put(u8kw, count);
		}else{
			kwToCount.put(u8kw, count+integer   );
		}
		sumCount += count;
		
		
		Map<String, KWCount> kwCntMap = kbToKwCntMap.get(u8kb);
		if( kwCntMap == null ){
			kwCntMap = new HashMap<String, StorePNObj.KWCount>();
			kbToKwCntMap.put(  u8kb , kwCntMap);
		}
		KWCount kwCount = kwCntMap.get(u8kw);
		if( kwCount ==null ){
			kwCount = new KWCount(u8kw, count);
			kwCount.setU8kb(u8kb);
			kwCntMap.put(u8kw, kwCount);
		}else{
			kwCount.setCount(kwCount.getCount() + count);
		}
		
	}
	
	public double calcSum(){
		double ret = 0;
		
		Set<Entry<String,Map<String,KWCount>>> entrySet = kbToKwCntMap.entrySet();
		for(  Entry<String,Map<String,KWCount>> en : entrySet  ){
			Map<String, KWCount> value = en.getValue();
			Set<Entry<String,KWCount>> entrySet2 = value.entrySet();
			for(   Entry<String,KWCount> en2 :  entrySet2){
				KWCount value2 = en2.getValue();
				double count = value2.getCount();
				ret += count;
			}
		}
		
		return ret;
	}
	
	public String getKWCountStr(){
		StringBuilder sb = new StringBuilder();
		Set<Entry<String,Double>> entrySet = kwToCount.entrySet();
		for(  Entry<String,Double> en : entrySet  ){
			String key = en.getKey();
			Double value = en.getValue();
			sb.append(key+"="+ value + " ");
		}
		return sb.toString().trim();
	}
	
	public double getSumCount(){
		return sumCount;
	}
	
	
	public String getPn() {
		return pn;
	}

	public void setPn(String pn) {
		this.pn = pn.toUpperCase().trim();
	}

	public List<KWCount> getKws() {
		return kws;
	}

	public void setKws(List<KWCount> kws) {
		this.kws = kws;
	}

	public Map<String, Double> getKwToCount() {
		return kwToCount;
	}

	public void setKwToCount(Map<String, Double> kwToCount) {
		this.kwToCount = kwToCount;
	}



	static class KWCount implements Serializable{
		
		private String u8kb ;
		private String u8kw ;
		private double count =0 ;
		
		public KWCount(String kw, double count) {
			this.u8kw = kw;
			this.count = count;
		}
		public double getCount() {
			return count;
		}
		public void setCount(double count) {
			this.count = count;
		}
		public String getU8kw() {
			return u8kw;
		}
		public void setU8kw(String u8kw) {
			this.u8kw = u8kw;
		}
		public String getU8kb() {
			return u8kb;
		}
		public void setU8kb(String u8kb) {
			this.u8kb = u8kb;
		}
		
	}


	public U8kwPn_NcKbKw getKWForVSN( ) {
		
		
		Map<String, KWCount> map = kbToKwCntMap.get("30");
		if( map==null ){
			String kw = null;
			Collection<Map<String,KWCount>> values = kbToKwCntMap.values();
			for( Map<String,KWCount> kwToCntMap : values   ){
				Set<Entry<String,KWCount>> entrySet = kwToCntMap.entrySet();
				for(   Entry<String,KWCount> en : entrySet ){
					KWCount value = en.getValue();
					String u8kw = value.getU8kw();
					if(   kw == null ){
						kw = u8kw;
					}else{
						int compareTo = kw.compareTo(u8kw);
						if( compareTo>=0  ){
							kw = u8kw;
						}
					}
				}
				U8kwPn_NcKbKw u8kwPn_NcKbKw = LoadU8NCKwMap.getNCKWBykw_pn(kw, pn);
				return u8kwPn_NcKbKw;
			}
			
		}else{
			String kw = null;
			Set<Entry<String,KWCount>> entrySet = map.entrySet();
			for(  Entry<String,KWCount> en :   entrySet){
				KWCount value = en.getValue();
				String u8kw = value.getU8kw();
				if(   kw == null ){
					kw = u8kw;
				}else{
					int compareTo = kw.compareTo(u8kw);
					if( compareTo>=0  ){
						kw = u8kw;
					}
				}
			}
			U8kwPn_NcKbKw u8kwPn_NcKbKw = LoadU8NCKwMap.getNCKWBykw_pn(kw, pn);
			return u8kwPn_NcKbKw;
		}
		
		return null;
	}
	
}
