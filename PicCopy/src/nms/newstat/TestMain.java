package nms.newstat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nms.KWObj;
import nms.RowData;

public class TestMain {
	
	public static void main(String[] args) {
		
		
		String fp = "D:/ForBdcom/0stat1/A10»õ¼Ü.xlsx";
		Convertor2 c = new Convertor2(fp);
		Map<String, List<KWObj> > retA = c.parseExcel2( );
		List<KWObj> kwErrA = retA.get("error");
		List<KWObj> kwAllSuccessA = retA.get("success");
		List<String[]> errPnsA = c.writeSuccess2( kwAllSuccessA  , false);
		
		Map<String, RowData> aaa = new HashMap<String, RowData>();
		List<RowData> aaa2 = new ArrayList<RowData>();
		 
		int kwCount = kwAllSuccessA.size();
		for(  int kwi = 0 ; kwi<kwCount ; kwi++ ){
			KWObj kwObj = kwAllSuccessA.get(kwi);
			List<RowData> datas = kwObj.getRowSnList();
			int size = datas.size();
			for (int i = 0; i < size; i++) {
				RowData rowData = datas.get(i);
				String kw = rowData.getPosNum();
				String pn = rowData.getMaterialNum();
				String snsStr = rowData.getSnsStr();
				aaa.put( snsStr , rowData );
			}
			aaa2.addAll(datas);
		}
		
		System.out.println(  aaa2.size() + " ,   " +  aaa.size() + "===============" );
	}
	
	
}
