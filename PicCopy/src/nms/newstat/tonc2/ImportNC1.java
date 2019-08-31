package nms.newstat.tonc2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import nms.newstat.Convertor2;
import nms.newstat.FPath;
import nms.newstat.LoadPnInfos;

public class ImportNC1 {
	
	public static final String importdir1 = "D:/ForBdcom/0stat1/Data0831/import/";
	public static String[][] importSrcFile = new String[][]{
		// fname                                  sheenum ,startrow , pncol ,countcol,  
		{ importdir1+ "2_MY已发货未开票.xlsx" ,      "0" , "1"     ,"0" , "2"   }
	};
	
	public static String[][] useNCKW = new String[][]{
		//
		{ "01" ,"66" , "02" , "已发货未开票1.xlsx"  } ,  //    nc kczz ,  nc kb ,  nc kw 
		{ "0101" ,"10" , "01"  } ,  //科技    nc kczz ,  nc kb ,  nc kw 
		
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
			
//			System.out.println( (i+1) +"!!!!"+  pnStr + " , " +  countStr );
			
			Boolean pnEnable = LoadPnInfos.pnInSNManage.get(pnStr);
			if( pnEnable==null  ){
				pnToCountNotInNC.put(pnStr, dCnt);
			}else if( pnEnable ){
				pnToCountSN.put(pnStr, dCnt);
			}else{
				pnToCountNotSN.put(pnStr, dCnt);
			}
		}
		
//		Set<Entry<String, Double>> entrySet = pnToCountNotInNC.entrySet();
//		for( Entry<String, Double> en : entrySet  ){
//			System.out.println(  "###物料编码不在NC中 :: , "  + en.getKey() /*+ " , "  +en.getValue()*/ );
//		}
		System.out.println( " 物料编码不在NC中个：：" +  pnToCountNotInNC.size() );
		System.out.println( " 物料编码启用SN个：：" +  pnToCountSN.size() );
		System.out.println( " 物料编码未启用SN个：：" +  pnToCountNotInNC.size() );
		
		createExcel(  pnToCountSN , pnToCountNotInNC  , kwInfo);
	}

	public static String dataStr = "2019-08-31";
	private static void createExcel(Map<String, Double> pnToCountSN, Map<String, Double> pnToCountNotInNC, String[] kwInfo) {
		
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
		
		entrySet = pnToCountNotInNC.entrySet();
		for(    Entry<String, Double>  en :  entrySet ){
			String tmpPn = en.getKey();
			Double tmpCnt = en.getValue();
			
			String unitStr = LoadPnInfos.pnToUnit.get(tmpPn);
			ImportRowObj row = new ImportRowObj(  titleObj.getTitleNum() , counter1++ ,  tmpPn , unitStr, tmpCnt, dataStr, ncKwStr);
			sheetObj.addRow( row );
			
		}
		
		sheetObj.writeToFile(  kwInfo[3] );
	}


	private static String getVSN(String nckczz, String nckb, String nckw) {
		
		return System.currentTimeMillis() + "" ;
	}

	public static void main(String[] args) {
		
		LoadPnInfos.loadPNStatus();
		load();
		
		
	}
	
}	
