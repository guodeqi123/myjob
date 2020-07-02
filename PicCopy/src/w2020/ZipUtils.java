package w2020;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;
import java.util.Enumeration;
import java.util.Properties;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

public class ZipUtils {
	/**
	 * ɾ��һ���ļ���
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
			// ɾ�����ļ�
			if (files[i].isFile()) {
				flag = files[i].delete();
				if (!flag)
					break;
			} // ɾ����Ŀ¼
			else {
				flag = deleteADir(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// ɾ����ǰĿ¼
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	public static void zip(String zipFileName, File inputFile) throws Exception {
		System.out.println("ѹ����...");

		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
				zipFileName));
		BufferedOutputStream bo = new BufferedOutputStream(out);
		zip(out, inputFile, inputFile.getName(), bo);
		bo.close();
		out.close(); // ������ر�

		System.out.println("ѹ�����");
	}

	public static void zip(ZipOutputStream out, File f, String base,
			BufferedOutputStream bo) throws Exception { // ��������
		if (f.isDirectory()) {
			if (base.indexOf(".svn") == -1) {
				File[] fl = f.listFiles();
				if (fl.length == 0) {
					out.putNextEntry(new ZipEntry(base + "/")); // ����zipѹ�������base
					System.out.println(base + "/");
				}
				for (int i = 0; i < fl.length; i++) {
					zip(out, fl[i], base + "/" + fl[i].getName(), bo); // �ݹ�������ļ���
				}
			}
		} else {
			out.putNextEntry(new ZipEntry(base)); // ����zipѹ�������base
			System.out.println(base);
			FileInputStream in = new FileInputStream(f);
			BufferedInputStream bi = new BufferedInputStream(in);
			int b;
			while ((b = bi.read()) != -1) {
				bo.write(b); // ���ֽ���д�뵱ǰzipĿ¼
			}
			bo.flush();
			bi.close();
			in.close(); // �������ر�

		}
	}

	/**
	 * ����properties�ļ��ļ�ֵ�� ����������Ѿ����ڣ����¸�������ֵ�� ��������������ڣ�����һ�Լ�ֵ��
	 * 
	 * @param keyname
	 *            ����
	 * @param keyvalue
	 *            ��ֵ
	 */
	public static void updateProperties(String folderPath, String filePath,
			String keyname, String keyvalue) {
		// ����ļ��в����ھʹ���
		try {
			File folder = new File(folderPath);
			if (!folder.exists() && !folder.isDirectory()) {
				System.out.println("//������");
				folder.mkdirs();
			}
			// ����ļ������ھʹ���
			File file = new File(filePath);
			if (!file.exists()) {

				file.createNewFile();

			}
			// else {
			// file.delete();
			// file.createNewFile();
			// }
			Properties props = new Properties();
			// ��ȡ�ļ�
			FileInputStream fis = new FileInputStream(filePath);
			props.load(fis);
			fis.close();
			// ���� Hashtable �ķ��� put��ʹ�� getProperty �����ṩ�����ԡ�
			// ǿ��Ҫ��Ϊ���Եļ���ֵʹ���ַ���������ֵ�� Hashtable ���� put �Ľ����
			keyvalue = keyvalue.replace("\\", "/");
			props.setProperty(keyname, keyvalue);
			FileOutputStream fos = new FileOutputStream(filePath);
			// ���ʺ�ʹ�� load �������ص� Properties ���еĸ�ʽ��
			// ���� Properties ���е������б�����Ԫ�ضԣ�д�������
			props.store(fos, "Update '" + keyname + "' value");

			fos.close();
		} catch (IOException e) {
			System.err.println("�����ļ����´���");
		}
	}

	public static String renameFolder(String path, String oldname,
			String newname) {
		new File(path + "/" + oldname).renameTo(new File(path + "/" + newname));
		return path + "/" + newname;
	}

	public static void zipFile(String fileName, String zipFileName) {
		File srcFile = new File(fileName);// Ҫѹ�����ļ�����
		File targetZipFile = new File(zipFileName);// ѹ������ļ���
		ZipOutputStream out = null;
		boolean boo = false;// �Ƿ�ѹ���ɹ�

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
				throw new RuntimeException("�ر�Zip����������쳣", ex);
			} finally {
				// �������
				if (!boo && targetZipFile.exists())// ѹ�����ɹ�,
					targetZipFile.delete();
			}

		}
	}

	/**
	 * ѹ��zip�ļ�
	 * 
	 * @param file
	 *            ѹ�����ļ�����
	 * @param out
	 *            ���ZIP��
	 * @param dir
	 *            ��Ը�Ŀ¼����
	 * @param boo
	 *            �Ƿ�ѿ�Ŀ¼ѹ����ȥ
	 */
	public static void zip(File file, ZipOutputStream out, String dir,
			boolean boo) throws IOException {

		if (file.isDirectory()) {// ��Ŀ¼
			if (file.getName().indexOf(".svn") == -1) {
				File[] listFile = file.listFiles();// �ó�Ŀ¼�����е��ļ�����

				if (listFile.length == 0 && boo) {// ��Ŀ¼ѹ��

					out.putNextEntry(new ZipEntry(dir + file.getName() + "/"));// ��ʵ��������ZIP����
//					System.out.println("ѹ��." + dir + file.getName() + "/");
					return;
				} else {

					for (File cfile : listFile) {

						zip(cfile, out, dir + file.getName() + "/", boo);// �ݹ�ѹ��
					}
				}
			}

		} else if (file.isFile()) {// ���ļ�

//			System.out.println("ѹ��." + dir + file.getName() + "/");

			byte[] bt = new byte[2048 * 2];

			ZipEntry ze = new ZipEntry(dir + file.getName());// ����ѹ��ʵ��
			// ����ѹ��ǰ���ļ���С
			ze.setSize(file.length());

			out.putNextEntry(ze);// //��ʵ��������ZIP����

			FileInputStream fis = null;

			try {

				fis = new FileInputStream(file);

				int i = 0;

				while ((i = fis.read(bt)) != -1) {// ѭ��������д�����Zip����

					out.write(bt, 0, i);
				}

			} catch (IOException ex) {
				throw new IOException("д��ѹ���ļ������쳣", ex);
			} finally {

				try {
					if (fis != null)
						fis.close();// �ر�������

				} catch (IOException ex) {

					throw new IOException("�ر������������쳣");
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


	//=============================unzip  unrar
	public static void unZip1(File zipFile, String outDir) throws IOException {

		File outFileDir = new File(outDir);
		if (!outFileDir.exists()) {
			boolean isMakDir = outFileDir.mkdirs();
			if (isMakDir) {
				System.out.println("����ѹ��Ŀ¼�ɹ�");
			}
		}

		ZipFile zip = new ZipFile(zipFile);
		for (Enumeration enumeration = zip.getEntries(); enumeration.hasMoreElements(); ) {
			ZipEntry entry = (ZipEntry) enumeration.nextElement();
			String zipEntryName = entry.getName();
			InputStream in = zip.getInputStream(entry);

			if (entry.isDirectory()) {      //����ѹ���ļ������ļ��е����
				File fileDir = new File(outDir + zipEntryName);
				fileDir.mkdir();
				continue;
			}

			File file = new File(outDir, zipEntryName);
			file.createNewFile();
			OutputStream out = new FileOutputStream(file);
			byte[] buff = new byte[1024];
			int len;
			while ((len = in.read(buff)) > 0) {
				out.write(buff, 0, len);
			}
			in.close();
			out.close();
		}
	}

	public static void unRar1(File rarFile, String outDir) throws Exception {
		File outFileDir = new File(outDir);
		if (!outFileDir.exists()) {
			boolean isMakDir = outFileDir.mkdirs();
			if (isMakDir) {
				System.out.println("����ѹ��Ŀ¼�ɹ�");
			}
		}
		Archive archive = new Archive(new FileInputStream(rarFile));
		FileHeader fileHeader = archive.nextFileHeader();
		while (fileHeader != null) {
			if (fileHeader.isDirectory()) {
				fileHeader = archive.nextFileHeader();
				continue;
			}
			File out = new File(outDir + fileHeader.getFileNameString());
			if (!out.exists()) {
				if (!out.getParentFile().exists()) {
					out.getParentFile().mkdirs();
				}
				out.createNewFile();
			}
			FileOutputStream os = new FileOutputStream(out);
			archive.extractFile(fileHeader, os);

			os.close();

			fileHeader = archive.nextFileHeader();
		}
		archive.close();
	}


}
