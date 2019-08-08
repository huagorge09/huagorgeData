package com.cmwa.ecc.business.service.impl.datamanager;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cmwa.ecc.business.commonVo.Employee;
import com.cmwa.ecc.business.dao.datamanager.CustDataDocUploadDao;
import com.cmwa.ecc.business.entity.accountManger.DocumentDto;
import com.cmwa.ecc.business.service.datamanager.CustDataDocUploadService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.PageUtil;
import com.cmwa.ecc.business.utils.ResultConstant;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;

/**
 * 客户资料管理
 * 附件上传 接口
 * @author ex-liuy
 *
 */
@Service
public class CustDataDocUploadServiceImpl implements CustDataDocUploadService {
	private Logger logger = LoggerFactory.getLogger(CustDataDocUploadServiceImpl.class);
	/**
	 * CRM BP库
	 */
	@Autowired
	private CustDataDocUploadDao custDataDocUploadDao; 
	
	@Override
	public JSONObject insertCustDataDoc(HttpServletRequest request,HttpServletResponse response,MultipartFile multipartFile) {
		JSONObject result = new JSONObject();
		result.put(ResultConstant.C_RESULT_CODE,ResultConstant.C_RESULT_SUCCESS);
		result.put(ResultConstant.C_RESULT_MSG,ResultConstant.resultMap.get(ResultConstant.C_RESULT_SUCCESS));
		try{
			Employee emp = SessionUtils.getEmployee(request);
			String appserialno = request.getParameter("appserialno");
			String fundacct = request.getParameter("fundacct");
			String custno = request.getParameter("custno");
			
			if(null == multipartFile){
				result.put(ResultConstant.C_RESULT_CODE,ResultConstant.C_RESULT_FAILE);
				result.put(ResultConstant.C_RESULT_MSG,"文件接收错误，请重试！");
				return result;
			}
			
			if(StringUtils.isEmpty(custno)){
				result.put(ResultConstant.C_RESULT_CODE,ResultConstant.C_RESULT_FAILE);
				result.put(ResultConstant.C_RESULT_MSG,"找不到该基金账号对应的客户号！");
				return result;
			}
			
			//封装保存入库的 资料
			DocumentDto document = new DocumentDto();
			document.setCatalog("TCUSTOMERINFO");
			document.setFundacco(fundacct);
			document.setSubkeyid(custno);
			document.setCreateId(emp.getID());
			document.setFilename(multipartFile.getOriginalFilename());
			document.setFilesize(multipartFile.getSize());
			document.setFilecontent(multipartFile.getBytes());
			custDataDocUploadDao.insertCustDataDoc(document);
		}catch(Exception e){
			logger.error("----insertCustDataDoc-Exception-",e);
			result.put(ResultConstant.C_RESULT_CODE,ResultConstant.C_RESULT_FAILE);
			result.put(ResultConstant.C_RESULT_MSG,ResultConstant.resultMap.get(ResultConstant.C_RESULT_FAILE));
		}
		return result;
	}

	@Override
	public Page<DocumentDto> queryCustDataDocAllList(SearchParam sp) {
		List<DocumentDto> result = new ArrayList<DocumentDto>();
		List<DocumentDto> paramList = new ArrayList<DocumentDto>();
		try{
			paramList = custDataDocUploadDao.queryCustDataDocAllList(sp);
			PageUtil<DocumentDto> pageUtil = new PageUtil<DocumentDto>();
			result = pageUtil.getPageList(sp, paramList);
		}catch(Exception e){
			logger.error("----queryCustDataDocAllList-Exception-sp-"+sp.toString(),e);
		}
		return Page.create(result, sp.getStart(), sp.getLimit(), paramList.size());
	}

	@Override
	public JSONObject custDataDocDel(DocumentDto document) {
		JSONObject result = new JSONObject();
		result.put(ResultConstant.C_RESULT_CODE,ResultConstant.C_RESULT_SUCCESS);
		result.put(ResultConstant.C_RESULT_MSG,ResultConstant.resultMap.get(ResultConstant.C_RESULT_SUCCESS));
		try{
			custDataDocUploadDao.custDataDocDel(document);
		}catch(Exception e){
			logger.error("----custDataDocDel-Exception-"+document.toString(),e);
			result.put(ResultConstant.C_RESULT_CODE,ResultConstant.C_RESULT_FAILE);
			result.put(ResultConstant.C_RESULT_MSG,ResultConstant.resultMap.get(ResultConstant.C_RESULT_FAILE));
		}
		return result;
	}

	@Override
	public void custDataDocDownload(HttpServletRequest request,HttpServletResponse response,DocumentDto document) {
		ServletOutputStream out = null;
		try{
			SearchParam sp = new SearchParam();
			sp.getSp().put("storageid", document.getStorageid());
			DocumentDto paramDoc = custDataDocUploadDao.queryCustDataDocById(sp);
			if(null != paramDoc){
				response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(paramDoc.getFilename(), "UTF-8"));
				response.setHeader("Connection", "close");
				response.setHeader("Content-Type", "application/octet-stream");
				out = response.getOutputStream();
				out.write(paramDoc.getFilecontent());
			}
		}catch(Exception e){
			logger.error("----custDataDocDownload-Exception-"+document.toString(),e);
		}finally{
			if(null != out){
				try {
					out.flush();
				} catch (IOException e) {
					logger.error("----custDataDocDownload-out.flush-IOException-",e);
					e.printStackTrace();
				}
				try {
					out.close();
				} catch (IOException e) {
					logger.error("----custDataDocDownload-out.close-IOException-",e);
					e.printStackTrace();
				}
			}
		}
	}

}
