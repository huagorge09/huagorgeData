package com.cmwa.ecc.business.service.mail;

import java.util.List;

import com.cmwa.ecc.business.entity.mail.MailAppend;
import com.cmwa.ecc.business.entity.mail.MailAppendDetail;
import com.cmwa.ecc.business.entity.mail.MailConfPath;
import com.cmwa.ecc.business.entity.mail.MailConfig;
import com.cmwa.ecc.business.entity.mail.MailSendRecord;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

public interface MailService {
	
	/**
	 * 查询邮件的配置
	 * 
	 * @return
	 */
	Page<MailConfig> queryMailConfigListPage(SearchParam sp);
	
	
	/**
	 * 查找邮件的收件人和抄送人
	 * 
	 * @param configId
	 * @param category
	 *            //to cc
	 * @return
	 * @throws Exception
	 */
	List<MailAppendDetail> queryMailAppendList(String configId, String category) throws Exception;
	
	
	/**
	 * 查询用户的收件人和抄送人
	 * 
	 * @param id
	 * @return
	 */
	MailConfig queryMailConfig(String id) throws Exception;
	
	
	/**
	 * 修改邮件配置
	 */
	void updMailConfig(MailConfig config, List<MailAppend> appends) throws Exception;
	
	
	/**
	 * 发送邮件前保存发送记录
	 * 
	 * @param record
	 * @return
	 * @throws Exception
	 */
	String addRecord(MailSendRecord record) throws Exception;
	
	
	/**
	 * 保存发送邮件的结果
	 * 
	 * @param id
	 * @param result
	 * @throws Exception
	 */
	void setResult(String id, String result) throws Exception;

	/**
	 * @TODO	操作邮件运行状态
	 * @author ex-liuy
	 * @param maillConfig
	 */
	public void mailStatusOperate(MailConfig maillConfig);
	/**
	 * 保存手动输入邮件地址
	 * @author ex-lil
	 * @param mailConfPath
	 * @throws Exception
	 */
	MailConfPath queryMailPath(String confId) throws Exception;
	/**
	 * 保存手动输入邮件地址
	 * @author ex-lil
	 * @param mailConfPath
	 * @throws Exception
	 */
	void addMailPath(MailConfPath mailConfPath);
	/**
	 * 删除手动输入邮件地址
	 * @author ex-lil
	 * @param mailConfPath
	 * @throws Exception
	 */
	void deleteMailPath(MailConfPath mailConfPath);
	/**
	 * 更新手动输入邮件地址
	 * @author ex-lil
	 * @param mailConfPath
	 * @throws Exception
	 */
	void updateMailPath(MailConfPath mailConfPath);

    
   
	

	

	
	
	
	
}
