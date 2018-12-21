package org.tool.doc;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class QuestionBean {

	private int seqNum;
	
	private String stem;
	
	private LinkedHashMap<String ,String> options = new LinkedHashMap();
	
	private String aSelect = "";
	private String bSelect = "";
	private String cSelect = "";
	private String dSelect = "";
	private String eSelect = "";
	private String fSelect = "";
	
	private String aScroe = "0";
	private String bScroe = "0";
	private String cScroe = "0";
	private String dScroe = "0";
	private String eScroe = "0";
	private String fScroe = "0";

	private String difficulty = "";
	
	private String remark = "";
	
	private String bigTitle;
	private String type;
	
	public QuestionBean(int seqNum) {
		super();
		this.seqNum = seqNum;
	}
	
	
	public int getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}

	public String getStem() {
		return stem;
	}

	public void setStem(String stem) {
		this.stem = stem;
	}
	
	public String getDifficulty() {
		return difficulty;
	}


	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}


	public LinkedHashMap<String, String> getOptions() {
		return options;
	}

	public void setOptions(LinkedHashMap<String, String> options) {
		this.options = options;
	}
	
	
	public void addOption(  String k , String v ){
		options.put(k, v);
	}

	public String getaScroe() {
		return aScroe;
	}

	public void setaScroe(String aScroe) {
		this.aScroe = aScroe;
	}

	public String getbScroe() {
		return bScroe;
	}

	public void setbScroe(String bScroe) {
		this.bScroe = bScroe;
	}

	public String getcScroe() {
		return cScroe;
	}

	public void setcScroe(String cScroe) {
		this.cScroe = cScroe;
	}

	public String getdScroe() {
		return dScroe;
	}

	public void setdScroe(String dScroe) {
		this.dScroe = dScroe;
	}

	public String geteScroe() {
		return eScroe;
	}


	public void seteScroe(String eScroe) {
		this.eScroe = eScroe;
	}

	public String getaSelect() {
		return aSelect;
	}


	public void setaSelect(String aSelect) {
		this.aSelect = aSelect;
	}


	public String getbSelect() {
		return bSelect;
	}


	public void setbSelect(String bSelect) {
		this.bSelect = bSelect;
	}


	public String getcSelect() {
		return cSelect;
	}


	public void setcSelect(String cSelect) {
		this.cSelect = cSelect;
	}


	public String getdSelect() {
		return dSelect;
	}


	public void setdSelect(String dSelect) {
		this.dSelect = dSelect;
	}


	public String geteSelect() {
		return eSelect;
	}


	public void seteSelect(String eSelect) {
		this.eSelect = eSelect;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getfSelect() {
		return fSelect;
	}


	public void setfSelect(String fSelect) {
		this.fSelect = fSelect;
	}


	public String getfScroe() {
		return fScroe;
	}


	public void setfScroe(String fScroe) {
		this.fScroe = fScroe;
	}

	public String getBigTitle() {
		return bigTitle;
	}


	public void setBigTitle(String bigTitle) {
		this.bigTitle = bigTitle;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Set<Entry<String,String>> entrySet = options.entrySet();
		for(  Entry<String,String> en : entrySet ){
			String key = en.getKey();
			String value = en.getValue();
			sb.append( key + ":::::" + value +"	");
		}
		
		return "QuestionBean [BigTitle= "+bigTitle+" seqNum=" + seqNum + ", stem=" + stem + "]" + sb.toString();
	}
	
	
	
}
