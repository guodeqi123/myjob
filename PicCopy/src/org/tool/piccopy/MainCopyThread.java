package org.tool.piccopy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.tool.piccopy.util.ExcelTool;

public class MainCopyThread implements Runnable{

	private MainApp app ; 
	
	public MainCopyThread( MainApp app ){
		this.app = app;
	}
	
	public static String ROOT_DIR = "result";
	public static String FILE_SEP = "/";
	public int copyCount = 0;
	
	public static String AREA_SEP_KEY = "AreaSep";
	public static String AREA_COL_KEY = "AreaCol";
	public static String PICNAME_COL_KEY = "PicNameCol";
	
	public static int AREA_COL = 1;
	public static int[] picCols = new int[]{ 4 , 0 };
	public static String AREA_SEP = "-";
	
	public static String region = "区";
	public static String county = "县";
	
	@Override
	public void run() {
		
		List<String> imgDirs = app.getImgDirs();
		String excelPath = app.getExcelPath();
		
		Map<String, String> nameToPath = loadAllImg( imgDirs ); 
		
		Properties pro = new Properties();
		try {
			FileInputStream fis = new FileInputStream(new File("conf/conf.properties"));
			pro.load( fis );
			fis.close();
			
			AREA_COL = Integer.parseInt( pro.getProperty(AREA_COL_KEY) );
			String property = pro.getProperty(PICNAME_COL_KEY);
			String[] split = property.split(",");
			if( split.length>0 ){
				picCols = new int[ split.length ];
				for( int i=0; i<split.length ;i++ ){
					picCols[i] = Integer.parseInt( split[i] );
				}
			}
			AREA_SEP =  pro.getProperty(AREA_SEP_KEY);
		} catch ( Exception e1) {
			e1.printStackTrace();
		}  
		
		File file = new File( ROOT_DIR );
		try {
			FileUtils.deleteDirectory(file);
		} catch ( Exception e) {
			e.printStackTrace();
		}
		file = new File( ROOT_DIR );
		file.mkdir();
		
		
		List<List<String>> rows = ExcelTool.readExcelToRows(excelPath);
		int size = rows.size();
		for(  int i=0; i<size ;i++ ) {
			List<String> tmpRow = rows.get(i);
			String picName = "";
			for( int a=0; a<picCols.length ;a++ ){
				String ttt = tmpRow.get( picCols[a] );
				picName = picName + ttt.trim();
			}
			
			String areaName = tmpRow.get(AREA_COL);
			String srcPicPath = nameToPath.get(picName);
			
			
			
			String[] split = areaName.split(AREA_SEP);
			String cityName = split[0].trim();
			String dirName = ROOT_DIR + FILE_SEP + cityName ;
			String detailArea = split[1].trim();
			if( detailArea.indexOf(region)!=-1 ){
				String ttt = detailArea.substring(0 , detailArea.indexOf(region) + 1);
				dirName = dirName + FILE_SEP + ttt;
			}
			if( detailArea.indexOf(county)!=-1 ){
				String ttt = detailArea.substring(0 , detailArea.indexOf(county) + 1);
				dirName = dirName + FILE_SEP + ttt;
			}
			
			if( srcPicPath == null ){
				app.appendErrorLog( "不能找到图片 : " + dirName + ";;;图片名称 : " + picName );
				continue;
				
			}
			String picSuffix = srcPicPath.substring( srcPicPath.lastIndexOf(".") );
			String newPicName = dirName + FILE_SEP + picName+picSuffix ;
			
			
			File tmpDir = new File( dirName );
			if( !tmpDir.exists() ){
				tmpDir.mkdirs();
			}
			File srcFile = new File(srcPicPath);
			File destFile = new File(newPicName);
			
			try {
				FileUtils.copyFile(srcFile, destFile);
				copyCount++;
			} catch ( Exception e) {
				app.appendErrorLog( "拷贝文件失败 : " + areaName + " ,图片名称 : " +picName  + " ; " );
				app.appendErrorLog( "原始路径 : " + srcPicPath +" 异常 : "+ e.getMessage() );
				e.printStackTrace();
			}
			
			app.appendLog( "拷贝文件:" + srcPicPath + " 至 : " + newPicName );
			int progres = (int) (((double)(i+1)/(double)size) * 100);
			app.setProgress(progres);
			if( i+1==size ){
				app.appendLog( "执行完毕!共拷贝文件:" + copyCount + " 个 " );
				app.setFinished();
			}
		}
		
	}


	private Map<String, String> loadAllImg(List<String> imgDirs) {
		Map<String, String> nameToPath = new HashMap<String, String>();
		int size = imgDirs.size();
		for( int i=0; i<size ;  i++ ){
			String tmpDir = imgDirs.get(i);
			Map<String, String> oneDirImg = loadImg(tmpDir);
			nameToPath.putAll(oneDirImg);
		}
		return nameToPath;
	}
	
	
	private Map<String, String> loadImg(String imgDir) {
		
		Map<String, String> nameToPath = new HashMap<String, String>();
		
		File rootDir = new File(imgDir);
		File[] listFiles = rootDir.listFiles();
		for( File ff :   listFiles ){
			String fName = ff.getName();
			String absolutePath = ff.getAbsolutePath();
			if( ff.isFile() ){
				String[] split = fName.split("\\.");
				String fNameWithoutPreffix = split[0];
				nameToPath.put(fNameWithoutPreffix, absolutePath);
			}else if( ff.isDirectory() ){
				Map<String, String> loadImg = loadImg(absolutePath);
				nameToPath.putAll(loadImg);
			}
		}
		
		return nameToPath;
	}
	
}
