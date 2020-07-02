package nms.t1101;

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

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class Test1101 {


	private static final String path1 = "D:/ForBdcom/1101/在线工单整理.xlsx";
	private static final String path11 = "D:/ForBdcom/1101/在线工单整理20191031.XLSX";
	
	public static Map<String, ProdOrder> MAP1 = new HashMap<String, ProdOrder>();
	
	public static void loadSrc(){
		Workbook wb = FPath.getWB(path1);
		Sheet sheetAt = wb.getSheetAt(0);
		int lastRowNum = sheetAt.getLastRowNum();
		Row oneRow = null;
		for (int i = 1; i <= lastRowNum; i++) {
			try {
				oneRow = sheetAt.getRow(i);
				if (oneRow == null) {
					continue;
				}
				String key = Convertor2.getCellValue( oneRow.getCell( 1 ) );
				String pn = Convertor2.getCellValue( oneRow.getCell( 3 ) );
				Double sumCount = Double.parseDouble( Convertor2.getCellValue( oneRow.getCell( 9 ) )  );
				Double doneCount = Double.parseDouble( Convertor2.getCellValue( oneRow.getCell( 13 ) )  ) ;
				
				ProdOrder prodOrder = new  ProdOrder(key, pn, sumCount, doneCount);
				ProdOrder prodOrder2 = MAP1.get(key);
				if( prodOrder2 == null  ){
					MAP1.put(  key,  prodOrder    );
				}else{
					System.out.println( "！！存在重复 的工单号" +  (i+1 ) );
				}
			} catch ( Exception e) {
				System.out.println( "ERROR:::" +  (i+1 ) );
				e.printStackTrace();
				break;
			}
		}
		System.out.println( MAP1.size()   );
	}
	
	public static Set<String> keySets = new HashSet<String>();
//	private static final String path2 = "D:/ForBdcom/1101/子件明细不包含副产品.xlsx";
	private static final String path2 = "D:/ForBdcom/1101/1101子件明细（去掉无在线工单对应的子件）_修改20191102.xlsx";
	public static boolean needCalcNcInfo = false;
	
	public static ArrayList< ProdSubOrder> list1 = new ArrayList<ProdSubOrder>(); 
	public static void loadSrc2(){
		Workbook wb = FPath.getWB(path2);
		Sheet sheetAt = wb.getSheetAt(0);
		int lastRowNum = sheetAt.getLastRowNum();
		Row oneRow = null;
		for (int i = 1; i <= lastRowNum; i++) {
			oneRow = sheetAt.getRow(i);
			if (oneRow == null) {
				continue;
			}
			String oNum = Convertor2.getCellValue(oneRow.getCell(0));
			String rNum = Convertor2.getCellValue(oneRow.getCell(1));
			String parentPN = Convertor2.getCellValue(oneRow.getCell(2));
			
			String subRNum = Convertor2.getCellValue(oneRow.getCell(3));
			String subPN = Convertor2.getCellValue(oneRow.getCell(4));
			Double u8SubNeedCount = Double.parseDouble(Convertor2 .getCellValue(oneRow.getCell(5)));
			Double u8SubHaveCount = Double.parseDouble(Convertor2 .getCellValue(oneRow.getCell(6)));
			String secProduct = Convertor2.getCellValue(oneRow.getCell(7));
			if( "3".equals(secProduct.trim()) ){
				continue;
			}
			ProdSubOrder subOrder  = new ProdSubOrder(oNum, rNum, parentPN, subPN, u8SubNeedCount, u8SubHaveCount);
			subOrder.setSubRNum(subRNum);
			subOrder.setSecProductFlag(secProduct);
			
			if( !needCalcNcInfo ){
				String u8ProdCount = Convertor2.getCellValue(oneRow.getCell(8));
				String u8DoneCount = Convertor2.getCellValue(oneRow.getCell(9));
				String ncParentProdCount = Convertor2.getCellValue(oneRow.getCell(10));
				String ncSubNeedCount = Convertor2.getCellValue(oneRow.getCell(11));
				String ncSubHaveCount = Convertor2.getCellValue(oneRow.getCell(12));
				String remark = Convertor2.getCellValue(oneRow.getCell(13));
				
				if( !StringUtils.isEmpty(u8ProdCount) ){
					subOrder.setU8ProdCount(  Double.parseDouble( u8ProdCount ) );
				}
				if( !StringUtils.isEmpty(u8DoneCount) ){
					subOrder.setU8DoneCount( Double.parseDouble( u8DoneCount ) );
				}
				if( !StringUtils.isEmpty(ncParentProdCount) ){
					subOrder.setNcParentProdCount( Double.parseDouble( ncParentProdCount )  );
				}
				if( !StringUtils.isEmpty(ncSubNeedCount) ){
					subOrder.setNcSubNeedCount(  Double.parseDouble( ncSubNeedCount )  );
				}
				if( !StringUtils.isEmpty(ncSubHaveCount) ){
					subOrder.setNcSubHaveCount(  Double.parseDouble( ncSubHaveCount )    );
				}
				subOrder.setRemark( remark );
				
				double AA = subOrder.getNcSubNeedCount();
				double BB = subOrder.getNcSubHaveCount();
				double CC = 0;
				double DD = 0;
				if( AA>BB ){
					CC = BB;
					DD=0d;
				}else{
					CC=AA;
					DD=BB-AA;
				}
				subOrder.setCc(CC);
				subOrder.setDd(DD);
			}
			
			list1.add(subOrder);
			keySets.add(subOrder.getKey());
		}

		System.out.println(   "子件明细行数 :" + list1.size()  );
	}
	
	private static void checkParent() {
		Set<String> keySet = MAP1.keySet();
		for( String pkey : keySet ){
			if(  !keySets.contains(pkey)  ){
				System.out.println("！！！父工单在子件明细中不存在：" +pkey );
			}
		}
	}

	public static Map<String, ProdSubOrder> subPNToNCCnt = new HashMap<String, ProdSubOrder>();
	
	public static void doMerge(){
		int size = list1.size();
		for(  int i=0;i<size;i++ ){
			ProdSubOrder subOrder = list1.get(i);
			String key = subOrder.getKey();
			ProdOrder pOrder = MAP1.get(key);
			
			if( pOrder == null  ){
				subOrder.setRemark(  "无法找到对应父工单信息" );
				continue;
			}
			pOrder.addSubRow(subOrder);
			
			Double ncSubHaveCount2 = subOrder.getNcSubHaveCount();
			Double ncSubNeedCount2 = subOrder.getNcSubNeedCount();
			String subPN = subOrder.getSubPN();
			ProdSubOrder prodSubOrder = subPNToNCCnt.get(subPN);
			if( prodSubOrder ==null ){
				prodSubOrder = new ProdSubOrder();
				prodSubOrder.setNcSubHaveCount( ncSubHaveCount2 );
				prodSubOrder.setNcSubNeedCount(  ncSubNeedCount2 );
				prodSubOrder.setSubPN(subPN);
				subPNToNCCnt.put(subPN, prodSubOrder);
			}else{
				prodSubOrder.setNcSubHaveCount( ncSubHaveCount2 + prodSubOrder.getNcSubHaveCount() );
				prodSubOrder.setNcSubNeedCount(  ncSubNeedCount2 +  prodSubOrder.getNcSubNeedCount() );
			}
			
			if( !needCalcNcInfo ){
				continue;
			}
			
			double sumCount = pOrder.getSumCount();
			double doneCount = pOrder.getDoneCount();
			subOrder.setU8ProdCount(sumCount);
			subOrder.setU8DoneCount(doneCount);
			subOrder.setNcParentProdCount(  sumCount - doneCount );
			
			//计算NC子件数量
			double u8SubNeedCount = subOrder.getU8SubNeedCount();
			double u8SubHaveCount = subOrder.getU8SubHaveCount();
			if(  doneCount == 0 ){
				subOrder.setNcSubNeedCount(u8SubNeedCount);
				subOrder.setNcSubHaveCount( u8SubHaveCount);
				subOrder.setRemark(  "已生产数量为0" );
				continue;
			}
			
			if(  u8SubNeedCount == 0 ){
				subOrder.setNcSubNeedCount( 0d );
				subOrder.setNcSubHaveCount( 0d );
				subOrder.setRemark(  "应领数量为0" );
				continue;
			}
			
			if( !isInt(u8SubNeedCount) || !isInt(u8SubHaveCount)  ){
				double multiple = u8SubNeedCount / sumCount;
				subOrder.setNcSubNeedCount( multiple * subOrder.getNcParentProdCount() );
				subOrder.setNcSubHaveCount(  u8SubHaveCount -  multiple * doneCount   );
				subOrder.setRemark("数量不为整数");
				continue;
			}
			
			double multiple = u8SubNeedCount / sumCount;
			if( !isInt(multiple)  ){
				subOrder.setRemark("所需子件总数不是订单数量整倍数");
				continue;
			}
			
			double ncSubHaveCount = u8SubHaveCount -  multiple * doneCount  ;
			subOrder.setNcSubNeedCount( multiple * subOrder.getNcParentProdCount() );
			subOrder.setNcSubHaveCount( ncSubHaveCount  );
			
			if( ncSubHaveCount < 0  ){
				subOrder.setRemark(  "使用数量大于已领数量" );
				continue;
			}
			
			subOrder.setRemark(  "已正确计算" );
		}
		
	}
	
	public static void main(String[] args) {
		
		loadSrc();
		loadSrc2();
		checkParent();
		
		doMerge();
		
		writeFile();
		writeSepFile();
		
		writeSubPNCnt();
		
	}	
	
	public static final String tmpPath = "D:/ForBdcom/1101/tmp.xlsx";
	private static void writeSepFile() {
		
		Set<Entry<String,ProdOrder>> entrySet = MAP1.entrySet();
		for(Entry<String,ProdOrder> en :  entrySet   ){
			String key = en.getKey();
			ProdOrder value = en.getValue();
			List<ProdSubOrder> subInfos = value.getSubInfos();
			
			String orNum = value.getKey();
			
			Workbook wb = FPath.getWB(tmpPath);
			Sheet sheetAt = wb.getSheetAt(0);
			int lastRowNum = sheetAt.getLastRowNum();
			Row oneRow = null;
			oneRow = sheetAt.getRow(0);
			for(   int i =0; i<subInfos.size()  ;i++ ){
				oneRow = sheetAt.createRow( i+1 );
				ProdSubOrder prodSubOrder = subInfos.get(i);
				createCellForRow( oneRow , prodSubOrder);
			}
			FileOutputStream fout = null;
			try {
				File file = new File("D:/ForBdcom/1101/res/" +orNum +".xlsx" );
				if( file.exists()){
					file.delete();
				}
				file.createNewFile();
				fout = new FileOutputStream(file);
				wb.write(fout);
				fout.close();
			} catch (Exception e) {
				e.printStackTrace();
			}  
			
		}
		
		
	}

	private static void createCellForRow(Row oneRow, ProdSubOrder pso ) {
		Cell	createCell1 = oneRow.createCell(0);
		Cell	createCell2 = oneRow.createCell(1);
		Cell	createCell3= oneRow.createCell(2);
		Cell	createCell4 = oneRow.createCell(3);
		Cell	createCell5 = oneRow.createCell(4);
		Cell	createCell6= oneRow.createCell(5);
		Cell	createCell7= oneRow.createCell(6);
		Cell	createCell8= oneRow.createCell(7);
		
		Cell	createCell9 = oneRow.createCell(8);
		Cell	createCell10 = oneRow.createCell(9);
		Cell	createCell11 = oneRow.createCell(10);
		Cell	createCell12 = oneRow.createCell(11);
		Cell	createCell13 = oneRow.createCell(12);
		
		Cell	createCell14 = oneRow.createCell(13);
		Cell	createCell15 = oneRow.createCell(14);
		Cell	createCell16 = oneRow.createCell(15);
		
		
		createCell1.setCellValue( pso.getoNum() );
		createCell2.setCellValue( pso.getrNum() );
		createCell3.setCellValue( pso.getParentPN() );
		createCell4.setCellValue( pso.getSubRNum() );
		createCell5.setCellValue( pso.getSubPN() );
		createCell6.setCellValue( pso.getU8SubNeedCount()  );
		createCell7.setCellValue( pso.getU8SubHaveCount() );
		createCell8.setCellValue( pso.getSecProductFlag() );
		
		createCell9.setCellValue(  pso.getU8ProdCount()==null? "" : pso.getU8ProdCount().toString()  );
		createCell10.setCellValue( pso.getU8DoneCount() ==null? "" : pso.getU8DoneCount().toString()    );
		createCell11.setCellValue( pso.getNcParentProdCount() ==null? "" : pso.getNcParentProdCount().toString()   );
		createCell12.setCellValue( pso.getNcSubNeedCount()  ==null? "" : pso.getNcSubNeedCount().toString()     );
		createCell13.setCellValue( pso.getNcSubHaveCount() ==null? "" : pso.getNcSubHaveCount().toString()     );
		
		createCell14.setCellValue(   pso.getCc() ==null? "" : pso.getCc().toString()    );
		createCell15.setCellValue(  pso.getDd() ==null? "" : pso.getDd().toString()    );
		createCell16.setCellValue( pso.getRemark() );
		
	}

	public static boolean isInt(double number) {
		if (number % 1 == 0) {
			// number小数部分是0;
			return true;
		} else {
			// number小数部分不是0;
			return false;
		}
	}
	
	public static void writeFile(){
		// 8 9 10 11 12 13 
		
		Workbook wb = FPath.getWB(tmpPath);
		Sheet sheetAt = wb.getSheetAt(0);
		int lastRowNum = sheetAt.getLastRowNum();
		Row oneRow = null;
		oneRow = sheetAt.getRow(0);
		
		int size = list1.size();
		int counter = 1;
		for (int i = 1; i <= size; i++) {
			ProdSubOrder pso = list1.get( i-1 );
			
			if( "无法找到对应父工单信息".equals(pso.getRemark())  ){
				continue;
			}
			oneRow = sheetAt.createRow( counter++ );
			createCellForRow(oneRow, pso);
			
		}
		
		FileOutputStream fout = null;
		try {
			File file = new File("res/zijianmingxiExt.xlsx" );
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
	
	
	public static final String tmpPath2 = "D:/ForBdcom/1101/tmp2.xlsx";
	private static void writeSubPNCnt() {
		Workbook wb = FPath.getWB(tmpPath2);
		Sheet sheetAt = wb.getSheetAt(0);
		int lastRowNum = sheetAt.getLastRowNum();
		Row oneRow = null;
		oneRow = sheetAt.getRow(0);
		
		Set<Entry<String,ProdSubOrder>> entrySet = subPNToNCCnt.entrySet();
		int counter = 1;
		for (Entry<String,ProdSubOrder> en :entrySet ) {
			ProdSubOrder pso = en.getValue();
			oneRow = sheetAt.createRow( counter++ );
			
			String subPN = pso.getSubPN();
			Double ncSubNeedCount = pso.getNcSubNeedCount();
			Double ncSubHaveCount = pso.getNcSubHaveCount();
			
			Cell	createCell1 = oneRow.createCell(0);
			Cell	createCell2 = oneRow.createCell(1);
			Cell	createCell3= oneRow.createCell(2);
			
			createCell1.setCellValue( pso.getSubPN()     );
			createCell2.setCellValue( pso.getNcSubNeedCount()  ==null? "" : pso.getNcSubNeedCount().toString()     );
			createCell3.setCellValue( pso.getNcSubHaveCount() ==null? "" : pso.getNcSubHaveCount().toString()     );
		}
		
		FileOutputStream fout = null;
		try {
			File file = new File("res/zijianhuizong.xlsx" );
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

	
	
}
