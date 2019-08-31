package nms.newstat.tonc2;

import java.io.Serializable;

public class NCKwObj implements Serializable{
	
	private String ncKczz;
	private String ncKb;
	private String ncKw;
	
	private String u8Kw;
	
	//‘› ±Œﬁ”√
	private String u8Kczz;
	private String u8Kb;
	
	
	public NCKwObj(){
		
	}
	
	public String getNcKczz() {
		return ncKczz;
	}
	public void setNcKczz(String ncKczz) {
		this.ncKczz = ncKczz;
	}
	public String getNcKb() {
		return ncKb;
	}
	public void setNcKb(String ncKb) {
		this.ncKb = ncKb;
	}
	public String getNcKw() {
		return ncKw;
	}
	public void setNcKw(String ncKw) {
		this.ncKw = ncKw;
	}
	public String getU8Kczz() {
		return u8Kczz;
	}
	public void setU8Kczz(String u8Kczz) {
		this.u8Kczz = u8Kczz;
	}
	public String getU8Kb() {
		return u8Kb;
	}
	public void setU8Kb(String u8Kb) {
		this.u8Kb = u8Kb;
	}

	public String getU8Kw() {
		return u8Kw;
	}

	public void setU8Kw(String u8Kw) {
		this.u8Kw = u8Kw;
	}

	@Override
	public String toString() {
		return "NCKwObj [ncKczz=" + ncKczz + ", ncKb=" + ncKb + ", ncKw=" + ncKw + ", u8Kw=" + u8Kw + "]";
	}
	
}
