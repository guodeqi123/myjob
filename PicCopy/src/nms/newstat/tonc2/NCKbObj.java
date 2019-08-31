package nms.newstat.tonc2;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class NCKbObj  implements Serializable{
	
	private String nckb;
	
	private Map<String,  NCKwObj> u8kwToObj = new HashMap<String, NCKwObj>();
	
	public NCKbObj( String  nckb ){
		this.nckb = nckb;
	}

	public String getNckb() {
		return nckb;
	}

	public void setNckb(String nckb) {
		this.nckb = nckb;
	}

	public Map<String, NCKwObj> getU8kwToObj() {
		return u8kwToObj;
	}

	public void setU8kwToObj(Map<String, NCKwObj> u8kwToObj) {
		this.u8kwToObj = u8kwToObj;
	}

	

}
