package nms;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nms.seqparser.AccessDBParser;
import nms.seqparser.QrCodeParser;

public class RowData {
	
	private String posNum;
	
	private String materialNum;
	
	private String snsStr;
	
	private String count ;
	
	private List<String> snList;
	private Set<String> snListSet ;
	
	private String msg = "";
	
	private String fName ;
	private String sheetname;
	
	
	private int statcount=0;  //���������̿�ʹ��
	
	public RowData(){}
	
	
	public RowData(String posNum, String materialNum, String snsStr ,String count ) {
		super();
		this.posNum = posNum;
		this.materialNum = materialNum;
		this.snsStr = snsStr;
		this.count = count;
		
	}

	public RowData(String cUseKW, String cUsePN, String snStr) {
		this.posNum = cUseKW;
		this.materialNum = cUsePN;
		this.snsStr = snStr;
	}


//	@Override
//	public boolean equals(Object obj) {
//		 
//		if(  obj instanceof RowData  ){
//			RowData toCompare = (RowData) obj;
//			
//			String sheetname2 = toCompare.getSheetname();
//			String materialNum2 = toCompare.getMaterialNum();
//			String posNum2 = toCompare.getPosNum();
//			String snsStr2 = toCompare.getSnsStr();
//			if(     posNum2.equals(posNum) &&  snsStr2.equals(snsStr2)  ){
//				return true;
//			}
//		}
//		return false;
//	}
	
	public boolean parseSNsToList(   ){
	
		try {
			snList = QrCodeParser.parse(snsStr);
			snListSet = new HashSet<String>();
			snListSet.addAll(snList);
			if(  snList.size() != snListSet.size()  ){
				throw new Exception("���к����ظ�::" + snsStr );
			}
			
			String mNum = materialNum==null?"":materialNum.trim();
			if( mNum.length()<2 ){
				for(String sn :  snList){
					String mStrBySN = AccessDBParser.getMStrBySN(sn);
					if(  mStrBySN !=null ){
						mNum = mStrBySN;
						break;
					}
				}
				if( mNum.length()<2 ){
					throw new Exception("MMMM�޷��ҵ����ϱ���"  );
				}
			}
			
			if( snList != null && snList.size()>0 ){
				
				int parseInt = (int) Double.parseDouble(count);
				if(  parseInt == snList.size() ){
					return true;
				}else{
					throw new Exception("���к�������ƥ�䣬���:" + count +" , ��������: "+snList.size() );
				}
			}
		} catch (Exception e) {
			this.msg = e.getMessage();
			e.printStackTrace();
			
		}
		return false;
		
	}

	public String getPosNum() {
		return posNum;
	}

	public void setPosNum(String posNum) {
		this.posNum = posNum;
	}

	public String getMaterialNum() {
		return materialNum;
	}

	public void setMaterialNum(String materialNum) {
		this.materialNum = materialNum;
	}


	public String getSnsStr() {
		return snsStr;
	}


	public void setSnsStr(String snsStr) {
		this.snsStr = snsStr;
	}


	public List<String> getSnList() {
		return snList;
	}


	public void setSnList(List<String> snList) {
		this.snList = snList;
	}


	public String getCount() {
		return count;
	}


	public void setCount(String count) {
		this.count = count;
	}


	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}


	public Set<String> getSnListSet() {
		return snListSet;
	}


	public void setSnListSet(Set<String> snListSet) {
		this.snListSet = snListSet;
	}


	public int getStatcount() {
		return statcount;
	}


	public void setStatcount(int statcount) {
		this.statcount = statcount;
	}


	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}


	public String getSheetname() {
		return sheetname;
	}


	public void setSheetname(String sheetname) {
		this.sheetname = sheetname;
	}

	
	
}
