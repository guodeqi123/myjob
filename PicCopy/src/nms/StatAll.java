package nms;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
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
					allIn.addAll(parseIn);
				}
				if(  fileExist(fout) ){
					OutFileParser outFileParser = new OutFileParser( fout );
					List<InOutObj> parseOut = outFileParser.parseOut();
					allOut.addAll(parseOut);
				}
			}
		}
		
		compareAll();
		
	}
	
	public static List<InOutObj>  allIn = new ArrayList<InOutObj>();
	public static List<InOutObj>  allOut = new ArrayList<InOutObj>();
	
	private static boolean fileExist(String fa) {
		return new File(fa).exists();
	}

	private static void compareAll() {
		
		PnCountLoader.load();
		Map<String, Integer> pnToCount1 = PnCountLoader.pnToCount;
		
		for(  InOutObj oneIn : allIn ){
			String sn = oneIn.getSn();
			String pn = oneIn.getPn();
			Integer integer = pnToCount2.get(pn);
			if( integer ==null    ){
				pnToCount2.put(pn, 1);
			}else{
				pnToCount2.put(pn, integer+1);
			}
		}
		for( InOutObj oneOut : allOut ){
			String sn = oneOut.getSn();
			String pn1 = snTOPn.get(sn);
//			String pn2 = oneOut.getPn();
			if(  pn1==null ){
//				appendErr( "####出库SN 无法找到对应 PN , "  + sn + "出库单号:"+oneOut.getNum()  ,  2 );
			}
			Integer integer = pnToCount2.get(pn1);
			if( integer ==null || integer<1  ){
//				appendErr( "####出库SN 对应 PN数量不足 "  + sn + "出库单号:"+oneOut.getNum()  ,  2 );
			}else{
				pnToCount2.put(pn1, integer-1);
			}
		}
		
		Set<Entry<String,Integer>> entrySet = pnToCount2.entrySet();
		for( Entry<String,Integer> en: entrySet   ){
			String key = en.getKey().trim().toUpperCase();
			Integer v1 = en.getValue();
			
			Integer v2 = pnToCount1.get(key);
			if( v2==null ){
				appendErr(   "！财务反馈数据没有找到相关物料编码统计：" + key + v1  , 1 );
			}else if( v1<v2   ){
				int a = v2-v1;
				int abs = Math.abs(a);
				if(  abs<20  ){
					appendErr(   "！！！！！！物料编码个数存在隐患：" + key + ", 数量：" + v1 + " , 财务核算数量:" + v2 , 2 );
				}
			}else{
				int a = v2-v1;
				int abs = Math.abs(a);
				if( abs>0 && abs<20  ){
					appendErr(   "！！！物料编码个数存在隐患,财务核算数量较少：" + key + ", 数量：" + v1 + " , 财务核算数量:" + v2 ,3 );
				}
			}
		}
		writeResult();
	}

	public static Map<String, Integer> pnToCount2 = new HashMap<String, Integer>();
	public static Map<String, String> snTOPn = new HashMap<String, String>();
	
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
				String snsStr = rowData.getSnsStr();
				
				snTOPn.put(snsStr, pn);
				
				Integer integer = pnToCount2.get(pn);
				if( integer ==null    ){
					pnToCount2.put(pn, 1);
				}else{
					pnToCount2.put(pn, integer+1);
				}
				
				
			}
		}
	}
	
	public static List<String> rowsNo = new ArrayList<String>();
	public static List<String> rowsLess = new ArrayList<String>();
	public static List<String> rowsMore = new ArrayList<String>();
	
	public static void appendErr(String err , int  type ){
		if( type == 1 ){
			rowsNo.add(err);
		}else if( type ==2 ){
			rowsLess.add(err);
		}else{
			rowsMore.add( err) ;
		}
	}
	
	@SuppressWarnings("resource")
	public static void writeResult( ){
		File file = new File("res/a.txt");
		try {
			if( file.exists() ){
				System.out.println(  "YYYYYYY"  );
			}else{
				System.out.println(  "NNNNNN"  );
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			BufferedWriter br = new BufferedWriter(new OutputStreamWriter(fos));
			
			for(String sss : rowsLess   ){
				try {
					br.write(sss+"\r\n");
					System.out.println(  sss  ); 
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			for(String sss : rowsMore   ){
				try {
					br.write(sss+"\r\n");
					System.out.println(  sss  ); 
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			for(String sss : rowsNo   ){
				try {
					br.write(sss+"\r\n");
					System.out.println(  sss  ); 
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			
			br.flush();
			br.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		

		
	}
	
	
}
