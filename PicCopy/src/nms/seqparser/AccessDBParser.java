package nms.seqparser;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

public class AccessDBParser {
	
	public static LinkedHashMap<String, String> MIDToStr = new LinkedHashMap<String, String>();
	
	public static LinkedHashMap<String, String> SNToMID = new LinkedHashMap<String, String>();
	
	public static String[] snTables = new String[]{ "`序号管理`" , "`2012年7月前序号管理`" , "`序号管理2015年12月份`" ,"`2019年1月序号管理`" };
	
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
//		loadMidToMNo();
		
//		for(  int i=0; i<snTables.length ;i++  ){
//			loadSnToMid( snTables[i] );
//		}
		
		
		loadSnToMid(  snTables[0]  ) ;
		
		System.out.println( SNToMID.size()  );
		
		
		
	}
	
	public static void doInit(){
		
//		loadMidToMNo();
//		for(  int i=0; i<snTables.length ;i++  ){
//			loadSnToMid( snTables[i] );
//		}
		System.out.println("DB SN 个数:"+ SNToMID.size()  );
		
	}
	
	public static String getMStrBySN(String sn ){
		
		
		return  null;
	}
	
	
	public static void loadSnToMid( String tname ) throws ClassNotFoundException, SQLException, IOException{
		
		Connection con = null;
		Statement sql;
		ResultSet rs;
		con = getConnection();
		sql = con.createStatement();
		String sqlStr = " select ID, 产品编码ID  , 内部序号前缀  ,  内部序号起始  ,  内部序号结束  from   " + tname;
		rs = sql.executeQuery( sqlStr );
		int count = 0 ;
		while( rs.next() ) {
			String rowID = rs.getString(1);
			String mID = rs.getString(2);
			String snPrefix = rs.getString(3);
			String snStart = rs.getString(4);
			String snEnd = rs.getString(5);
			
			if(  snPrefix==null || snStart==null ||  snEnd==null  ){
				continue;
			}
			System.out.println(  rowID + " , "  +mID + " , " + snPrefix+ " , " + snStart   +  " , " + snEnd);
			List<String> tmpSns = parseSn( snPrefix ,  snStart  ,snEnd );
			for(String ttt :  tmpSns ){
				SNToMID.put( ttt, mID );
			}
			count ++ ;
		}

		System.out.println( "加载行 :" + count  );
		con.close();
		
	}
	
	private static List<String> parseSn(String snPrefix, String snStart, String snEnd) {
		List<String> ret = new ArrayList<String>();
		if( snStart.equals(snEnd) ){
			ret.add(snPrefix +snStart );
		}else{
			
			
			
			
		}
		return ret;
	}

	public static void loadMidToMNo() throws ClassNotFoundException, SQLException, IOException{
		Connection con = null;
		Statement sql;
		ResultSet rs;
		con = getConnection();
		sql = con.createStatement();
		String sqlStr = " SELECT 物料清单.物料ID, [物料类属] & [编码序号] AS 成品编码 FROM 物料类属 "+
							" RIGHT JOIN 物料清单 ON 物料类属.物料类属ID=物料清单.物料类属ID "+
							" ORDER BY [物料类属] & [编码序号]; ";
		
		rs = sql.executeQuery( sqlStr );
		
		while( rs.next() ) {
			String mid = rs.getString(1);
			String mdesc = rs.getString(2);
			MIDToStr.put(mid, mdesc);
		}
		
//		Set<Entry<String,String>> entrySet = midToStr.entrySet();
//		for(Entry<String,String> en : entrySet  ){
//			String key = en.getKey();
//			String value = en.getValue();
//			System.out.println(  key +" , " +value   );
//		}
		System.out.println( MIDToStr.size()   );
		
		con.close();
	}
	
	public static Connection getConnection() throws SQLException, IOException,
			ClassNotFoundException {
		// 连接Access数据库
		Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
		// 这个url就是前面的ucanaccess://+ 数据库存储路径
		String url = "jdbc:ucanaccess://D:/ForBdcom/test.mdb";
		String username = "";
		String password = "";
		// return DriverManager.getConnection(url, username, password);
		return DriverManager.getConnection(url);
	}
	
}
