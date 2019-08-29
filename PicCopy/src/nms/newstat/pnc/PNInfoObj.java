package nms.newstat.pnc;

public class PNInfoObj {
	
	private String pn ;
	private double price;
	private String pnName;
	private String specification;
	private String kcType;
	private int sheetNum;
	
	public PNInfoObj(){}

	public PNInfoObj(String pn, double price, String pnName, String specification) {
		this.pn = pn;
		this.price = price;
		this.pnName = pnName;
		this.specification = specification;
	}

	public String getPn() {
		return pn;
	}

	public void setPn(String pn) {
		this.pn = pn;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getPnName() {
		return pnName;
	}

	public void setPnName(String pnName) {
		this.pnName = pnName;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getKcType() {
		return kcType;
	}

	public void setKcType(String kcType) {
		this.kcType = kcType.trim();
	}

	public int getSheetNum() {
		return sheetNum;
	}

	public void setSheetNum(int sheetNum) {
		this.sheetNum = sheetNum;
	}
	
	
}
