package org.tool.piccopy.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class FileUtil {

	
	 /** 
     * 复制文件或者目录,复制前后文件完全一样。 
     * 
     * @param resFilePath 源文件路径 
     * @param distFolder    目标文件夹 
     * @IOException  当操作发生异常时抛出 
     */ 
    public static void copyFile(String destDir,String key,String distFolder) throws IOException { 
		String resFilePath=destDir + "/" + key;
        File resFile = new File(resFilePath); 
        File distFile = new File(distFolder); 
        if (resFile.isDirectory()) { 
                FileUtils.copyDirectoryToDirectory(resFile, distFile); 
        } else if (resFile.isFile()) { 
                FileUtils.copyFileToDirectory(resFile, distFile, true); 
        } 
    }
    
	/**
	 * 清理文件(目录或文件)
	 * 
	 * @param file
	 */
	public static void deleteDirectory(File file) {
		if (file.isFile()) {
			file.delete();// 清理文件
		} else {
			File list[] = file.listFiles();
			if (list != null) {
				for (File f : list) {
					deleteDirectory(f);
				}
				file.delete();// 清理目录
			}
		}
	}
	
}
