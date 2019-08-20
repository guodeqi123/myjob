package nms;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import nms.stat.PnCountLoader;

public class StatAll {
	
	public static String baseDir = "D:/ForBdcom/";
	
	public static void main(String[] args) {
		
		File file = new File(baseDir);
		
		File[] listFiles = file.listFiles();
		for( File ff : listFiles  )  {
			String name = ff.getName();
			if(ff.isDirectory() && name.endsWith("_PP")  ){
				String namePrefix  = name.split("_")[0];
				String fa = baseDir + name +"/"+namePrefix+"A.xlsx";
				String fb = baseDir + name +"/"+namePrefix+"B.xlsx";
				
				if( fileExist(fa) ){
					Convertor convertorA = new Convertor(fa);
					Map<String, List<KWObj> > retA = convertorA.parseExcel2( );
					List<KWObj> kwErrA = retA.get("error");
					List<KWObj> kwAllSuccessA = retA.get("success");
					List<String[]> errPnsA = convertorA.writeSuccess2( kwAllSuccessA  , false);
					gatherAll(kwAllSuccessA);
				}
				
				if( fileExist(fb) ){
					Convertor convertorB = new Convertor(fb);
					Map<String, List<KWObj> > retB = convertorB.parseExcel2( );
					List<KWObj> kwErrB = retB.get("error");
					List<KWObj> kwAllSuccessB = retB.get("success");
					List<String[]> errPnsB = convertorB.writeSuccess2( kwAllSuccessB  , false);
					gatherAll(kwAllSuccessB );
				}
				
				String fin = baseDir + name +"/"+namePrefix+"in.xlsx";
				String fout = baseDir + name +"/"+namePrefix+"out.xlsx";
				if(  fileExist(fin) ){
					InFileParser inFileParser = new InFileParser( fin );
					List<InOutObj> parseIn = inFileParser.parseIn();
				}
				if(  fileExist(fout) ){
					OutFileParser outFileParser = new OutFileParser( fout );
					List<InOutObj> parseOut = outFileParser.parseOut();
				}
			}
			
			compareAll();
			
			System.out.println(); 
		}
		
	}
	
	private static boolean fileExist(String fa) {
		return new File(fa).exists();
	}

	private static void compareAll() {
		
		PnCountLoader.load();
		Map<String, Integer> pnToCount1 = PnCountLoader.pnToCount;
		
		Set<Entry<String,Integer>> entrySet = pnToCount2.entrySet();
		for( Entry<String,Integer> en: entrySet   ){
			String key = en.getKey();
			Integer v1 = en.getValue();
			
			Integer v2 = pnToCount1.get(key);
			if( v2==null ){
				System.out.println(   "！！！财务反馈数据没有找到相关物料编码统计：" + key + v1);
			}else {
				int a = v1-v2;
				int abs = Math.abs(a);
				if(  abs<20  ){
					System.out.println(   "！！！！！！物料编码个数存在隐患：" + key + ", 数量：" + v1 + " , 财务核算数量:" + v2);
				}
			}
		}
		
		
	}

	public static Map<String, Integer> pnToCount2 = new HashMap<String, Integer>();
	
	public static void gatherAll( List<KWObj> kwAllSuccess ){
		int kwCount = kwAllSuccess.size();
		for(  int kwi = 0 ; kwi<kwCount ; kwi++ ){
			KWObj kwObj = kwAllSuccess.get(kwi);
			List<RowData> datas = kwObj.getRowSnList();
			int size = datas.size();
			for (int i = 0; i < size; i++) {
				RowData rowData = datas.get(i);
				String kw = rowData.getPosNum();
				String pn = rowData.getMaterialNum();
				
				Integer integer = pnToCount2.get(pn);
				if( integer ==null    ){
					pnToCount2.put(pn, 1);
				}else{
					pnToCount2.put(pn, integer+1);
				}
				
				
			}
		}
	}
	
	
	
	
}
