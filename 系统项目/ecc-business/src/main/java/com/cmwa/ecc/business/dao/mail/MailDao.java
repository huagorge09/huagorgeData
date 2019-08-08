package com.cmwa.ecc.business.dao.mail;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ecc.business.entity.mail.MailAppend;
import com.cmwa.ecc.business.entity.mail.MailAppendDetail;
import com.cmwa.ecc.business.entity.mail.MailConfPath;
import com.cmwa.ecc.business.entity.mail.MailConfig;
import com.cmwa.ecc.business.entity.mail.MailSendRecord;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

@MybatisDao
public interface MailDao {
	
	/**
	 * 添加发送记录
	 */
	void addRecord(MailSendRecord record) throws Exception;
	
	
	/**
	 * 设置发送结果
	 * 
	 * @param id
	 * @param result
	 */
	void setResult(@Param("id") String id, @Param("result") String result);
	
	
	/**
	 * 删除附加信息的数据(每次修改都重新创建)
	 * 
	 * @param configId
	 */
	void deleteAppend(@Param("configId") String configId);
	
	
	/**
	 * 修改配置主表
	 * 
	 * @param config
	 */
	void updConfig(MailConfig config);
	
	
	/**
	 * 修改附加表
	 * 
	 * @param appends
	 */
	void addAppend(List<MailAppend> appends);
	
	
	/**
	 * 邮件列表
	 * 
	 * @return
	 */
	List<MailConfig> queryMailConfigListPage(SearchParam sp);
	
	
	/**
	 * 根据Id查询详情
	 * 
	 * @param id
	 * @return
	 */
	MailConfig queryMailConfig(@Param("configId") String id);
	
	
	/**
	 * 查询收件和和抄送人
	 * 
	 * @param configId
	 * @param category
	 *            //to cc
	 * @return
	 * @throws Exception
	 */
	List<MailAppendDetail> queryMailAppendList(@Param("configId")String configId, @Param("category")String category) throws Exception;
	
	/**
	 * @TODO	操作邮件运行状态
	 * @author ex-liuy
	 * @param maillConfig
	 */
	public void mailStatusOperate(MailConfig maillConfig);

	/**
	 * 保存手动输入邮件地址
	 * @param mailConfPath
	 */
	void addMailPath(MailConfPath mailConfPath);
	
	/**
	 * 根据confId查询手动输入邮件地址
	 * @param confId
	 */
	public MailConfPath queryMailPath(@Param("confId")String confId);
	
	/**
	 * 根据confId更新手动输入邮件地址
	 * @param confId
	 */
	void updateMailPath(MailConfPath mailConfPath);
	
	/**
	 * 根据confId删除手动输入的抄送邮件地址
	 * @param confId
	 */
	void deleteMailPath(MailConfPath mailConfPath);
}
