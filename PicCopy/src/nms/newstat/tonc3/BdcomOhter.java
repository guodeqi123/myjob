package nms.newstat.tonc3;

import java.util.HashMap;
import java.util.HashSet;
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
import nms.newstat.tonc2.LoadNCKWInfo;
import nms.newstat.tonc2.NCKwObj;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class BdcomOhter {
	
	public static final String bdOther = "D:/ForBdcom/0stat1/Data0901/666�ִ�����ѯ.xlsx";
	
	public static void load(){
		
		int sheeNum = 0; 
		int startrow = 3; 
		
		int storeNum = 0 ; 
		int pncol = 1 ; 
		int countcol = 3; 
		
		 Map<String, U8KbObj> u8kbToObj = new HashMap<String, U8KbObj>();
		 
		Workbook wb = FPath.getWB(bdOther);
		Sheet sheetAt = wb.getSheetAt(sheeNum);
		int lastRowNum = sheetAt.getLastRowNum();
		Row oneRow = null;
		for(  int i=startrow ; i<=lastRowNum  ; i++  ){
			oneRow = sheetAt.getRow(i);
			if(  oneRow == null ){
			 	continue;
			}
			Cell storeNumCell = oneRow.getCell( storeNum );
			Cell pnCell = oneRow.getCell( pncol );
			Cell countCell = oneRow.getCell( countcol );
			
			String u8KbStr = Convertor2.getCellValue(storeNumCell);
			String pnStr = Convertor2.getCellValue(pnCell);
			String countStr = Convertor2.getCellValue(countCell);
			if( StringUtils.isEmpty(countStr) || StringUtils.isEmpty(pnStr)  || StringUtils.isEmpty(u8KbStr)){
				continue;
			}
			Double dCnt = Double.parseDouble(countStr);
			if( dCnt<=0 ){
				continue;
			}
			pnStr = pnStr.trim().toUpperCase();
			u8KbStr = u8KbStr.trim().toUpperCase();
			
			kwpn.add( pnStr + "_"+ u8KbStr);
			
			U8KbObj u8KbObj = u8kbToObj.get(u8KbStr);
			if( u8KbObj ==null  ){
				u8KbObj = new U8KbObj(u8KbStr);
				u8kbToObj.put(u8KbStr   , u8KbObj);
			}
			
			Boolean pnEnable = LoadPnInfos.pnInSNManage.get(pnStr);
			if( pnEnable==null  ){
				u8KbObj.getPnToCountNotInNC().put(pnStr, dCnt);
			}else if( pnEnable ){
				Map<String, Double> pnToCountSN = u8KbObj.getPnToCountSN();
				Double double1 = pnToCountSN.get(pnStr);
				if( double1 ==null ){
					pnToCountSN.put(pnStr, dCnt);
				}else{
					pnToCountSN.put(pnStr,  double1 + dCnt);
				}
			}else{
				Map<String, Double> pnToCountNotSN = u8KbObj.getPnToCountNotSN();
				Double double1 = pnToCountNotSN.get(pnStr);
				if( double1 ==null ){
					pnToCountNotSN.put(pnStr, dCnt);
				}else{
					pnToCountNotSN.put(pnStr, dCnt + double1);
				}
			}
		}
		
		printInfo(u8kbToObj);
		writeExcel(u8kbToObj);
		System.out.println( "BdcomOhter.load() �������ܸ��� :: "  + u8kbToObj.size() );
		
	}
	
	public static Set<String> kwpn = new HashSet<String>();

	private static void printInfo(Map<String, U8KbObj> u8kbToObj) {
		int counter = 0;
		Set<Entry<String, U8KbObj>> entrySet = u8kbToObj.entrySet();
		for(  Entry<String, U8KbObj> en : entrySet ){
			String key = en.getKey();
			U8KbObj value = en.getValue();
			Map<String, Double> pnToCountNotInNC = value.getPnToCountNotInNC();
			Map<String, Double> pnToCountSN = value.getPnToCountSN();
			Map<String, Double> pnToCountNotSN = value.getPnToCountNotSN();
			String str = "BdcomOhter.printInfo ������"+ key +  " , ���������� PNδ��NC������!!!!:" + pnToCountNotInNC.size() +
					" ,     PN����++++++++++SN���������" + pnToCountSN.size() + 
					" ,     PN����δSN���������" + pnToCountNotSN.size() ;
//			System.out.println(str   );
			
			counter = counter + pnToCountNotInNC.size()  + pnToCountSN.size() + pnToCountNotSN.size();
		}
		System.out.println(  "BdcomOhter.printInfo ========================================" + counter  + " , " + kwpn.size() );
	}
	
	public static final String dateStr = "2019-09-01";
	
	private static void writeExcel(Map<String, U8KbObj> u8kbToObj) {
		
		Set<Entry<String, U8KbObj>> entrySet = u8kbToObj.entrySet();
		for(  Entry<String, U8KbObj> en : entrySet ){
			String u8kb = en.getKey();
			U8KbObj value = en.getValue();
			
			String nckczz = "01";
			String nckb = LoadNCKWInfo.kbU8ToNC.get(u8kb);
			ImportSheetObject sheetObj = new ImportSheetObject();
			ImportTitleObj titleObj = new ImportTitleObj(1, nckczz, dateStr, nckb);
			sheetObj.addTtile(titleObj);
			int counter1 = 1;
			
			Map<String, Double> pnToCountSN = value.getPnToCountSN();
			Set<Entry<String,Double>> entrySet2 = pnToCountSN.entrySet();
			for(  Entry<String,Double> pnCnt :    entrySet2 ){
				String tmpPn = pnCnt.getKey();
				Double tmpCnt = pnCnt.getValue();
				String[] nckw = getNCKW( nckczz , u8kb , nckb ,tmpPn );
				String nckwStr = nckw[0];
				
				int size = tmpCnt.intValue();
				String unitStr = LoadPnInfos.pnToUnit.get(tmpPn);
				for(  int i=0 ; i<size  ; i++  ){
					String sn = getVSN( nckczz ,  nckb  , nckwStr );
					ImportRowObj row = new ImportRowObj(  titleObj.getTitleNum() , counter1++ ,  tmpPn , unitStr, 1, dateStr , nckwStr );
					row.setSn(sn);
					row.setSnUnit(unitStr);
					sheetObj.addRow( row );
				}
			}
			
			Map<String, Double> pnToCountNotSN = value.getPnToCountNotSN();
			entrySet2 = pnToCountNotSN.entrySet();
			for(    Entry<String, Double>  pnCnt :  entrySet2 ){
				String tmpPn = pnCnt.getKey();
				Double tmpCnt = pnCnt.getValue();
				String unitStr = LoadPnInfos.pnToUnit.get(tmpPn);
				String[] nckw = getNCKW( nckczz , u8kb , nckb ,tmpPn );
				String nckwStr = nckw[0];
				ImportRowObj row = new ImportRowObj(  titleObj.getTitleNum() , counter1++ ,  tmpPn , unitStr, tmpCnt, dateStr, nckwStr);
				sheetObj.addRow( row );
			}
			
			String importFile = "import_" + u8kb+"��.xlsx";
			sheetObj.writeToFile( importFile );
		}
	}
	
	private static String getVSN(String nckczz, String nckb, String nckw) {
		
		String vsn = InventedSerialNumberUtil.getInventedSerialNumber(nckczz, nckb, nckw );
		return vsn ;
	}

	public static Set<String> pnNoKWList = new HashSet<String>();
	
	private static String[] getNCKW(String nckczz, String u8kb, String nckb , String pn ) {
		String[] ret = new String[]{  "" , ""  };
		
		if(  "16".equals(u8kb) ){
			
			String u8kw = Load16KbPNInfo.pnToU8KW.get(pn);
			if(  StringUtils.isEmpty( u8kw )  ){
				pnNoKWList.add( u8kb +" , " +pn);
				return ret;
			}
			NCKwObj ncKw;
			try {
				ncKw = LoadNCKWInfo.getNCKw(nckczz, u8kb, u8kw);
				ret = new String[]{  ncKw.getNcKw() , ncKw.getU8Kw()  };
			} catch (RuntimeException e) {
//				System.err.println( "PN:::" + pn + " ,  " + nckczz +  " , "  + u8kb + " , "+ u8kw);
//				e.printStackTrace();
				return ret;
			}
		}
		
		return ret ;
	}

	public static void main(String[] args) {
		// ����NC��λ��U8��λ ӳ��
		LoadNCKWInfo.load();
		// �������ϵ���״̬״̬
		LoadPnInfos.loadPNStatus();
		
		Load16KbPNInfo.load();
		
		load();
		
		System.out.println(  "BdcomOhter.main  PN �޷��ҵ���λ�ܸ���SUM ::" + pnNoKWList.size() );
		for(String  str :   pnNoKWList ){
			System.out.println(  "BdcomOhter.main  PN �޷��ҵ���λ ::" + str );
		}
		
	}
	
	
}
