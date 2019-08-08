package com.cmwa.ecc.business.dao.dsquery;

import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;

@MybatisDao
public interface DSQueryDao {
	public String getCustId(@Param("opId")String opId);
	public String queryUrlByCode(@Param("permissionId")String permissionId);
	public String querySSOKey();
	/**
	 * 查询直销柜台系统状态等
	 * @return
	 */
	Map<String, Object> queryEccSystemInfo();
}
