package com.cmwa.ec.webapp.manager;

import com.cmwa.ec.trade.facade.dto.AmazonResult;
import com.cmwa.ec.trade.facade.dto.log.CmwaFileUploadRecordDto;

import java.io.InputStream;

/**
 * @author by ex-hezk
 * @Date 2019/1/18 9:10
 */
public interface AmazonS3Manager {

    /**
     * 通过key下载文件
     * @param key 文件名
     * @return
     */
    AmazonResult<byte[], String> downloadByKey(String key);

    /**
     * 上传指定文件名的文件
     * @param key 文件名
     * @param is 文件输入流
     * @param contentType
     * @param record
     * @return
     */
    AmazonResult<String, String> upload(String key, InputStream is, String contentType, CmwaFileUploadRecordDto record);
    
    /**
     * 通过key删除文件
     * @param key
     * @return
     */
	AmazonResult<String,String> removeObjectByKey(String key);
}
