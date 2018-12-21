package test;

import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

public class View {
	
	public static int num = 1;
	
	protected void onDraw(){
		System.out.println(  "test.View" );
	}
	
	public static volatile ReentrantLock loc = new ReentrantLock();
	
	public static void main(String[] args) {
		
		
		new ThreadT("AAAA").start();;
		new ThreadT("BBBB").start();;
		
	}
	
	
	static class ThreadT extends Thread {
		
		public ThreadT(String name){
			super(name);
		}
		
		@Override
		public void run() {
		
//			loc.lock();
			try {
				
				for( int i=0 ; i<100000 ; i++){
					
					String name2 = this.getName();
					System.out.println( name2 + "------------------------"  );
					
					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
//				loc.unlock();
			}
			
		}
		
		
		
	}
	
}
