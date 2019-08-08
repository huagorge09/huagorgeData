package com.cmwa.ecc.business.utils;

import java.util.List;
import java.util.Vector;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmwa.ecc.business.commonVo.UserInfoVo;
import com.cmwa.ecc.business.service.userInfo.UserInfoService;

public class DepartmentUtil {

	private static Logger log = LoggerFactory.getLogger(DictionaryUtil.class.getName());

	/**
	 * 部门信息缓存集合
	 */
	private static List<UserInfoVo> departmentList = new Vector<UserInfoVo>();

	@Resource
	private UserInfoService userInfoService;

	// 同步锁
	private static byte[] lock = new byte[0];

	private static DepartmentUtil instance;

	private DepartmentUtil() {
	}

	public static DepartmentUtil getInstance() {
		if (instance == null) {
			instance = new DepartmentUtil();
		}

		return instance;
	}

	/**
	 * 刷新缓存
	 */
	public void reload() {
		init();
	}

	/**
	 * 校验缓存的有效性
	 */
	public boolean validateCach() {
		if (CollectionUtils.isEmpty(departmentList)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 缓存初始化
	 */
	private void init() {

		// 加同步锁
		synchronized (lock) {
			try {
				if (departmentList != null) {
					departmentList.clear();
				}
				departmentList = userInfoService.queryAllDepartmentList();
			} catch (Exception e) {
				log.error("部门信息加载异常：", e);
				e.getStackTrace();
			}
		}
	}

	/**
	 * 获取所有的部门信息
	 * 
	 * @return List
	 */
	public List getdepartments() {
		return departmentList;
	}

	/**
	 * 根据部门名称获取部门信息
	 * 
	 * @param orgName
	 * @return
	 */
	public static List<UserInfoVo> getDepartmentsByName(String orgName) {
		List<UserInfoVo> vList = new Vector<UserInfoVo>();
		if ((departmentList == null || departmentList.size() == 0)) {
			return vList;
		} else {
			for (int i = 0; i < departmentList.size(); i++) {
				UserInfoVo vo = (UserInfoVo) departmentList.get(i);
				if (vo == null) {
					continue;
				}
				String name = "";

				if (vo.getReOrgName() != null) {
					name += vo.getReOrgName();
				}
				if (vo.getOrgName() != null) {
					name += vo.getOrgName();
				}
				if (vo.getSecondOrgname() != null) {
					name += vo.getSecondOrgname();
				}
				if (vo.getTitleName() != null) {
					name += vo.getTitleName();
				}
				if (name.indexOf(orgName) > -1) {
					vList.add(vo);
				}
			}
			return vList;
		}
	}

	public UserInfoService getUserInfoService() {
		return userInfoService;
	}

	public void setUserInfoService(UserInfoService userInfoService) {
		this.userInfoService = userInfoService;
	}

}
