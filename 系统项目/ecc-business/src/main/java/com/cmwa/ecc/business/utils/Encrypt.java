package com.cmwa.ecc.business.utils;

/******************************************
 *  这个类主要用于对密码进行加,由于加密理论讲是不加逆的
 *MD5算法.
 *  加密的方法为: addPwd()
 *  校验密码的方法为:checkPassword()
 *******************************************/

import java.security.MessageDigest;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Encrypt {

    public static void main(String[] args) throws Exception {
        Encrypt my = new Encrypt();
        System.out.println(my.base64Encode("200309030000001"));
        System.out.println(my.base64Encode("1234528888010676"));
        System.out.println(my.base64Encode("010676"));
        //System.out.println("12345678加密后为:"+my.addPwd("12345678"));
        //System.out.println("解密情况:"+my.checkPassword("12345678","25D55AD283AA400AF464C76D713C07AD"));
    }

    /**********************************************
     *解密方法:
     *参数: str_pwds 用户录入的密码
     *      str_passwd 从数据库查查出的用户加密的口令文本
     *
     *返回: boolean TRUE 正确 FALSE 为失败
     *
     ************************************************/

    private boolean checkPassword(String str_pwds, String str_passwd) {
        try {
            //String myinfo=str_pwds+"0";
            String myinfo = str_pwds;
            MessageDigest alga = MessageDigest.getInstance("MD5");
            //MessageDigest alga=MessageDigest.getInstance("SHA-1");
            alga.update(myinfo.getBytes());

            byte[] digesta = alga.digest();

            //MessageDigest algb=MessageDigest.getInstance("SHA-1");
            String ls_str = byte2hex(digesta);

            return ls_str.equals(str_passwd);
        }
        catch (java.security.NoSuchAlgorithmException ex) {
            System.out.println("没有这个加密算法请检查JDK版本");
        }
        return false;
    }

    /**********************************************
     *
     *将加密后的数据(二进制转为16进制的字符)
     *
     *
     *
     *
     ************************************************/
    private String byte2hex(byte[] b) { //二行制转字符串
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            }
            else {
                hs = hs + stmp;
            }
            if (n < b.length - 1) {
                hs = hs;
            }
        }
        //return hs.toUpperCase();
        return hs;
    }


    /**********************************************
     *解密方法:
     *参数: str_pwds 要加密的明文
     *
     *
     *返回: 加密过的密文
     *
     ************************************************/
    private String addPwd(String str_pwds) {
        try {
            //String myinfo=str_pwds+"0";
            String myinfo = str_pwds;
            MessageDigest alga = MessageDigest.getInstance("MD5");
            //MessageDigest alga=MessageDigest.getInstance("SHA-1");
            alga.update(myinfo.getBytes());

            byte[] digesta = alga.digest();

            //MessageDigest algb=MessageDigest.getInstance("SHA-1");
            String ls_str = byte2hex(digesta);

            return ls_str;
        }
        catch (java.security.NoSuchAlgorithmException ex) {
            System.out.println("没有这个加密算法请检查JDK版本");
        }
        return null;
    }


    /**********************************************
     *加密口令:
     *参数: str_pwds 要加密的明文
     *
     *
     *返回: 加密过的密文
     *
     ************************************************/
    public String passwordEncrypt(String str_pwds) {
        return addPwd(str_pwds);
    }

    /**********************************************
     *加密口令:
     *参数: str_pwds 要加密的明文
     *
     *
     *返回: 加密过的密文
     *
     ************************************************/
    public String passwordEncryptUpper(String str_pwds) {
        return addPwd(str_pwds).toUpperCase();
    }


    /**********************************************
     *检查口令是否相等:
     *参数: str_pwds 口令的明文
     *      str_passwd 口令的密文在数据库在保存
     *
     *返回: 加密过的密文
     *
     ************************************************/
    public boolean isPass(String str_pwds, String str_passwd) {
        return checkPassword(str_pwds, str_passwd);
    }


    public String base64Decode(String password)
            throws Exception {
        BASE64Decoder dc = new BASE64Decoder();
        String tmpStr = new String(dc.decodeBuffer(password));
        return tmpStr.substring(0, tmpStr.length() - 1);
    }

    public String base64Encode(String password)
            throws Exception {
        BASE64Encoder en = new BASE64Encoder();
        password = password + 1;
        return en.encode(password.getBytes());
    }
}
