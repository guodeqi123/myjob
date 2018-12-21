package org.tool.piccopy.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

public class ZipUtils {
	/**
	 * 删除一个文件夹
	 * 
	 * @param dirpath
	 * @return
	 */
	public static boolean deleteADir(String dirpath) {
		File dirFile = new File(dirpath);
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = files[i].delete();
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteADir(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	public static void zip(String zipFileName, File inputFile) throws Exception {
		System.out.println("压缩中...");

		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
				zipFileName));
		BufferedOutputStream bo = new BufferedOutputStream(out);
		zip(out, inputFile, inputFile.getName(), bo);
		bo.close();
		out.close(); // 输出流关闭

		System.out.println("压缩完成");
	}

	public static void zip(ZipOutputStream out, File f, String base,
			BufferedOutputStream bo) throws Exception { // 方法重载
		if (f.isDirectory()) {
			if (base.indexOf(".svn") == -1) {
				File[] fl = f.listFiles();
				if (fl.length == 0) {
					out.putNextEntry(new ZipEntry(base + "/")); // 创建zip压缩进入点base
					System.out.println(base + "/");
				}
				for (int i = 0; i < fl.length; i++) {
					zip(out, fl[i], base + "/" + fl[i].getName(), bo); // 递归遍历子文件夹
				}
			}
		} else {
			out.putNextEntry(new ZipEntry(base)); // 创建zip压缩进入点base
			System.out.println(base);
			FileInputStream in = new FileInputStream(f);
			BufferedInputStream bi = new BufferedInputStream(in);
			int b;
			while ((b = bi.read()) != -1) {
				bo.write(b); // 将字节流写入当前zip目录
			}
			bo.flush();
			bi.close();
			in.close(); // 输入流关闭

		}
	}

	/**
	 * 更新properties文件的键值对 如果该主键已经存在，更新该主键的值； 如果该主键不存在，则插件一对键值。
	 * 
	 * @param keyname
	 *            键名
	 * @param keyvalue
	 *            键值
	 */
	public static void updateProperties(String folderPath, String filePath,
			String keyname, String keyvalue) {
		// 如果文件夹不存在就创建
		try {
			File folder = new File(folderPath);
			if (!folder.exists() && !folder.isDirectory()) {
				System.out.println("//不存在");
				folder.mkdirs();
			}
			// 如果文件不存在就创建
			File file = new File(filePath);
			if (!file.exists()) {

				file.createNewFile();

			}
			// else {
			// file.delete();
			// file.createNewFile();
			// }
			Properties props = new Properties();
			// 读取文件
			FileInputStream fis = new FileInputStream(filePath);
			props.load(fis);
			fis.close();
			// 调用 Hashtable 的方法 put，使用 getProperty 方法提供并行性。
			// 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
			keyvalue = keyvalue.replace("\\", "/");
			props.setProperty(keyname, keyvalue);
			FileOutputStream fos = new FileOutputStream(filePath);
			// 以适合使用 load 方法加载到 Properties 表中的格式，
			// 将此 Properties 表中的属性列表（键和元素对）写入输出流
			props.store(fos, "Update '" + keyname + "' value");

			fos.close();
		} catch (IOException e) {
			System.err.println("属性文件更新错误");
		}
	}

	public static String renameFolder(String path, String oldname,
			String newname) {
		new File(path + "/" + oldname).renameTo(new File(path + "/" + newname));
		return path + "/" + newname;
	}

	public static void zipFile(String fileName, String zipFileName) {
		File srcFile = new File(fileName);// 要压缩的文件对象
		File targetZipFile = new File(zipFileName);// 压缩后的文件名
		ZipOutputStream out = null;
		boolean boo = false;// 是否压缩成功

		try {

			CheckedOutputStream cos = new CheckedOutputStream(
					new FileOutputStream(targetZipFile), new CRC32());
			out = new ZipOutputStream(cos);

			// out = new ZipOutputStream(new BufferedOutputStream(new
			// FileOutputStream("c:/uploadd.zip")));

			zip(srcFile, out, "", true);

			boo = true;

		} catch (IOException ex) {
			throw new RuntimeException(ex);
		} finally {

			try {
				if (out != null)
					out.close();
			} catch (IOException ex) {
				throw new RuntimeException("关闭Zip输出流出现异常", ex);
			} finally {
				// 清理操作
				if (!boo && targetZipFile.exists())// 压缩不成功,
					targetZipFile.delete();
			}

		}
	}

	/**
	 * 压缩zip文件
	 * 
	 * @param file
	 *            压缩的文件对象
	 * @param out
	 *            输出ZIP流
	 * @param dir
	 *            相对父目录名称
	 * @param boo
	 *            是否把空目录压缩进去
	 */
	public static void zip(File file, ZipOutputStream out, String dir,
			boolean boo) throws IOException {

		if (file.isDirectory()) {// 是目录
			if (file.getName().indexOf(".svn") == -1) {
				File[] listFile = file.listFiles();// 得出目录下所有的文件对象

				if (listFile.length == 0 && boo) {// 空目录压缩

					out.putNextEntry(new ZipEntry(dir + file.getName() + "/"));// 将实体放入输出ZIP流中
//					System.out.println("压缩." + dir + file.getName() + "/");
					return;
				} else {

					for (File cfile : listFile) {

						zip(cfile, out, dir + file.getName() + "/", boo);// 递归压缩
					}
				}
			}

		} else if (file.isFile()) {// 是文件

//			System.out.println("压缩." + dir + file.getName() + "/");

			byte[] bt = new byte[2048 * 2];

			ZipEntry ze = new ZipEntry(dir + file.getName());// 构建压缩实体
			// 设置压缩前的文件大小
			ze.setSize(file.length());

			out.putNextEntry(ze);// //将实体放入输出ZIP流中

			FileInputStream fis = null;

			try {

				fis = new FileInputStream(file);

				int i = 0;

				while ((i = fis.read(bt)) != -1) {// 循环读出并写入输出Zip流中

					out.write(bt, 0, i);
				}

			} catch (IOException ex) {
				throw new IOException("写入压缩文件出现异常", ex);
			} finally {

				try {
					if (fis != null)
						fis.close();// 关闭输入流

				} catch (IOException ex) {

					throw new IOException("关闭输入流出现异常");
				}

			}
		}

	}
	
	
	public static void unZip(String srcZipFilePath, String outputDir) throws IOException {
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(srcZipFilePath);
			createDirectory(outputDir, null);
			Enumeration enums = zipFile.getEntries();
			while (enums.hasMoreElements()) {

				ZipEntry entry = (ZipEntry) enums.nextElement();


				if (entry.isDirectory()) {

					createDirectory(outputDir, entry.getName());

				} else {
					File tmpFile = new File(outputDir + "/" + entry.getName());

					createDirectory(tmpFile.getParent() + "/", null);

					InputStream in = null;

					OutputStream out = null;

					try {
						in = zipFile.getInputStream(entry);

						out = new FileOutputStream(tmpFile);

						int length = 0;

						byte[] b = new byte[2048];

						while ((length = in.read(b)) != -1) {
							out.write(b, 0, length);
						}

					} catch (Exception ex) {
						ex.printStackTrace();
					} finally {
						if (in != null)
							in.close();
						if (out != null)
							out.close();
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (zipFile != null) {
					zipFile.close();
					System.out.println("finish");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}
	
	private static void createDirectory(String outputDir, String subDir) {

		File file = new File(outputDir);

		if (!(subDir == null || subDir.trim().equals(""))) {

			file = new File(outputDir + "/" + subDir);
		}
		if (!file.exists()) {

			file.mkdirs();
		}

	}

}
