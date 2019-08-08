package com.cmwa.ecc.business.service.datamanager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.web.multipart.MultipartFile;

import com.cmwa.ecc.business.entity.accountManger.DocumentDto;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 客户资料管理
 * 附件上传 接口
 * @author ex-liuy
 *
 */
public interface CustDataDocUploadService {
	/**
	 * 新增 客户资料 附件
	 * @param document
	 */
	public JSONObject insertCustDataDoc(HttpServletRequest request,HttpServletResponse response,MultipartFile multipartFile);
	
	/**
	 * 
	 * @param sp
	 * @return
	 */
	public Page<DocumentDto> queryCustDataDocAllList(SearchParam sp);
	
	/**
	 * 删除 附件
	 * @param
	 */
	public JSONObject custDataDocDel(DocumentDto document);
	
	/**
	 * 下载附件
	 * @param
	 */
	public void custDataDocDownload(HttpServletRequest request,HttpServletResponse response,DocumentDto document);
}
