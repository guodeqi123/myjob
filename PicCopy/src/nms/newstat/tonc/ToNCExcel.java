package nms.newstat.tonc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import nms.RowData;
import nms.newstat.StatRow;

public class ToNCExcel {
	
	/**
	 * ����NC����
	 * @param srcDatas
	 */
	public static void createToNC(   Map<String , RowData> distinctMap ){
		//���� NC ��λ����
		//1 ����SN�����   ���� SN���кŵ����ļ�
		
		//2 û�����õ� �������� ���ɵ����ļ�
		
		//���ٺ� ���������
		//�� ������ �������
		
		
	}
	
	
	public static String[ ]  title1 = new String[]{
		"\"InitializtionInHeadVO_$head,pk_org,dbilldate,cwarehouseid,ctrantypeid\"",
		"* �����֯" , 
		"* ��������"  , 
		"* �ֿ�" , 
		"* ���������"
	};
	
	public static String[ ]  title2 = new String[]{
		"\"cgeneralbid,crowno,cmaterialvid,castunitid,nassistnum,dbizdate,clocationid,vserialcode,csnunitid\"",	
		"* �к�",
		"* ���ϱ���",
		"* ��λ"	,
		"* ����","* �������",
		"��λ",
		"���к�",	
		"���кŵ�λ"
	};
	
	public static void createExcel(){
		
		XSSFWorkbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet( "Sheet1" );
		Row row = sheet.createRow((int) 1);
		
		CellStyle style = wb.createCellStyle();
		
		for( int i=0; i<title1.length  ; i++ ){
			Cell cell = row.createCell((short) i);
			cell.setCellValue(title1[i]);
			cell.setCellStyle(style);
		}
		
		//ID ��  �����֯  , �������� , �ֿ� ,   ���������
		String[] storeInfo = new String[]{
				"1" , "01" , "2019-08-31" , "30" , "�ڳ����"  
		};
		
		row = sheet.createRow((int) 3);
		for( int i=0; i<title2.length  ; i++ ){
			Cell cell = row.createCell((short) i);
			cell.setCellValue(title2[i]);
			cell.setCellStyle(style);
		}
		
		//storeID ��  rownum  , PN , unit ,   count�����ڣ� ��λ�� SN �� SN UNIT
		String[] onePnInfo = new String[]{
				"1" , "1" ,  "PN" , "ֻ" ,"1"  ,"2019-08-31" , "010101" , "SN", "ֻ"
		};
		
		writeToFile("temp1.xlsx", wb);
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
		
		createExcel();
		
	}
	
	
}
