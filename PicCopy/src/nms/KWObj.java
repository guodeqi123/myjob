package nms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import nms.newstat.GatherAll;
import nms.seqparser.AccessDBParser;
import nms.seqparser.QrCodeParser;

public class KWObj {
		
	private String kwNum;
	
	private List<RowData> rowSnList  = new ArrayList<RowData>();
	
	private String msg = "";
	
	private int count = 0;
	
	private List<String[]> errPns = new ArrayList<String[]>();//kw  sn ,  inputpn , dbpn 
	
	public static int pnnullCounter = 0;
	
	private int sheetRowNum = 0;
	
	public KWObj( ){}
	
	
	public KWObj( String kwNum ){
		this.kwNum = kwNum;
	}

	public String getKwNum() {
		return kwNum;
	}

	public void setKwNum(String kwNum) {
		this.kwNum = kwNum.toUpperCase().trim();
	}

	public List<RowData> getRowSnList() {
		return rowSnList;
	}

	public void setRowSnList(List<RowData> rowSnList) {
		this.rowSnList = rowSnList;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public void addCount ( int a ){
		this.count = this.count +a;
	}
	
	public void addRow( RowData srcRow ){
		//解析转化
		String pn = srcRow.getMaterialNum();
		String snsStr = srcRow.getSnsStr();
		
		List<String> parseSns = QrCodeParser.parse(snsStr);
		for(  String tmpSN : parseSns ){
			RowData rowData = new RowData(kwNum, pn, tmpSN);
			rowData.setfName(srcRow.getfName());
			rowSnList.add(rowData);
		}
		
	}

	public void checkSelf() throws Exception {
		
		List<String> repetSn = new ArrayList<String>();
		
		List<String> snStrList = new ArrayList<String>();
		Set<String> snStrSet = new HashSet<String>();
		int size = rowSnList.size();
		for(int i=0; i<size ;i++){
			RowData rowData = rowSnList.get(i);
			String tmpSn = rowData.getSnsStr();
			snStrList.add(tmpSn);
			
			if(  snStrSet.contains(tmpSn)  ){
				repetSn.add(tmpSn);
			}
			snStrSet.add(tmpSn);
			
			String tmpPn = rowData.getMaterialNum();
			if(  StringUtils.isEmpty(tmpPn)  ){
				msg = "KWObj:::物料编码为空, SN:"+ tmpSn  ;
				throw new Exception(msg);
			}
			String tmpdbPn = AccessDBParser.getMStrBySN(tmpSn);
			if( tmpdbPn!=null && !tmpdbPn.equals(tmpPn)   ){
				String[] aaa = new String[]{  kwNum, tmpSn , tmpPn , tmpdbPn  };
				errPns.add(aaa);
			}
			if( tmpdbPn ==null  ){
				pnnullCounter++;
			}
			
		}
		int listSize = snStrList.size();
		int setSize = snStrSet.size();
		if(  listSize!= setSize  )  {
			String rps = "";
			for(String str:  repetSn ){
				rps += str+  " , ";
			}
			msg = "存在重复序列号,重复个数:"+ (listSize-setSize)  +" ,  序列号:"+rps;
			throw new RuntimeException(msg);
		}
		
		if(  count != size  ){
			msg = "库位个数不准确，实际总个数:"+ (listSize)  + " , 人工输入个数之和:" + count ;
			throw new RuntimeException(msg);
		}
		
	}

	public List<String[]> getErrPns() {
		return errPns;
	}

	public void setErrPns(List<String[]> errPns) {
		this.errPns = errPns;
	}

	public int getSheetRowNum() {
		return sheetRowNum;
	}

	public void setSheetRowNum(int sheetRowNum) {
		this.sheetRowNum = sheetRowNum;
	}
	
	
	
}
