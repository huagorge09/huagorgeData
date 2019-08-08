package com.cmwa.ecc.business.dao.common;

import java.util.List;

import com.cmwa.ecc.business.entity.dsbanks.DSBanksVo;
import com.cmwa.ecc.business.entity.paramvo.ParameterVo;
import com.cmwa.ecc.business.entity.workdate.WorkDateVo;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

@MybatisDao
public interface CommonDao {
	
	/**
	 * 获取参数列表
	 * @param sp
	 * @return
	 */
	public List<ParameterVo> getParameterListPage(SearchParam sp);
	
	/**
	 * 获取参数列表
	 * @param sp
	 * @return
	 */
	public List<ParameterVo> getParameterAllList(SearchParam sp);
	
	/**
	 * 业务管理获取参数列表
	 * @param sp
	 * @return
	 */
	public List<ParameterVo> queryBusinParameterListPage(SearchParam sp);
	
	
	/**
	 * 获取子参数数量
	 * @param sp
	 * @return
	 */
	public int getParameterListTotal(SearchParam sp);
	
	/**
	 * 获取银行信息
	 * @param sp
	 * @return
	 */
	public List<DSBanksVo> getBankDetail(SearchParam sp);
	
	/**
	 * 获取系统时间
	 * @param workDateVo
	 */
	public void getWorkDates(WorkDateVo workDateVo);
	
	
	/**
	 * 获取所有参数类型
	 * @param sp
	 * @return
	 */
	public List<ParameterVo> queryAllParameterListPage(SearchParam sp);
	/**
	 * 获取参数类型长度
	 * @param sp
	 * @return
	 */
	public int queryAllParameterTotal(SearchParam sp);

	
	/**
	 * 新增参数
	 * @param parameterVo
	 */
	public void addParameter(ParameterVo parameterVo);
	/**
	 * 修改参数
	 * @param parameterVo
	 */
	public void updateParameter(ParameterVo parameterVo);

	
	/**
	 * 删除参数
	 * @param parameterVo
	 */
	public void delParameter(ParameterVo parameterVo);
}