package nms.newstat.tonc3;

import java.util.HashMap;
import java.util.Map;

import nms.newstat.Convertor2;
import nms.newstat.FPath;
import nms.newstat.tonc2.LoadNCKWInfo;
import nms.newstat.tonc2.NCKwObj;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class Load16KbPNInfo {

	public static Map<String, String> pnToU8KW = new HashMap<String, String>();

	public static final String kb16PnInfoPath = "D:/ForBdcom/0stat1/Data0901/16_基板库库位表.xlsx";

	public static void load() {

		int sheeNum = 0;
		int startrow = 1;

		int pnCol = 1;
		int kwcol =3;

		Workbook wb = FPath.getWB(kb16PnInfoPath);
		Sheet sheetAt = wb.getSheetAt(sheeNum);
		int lastRowNum = sheetAt.getLastRowNum();
		lastRowNum = 1000;
		Row oneRow = null;
		for (int i = startrow; i < lastRowNum; i++) {
			oneRow = sheetAt.getRow(i);
			if (oneRow == null) {
				continue;
			}
			Cell pnCell = oneRow.getCell(pnCol);
			Cell kwCell = oneRow.getCell(kwcol);

			String pnStr = Convertor2.getCellValue(pnCell);
			String kwStr = Convertor2.getCellValue(kwCell);
			
			if (StringUtils.isEmpty(pnStr) || StringUtils.isEmpty(kwStr)) {
				continue;
			}

			pnStr = pnStr.trim().toUpperCase();
			kwStr = kwStr.trim().toUpperCase();
			if (kwStr.contains("/")) {
				kwStr = kwStr.split("/")[0];
			}

			String cckw = pnToU8KW.get(pnStr);
			if (cckw != null  /* && !cckw.equals(kwStr)*/  ) {
				System.out.println("PN存在重复库位" + pnStr);
			}
			if( "XKPON-CAR0072A".equals(pnStr) ){
				kwStr = "A1-2-1";
			}
			
			//debug 手动出去一些库位
//			if( "B1-1-1".equals(kwStr) ||  "B1-1-2".equals(kwStr)   ){
//				continue;
//			}
			
			NCKwObj ncKw = LoadNCKWInfo.getNCKw("01", "16", kwStr);
			if( ncKw == null ){
				System.out.println( "Load16KbPNInfo.load()库位无法在NC中找到 ：：：" + pnStr + " ,  "+  kwStr  );
			}
			pnToU8KW.put(pnStr, kwStr);
		}
		System.out.println(  "Load16KbPNInfo.load()  PNTO KW 总个数：：" +   pnToU8KW.size()  );
	}

	public static void main(String[] args) {
		
		LoadNCKWInfo.load();
		load();
		
	}

}
