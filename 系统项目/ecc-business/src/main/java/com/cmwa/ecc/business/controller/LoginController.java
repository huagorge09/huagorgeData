package com.cmwa.ecc.business.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmwa.ecc.business.commonVo.Employee;
import com.cmwa.ecc.business.commonVo.LoginErrorVo;
import com.cmwa.ecc.business.commonVo.UserInfoVo;
import com.cmwa.ecc.business.service.userInfo.UserInfoService;
import com.cmwa.ecc.business.utils.Client;
import com.cmwa.ecc.business.utils.ClientManager;
import com.cmwa.ecc.business.utils.Constant;
import com.cmwa.ecc.business.utils.DateDistance;
import com.cmwa.ecc.business.utils.SessionUtils;
import com.cmwa.ecc.business.utils.SysConf;
import com.cmwa.ecc.business.utils.SysConstant;
import com.cmwa.ecc.business.utils.login.ADcheck;


@Controller("loginController")
@RequestMapping(value="/service/login")
public class LoginController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private UserInfoService userInfoService;
	
	/**
	 * 跳转至系统首页页面
	 * @return
	 */
	@RequestMapping(value = "/loginView.action")
	public String goLoginPage(ModelMap model) {
		return "login";
	};
	
	/**
	 * 用户登录功能
	 * @param loginName
	 * @param loginPwd
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/loginManager.action")
	@ResponseBody
	public String loginManager(@RequestParam("loginName") String loginName,
							   @RequestParam("loginPwd") String loginPwd,
							   @RequestParam("verifyCode") String verifyCode,
							   HttpServletRequest request){
		HttpSession session = request.getSession();
		JSONObject jsonObject = new JSONObject();
		session.removeAttribute(SysConstant.BUSIN_MENU_CACHE);
		/**
		 * 验证码是否正确
		 */
		String session_verifycode = (String) session.getAttribute(SessionUtils.SESSION_VERIFYCODE);
		if(null == session_verifycode || session_verifycode.equals("")){
			jsonObject.put("resultCode", "9300");
			jsonObject.put("resultMsg", "请重新获取验证码！");
			return jsonObject.toString();
		}
		
		if(!verifyCode.toLowerCase().equals(session_verifycode.toLowerCase())){
			jsonObject.put("resultCode", "9200");
			jsonObject.put("resultMsg", "验证码有误！");
			return jsonObject.toString();
		}
		String envVar = SysConf.get("ENVVARIABLE");
		
		if(Constant.C_EVNVAR_TEST.equals(envVar)){
			UserInfoVo userInfo = new UserInfoVo();
			userInfo.setEmpSName(loginName);
			List<UserInfoVo> loginUserList = userInfoService.queryUserInfoByLoginName(userInfo);
			if(loginUserList.size() != 1){
				jsonObject.put("resultCode", "9998");
				jsonObject.put("resultMsg", "登录用户不存在");
				return jsonObject.toString();
			}
			userInfo = loginUserList.get(0);
			Employee employee = new Employee();
			userInfoService.deleteLoginInfo(loginName);
			employee.setLoginName(userInfo.getEmpSName());
			employee.setID(userInfo.getEmpID());
			employee.setName(userInfo.getEmpName());
			employee.setTopOrganid(userInfo.getOrgId());
//			session.setAttribute(SessionUtils.SESSION_EMPLOYEE, employee);
			SessionUtils.setEmployee(request,employee);
			// 客户端菜单管理的缓存类
			Client client = new Client();
			client.setEmployee(employee);
			ClientManager.getInstance().addClinet(session.getId(), client);
			jsonObject.put("resultCode", "0000");
			jsonObject.put("resultMsg", "登录成功！");
			return jsonObject.toString();
		}
		
		
		//判断当前登录用户登录失败次数（如果超过五次则禁止15分钟内登录）
		String loginDate = "";
		LoginErrorVo errorVo = userInfoService.queryLoginInfo(loginName);
		if(null != errorVo){
			if(errorVo.getLoginErrorCount() >= 5){
				loginDate = errorVo.getLoginDate();
				long[] longs = DateDistance.getDistanceTimes(loginDate);
				if(longs[2] <= 15){
					jsonObject.put("resultCode", "9300");
					jsonObject.put("resultMsg", "请15分钟后再试！");
					return jsonObject.toString();
				}else{
					userInfoService.deleteLoginInfo(loginName);
				}
			}
		}
		
		UserInfoVo userInfoVo = new UserInfoVo();
		userInfoVo.setEmpSName(loginName);
		Employee employee = new Employee();
		List<UserInfoVo> infoVos = userInfoService.searchUserInfoList(userInfoVo);
		if (null != infoVos && infoVos.size() > 0) {
			userInfoVo = new UserInfoVo();
			userInfoVo = infoVos.get(0);
			try {
				boolean flag = false;
				ADcheck aDcheck = new ADcheck();
				flag = aDcheck.winADLoginAction(loginName,loginPwd);
				if(flag){
					userInfoService.deleteLoginInfo(loginName);
					employee.setLoginName(userInfoVo.getEmpSName());
					employee.setID(userInfoVo.getEmpID());
					employee.setName(userInfoVo.getEmpName());
					employee.setTopOrganid(userInfoVo.getOrgId());
					session.setAttribute(SessionUtils.SESSION_EMPLOYEE, employee);
					// 客户端菜单管理的缓存类
					Client client = new Client();
					client.setEmployee(employee);
					ClientManager.getInstance().addClinet(session.getId(), client);
					jsonObject.put("resultCode", "0000");
					jsonObject.put("resultMsg", "登录成功！");
				}else{
					jsonObject.put("resultCode", "9100");
					jsonObject.put("resultMsg", "用户名或密码错误！");
				}
			} catch (Exception e) {
				jsonObject.put("resultCode", "9999");
				jsonObject.put("resultMsg", "登录异常："+e.getMessage());
			}
		}else{
			jsonObject.put("resultCode", "9100");
			jsonObject.put("resultMsg", "用户名或密码错误！");
			LoginErrorVo loginErrorVo = new LoginErrorVo();
			loginErrorVo.setLoginId(loginName);
			userInfoService.saveOrUpdLoginInfo(loginErrorVo);
		}
		session.removeAttribute(SessionUtils.SESSION_VERIFYCODE);
		return jsonObject.toString();
	}
	
	/**
	 * 登出
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/loginOutManager.do")
	public String loginOutManager(HttpServletRequest request){
		HttpSession session = request.getSession();
		session.invalidate();
		SessionUtils.setEmployee(null);
		return redirectLogin();
	}
}
