package com.cmwa.ecc.business.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmwa.ecc.business.commonVo.UserInfoVo;
import com.cmwa.ecc.business.service.userInfo.UserInfoService;

public class UserInfoUtil {

	private static Logger logger = LoggerFactory.getLogger(UserInfoUtil.class);
	
	/**
	 * 用户信息缓存集合
	 */
	private static List<UserInfoVo> userInfoList = new Vector<UserInfoVo>();

	@Resource
	private UserInfoService userInfoService;

	// 同步锁
	private static byte[] lock = new byte[0];

	private static UserInfoUtil instance;

	private UserInfoUtil() {
	}

	public static UserInfoUtil getInstance() {
		if (instance == null) {
			instance = new UserInfoUtil();
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
		if (CollectionUtils.isEmpty(userInfoList)) {
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
				UserInfoVo userInfoVo = new UserInfoVo();
				if (userInfoList != null) {
					userInfoList.clear();
				}
				userInfoList = userInfoService.searchUserInfoList(userInfoVo);
			} catch (Exception e) {
				logger.error("用户信息加载异常：", e);
				e.getStackTrace();
			}
		}
	}

	/**
	 * 获取所有的用户信息
	 * 
	 * @return List
	 */
	public List getUserInfos() {
		return userInfoList;
	}

	/**
	 * @param empName
	 * @param queryDimission
	 * @return
	 */
	public static List<UserInfoVo> getUserInfosByName(String empName, boolean queryDimission) {
		List<UserInfoVo> vList = new Vector<UserInfoVo>();
		if ((userInfoList == null || userInfoList.size() == 0)) {
			return vList;
		} else {
			for (int i = 0; i < userInfoList.size(); i++) {
				UserInfoVo vo = (UserInfoVo) userInfoList.get(i);
				String name = buildQueryName(vo);
				if (name.indexOf(empName) > -1) {
					// 不查询离职人员，过滤vo
					if (!queryDimission) {
						if (filterDismission(vo)) {
							vList.add(vo);
						}
					} else {
						vList.add(vo);
					}
				}
			}

			return vList;
		}
	}

	/**
	 * 查询没有重名的人员信息
	 * 
	 * @param empName
	 * @param queryDimission
	 * @return
	 */
	public static List<UserInfoVo> getDuplicateUserInfosByName(String empName, boolean queryDimission) {
		Map<String, UserInfoVo> vMap = new HashMap<String, UserInfoVo>();
		if ((userInfoList == null || userInfoList.size() == 0)) {
			return asList(vMap);
		} else {
			for (int i = 0; i < userInfoList.size(); i++) {
				UserInfoVo vo = (UserInfoVo) userInfoList.get(i);
				String name = buildQueryName(vo);
				if (name.indexOf(empName) > -1) {
					// 不查询离职人员，过滤vo
					if (!queryDimission) {
						if (filterDismission(vo)) {
							vMap.put(vo.getEmpID(), vo);
						}
					} else {
						vMap.put(vo.getEmpID(), vo);
					}
				}
			}
			return asList(vMap);
		}
	}

	/**
	 * 过滤离职人员，只返回empStat为STAR
	 * 
	 * @param vo
	 * @return
	 */
	private static boolean filterDismission(UserInfoVo vo) {
		if (vo != null && StringUtils.equals(vo.getEmpStat(), "STAR") && StringUtils.equals(vo.getOrmemberStat(), "STAR")) {
			return true;
		}
		return false;
	}

	/**
	 * 构建查询的名称
	 * 
	 * @param vo
	 * @return
	 */
	public static String buildQueryName(UserInfoVo vo) {
		String name = "";
		if (vo.getEmpName() != null) {
			name += vo.getEmpName();
		}
		if (vo.getEmpSName() != null) {
			name += vo.getEmpSName();
		}
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
		return name;
	}

	/**
	 * map转为list
	 * 
	 * @param userMap
	 * @return
	 */
	private static List<UserInfoVo> asList(Map<String, UserInfoVo> userMap) {
		List<UserInfoVo> list = new ArrayList<UserInfoVo>();
		for (UserInfoVo vo : userMap.values()) {
			list.add(vo);
		}
		return list;
	}

	public UserInfoService getUserInfoService() {
		return userInfoService;
	}

	public void setUserInfoService(UserInfoService userInfoService) {
		this.userInfoService = userInfoService;
	}

}
