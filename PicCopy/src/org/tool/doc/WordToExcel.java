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
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.xmlbeans.XmlException;

/**
 * 
 * 存在大标题
 * @author GDQ
 *
 */
public class WordToExcel {
	
	
	public static String bigTitleSEP = "、";
	
	public static String titleSEP = ".";
	
	public static String optionSEP1 = "、";
	public static String optionSEP2 = ".";
	public static String optionSEP3 = "．";
	public static LinkedHashMap<String, Integer> MAINSEQ = new LinkedHashMap<String, Integer>();
	
	public static String filePath = "D:/GG/src.doc";
	public static String answerPath = "answer.properties";
	
	public static void main(String[] args) throws Exception {
//		String path1 = "D:/GG/src1.docx";
//		String path2 = "D:/GG/src2.doc";
//		String[] aa1 = new String[]{   path1, "scroe1.properties"};
//		String[] aa2 = new String[]{   path2, "scroe2.properties"};
//		loadTxtFromDoc(filePath );
		

		wordToExcel( filePath ,"");
		
	}
	
	public static String loadTxtFromDoc(String wordPath  ) throws IOException, XmlException, OpenXML4JException{
		String textAll = "";
		if( wordPath.endsWith(".doc") ){
			InputStream is = new FileInputStream(new File(wordPath));
			WordExtractor ex = new WordExtractor(is);
			textAll = ex.getText();
			System.out.println(textAll);
		}else if( wordPath.endsWith(".docx") ){
			 OPCPackage opcPackage = POIXMLDocument.openPackage(wordPath);
			 POIXMLTextExtractor extractor = new
			 XWPFWordExtractor(opcPackage);
			 textAll = extractor.getText();
			 System.out.println(textAll);
		}
		
		return textAll;
	}
	
	public static void wordToExcel( String wordPath , String scroeFile ){
		MAINSEQ.put("一", 1);
		MAINSEQ.put("二", 2);
		MAINSEQ.put("三", 3);
		MAINSEQ.put("四", 4);
		MAINSEQ.put("五", 5);
		MAINSEQ.put("六", 6);
		MAINSEQ.put("七", 7);
		MAINSEQ.put("八", 8);
		MAINSEQ.put("九", 9);
		MAINSEQ.put("十", 10);
		
		Set<Entry<String,Integer>> entrySet = MAINSEQ.entrySet();
		Iterator<Entry<String, Integer>> iterator = entrySet.iterator();
		Entry<String, Integer> entry = iterator.next();
		String nextBigTitle = entry.getKey();
		Integer bigTitleNum = entry.getValue();
		String currentBigTitle = "";
		
		try {
			String textAll = loadTxtFromDoc(wordPath);

			 
			ByteArrayInputStream byteInputStream = new ByteArrayInputStream(textAll.getBytes());
			BufferedReader br = new BufferedReader( new InputStreamReader( byteInputStream ) );
			String line = br.readLine();
			
			ArrayList<QuestionBean> questions = new ArrayList<QuestionBean>();
			int counter = 1;
			while(  line !=null ) {
				String trimLine = line.trim();
				
				String bigTitlePrefixStr = nextBigTitle + bigTitleSEP;
				if( trimLine.startsWith(bigTitlePrefixStr) ){
					counter = 1;
					entry = iterator.next();
					currentBigTitle = nextBigTitle;
					
					nextBigTitle = entry.getKey();
					bigTitleNum = entry.getValue();
					
					line = br.readLine();
					continue;
				}
				if( counter==58 ){
				}
				
				if( trimLine.startsWith(counter+titleSEP) ){
					if( counter == 11 ){
						System.out.println();
					}
					QuestionBean bean = new QuestionBean( counter );
					bean.setBigTitle(currentBigTitle);
					int indexOf = trimLine.indexOf(titleSEP);
					if( indexOf!=-1 ){
						trimLine = trimLine.substring( indexOf +1 );
					}
					bean.setStem(trimLine);
					
					LinkedList<String> abcd = getOptionStarts();
					String detail = null;
					boolean isStem = true;
					Iterator<String> abcdIter = abcd.iterator();
					String firstOption = abcdIter.next();
					while(  (detail=br.readLine())!=null&& !detail.trim().startsWith(  (counter+1)+titleSEP )  ){
						if( !abcdIter.hasNext() ){
							break;
						}
						String detailTrim = detail.trim();
						if( bean.getOptions().size()>0 && detailTrim.length()<=2 ){
							break;
						}
						if(  isStem && !detailTrim.startsWith(firstOption) ){
							bean.setStem(bean.getStem()+detailTrim);
						}
						if( detailTrim.startsWith(firstOption) ){
							isStem = false;
						}
						
						String toUseSep = "-";
						if( detailTrim.startsWith(firstOption+optionSEP1) ){
							toUseSep = optionSEP1;
						}else if( detailTrim.startsWith(firstOption+optionSEP2) ){
							toUseSep = optionSEP2;
						} else if( detailTrim.startsWith(firstOption+optionSEP3) ){
							toUseSep = optionSEP3;
						}
						if( toUseSep.equals("-") ){
							continue;
						}
						String key = firstOption + toUseSep;
//						bean.addOption(firstOption, detailTrim.substring( key.length()));
//						firstOption = abcdIter.next();
						
						int lastIndex = 0;
						String nextOpt = abcdIter.next();
						String nextKey = nextOpt +toUseSep;
						while(  detailTrim.contains( nextKey  )   ){
							int indexOf2 = detailTrim.indexOf(nextKey);
							bean.addOption(firstOption, detailTrim.substring( lastIndex +key.length() , indexOf2 )  );
							
							firstOption = nextOpt;
							nextOpt = abcdIter.next();
							nextKey = nextOpt+toUseSep;
							
							lastIndex = indexOf2;
						}
						bean.addOption(firstOption, detailTrim.substring( lastIndex + nextKey.length() )  );
						firstOption = nextOpt;
						
					}
					
					line = detail;
					questions.add(bean);
					counter++;
				}else{
					line = br.readLine();
				}
				
			}
			
//			ScoreArray.getScroe("conf/" + scroeFile, questions);
			printBean(questions , true);
//			createExcel2(questions );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void printBean(List<QuestionBean> qs , boolean isprintAll ){
		System.out.println( qs.size()  + "  总题目" );
		System.out.println( "MAYBE HAVE PROBLEMS ?????????????????????" );
		for( QuestionBean b : qs ){
			if( b.getOptions().size()<4 ){
				System.out.println( b );
			}
		}
		
		if( !isprintAll  ){
			return ;
		}
		
		System.out.println( "ALL QUES===============================" );
		System.out.println( qs.size()  + "  总题目" );
		Set<QuestionBean> set = new HashSet<QuestionBean>();
		for( QuestionBean b : qs ){
			System.out.println( b );
			set.add(b);
		}
		
		InputStream ras = WordToExcel.class.getResourceAsStream(answerPath);
		Properties pro = new Properties();
		try {
			pro.load(ras);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String answer = "";
		
		System.out.println( "RANDOM PRINT ===============================" );
		int counter = 1;
		for( QuestionBean b : set  ){
			
			StringBuilder sb = new StringBuilder();
			Set<Entry<String,String>> entrySet = b.getOptions().entrySet();
			for(  Entry<String,String> en : entrySet ){
				String key = en.getKey();
				String value = en.getValue();
				sb.append( key.trim() + "." + value.trim() +"    ");
			}
			
			System.out.println( counter ++ + "、"+b.getStem().trim() );
			System.out.println(sb.toString());
			System.out.println( "========= src num:::" + b.getSeqNum()  );
			System.out.println(   );
			
			answer = answer + " , "+pro.getProperty(b.getSeqNum()+"");
		}
		
		System.out.println(  answer  );
		
	}
	
	public static final String A ="A";
	public static final String B ="B";
	public static final String C ="C";
	public static final String D ="D";
	public static final String EE ="E";
	public static final String F ="F";
	public static final String G ="G";
	public static final String H ="H";

	public static String[] title= { "序号"  , "题干" , A  ,  B , C , D  , EE , F,
		"Ascore", "Bscore", "Cscore", "Dscore", "Escore" , "Fscore"  ,  "难度" , "Remark(备注)" };

	
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
			int seqNum = i+1;
			cell2.setCellValue(seqNum+"");
		
			cell2 = row2.createCell(1);
			cell2.setCellValue(bean.getStem()+"");
			
			int counter = 2;
			LinkedHashMap<String,String> options = bean.getOptions();
			cell2 = row2.createCell(counter++);
			cell2.setCellValue( options.get(A) );
			cell2 = row2.createCell(counter++);
			cell2.setCellValue( options.get(B) );
			cell2 = row2.createCell(counter++);
			cell2.setCellValue( options.get(C) );
			cell2 = row2.createCell(counter++);
			cell2.setCellValue( options.get(D) );
			cell2 = row2.createCell(counter++);
			cell2.setCellValue( options.get(EE) );
			cell2 = row2.createCell(counter++);
			cell2.setCellValue( options.get(F) );
			
			cell2 = row2.createCell(counter++);
			cell2.setCellValue( bean.getaScroe() );
			cell2 = row2.createCell(counter++);
			cell2.setCellValue( bean.getbScroe() );
			cell2 = row2.createCell(counter++);
			cell2.setCellValue( bean.getcScroe() );
			cell2 = row2.createCell(counter++);
			cell2.setCellValue( bean.getdScroe() );
			cell2 = row2.createCell(counter++);
			cell2.setCellValue( bean.geteScroe() );
			cell2 = row2.createCell(counter++);
			cell2.setCellValue( bean.getfScroe() );
			
			cell2 = row2.createCell(counter++);
			cell2.setCellValue( bean.getDifficulty() );
			cell2 = row2.createCell(counter++);
			cell2.setCellValue( bean.getRemark() );
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
		sss.add(EE);
		sss.add(F);
		sss.add(G);
		sss.add(H);
		return sss;
	}

}
