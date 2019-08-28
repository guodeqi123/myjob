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

import org.apache.commons.lang.StringUtils;

import nms.InOutObj;
import nms.KWObj;
import nms.RowData;
import nms.newstat.inout.LoadInOut;
import nms.newstat.pnc.PNCompareObj;
import nms.stat.PnCountLoader;
import nms.stat.StorePNObj;
import nms.stat.U8PNObj;

public class GatherAll {
	
//	public static String basePath = "D:/ForBdcom/0stat";
	

	public static List<RowData> srcData = new ArrayList<RowData>();
	
	public static Map<String,  StatRow > pnToCountEnableMap = new HashMap<String, StatRow>();
	public static Map<String,  StatRow > pnToCountDisableMap = new HashMap<String, StatRow>();
	
	public static Map<String,  StatRow > pnToCountU8Map = new HashMap<String, StatRow>();
	public static Map<String,  StatRow > pnToCountStoreMap = new HashMap<String, StatRow>();
	
	public static void main(String[] args) {
		
		LoadInOut.load();//加载出入库
		LoadPNCompare.load();//加载物料对照 
		LoadPnInfos.loadPNStatus();  //加载物料档案
		PnCountLoader.loadStoreData();//加载仓库自盘数量
		PnCountLoader.loadU8Data();//加载U8数量并计算差异
		
		doFileParse();  //解析所有扫码文件
		printInfo( fileCountInfo ); //打印扫码单个文件统计信息
		printInfo( parseErrorInfo );//打印解析异常信息
		amendPN();//修正物料编码
		
		gatherInAndOut();//合并出入库
		System.out.println( "SN总数统计" + srcData.size() );
		System.out.println(   "去重总数:"+distinctMap.size() );		
		
		doCheckAllPNAndGatherScanPNCount(); //检查所有盘库出库结果物料编码   并汇总个数
		System.out.println( "doCheckAllPN!!!!!启用SN 扫码物料编码个数:"  + pnToCountEnableMap.size() );
		System.out.println( "doCheckAllPN!!!!!未启用SN 扫码物料编码个数:"  + pnToCountDisableMap.size() );
		System.out.println( "doCheckAllPN!!!!!扫码物料编码个数:"  + ( pnToCountDisableMap.size()  + pnToCountEnableMap.size()) );
//		printErrorPN( errorNotManageSN );
		
		gatherAllData(  pnToCountEnableMap , pnToCountDisableMap , LoadPnInfos.pnInSNManage ,  PnCountLoader.pnToKWCount , PnCountLoader.pnToU8Obj );
		
	}
	
	private static void amendPN() {
		//待修正
		Map<String, PNCompareObj> toAmend =  LoadPNCompare.PNCompareMapU8NotExist;
		
		Set<Entry<String, RowData>> entrySet = distinctMap.entrySet();
		for(  Entry<String, RowData> en : entrySet  ){
			RowData rd = en.getValue();
			String srcPn = rd.getMaterialNum();
			
			PNCompareObj pnCompareObj = toAmend.get(srcPn);
			if(   pnCompareObj==null  ||  StringUtils.isEmpty(  pnCompareObj.getToPn() )    ){
				continue;
			}
			rd.setMaterialNum( pnCompareObj.getToPn() );
		}
		
		List<InOutObj> inList = LoadInOut.inList;
		for(  InOutObj in :inList  ) {
			String pn = in.getPn();
			PNCompareObj pnCompareObj = toAmend.get(pn);
			if(   pnCompareObj==null  ||  StringUtils.isEmpty(  pnCompareObj.getToPn() )    ){
				continue;
			}
			in.setPn(  pnCompareObj.getToPn()  );
		}
	}

	public static void doCheckAllPNAndGatherScanPNCount(){
		Set<Entry<String,RowData>> entrySet = distinctMap.entrySet();
		for( Entry<String,RowData> en : entrySet    ){
			String sn = en.getKey();
			RowData value = en.getValue();
			String pn = value.getMaterialNum();
			if(   !LoadPnInfos.pnInSNManage.contains(pn)  ){
				String errMsg = "物料编码没有启用SN管理：" + value.getPosNum() + " , "+value.getMaterialNum() +" , "+value.getSnsStr();
				errorNotManageSN.add(value);
				
				StatRow statRow = pnToCountDisableMap.get(pn);
				if( statRow==null  ){
					statRow = new StatRow(pn);
					pnToCountDisableMap.put(pn, statRow);
				}
				statRow.addKw(value.getPosNum() );
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
		
	}

	private static void gatherAllData(Map<String, StatRow> scanPNToEnable,
			Map<String, StatRow> scanPNToDisable, Set<String> pnInSNManage,
			Map<String, StorePNObj> storePnToObjMap,
			Map<String, U8PNObj> u8Map ) {
		
		Set<Entry<String,StatRow>> entrySet = scanPNToEnable.entrySet();
		for(  Entry<String,StatRow> en :entrySet   ){
			String scanPn = en.getKey();
			StatRow value = en.getValue();
			StorePNObj storePNObj = storePnToObjMap.remove(scanPn);
			if(  storePNObj!=null  ){
				int sumCount = storePNObj.getSumCount();
				value.setCountStore(sumCount);
				String kwCountStr = storePNObj.getKWCountStr();
				value.setStoreKwCountStr(kwCountStr);
			}
			U8PNObj u8pnObj = u8Map.remove(scanPn);
			if( u8pnObj!=null ){
				int newValue = u8pnObj.getNewValue();
				int increment = u8pnObj.getIncrement();
				value.setCountU8(newValue);
				value.setU8increase(increment);
			}
		}
		
		entrySet = scanPNToDisable.entrySet();
		for(  Entry<String,StatRow> en :entrySet   ){
			String scanPn = en.getKey();
			StatRow value = en.getValue();
			StorePNObj storePNObj = storePnToObjMap.remove(scanPn);
			if(  storePNObj!=null  ){
				int sumCount = storePNObj.getSumCount();
				value.setCountStore(sumCount);
				String kwCountStr = storePNObj.getKWCountStr();
				value.setStoreKwCountStr(kwCountStr);
			}
			U8PNObj u8pnObj = u8Map.remove(scanPn);
			if( u8pnObj!=null ){
				int newValue = u8pnObj.getNewValue();
				int increment = u8pnObj.getIncrement();
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
			int sumCount = value.getSumCount();
			statRow.setStoreKwCountStr(kwCountStr);
			statRow.setCountStore(sumCount);
			pnToCountStoreMap.put(cPn, statRow);
			
			U8PNObj u8pnObj = u8Map.remove(cPn);
			if( u8pnObj!=null ){
				int newValue = u8pnObj.getNewValue();
				int increment = u8pnObj.getIncrement();
				statRow.setCountU8(newValue);
				statRow.setU8increase(increment);
			}
		}
		
		Set<Entry<String,U8PNObj>> u8Ens = u8Map.entrySet();
		for(  Entry<String,U8PNObj> en : u8Ens  ){
			String cPn = en.getKey();
			U8PNObj value = en.getValue();
			StatRow statRow = new StatRow(cPn);
			int newValue = value.getNewValue();
			int increment = value.getIncrement();
			statRow.setCountU8(newValue);
			statRow.setU8increase(increment);
			
			pnToCountU8Map.put(cPn, statRow);
		}
		
		
		
		getMsg( pnToCountEnableMap );
		getMsg( pnToCountDisableMap );
		getMsg( pnToCountStoreMap );
		getMsg( pnToCountU8Map );
		
		writeRes(allRight  ,"allRight.csv"  );
		writeRes(other , "other.csv");
		writeRes(errPN , "errPN.csv");
		writeRes(noScanU8StoreSame , "noScanU8StoreSame.csv");
		
	}
	
	private static void writeRes(ArrayList<StatRow> resList, String fname) {
		ArrayList<String> msgs = new ArrayList<String>();
		msgs.add("PN , 扫描数量 ,自盘数量 , U8数量 , U8变动数量 ,  扫码涉及库位, 自盘涉及库位" );
		for( StatRow value :  resList  ){
			String pn = value.getPn();
			int countStore = value.getCountStore();
			int countU8 = value.getCountU8();
			int u8increase = value.getU8increase();
			String kwForStore = value.getStoreKwCountStr();
			if( kwForStore ==null ){
				kwForStore = "";
			}
			String kwStrs = getKWInfo(value.getKwCountMap());
			String msg =  pn + " , "+value.getCountScan()+"  ,"+countStore +"  , "  + countU8 + ","  + u8increase +","+kwStrs + "," +kwForStore ;
			
			msgs.add(msg);
		}
		writeResult(msgs , fname);
	}

	public static ArrayList<StatRow> allRight = new ArrayList<StatRow>();
	public static ArrayList<StatRow> allZero = new ArrayList<StatRow>();
	public static ArrayList<StatRow> other = new ArrayList<StatRow>();
	public static ArrayList<StatRow> errPN = new ArrayList<StatRow>();
	public static ArrayList<StatRow> noScanU8StoreSame = new ArrayList<StatRow>();
	
	private static void getMsg(	Map<String, StatRow> map) {
		
		Set<Entry<String,StatRow>> entrySet = map.entrySet();
		for(  Entry<String,StatRow> en  : entrySet){ 
			String key = en.getKey();
			StatRow value = en.getValue();
			int countScan = value.getCountScan();
			int countU8 = value.getCountU8();
			int countStore = value.getCountStore();
			int u8increase = value.getU8increase();
			String kwForStore = value.getStoreKwCountStr();
			if( "CBSWI-SWI0360A".equals(key) ){
				System.out.println(  );
			}
			PNCompareObj pnCompareObj = LoadPNCompare.PNCompareMapU8NotExist.get(key);
			
			if(  countScan<=0 && countU8<=0 && countStore<=0 ){
				allZero.add(value);
			}else if(  countScan<=0  && countU8==countStore ){
				noScanU8StoreSame.add(value);
			}else if( countScan == countU8 && countScan == countStore){
				allRight.add(value);
			}else if( pnCompareObj!=null && StringUtils.isEmpty(pnCompareObj.getToPn()) ) {
				errPN.add(value);
			}else{
				other.add(value);
			}
			
			
			
			
		}
		
	}

	private static String getKWInfo(Map<String, Integer> kwCountMap) {
		StringBuilder sb = new StringBuilder();
		Set<Entry<String,Integer>> entrySet = kwCountMap.entrySet();
		for(  Entry<String,Integer> en : entrySet  ){
			String key = en.getKey();
			Integer value = en.getValue();
			sb.append(key+"="+ value + "  ");
		}
		
		return sb.toString();
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
		
	}
	
	public static void gatherInAndOut(){
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
