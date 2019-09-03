package nms.newstat.tonc34;

import java.util.HashMap;
import java.util.Map;

import nms.newstat.Convertor2;
import nms.newstat.FPath;
import nms.newstat.tonc2.LoadNCKWInfo;
import nms.newstat.tonc2.NCKwObj;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class LoadU8NCKwMap {
	
	
	private static Map<String,  U8kwPn_NcKbKw > scankw_Pn2Obj = new HashMap<String, U8kwPn_NcKbKw>();
	
	public static final String kwpn2NCPath = "D:/ForBdcom/0stat1/Data0903/KWMAP.xlsx";
	
	public static void load(){
		
		int sheeNum = 0;
		int startrow = 1;

		Workbook wb = FPath.getWB(kwpn2NCPath);
		Sheet sheetAt = wb.getSheetAt(sheeNum);
		int lastRowNum = sheetAt.getLastRowNum();
		Row oneRow = null;
		for (int i = startrow; i <= lastRowNum; i++) {
			oneRow = sheetAt.getRow(i);
			if (oneRow == null) {
				continue;
			}
			Cell pnCell = oneRow.getCell(0);
			Cell skwCell = oneRow.getCell(1);
			Cell nckwCell = oneRow.getCell(2);
			Cell nckwNameCell = oneRow.getCell(3);
			Cell nckbCell = oneRow.getCell(4);
			Cell u8kbCell = oneRow.getCell(5);

			String pnStr = Convertor2.getCellValue(pnCell);
			String skwStr = Convertor2.getCellValue(skwCell);
			String nckwStr = Convertor2.getCellValue(nckwCell);
			String nckwNameStr = Convertor2.getCellValue(nckwNameCell);
			String nckbStr = Convertor2.getCellValue(nckbCell);
			String u8kbStr = Convertor2.getCellValue(u8kbCell);
		
			U8kwPn_NcKbKw u8kwPn_NcKbKw = new U8kwPn_NcKbKw( pnStr , skwStr , u8kbStr, nckwNameStr,nckbStr, nckwStr , nckwNameStr);
			
			String key = skwStr +FPath.sep1+ pnStr;
			if(  scankw_Pn2Obj.containsKey(key) ){
//				System.out.println( "########"+ key );
			}
			scankw_Pn2Obj.put(key, u8kwPn_NcKbKw);
		
		}
		System.out.println(  "Load16KbPNInfo.load()  PNTO KW 总个数：：" +   scankw_Pn2Obj.size()  );
		
	}
	
	public static U8kwPn_NcKbKw getNCKWBykw_pn( String u8kw , String pn  ){
		
		String key = u8kw+FPath.sep1 + pn;
		U8kwPn_NcKbKw u8kwPn_NcKbKw = LoadU8NCKwMap.scankw_Pn2Obj.get(key);
		if( u8kwPn_NcKbKw == null ){
			System.out.println( "!!!!!!!!!!!!!!!!!!!!!!!!!!无法找到NC库位：：：" +  key );
		}
//		u8kwPn_NcKbKw = new U8kwPn_NcKbKw("pn", "scankw", "30", u8kw, "10", "nckw", "nckwName");
		
		return u8kwPn_NcKbKw;
	}
	
	public static void main(String[] args) {
		load();
	}
	
	public static class U8kwPn_NcKbKw {
		
		private String pn;
		private String scankw; 
		
		private String u8kb;
		private String u8kw;
		
		private String ncKb;
		private String nckw;
		private String nckwName;


		public U8kwPn_NcKbKw(String pn, String scankw, String u8kb, String u8kw, String ncKb, String nckw, String nckwName) {
			this.pn = pn;
			this.scankw = scankw;
			this.u8kb = u8kb;
			this.u8kw = u8kw;
			this.ncKb = ncKb;
			this.nckw = nckw;
			this.nckwName = nckwName;
		}
		public String getU8kw() {
			return u8kw;
		}
		public void setU8kw(String u8kw) {
			this.u8kw = u8kw;
		}
		public String getPn() {
			return pn;
		}
		public void setPn(String pn) {
			this.pn = pn;
		}
		public String getNcKb() {
			return ncKb;
		}
		public void setNcKb(String ncKb) {
			this.ncKb = ncKb;
		}
		public String getNckw() {
			return nckw;
		}
		public void setNckw(String nckw) {
			this.nckw = nckw;
		}
		public String getU8kb() {
			return u8kb;
		}
		public void setU8kb(String u8kb) {
			this.u8kb = u8kb;
		}
		public String getNckwName() {
			return nckwName;
		}
		public void setNckwName(String nckwName) {
			this.nckwName = nckwName;
		}
		public String getScankw() {
			return scankw;
		}
		public void setScankw(String scankw) {
			this.scankw = scankw;
		}
		
		
	}
	
}
