package nms;

public class InOutObj {
	
	public InOutObj(){}
	
	private boolean isIn = true;
	private String sn;
	private String pn;
	private String kw;
	private String num;
	private String dateStr;
	
	public boolean getIsIn() {
		return isIn;
	}
	public void setIsIn(boolean isIn) {
		this.isIn = isIn;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getPn() {
		return pn;
	}
	public void setPn(String pn) {
		this.pn = pn;
	}
	public String getKw() {
		return kw;
	}
	public void setKw(String kw) {
		this.kw = kw;
	}
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	
	public RowData toRowData(){
		RowData rd = new RowData();
		rd.setSnsStr(sn);
		rd.setPosNum(kw);
		rd.setMaterialNum(pn);
		return rd;
	}
	
}
