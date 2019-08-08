package com.cmwa.ecc.business.dao.datamanager;

import java.util.List;

import com.cmwa.ecc.business.entity.accountManger.DocumentDto;
import com.cmwa.ecc.business.mybatis.annotation.CrmMybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 客户资料管理
 * 资料上传-CRM-BP库
 * @author ex-liuy
 *
 */
@CrmMybatisDao
public interface CustDataDocUploadDao {
	
	/**
	 * 新增 客户资料 附件
	 * @param document
	 */
	public void insertCustDataDoc(DocumentDto document);
	
	/**
	 * 查询所有附件
	 * @param sp
	 * @return
	 */
	public List<DocumentDto> queryCustDataDocAllList(SearchParam sp);
	
	/**
	 * 查询ID附件
	 * @param sp
	 * @return
	 */
	public DocumentDto queryCustDataDocById(SearchParam sp);
	
	
	/**
	 * 删除 附件
	 * @param sp
	 */
	public void custDataDocDel(DocumentDto document);
	
}
