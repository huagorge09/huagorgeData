 package com.cmwa.ec.weixin.controller;

import java.io.BufferedOutputStream;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.cmwa.ec.trade.facade.dto.AmazonResult;
import com.cmwa.ec.trade.facade.dto.log.CmwaFileUploadRecordDto;
import com.cmwa.ec.user.facade.dto.UserAccoRlaDto;
import com.cmwa.ec.weixin.manager.business.AmazonS3Manager;
import com.cmwa.ec.weixin.util.SessionValue;

import net.sf.json.JSONObject;

/**
 * @author by ex-hezk
 */
@Controller
@RequestMapping("/WeixinService/business/amazon")
public class AmazonS3Controller {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AmazonS3Manager amazonS3Manager;

    /**
     * 
     * 上传文件
     * @param fileType
     * @param recordSource
     * @param file
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/upload.xhtml",method = {RequestMethod.POST,RequestMethod.GET},produces="text/html;charset=UTF-8")
    @ResponseBody
	public String upload(@RequestParam("fileType") String fileType,
                         @RequestParam("recordSource") String recordSource,
                         @RequestParam("file") CommonsMultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        JSONObject returnObj = new JSONObject();
        try{
            String fileName = file.getOriginalFilename();
            if(file == null || StringUtils.isBlank(fileType)) {
                returnObj.put("returnCode","9999");
                returnObj.put("returnMsg","参数丢失");
                return returnObj.toString();
            }
            UserAccoRlaDto userAccoRlaDto = (UserAccoRlaDto) request.getSession().getAttribute(SessionValue.SESSION_USERACCORLA);
            if(userAccoRlaDto == null) {
                returnObj.put("returnCode","9999");
                returnObj.put("returnMsg","请实名之后再进行上传");
                return returnObj.toString();
            }
            String custno = userAccoRlaDto.getEcCustNo();
            fileName = fileName.replaceAll(" ","-");
            String key = custno.concat("-").concat(fileType).concat("-").concat(String.valueOf(System.currentTimeMillis())).concat("-").concat(fileName);
            logger.info("文件类型:{}",file.getContentType());
            logger.info("文件名:{}",fileName);
            logger.info("生成的key:{}",key);

            // 声明上传记录类
            CmwaFileUploadRecordDto record = new CmwaFileUploadRecordDto();
            record.setCmfuserid(String.valueOf(request.getSession().getAttribute(SessionValue.SESSION_CMFUSERID)));
            record.setRecordSource(recordSource);
            record.setFileName(fileName);
            record.setUpdatedUser("WxSystem");
            record.setCreatedUser("WxSystem");
            record.setFileType(fileType);
            AmazonResult<String,String> result = amazonS3Manager.upload(key, file.getInputStream(),file.getContentType(),record);
            returnObj.put("returnCode",result.getReturnCode());
            returnObj.put("returnMsg",result.getReturnMsg());
            returnObj.put("key",result.getData());
            returnObj.put("fileName",file.getOriginalFilename());
        }catch(Exception e) {
            logger.error("上传文件处理参数时捕获异常:",e);
            returnObj.put("returnCode","9999");
            returnObj.put("retrunMsg","error");
        }
        return returnObj.toString();
    }

    /**
     * 下载文件
     * @param key
     * @param resp
     */
    @RequestMapping(value = "/download/{key}.xhtml",produces="text/html;charset=UTF-8")
    @ResponseBody
    public void download(@PathVariable("key") String key,HttpServletResponse resp) {
        try{
            logger.info("接受到请求，key值为：{}",key);
            AmazonResult<byte[], String> result = amazonS3Manager.downloadByKey(key);
            String contentType =  result.getOtherData();
            byte[] bytes = result.getData();
            if(!StringUtils.isBlank(contentType)) {
               resp.setHeader("Content-Type",contentType);
            }
            if(bytes != null && bytes.length != 0) {
                BufferedOutputStream ops = new BufferedOutputStream(resp.getOutputStream());
                ops.write(bytes,0,bytes.length);
            }
        } catch(Exception e) {
            logger.error("下载文件捕获异常：{}",e);
        }
    }
    
    /**
     * 下载文件
     * @param key
     * @param resp
     */
    @RequestMapping(value = "/downloadVideo/{key}.xhtml",produces="text/html;charset=UTF-8")
    @ResponseBody
    public void downloadVideo(@PathVariable("key") String key,HttpServletResponse resp,HttpServletRequest req) {
    	try{
            logger.info("接受到请求，key值为：{}",key);
            AmazonResult<byte[], String> result = amazonS3Manager.downloadByKey(key);
            String contentType =  result.getOtherData();
            byte[] byteArray = result.getData();
            if(!StringUtils.isBlank(contentType)) {
	               resp.setHeader("Content-Type",contentType);
            }
            if(byteArray != null && byteArray.length != 0) {
        		String range = req.getHeader("Range");
        		if(!StringUtils.isBlank(range)) {
        			logger.info("本次range为："+range);
        			// 匹配不同格式的range
        			int start = -1;
        			int end = 0;
        			String contentRange = "";	
        			if(range.endsWith("-")) {
        				// 匹配500- 从500到最后一个字节的range
        				start = Integer.parseInt(range.replaceAll("-","").replaceAll("bytes=", ""));
        				end = byteArray.length;
        				contentRange =  "bytes "+start+"-"+(byteArray.length-1)+"/"+byteArray.length;
        			} else if (range.startsWith("-")) {
        				// 匹配 -500 最后500个字节的range
        				start = byteArray.length - Integer.parseInt(range.replaceAll("-","").replaceAll("bytes=", ""));
        				end = byteArray.length;
        				contentRange =  "bytes "+start+"-"+(byteArray.length-1)+"/"+byteArray.length;
        			} else {
        				// 匹配这样的类型 bytes=0-0,-1 || 500-600,601-999 || 0-500
        				String[] split = range.split(",");
        				if (split.length == 2) {
        					// 分段请求
        					if("-1".equals(split[1])) {
        						end = byteArray.length;
        						contentRange =  "bytes "+start+"-"+(byteArray.length-1)+"/"+byteArray.length;
        					}
        				} else { 
        					String[] temp = range.split("-");
        					start = Integer.parseInt(temp[0].replaceAll("bytes=", ""));
        					end = Integer.parseInt(temp[1]);
        					contentRange =  "bytes "+start+"-"+end+"/"+byteArray.length;
        				}
        			}
        			
        			logger.info("本次请求的start为："+start+",end为："+end);
        			resp.setStatus(206);
        			resp.setHeader("Accept-Range","bytes");
    				if(end < byteArray.length) {
    					end = end + 1;
    				}
    				byte[] copyOfRange = Arrays.copyOfRange(byteArray, start, end);
    				logger.info("截取的长度为:"+copyOfRange.length);
    				logger.info("设置的content-range属性为："+contentRange);
    				resp.setHeader("Content-Range", contentRange);
    				logger.info("文件返回类型为Content-Type:"+resp.getContentType());
    				resp.getOutputStream().write(copyOfRange);
        		} else {
        			resp.setStatus(200);
        			logger.info("文件返回类型为Content-Type:"+resp.getContentType());
        			resp.getOutputStream().write(byteArray);
        		}
            }
        } catch(Exception e) {
            logger.error("下载文件捕获异常：{}",e);
        }
    }
    
    
}
