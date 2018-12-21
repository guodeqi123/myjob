package test;

public class MainTest {

	
	
	
	
	
	public static void main(String[] args) {
		
		
//		NMSDevice nmsDevice = new NMSDevice();
//		
//		nmsDevice.drawComponent();
		
		
		double nowg = 335 ;
		double addg1 = 100; 
		double nowMoney = 266.189 * nowg;
		double residueMoney1 = addg1 * 263;
		double residueMoney2 = addg1 * 265;
		double allg = nowg + addg1;
				
		System.out.println(  nowMoney   );
		System.out.println(  residueMoney1   );
		System.out.println(  residueMoney2   );
		
		System.out.println(" 263 add "+addg1+"g avg price: " + ( nowMoney + residueMoney1 ) / allg );
		System.out.println(" 265 add "+addg1+"g avg price: " + ( nowMoney + residueMoney2 ) / allg );
	}
	
	
	
	
}
