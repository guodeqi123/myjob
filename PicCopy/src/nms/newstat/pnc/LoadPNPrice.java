package nms.newstat.pnc;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nms.Convertor;
import nms.newstat.Convertor2;
import nms.newstat.FPath;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LoadPNPrice {
	
	public static Map<String , PNInfoObj> pnToObj = new HashMap<String, PNInfoObj>();
	public static List< PNInfoObj> pnObjs = new ArrayList< PNInfoObj>();
	public static Set<String> vitualPN = new HashSet<String>();
 	
	public static void load(){
		Workbook wb = null;
		Sheet sheet;
		try {
			File file = new File( FPath.u8PNToPrice);
			InputStream input = new FileInputStream(file);
			String fileExt = file.getName().substring(
					file.getName().lastIndexOf(".") + 1);
			wb = null;
			sheet = null;
			// 根据后缀判断excel 2003 or 2007+
			if ("xls".equals(fileExt)) {
				wb = (HSSFWorkbook) WorkbookFactory.create(input);
			} else {
				wb = new XSSFWorkbook(input);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Row row = null;
		int sheetNum = wb.getNumberOfSheets();
		for(  int si =0 ; si<sheetNum ; si++ ){
			sheet = wb.getSheetAt(si);
			String sheetName = sheet.getSheetName();
//			System.out.println( "###sheetName : " +sheetName + " ,  " +   si  );
			int rownum = sheet.getLastRowNum();
			for(  int i=1 ; i<=rownum  ; i++ ){
				 try {
					row = sheet.getRow(i);
					 if( row ==null ){
						 continue;
					 }
					 Cell cellPn = row.getCell( 4 );
					 Cell cellPNname = row.getCell( 5 );
					 Cell cellDesc = row.getCell( 6 );
					 Cell cellPrice = row.getCell( 8 );
					 
					 String pnStr = Convertor2.getCellValue(cellPn);
					 if( StringUtils.isEmpty(pnStr) ){
						 continue;
					 }
					 String pnNameStr = Convertor2.getCellValue(cellPNname);
					 String pnSpeci = Convertor2.getCellValue(cellDesc);
					 double price = cellPrice.getNumericCellValue();
					 
					 Cell kcTypeCell = row.getCell( 2 );
					 String kcTypeStr = Convertor2.getCellValue(kcTypeCell);
					 
					 PNInfoObj pnInfoObj = new PNInfoObj(pnStr, price, pnNameStr, pnSpeci);
					 pnInfoObj.setSheetNum(si);
					 pnInfoObj.setKcType(kcTypeStr);
					 pnObjs.add(pnInfoObj);
					 
					 PNInfoObj pnInfoObj2 = pnToObj.get(pnStr);
					 if( pnInfoObj2 ==null ){
						 pnToObj.put(pnStr, pnInfoObj);
					 }else{
						 double price2 = pnInfoObj2.getPrice();
						 if(price2 != price   ){
							 System.out.println( "加载PN价格：："+ sheetName + " ,存在不同价格： " +   pnStr + "" );
						 }
					 }
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		
		for(   PNInfoObj obj : pnObjs ){
			String pn = obj.getPn();
			String kcType = obj.getKcType();
			int cSheetNum = obj.getSheetNum();
			
			if( "虚拟库存".equals(kcType) ){
				vitualPN.add(pn);
			}
		}
		
		System.out.println(  "加载PN Price存在PN行：："+ pnObjs.size() );
		System.out.println(  "加载PN Price存在PN个去重：："+ pnToObj.size() );
		System.out.println(  "加载PN Price虚拟PN个：："+ vitualPN.size() );
		
		
	}
	
	
	public static void main(String[] args) {
		load();
	}
	
}
