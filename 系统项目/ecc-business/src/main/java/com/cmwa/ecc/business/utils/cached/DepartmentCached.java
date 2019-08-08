package com.cmwa.ecc.business.utils.cached;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.cmwa.ecc.business.commonVo.UserInfoVo;
import com.cmwa.ecc.business.service.cached.ShareCachedService;
import com.cmwa.ecc.business.service.userInfo.UserInfoService;

@Component
public class DepartmentCached implements ShareCachedService, InitializingBean {
	@Resource
	private UserInfoService userInfoService;
	private static Map<String, String> depIdAndNameCached = new HashMap<String, String>(1024);

	@Override
	public void afterPropertiesSet() throws Exception {
		loadMappingCached();
	}

	@Override
	public void loadMappingCached() {
		depIdAndNameCached.clear();
		List<UserInfoVo> userInfoVos = userInfoService.searchDepartmentList(new UserInfoVo());
		for (UserInfoVo userInfoVo : userInfoVos) {
			depIdAndNameCached.put(userInfoVo.getReorgId(), userInfoVo.getReOrgName());
			depIdAndNameCached.put(userInfoVo.getOrgId(), userInfoVo.getOrgName());
			depIdAndNameCached.put(userInfoVo.getSecondOrgid(), userInfoVo.getSecondOrgname());
		}
	}

	/**
	 * {@link} IBF_GET_DEPTNAME_BY_ID()
	 * 根据部门或组织的ID取得部门名称或组织名称
	 * @param depId
	 * @return
	 */
	public static String getName(String depId) {
		return depIdAndNameCached.get(depId);
	}
	/**
	 * 根据多个部门id取得部门姓名，多个DPEPT Id用,隔开
	 * @param  deptIds
	 * @return
	 */
	public static String getAllName(String deptIds) {
		if (StringUtils.isEmpty(deptIds)) {
			return "";
		}
		String[] deptIdList=deptIds.split(",");
		StringBuffer allNames=new StringBuffer();
		for (int i = 0; i < deptIdList.length; i++) {
			String prefix=",";
			if (i==0) 
				prefix="";
			
			String name=depIdAndNameCached.get(deptIdList[i]);
			allNames.append(prefix+(name==null? "":name));
		}
		return allNames.toString();
	}
}
