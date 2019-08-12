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
	
	public static String[] snTables = new String[]{ "`��Ź���`" , "`2012��7��ǰ��Ź���`" , "`��Ź���2015��12�·�`" ,"`2019��1����Ź���`" };
	
	
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
		System.out.println("DB SN ����:"+ SNToMID.size()  );
		
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
		String sqlStr = " select ID, ��Ʒ����ID  , �ڲ����ǰ׺  ,  �ڲ������ʼ  ,  �ڲ���Ž���  from   " + tname;
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

		System.out.println( "������ :" + count  );
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
		String sqlStr = " SELECT �����嵥.����ID, [��������] & [�������] AS ��Ʒ���� FROM �������� "+
							" RIGHT JOIN �����嵥 ON ��������.��������ID=�����嵥.��������ID "+
							" ORDER BY [��������] & [�������]; ";
		
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
		// ����Access���ݿ�
		Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
		// ���url����ǰ���ucanaccess://+ ���ݿ�洢·��
		String url = "jdbc:ucanaccess://D:/ForBdcom/test.mdb";
		String username = "";
		String password = "";
		// return DriverManager.getConnection(url, username, password);
		return DriverManager.getConnection(url);
	}
	
}
