package org.tool.doc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.List;

public class ScoreArray {
	
	
	
	public static void getScroe(  String filePath , List<QuestionBean> beans ) {
		
		try {
			FileInputStream fis = new FileInputStream(new File(filePath));
			BufferedReader br = new BufferedReader( new InputStreamReader( fis ) );
			 
			int counter = 0 ;
			
			String line = null;
			while( (line=br.readLine())!=null ){
				if( line.startsWith("#") || line.trim().length()==0	){
					continue;
				}

				
				String[] split = line.split("=");
				String allScroe = split[0];  //0.5=5=S=C D D B D    
				int questionCount = Integer.parseInt( split[1] ); 
				String type = split[2];  
				String answersStr = split[3];   
				String[] answers = answersStr.split(" ");
				
				System.out.println(  allScroe + " ||  " + questionCount + " ||  "+ type + " ||"  + answersStr   );
				
				for( int jj= 0 ; jj<answers.length ; jj++){
					QuestionBean tmpBean = beans.get(counter++);
					String currentAnswer = answers[jj];
					tmpBean.setType(type);
					if( type.equals("S")  ||type.equals("C") ){
						if(  WordToExcel.A.equals(currentAnswer) ){
							tmpBean.setaScroe(allScroe);
						}
						if(  WordToExcel.B.equals(currentAnswer) ){
							tmpBean.setbScroe(allScroe);
						}
						if(  WordToExcel.C.equals(currentAnswer) ){
							tmpBean.setcScroe(allScroe);
						}
						if(  WordToExcel.D.equals(currentAnswer) ){
							tmpBean.setdScroe(allScroe);
						}
						if(  WordToExcel.EE.equals(currentAnswer) ){
							tmpBean.seteScroe(allScroe);
						}
						if(  WordToExcel.F.equals(currentAnswer) ){
							tmpBean.setfScroe(allScroe);
						}
					}else if(type.equals("M")){
						
						int length = currentAnswer.length();
						for( int kk=0; kk<length ;kk++ ){
							String charAt = currentAnswer.charAt(kk)+"";
							double toUseScroe = 0.1;
							if( kk==length-1 ){
								toUseScroe = Double.parseDouble(allScroe)-(length-1)*0.1;
							}
							String toUseScroeStr = toUseScroe + "";
							if(  WordToExcel.A.equals(charAt) ){
								tmpBean.setaScroe(toUseScroeStr);
							}
							if(  WordToExcel.B.equals(charAt) ){
								tmpBean.setbScroe(toUseScroeStr);
							}
							if(  WordToExcel.C.equals(charAt) ){
								tmpBean.setcScroe(toUseScroeStr);
							}
							if(  WordToExcel.D.equals(charAt) ){
								tmpBean.setdScroe(toUseScroeStr);
							}
							if(  WordToExcel.EE.equals(charAt) ){
								tmpBean.seteScroe(toUseScroeStr);
							}
							if(  WordToExcel.F.equals(charAt) ){
								tmpBean.setfScroe(toUseScroeStr);
							}
						}
						
					} 
				}
			}
			
		} catch ( Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static void main(String[] args) {
		String path = "conf/score1.properties";
		getScroe(path, null);
		
	}
	
}
