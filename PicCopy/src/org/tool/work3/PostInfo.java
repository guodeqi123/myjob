package org.tool.work3;

import java.util.HashSet;
import java.util.Set;

public class PostInfo {
	
	//����  ר��   ��ѧ����   ˶ʿ�о���   
	public final static int EDU_senior_high_school = 1;
	public final static int EDU_junior_college = 2;
	public final static int EDU_regular_college = 3;
	public final static int EDU_MASTER = 4;
	
	private String id = "";
	
	private String postName = "";
	
	private int minEducation = -1;
	
	private Set<DomainBean> professions = new HashSet<DomainBean>();
	
	private int minAge = -1;
	private int maxAge = -1;
	
	private String gender = null;
	
	

	public PostInfo(String id , String postName, int minEducation, int minAge, int maxAge, String gender) {
		this.id = id;
		this.postName = postName;
		this.minEducation = minEducation;
		this.professions = professions;
		this.minAge = minAge;
		this.maxAge = maxAge;
		this.gender = gender;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public int getMinEducation() {
		return minEducation;
	}

	public void setMinEducation(int minEducation) {
		this.minEducation = minEducation;
	}

	public Set<DomainBean> getProfessions() {
		return professions;
	}

	public void setProfessions(Set<DomainBean> professions) {
		this.professions = professions;
	}
	
	public PostInfo addPro(DomainBean bean) {
		this.professions.add(bean);
		return this;
	}

	public int getMinAge() {
		return minAge;
	}

	public void setMinAge(int minAge) {
		this.minAge = minAge;
	}

	public int getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 
	*	��λ ��>Ͷ��ְλ
	*	ѧ�� -> ���ѧ��       
	*	רҵ -> ��ѧרҵ
	*	���� ->��������
	*	�Ա� ��->�Ա�
	 */
	public boolean isMatch(PersonInfo person){
		int age = person.getAge();
		if( age<minAge || age>maxAge){
			person.setMsg("���䲻��");
			return false;
		}
		
		int edu = person.getEdu();
		if( edu<minEducation ){
			person.setMsg("ѧ������ ");
			return false;
		}
		
		String pGender = person.getGender();
		if( gender!=null && !pGender.equals(gender) ){
			person.setMsg("�Ա𲻷�");
			return false;
		}
		
		String profession = person.getProfession();
		String tmpIdentity = person.getIdentity();
		String tmpSchool = person.getSchool();
		
		if( "8".equals(id)  ){
			if( tmpIdentity.contains("ʿ��") || tmpIdentity.contains( "����")  || tmpIdentity.contains( "����")   ){
				return true;
			}
			if( profession.contains("ʿ��") || profession.contains( "����")  || profession.contains( "����")   ){
				return true;
			}
			if(tmpSchool.contains("����")){
				return true;
			}
		}
		
		for( DomainBean bean :  professions ){
			boolean match = bean.isMatch(person.getProfession());
			if( match ){
				return true;
			}
		}
		
		Set<String> set = ProfessionInfo.mapping.get(profession);
		if(set==null || !set.contains(id) ){
			person.setMsg("רҵ����");
			return false;
		}
		
		return true;
	}
	
	
	
}
