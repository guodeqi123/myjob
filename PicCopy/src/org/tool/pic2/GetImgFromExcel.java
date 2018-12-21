package org.tool.pic2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;

/**
 * @since 2013-04-22
 * 
 * @author Gerrard
 * 
 *         ��ȡexcel�� ͼƬ�����õ�ͼƬλ�ã�֧��03 07 ��sheet
 */
public class GetImgFromExcel {

	/**
	 * @param args
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static void main(String[] args) throws InvalidFormatException,
			IOException {

		// �����ļ�
		File file = new File("model/test.xls");

		// ������
		InputStream input = new FileInputStream(file);

		// ��ȡ�ļ���׺��
		String fileExt = file.getName().substring(
				file.getName().lastIndexOf(".") + 1);

		// ����Workbook
		Workbook wb = null;

		// ����sheet
		Sheet sheet = null;

		// ���ݺ�׺�ж�excel 2003 or 2007+
		if (fileExt.equals("xls")) {
			wb = (HSSFWorkbook) WorkbookFactory.create(input);
		} else {
			wb = new XSSFWorkbook(input);
		}

		// ��ȡexcel sheet����
		int sheetNumbers = wb.getNumberOfSheets();

		// sheet list
		List<Map<String, PictureData>> sheetList = new ArrayList<Map<String, PictureData>>();

		// ѭ��sheet
		for (int i = 0; i < sheetNumbers; i++) {

			sheet = wb.getSheetAt(i);
			// map�ȴ��洢excelͼƬ
			Map<String, PictureData> sheetIndexPicMap;

			// �ж���07����03�ķ�����ȡͼƬ
			if (fileExt.equals("xls")) {
				sheetIndexPicMap = getSheetPictrues03(i, (HSSFSheet) sheet,
						(HSSFWorkbook) wb);
			} else {
				sheetIndexPicMap = getSheetPictrues07(i, (XSSFSheet) sheet,
						(XSSFWorkbook) wb);
			}
			// ����ǰsheetͼƬmap����list
			sheetList.add(sheetIndexPicMap);
		}

		printImg(sheetList);

	}

	/**
	 * ��ȡExcel2003ͼƬ
	 * 
	 * @param sheetNum
	 *            ��ǰsheet���
	 * @param sheet
	 *            ��ǰsheet����
	 * @param workbook
	 *            ����������
	 * @return Map key:ͼƬ��Ԫ��������0_1_1��String��value:ͼƬ��PictureData
	 * @throws IOException
	 */
	public static Map<String, PictureData> getSheetPictrues03(int sheetNum,
			HSSFSheet sheet, HSSFWorkbook workbook) {

		Map<String, PictureData> sheetIndexPicMap = new HashMap<String, PictureData>();
		List<HSSFPictureData> pictures = workbook.getAllPictures();
		if (pictures.size() != 0) {
			for (HSSFShape shape : sheet.getDrawingPatriarch().getChildren()) {
				HSSFClientAnchor anchor = (HSSFClientAnchor) shape.getAnchor();
				if (shape instanceof HSSFPicture) {
					HSSFPicture pic = (HSSFPicture) shape;
					int pictureIndex = pic.getPictureIndex() - 1;
					HSSFPictureData picData = pictures.get(pictureIndex);
					String picIndex = String.valueOf(sheetNum) + "_"
							+ String.valueOf(anchor.getRow1()) + "_"
							+ String.valueOf(anchor.getCol1());
					sheetIndexPicMap.put(picIndex, picData);
				}
			}
			return sheetIndexPicMap;
		} else {
			return null;
		}
	}

	/**
	 * ��ȡExcel2007ͼƬ
	 * 
	 * @param sheetNum
	 *            ��ǰsheet���
	 * @param sheet
	 *            ��ǰsheet����
	 * @param workbook
	 *            ����������
	 * @return Map key:ͼƬ��Ԫ��������0_1_1��String��value:ͼƬ��PictureData
	 */
	public static Map<String, PictureData> getSheetPictrues07(int sheetNum,
			XSSFSheet sheet, XSSFWorkbook workbook) {
		Map<String, PictureData> sheetIndexPicMap = new HashMap<String, PictureData>();

		for (POIXMLDocumentPart dr : sheet.getRelations()) {
			if (dr instanceof XSSFDrawing) {
				XSSFDrawing drawing = (XSSFDrawing) dr;
				List<XSSFShape> shapes = drawing.getShapes();
				for (XSSFShape shape : shapes) {
					XSSFPicture pic = (XSSFPicture) shape;
					XSSFClientAnchor anchor = pic.getPreferredSize();
					CTMarker ctMarker = anchor.getFrom();
					String picIndex = String.valueOf(sheetNum) + "_"
							+ ctMarker.getRow() + "_" + ctMarker.getCol();
					sheetIndexPicMap.put(picIndex, pic.getPictureData());
				}
			}
		}

		return sheetIndexPicMap;
	}

	public static void printImg(List<Map<String, PictureData>> sheetList)
			throws IOException {

		for (Map<String, PictureData> map : sheetList) {
			Object key[] = map.keySet().toArray();
			for (int i = 0; i < map.size(); i++) {
				// ��ȡͼƬ��
				PictureData pic = map.get(key[i]);
				// ��ȡͼƬ����
				String picName = key[i].toString();
				// ��ȡͼƬ��ʽ
				String ext = pic.suggestFileExtension();

				byte[] data = pic.getData();

				FileOutputStream out = new FileOutputStream("D:\\pic" + picName + "." + ext);
				out.write(data);
				out.close();
			}
		}

	}

}