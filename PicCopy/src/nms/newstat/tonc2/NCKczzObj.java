package nms.newstat.tonc2;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class NCKczzObj  implements Serializable{

	private String nckczz;
	
	private Map<String, NCKbObj> nckbToObj = new HashMap<String, NCKbObj>();
	
	public NCKczzObj(String nckczz ){
		this.nckczz = nckczz;
	}

	public String getNckczz() {
		return nckczz;
	}

	public void setNckczz(String nckczz) {
		this.nckczz = nckczz;
	}

	public Map<String, NCKbObj> getNckbToObj() {
		return nckbToObj;
	}

	public void setNckbToObj(Map<String, NCKbObj> nckbToObj) {
		this.nckbToObj = nckbToObj;
	}

	
}
