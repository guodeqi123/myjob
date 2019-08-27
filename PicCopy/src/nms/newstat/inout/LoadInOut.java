package nms.newstat.inout;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nms.InFileParser;
import nms.InOutObj;
import nms.OutFileParser;
import nms.RowData;

public class LoadInOut {

	public static List<InOutObj> inList = new ArrayList<InOutObj>();
	public static List<InOutObj> outList = new ArrayList<InOutObj>();
	public static String outDir = "D:/ForBdcom/0stat1/����ɨ���¼";
	
	public static 	String indir1 = "D:/ForBdcom/0stat1/���ɨ���¼";
	public static 	String indir2 = "D:/ForBdcom/0stat1/���ɨ���¼/nf";
	
	
	public static void load(){
		
		Set<String> snSetout = new HashSet<String>();
		File file2 = new File(outDir);
		File[] listFiles2 = file2.listFiles();
		for (File ff : listFiles2) {
			String oneInFile = ff.getAbsolutePath();
			OutFileParser par = new OutFileParser( oneInFile );
			List<InOutObj> aaa = par.parseOut();
			outList.addAll(aaa);
			for(  InOutObj  in : aaa ){
				snSetout.add(in.getSn());
			}
		}
		
		
		Set<String> snSetIn = new HashSet<String>();
		File file = new File(indir1);
		File[] listFiles = file.listFiles();
		for (File ff : listFiles) {
			if( ff.isDirectory()  ){
				continue;
			}
			String oneInFile = ff.getAbsolutePath();
			InFileParser par = new InFileParser( oneInFile );
			List<InOutObj> aaa = par.parseIn();
			inList.addAll(aaa);
			for(  InOutObj  in : aaa ){
				snSetIn.add(in.getSn());
			}
		}
		
		//����¸�ʽ���ֽ���
		file = new File(indir2);
		listFiles = file.listFiles();
		for (File ff : listFiles) {
			String oneInFile = ff.getAbsolutePath();
			InFileParser par = new InFileParser( oneInFile );
			List<InOutObj> aaa = par.parseIn2();
			inList.addAll(aaa);
			for(  InOutObj  in : aaa ){
				snSetIn.add(in.getSn());
			}
		}
		
//		System.out.println("���"+   inList.size()  );
//		System.out.println(   outList.size()  );
//		System.out.println(  "" );
		
		System.out.println("���"+   snSetIn.size()  );
		System.out.println(  "����" +  snSetout.size()  );
		
	}
	
	
	public static void main(String[] args) {
		
	
		load();

		
	}
	
}
