package nms.newstat.tonc34;

import java.util.HashSet;
import java.util.Set;

public class SpecialHandle {
	
	public static Set<String> PNRemove = new HashSet<String>();
	
	public static void load(){
		
		//15��
		PNRemove.add("WGMAC-POW0312");
		
		//�ѳ��⣬ û�г����¼
		PNRemove.add("XKSWI-SWI2021C");
		PNRemove.add("CBNNN-JHJ0151A");
		
	}
	
	public static void main(String[] args) {
		
		String a = "5¥";
		String a2 = "KWA9ͨ��";
		int compareTo = a.compareTo(a2);
		
		System.out.println( compareTo );
		
		
	}
	
	
}
