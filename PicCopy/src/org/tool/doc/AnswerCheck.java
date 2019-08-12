package org.tool.doc;

import java.io.InputStream;
import java.util.Properties;

public class AnswerCheck {
	
	public static String answerPath = "answer.properties";
	public static String answerPath2 = "answer2.properties";
	public static int[] nums = new int[]{
		36, 33 , 12, 24 ,  6  , 
		11 , 28 , 15 , 19 , 22  , 
		42 ,  23  , 34 , 9 , 25 , 
		30, 17 , 32 , 10, 31 , 
		1 , 41 , 2, 7 , 43 , 
		13, 4 , 40, 5, 38 , 
		20, 29 , 8, 37 , 16 , 
		39, 3 , 26, 14, 35 , 
		21, 18 , 27,  
	};
	public static int[] nums2 = new int[]{
		17, 12 , 20, 1 , 19  , 
		2 , 3 , 4 , 5 , 11 , 
		18, 10 , 13, 15 , 14 , 
		6 , 9 , 16, 7, 8 , 
	};
	
	public static void main(String[] args) {
		
		int [] toUse = nums2;
		InputStream ras = WordToExcel.class.getResourceAsStream(answerPath2);
		
		
		Properties pro = new Properties();
		try {
			pro.load(ras);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String answer = "";
		
		for( int i=0; i<toUse.length  ;i++ ){
			if( i!=0 && i%5==0 ){
				answer = answer+"\r\n";
			}
			answer = answer + " , "+pro.getProperty( toUse[i] +"" );
		}
		System.out.println(  answer  );
		
		
	}
}
