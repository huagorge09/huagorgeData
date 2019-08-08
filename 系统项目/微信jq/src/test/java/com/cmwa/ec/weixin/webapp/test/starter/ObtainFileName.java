package com.cmwa.ec.weixin.webapp.test.starter;

import java.io.File;

public class ObtainFileName {

	
	public static void main(String[] args) {
		// 指定一个目录
		wx();
//		pc();
//
	}

	private static void wx() {
		File file = new File("E:\\workSpace\\cmwa-wx-webapp\\src\\main\\webapp\\WeixinWeb\\WeixinWeb_JSLibrary\\pageReq");
		String outFile="E:\\workSpace\\cmwa-wx-webapp\\src\\main\\webapp\\WeixinWeb\\WeixinWeb_JSLibrary\\pageReq.min\\";
		// public String[] list():获取指定目录下的所有文件或者文件夹的名称数组
		String[] strArray = file.list();
		String temp = "";
		String temp1 = "";
		String temp2 = "";
		for (String s : strArray) {
			temp = "ajaxmin  E:\\workSpace\\cmwa-wx-webapp\\src\\main\\webapp\\WeixinWeb\\WeixinWeb_JSLibrary\\pageReq\\" + s + "  -o  " +outFile+ s.split("\\.")[0]+".min."+s.split("\\.")[1];
			System.out.println(temp);
			temp = "";  
			temp1 = ""; 
			temp2 = ""; 
		}
	}
	private static void pc() {
		File file = new File("D:\\work\\cmfworkspaces\\cmwa-ec-webapp\\src\\main\\webapp\\AppWeb\\AppWeb_JSLibrary\\pageReq");
		String outFile="D:\\work\\cmfworkspaces\\cmwa-ec-webapp\\src\\main\\webapp\\AppWeb\\AppWeb_JSLibrary\\pageReq.min\\";
		// public String[] list():获取指定目录下的所有文件或者文件夹的名称数组
		String[] strArray = file.list(); 
		String temp = "";
		String temp1 = "";
		String temp2 = "";
		for (String s : strArray) {
			temp = "ajaxmin  D:\\work\\cmfworkspaces\\cmwa-ec-webapp\\src\\main\\webapp\\AppWeb\\AppWeb_JSLibrary\\pageReq\\" + s + "  -o  " +outFile+ s.split("\\.")[0]+".min."+s.split("\\.")[1];
			System.out.println(temp);
			temp = "";  
			temp1 = ""; 
			temp2 = ""; 
		}
	}

}
