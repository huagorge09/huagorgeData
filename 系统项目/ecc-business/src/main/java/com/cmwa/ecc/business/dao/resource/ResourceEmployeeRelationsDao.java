package com.cmwa.ecc.business.dao.resource;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ecc.business.commonVo.ResourceEmployeeRelationsVo;
import com.cmwa.ecc.business.mybatis.annotation.BaseDao;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * @TODO	资源-角色
 * @author ex-liuy
 * @createDate 2017年6月6日
 */
@MybatisDao
public interface ResourceEmployeeRelationsDao extends BaseDao<ResourceEmployeeRelationsVo>{
	/**
	 * 批量新增资源员工关系数据
	 * @author ex-liuy
	 * @param param
	 */
	public void batchInsert(SearchParam param);
	/**
	 * @TODO	传入资源id或者员工id 删除
	 * @author ex-liuy
	 * @param resId
	 * @param empId
	 */
	public void deleteRelByResOrEmpId(@Param("resId")String resId,@Param("empId")String empId);
}
