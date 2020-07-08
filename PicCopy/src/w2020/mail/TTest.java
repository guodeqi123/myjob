package w2020.mail;

public class TTest {
		
	
	
	public static void main(String[] args) throws Exception {
		System.out.println(  "AAAAAA" );
		Thread t1 = startThread( "111" );
		Thread t2 = startThread( "222"  );
		Thread t3 = startThread(  "333");
		
		t1.join();
		t2.join();
		t3.join();
		
		System.out.println(  "BBBBB" );
		
	}

	public static Thread startThread(String  name ) {
		
		
		Thread t = new Thread( name ){
			@Override
			public void run() {

				for(  int i=0; i<5 ;i++ ){
					System.out.println( name + " :: " +   i );
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
			}
		};
		
		t.start();
		return t;
	}
	
	
	
}
