package nms.stat;

public class SheetObj {

	public int sheetNum;
	
	public int startRow;
	
	public int pnCol;
	public int countCol;
	
	public SheetObj(String tmpa) {
		
		String[] split = tmpa.split(",");
		sheetNum = Integer.parseInt( split[0] );
		startRow = Integer.parseInt( split[1] );
		pnCol = Integer.parseInt( split[2] );
		countCol = Integer.parseInt( split[3] );
		
	}

	public int getSheetNum() {
		return sheetNum;
	}

	public void setSheetNum(int sheetNum) {
		this.sheetNum = sheetNum;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getPnCol() {
		return pnCol;
	}

	public void setPnCol(int pnCol) {
		this.pnCol = pnCol;
	}

	public int getCountCol() {
		return countCol;
	}

	public void setCountCol(int countCol) {
		this.countCol = countCol;
	}

}
