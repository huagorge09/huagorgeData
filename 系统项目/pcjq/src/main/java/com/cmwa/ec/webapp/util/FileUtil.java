package com.cmwa.ec.webapp.util;

import java.io.File;
import java.io.FileOutputStream;

public class FileUtil {

	/**
	 * 检查指定目录中是否有指定的文件
	 * 
	 * @param url
	 *            目录
	 * @param fileName
	 *            文件名称
	 * @return boolean
	 * @author maj
	 */
	public static boolean checkHasFile(String url, String fileName) {
		File file = new File(url);
		String[] fileArray = file.list();

		boolean flag = false;

		if (fileArray != null && fileArray.length > 0) {
			for (String name : fileArray) {
				if (fileName.equals(name)) {
					flag = true;
					continue;
				}
			}
		}

		return flag;
	}

	public static boolean writeFile(String url, String fileName, byte[] pic) {
		File file = new File(url + fileName);
		FileOutputStream fos = null;

		boolean flag = false;

		try {
			fos = new FileOutputStream(file);
			if (!file.exists()) {
				file.createNewFile();
			}
			fos.write(pic);// 写文件
			fos.flush();
			fos.close();
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

}
