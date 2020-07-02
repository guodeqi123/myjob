package nms.t1023;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nms.newstat.Convertor2;
import nms.newstat.FPath;
import nms.stat.PnCountLoader;
import nms.t1023.OrderDetail.SubPNBean;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * 
 * 
 * @author user
 *
 */
public class Check1 {
	
//	public static String fpath = "D:/ForBdcom/1023/����Ʒ��ⵥ�б�20191023.XLS";
//	public static Map<String, OrderBean> oNumORNumToBean = new HashMap<String, OrderBean>();
	
	public static final String fpath2 = "D:/ForBdcom/1023/����������������20191023.XLS";
	public static Map<String, OrderStatusBean> oNumORNumToSBean = new HashMap<String, OrderStatusBean>();
	
	public static final String fpath3 = "D:/ForBdcom/1023/�����Ӽ�20191023.xls";
	public static Map<String, OrderDetail> MAP4 = new HashMap<String, OrderDetail>();
	
	public static final String fpathU8 = "D:/ForBdcom/1023/U8�Ӽ���Ϣ.xlsx";
	public static Map<String, U8SubInfo> ORNumToSecProd = new HashMap<String, U8SubInfo>();
	
	private static void loadU8Sub() {
		Workbook wb = FPath.getWB(fpathU8);
		Sheet sheetAt = wb.getSheetAt(0);
		int lastRowNum = sheetAt.getLastRowNum();
		Row oneRow = null;
		for (int i = 1; i <= lastRowNum; i++) {
			oneRow = sheetAt.getRow(i);
			if (oneRow == null) {
				continue;
			}
			
			String oNum = Convertor2.getCellValue( oneRow.getCell( 0 ) );
			String rNum = Convertor2.getCellValue( oneRow.getCell( 1 ) );
			String pn = Convertor2.getCellValue( oneRow.getCell( 2 ) );
			String subRNum = Convertor2.getCellValue( oneRow.getCell( 3 ) );
			String subPN = Convertor2.getCellValue( oneRow.getCell( 4 ) );
			String isSecProd = Convertor2.getCellValue( oneRow.getCell( 5 ) );
			
			String key = U8SubInfo.getKey(oNum, rNum);
			U8SubInfo u8SubInfo = ORNumToSecProd.get(key);
			if( u8SubInfo == null ){
				u8SubInfo = new U8SubInfo(oNum, rNum, pn);
				u8SubInfo.putSubPN(subPN, subRNum, isSecProd);
				ORNumToSecProd.put(key, u8SubInfo);
			}else{
				u8SubInfo.putSubPN(subPN, subRNum, isSecProd);
			}
			
		}
	}
	
	public static void check2(){
		
		Workbook wb = FPath.getWB(fpath2);
		Sheet sheetAt = wb.getSheetAt(0);
		int lastRowNum = sheetAt.getLastRowNum();
		Row oneRow = null;
		
		int orderNumCol = 2;//s  ����������
		int orderRowNumCol = 3;//t  �к�
		int pnCol = 7;
		int typeDescCol = 11;
		int produceCountCol = 15;
		int finishedCountCol = 24;
		int unFinishedCountCol = 25;
		
		for (int i = 1; i < lastRowNum; i++) {
			oneRow = sheetAt.getRow(i);
			if (oneRow == null) {
				continue;
			}
			
			Cell orderNumCell = oneRow.getCell(orderNumCol);
			Cell orderRowNumCell = oneRow.getCell(orderRowNumCol);
			Cell pnCell = oneRow.getCell(pnCol);
			Cell typeDescCell = oneRow.getCell(typeDescCol);
			Cell produceCountCell = oneRow.getCell(produceCountCol);
			Cell finishedCountCell = oneRow.getCell(finishedCountCol);
			Cell unFinishedCountCell = oneRow.getCell(unFinishedCountCol);

			String oNum = Convertor2.getCellValue(orderNumCell);
			String rNum = Convertor2.getCellValue(orderRowNumCell);
			String pnStr = Convertor2.getCellValue(pnCell);
			String typeDesc = Convertor2.getCellValue(typeDescCell);
			Double sumNum = Double.parseDouble( Convertor2.getCellValue(produceCountCell) ) ;
			Double fiNum = Double.parseDouble( Convertor2.getCellValue(finishedCountCell) )  ;
			Double unFiNum = Double.parseDouble( Convertor2.getCellValue(unFinishedCountCell) ) ;
			
			if( StringUtils.isEmpty(oNum) || StringUtils.isEmpty(rNum) ){
				continue;
			}
			
			String key = OrderStatusBean.getKey(oNum , rNum);
			OrderStatusBean orderStatusBean = oNumORNumToSBean.get(key);
			if( orderStatusBean == null  ){
				orderStatusBean = new OrderStatusBean(oNum, rNum, pnStr, typeDesc, sumNum, fiNum, unFiNum);
				oNumORNumToSBean.put(key, orderStatusBean);
			}else{
				System.out.println(  "�����ظ��Ķ������к�  :"+  key  );
			}
			
			try {
				orderStatusBean.checkSelf();
			} catch (Exception e) {
				System.out.println( "i:" + (i+1) +"  "+ e.getMessage()  ); 
			}
		}
	}
	
	private static void loadSubInfo() {
		Workbook wb = FPath.getWB(fpath3);
		Sheet sheetAt = wb.getSheetAt(0);
		int lastRowNum = sheetAt.getLastRowNum();
		Row oneRow = null;
		for (int i = 1; i <= lastRowNum; i++) {
			oneRow = sheetAt.getRow(i);
			if (oneRow == null) {
				continue;
			}
			
			String oNum = Convertor2.getCellValue( oneRow.getCell( 2 ) );
			String rNum = Convertor2.getCellValue( oneRow.getCell( 3 ) );
			String pn = Convertor2.getCellValue( oneRow.getCell( 7 ) );
			double count = Double.parseDouble(  Convertor2.getCellValue( oneRow.getCell( 4 ) ) );
			
			String subPN = Convertor2.getCellValue( oneRow.getCell( 9 ) );
			double needcount = Double.parseDouble( Convertor2.getCellValue( oneRow.getCell( 11 ) ) );
			double havecount = Double.parseDouble( Convertor2.getCellValue( oneRow.getCell( 12 ) ) );
			
			String key = OrderBean.getKey(oNum, rNum);
			OrderDetail orderDetail = MAP4.get(key);
			if( orderDetail == null  ){
				orderDetail = new OrderDetail(oNum, rNum, pn, count);
				MAP4.put(key, orderDetail);
			} else {
				String pn2 = orderDetail.getPn();
				if( !pn2.equals(pn)  ){
					System.out.println(  "�������к���ͬ��ĸ�����벻ͬ oNum:" +oNum +" rNum :"+ rNum  +" " + pn+ " "+pn2 );
				}
//				orderDetail.setCount( orderDetail.getCount() + count );
			}
			SubPNBean subPNBean = new OrderDetail.SubPNBean( i, subPN, needcount, havecount);
			orderDetail.addSub(subPNBean);
		}
	}

	public static Map<String	,SubPNBean > lineToBean = new HashMap<String, OrderDetail.SubPNBean>();
	public static Map<String	,SubPNBean > lineToBeanRepeat = new HashMap<String, OrderDetail.SubPNBean>();
	public static void checkSubInfo() {
		int counter1 = 0;
		int counter2 = 0;
		int counter3 = 0;
		int counter4 = 0;
		int counter5 = 0;
		int counter6 = 0;
		Set<Entry<String,OrderDetail>> entrySet = MAP4.entrySet();
		for(  Entry<String,OrderDetail>  en : entrySet ){
			String key = en.getKey();
			OrderDetail value = en.getValue();
			double pcount = value.getCount();
			
			Map<String, SubPNBean> subMap = value.getSubMap();
			Set<Entry<String,SubPNBean>> entrySet2 = subMap.entrySet();
			for(  Entry<String,SubPNBean>  en2 : entrySet2 ){
				String subPn = en2.getKey();
				SubPNBean subPnBean = en2.getValue();
				lineToBean.put( subPnBean.getLineNo() +"" , subPnBean );
				
				double haveCount = subPnBean.getHaveCount();
				double needCount = subPnBean.getNeedCount();
				U8SubInfo u8SubInfo = ORNumToSecProd.get(key);
				if( u8SubInfo!=null && u8SubInfo.subPNumSecProd.contains(subPn)  ){ 
					subPnBean.setRemark("��Ϊ����Ʒ");
					counter1++;
					continue;
				}
				Double double1 = new Double(haveCount);
				Double double2 = new Double(needCount);
				if( !isInt(double1) || !isInt(double2)  ){
					subPnBean.setRemark("������Ϊ����");
					counter5++;
					continue;
				}
				if( needCount%pcount != 0  ){
					subPnBean.setRemark("�����Ӽ��������Ƕ�������������");
					counter2++;
					continue;
				}
				OrderStatusBean orderStatusBean = oNumORNumToSBean.get(key);
				if( orderStatusBean == null  ){
					subPnBean.setRemark("���������������������޷��ҵ�");
					counter3++;
					continue;
				}
				if( needCount == 0  ){
					subPnBean.setRemark("Ӧ������Ϊ0");
					counter4++;
					continue;
				}
				
				int multiple = (int) (needCount/pcount);
				int fiNum = orderStatusBean.getFiNum().intValue();
				value.setDoneCount(fiNum);
				
				int useCount =  fiNum * multiple;
				int unUseCount = (int) (haveCount - useCount);
				subPnBean.setUseCount(useCount);
				subPnBean.setUnusedCount(unUseCount);
				if( unUseCount<0  ){
					subPnBean.setRemark("ʹ������������������");
					counter6++;
				}else{
					subPnBean.setRemark( "����ȷ����" );
				}
			}
			Map<String, SubPNBean> repeatORSubPN = value.getRepeatORSubPN();
			Set<Entry<String,SubPNBean>> entrySet3 = repeatORSubPN.entrySet();
			U8SubInfo u8SubInfo = ORNumToSecProd.get(key);
			for( Entry<String,SubPNBean> en3 : entrySet3  ){
				SubPNBean value2 = en3.getValue();
				String subPn = value2.getSubPn();
				if( u8SubInfo!=null && u8SubInfo.subPNumSecProd.contains(subPn)  ){ 
					value2.setRemark("��Ϊ����Ʒ");
				}
			}
			lineToBeanRepeat.putAll(repeatORSubPN);
		}
		
		System.out.println(  "����Ʒ����"+counter1 );
		System.out.println(  "������Ϊ��������"+counter5 );
		System.out.println(  "�����Ӽ��������Ƕ�����������������"+counter2 );
		System.out.println(  "���������������������޷��ҵ�����"+counter3 );
		System.out.println(  "Ӧ������Ϊ0����"+counter4 );
		System.out.println(  "ʹ����������������������"+counter6 );
	}
	
	public static void writeFile() {
		Workbook wb = FPath.getWB(fpath3);
		Sheet sheetAt = wb.getSheetAt(0);
		int lastRowNum = sheetAt.getLastRowNum();
		Row oneRow = null;
		oneRow = sheetAt.getRow(0);
		Cell createCell1 = oneRow.createCell(18);
		Cell createCell2 = oneRow.createCell(19);
		Cell createCell3 = oneRow.createCell(20);
		Cell createCell4 = oneRow.createCell(21);
		createCell1.setCellValue("��������");
		createCell2.setCellValue("δ������");
		createCell3.setCellValue( "��ע" );
		createCell4.setCellValue("ĸ�� δ�������");
		
		for (int i = 1; i <= lastRowNum; i++) {
			oneRow = sheetAt.getRow(i);
			
			String oNum = Convertor2.getCellValue( oneRow.getCell( 2 ) );
			String rNum = Convertor2.getCellValue( oneRow.getCell( 3 ) );
			String pn = Convertor2.getCellValue( oneRow.getCell( 7 ) );
			String subPN = Convertor2.getCellValue( oneRow.getCell( 9 ) );
			
			String lineKey = i+"";
			createCell1 = oneRow.createCell(18);
			createCell2 = oneRow.createCell(19);
			createCell3 = oneRow.createCell(20);
			createCell4 = oneRow.createCell(21);
			
			if( lineToBeanRepeat.containsKey(lineKey) ){
				SubPNBean subPNBean = lineToBeanRepeat.get(lineKey);
				createCell1.setCellValue("-");
				createCell2.setCellValue("-");
//				createCell3.setCellValue("�������к��Ӽ����� �ظ�");
				createCell3.setCellValue( subPNBean.getRemark() );
				createCell4.setCellValue("-");
				
			}else{
				SubPNBean subPNBean = lineToBean.get(lineKey);
				createCell1.setCellValue( subPNBean.getUseCount() +"" );
				createCell2.setCellValue( subPNBean.getUnusedCount() +""  );
				createCell3.setCellValue( subPNBean.getRemark() + "" );
				
				OrderDetail parent = subPNBean.getParent();
				createCell4.setCellValue("" + (parent.getCount() - parent.getDoneCount()) );
				
			}
		}
		
		FileOutputStream fout = null;
		try {
			File file = new File("res/suborder.xlsx" );
			if( file.exists()){
				file.delete();
			}
			file.createNewFile();
			fout = new FileOutputStream(file);
			wb.write(fout);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		loadU8Sub();
		System.out.println( "===========next check ================="  );
		check2();
		System.out.println( "===========next check ================="  );
		loadSubInfo();
		checkSubInfo();
		System.out.println( "===========next check ================="  );
		
		writeFile();
	}


	public static boolean isInt(double number) {
		if (number % 1 == 0) {
			// numberС��������0;
			return true;
		} else {
			// numberС�����ֲ���0;
			return false;
		}
	}
	
//	public static void check1(  ){
//		
//		Workbook wb = FPath.getWB(fpath);
//		Sheet sheetAt = wb.getSheetAt(0);
//		int lastRowNum = sheetAt.getLastRowNum();
//		Row oneRow = null;
//		
//		int inStoreNumCol = 5;//f  ��ⵥ��
//		int proNumCol = 13;//n   ��Ʒ����
//		int countCol = 17;//r ����
//		int orderNumCol = 18;//s  ����������
//		int orderRowNumCol = 19;//t  �к�
//		int secProdCounter = 0;
//		Set<String> orNum = new HashSet<String>();
//		
//		for (int i = 1; i < lastRowNum; i++) {
//			oneRow = sheetAt.getRow(i);
//			if (oneRow == null) {
//				continue;
//			}
//			Cell storeNumCell = oneRow.getCell(inStoreNumCol);
//			Cell prodNumCell = oneRow.getCell(proNumCol);
//			Cell countCell = oneRow.getCell(countCol);
//			Cell orderNumCell = oneRow.getCell(orderNumCol);
//			Cell orderRowNumCell = oneRow.getCell(orderRowNumCol);
//
//			String inStoreNum = Convertor2.getCellValue(storeNumCell);
//			String prodNum = Convertor2.getCellValue(prodNumCell);
//			Double count = Double.parseDouble( Convertor2.getCellValue(countCell) );
//			String oNum = Convertor2.getCellValue(orderNumCell);
//			String rNum = Convertor2.getCellValue(orderRowNumCell);
//			
//			
//			if( StringUtils.isEmpty(oNum) || StringUtils.isEmpty(rNum) ){
//				continue;
//			}
//			
//			String key = OrderBean.getKey(oNum, rNum);
//			U8SubInfo u8SubInfo = ORNumToSecProd.get(key);
//			if( u8SubInfo!=null &&  u8SubInfo.subPNumSecProd.contains(prodNum)  ){
//				System.out.println(   "����Ʒ������" +  prodNum);
//				secProdCounter++;
//				continue;
//			}
//			
//			OrderBean orderBean = oNumORNumToBean.get(key);
//			if( orderBean == null ){
//				orderBean = new OrderBean(oNum ,rNum ,count ,prodNum ,inStoreNum );
//				oNumORNumToBean.put(key, orderBean);
//			}else{
//				try {
//					orderBean.addCount(inStoreNum,count, prodNum);
//				} catch (Exception e) {
//					orNum.add( key );
//					System.out.println(  "i : "+  (i+1) +" oNum:" +oNum+ " rNum:"+rNum +" , "  + e.getMessage() );
//				}
//			}
//		}
//		
//		System.out.println( " second sum ::" + secProdCounter +""  + " , orNumCount :" + orNum.size() );
//	}
	
//	public static void sumCheck() throws ClassNotFoundException, IOException {
//		
//		Map<String, OrderBean> inStore = PnCountLoader.deepClone(oNumORNumToBean);
//		Map<String, OrderStatusBean> orderHandle = PnCountLoader.deepClone(oNumORNumToSBean);
//		
//		List<String> ret1Same = new ArrayList<String>();
//		List<String> ret2NotSame = new ArrayList<String>();
//		List<String> ret3NotExist1 = new ArrayList<String>();
//		List<String> ret3NotExist2 = new ArrayList<String>();
//		
//		Set<Entry<String,OrderBean>> entrySet = inStore.entrySet();
//		for(  Entry<String,OrderBean> ob :  entrySet ){
//			String key = ob.getKey();
//			OrderBean value = ob.getValue();
//			OrderStatusBean remove = orderHandle.remove(key);
//			if(  remove  != null ){
//				Double sumNum = remove.getFiNum();
//				double count = value.getCount();
//				if( sumNum.intValue() ==  count ){
//					ret1Same.add(key);
//				}else{
//					ret2NotSame.add(key);
//				}
//			}else{
//				ret3NotExist1.add(key);
//			}
//		}
//		
//		if( orderHandle.size()>0 ){
//			Set<Entry<String,OrderStatusBean>> entrySet2 = orderHandle.entrySet();
//			for( Entry<String,OrderStatusBean> en :  entrySet2  ){
//				OrderStatusBean value = en.getValue();
//				Double fiNum = value.getFiNum();
//				if( fiNum.doubleValue()==0  ){
//					
//				}else{
//					ret3NotExist2.add( en.getKey() );
//				}
//			}
//		}
//		
//		System.out.println( "��ͬ������"+ ret1Same.size() );
//		System.out.println( "��ͬ������"+ ret2NotSame.size() );
//		System.out.println( "���ཻ������"+ret3NotExist1.size() );
//		System.out.println( "���ཻ������"+ret3NotExist2.size() );
//		for( String aa : ret3NotExist2 ){
//			System.out.println(  aa );
//		}
//	}


	
}
