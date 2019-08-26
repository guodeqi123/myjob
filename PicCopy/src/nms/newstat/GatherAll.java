package nms.newstat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nms.InOutObj;
import nms.KWObj;
import nms.RowData;
import nms.newstat.inout.LoadInOut;
import nms.stat.PnCountLoader;

public class GatherAll {
	
//	public static String basePath = "D:/ForBdcom/0stat";
	public static String basePath = "D:/ForBdcom/0stat1/扫描所有库位信息";

	public static List<RowData> srcData = new ArrayList<RowData>();
	
	public static Map<String,  StatRow > pnToCountEnableMap = new HashMap<String, StatRow>();
	public static Map<String,  StatRow > pnToCountDisableMap = new HashMap<String, StatRow>();
	
	public static Map<String,  StatRow > pnToCountU8Map = new HashMap<String, StatRow>();
	public static Map<String,  StatRow > pnToCountStoreMap = new HashMap<String, StatRow>();
	
	public static void main(String[] args) {
		LoadInOut.main( null );
		
		
		LoadPnInfos.loadPNStatus(LoadPnInfos.fpath);
		Set<String> pnInSNManage =  LoadPnInfos.pnInSNManage;
		Map<String, Integer> u8pnToCount = PnCountLoader.load( PnCountLoader.u8SrcFileInfo );
		Map<String, Integer> storepnToCount = PnCountLoader.load( PnCountLoader.srcFileInfo );
		
		doFileParse();
		
		Set<Entry<String,RowData>> entrySet = distinctMap.entrySet();
		for( Entry<String,RowData> en : entrySet    ){
			String sn = en.getKey();
			RowData value = en.getValue();
			String pn = value.getMaterialNum();
			if(   !pnInSNManage.contains(pn)  ){
				String errMsg = "物料编码没有启用SN管理：" + value.getPosNum() + " , "+value.getMaterialNum() +" , "+value.getSnsStr();
				errorNotManageSN.add(value);
				
				StatRow statRow = pnToCountDisableMap.get(pn);
				if( statRow==null  ){
					statRow = new StatRow(pn);
					statRow.addKw(value.getPosNum() );
				}
				pnToCountDisableMap.put(pn, statRow);
				statRow.addScan(1);
			}else{
				StatRow statRow = pnToCountEnableMap.get(pn);
				if( statRow==null  ){
					statRow = new StatRow(pn);
					pnToCountEnableMap.put(pn, statRow);
				}
				statRow.addKw(value.getPosNum() );
				statRow.addScan(1);
			}
		}
		
		gatherAllData(  pnToCountEnableMap , pnToCountDisableMap ,pnInSNManage ,  u8pnToCount , storepnToCount );
		
		// ++++++++++++++++++++++
		System.out.println( "SN总数统计" + srcData.size() );
		System.out.println(   "去重总数:"+distinctMap.size() );		
		
		printInfo( fileCountInfo ); 
		printInfo( parseErrorInfo );
		
		
//		int size = errorNotManageSN.size();
//		System.out.println(  "没有启用SN管理的 盘库SN ："+  size );
//		Set<String> noEnableSnPN = new HashSet<String>();
//		for( int i =0; i <size; i++    ){
//			RowData rowData = errorNotManageSN.get(i);
//			noEnableSnPN.add(rowData.getMaterialNum());
//		}
//		System.out.println(  "没有启用SN管理的 盘库PN ："+  noEnableSnPN.size()  );
//		for(  String str : noEnableSnPN  ){
//			System.out.println(   "PN :::  " +   str);
//		}
//		
//		System.out.println(   "errorSamePN1=========" + errorSamePN1.size()  );
//		System.out.println(   "errorSamePN2=========" + errorSamePN2.size()  );
//		System.out.println(   "errorSamePN3=========" + errorSamePN3.size()  );
//		for(String sss :  errorSamePN3  ){
//			System.out.println(   sss   );
//		}
		
		
		
		
	}

	private static void gatherAllData(Map<String, StatRow> pnToEnable,
			Map<String, StatRow> pnToDisable, Set<String> pnInSNManage,
			Map<String, Integer> u8pnToCount,
			Map<String, Integer> storepnToCount) {
		
		Set<Entry<String,StatRow>> entrySet = pnToEnable.entrySet();
		for(  Entry<String,StatRow> en : entrySet ){
			String cpn = en.getKey();
			StatRow value = en.getValue();
			Integer u8Count = u8pnToCount.remove(cpn);
			Integer storeCount = storepnToCount.remove(cpn);
			if(  u8Count!=null   ){
				value.setCountU8(u8Count);
			}
			if(  storeCount!=null   ){
				value.setCountStore(storeCount);
			}
		}
		
		entrySet = pnToDisable.entrySet();
		for(  Entry<String,StatRow> en : entrySet ){
			String cpn = en.getKey();
			StatRow value = en.getValue();
			Integer u8Count = u8pnToCount.remove(cpn);
			Integer storeCount = storepnToCount.remove(cpn);
			if(  u8Count!=null   ){
				value.setCountU8(u8Count);
			}
			if(  storeCount!=null   ){
				value.setCountStore(storeCount);
			}
		}
		
		Set<Entry<String,Integer>> entrySet2 = u8pnToCount.entrySet();
		for(  Entry<String,Integer>  u8Row :  entrySet2 ){
			String key = u8Row.getKey();
			Integer value = u8Row.getValue();
			StatRow statRow = new StatRow(key);
			statRow.setCountU8(value);
			Integer storeCount = storepnToCount.remove(key);
			if(  storeCount!=null   ){
				statRow.setCountStore(storeCount);
			}
			pnToCountU8Map.put(key, statRow);
		}
		
		entrySet2 = storepnToCount.entrySet();
		for(  Entry<String,Integer>  storeRow :  entrySet2 ){
			String key = storeRow.getKey();
			Integer value = storeRow.getValue();
			StatRow statRow = new StatRow(key);
			statRow.setCountStore(value);
			pnToCountStoreMap.put(key, statRow);
		}
		
		ArrayList<String> list1 = getMsg( pnToCountEnableMap );
		ArrayList<String> list2 = getMsg( pnToCountDisableMap );
		ArrayList<String> list3 = getMsg( pnToCountU8Map );
		ArrayList<String> list4 = getMsg( pnToCountStoreMap );
		ArrayList<String> msgs = new ArrayList<String>();
		
		msgs.add("PN , 扫描数量, U8数量 ,自盘数量");
		msgs.addAll(list1);
		msgs.addAll(list2);
		msgs.addAll(list3);
		msgs.addAll(list4);
		
		writeResult(msgs);
	}
	
	private static ArrayList<String> getMsg(	Map<String, StatRow> map) {
		
		ArrayList<String> ret = new ArrayList<String>();
		Set<Entry<String,StatRow>> entrySet = map.entrySet();
		for(  Entry<String,StatRow> en  : entrySet){ 
			String key = en.getKey();
			StatRow value = en.getValue();
			String msg =  key + " , "+value.getCountScan()+"  ,"+value.getCountU8()+"  , "  + value.getCountStore();
			ret.add(msg);
		}
		
		return ret;
	}

	public static void writeResult( List<String>  msg ){
		File file = new File("res/a.txt");
		try {
			FileOutputStream fos = new FileOutputStream(file);
			BufferedWriter br = new BufferedWriter(new OutputStreamWriter(fos));
			
			for(String sss : msg   ){
				try {
					br.write(sss+"\r\n");
//					System.out.println(  sss  ); 
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

	private static void printSamePN1(List<RowData> errorSamePN) {
		Map<String , StringBuilder> kwToSns = new HashMap<String, StringBuilder>();
		int e = errorSamePN.size();
		for(  int i=0; i<e ;i++ ) {
			RowData rd = errorSamePN.get(i);
			String kw = rd.getPosNum();
			String pn = rd.getMaterialNum();
			String snsStr = rd.getSnsStr();
			StringBuilder stringBuilder = kwToSns.get(kw);
			if(  stringBuilder ==null  ){
				stringBuilder = new StringBuilder();
				kwToSns.put(kw, stringBuilder);
			}
			stringBuilder.append(snsStr + " , ");
		}
		
		Set<Entry<String,StringBuilder>> entrySet = kwToSns.entrySet();
		for( Entry<String,StringBuilder> en : entrySet ){
			String kwStr = en.getKey();
			StringBuilder snStr = en.getValue();
			System.out.println(   "库位:" +  kwStr +"存在重复SN:"  +   snStr);
		}
		
	}

	public static Set<String>errorSamePN3 = new HashSet<String>();
	
	public static List<RowData> errorSamePN1 = new ArrayList<RowData>();  //同库位 同PN   SN same
	public static List<RowData> errorSamePN2 = new ArrayList<RowData>();  //  不同库 或 不同 PN  SN same
	public static List<RowData> errorNotManageSN = new ArrayList<RowData>();  //物料编码 目前没有启用SN
	
	public static List<String> fileCountInfo = new ArrayList<String>();  //文件个数统计 
	public static List<String> parseErrorInfo = new ArrayList<String>();//解析错误统计  
	
	public static Map<String , RowData> distinctMap = new HashMap<String, RowData>();
	
	public  static void doFileParse() {
		
		File file = new File(basePath);

		File[] listFiles = file.listFiles();
		for (File ff : listFiles) {
//			File[] excelList = ff.listFiles();
//			for( File ee :  excelList ){
				String name = ff.getName();
				String afp = ff.getAbsolutePath();
				Convertor2 ccc = new Convertor2(afp);
				
				Map<String, List<KWObj> > retA = ccc.parseExcel2( );
//				List<KWObj> kwErrA = retA.get("error");
//				List<String[]> errPnsA = ccc.writeSuccess2( kwAllSuccessA  , false);
				List<KWObj> kwAllSuccess = retA.get("success");
				int kwCount = kwAllSuccess.size();
				
				List<String> snList1 = new ArrayList<String>();
				Set<String> snSet1 = new HashSet<String>();
				for(  int kwi = 0 ; kwi<kwCount ; kwi++ ){
					KWObj kwObj = kwAllSuccess.get(kwi);
					List<RowData> datas = kwObj.getRowSnList();
					int size = datas.size();
					for (int i = 0; i < size; i++) {
						RowData rowData = datas.get(i);
						srcData.add(rowData);
						String snsStr = rowData.getSnsStr();
						snList1.add(snsStr);
						snSet1.add(snsStr);
					}
				}
				fileCountInfo.add( ccc.getSrcFilePath() +   "  统计个数, "+ snList1.size()  + " , 去重"  + snSet1.size());
//			}
		}
		
		int size = srcData.size();
		for(  int i=0; i<size ;i++  ){
			RowData rowData = srcData.get(i);
			String snsStr = rowData.getSnsStr();
			RowData srcRowData = distinctMap.get(snsStr);
			if( srcRowData!=null  ){
				String kw1 = srcRowData.getPosNum();
				String pn1 = srcRowData.getMaterialNum();
				
				String kw2 = rowData.getPosNum();
				String pn2 = rowData.getMaterialNum();
				
				if( kw1.equals(kw2)  && pn1.equals(pn2)  ){
					distinctMap.put(snsStr, rowData);
					String errMsg = " 序列号重复,涉及库位： "+kw1 +"  , SN : "+ snsStr ;
					errorSamePN1.add(rowData);
				}else{
					String errMsg = " ！！！序列号重复,涉及库位： "+kw1+"&" +kw2 +" , PN: "+pn1+"& " +pn2+"  , SN : "+ snsStr ;
					errorSamePN2.add(rowData);
					errorSamePN3.add(kw1+" , "+kw2+" , "+pn1+ " , " +pn2 + " , " + snsStr );
				}
			}else{
				distinctMap.put(snsStr, rowData);
			}
		}
		
		
		List<InOutObj> inList = LoadInOut.inList;
		for(  InOutObj in :inList  ) {
			RowData rowData = in.toRowData();
			distinctMap.put(rowData.getSnsStr(), rowData);
		}
		
		List<InOutObj> outList = LoadInOut.outList;
		for(  InOutObj out :outList  ) {
			String sn = out.getSn();
			distinctMap.remove(sn);
		}
		
	}
	

	private static void printInfo(List<String> list) {
		for(  String sss : list  ){
			System.out.println( sss  );
		}
		System.out.println(  "+------------------------+" );
	}
	
}
