package org.tool.pic2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;

public class ReadPicFromDoc {
	

	public static void readPictureFromDoc(String path) throws Exception {
		FileInputStream in = new FileInputStream(new File(path));
		HWPFDocument doc = new HWPFDocument(in);
		int length = doc.characterLength();
		PicturesTable pTable = doc.getPicturesTable(); // int
		// TitleLength=doc.getSummaryInformation().getTitle().length();
		// System.out.println(TitleLength);
		// System.out.println(length);
		for (int i = 0; i < length; i++) {
			Range range = new Range(i, i + 1, doc);
			CharacterRun cr = range.getCharacterRun(0);
			if (pTable.hasPicture(cr)) {
				Picture pic = pTable.extractPicture(cr, false);
				String afileName = pic.suggestFullFileName();
				OutputStream out = new FileOutputStream(new File( "E:\\上海项目测试\\docImage\\" + UUID.randomUUID()+ afileName));
				pic.writeImageContent(out);
			}
		}
	}
	
	public static void readPictureFromDocx( String path ) {
		File file = new File(path);
		try {
			FileInputStream fis = new FileInputStream(file);
			XWPFDocument document = new XWPFDocument(fis);
			XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(document);
			String text = xwpfWordExtractor.getText();
			System.out.println(text);
			List<XWPFPictureData> picList = document.getAllPictures();
			for (XWPFPictureData pic : picList) {
				System.out.println(pic.getPictureType() + file.separator + pic.suggestFileExtension() + file.separator + pic.getFileName());
				byte[] bytev = pic.getData();
				FileOutputStream fos = new FileOutputStream( "E:\\上海项目测试\\docxImage\\" + pic.getFileName());
				fos.write(bytev);
			}
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取excel 2003表中的图片
	 * 
	 * @return
	 * @Param fis 文件输入流
	 * @Param sheetNum Excel表中的sheet编号
	 * @throws IOException
	 * @throws InvalidFormatException
	 * @throws EncryptedDocumentException
	 */
	public Map<String, PictureData> getPictureFromExcel2003(FileInputStream fis,
			int sheetNum) throws EncryptedDocumentException, InvalidFormatException, IOException {
		// 创建Map
		Map<String, PictureData> map = new HashMap<String, PictureData>();
		// 获取HSSFWorkbook对象
		HSSFWorkbook workbook = (HSSFWorkbook) WorkbookFactory.create(fis);
		// 获取图片HSSFPictureData集合
		List<HSSFPictureData> pictures = workbook.getAllPictures();
		// 获取当前表编码所对应的表
		HSSFSheet sheet = (HSSFSheet) workbook.getSheetAt(sheetNum - 1);
		// 对表格进行操作
		for (HSSFShape shape : sheet.getDrawingPatriarch().getChildren()) {
			HSSFClientAnchor anchor = (HSSFClientAnchor) shape.getAnchor();
			if (shape instanceof HSSFPicture) {
				HSSFPicture pic = (HSSFPicture) shape;
				// 获取行编号
				int row = anchor.getRow2();
				// 获取列编号
				int col = anchor.getCol2();
				int pictureIndex = pic.getPictureIndex() - 1;
				HSSFPictureData picData = pictures.get(pictureIndex);
				map.put(row + ":" + col, picData);
			}
		}
		return map;
	}

	

}
