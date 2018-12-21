package org.tool.piccopy.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ExcelTool implements ExcelConstant {

	/**
	 * excel-2010���ȡ
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static List<JSONObject> readXlsx(String path) throws IOException {

		InputStream is = new FileInputStream(path);
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);

		List<JSONObject> list = new ArrayList<JSONObject>();
		// Read the Sheet
		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}

			// ��0��Ϊ��������
			XSSFRow columnRow = xssfSheet.getRow(0);
			if (columnRow == null) {
				continue;
			}
			int columns = columnRow.getPhysicalNumberOfCells();
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);

				if (xssfRow != null) {
					JSONObject jsonObject = new JSONObject();
					for (int column = 0; column < columns; column++) {
						XSSFCell columnCell = columnRow.getCell(column);
						XSSFCell dataCell = xssfRow.getCell(column);
						jsonObject
								.put(getValue(columnCell), getValue(dataCell));
					}
					list.add(jsonObject);

				}
			}
		}
		return list;
	}
	
	/**
	 * excel-2010���ȡ
	 */
	public static List<List<String>> readXlsxToRows(String path) throws IOException {
		InputStream is = new FileInputStream(path);
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
		List<List<String>> list = new ArrayList<List<String>>();
		// Read the Sheet
		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}
			// ��0��Ϊ��������
			XSSFRow columnRow = xssfSheet.getRow(0);
			if (columnRow == null) {
				continue;
			}
			int columns = columnRow.getPhysicalNumberOfCells();
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);

				if (xssfRow != null) {
					List<String> row = new ArrayList<String>();
					for (int column = 0; column < columns; column++) {
						XSSFCell columnCell = columnRow.getCell(column);
						XSSFCell dataCell = xssfRow.getCell(column);
						row.add( getValue(dataCell));
					}
					list.add(row);
				}
			}
		}
		return list;
	}

	/**
	 * excel-2003���ȡ
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static List<JSONObject> readXls(String path) throws IOException {

		InputStream is = new FileInputStream(path);
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);

		List<JSONObject> list = new ArrayList<JSONObject>();
		// Read the Sheet
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}

			// ��0��Ϊ��������
			HSSFRow columnRow = hssfSheet.getRow(0);
			if (columnRow == null) {
				continue;
			}
			int columns = columnRow.getPhysicalNumberOfCells();
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);

				if (hssfRow != null) {
					JSONObject jsonObject = new JSONObject();
					for (int column = 0; column < columns; column++) {
						HSSFCell columnCell = columnRow.getCell(column);
						HSSFCell dataCell = hssfRow.getCell(column);
						jsonObject
								.put(getValue(columnCell), getValue(dataCell));
					}
					list.add(jsonObject);

				}
			}
		}
		return list;
	}
	
	/**
	 * excel-2003���ȡ
	 */
	public static List<List<String>> readXlsToRows(String path) throws IOException {
		InputStream is = new FileInputStream(path);
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		List<List<String>> list = new ArrayList<List<String>>();
		// Read the Sheet
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}

			// ��0��Ϊ��������
			HSSFRow columnRow = hssfSheet.getRow(0);
			if (columnRow == null) {
				continue;
			}
			int columns = columnRow.getPhysicalNumberOfCells();
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);

				if (hssfRow != null) {
					List<String> row = new ArrayList<String>();
					for (int column = 0; column < columns; column++) {
						HSSFCell columnCell = columnRow.getCell(column);
						HSSFCell dataCell = hssfRow.getCell(column);
						row.add( getValue(dataCell) );
					}
					list.add(row);
				}
			}
		}
		return list;
	}
	
	public static List<List<String>> readExcelToRows(String path) {
		if (null == path) {
			return null;
		}
		if (!path.endsWith(EXCEL_2003) && !path.endsWith(EXCEL_2010)) {
			return null;
		}

		if (path.endsWith(EXCEL_2003)) {
			try {
				return readXlsToRows(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (path.endsWith(EXCEL_2010)) {
			try {
				return readXlsxToRows(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static List<JSONObject> readExcel(String path) {
		if (null == path) {
			return null;
		}
		if (!path.endsWith(EXCEL_2003) && !path.endsWith(EXCEL_2010)) {
			return null;
		}

		if (path.endsWith(EXCEL_2003)) {
			try {
				return readXls(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (path.endsWith(EXCEL_2010)) {
			try {
				return readXlsx(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static JSONArray readExcelRetJsonArray(String path) {

		List<JSONObject> list = readExcel(path);
		if (null == list)
			return null;
		JSONArray jsonArray = new JSONArray();
		for (JSONObject jsonObject : list) {
			jsonArray.add(jsonObject);
		}
		return jsonArray;

	}
	
	
	
	
	private static String getValue(XSSFCell xssfRow) {
		if (xssfRow == null) {
			return "";
		}
		if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
			return String.valueOf(xssfRow.getBooleanCellValue());
		} else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
			String result = String.valueOf(xssfRow.getNumericCellValue());
			if (result.endsWith(".0")) {
				result = result.substring(0, result.length() - 2);
			}
			return result;
		} else {
			return String.valueOf(xssfRow.getStringCellValue());
		}
	}

	private static String getValue(HSSFCell hssfCell) {
		if (hssfCell == null) {
			return "";
		}
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {

			double numericCellValue = hssfCell.getNumericCellValue();
			long a = (long) numericCellValue;
			String result = String.valueOf( a );
			if (result.endsWith(".0")) {
				result = result.substring(0, result.length() - 2);
			}
			return result;
		} else {
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}
	
	///////////////////////////////////////////////////////////////////////

	public static boolean WriteExcel(String path, String fileName,
			JSONArray array) {
		File file = new File(path);
		if (!file.exists() && !file.isDirectory()) {
			file.mkdir();
		}

		if (fileName.endsWith(ExcelConstant.EXCEL_2003)) {
			writeXls(array, path + fileName);
		}
		if (fileName.endsWith(ExcelConstant.EXCEL_2010)) {
			writeXlsx(array, path + fileName);
		}

		return true;
	}


	/**
	 * ����Ϊ2003 excel
	 * 
	 * @param jsonArray
	 * @param savePath
	 */

	public static void writeXls(JSONArray jsonArray, String savePath) {
		// ��һ��������һ��webbook����Ӧһ��Excel�ļ�
		HSSFWorkbook wb = new HSSFWorkbook();
		// �ڶ�������webbook�����һ��sheet,��ӦExcel�ļ��е�sheet
		HSSFSheet sheet = wb.createSheet("sheet1");
		// ����������sheet����ӱ�ͷ��0��,ע���ϰ汾poi��Excel����������������short
		HSSFRow row = sheet.createRow((int) 0);
		// ���Ĳ���������Ԫ�񣬲�����ֵ��ͷ ���ñ�ͷ����
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // ����һ�����и�ʽ

		JSONObject jsonObject = jsonArray.getJSONObject(0);
		if (null == jsonObject) {
			return;
		}
		int column = 0;
		// �����У����У�
		List<Object> columnsList = new ArrayList<Object>();
		for (Object key : jsonObject.keySet()) {
			columnsList.add(key);
			HSSFCell cell = row.createCell((short) column);
			if (key instanceof Boolean) {
				cell.setCellValue((Boolean) key);
			} else if (key instanceof Calendar) {
				cell.setCellValue((Calendar) key);
			} else if (key instanceof Date) {
				cell.setCellValue((Date) key);
			} else if (key instanceof Double) {
				cell.setCellValue((Double) key);
			} else if (key instanceof RichTextString) {
				cell.setCellValue((RichTextString) key);
			} else if (key instanceof String) {
				cell.setCellValue((String) key);
			} else {
				cell.setCellValue(key.toString());
			}

			cell.setCellStyle(style);

			column++;

		}
		// ������
		for (int i = 0; i < jsonArray.size(); i++) {

			JSONObject jObject = jsonArray.getJSONObject(i);
			HSSFRow row2 = sheet.createRow(i + 1);
			HSSFCell cell2 = null;
			for (int j = 0; j < columnsList.size(); j++) {

				cell2 = row2.createCell(j);
				Object key = jObject.get(columnsList.get(j));

				if (key instanceof Boolean) {
					cell2.setCellValue((Boolean) key);
				} else if (key instanceof Calendar) {
					cell2.setCellValue((Calendar) key);
				} else if (key instanceof Date) {
					cell2.setCellValue((Date) key);
				} else if (key instanceof Double) {
					cell2.setCellValue((Double) key);
				} else if (key instanceof RichTextString) {
					cell2.setCellValue((RichTextString) key);
				} else if (key instanceof String) {
					cell2.setCellValue((String) key);
				} else {
					cell2.setCellValue(key.toString());
				}

			}

		}

		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(savePath);
			wb.write(fout);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fout.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * ����Ϊ2010��excel
	 * 
	 * @param jsonArray
	 * @param savePath
	 */
	public static void writeXlsx(JSONArray jsonArray, String savePath) {
		// ��һ��������һ��webbook����Ӧһ��Excel�ļ�
		XSSFWorkbook wb = new XSSFWorkbook();
		// �ڶ�������webbook�����һ��sheet,��ӦExcel�ļ��е�sheet
		XSSFSheet sheet = wb.createSheet("sheet1");
		// ����������sheet����ӱ�ͷ��0��,ע���ϰ汾poi��Excel����������������short
		XSSFRow row = sheet.createRow((int) 0);
		// ���Ĳ���������Ԫ�񣬲�����ֵ��ͷ ���ñ�ͷ����
		XSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER); // ����һ�����и�ʽ

		JSONObject jsonObject = jsonArray.getJSONObject(0);
		if (null == jsonObject) {
			return;
		}
		int column = 0;
		// �����У����У�
		List<Object> columnsList = new ArrayList<Object>();
		for (Object key : jsonObject.keySet()) {
			columnsList.add(key);
			XSSFCell cell = row.createCell((short) column);
			if (key instanceof Boolean) {
				cell.setCellValue((Boolean) key);
			} else if (key instanceof Calendar) {
				cell.setCellValue((Calendar) key);
			} else if (key instanceof Date) {
				cell.setCellValue((Date) key);
			} else if (key instanceof Double) {
				cell.setCellValue((Double) key);
			} else if (key instanceof RichTextString) {
				cell.setCellValue((RichTextString) key);
			} else if (key instanceof String) {
				cell.setCellValue((String) key);
			} else {
				cell.setCellValue(key.toString());
			}

			cell.setCellStyle(style);

			column++;

		}
		// ������
		for (int i = 0; i < jsonArray.size(); i++) {

			JSONObject jObject = jsonArray.getJSONObject(i);
			XSSFRow row2 = sheet.createRow(i + 1);
			XSSFCell cell2 = null;
			for (int j = 0; j < columnsList.size(); j++) {

				cell2 = row2.createCell(j);
				Object key = jObject.get(columnsList.get(j));

				if (key instanceof Boolean) {
					cell2.setCellValue((Boolean) key);
				} else if (key instanceof Calendar) {
					cell2.setCellValue((Calendar) key);
				} else if (key instanceof Date) {
					cell2.setCellValue((Date) key);
				} else if (key instanceof Double) {
					cell2.setCellValue((Double) key);
				} else if (key instanceof RichTextString) {
					cell2.setCellValue((RichTextString) key);
				} else if (key instanceof String) {
					cell2.setCellValue((String) key);
				} else {
					cell2.setCellValue(key.toString());
				}

			}

		}

		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(savePath);
			wb.write(fout);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fout.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private static void writer(String path, String fileName, String fileType)
			throws IOException {
		// ���������ĵ�����
		Workbook wb = null;
		if (fileType.equals("xls")) {
			wb = new HSSFWorkbook();
		} else if (fileType.equals("xlsx")) {
			wb = new XSSFWorkbook();
		} else {
			System.out.println("�����ĵ���ʽ����ȷ��");
		}
		// ����sheet����
		Sheet sheet1 = wb.createSheet("sheet1");
		// ѭ��д��������
		for (int i = 0; i < 5; i++) {
			Row row = (Row) sheet1.createRow(i);
			// ѭ��д��������
			for (int j = 0; j < 8; j++) {
				Cell cell = row.createCell(j);
				cell.setCellValue("����" + j);
			}
		}
		// �����ļ���
		OutputStream stream = new FileOutputStream(path + fileName + "."
				+ fileType);
		// д������
		wb.write(stream);
		// �ر��ļ���
		stream.close();
	}

	public static void main(String[] args) {
		JSONArray array = new JSONArray();
		JSONObject object = new JSONObject();
		object.put("id", 12);
		object.put("name", "jjl");
		object.put("flag", false);
		array.add(object);
		object.put("id", 13);
		object.put("name", "cjzm888999");
		object.put("flag", true);
		array.add(object);
		WriteExcel("D:\\test\\", "test.xlsx", array);
		// writeXls(array, "D:\\test.xls");
		/*
		 * try { writer("D:\\", "test", "xlsx"); } catch (IOException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); }
		 */

	}

}
