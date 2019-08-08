package com.cmwa.ecc.business.dao.bank;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ecc.business.entity.bank.BankBnkbaseVo;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;


@MybatisDao
public interface BankBaseDao {
	
	
	/**
	 * 银行基本信息列表
	 * @param sp
	 * @return
	 */
	public List<BankBnkbaseVo> queryBankBaseInfoListPage(SearchParam sp);
	public List<BankBnkbaseVo> queryBankBaseInfoList(SearchParam sp);
	
	
	public int queryBankBaseInfoListTotal(SearchParam sp);

	/**
	 * 删除银行基本信息
	 * @param bnkNo
	 */
	public void delBankBase(@Param("bnkNo")String bnkNo);
	
	/**
	 * 新增银行基本信息
	 * @param bankBaseInfo
	 */
	public void addBankBaseInfo(BankBnkbaseVo bankBaseInfo);
	
	/**
	 * 修改银行基本信息
	 * @param bankBaseInfo
	 */
	public void updateBankBaseInfo(BankBnkbaseVo bankBaseInfo);
	
	/**
	 * 获取柜台支持的银行信息
	 * @return
	 */
	public List<BankBnkbaseVo> queryDsBankBase(SearchParam sp);
}
