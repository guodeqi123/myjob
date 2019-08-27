package nms.newstat.pnc;

public class PNCompareObj {

	
	public String srcPn ; 
	public String toPn ; 
	
	public boolean u8Exist ; 
	
	public PNCompareObj() {
	}

	public PNCompareObj(String srcPn, String toPn, boolean u8Exist) {
		this.srcPn = srcPn;
		this.toPn = toPn;
		this.u8Exist = u8Exist;
	}

	public String getSrcPn() {
		return srcPn;
	}

	public void setSrcPn(String srcPn) {
		this.srcPn = srcPn;
	}

	public String getToPn() {
		return toPn;
	}

	public void setToPn(String toPn) {
		this.toPn = toPn;
	}

	public boolean isU8Exist() {
		return u8Exist;
	}

	public void setU8Exist(boolean u8Exist) {
		this.u8Exist = u8Exist;
	}


	
	
}
