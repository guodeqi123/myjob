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
import nms.newstat.pnc.LoadPNAmend;
import nms.newstat.pnc.LoadPNPrice;
import nms.newstat.pnc.LoadSNUsePN;
import nms.newstat.pnc.PNCompareObj;
import nms.newstat.pnc.PNInfoObj;
import nms.stat.PnCountLoader;
import nms.stat.StorePNObj;
import nms.stat.U8PNObj;

public class GatherAll {
	
//	public static String basePath = "D:/ForBdcom/0stat";
	
	public static Set<String> scanPNS = new HashSet<String>();
	public static List<RowData> srcData = new ArrayList<RowData>();
	
	public static Map<String,  StatRow > pnToCountEnableMap = new HashMap<String, StatRow>();
	public static Map<String,  StatRow > pnToCountDisableMap = new HashMap<String, StatRow>();
	
	public static Map<String,  StatRow > pnToCountU8Map = new HashMap<String, StatRow>();
	public static Map<String,  StatRow > pnToCountStoreMap = new HashMap<String, StatRow>();
	
	public static void main(String[] args) {
		LoadSNUsePN.load();
		LoadInOut.load();//���س����
		try { Thread.sleep(1000); } catch (InterruptedException e1) { }
		System.out.println(  "!!!�����������!!!" );
		LoadPNCompare.load();//�������϶��� 
		LoadPnInfos.loadPNStatus();  //�������ϵ���
		PnCountLoader.loadStoreData();//���زֿ���������
		PnCountLoader.loadU8Data();//����U8�������������
		LoadPNPrice.load();
		
		doFileParse();  //��������ɨ���ļ�
//		printInfo( fileCountInfo ); //��ӡɨ�뵥���ļ�ͳ����Ϣ
		printInfo( parseErrorInfo );//��ӡ�����쳣��Ϣ
		amendPN();//�������ϱ���
		
		gatherInAndOut();//�ϲ������
		System.out.println( "SN����ͳ��" + srcData.size() );
		System.out.println(   "ȥ������:"+distinctMap.size() );	
		Set<Entry<String,RowData>> entrySet = distinctMap.entrySet();
		for(  Entry<String,RowData> en : entrySet ){
			RowData value = en.getValue();
			String materialNum = value.getMaterialNum();
			scanPNS.add(materialNum);
		}
		System.out.println(   "PN����:"+scanPNS.size() );	
		
		doCheckAllPNAndGatherScanPNCount(); //��������̿���������ϱ���   �����ܸ���
		System.out.println( "doCheckAllPN!!!!!����SN ɨ�����ϱ������:"  + pnToCountEnableMap.size() );
		System.out.println( "doCheckAllPN!!!!!δ����SN ɨ�����ϱ������:"  + pnToCountDisableMap.size() );
		System.out.println( "doCheckAllPN!!!!!ɨ�����ϱ������:"  + ( pnToCountDisableMap.size()  + pnToCountEnableMap.size()) );
//		printErrorPN( errorNotManageSN );
		
		try {
			Map<String, StorePNObj> tmppnToKWCount = PnCountLoader.deepClone( PnCountLoader.pnToKWCount ) ;
			Map<String,U8PNObj > tmppnToU8Obj = PnCountLoader.deepClone( PnCountLoader.pnToU8Obj ) ;
			gatherAllData(  pnToCountEnableMap , pnToCountDisableMap , LoadPnInfos.pnInSNManage ,  tmppnToKWCount,  tmppnToU8Obj);
		} catch (Exception e) {
			e.printStackTrace();
		}  
		
	}
	
	private static void amendPN() {
		//������
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
			if(   !LoadPnInfos.pnInSNManage.contains(pn)  ){
				String errMsg = "���ϱ���û������SN����" + value.getPosNum() + " , "+value.getMaterialNum() +" , "+value.getSnsStr();
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

	private static <E> void gatherAllData(Map<String, StatRow> scanPNToEnable,
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
		
//		writeRes(allRight  ,"allRight.csv"  );
//		writeRes(other , "other.csv");
//		writeRes(errPN , "errPN.csv");
//		writeRes(noScanU8StoreSame , "noScanU8StoreSame.csv");
		
		WriteExcel.createExcel(allRight, "allRight_GDQ.xlsx");
		WriteExcel.createExcel(other, "other_GDQ.xlsx");
		WriteExcel.createExcel(errPN, "errPN_U8NotExist_GDQ.xlsx");
		WriteExcel.createExcel(noScanU8StoreSame, "noScanU8StoreSame_GDQ.xlsx");
		
		WriteExcel.createExcel(vituralPN, "vituralPN_GDQ.xlsx");
		WriteExcel.createExcel( NotINSNManage , "errPN_NotEnableSN_GDQ.xlsx");
		
		ArrayList<StatRow> arrayList = new ArrayList<StatRow>();
		arrayList.addAll(allRight);
		arrayList.addAll(other);
		arrayList.addAll(errPN);
		arrayList.addAll(noScanU8StoreSame);
		WriteExcel.createExcel( arrayList , "ALL_GDQ.xlsx");
		
	}
	
	private static void writeRes(ArrayList<StatRow> resList, String fname) {
		ArrayList<String> msgs = new ArrayList<String>();
		msgs.add("PN , ɨ������ ,�������� , U8���� , U8�䶯���� ,  ɨ���漰��λ, �����漰��λ , ������� , ����" );
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
	public static ArrayList<StatRow> errPN = new ArrayList<StatRow>();
	//û��ɨ�� �� U8������������ͬ
	public static ArrayList<StatRow> noScanU8StoreSame = new ArrayList<StatRow>();
	
	public static ArrayList<StatRow> allZero = new ArrayList<StatRow>();
	public static ArrayList<StatRow> vituralPN = new ArrayList<StatRow>();
	
	public static ArrayList<StatRow> NotINSNManage = new ArrayList<StatRow>();
	
	private static void getMsg(	Map<String, StatRow> map) {
		
		Set<Entry<String,StatRow>> entrySet = map.entrySet();
		for(  Entry<String,StatRow> en  : entrySet){ 
			String keyPN = en.getKey();
			StatRow value = en.getValue();
			int countScan = value.getCountScan();
			int countU8 = value.getCountU8();
			int countStore = value.getCountStore();
			int u8increase = value.getU8increase();
			String kwForStore = value.getStoreKwCountStr();
			if( "CBSWI-SWI0360A".equals(keyPN) ){
				System.out.println(  );
			}
			PNCompareObj pnCompareObj = LoadPNCompare.PNCompareMapU8NotExist.get(keyPN);
			
			if(   LoadPNPrice.vitualPN.contains(keyPN) ){
				vituralPN.add(value);
				continue;
			}
			
			if( "CBNNN-WAP0060A".equals(keyPN) ) {
				 System.out.println();
			}
			
			U8PNObj u8pnObj = PnCountLoader.pnToU8Obj.get(keyPN);
			boolean isExistInU8 = PnCountLoader.pnToU8Obj.containsKey(keyPN);
			
			if(  countScan<=0 && countU8<=0 && countStore<=0 ){
				allZero.add(value);
			}else if(  countScan<=0  && countU8==countStore ){
				noScanU8StoreSame.add(value);
			}else if( countScan == countU8 && countScan == countStore){
				allRight.add(value);
			}else if( !isExistInU8) {
				errPN.add(value);
			}else{
				other.add(value);
			}
			
			if( !LoadPnInfos.pnInSNManage.contains(keyPN)  && countScan>=0 ){
				NotINSNManage.add(value);
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
			String errMsg = "���ϱ���û������SN����," + value.getPosNum() + " , "+value.getMaterialNum() +" , "+value.getSnsStr();
			System.out.println( "printErrorPN!!!! , " + errMsg  );
		}
	}

	public static Set<String>errorSamePN3 = new HashSet<String>();
	
	public static List<RowData> errorSamePN1 = new ArrayList<RowData>();  //ͬ��λ ͬPN   SN same
	public static List<RowData> errorSamePN2 = new ArrayList<RowData>();  //  ��ͬ�� �� ��ͬ PN  SN same
	public static List<RowData> errorNotManageSN = new ArrayList<RowData>();  //���ϱ��� Ŀǰû������SN
	
	public static List<String> fileCountInfo = new ArrayList<String>();  //�ļ�����ͳ�� 
	public static List<String> parseErrorInfo = new ArrayList<String>();//��������ͳ��  
	
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
				fileCountInfo.add( ccc.getSrcFilePath() +   "  ͳ�Ƹ���, "+ snList1.size()  + " , ȥ��"  + snSet1.size());
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
					String errMsg = " ���к��ظ�,�漰��λ�� "+kw1 +"  , SN : "+ snsStr ;
					errorSamePN1.add(rowData);
				}else{
					String errMsg = " ���������к��ظ�,�漰��λ�� "+kw1+"&" +kw2 +" , PN: "+pn1+"& " +pn2+"  , SN : "+ snsStr ;
					errorSamePN2.add(rowData);
					errorSamePN3.add(kw1+" , "+kw2+" , "+pn1+ " , " +pn2 + " , " + snsStr );
				}
			}else{
				String toChangePN = LoadSNUsePN.snToPN.get(snsStr);
				String cPn = rowData.getMaterialNum();
				if( StringUtils.isEmpty(toChangePN)  ){
					distinctMap.put(snsStr, rowData);
				}else{
					if( cPn.equals(toChangePN) ){
						distinctMap.put(snsStr, rowData);
					}
				}
			}
		}
		
	}
	
	public static void gatherInAndOut(){
		List<InOutObj> inList = LoadInOut.inList;
		for(  InOutObj in :inList  ) {
			RowData rowData = in.toRowData();
			String snsStr = rowData.getSnsStr();
			String cpn = rowData.getMaterialNum();
			
			if( "".equals(snsStr) ){
				System.out.println(   );
			}
			
			if(  !FPath.isSNRight(snsStr) ){
				continue;
			}
			
			String toChangePN = LoadSNUsePN.snToPN.get(snsStr);
			if( StringUtils.isEmpty(toChangePN)  ){
				distinctMap.put(snsStr, rowData);
			}else{
				if( cpn.equals(toChangePN) ){
					distinctMap.put(snsStr, rowData);
				}
			}
		}
		
		List<InOutObj> outList = LoadInOut.outList;
		for(  InOutObj out :outList  ) {
			String sn = out.getSn();
			if( "".equals(sn) ){
				System.out.println(   );
			}
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
