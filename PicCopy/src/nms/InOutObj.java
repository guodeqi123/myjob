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
		if( sn!=null ){
			sn = sn.trim().toUpperCase();
		}
		this.sn = sn;
	}
	public String getPn() {
		return pn;
	}
	public void setPn(String pn) {
		if( pn!=null ){
			pn = pn.trim().toUpperCase();
		}
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
		
		String snsStr = sn; 
		if( "84:79:73:1A:27:60".equals(snsStr)  || "3205I00436£º45:62:64:59:01".equals(snsStr) ||
				"20025000247£º53:85:46".equals(snsStr)  || "84:79:73:1A:27:60".equals( snsStr)  ){
			System.err.println( snsStr + " , " +kw +" , "+ pn +" ,  XXXXXXXXXXXXXX " );
		}
		
		return rd;
	}
	
}
