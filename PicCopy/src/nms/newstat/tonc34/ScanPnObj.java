package nms.newstat.tonc34;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import nms.RowData;
import nms.newstat.FPath;
import nms.newstat.tonc34.LoadU8NCKwMap.U8kwPn_NcKbKw;

public class ScanPnObj {
	
	private String pn ;
	
	private List<RowData> snList = new ArrayList<RowData>();
	
	private LinkedHashSet<String> kwSet = new LinkedHashSet<String>();
	
	public ScanPnObj(){
		
	}

	public ScanPnObj(String pn) {
		this.pn = pn.toUpperCase().trim();
	}

	public String getPn() {
		return pn;
	}

	public void setPn(String pn) {
		this.pn = pn.toUpperCase().trim();
	}

	public List<RowData> getSnList() {
		return snList;
	}

	public void setSnList(List<RowData> snList) {
		this.snList = snList;
	}
	
	
	public void addSN( RowData rd ){
		this.snList.add(rd);
		kwSet.add( rd.getPosNum().toUpperCase().trim() );
	}
	
	public int getSnCount(){
		return snList.size();
	}

	public LinkedHashSet<String> getKwSet() {
		return kwSet;
	}

	public void setKwSet(LinkedHashSet<String> kwSet) {
		this.kwSet = kwSet;
	}

	public U8kwPn_NcKbKw getKWForVSN() {
		
		U8kwPn_NcKbKw ret = null;
		
		if( "CBNNN-PON0088E".equals(pn) ){
			System.out.println();
		}
		
		for( String kwStr : kwSet  ){
			if( ret == null ){
				ret = LoadU8NCKwMap.getNCKWBykw_pn(kwStr, pn);
				continue;
			}
			
			U8kwPn_NcKbKw toCompare = LoadU8NCKwMap.getNCKWBykw_pn(kwStr, pn);
			String retU8Kb = ret.getU8kb();
			String tmpu8kb = toCompare.getU8kb();
			if( "30".equals(retU8Kb)  && !"30".equals(tmpu8kb)   ){
				continue;
			}else if( !"30".equals(retU8Kb)  && "30".equals(tmpu8kb)  ){
				ret = toCompare;
				continue;
			}
			
			String retU8kw = ret.getU8kw();
			String tmpU8kw = toCompare.getU8kw();
			int compareTo = retU8kw.compareTo(tmpU8kw);
			if( compareTo>=0  ){
				ret = toCompare;
			}
		}
		
		return ret;
	}
	
	
}
