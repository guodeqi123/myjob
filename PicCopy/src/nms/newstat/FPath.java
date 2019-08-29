package nms.newstat;

public class FPath {
	
	
	public final static String scanbasePath = "D:/ForBdcom/0stat1/扫描所有库位信息";
	
	//0829
	public final static String outDir = "D:/ForBdcom/0stat1/出库扫码记录";
	
	//0829
	public final static 	String indir1 = "D:/ForBdcom/0stat1/入库扫码记录";
	public final static 	String indir2 = "D:/ForBdcom/0stat1/入库扫码记录/nf";
	
	public final static String PNComparePath = "D:/ForBdcom/0stat1/物料档案比对.xls";
	
	//0829
	public final static String PNStatuspath = "D:/ForBdcom/0stat1/Data0829/物料档案（序列号管理）0829.xlsx";
	
	//0829
	public final static String storePath  = "D:/ForBdcom/0stat1/Data0829/0828收发存记录/物料编码异常_基于电子台账（0828)_0829确认完成.xlsx";
	
	//0829
	public final static String u8PathNew  = "D:/ForBdcom/0stat1/u8/现存量查询_2019.08.29.xlsx";
	public final static String u8PathOld  = "D:/ForBdcom/0stat1/u8/现存量查询_2019.08.28.xlsx";
	
	//0828   物料编码与价格对照
	public final static String u8PNToPrice  = "D:/ForBdcom/0stat1/u8/物料单价/现存量查___0190828.9.41.xlsx";
	
	
	//0829每日PN修正
	public static String fDir = "D:/ForBdcom/0stat1/Data0829/";
	public static String[][] PNAmendFiles =new String[][] {
		//sheet num , startrow , srcpncol , topncol
		{ fDir+"错误物料编码统计0827修改.xlsx" , "0", "1","0","1"  }, 
		{ fDir+"物料编码不在U8中存在0828修改确认.xlsx" , "0", "1","0","1"  }, 
	};
	
}
