package com.cmwa.ec.webapp.manager.impl;


import com.cmwa.ec.trade.facade.dto.AmazonResult;
import com.cmwa.ec.trade.facade.dto.log.CmwaFileUploadRecordDto;
import com.cmwa.ec.webapp.client.TradeServiceClient;
import com.cmwa.ec.webapp.manager.AmazonS3Manager;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;

/**
 * @author by ex-hezk
 * @Date 2019/1/18 9:10
 */
public class AmazonS3ManagerImpl implements AmazonS3Manager {

    @Autowired
    private TradeServiceClient tradeServiceClient;

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    
    /* (non-Javadoc)
     * @see com.cmwa.ec.webapp.manager.AmazonS3Manager#downloadByKey(java.lang.String)
     */
    @Override
    public AmazonResult<byte[], String> downloadByKey(String key) {
    	try{
    		return tradeServiceClient.download(key);
    	} catch (Exception e) {
    		logger.error("下载文件时捕获异常：",e);
    		return AmazonResult.ERROR();
    	}
    }

   
    /* (non-Javadoc)
     * @see com.cmwa.ec.webapp.manager.AmazonS3Manager#upload(java.lang.String, java.io.InputStream, java.lang.String, com.cmwa.ec.trade.facade.dto.log.CmwaFileUploadRecordDto)
     */
    @Override
    public AmazonResult<String, String> upload(String key, InputStream is, String contentType, CmwaFileUploadRecordDto record) {
        try{
            byte[] bytes = IOUtils.toByteArray(is);
            return tradeServiceClient.upload(key, bytes, contentType,record);
        }catch(Exception e) {
            logger.error("上传文件时捕获异常：",e);
            return AmazonResult.ERROR();
        }
    }

	/* (non-Javadoc)
	 * @see com.cmwa.ec.webapp.manager.AmazonS3Manager#removeObjectByKey(java.lang.String)
	 */
	@Override
	public AmazonResult<String,String> removeObjectByKey(String key) {
		try{
            return tradeServiceClient.removeObjectByKey(key);
        }catch(Exception e) {
            logger.error("删除文件时捕获异常：",e);
            return AmazonResult.ERROR();
        }
	}
}
