package com.cmwa.ecc.business.dao.userInfo;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.cmwa.ecc.business.commonVo.LoginErrorVo;
import com.cmwa.ecc.business.commonVo.UserInfoVo;
import com.cmwa.ecc.business.mybatis.annotation.BaseDao;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;


@MybatisDao
public interface UserInfoDao extends BaseDao<UserInfoVo> {
	/**
	 * 查询用户信息
	 * @param userInfoVo
	 * @return
	 */
	List<UserInfoVo> searchUserInfoList(UserInfoVo userInfoVo);
	/**
	 * 查询KM用户单表信息
	 * @author ex-lix
	 * @param userInfoVo
	 * @return
	 */
	List<UserInfoVo> searchUserInfo(UserInfoVo userInfoVo);
	/**
	 * 获取部门信息
	 * @param userInfoVo
	 * @return
	 */
	List<UserInfoVo> searchDepartmentList(UserInfoVo userInfoVo);
	
	/**
	 * 查询全部部门
	 * @return
	 */

	List<UserInfoVo> queryAllDepartmentList();

	/**
	 * 查询某岗位下的用户列表
	 * @param orgId
	 * @param stationId
	 * @return
	 */
	List<UserInfoVo> queryEmpListByStationId(@Param("orgId")String orgId, @Param("stationId")String stationId);
	
	/**
	 * 查询用户信息 
	 * @param userInfoVo
	 * @return
	 */
	List<UserInfoVo> searchUserInfoListByParam(@Param("empName")String empName,@Param("limit")Integer limit);
	/**
	 * @TODO	查询用户关键信息 及 部门
	 * @author ex-liuy
	 * @param userId
	 * @return
	 */
	public List<UserInfoVo> searchUserInfoById(String userId);
	
	/**
	 * 登录失败记录失败次数
	 * @param loginErrorVo
	 */
	public void saveOrUpdLoginInfo(LoginErrorVo loginErrorVo);
	
	/**
	 * 查询用户登录失败信息
	 * @param empName
	 * @return
	 */
	public LoginErrorVo queryLoginInfo(@Param("loginId")String loginId);
	
	/**
	 * 删除用户登录失败信息
	 * @param loginId
	 */
	public void deleteLoginInfo(@Param("loginId")String loginId);
	
	/**
	 * 根据登录名查询用户 信息
	 * @param userInfo
	 * @return
	 */
	public List<UserInfoVo> queryUserInfoByLoginName(@Param("user")UserInfoVo userInfo);
}
