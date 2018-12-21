package org.tool.piccopy.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class FileUtil {

	
	 /** 
     * �����ļ�����Ŀ¼,����ǰ���ļ���ȫһ���� 
     * 
     * @param resFilePath Դ�ļ�·�� 
     * @param distFolder    Ŀ���ļ��� 
     * @IOException  �����������쳣ʱ�׳� 
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
	 * �����ļ�(Ŀ¼���ļ�)
	 * 
	 * @param file
	 */
	public static void deleteDirectory(File file) {
		if (file.isFile()) {
			file.delete();// �����ļ�
		} else {
			File list[] = file.listFiles();
			if (list != null) {
				for (File f : list) {
					deleteDirectory(f);
				}
				file.delete();// ����Ŀ¼
			}
		}
	}
	
}
