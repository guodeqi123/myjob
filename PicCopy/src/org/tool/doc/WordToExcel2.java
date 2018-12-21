package org.tool.doc;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.extractor.WordExtractor;


public class WordToExcel2 {
	
	
	public static String titleSEP = ".";
	
	public static String optionSEP1 = "、";
	public static String optionSEP2 = ".";

	
	public static void main(String[] args) {
//		wordToExcel("D:\\a.doc");
		String aaa = "A. Whether melatonin supplements help "
				+ "people who have difficulty maintaining a 24-hour sleep cycle to establish such a pattern";
		 String[] split = aaa.split(optionSEP2);
		 int indexOf = aaa.indexOf("A"+optionSEP2);
		 int indexOf2 = aaa.indexOf( optionSEP2);
		System.out.println(    aaa.substring(indexOf2+1) );
	}
	
	public static void wordToExcel( String wordPath ){
		
		try {
			InputStream is = new FileInputStream(new File(wordPath));
			WordExtractor ex = new WordExtractor(is);
			String text2003 = ex.getText();
			System.out.println(text2003);
			
			// OPCPackage opcPackage = POIXMLDocument.openPackage("2007.docx");
			// POIXMLTextExtractor extractor = new
			// XWPFWordExtractor(opcPackage);
			// String text2007 = extractor.getText();
			// System.out.println(text2007);
			ByteArrayInputStream byteInputStream = new ByteArrayInputStream(text2003.getBytes());
			BufferedReader br = new BufferedReader( new InputStreamReader( byteInputStream ) );
			String line = br.readLine();
			
			List<QuestionBean> questions = new ArrayList<QuestionBean>();
			int counter = 1;
			while(  line !=null ) {
				String trimLine = line.trim();
				
				if( trimLine.startsWith(counter+titleSEP) ){
					QuestionBean bean = new QuestionBean( counter );
					int indexOf = trimLine.indexOf(titleSEP);
					if( indexOf!=-1 ){
						trimLine = trimLine.substring( indexOf +1 );
					}
					bean.setStem(trimLine);
					
					LinkedList<String> abcd = getOptionStarts();
					String detail = null;
					boolean isStem = true;
					String firstOption = abcd.get(0);
					while(  (detail=br.readLine())!=null&& !detail.trim().startsWith(  (counter+1)+titleSEP ) ){
						if( abcd.size()==0 ){
							break;
						}
						String detailTrim = detail.trim();
						if(  isStem && !detailTrim.startsWith(firstOption) ){
							bean.setStem(bean.getStem()+detailTrim);
						}
						if( detailTrim.startsWith(firstOption) ){
							isStem = false;
						}
						int size = abcd.size();
						while(  abcd.size()>0 ){
							String optionValue = "";
							String flag0 = abcd.get(0);
							String useSSSSSSep = optionSEP1;
							int indexOf1 = detailTrim.indexOf(flag0+optionSEP1);
							int in3 = detailTrim.indexOf(flag0+optionSEP2);
							if( in3 != -1 && indexOf1==-1  ){
								useSSSSSSep = optionSEP2;
								indexOf1 = in3;
							}
							
							if( indexOf1!=-1 ){
								abcd.removeFirst();
							}else{
								break;
							}
							if( abcd.size()==0 ){
								optionValue = detailTrim.substring(indexOf1);
							}else{
								String flag2 = abcd.get(0);
								int indexOf2 = detailTrim.indexOf(flag2+useSSSSSSep);
								if( indexOf2 != -1){
									optionValue = detailTrim.substring(indexOf1 , indexOf2);
								}else{
									optionValue = detailTrim.substring(indexOf1 );
								}
							}
							if( optionValue != null ){
								String trim = optionValue.trim();
								int aaa = trim.indexOf(useSSSSSSep);
								try {
									if(aaa != -1 ){
										trim = trim.substring(aaa+1).trim();
									}
								} catch (Exception e) {
								}
								bean.addOption(flag0, trim);
							}else{
								bean.addOption(flag0, "" );
							}
						}						
					}
					
					line = detail;
					questions.add(bean);
					counter++;
				}else{
					line = br.readLine();
				}
				
			}

			
//			createExcel(questions );
			createExcel2(questions );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final String A ="A";
	public static final String B ="B";
	public static final String C ="C";
	public static final String D ="D";
	public static final String EE ="E";
	static String[] title = { "序号"  , "题干" , A  ,  B , C , D  , "答案"};
	
	private static void createExcel2 (List<QuestionBean> questions) {
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("sheet1");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		
		for( int i=0; i<title.length  ; i++ ){
			HSSFCell cell = row.createCell((short) i);
			cell.setCellValue(title[i]);
			cell.setCellStyle(style);
		}
		
		for( int i=0; i<questions.size() ;i++  ) {
			QuestionBean bean = questions.get(i);
			HSSFRow row2 = sheet.createRow(i + 1);
			HSSFCell cell2 = null;
			
			cell2 = row2.createCell(0);
			cell2.setCellValue(bean.getSeqNum()+"");
		
			cell2 = row2.createCell(1);
			cell2.setCellValue(bean.getStem()+"");
			
			LinkedHashMap<String,String> options = bean.getOptions();
			cell2 = row2.createCell(2);
			cell2.setCellValue( options.get(A) );
			cell2 = row2.createCell(3);
			cell2.setCellValue( options.get(B) );
			cell2 = row2.createCell(4);
			cell2.setCellValue( options.get(C) );
			cell2 = row2.createCell(5);
			cell2.setCellValue( options.get(D) );
			cell2 = row2.createCell(6);
			cell2.setCellValue( A );
			
		}
		
		FileOutputStream fout = null;
		try {
			long currentTimeMillis = System.currentTimeMillis();
			File excelFile = new File("result/"+currentTimeMillis + ".xls");
			fout = new FileOutputStream(excelFile);
			wb.write(fout);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fout.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	

	private static void createExcel(List<QuestionBean> questions) {
		FileOutputStream fos = null;
		PrintWriter pw = null;
		try {
			long currentTimeMillis = System.currentTimeMillis();
			File excelFile = new File("result/"+currentTimeMillis + ".csv");
			excelFile.createNewFile();
			fos = new FileOutputStream(excelFile);
			pw = new PrintWriter(new OutputStreamWriter(fos));
			String sep = ",";
			String title = "序号" + sep+ "题干" + sep+ A + sep+ B + sep+ C+ sep+ D;
			pw.println(title);
			for( QuestionBean bean : questions ) {
				LinkedHashMap<String,String> options = bean.getOptions();
				String line = bean.getSeqNum() + sep+ bean.getStem() + sep
						+ options.get(A) + sep+ options.get(B) + sep+ options.get(C) + sep+  options.get(D);
				pw.println(line);
				System.out.println(  bean  );
			}
			pw.flush();
		} catch ( Exception e) {
			e.printStackTrace();
		}finally{
			pw.close();
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static LinkedList<String> getOptionStarts(){
		LinkedList<String> sss = new LinkedList<String>();
		sss.add(A);
		sss.add(B);
		sss.add(C);
		sss.add(D);
		return sss;
	}

}
