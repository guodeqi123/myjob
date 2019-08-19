package nms;

import java.io.File;
import java.util.List;
import java.util.Map;

import nms.stat.PnCountLoader;

public class StatAll {
	
	public static String baseDir = "D:/ForBdcom/";
	
	public static void main(String[] args) {
		
		PnCountLoader.load();
		Map<String, Integer> pnToCount = PnCountLoader.pnToCount;
		
		File file = new File(baseDir);
		
		File[] listFiles = file.listFiles();
		for( File ff : listFiles  )  {
			String name = ff.getName();
			if(ff.isDirectory() && name.endsWith("_PP")  ){
				String namePrefix  = name.split("_")[0];
				String fa = baseDir + name +"/"+namePrefix+"A.xlsx";
				String fb = baseDir + name +"/"+namePrefix+"B.xlsx";
				
				Convertor convertorA = new Convertor(fa);
				Map<String, List<KWObj> > retA = convertorA.parseExcel2( );
				List<KWObj> kwErrA = retA.get("error");
				List<KWObj> kwAllSuccessA = retA.get("success");
				List<String[]> errPnsA = convertorA.writeSuccess2( kwAllSuccessA  , true);
				
				Convertor convertorB = new Convertor(fb);
				Map<String, List<KWObj> > retB = convertorB.parseExcel2( );
				List<KWObj> kwErrB = retB.get("error");
				List<KWObj> kwAllSuccessB = retB.get("success");
				List<String[]> errPnsB = convertorB.writeSuccess2( kwAllSuccessB  , true);
				
				String fin = baseDir + name +"/"+namePrefix+"in.xlsx";
				String fout = baseDir + name +"/"+namePrefix+"out.xlsx";
				
				InFileParser inFileParser = new InFileParser( fin );
				List<InOutObj> parseIn = inFileParser.parseIn();
				OutFileParser outFileParser = new OutFileParser( fout );
				List<InOutObj> parseOut = outFileParser.parseOut();
				
				
//				System.out.println(  fa + "  , " +  new File(fa).exists()  );
//				System.out.println(  fb + "  , " +  new File(fb).exists()  );
//				System.out.println(  fin + "  , " +  new File(fin).exists()  );
//				System.out.println(  fout + "  , " +  new File(fout).exists()  );
				
			}
			
			System.out.println(); 
		}
		
		
		
	}
	
	
	
	
}
