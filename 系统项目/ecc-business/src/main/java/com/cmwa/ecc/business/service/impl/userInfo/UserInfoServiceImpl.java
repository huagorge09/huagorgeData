package com.cmwa.ecc.business.service.impl.userInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.cmwa.ec.base.util.SpringUtil;
import com.cmwa.ecc.business.commonVo.Employee;
import com.cmwa.ecc.business.commonVo.LoginErrorVo;
import com.cmwa.ecc.business.commonVo.UserInfoVo;
import com.cmwa.ecc.business.dao.userInfo.UserInfoDao;
import com.cmwa.ecc.business.service.userInfo.UserInfoService;
import com.cmwa.ecc.business.utils.HttpRequestUtils;
import com.cmwa.ecc.business.utils.SessionUtils;


@Service
public class UserInfoServiceImpl implements UserInfoService {
	
	private static Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);
	
	@Resource
	private UserInfoDao userInfoDao;

	@Override
	public List<UserInfoVo> searchUserInfoList(UserInfoVo userInfoVo) {
		
		return userInfoDao.searchUserInfoList(userInfoVo);
	}

	@Override
	public List<UserInfoVo> searchDepartmentList(UserInfoVo userInfoVo) {
		return userInfoDao.searchDepartmentList(userInfoVo);
	}

	@Override
	public String queryOrgIdByEmpId(String empId) {
		UserInfoVo userInfoVo=new UserInfoVo();
		userInfoVo.setEmpID(empId);
		List<UserInfoVo> userInfoVos=	userInfoDao.searchUserInfoList(userInfoVo);
		if (userInfoVos!=null && userInfoVos.size()!=0) {
			return userInfoVos.get(0).getSecondOrgid();
		}
		return null;
	}

	@Override
	public List<UserInfoVo> queryAllDepartmentList() {
		return userInfoDao.queryAllDepartmentList();
	}

	@Override
	public List<UserInfoVo> queryEmpListByStationId(String orgId, String stationId) {
		return userInfoDao.queryEmpListByStationId(orgId,stationId);
	}

	@Override
	public List<UserInfoVo> searchUserInfo(UserInfoVo userInfoVo) {
		return userInfoDao.searchUserInfo(userInfoVo);
	}

	@Override
	public List<UserInfoVo> searchUserInfoListByParam(String empName, Integer limit) {
		return userInfoDao.searchUserInfoListByParam(empName, limit);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Employee tokenUserLogin(String tokenId, String sessionId) {
		logger.info(String.format("token用户登陆开始: token=%s sessionId=%s", tokenId, sessionId));
		JSONObject param = new JSONObject();
		param.put("data", tokenId);
		param.put("appId", "A101001");
		param.put("seq", sessionId);
		param.put("cmd", "");
		param.put("curTimeStamp", "");
		param.put("version", "");
		param.put("sign", "");
		try {
			System.out.println("请求地址："+SpringUtil.getProperty("token.login.interface"));
			JSONObject dataStr = HttpRequestUtils.httpPost(SpringUtil.getProperty("token.login.interface"), param);
			Map<String, String> data = new HashMap<String, String>();
			data.putAll(dataStr);
			if(SessionUtils.TOKEN_CODE_SUCCESS.equals(data.get("code"))){
				String userName = data.get("data");
				JSONObject jsonObject = JSONObject.fromObject(userName);
				Employee employee = new Employee();
				employee.setID(jsonObject.getString("loginId"));
				employee.setName(jsonObject.getString("loginName"));
				logger.info(String.format("token用户登陆结果成功: token=%s sessionId=%s userName=%s", tokenId, sessionId, userName));
				return employee;
			}else{
				logger.error(String.format("token用户登陆失败: token=%s sessionId=%s errorMsg=%s", tokenId, sessionId, dataStr.toString()));
			}
		} catch (Exception e) {
			logger.error(String.format("token用户登陆失败: token=%s sessionId=%s errorMsg=%s", tokenId, sessionId, e.getMessage()));
		}
		return null;
	}

	@Override
	public List<UserInfoVo> searchUserInfoById(String userId) {
		return userInfoDao.searchUserInfoById(userId);
	}

	@Override
	public void saveOrUpdLoginInfo(LoginErrorVo loginErrorVo) {
		try {
			userInfoDao.saveOrUpdLoginInfo(loginErrorVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public LoginErrorVo queryLoginInfo(String loginId) {
		LoginErrorVo loginErrorVo = null;
		try {
			loginErrorVo = userInfoDao.queryLoginInfo(loginId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginErrorVo;
	}

	@Override
	public void deleteLoginInfo(String loginId) {
		try {
			userInfoDao.deleteLoginInfo(loginId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<UserInfoVo> queryUserInfoByLoginName(UserInfoVo userInfo) {
		List<UserInfoVo> userInfoList = new ArrayList<UserInfoVo>();
		try{
			userInfoList = userInfoDao.queryUserInfoByLoginName(userInfo);
		}catch(Exception e){
			logger.error("----UserInfoServiceImpl-queryUserInfoByLoginName-Exception:",e);
		}
		
		return userInfoList;
	}

	
}
