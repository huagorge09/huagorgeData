package com.cmwa.ec;

import java.io.File;

public class ObtainFileName {

	
	public static void main(String[] args) {
		// 指定一个目录
		File file = new File("C:\\pageReq");

		// public String[] list():获取指定目录下的所有文件或者文件夹的名称数组
		String[] strArray = file.list();
		String temp = "";
		String temp1 = "";
		String temp2 = "";
		for (String s : strArray) {
			temp = "ajaxmin  " + s + "  -o  " + s.split("\\.")[0]+".min."+s.split("\\.")[1];
			System.out.println(temp);
			temp = "";  
			temp1 = ""; 
			temp2 = ""; 
		}

	}

}
