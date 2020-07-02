package nms.t1023;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class U8SubInfo implements Serializable{
	
	
	private String oNum;
	private String rNum;
	private String pn;
	
	private Map<String, String> subRNumToSubPN = new HashMap<String, String>();
	
	public Set<String> subPNumSecProd = new HashSet< String>();

	public U8SubInfo(String oNum, String rNum, String pn) {
		super();
		this.oNum = oNum;
		this.rNum = rNum;
		this.pn = pn;
	}
	
	public void putSubPN(  String subpn , String subRnum , String secProd   ){
		subRNumToSubPN.put( subRnum ,  subpn );
		if( "3".equals(secProd)  ){
			subPNumSecProd.add( subpn );
		}
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

	public Map<String, String> getSubRNumToSubPN() {
		return subRNumToSubPN;
	}

	public void setSubRNumToSubPN(Map<String, String> subRNumToSubPN) {
		this.subRNumToSubPN = subRNumToSubPN;
	}

	public static String getKey(String oStr ,String rStr){
		return oStr +"-"+  rStr;
	} 

	
}
