package nms.newstat.tonc34;

import java.util.HashSet;
import java.util.Set;

public class SpecialHandle {
	
	public static Set<String> PNRemove = new HashSet<String>();
	
	public static void load(){
		
		//15库
		PNRemove.add("WGMAC-POW0312");
		
		//已出库， 没有出库记录
		PNRemove.add("XKSWI-SWI2021C");
		PNRemove.add("CBNNN-JHJ0151A");
		
	}
	
	public static void main(String[] args) {
		
		String a = "5楼";
		String a2 = "KWA9通道";
		int compareTo = a.compareTo(a2);
		
		System.out.println( compareTo );
		
		
	}
	
	
}
