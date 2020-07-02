package nms.newstat.tonc34;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nms.newstat.Convertor2;
import nms.newstat.FPath;
import nms.newstat.LoadPnInfos;
import nms.newstat.tonc2.ImportRowObj;
import nms.newstat.tonc2.ImportSheetObject;
import nms.newstat.tonc2.ImportTitleObj;
import nms.newstat.tonc2.InventedSerialNumberUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ImportNC2 {
	
	public static String[][] importSrcFile = new String[][]{
		// fname                                  sheenum ,startrow , pncol ,countcol,  
		{ "D:/ForBdcom/0stat1/Date0904/0科技真实库存.XLSX" ,      "1" , "1"     ,"4" , "8"   } , 
		{ "D:/ForBdcom/0stat1/Date0904/0迅坤真实库存.XLSX" ,      "1" , "1"     ,"4" , "8"   } , 
		
	};
	
	public static String[][] useNCKW = new String[][]{
		{ "0101" ,"10" , "成品" , "import_科技30.xlsx"  } ,  //nc kczz ,  nc kb ,  nc kw 
		{ "0102" ,"10" , "成品"  ,"import_迅坤30.xlsx" } ,  //科技    nc kczz ,  nc kb ,  nc kw 
		
	};
	
	
	public static void load(){
		
		for( int i=0; i<importSrcFile.length  ; i++  ){
			String[] fileInfo = importSrcFile[i];
			String[] kwInfo = useNCKW[i];
			doParse( fileInfo ,  kwInfo);
		}
	}
	
	/**
	 * 解析并生成Excel
	 * @param fileInfo
	 * @param kwInfo
	 */
	private static void doParse(String[] fileInfo, String[] kwInfo) {
		
		String fpath = fileInfo[0];
		int sheeNum = Integer.parseInt( fileInfo[1]  ); 
		int startrow = Integer.parseInt( fileInfo[2]  ); 
		int pncol = Integer.parseInt( fileInfo[3] ); 
		int countcol = Integer.parseInt( fileInfo[4]  ); 
		
		Map<String, Double> pnToCountSN = new HashMap<String, Double>();
		Map<String, Double> pnToCountNotSN = new HashMap<String, Double>();
		Map<String, Double> pnToCountNotInNC = new HashMap<String, Double>();
		
		Workbook wb = FPath.getWB(fpath);
		Sheet sheetAt = wb.getSheetAt(sheeNum);
		int lastRowNum = sheetAt.getLastRowNum();
		Row oneRow = null;
		for(  int i=startrow ; i<=lastRowNum  ; i++  ){
			oneRow = sheetAt.getRow(i);
			if(  oneRow == null ){
			 	continue;
			}
			Cell pnCell = oneRow.getCell( pncol );
			Cell countCell = oneRow.getCell( countcol );
			
			String pnStr = Convertor2.getCellValue(pnCell);
			String countStr = Convertor2.getCellValue(countCell);
			Double dCnt = Double.parseDouble(countStr);
			if( dCnt<=0 || StringUtils.isEmpty(pnStr) || "总计".equals(pnStr.trim()) ){
				continue;
			}
//			System.out.println( (i+1) +"!!!!"+  pnStr + " , " +  countStr );
			pnStr = pnStr.trim().toUpperCase();
			
			Boolean pnEnable = LoadPnInfos.pnInSNManage.get(pnStr);
			if( pnEnable==null  ){
				pnToCountNotInNC.put(pnStr, dCnt);
			}else if( pnEnable ){
				Double double1 = pnToCountSN.get(pnStr);
				if( double1 ==null ){
					pnToCountSN.put(pnStr, dCnt);
				}else{
					pnToCountSN.put(pnStr,  double1 + dCnt);
				}
			}else{
				Double double1 = pnToCountNotSN.get(pnStr);
				if( double1 ==null ){
					pnToCountNotSN.put(pnStr, dCnt);
				}else{
					pnToCountNotSN.put(pnStr, dCnt + double1);
				}
			}
		}
		
		Set<Entry<String, Double>> entrySet = pnToCountNotInNC.entrySet();
		for( Entry<String, Double> en : entrySet  ){
			System.out.println(  "ImportNC1.doParse ###物料编码不在NC中 :: , "  + en.getKey() /*+ " , "  +en.getValue()*/ );
		}
		System.out.println( "ImportNC1.doParse 物料编码不在NC中个：：" +  pnToCountNotInNC.size() );
		System.out.println( "ImportNC1.doParse 物料编码启用++++++++++++++++++SN个：：" +  pnToCountSN.size() );
		System.out.println( "ImportNC1.doParse 物料编码未启用SN个：：" +  pnToCountNotSN.size() );
		
		createExcel(  pnToCountSN , pnToCountNotSN  , kwInfo);
	}

	public static String dataStr = "2019-08-31";
	private static void createExcel(Map<String, Double> pnToCountSN, Map<String, Double> pnToCountNotSN, String[] kwInfo) {
		
		ImportSheetObject sheetObj = new ImportSheetObject();
		ImportTitleObj titleObj = new ImportTitleObj( 1, kwInfo[0], dataStr, kwInfo[1] );
		sheetObj.addTtile(titleObj);
		
		String ncKwStr = kwInfo[2];
		
		int counter1 = 1;
		Set<Entry<String, Double>> entrySet = pnToCountSN.entrySet();
		for(    Entry<String, Double>  en :  entrySet ){
			String tmpPn = en.getKey();
			Double tmpCnt = en.getValue();
			int size = tmpCnt.intValue();
			String unitStr = LoadPnInfos.pnToUnit.get(tmpPn);
			
			for(  int i=0 ; i<size  ; i++  ){
				String sn = getVSN( kwInfo[0] ,  kwInfo[1] , kwInfo[2] );
				ImportRowObj row = new ImportRowObj(  titleObj.getTitleNum() , counter1++ ,  tmpPn , unitStr, 1, dataStr, ncKwStr);
				row.setSn(sn);
				row.setSnUnit(unitStr);
				sheetObj.addRow( row );
			}
		}
		
		entrySet = pnToCountNotSN.entrySet();
		for(    Entry<String, Double>  en :  entrySet ){
			String tmpPn = en.getKey();
			Double tmpCnt = en.getValue();
			
			String unitStr = LoadPnInfos.pnToUnit.get(tmpPn);
			ImportRowObj row = new ImportRowObj(  titleObj.getTitleNum() , counter1++ ,  tmpPn , unitStr, tmpCnt, dataStr, ncKwStr);
			sheetObj.addRow( row );
			
		}
		
		sheetObj.writeToFile(  kwInfo[3] );
	}

	
	public static final String vsnDateStr = "19908";
	private static String getVSN(String nckczz, String nckb, String nckw) {
		
		String vsn = InventedSerialNumberUtil.getInventedSerialNumber(nckczz, nckb, nckw , vsnDateStr );
		return vsn ;
	}

	public static void main(String[] args) {
		
		LoadPnInfos.loadPNStatus();
		load();
		int currentIndex = InventedSerialNumberUtil.getCurrentIndex();
		System.out.println( "ImportNC1.main()  INDEX已用到 :: " +currentIndex   );
		
	}
	
}	
