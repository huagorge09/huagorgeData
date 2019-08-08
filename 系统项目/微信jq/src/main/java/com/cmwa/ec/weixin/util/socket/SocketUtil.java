package com.cmwa.ec.weixin.util.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.cmwa.ec.weixin.constants.WXConstants;
import com.cmwa.ec.weixin.util.cache.ParameterCache;

 
 
public class SocketUtil {

	private static Logger logger = Logger.getLogger(SocketUtil.class.getName());
	 
     
	/**
     * 通过HTTPS的方式将 指定的post内容发送数据并获取返回String值,
     * 并且立即取出XML的内容填充到PkgBodyDPERRep对象中返回给SingleVirement,
     * 前置机版
     * 
     * at 2019/01/29 因前置机的废除 该方法不再使用
     * @param pXmlStr String
     * @return InputStream
     * @throws Exception
     */
	@Deprecated()
    public static String sendSocketMessage(String postXmlStr) throws Exception
    {
    	String ip = ParameterCache.getValue(WXConstants.PMST_CONFIG, WXConstants.PMKY_PUBLIC, "socketIP");
    	int port = Integer.parseInt(ParameterCache.getValue(WXConstants.PMST_CONFIG, WXConstants.PMKY_PUBLIC, "socketPort"));
    	logger.info("----->>ip:"+ip+"----->>port:"+port);
    	Socket server = new Socket(ip, port);
    	server.setSoTimeout(5000);
    	DataInputStream input = new DataInputStream(server.getInputStream());
    	DataOutputStream output = new DataOutputStream(server.getOutputStream());
    	logger.info("1、连接成功！");

    	output.writeUTF(postXmlStr);
    	output.flush();
    	logger.info("2、发送消息:" + postXmlStr);
    			
    	String resultInfo = input.readUTF();
    	logger.info("3、接收发送消息的返回信息: " + resultInfo);
    	
    	 
        return resultInfo;
    }
    

}
