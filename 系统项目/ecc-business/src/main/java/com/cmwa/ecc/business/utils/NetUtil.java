package com.cmwa.ecc.business.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetUtil {
	
	public static String getLocalHostIp() throws Exception {
		return getInetAddress().getHostAddress();
	}
	
	
	public static String getLocalHostName() throws Exception {
		return getInetAddress().getHostName();
	}
	
	
	public static InetAddress getInetAddress() {
		
		try{
			return InetAddress.getLocalHost();
		}catch(UnknownHostException e){
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	public static String getHostIp(InetAddress netAddress) {
		return netAddress != null?netAddress.getHostAddress() : null;
	}
	
	
	public static String getHostName(InetAddress netAddress) {
		return netAddress != null?netAddress.getHostName() : null;
	}
}
