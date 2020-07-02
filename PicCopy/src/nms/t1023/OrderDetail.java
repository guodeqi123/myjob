package nms.t1023;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class OrderDetail {
	
	private String oNum;
	private String rNum;
	private String pn;
	private double count ;
	
	private double doneCount ;
	
	private Map<String, SubPNBean> subMap = new HashMap<String, OrderDetail.SubPNBean>(); 
	
	private Map<String,SubPNBean > repeatORSubPN = new HashMap<String, OrderDetail.SubPNBean>(); 
	
	public OrderDetail(String oNum, String rNum, String pn, double count) {
		this.oNum = oNum;
		this.rNum = rNum;
		this.pn = pn;
		this.count = count;
	}

	public String getoNum() {
		return oNum;
	}

	public void setoNum(String oNum) {
		this.oNum = oNum;
	}

	public String getrNum() {
		return rNum;
	}

	public void setrNum(String rNum) {
		this.rNum = rNum;
	}

	public String getPn() {
		return pn;
	}

	public void setPn(String pn) {
		this.pn = pn;
	}

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}

	public Map<String, SubPNBean> getSubMap() {
		return subMap;
	}

	public double getDoneCount() {
		return doneCount;
	}

	public void setDoneCount(double doneCount) {
		this.doneCount = doneCount;
	}

	public void setSubMap(Map<String, SubPNBean> subMap) {
		this.subMap = subMap;
	}
	
	public void addSub(SubPNBean bb){
		String subPn = bb.getSubPn();
		bb.setParent(this);
		
		if(  subMap.containsKey(subPn) ){
			SubPNBean subPNBean = subMap.get(subPn);
			double needCount = bb.getNeedCount();
			double haveCount = bb.getHaveCount();
			
			subPNBean.setNeedCount(subPNBean.getNeedCount() +needCount );
			subPNBean.setHaveCount(subPNBean.getHaveCount() +haveCount );
			
			bb.setRemark("工单号行号子件编码 重复");
			repeatORSubPN.put( bb.getLineNo() + "",bb );
		}else{
			subMap.put(subPn, bb);
		}
	}
	
	public Map<String, SubPNBean> getRepeatORSubPN() {
		return repeatORSubPN;
	}

	public void setRepeatORSubPN(Map<String, SubPNBean> repeatORSubPN) {
		this.repeatORSubPN = repeatORSubPN;
	}

	public String getKey( ){
		return oNum +"-"+  rNum;
	} 
	

	
	static class SubPNBean{
		
		private int lineNo;
		private String subPn;
		private double needCount;
		private double haveCount;
		
		//need calc
		private int useCount;
		private int unusedCount;
		private String remark = "";
		
		private OrderDetail parent ;
		
		public SubPNBean( int lineNo , String subPn, double needCount, double haveCount) {
			this.lineNo =lineNo;
			this.subPn = subPn;
			this.needCount = needCount;
			this.haveCount = haveCount;
		}
		
		public int getLineNo() {
			return lineNo;
		}

		public void setLineNo(int lineNo) {
			this.lineNo = lineNo;
		}

		public String getSubPn() {
			return subPn;
		}
		public void setSubPn(String subPn) {
			this.subPn = subPn;
		}
		public double getNeedCount() {
			return needCount;
		}
		public void setNeedCount(double needCount) {
			this.needCount = needCount;
		}
		public double getHaveCount() {
			return haveCount;
		}
		public void setHaveCount(double haveCount) {
			this.haveCount = haveCount;
		}

		public int getUseCount() {
			return useCount;
		}

		public void setUseCount(int useCount) {
			this.useCount = useCount;
		}

		public int getUnusedCount() {
			return unusedCount;
		}

		public void setUnusedCount(int unusedCount) {
			this.unusedCount = unusedCount;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public OrderDetail getParent() {
			return parent;
		}

		public void setParent(OrderDetail parent) {
			this.parent = parent;
		}
		
		
	}
	
}
