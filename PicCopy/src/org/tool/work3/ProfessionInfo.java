package org.tool.work3;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProfessionInfo {
	
	// ��ѧרҵ  - >  ����ƥ���λID
	public static Map<String, Set<String>> mapping = new HashMap<String, Set<String>>();
	
	
	public static Map<String, DomainBean > domainBeanMapping = new HashMap<String, DomainBean>();
	
	public static Map<String, PostInfo>  postMapping = new HashMap<String, PostInfo>();
	
	public static  void loadMappingInfo() throws Exception{
		String fName = "ProMapping.txt";
		InputStream resourceAsStream = ProfessionInfo.class.getResourceAsStream(fName);
		BufferedReader br = new BufferedReader( new InputStreamReader(resourceAsStream));
		
		int counter = 0 ;
		String line = null;
		while(  (line=br.readLine())!=null ){
			counter++;
//			System.out.println( line );
			
			String[] split = line.split("=");
			if( split.length>1 ){
				String pro = split[0];
				
				String[] posts = split[1].split(",");
				Set<String> hashSet = new HashSet<String>();
				for( String str :  posts){
					hashSet.add(str.trim());
				}
				mapping.put(pro, hashSet);
			}
			
		}
		
		System.out.println(  mapping.size() +  " .......... over...................." + counter );
		
		
		
//		1.���ý���רҵ����Ҫ������۾��á�����ѧ�����ʽ��� ���ڹ��̡����򾭼�ѧ�����񾭼�ѧ����ҵ����ѧ�����ξ���ѧ ��������ѧ��
//		�˿���Դ��������ѧ�����ڹ�����ʵ��Ͷ��ѧ    ��������ѧ������ó��
//
//		2.���רҵ����Ҫ�������������ơ���ơ����˶ʿ ע����ʦ�����ʻ�ơ�
//
//		3.����רҵ����Ҫ������ѧ�����ɡ�������ѧ�����÷����񷨡����̷���֪ʶ��Ȩ��������˶ʿ��
//
//		4.�����רҵ����Ҫ�������������������Ӳ���������Ӧ�á����ӿ�ѧ�뼼����ͨ�ż�������Ϣ��ȫ���������ѧ�뼼����ͨ�Ź��̡��������Ϣ����
//		��Ϣ������ѧ�����������������Ϣ���̡����繤�̡�ģʽʶ��������ϵͳ����������ݿ� ��������ơ�����������Ϣϵͳ����Ϣ��������Ϣϵͳ
//
//		5.����רҵ����Ҫ������������������Դ�������������Ͷ�����ᱣ�ϵ�
//		6.Ӣ��רҵ����Ҫ����Ӣ��������ѧ��Ӣ�����רҵ(Ӣ����롢Ӣ�����)������Ӣ���
//		7.����:��ѧ�ࡢ�����ࡢ��е�ࡢ�����ࡢ�������
//		8.����:����ѧ����������(��)ѧ���й��ŵ�����ѧ���й��ֵ�����ѧ�����ģ��й���ѧ���ŵ����ף�����ѧ
				
		String[] strs1 = new String[]{ "����" , "����" , "Ͷ��"  , "����" };
		DomainBean bean1 = new DomainBean("���ý���רҵ", strs1 , new String[]{"����ѧ" ,  "����ó��" } );
		
		String[] strs2 = new String[]{ "����" , "���" , "���"  };
		DomainBean bean2 = new DomainBean("���רҵ", strs2  );
		
		String[] strs3 = new String[]{ "��ѧ" , "����" , "���÷�"  , "���÷�" , "��" , "���̷�" , "֪ʶ��Ȩ��"   };
		DomainBean bean3 = new DomainBean("����רҵ", strs3  );
		
		String[] strs4 = new String[]{ "���" ,"Ӳ��","�����","���ӿ�ѧ�뼼��","ͨ��","��Ϣ��ȫ" 
				,  "��Ϣ������ѧ" , "������Ϣ����" ,"����","ģʽʶ��������ϵͳ"  , "���ݿ�"  };
		DomainBean bean4 = new DomainBean("�����רҵ", strs4  , new String[]{"������Ϣϵͳ" , "��Ϣ��������Ϣϵͳ" });
		
		String[] strs5 = new String[]{ "��������" ,"������Դ����"  , "��������"  , "��ᱣ��"   };
		DomainBean bean5 = new DomainBean("����רҵ", strs5  );
		
		String[] strs6 = new String[]{ "Ӣ��"   , "����"  };
		DomainBean bean6 = new DomainBean("Ӣ��רҵ", strs6  );
		
		String[] strs7 = new String[]{ "��ѧ"   , "����"   , "��е" , "����" , "����" ,"����"};
		DomainBean bean7 = new DomainBean("����", strs7  );
		
		String[] strs8 = new String[]{ "����"   , "�ŵ�����"   , "������ѧ" , "����" , "����" };
		DomainBean bean8 = new DomainBean("����", strs8  );
		

		PostInfo post1 = new PostInfo("1" , "���Ÿ�", PostInfo.EDU_junior_college, 18, 32, null);//���ý��ڡ���ơ����ɡ�����������������רҵ
		post1.addPro(bean1).addPro(bean2).addPro(bean3).addPro(bean4).addPro(bean5).addPro(bean6);
		PostInfo post2 = new PostInfo("2" ,"֧������", PostInfo.EDU_junior_college, 18, 32, null);//���ý��ڡ���ơ�����������רҵ
		post2.addPro(bean1).addPro(bean2).addPro(bean4).addPro(bean6);
		PostInfo post3 = new PostInfo("3" ,"�������", PostInfo.EDU_junior_college, 18, 32, null);//���ý��ڡ���ơ�����������רҵ
		post3.addPro(bean1).addPro(bean2).addPro(bean4).addPro(bean6);
		PostInfo post4 = new PostInfo("4" ,"���ҷ���", PostInfo.EDU_junior_college, 18, 32, PersonInfo.GENDER_MALE);//���ý��ڡ���ơ���������������רҵ
		post4.addPro(bean1).addPro(bean2).addPro(bean4).addPro(bean7).addPro(bean6);
		PostInfo post5 = new PostInfo("5" ,"�ĵ��ۺ�", PostInfo.EDU_junior_college, 18, 32, null);//���ý��ڡ����ء�����������רҵ
		post5.addPro(bean1).addPro(bean8).addPro(bean4).addPro(bean6);
		PostInfo post6 = new PostInfo("6" ,"��ƺ���", PostInfo.EDU_junior_college, 18, 32, null);//���ý��ڡ���������רҵ
		post6.addPro(bean1).addPro(bean2).addPro(bean6);
		PostInfo post7 = new PostInfo("7" ,"�Ƽ���", PostInfo.EDU_junior_college, 18, 32, null);//����������רҵ
		post7.addPro(bean4);
		//����תҵ�ɲ�������ʿ���͹���ԺУ��ҵ��
		PostInfo post8 = new PostInfo("8" ,"�ؿ�Ѻ��", PostInfo.EDU_senior_high_school, 20, 32, PersonInfo.GENDER_MALE);
		
		domainBeanMapping.put(bean1.getName(), bean1);
		domainBeanMapping.put(bean2.getName(), bean2);
		domainBeanMapping.put(bean3.getName(), bean3);
		domainBeanMapping.put(bean4.getName(), bean4);
		domainBeanMapping.put(bean5.getName(), bean5);
		domainBeanMapping.put(bean6.getName(), bean6);
		domainBeanMapping.put(bean7.getName(), bean7);
		domainBeanMapping.put(bean8.getName(), bean8);
		

		//���ҷ���   ��ƺ���   �Ƽ�   ֧������   ����   �������   �ĵ��ۺ�   �ؿ�Ѻ��
		postMapping.put("֧������", post2);
		postMapping.put("�ĵ��ۺ�", post5);
		postMapping.put("���ҷ���", post4);
		postMapping.put("��ƺ���", post6);
		postMapping.put("�������", post3);
		postMapping.put("����", post1);
		postMapping.put("���Ÿ�", post1);
		postMapping.put("�ؿ�Ѻ��", post8);
		postMapping.put("�Ƽ���", post7);
		postMapping.put("�Ƽ�", post7);
	}
	
	
	
	
	public static void main(String[] args) throws Exception {
		
		loadMappingInfo();
		
	}
}
