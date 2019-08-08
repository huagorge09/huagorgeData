package com.cmwa.ecc.business.utils.cached;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.cmwa.ecc.business.commonVo.UserInfoVo;
import com.cmwa.ecc.business.exception.CachedException;
import com.cmwa.ecc.business.service.cached.ShareCachedService;
import com.cmwa.ecc.business.service.userInfo.UserInfoService;

@Component
public class EmployeeCached implements ShareCachedService, InitializingBean {
	@Resource
	UserInfoService userInfoService;

	private static Map<String, String> empIdAndNameCached = new HashMap<String, String>(1024);

	@Override
	public void afterPropertiesSet() throws Exception {
		loadMappingCached();
	}

	/**
	 * {@link} IBF_GET_EMPNAME_BY_ID()
	 * 根据员工ID取得员工名字
	 * @param empId
	 * @return
	 */
	public static String getName(String empId) {
		String name=empIdAndNameCached.get(empId);
		return name==null?"":name;
	}

	@Override
	public void loadMappingCached() throws CachedException {
		empIdAndNameCached.clear();
		//List<UserInfoVo> userInfoVos = userInfoService.searchUserInfoList(new UserInfoVo());
		//查询用户表信息write by: ex-lix
		List<UserInfoVo> userInfoVos = userInfoService.searchUserInfo(new UserInfoVo());
		for (UserInfoVo userInfoVo : userInfoVos) {
			empIdAndNameCached.put(userInfoVo.getEmpID(), userInfoVo.getEmpName());
		}
	}

	/**
	 * 根据多个员工id取得员工姓名，多个员工id用,隔开
	 * @author ex-weicb
	 * @createDate 2016年4月29日 下午3:08:02
	 * @param empIds
	 * @return
	 */
	public static String getAllName(String empIds) {
		if (StringUtils.isEmpty(empIds)) {
			return "";
		}
		String[] empIdList=empIds.split(",");
		StringBuffer allNames=new StringBuffer();
		for (int i = 0; i < empIdList.length; i++) {
			String prefix=",";
			if (i==0) 
				prefix="";
			
			String name=empIdAndNameCached.get(empIdList[i]);
			allNames.append(prefix+(name==null? "":name));
		}
		return allNames.toString();
	}
}
