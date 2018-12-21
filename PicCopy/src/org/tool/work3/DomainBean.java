package org.tool.work3;

import java.util.ArrayList;
import java.util.List;

public class DomainBean {
	
	private String name;
	
	private List<String> keywords = new ArrayList<String>();
	private List<String> keywordsNot = new ArrayList<String>();
	
	
	public DomainBean(){}
	
	public DomainBean(String name , String[] keys ){
		this.name=name;
		
		for( String ttt : keys){
			keywords.add(ttt);
		}

	}
	
	public DomainBean(String name , String[] keys ,String[] keys2 ){
		this.name=name;
		
		for( String ttt : keys){
			keywords.add(ttt);
		}
		
		for( String ttt : keys2){
			keywordsNot.add(ttt);
		}
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public boolean isMatch(String str){
	
		for(String toMatch: keywordsNot ){
			if(  str.contains(toMatch) ){
				return false;
			}
		}
		
		for(String toMatch: keywords ){
			if(  str.contains(toMatch) ){
				return true;
			}
		}
		
		return false;
	}
	
}
