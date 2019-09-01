package nms.newstat.tonc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import nms.RowData;
import nms.newstat.StatRow;
import nms.newstat.tonc2.ImportRowObj;
import nms.newstat.tonc2.ImportSheetObject;
import nms.newstat.tonc2.ImportTitleObj;

public class ToNCExcel {
	
	/**
	 * 生成NC数据
	 * @param srcDatas
	 */
	public static void createToNC(   Map<String , RowData> distinctMap ){
		//加载 NC 库位对照
		//1 启用SN管理的   生成 SN序列号导入文件
		
		//2 没有启用的 计算数量 生成导入文件
		
		//补假号 导入虚拟库
		//补 假数量 如虚拟库
		
		
	}
	
	
	public static String[ ]  title1 = new String[]{
		"\"InitializtionInHeadVO_$head,pk_org,dbilldate,cwarehouseid,ctrantypeid\"",
		"* 库存组织" , 
		"* 单据日期"  , 
		"* 仓库" , 
		"* 出入库类型"
	};
	
	public static String[ ]  title2 = new String[]{
		"\"cgeneralbid,crowno,cmaterialvid,castunitid,nassistnum,dbizdate,clocationid,vserialcode,csnunitid\"",	
		"* 行号",
		"* 物料编码",
		"* 单位"	,
		"* 数量","* 入库日期",
		"货位",
		"序列号",	
		"序列号单位"
	};
	
	public static void createExcel(   ImportSheetObject sheetObj ,  String fname  ){
		
		XSSFWorkbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet( "Sheet1" );
		
		int rowCounter = 1; 
		Row row = sheet.createRow( rowCounter++ );
		
		CellStyle style = wb.createCellStyle();
		
		for( int i=0; i<title1.length  ; i++ ){
			Cell cell = row.createCell((short) i);
			cell.setCellValue(title1[i]);
			cell.setCellStyle(style);
		}
		
		List<ImportTitleObj> titleList = sheetObj.getTitleList();
		for( ImportTitleObj tObj :  titleList ){
			int titleNum = tObj.getTitleNum();
			String ncKczz = tObj.getNcKczz();
			String dateStr = tObj.getDateStr();
			String nckb = tObj.getNckb();
			String inoutType = tObj.getInoutType();
			//ID ，  库存组织  , 单据日期 , 仓库 ,   出入库类型
			String[] storeInfo = new String[]{  titleNum+"" ,  ncKczz  , dateStr  , nckb , inoutType };
			row = sheet.createRow( rowCounter++ );
			for( int i=0; i<storeInfo.length  ; i++ ){
				Cell cell = row.createCell((short) i);
				cell.setCellValue(storeInfo[i]);
			}
		}
		
		//空一行
		row = sheet.createRow( rowCounter++ );
		
		row = sheet.createRow( rowCounter++ );
		for( int i=0; i<title2.length  ; i++ ){
			Cell cell = row.createCell((short) i);
			cell.setCellValue(title2[i]);
			cell.setCellStyle(style);
		}
		
		List<ImportRowObj> rowList = sheetObj.getRowList();
		for(  ImportRowObj rowObj  : rowList ){
			int titleNum = rowObj.getTitleNum();
			int rowNum = rowObj.getRowNum();
			String pn = rowObj.getPn();
			String unit = rowObj.getUnit();
			double count = rowObj.getCount();
			String dateStr = rowObj.getDateStr();
			String nckw = rowObj.getNckw();
			String sn = rowObj.getSn();
			String snUnit = rowObj.getSnUnit();
			//storeID ，  rownum  , PN , unit ,   count，日期， 货位， SN ， SN UNIT
			String[] onePnInfo = new String[]{
					titleNum+"" , rowNum +""  ,  pn  ,  unit  , count+""  , dateStr  , nckw ,  sn , snUnit
			};
			row = sheet.createRow( rowCounter++ );
			for( int i=0; i<onePnInfo.length  ; i++ ){
				Cell cell = row.createCell((short) i);
				cell.setCellValue(onePnInfo[i]);
			}
			
		}
		
		writeToFile( fname , wb);
	}
	
	
	public static void writeToFile(String fname ,XSSFWorkbook wb ){
		FileOutputStream fout = null;
		try {
			File file = new File("res/" + fname);
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
	
	
	public static void main(String[] args) {
		
		
	}
	
	
}
