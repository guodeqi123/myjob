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
import nms.newstat.pnc.LoadPNAmend;
import nms.newstat.pnc.LoadPNPrice;
import nms.newstat.pnc.LoadSNUsePN;
import nms.newstat.pnc.PNInfoObj;
import nms.newstat.tonc.ToNCExcel;
import nms.newstat.tonc2.LoadNCKWInfo;
import nms.newstat.tonc34.ImportBdcom30;
import nms.newstat.tonc34.SpecialHandle;
import nms.stat.PnCountLoader;
import nms.stat.StorePNObj;
import nms.stat.U8PNObj;

import org.apache.commons.lang.StringUtils;

public class GatherAll {
	
//	public static String basePath = "D:/ForBdcom/0stat";
	
	public static Set<String> scanPNS = new HashSet<String>();
	public static List<RowData> srcData = new ArrayList<RowData>();
	
	public static Map<String,  StatRow > scanPNToCountMap = new HashMap<String, StatRow>();
	public static Map<String,  StatRow > pnToCountU8Map = new HashMap<String, StatRow>();
	public static Map<String,  StatRow > pnToCountStoreMap = new HashMap<String, StatRow>();
	
	public static void main(String[] args) {
		// 加载NC库位与U8库位 映射
		LoadNCKWInfo.load();
		
		LoadSNUsePN.load();
		LoadInOut.load();//加载出入库
		try { Thread.sleep(1000); } catch (InterruptedException e1) { }
		System.out.println(  "!!!出入库加载完毕!!!" );
		LoadPNCompare.load();//加载物料对照 
		LoadPnInfos.loadPNStatus();  //加载物料档案
		PnCountLoader.loadStoreData();//加载仓库自盘数量
		PnCountLoader.loadU8Data();//加载U8数量并计算差异
		LoadPNPrice.load();
		
		doFileParse();  //解析所有扫码文件
//		printInfo( fileCountInfo ); //打印扫码单个文件统计信息
		printInfo( parseErrorInfo );//打印解析异常信息
		amendPN();//修正物料编码
		
		gatherInAndOut();//合并出入库
		System.out.println( "SN总数统计" + srcData.size() );
		System.out.println(   "去重总数:"+distinctMap.size() );	
		Set<Entry<String,RowData>> entrySet = distinctMap.entrySet();
		for(  Entry<String,RowData> en : entrySet ){
			RowData value = en.getValue();
			String materialNum = value.getMaterialNum();
			scanPNS.add(materialNum);
		}
		System.out.println(   "PN总数:"+scanPNS.size() );	
		
		
		doCheckAllPNAndGatherScanPNCount(); //检查所有盘库出库结果物料编码 并汇总个数
//		printErrorPN( errorNotManageSN );
		
		try {
			Map<String, StorePNObj> tmppnToStoreObj = PnCountLoader.deepClone( PnCountLoader.pnToKWCount ) ;
			Map<String,U8PNObj > tmppnToU8Obj = PnCountLoader.deepClone( PnCountLoader.pnToU8Obj ) ;
//			gatherAllData(  scanPNToCountMap  , LoadPnInfos.pnInSNManage ,  tmppnToKWCount,  tmppnToU8Obj);
			
			ImportBdcom30.doExport(  distinctMap ,   tmppnToStoreObj    , tmppnToU8Obj );
			
		} catch (Exception e) {
			e.printStackTrace();
		}  
		
	}
	
	private static void amendPN() {
		//待修正
//		Map<String, PNCompareObj> toAmend =  LoadPNCompare.PNCompareMapU8NotExist;
		Map<String, String> toAmend =  LoadPNAmend.srcPNToPN;
		
		Set<Entry<String, RowData>> entrySet = distinctMap.entrySet();
		for(  Entry<String, RowData> en : entrySet  ){
			RowData rd = en.getValue();
			String srcPn = rd.getMaterialNum();
			
			String toPn = toAmend.get(srcPn);
			if(    StringUtils.isEmpty(  toPn)    ){
				continue;
			}
			rd.setMaterialNum( toPn);
		}
		
		List<InOutObj> inList = LoadInOut.inList;
		for(  InOutObj in :inList  ) {
			String pn = in.getPn();
			String toPnIn = toAmend.get(pn);
			if(    StringUtils.isEmpty(   toPnIn  )    ){
				continue;
			}
			in.setPn(  toPnIn );
		}
	}

	public static void doCheckAllPNAndGatherScanPNCount(){
		Set<Entry<String,RowData>> entrySet = distinctMap.entrySet();
		for( Entry<String,RowData> en : entrySet    ){
			String sn = en.getKey();
			RowData value = en.getValue();
			String pn = value.getMaterialNum();
			
			StatRow statRow = scanPNToCountMap.get(pn);
			if( statRow==null  ){
				statRow = new StatRow(pn);
				scanPNToCountMap.put(pn, statRow);
			}
			statRow.addKw(value.getPosNum() );
			statRow.addScan(1);
		}
		
	}

	private static <E> void gatherAllData(Map<String, StatRow> scanPNToEnable, Map<String,Boolean> pnInSNManage,
			Map<String, StorePNObj> storePnToObjMap,
			Map<String, U8PNObj> u8Map ) {
		
		Set<Entry<String,StatRow>> entrySet = scanPNToEnable.entrySet();
		for(  Entry<String,StatRow> en :entrySet   ){
			String scanPn = en.getKey();
			StatRow value = en.getValue();
			StorePNObj storePNObj = storePnToObjMap.remove(scanPn);
			if(  storePNObj!=null  ){
				double sumCount = storePNObj.getSumCount();
				value.setCountStore(sumCount);
				String kwCountStr = storePNObj.getKWCountStr();
				value.setStoreKwCountStr(kwCountStr);
			}
			U8PNObj u8pnObj = u8Map.remove(scanPn);
			if( u8pnObj!=null ){
				double newValue = u8pnObj.getNewValue();
				double increment = u8pnObj.getIncrement();
				value.setCountU8(newValue);
				value.setU8increase(increment);
			}
		}
		
		Set<Entry<String,StorePNObj>> storeEns = storePnToObjMap.entrySet();
		for(  Entry<String,StorePNObj> en :  storeEns ){
			String cPn = en.getKey();
			StorePNObj value = en.getValue();
			StatRow statRow = new StatRow(cPn);
			String kwCountStr = value.getKWCountStr();
			double sumCount = value.getSumCount();
			statRow.setStoreKwCountStr(kwCountStr);
			statRow.setCountStore(sumCount);
			pnToCountStoreMap.put(cPn, statRow);
			
			U8PNObj u8pnObj = u8Map.remove(cPn);
			if( u8pnObj!=null ){
				double newValue = u8pnObj.getNewValue();
				double increment = u8pnObj.getIncrement();
				statRow.setCountU8(newValue);
				statRow.setU8increase(increment);
			}
		}
		
		Set<Entry<String,U8PNObj>> u8Ens = u8Map.entrySet();
		for(  Entry<String,U8PNObj> en : u8Ens  ){
			String cPn = en.getKey();
			U8PNObj value = en.getValue();
			StatRow statRow = new StatRow(cPn);
			double newValue = value.getNewValue();
			double increment = value.getIncrement();
			statRow.setCountU8(newValue);
			statRow.setU8increase(increment);
			
			pnToCountU8Map.put(cPn, statRow);
		}
		
		
		
		getMsg( scanPNToCountMap );
		getMsg( pnToCountStoreMap );
		getMsg( pnToCountU8Map );
		
		WriteExcel.createExcel(allRight, "allRight_GDQ.xlsx");
		WriteExcel.createExcel(noScanU8StoreSame, "noScanU8StoreSame_GDQ.xlsx");
		WriteExcel.createExcel(other, "other_GDQ.xlsx");
		WriteExcel.createExcel(InNCNotEnableSN, "InNCNotEnableSN_GDQ.xlsx");
		
		WriteExcel.createExcel(vituralPN, "vituralPN_GDQ.xlsx");
		WriteExcel.createExcel( NotINNC , "NotINNC_GDQ.xlsx");
		
		ArrayList<StatRow> arrayList = new ArrayList<StatRow>();
		arrayList.addAll(allRight);
		arrayList.addAll(noScanU8StoreSame);
		arrayList.addAll(other);
		arrayList.addAll(InNCNotEnableSN);
		WriteExcel.createExcel( arrayList , "ALL_GDQ.xlsx");
		
	}
	
	private static void writeRes(ArrayList<StatRow> resList, String fname) {
		ArrayList<String> msgs = new ArrayList<String>();
		msgs.add("PN , 扫描数量 ,自盘数量 , U8数量 , U8变动数量 ,  扫码涉及库位, 自盘涉及库位 , 存货名称 , 单价" );
		for( StatRow value :  resList  ){
			String pn = value.getPn();
			double countStore = value.getCountStore();
			double countU8 = value.getCountU8();
			double u8increase = value.getU8increase();
			String kwForStore = value.getStoreKwCountStr();
			if( kwForStore ==null ){
				kwForStore = "";
			}
			String kwStrs = getKWInfo(value.getKwCountMap());
			String msg =  pn + " , "+value.getCountScan()+"  ,"+countStore +"  , "  + countU8 + ","  + u8increase +","+kwStrs + "," +kwForStore ;
			PNInfoObj pnInfoObj = LoadPNPrice.pnToObj.get(pn);
			String pnName = "";
			double pnPrice = -1;
			if( pnInfoObj != null ){
				pnName = pnInfoObj.getPnName();
				pnPrice = pnInfoObj.getPrice();
			}
			msg = msg + " , " + pnName + " , "  +pnPrice;
			
			msgs.add(msg);
		}
		writeResult(msgs , fname);
	}

	public static ArrayList<StatRow> allRight = new ArrayList<StatRow>();
	public static ArrayList<StatRow> other = new ArrayList<StatRow>();
	public static ArrayList<StatRow> InNCNotEnableSN = new ArrayList<StatRow>();
	public static ArrayList<StatRow> noScanU8StoreSame = new ArrayList<StatRow>();	//没有扫码 ， U8与自盘数量相同
	
	public static ArrayList<StatRow> allZero = new ArrayList<StatRow>();
	public static ArrayList<StatRow> vituralPN = new ArrayList<StatRow>();
	
	public static ArrayList<StatRow> NotINNC = new ArrayList<StatRow>();
	
	private static void getMsg(	Map<String, StatRow> map) {
		
		Set<Entry<String,StatRow>> entrySet = map.entrySet();
		for(  Entry<String,StatRow> en  : entrySet){ 
			String keyPN = en.getKey();
			StatRow value = en.getValue();
			double countScan = value.getCountScan();
			double countU8 = value.getCountU8();
			double countStore = value.getCountStore();
			double u8increase = value.getU8increase();
			String kwForStore = value.getStoreKwCountStr();

			if( "CBOEM-PON0152B".equals(keyPN) ){
				System.out.println(  );
			}
			boolean isExistInNC = LoadPnInfos.pnInSNManage.containsKey(keyPN);
			Boolean isEnableInNC = LoadPnInfos.pnInSNManage.get(keyPN);
			if(  !isExistInNC  && ( countScan>0 || countU8>0 || countStore>0) ){
				NotINNC.add(value);
			}
			
			if(   LoadPNPrice.vitualPN.contains(keyPN) ){
				vituralPN.add(value);
				continue;
			}
			if(  countScan<=0 && countU8<=0 && countStore<=0 ){
				continue;
			}
			
			if (  countScan<=0 && countU8<=0 && countStore<=0 ) {
				allZero.add(value);
			}else if (  countScan<=0  && countU8==countStore ){
				noScanU8StoreSame.add(value);
			} else if ( isExistInNC && !isEnableInNC && countScan>0 ){
				InNCNotEnableSN.add(value);
			} else if ( countScan == countU8 && countScan == countStore){
				allRight.add(value);
			} else {
				other.add(value);
			}
			
		}
		
	}

	public static String getKWInfo(Map<String, Integer> kwCountMap) {
		StringBuilder sb = new StringBuilder();
		Set<Entry<String,Integer>> entrySet = kwCountMap.entrySet();
		for(  Entry<String,Integer> en : entrySet  ){
			String key = en.getKey();
			Integer value = en.getValue();
			sb.append(key+"="+ value + " ");
		}
		
		return sb.toString().trim();
	}

	public static void writeResult( List<String>  msg  , String fname){
		File file = new File("res/" + fname);
		if( file.exists()){
			file.delete();
		}
		try {
			file.createNewFile();
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

	private static void printErrorPN( List<RowData> errorPN  ) {
		Map<String , StringBuilder> kwToSns = new HashMap<String, StringBuilder>();
		int e = errorPN.size();
		for(  int i=0; i<e ;i++ ) {
			RowData value = errorPN.get(i);
			String errMsg = "物料编码没有启用SN管理：," + value.getPosNum() + " , "+value.getMaterialNum() +" , "+value.getSnsStr();
			System.out.println( "printErrorPN!!!! , " + errMsg  );
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
		
		File file = new File(FPath.scanbasePath);

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
						String snsStr = rowData.getSnsStr();
						String materialNum = rowData.getMaterialNum();
						if(  !FPath.isSNRight(snsStr) ){
							continue;
						}
						srcData.add(rowData);
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
			String cUsePN = rowData.getMaterialNum();
			String toChangePN = LoadSNUsePN.snToPN.get(snsStr);
			if( !StringUtils.isEmpty(toChangePN)  &&  !toChangePN.equals(cUsePN) ){
				continue;
			}
			

			
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
				}else if( pn1.equals(pn2) ){
					int compareTo = kw1.compareTo(kw2);
					if( compareTo <=0  ){
						distinctMap.put(snsStr, srcRowData);
					}else{
						distinctMap.put(snsStr, rowData);
					}
				}else{
					String errMsg = " ！！！序列号重复,涉及库位： "+kw1+"&" +kw2 +" , PN: "+pn1+"& " +pn2+"  , SN : "+ snsStr ;
					errorSamePN2.add(rowData);
					errorSamePN3.add(kw1+" , "+kw2+" , "+pn1+ " , " +pn2 + " , " + snsStr );
				}
			}else{
				distinctMap.put(snsStr, rowData);
			}
		}
		
	}
	
	public static void gatherInAndOut(){
		List<InOutObj> inList = LoadInOut.inList;
		for(  InOutObj in :inList  ) {
			RowData rowData = in.toRowData();
			String snsStr = rowData.getSnsStr();
			String cpn = rowData.getMaterialNum();
			
//			if( "".equals(snsStr) ){
//				System.out.println(   );
//			}
//			if( "OT-2200-GP".equals(cpn)  || "C".equals(cpn)  ){
//				System.out.println(   );
//			}
			
			String toChangePN = LoadSNUsePN.snToPN.get(snsStr);
			if( !StringUtils.isEmpty(toChangePN)  &&  !toChangePN.equals(cpn) ){
				continue;
			}
			
			if(  !FPath.isSNRight(snsStr) ){
				continue;
			}
			
			RowData existRd = distinctMap.get(snsStr);
			if( existRd!=null  ){
				String kw1 = existRd.getPosNum();
				String pn1 = existRd.getMaterialNum();
				
				String kw2 = rowData.getPosNum();
				String pn2 = rowData.getMaterialNum();
				if( kw1.equals(kw2)  && pn1.equals(pn2)  ){
					distinctMap.put(snsStr, rowData);
				}else if( pn1.equals(pn2) ){
					int compareTo = kw1.compareTo(kw2);
					if( compareTo <=0  ){
						distinctMap.put(snsStr, existRd);
					}else{
						distinctMap.put(snsStr, rowData);
					}
				}else{
					distinctMap.put(snsStr, rowData);
				}
			} else {
				distinctMap.put(snsStr, rowData);
			}
		}
		
		List<InOutObj> outList = LoadInOut.outList;
		for(  InOutObj out :outList  ) {
			String sn = out.getSn();
			distinctMap.remove(sn);
		}
	}
	

	private static void printInfo(List<String> list) {
		for(  String sss : list  ){
			if( StringUtils.isEmpty(sss) ){
				 continue;
			}
			System.out.println( sss  );
		}
		System.out.println(  "+------------------------+" );
	}
	
}
