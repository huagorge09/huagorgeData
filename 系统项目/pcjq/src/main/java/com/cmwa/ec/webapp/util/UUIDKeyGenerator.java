package com.cmwa.ec.webapp.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;

/**
 * <p>Title: UUIDKeyGenerator </p>
 * <p>Description: UUID生成器. </p>
 * <p>Copyright: Copyright (c) 2004-2007</p>
 * <p>Company: 览众科技（Legion Technology）</p>
 * @author 李建海（Jianhai Lee）
 * @version 1.0
 * @Create date:
 * @Update date:
 * @todo: Nothing
 */

public class UUIDKeyGenerator {

    public UUIDKeyGenerator() {
    	
    }

    /**
     * 取当前时间（精确道毫秒）的指定位数的16进制字符串
     * @throws Exception
     * @return String: 当前时间的指定位数的16进制字符串
     */
    public String generateUUIDKey() {
        //8位 + 16位 + 8位
        //BE2A3329 C0A801B1007541F8 697C7A86
        StringBuffer buffer = new StringBuffer(32);
        StringBuffer bf = new StringBuffer(16);
        byte addr[] = null;
        try {
            addr = InetAddress.getLocalHost().getAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        bf.append(intToHexString(byteToInt(addr), 8));
        bf.append(intToHexString(System.identityHashCode(this), 8));
        String midValue = bf.toString();
        buffer.append(intToHexString((int)(System.currentTimeMillis() & -1L), 8));
        buffer.append(midValue);
        
        SecureRandom secureRandom = new SecureRandom();
        buffer.append(intToHexString(secureRandom.nextInt(), 8));
        return buffer.toString();
    }

    /**
     * 取当前时间（精确道毫秒）的指定位数的16进制字符串
     * @param stringLength 指定位数
     * @throws Exception
     * @return String: 当前时间的指定位数的16进制字符串
     */
    public String getCurrentTime(int stringLength) {
        StringBuffer currentTime = new StringBuffer(stringLength);
        //currentTime.append(intToHexString((int)(System.currentTimeMillis() & -1L), stringLength));
        currentTime.append(intToHexString((int)(System.currentTimeMillis()/1000), stringLength));
        return currentTime.toString();
    }

    /**
     * 取一个随机数的指定位数的16进制字符串
     * @param stringLength 指定位数
     * @throws Exception
     * @return String: 随机数的指定位数的16进制字符串
     */
    public String getRandomNum(int stringLength) {
        StringBuffer randomNum = new StringBuffer(stringLength);
        SecureRandom secureRandom = new SecureRandom();
        randomNum.append(intToHexString(secureRandom.nextInt(), stringLength));
        return randomNum.toString();
    }

    /**
     * 取机器码的指定位数的16进制字符串
     * @param stringLength 指定位数
     * @throws Exception
     * @return String: 机器码的指定位数的16进制字符串
     */
    public String getMachineID(int stringLength) {
        StringBuffer machineID = new StringBuffer(stringLength);
        byte addr[] = null;
        try {
            addr = InetAddress.getLocalHost().getAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        machineID.append(intToHexString(byteToInt(addr), stringLength));
        return machineID.toString();
    }

    /**
     * int转换成指定位数16进制字符串
     * @param value int值
     * @param stringLength 指定位数
     * @throws Exception
     * @return String: 16进制字符串
     */
    public String intToHexString(int value, int stringLength) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                            'A', 'B', 'C', 'D', 'E', 'F'};
        StringBuffer buffer = new StringBuffer(stringLength);
        int shift = stringLength - 1 << 2;
        for (int i = -1; ++i < stringLength;){
            buffer.append(hexDigits[value >> shift & 0XF]);
            value <<= 4;
        }
        return buffer.toString();
    }

    /**
     * byte转换成int
     * @param bytes byte
     * @throws Exception
     * @return int: int
     */
    public static int byteToInt(byte bytes[]) {
        int value = 0;
        for (int i = -1; ++i < bytes.length;) {
            value <<= 8;
            int b = bytes[i] & 0XFF;
            value |= b;
        }
        return value;
    }

    public static void main(String[] args) {
    	
    	int i =100000;
    	while(i >0){
    		UUIDKeyGenerator u = new UUIDKeyGenerator();
    		System.out.println(u.getRandomNum(8));
    		i--;
    	}
    	
	}

}

