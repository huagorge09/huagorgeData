package com.cmwa.ec.weixin.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.cmwa.ec.weixin.dto.BaseDto;


/**
 * <p>Title: DateUtils </p>
 * <p>Description: 日期通用工具. </p>
 * <p>Copyright: Copyright (c) 2004-2008</p>
 * <p>Company: Legion Technology</p>
 * @author 李建海
 * @version 1.0
 * @Create date: 2008-05-30
 *
 */

public class StringUtils {

	private static Logger log = Logger.getLogger(StringUtils.class.getName());
	public static String invalidCharSetDefault = "\'_&_;_$_@_\"_+_,_\\_-"; 
     
    /**
     * 判断字符串是否不包含非法字符集
     * @author songyb
     * @param str 待校验字符串
     * @param invalidCharSet 非法字符集
     * @return boolean
     */
    public static boolean checkStr(String str) {
        //没有输入非法字符集则用默认的
        return checkStr(str, invalidCharSetDefault);
    }

    /**
     * 判断字符串是否不包含非法字符集
     * @author songyb
     * @param str 待校验字符串
     * @param invalidCharSet 非法字符集
     * @return boolean
     */
    public static boolean checkStr(String str, String invalidCharSet) {
        String[] invalidCharList = null;
        //没有输入非法字符集则用默认的
        if (!isEmptyString(invalidCharSet)) {
            invalidCharList = invalidCharSet.split("_");
        } else {
            invalidCharList = invalidCharList;
        }
        //对参数进行循环检查，如发现有非法字符则返回“真”,检查当中忽略大小写
        for (int i = 0; i < invalidCharList.length; i++) {
            if (str != null
                && invalidCharList[i] != null
                && str.toLowerCase().indexOf(invalidCharList[i].toLowerCase()) >= 0) {
                System.out.println("参数含有非法字符：" + invalidCharList[i]);
                return true;
            }
        }
        return false;
    }
    
    /**
     * null判断  为空：true 不为空：false
     * @param str
     * @return
     */
    public static boolean isEmptyString(String str) {
        if (str == null || "".equals(str)) {
            return true;
        } else {
            return false;
        }
    }
     
    public static String toJson(String errorCode, String errorMsg) {
    	BaseDto dto = new BaseDto();
    	dto.setErrorCode(errorCode);
    	dto.setErrorMsg(errorMsg);
    	
        return toJson(dto);
    }
    public static String toJson(HashMap map) {

         return "";
    }
    public static String toJson(BaseDto dto) {
    	if(dto == null){
    		return "";
    	}
    	ObjectMapper mapper = new ObjectMapper();
        try {
			return mapper.writeValueAsString(dto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("toJson异常：",e);
		}
		return "";
    }
    /**
     * 拼消息报文到前置机
     * @param msgtype template:模板消息,service：客服消息 ,changeGroup:改变用户分组
     * @param msgcontent
     * @return
     */
    public static String toXmlMessage(String msgtype,String msgcontent){
    	Document document = DocumentHelper.createDocument();
		
		Element xml = document.addElement("xml");
		document.setRootElement(xml);
        
		Element MsgType = xml.addElement("MsgType");
		MsgType.addText(msgtype);
		
		Element MsgContent = xml.addElement("MsgContent");
		MsgContent.addText(msgcontent);
		
		return xml.asXML();
    }
    
    /**
     * 原始参数 ：param1=val1&param2=val2
     * 签名值 signed = MD5(param1=val1&param2=val2&tokenName=token)
     * 最后传的加密串参数:param1=val1&param2=val2&signName=signed
     * &sign=xxxxx
     * param1=val1&param2=val2&sign=xxxxx
     * @param paramMap 请求参数Map(最后传的加密串参数)
     * @param tokenName 加密是token的参数名
     * @param tokenval 加密是token的值
     * @param signName 传递签名串的参数名
     * @return
     */
    public static boolean validMd5Sign(Map paramMap,String tokenName,String tokenval,String signName){
    	log.info("paramMap="+(paramMap == null ? paramMap : paramMap.toString())+",tokenName="+tokenName+",tokenval="+tokenval+",signName="+signName);
    	//两个以上参数才验证，至少一个业务参数，一个sign
    	if(paramMap == null || paramMap.size() < 2){
    		return false;
    	}
    	
    	
    	List<String> paramList = new ArrayList<String>();
    	Iterator<String> it = paramMap.keySet().iterator();
    	
    	//获取业务参数名（sgin除外） list
    	while(it.hasNext()){
    		String pname = it.next();
    		if(!pname.equals(signName)){
    			paramList.add(pname);
    		} 
    	}
    	//MD5值
    	String sign = (String)paramMap.get(signName);
    	
    	//排序
    	Collections.sort(paramList);
    	StringBuffer sb = new StringBuffer();
    	for(int i=0; i < paramList.size(); i++){
    		String pname = paramList.get(i);
    		sb.append(pname+"="+paramMap.get(pname).toString()+"&");
    	}
    	String toSign = sb.toString()+tokenName+"="+tokenval;
    	log.info("paramStr="+toSign);
    	log.info("paramSign="+sign);
    	MD5 md5 = new MD5();
    	String signed = md5.getMD5ofStr(toSign);
    	log.info("signed="+signed+",验证结果="+signed.equals(sign));
    	return signed.equals(sign);
    	
    	
    	
    }
    
    /**
	 * 获取MD5参数
	 * @param paramStr : param1=val1&param2=val2&param3=val3
	 * @param tokenName token的参数名
	 * @param token 加密KEN，可以为空
	 * @return
	 */
	public static String getMd5SignString(String paramStr,String tokenName,String token){
		log.info("paramStr="+paramStr+",token="+token+",tokenName="+tokenName);
		if(paramStr == null || paramStr == "" || tokenName== null || tokenName == ""){
			return "";
		}
		
		String signed = "";
		String stoken = token;
		 
		String[] arr = paramStr.split("&");
		 
		HashMap mapParam = new HashMap();
		List paramList = new ArrayList();
		try{
			//把参数名排序
			for(int i = 0; i < arr.length; i++)
			{
				String para = arr[i];
				String[] proper = para.split("=");
				String val = "";
				if(proper.length > 1)
				{
					val = proper[1];
				}
				mapParam.put(proper[0], val);
				paramList.add(proper[0]);
			}
			
			Collections.sort(paramList);
			
			StringBuffer toSign = new StringBuffer();
			for(int i = 0; i < paramList.size(); i++){
				String pnm = (String)paramList.get(i);
				toSign.append(pnm+"="+mapParam.get(pnm)+"&");
			}
			String toSignStr = toSign.toString();
			 
			
			toSignStr = toSignStr+tokenName+"="+token;
			MD5 md5 = new MD5();
			signed = md5.getMD5ofStr(toSignStr);
			
			 
			
			
		}catch(Exception ee){
			 
			log.error("参数串格式错误",ee);
			

		}
		log.info("paramStr="+paramStr+",token="+token+",signed="+signed);
		return signed;
	}
	
	
	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}
	
	
	
	public static boolean isNotBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return false;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false) ) {
                return true;
            }
        }
        return false;
    }

}
