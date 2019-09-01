package nms.newstat.tonc3;

import java.util.HashMap;
import java.util.Map;

public class U8KbObj {
	
	private String u8kb;
	
	Map<String, Double> pnToCountSN = new HashMap<String, Double>();
	Map<String, Double> pnToCountNotSN = new HashMap<String, Double>();
	//xx  xx
	Map<String, Double> pnToCountNotInNC = new HashMap<String, Double>();
	
	public U8KbObj(){}
	
	public U8KbObj( String u8kb ){
		this.u8kb =u8kb;
	}

	public String getU8kb() {
		return u8kb;
	}

	public void setU8kb(String u8kb) {
		this.u8kb = u8kb;
	}

	public Map<String, Double> getPnToCountSN() {
		return pnToCountSN;
	}

	public void setPnToCountSN(Map<String, Double> pnToCountSN) {
		this.pnToCountSN = pnToCountSN;
	}

	public Map<String, Double> getPnToCountNotSN() {
		return pnToCountNotSN;
	}

	public void setPnToCountNotSN(Map<String, Double> pnToCountNotSN) {
		this.pnToCountNotSN = pnToCountNotSN;
	}

	public Map<String, Double> getPnToCountNotInNC() {
		return pnToCountNotInNC;
	}

	public void setPnToCountNotInNC(Map<String, Double> pnToCountNotInNC) {
		this.pnToCountNotInNC = pnToCountNotInNC;
	}
	
	
	
}
