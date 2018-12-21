package test;

import java.util.concurrent.locks.LockSupport;

public class ThreadTest extends Thread{

	private ThreadTest me = this;
	
	private Object sync1 = new Object();
	
	@Override
	public void run() {
		
		try {
			synchronized (sync1) {
				sync1.wait();
			}
//			Thread.sleep(10 * 1000);
			System.out.println( " I am awake. " );
		} catch (InterruptedException e) {
			System.err.println(  e.getMessage() );
		}
		System.out.println(  "3333" +"Thread Run finished" );
		
		class aaa {
			
		}
	}
	
	class aaa {
		
	}

	public static void main(String[] args) throws InterruptedException {
		
//		ThreadTest threadTest = new ThreadTest();
//		threadTest.start();
//		Thread.sleep(2000);
//		System.out.println(  "1111" + threadTest.isInterrupted()   );
//		threadTest.interrupt();
//		System.out.println(  "2222" + threadTest.isInterrupted()   );
//		class aaa {
//			
//		}
		String name = "COS";//67.79.83
		StringBuilder sb = new StringBuilder();
		for ( int i = 0; i < name.length(); i++ ) {
			char cc = name.charAt(i);
			if ( i == 0 ) {
				sb.append( (byte) cc );
			} else {
				sb.append(".").append( (byte)cc );
			}
		}
		System.out.println(  sb.toString()  );
		
	}
	
	
	
	
}
