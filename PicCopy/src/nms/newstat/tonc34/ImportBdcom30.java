package nms.newstat.tonc34;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nms.RowData;
import nms.newstat.FPath;
import nms.newstat.LoadPnInfos;
import nms.newstat.tonc2.ImportRowObj;
import nms.newstat.tonc2.ImportSheetObject;
import nms.newstat.tonc2.ImportTitleObj;
import nms.newstat.tonc2.InventedSerialNumberUtil;
import nms.newstat.tonc2.LoadNCKWInfo;
import nms.newstat.tonc34.LoadU8NCKwMap.U8kwPn_NcKbKw;
import nms.stat.StorePNObj;
import nms.stat.U8PNObj;

public class ImportBdcom30 {


	public static Map<String,  ScanPnObj> pnToPNObj = new  HashMap<String, ScanPnObj>();
	public static Map<String,  ImportSheetObject> u8kbToSheet = new HashMap<String, ImportSheetObject>();
	public static Map<String,  ImportTitleObj> u8kbToTitle = new HashMap<String, ImportTitleObj>();
	private static final String  dateStr = "2019-08-31";
	private static final String  sndateStr = "19903";
	
	public static void doExport(Map<String, RowData> distinctMap,
			Map<String, StorePNObj> pnToStoreObj, Map<String, U8PNObj> pnToU8Obj) {
		
		try {
			LoadU8NCKwMap.load();
		} catch (Exception e) {
			System.err.println(  "张毅龙文件解析出错::"  +e.getMessage() );
		}
		//整理  并  除去某些PN
		SpecialHandle.load();
		gather1( distinctMap , pnToStoreObj , pnToU8Obj );
		initImportSheet();
		
		writeScan(  pnToStoreObj , pnToU8Obj );
		writeStore(  pnToStoreObj , pnToU8Obj );
		writeU8(  pnToU8Obj );
		
		Set<Entry<String,ImportSheetObject>> entrySet = u8kbToSheet.entrySet();
		for( Entry<String,ImportSheetObject>  en : entrySet ){
			String key = en.getKey();
			ImportSheetObject value = en.getValue();
			String fname = "Import_"+key+"库.xlsx";
			value.writeToFile(fname );
		}
		
	}

	private static void initImportSheet() {
		ImportSheetObject sheet30 = new ImportSheetObject();
		ImportTitleObj title30 = new ImportTitleObj( 1, "01", dateStr, "10");// 30 
		sheet30.addTtile(title30);
		
		ImportSheetObject sheet31 = new ImportSheetObject();
		ImportTitleObj title31 = new ImportTitleObj( 2, "01", dateStr, "11");// 31 
		sheet31.addTtile(title31);
		
		ImportSheetObject sheet86 = new ImportSheetObject();
		ImportTitleObj title86 = new ImportTitleObj( 3, "01", dateStr, "13");// 86 
		sheet86.addTtile(title86);
		
		u8kbToSheet.put("30", sheet30);
		u8kbToSheet.put("31", sheet31);
		u8kbToSheet.put("86", sheet86);
		
		u8kbToTitle.put("30", title30);
		u8kbToTitle.put("31", title31);
		u8kbToTitle.put("86", title86);
	}


	private static void writeScan( Map<String, StorePNObj> pnToStoreObj, Map<String, U8PNObj> pnToU8Obj) {
		
		Set<Entry<String,ScanPnObj>> entrySet = pnToPNObj.entrySet();
		for(   Entry<String,ScanPnObj> en : entrySet ){
			String pnStr = en.getKey();
			ScanPnObj scanPnObj = en.getValue();
			List<RowData> snList = scanPnObj.getSnList();
			
			double scanCount = scanPnObj.getSnCount();
			if( scanCount == 0 ){
				continue;
			}
			double storeCount = 0 ;
			StorePNObj storePNObj = pnToStoreObj.remove(pnStr);
			if(  storePNObj != null ){
				storeCount = storePNObj.getSumCount();
			}
			double u8Count = 0 ;
			U8PNObj u8pnobj = pnToU8Obj.remove(pnStr);
			if(  u8pnobj != null ){
				u8Count = u8pnobj.getNewValue();
			}
			
			
			handleRealSn( snList );
			//三者相等  或者扫码数量最大
			if( (scanCount==storeCount &&  u8Count == storeCount)  || (scanCount>=storeCount  && scanCount>=u8Count ) ){
				continue;
			}
			
			int vCount = 0;
			//自盘数量最大  补虚拟号 录入扫码 最小的 30库位
			if(  storeCount>=scanCount && storeCount>=u8Count  ){
				vCount = (int) (storeCount - scanCount);
			}
			//U8最大  补虚拟号    录入扫码最小的 30库位
			if(  u8Count>=scanCount && u8Count>=storeCount  ){
				vCount = (int) (u8Count - scanCount);
			}
			handleRealVSn( vCount , scanPnObj);
		}
		
	}
	
	private static int rowCounter = 1;
	
	private static void handleRealVSn(int vCount, ScanPnObj scanPnObj) {
		
		U8kwPn_NcKbKw ncKwInfo =  scanPnObj.getKWForVSN( );
		for(  int i=0; i<vCount  ;i++  ){
			
			String pn = scanPnObj.getPn();
			String u8kb = ncKwInfo.getU8kb();
			String ncKb = ncKwInfo.getNcKb();
			String nckw = ncKwInfo.getNckw();
			String nckwName = ncKwInfo.getNckwName();
			
			String vSN = InventedSerialNumberUtil.getInventedSerialNumber( ncBdcomKCZZ , ncKb, nckw, sndateStr);
			String unit = LoadPnInfos.pnToUnit.get(pn);
			ImportSheetObject sheetObj = u8kbToSheet.get(u8kb);
			ImportTitleObj titleObj = u8kbToTitle.get(u8kb);
			
			ImportRowObj rowObj = new ImportRowObj(titleObj.getTitleNum(), rowCounter++, pn, unit, 1, dateStr, nckwName);
			rowObj.setSn(vSN);
			rowObj.setSnUnit(unit);
			sheetObj.addRow(rowObj);
		}
	}

	private static void handleRealSn(List<RowData> snList) {
		for(  RowData rd: snList   ){
			String snsStr = rd.getSnsStr();
			String scanKw = rd.getPosNum().toUpperCase().trim();
			String pn = rd.getMaterialNum().toUpperCase().trim();
			
			U8kwPn_NcKbKw u8kwPn_NcKbKw = LoadU8NCKwMap.getNCKWBykw_pn(scanKw ,pn );
			String nckwName = u8kwPn_NcKbKw.getNckwName();
			String u8kb = u8kwPn_NcKbKw.getU8kb();
			
			ImportSheetObject sheetObj = u8kbToSheet.get(u8kb);
			ImportTitleObj titleObj = u8kbToTitle.get(u8kb);
			String unit = LoadPnInfos.pnToUnit.get(pn);
			ImportRowObj rowObj = new ImportRowObj(titleObj.getTitleNum(), rowCounter++, pn, unit, 1, dateStr, nckwName);
			rowObj.setSn(snsStr);
			rowObj.setSnUnit(unit);
			sheetObj.addRow(rowObj);
		}
		
	}

	private static void writeStore(Map<String, StorePNObj> pnToStoreObj, Map<String, U8PNObj> pnToU8Obj) {
		
		Set<Entry<String,StorePNObj>> entrySet = pnToStoreObj.entrySet();
		for(  Entry<String,StorePNObj> en : entrySet  ) {
			String pnStr = en.getKey();
			StorePNObj storePnObj = en.getValue();
			
			Boolean boolean1 = LoadPnInfos.pnInSNManage.get(pnStr);
			if( boolean1 == null ){
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!  物料编码仍然不在NC ：：：" +  pnStr );
				continue;
			}
	
			double storeCount = storePnObj.getSumCount();
			if( storeCount <=0  ){
				continue;
			}
			double u8count = 0;
			U8PNObj remove = pnToU8Obj.remove(pnStr);
			if (remove != null) {
				u8count = remove.getNewValue();
			}
			
			if(  storeCount>=u8count  ){
				handleVSNForStore1( storePnObj  ,  boolean1 );
			}else {
				handleVSNForStore2( storePnObj ,u8count  , boolean1);
			}
			
		}
		
	}
	
	//自盘数大
	private static void handleVSNForStore1(StorePNObj storePnObj, Boolean enableSNManage ) {
		
		String pnStr = storePnObj.getPn();
		Map<String, Double> kwToCount = storePnObj.getKwToCount();
		Set<Entry<String,Double>> entrySet = kwToCount.entrySet();
		for(   Entry<String,Double> en : entrySet  ){
			String storeKW = en.getKey();
			Double storeCnt = en.getValue();
			U8kwPn_NcKbKw ncKwByU8KwPN = getNCKwByU8KwPN( storeKW ,  pnStr );
			
			String u8kb = ncKwByU8KwPN.getU8kb();
			String nckwName = ncKwByU8KwPN.getNckwName();
			String ncKb = ncKwByU8KwPN.getNcKb();
			String nckw = ncKwByU8KwPN.getNckw();
			
			ImportSheetObject sheetObj = u8kbToSheet.get(u8kb);
			ImportTitleObj titleObj = u8kbToTitle.get(u8kb);
			
			if( enableSNManage ){
				for(  int i=0; i<storeCnt ; i++  ){
					String vSN = InventedSerialNumberUtil.getInventedSerialNumber( ncBdcomKCZZ , ncKb, nckw, sndateStr);
					String unit = LoadPnInfos.pnToUnit.get(pnStr);
					ImportRowObj rowObj = new ImportRowObj(titleObj.getTitleNum(), rowCounter++,  pnStr , unit, storeCnt , dateStr, nckwName);
					rowObj.setSn(vSN);
					rowObj.setSnUnit(unit);
					sheetObj.addRow(rowObj);
				}
			}else{
				String unit = LoadPnInfos.pnToUnit.get(pnStr);
				ImportRowObj rowObj = new ImportRowObj(titleObj.getTitleNum(), rowCounter++,  pnStr , unit, storeCnt , dateStr, nckwName);
				sheetObj.addRow(rowObj);
			}
		}
		
	}
	
	public static final String ncBdcomKCZZ = "01";
	
	//U8数大
	private static void handleVSNForStore2(StorePNObj storePnObj, double u8count , Boolean enableSNManage) {
		
		String pnStr = storePnObj.getPn();
		
		U8kwPn_NcKbKw ncKwInfo =  storePnObj.getKWForVSN(  );
		String u8kb = ncKwInfo.getU8kb();
		String nckwName = ncKwInfo.getNckwName();
		String ncKb = ncKwInfo.getNcKb();
		String nckw = ncKwInfo.getNckw();
		
		ImportSheetObject sheetObj = u8kbToSheet.get(u8kb);
		ImportTitleObj titleObj = u8kbToTitle.get(u8kb);
		
		if( enableSNManage ){
			for(  int i=0; i<u8count ; i++  ){
				String vSN = InventedSerialNumberUtil.getInventedSerialNumber( ncBdcomKCZZ, ncKb, nckw, sndateStr);
				String unit = LoadPnInfos.pnToUnit.get(pnStr);
				ImportRowObj rowObj = new ImportRowObj(titleObj.getTitleNum(), rowCounter++,  pnStr , unit, 1 , dateStr, nckwName);
				rowObj.setSn(vSN);
				rowObj.setSnUnit(unit);
				sheetObj.addRow(rowObj);
			}
		}else{
			String unit = LoadPnInfos.pnToUnit.get(pnStr);
			ImportRowObj rowObj = new ImportRowObj(titleObj.getTitleNum(), rowCounter++,  pnStr , unit, u8count , dateStr, nckwName);
			sheetObj.addRow(rowObj);
		}
		
	}

	

	private static void writeU8(Map<String, U8PNObj> pnToU8Obj  ) {
		
		Set<Entry<String,U8PNObj>> entrySet = pnToU8Obj.entrySet();
		for(  Entry<String,U8PNObj> en : entrySet  ){
			String pnStr = en.getKey();
			U8PNObj value = en.getValue();
			Boolean enableSNManage = LoadPnInfos.pnInSNManage.get(pnStr);
			if( enableSNManage == null ){
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!  物料编码仍然不在NC ：：：" +  pnStr );
				continue;
			}
			
			Map<String, Double> kbToCount = value.getKbToCount();
			Set<Entry<String,Double>> entrySet2 = kbToCount.entrySet();
			for(   Entry<String,Double> en2 :   entrySet2){
				String u8kbKey = en2.getKey();
				Double tmpU8Cnt = en2.getValue();
				
				U8kwPn_NcKbKw ncKwByU8KwPN =  value.getKWForVSN(  u8kbKey );
				String u8kb = ncKwByU8KwPN.getU8kb();
				String nckwName = ncKwByU8KwPN.getNckwName();
				String ncKb = ncKwByU8KwPN.getNcKb();
				String nckw = ncKwByU8KwPN.getNckw();
				
				ImportSheetObject sheetObj = u8kbToSheet.get(u8kb);
				ImportTitleObj titleObj = u8kbToTitle.get(u8kb);
				if( enableSNManage ){
					for(  int i=0; i<tmpU8Cnt ; i++  ){
						String vSN = InventedSerialNumberUtil.getInventedSerialNumber( ncBdcomKCZZ, ncKb, nckw, sndateStr);
						String unit = LoadPnInfos.pnToUnit.get(pnStr);
						ImportRowObj rowObj = new ImportRowObj(titleObj.getTitleNum(), rowCounter++,  pnStr , unit, 1 , dateStr, nckwName);
						rowObj.setSn(vSN);
						rowObj.setSnUnit(unit);
						sheetObj.addRow(rowObj);
					}
				}else{
					String unit = LoadPnInfos.pnToUnit.get(pnStr);
					ImportRowObj rowObj = new ImportRowObj(titleObj.getTitleNum(), rowCounter++,  pnStr , unit, tmpU8Cnt , dateStr, nckwName);
					sheetObj.addRow(rowObj);
				}
			}
		}
		
	}


	public static U8kwPn_NcKbKw getNCKwByU8KwPN(String kw , String pn ){
		U8kwPn_NcKbKw u8kwPn_NcKbKw = LoadU8NCKwMap.getNCKWBykw_pn(kw, pn);
		return u8kwPn_NcKbKw;
	}
	

	private static void gather1(Map<String, RowData> distinctMap,
			Map<String, StorePNObj> pnToStoreObj, Map<String, U8PNObj> pnToU8Obj) {
		Set<Entry<String,RowData>> entrySet = distinctMap.entrySet();
		for(Entry<String,RowData>  en :  entrySet  ){
			RowData value = en.getValue();
			String cPn = value.getMaterialNum();
			String sn = value.getSnsStr();
			if( SpecialHandle.PNRemove.contains( cPn )  )  {
				continue;
			}
			ScanPnObj scanPnObj = pnToPNObj.get(cPn);
			if( scanPnObj ==null ){
				scanPnObj = new ScanPnObj(cPn);
				pnToPNObj.put(cPn, scanPnObj);
			}
			scanPnObj.addSN(value);
		}
		
		for(  String pn : SpecialHandle.PNRemove ){
			pnToStoreObj.remove(pn);
			pnToU8Obj.remove(pn);
		}		
	}
	
	
	
	
}
