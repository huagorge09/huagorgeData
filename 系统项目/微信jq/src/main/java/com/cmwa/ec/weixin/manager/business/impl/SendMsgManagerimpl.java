package com.cmwa.ec.weixin.manager.business.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.cmwa.ec.weixin.dao.CustServiceInfoDao;
import com.cmwa.ec.weixin.dto.BatchSendMsgDto;
import com.cmwa.ec.weixin.dto.UserBaseInfoDto;
import com.cmwa.ec.weixin.manager.business.SendMsgManager;
import com.cmwa.ec.weixin.thread.BatchSendMsgThread;
import com.cmwa.ec.weixin.util.ParseExcel;

public class SendMsgManagerimpl implements  SendMsgManager{
	
	private Logger logger = Logger.getLogger(SendMsgManagerimpl.class);
	
	@Value("${wxFundElePicPre}")
	private String uploadPath;
	
	@Autowired
	private CustServiceInfoDao custServiceInfoDao;
	
	@SuppressWarnings({ "unused", "null" })
	@Override
	public JSONObject batchSendMsg(BatchSendMsgDto dto,
			HttpServletRequest request) {
		// 需要发送的手机号码文件
		MultipartFile sendFile = dto.getSendFile();
		// 需要发送的手机号码文件名
		String sendFileName = dto.getSendFileName();
		// 手动输入的手机号码
		String otherMobile = dto.getOtherMobile();
		// 需要发送的短息内容
		String content = dto.getContent();
		// 需要发送短息的号码
		Map<String, Object> map = new HashMap<String, Object>();
		String regex = "^0?(13[0-9]|15[012356789]|18[012356789]|14[57]|17[03678])[0-9]{8}$";
		JSONObject returnObject = new JSONObject();
		if(StringUtils.isBlank(content)){
			returnObject.put("resultCode", "9999");
			returnObject.put("resultMsg", "发送内容为空");
			return returnObject;
		}
		List<String> sendList = null;
		if(!StringUtils.isBlank(sendFileName) && !sendFileName.endsWith(".xlsx")){
			returnObject.put("resultCode", "9999");
			returnObject.put("resultMsg", "请上传以.xlsx为后缀的文件");
			return returnObject;
		}
		if("3".equals(dto.getSendType())){
			File file = new File(uploadPath);
			if(!file.exists()){
				file.mkdir();
			}
			if(StringUtils.isBlank(sendFileName)){
				sendFileName = "1.xlsx";
			}
			if(!sendFile.isEmpty()){
				String filePathOne = uploadPath+File.separator+System.currentTimeMillis()+sendFileName;
				try{
					sendFile.transferTo(new File(filePathOne));
					logger.info("sendMsgManagerImpl.batchSendMsg---获取到发送号码文件与过滤号码文件 分别保存在"+filePathOne);
				}catch(IOException e){
					logger.error("sendMsgManagerImpl.batchSendMsg: excetion--",e);
					returnObject.put("resultCode", "9999");
					returnObject.put("resultMsg", "保存文件时出错");
					return returnObject;
				}
				try{
					sendList = ParseExcel.getRowValue(filePathOne,0);
				}catch(Exception e){
					logger.error("sendMsgManagerImpl.batchSendMsg: excetion--",e);
					returnObject.put("resultCode", "9999");
					returnObject.put("resultMsg", "文件已损坏");
					return returnObject;
				}finally{
					File delFile = new File(filePathOne);
					boolean flagOne = delFile.delete();
					logger.info("sendMsgManagerImpl.batchSendMsg:删除文件结果》》》》》sendFile:"+flagOne);
				}
			}
		}else if("2".equals(dto.getSendType())){
			List<UserBaseInfoDto> list = custServiceInfoDao.queryUserBaseList(dto.getUserType());
			sendList = new ArrayList<String>();
			for(UserBaseInfoDto d : list){
				sendList.add(d.getMobile());
			}
		}else if("1".equals(dto.getSendType())){
			sendList = new ArrayList<String>();
			if(!StringUtils.isBlank(otherMobile)){
				String[] split = otherMobile.split(",");
				for(String s : split){
					sendList.add(s.trim());
				}
			}
		}
		// 去重
		HashSet<String> set = new HashSet<String>(sendList);
		sendList.clear();
		sendList.addAll(set);
		// 判断号码是否正确
		StringBuffer errorMsg = new StringBuffer("错误的号码有：");
		int errorCount = 0;
		if(sendList!=null && sendList.size() != 0){
			for (String key : sendList) {
				if(!key.matches(regex)){
					errorMsg.append(key+"；");
					errorCount++;
				}else{
					if(content.indexOf("XXX") != -1){
						if(!StringUtils.isBlank(dto.getUrl())){
							StringBuilder sb = new StringBuilder();
							sb.append(dto.getUrl().trim());
							sb.append("?tel=");
							sb.append(key);
							try {
								String string = shortUrl(sb.toString());
								map.put(key, content.replace("XXX",string));
							} catch (Exception e) {
								logger.error("SendMsgManagerimpl>>>>批量发送短信>>>调用shortUrl返回短链接方法时发送异常，异常信息:"+e);
								e.printStackTrace();
							}
						}
					}else{
						map.put(key, content);
					}
				}
			}
		}
		logger.info(">>>>>>检查号码格式,"+errorMsg);
		// 去重
		if(map.size()!=0){
			// 执行发送短信线程
			BatchSendMsgThread thread = new BatchSendMsgThread(map);
			thread.start();
			returnObject.put("resultCode","0000");
			returnObject.put("resultMsg", "执行成功,发送成功:"+map.size()+"条,发送失败:"+errorCount +"条");
			logger.info("sendMsgManagerImpl.batchSendMsg》》》》执行完毕！");
			return returnObject;
		} else {
			returnObject.put("resultCode", "9999");
			returnObject.put("resultMsg", errorMsg);
			return returnObject;
		}
	}
	
	/**
	 * 返回短链接网址
	 * @param url
	 * @return
	 * @throws Exception
	 */
	private String shortUrl(String url) throws Exception{
		URL u = new URL("http://h5ip.cn/index/api?url="+url);
        InputStream in = u.openStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte buf[] = new byte[1024];
            int read = 0;
            while ((read = in.read(buf)) > 0) {
                out.write(buf, 0, read);
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
        byte b[] = out.toByteArray();
        String result=new String(b, "utf-8");
        System.out.println("返回值："+result);
		return result;
	}
	
}
