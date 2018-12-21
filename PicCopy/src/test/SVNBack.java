package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SVNBack {
	
	
	public static void main(String[] args) throws Exception {
		
		String srcDir = "E:/Repositories/";
		String toDir = "E:/svnBack/";
		String suffix = ".dump";

		List<String> pNames = new ArrayList<String>();
		File file1 = new File("conf/SVNProject.txt");
		FileInputStream fis = new FileInputStream(file1);
		BufferedReader br = new BufferedReader( new InputStreamReader(fis) );
		String pname = null;
		while(  (pname=br.readLine())!=null ){
			pNames.add(pname);
		}
		System.out.println(  pNames.size() ); 
		
		System.out.println("----------------------Create Dump-------------------------");
		for( String tmpName : pNames ){
			//svnadmin dump E:\Repositories\fpp > e:\svnbak\fpp.dump将项目导出到e:\svnbak目录下
			String cmd = "svnadmin dump "+srcDir+tmpName+" > "+toDir+tmpName+".dump";
			System.out.println(cmd);
		}
		
		
		System.out.println("----------------------Load Dump-------------------------");
		String repDir = "E:/Repositories/";
		String dumpDir = "E:/svnBack/";
		for( String tmpName : pNames ){
			//svnadmin load /svndata/fpp < /root/fpp.dump
			String cmd = "svnadmin load "+repDir+tmpName+" < "+dumpDir+tmpName+".dump";
			System.out.println(cmd);
		}
		
		
	}
		
	
	
	
}
