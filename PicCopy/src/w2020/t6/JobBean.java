package w2020.t6;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class JobBean {
	
	public String name;
	
	public int edu;    //最低学历	    1 高中  2专科  3大学本科   4硕士研究生
	public List<String> majors = new ArrayList<String>();
	
	public int minBirth = - 1;
	public int maxBirth = -1;
	
	public String gender;

	public JobBean(String name) {
		super();
		this.name = name;
	}
	
	public JobBean addMajor(String mm ){
		majors.add(mm);
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getEdu() {
		return edu;
	}

	public void setEdu(int edu) {
		this.edu = edu;
	}

	public List<String> getMajors() {
		return majors;
	}

	public void setMajors(List<String> majors) {
		this.majors = majors;
	}

	public int getMinBirth() {
		return minBirth;
	}

	public void setMinBirth(int minBirth) {
		this.minBirth = minBirth;
	}

	public int getMaxBirth() {
		return maxBirth;
	}

	public void setMaxBirth(int maxBirth) {
		this.maxBirth = maxBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "JobBean [name=" + name + ", edu=" + edu + ", majors=" + majors
				+ ", minBirth=" + minBirth + ", maxBirth=" + maxBirth
				+ ", gender=" + gender + "]";
	}
	
	
	
}
