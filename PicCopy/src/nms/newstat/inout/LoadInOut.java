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
	public static String indir = "D:/ForBdcom/0stat1/in";
	public static 	String outDir = "D:/ForBdcom/0stat1/out";
	
	public static void main(String[] args) {
		
		Set<String> snSetIn = new HashSet<String>();
		Set<String> snSetout = new HashSet<String>();
		
		File file = new File(indir);
		File[] listFiles = file.listFiles();
		for (File ff : listFiles) {
			String oneInFile = ff.getAbsolutePath();
			InFileParser par = new InFileParser( oneInFile );
			List<InOutObj> aaa = par.parseIn();
			inList.addAll(aaa);
			for(  InOutObj  in : aaa ){
				snSetIn.add(in.getSn());
			}
		}
		
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
		
//		System.out.println("Èë¿â"+   inList.size()  );
		System.out.println("Èë¿â"+   snSetIn.size()  );
//		System.out.println(  "chuku :::" );
//		System.out.println(   outList.size()  );
		System.out.println(  "³ö¿â" +  snSetout.size()  );
		
	}
	
}
