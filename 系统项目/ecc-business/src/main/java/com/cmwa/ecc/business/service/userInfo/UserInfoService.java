package com.cmwa.ecc.business.service.userInfo;

import java.util.List;

import com.cmwa.ecc.business.commonVo.Employee;
import com.cmwa.ecc.business.commonVo.LoginErrorVo;
import com.cmwa.ecc.business.commonVo.UserInfoVo;


public interface UserInfoService {
	/**
	 * 查询用户信息
	 * @param userInfoVo
	 * @return
	 */
	List<UserInfoVo> searchUserInfoList(UserInfoVo userInfoVo);
	/**
	 * @author ex-lix
	 * 查询单张KM用户表信息
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
	 * 根据用户ID查询组织ID
	 * @param empId
	 * @return
	 */
	String queryOrgIdByEmpId(String empId);

	/**
	 * 查询全部 部门
	 * @param userInfoVo
	 * @return
	 */
	List<UserInfoVo> queryAllDepartmentList();
	
	/**
	 * 查询某岗位下的用户列表
	 * @param orgIdx
	 * @param stationId
	 * @return
	 */
	List<UserInfoVo> queryEmpListByStationId(String orgId, String stationId);
	
	/**
	 * 查询用户信息 
	 * @param userInfoVo
	 * @return
	 */
	List<UserInfoVo> searchUserInfoListByParam(String empName,Integer limit);
	
	/**
	 * @TODO	查询用户关键信息 及 部门
	 * @author ex-liuy
	 * @param userId
	 * @return
	 */
	public List<UserInfoVo> searchUserInfoById(String userId);
	
	/**
	 * token用户登陆
	 * @param tokenId
	 * @param session
	 * @param user
	 * @return
	 */
	public Employee tokenUserLogin(String tokenId, String sessionId);
	
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
	public LoginErrorVo queryLoginInfo(String loginId);
	
	/**
	 * 删除用户登录失败信息
	 * @param loginId
	 */
	public void deleteLoginInfo(String loginId);
	
	/**
	 * 根据登录名查询用户 信息
	 * @param userInfo
	 * @return
	 */
	public List<UserInfoVo> queryUserInfoByLoginName(UserInfoVo userInfo);
}
