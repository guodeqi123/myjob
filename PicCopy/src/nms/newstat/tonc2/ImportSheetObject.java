package nms.newstat.tonc2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nms.newstat.tonc.ToNCExcel;

public class ImportSheetObject implements Serializable{
	
	private List<ImportTitleObj> titleList = new ArrayList<ImportTitleObj>();
	private List<ImportRowObj> rowList = new ArrayList<ImportRowObj>();
	
	public ImportSheetObject(){
		
	}
	
	public void addTtile(ImportTitleObj tt ){
		titleList.add( tt );
	}
	
	public void addRow(ImportRowObj rr ){
		rowList.add( rr );
	}
	
	public List<ImportTitleObj> getTitleList() {
		return titleList;
	}
	public void setTitleList(List<ImportTitleObj> titleList) {
		this.titleList = titleList;
	}
	public List<ImportRowObj> getRowList() {
		return rowList;
	}
	public void setRowList(List<ImportRowObj> rowList) {
		this.rowList = rowList;
	}

	public void writeToFile(String fname) {
		
		ToNCExcel.createExcel(this , fname);
		
	}
	
	
	
}
