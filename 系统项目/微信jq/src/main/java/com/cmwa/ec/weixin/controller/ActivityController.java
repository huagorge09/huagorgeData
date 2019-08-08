package com.cmwa.ec.weixin.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.base.util.SpringUtil;
import com.cmwa.ec.trade.facade.dto.user.UserAcctDto;
import com.cmwa.ec.user.facade.dto.UserAccoRlaDto;
import com.cmwa.ec.user.facade.dto.UserBaseInfoDto;
import com.cmwa.ec.user.facade.dto.UserServiceMessage;
import com.cmwa.ec.weixin.client.QueryServiceClient;
import com.cmwa.ec.weixin.client.UserServiceClient;
import com.cmwa.ec.weixin.constants.WXConstants;
import com.cmwa.ec.weixin.dao.UserInfoDao;
import com.cmwa.ec.weixin.dao.UserInfoexDao;
import com.cmwa.ec.weixin.dao.WaecuserUserBaseInfoDao;
import com.cmwa.ec.weixin.dto.DrainageUserInfoDto;
import com.cmwa.ec.weixin.dto.InvectorUserInfoexDto;
import com.cmwa.ec.weixin.dto.InvestorUserDto;
import com.cmwa.ec.weixin.dto.ParameterDto;
import com.cmwa.ec.weixin.dto.SinoUserInfoDto;
import com.cmwa.ec.weixin.dto.UserInfoDto;
import com.cmwa.ec.weixin.dto.UserInfoexDto;
import com.cmwa.ec.weixin.interceptor.TracingInfo;
import com.cmwa.ec.weixin.manager.activity.DrainageUserInfoManager;
import com.cmwa.ec.weixin.manager.business.ActivityManager;
import com.cmwa.ec.weixin.manager.business.MessageManager;
import com.cmwa.ec.weixin.manager.business.QueryManager;
import com.cmwa.ec.weixin.manager.business.TradeManager;
import com.cmwa.ec.weixin.manager.business.UserInfoexManager;
import com.cmwa.ec.weixin.model.MobileMessagePushModel;
import com.cmwa.ec.weixin.thread.SendMsgAndRedirectThread;
import com.cmwa.ec.weixin.util.ContextUtils;
import com.cmwa.ec.weixin.util.DESUtils;
import com.cmwa.ec.weixin.util.DateUtils;
import com.cmwa.ec.weixin.util.HttpPostUtil;
import com.cmwa.ec.weixin.util.MD5;
import com.cmwa.ec.weixin.util.RequestHelper;
import com.cmwa.ec.weixin.util.SessionValue;
import com.cmwa.ec.weixin.util.WeixinUtil;

@Controller("activityController")
@RequestMapping(value = "/WeixinService")
public class ActivityController {

	@Autowired
	private ActivityManager activityManager;
	
	@Autowired
	private UserInfoDao userInfoDao;
	
	@Autowired
	private UserInfoexDao userInfoExDao;
	
	@Autowired
	private DrainageUserInfoManager drainageUserInfoManager;
	
	@Autowired
	private ThreadPoolExecutor executor;
	
	@Autowired
	private MessageManager messageManager;
	
	@Autowired
	private UserInfoexManager userInfoexManager;
	
	@Autowired
	private UserServiceClient userServiceClient;
	
	@Autowired
	private TradeManager tradeManager;
	
	@Autowired
	private WaecuserUserBaseInfoDao waecuserInfoDao;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private QueryManager queryManager;
	
	@Autowired
	private QueryServiceClient queryServiceClient;
	
	private final static String SECRECT;
	
	private final static Logger logger = LoggerFactory.getLogger(ActivityController.class);

	static {
		SECRECT = SpringUtil.getProperty("cmwa.wx.sion.send.secret");
	}
	
	/**
	 * 根据加密的userId获取用户信息
	 */
	@RequestMapping(value = "/investor/queryUserInfoByUerId.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST,RequestMethod.GET })
	@ResponseBody
	public String queryUserInfoByUerId(HttpServletResponse response,HttpServletRequest request) {
		String userId = null;
		Map<String, Object> map=new HashMap<String, Object>();
		JSONObject jsonObject = new JSONObject();
		try {
			userId = URLDecoder.decode(request.getParameter("userid"), "UTF-8");
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("encrypted", userId));
			String entity  = HttpPostUtil.sendPost("decrypt", param, "");
			// 判断返回的状态值
			if(!StringUtils.isBlank(entity)){
				// 如果请求成功，获取报文的实体
				// 创建连接参数对象
				JSONObject result = JSONObject.fromObject(entity);
				String errorMsg = String.valueOf(result.get("errMsg"));
				// response返回错误信息，获取失败
				if(!StringUtils.isBlank(errorMsg)){
					jsonObject.put("returnCode", "1");
					jsonObject.put("returnMsg", "userId解密失败");
					logger.warn("userId解密失败-------"+errorMsg+"----------");
					return jsonObject.toString();
				}
				// response未返回错误信息，获取成功
				String encryptedUserId = String.valueOf(result.get("resp"));
				logger.info("获取到解密后的UserId: {}",encryptedUserId);
				// 根据userid查询对应的用户信息
				InvectorUserInfoexDto dto=activityManager.queryUserInfoByUerId(encryptedUserId);
				if (dto != null) {
					logger.info("根据新表cmwa_wx_user_info表中userid查询得到的用户信息为:{}",JSONObject.fromObject(dto));
					request.getSession().setAttribute(SessionValue.SESSION_OPENID, dto.getOpenId());
					map.put("openId", dto.getOpenId());
					map.put("imgUrl", dto.getHeadImgurl());
					map.put("subscribeChannel", dto.getSubscribeChannel());
					map.put("subscribeTime", DateUtils.formatDate(dto.getSubscribeTime()));
					if (dto.getSubscribe() == null || "".equals(dto.getSubscribe())) {//新表为空，则去旧表里面查询关注状态
						//判断旧表数据：cmf_weixin_user_info中status=1,并且cmf_weixin_user_infoex表BINDSTAT字段不为C
						UserInfoDto userInfoDto = userInfoDao.queryByOpenId(dto.getOpenId());
						UserInfoexDto userInfoexDto = userInfoExDao.queryByOpenId(dto.getOpenId(), null);
						boolean subscrible = false;
						if (userInfoDto != null) {//非空
							if ("1".equals(userInfoDto.getStatus())) {
								if (userInfoexDto == null) {
									subscrible = true;
								} else {
									if (!"C".equals(userInfoexDto.getBindstat())) subscrible = true;
								}
							}
						}
						map.put("subscribe", subscrible ? "1": "0");
						logger.info("根据openid查询旧表cmf_weixin_user_info得到旧表信息为：{}，"
								+ "cmf_weixin_user_infoex为：{}",JSONObject.fromObject(userInfoDto) , JSONObject.fromObject(userInfoexDto));
					} else {//有值，则直接去取新表字段
						map.put("subscribe", dto.getSubscribe());
					}
					
					map.put("nickName", dto.getNickName());
					map.put("userId", encryptedUserId);
					jsonObject.put("returnCode", "0");
					jsonObject.put("returnMsg", "查询成功");
					jsonObject.put("data", map);
				}
				logger.info("根据userId获取到该用户的OpenId为: {}",dto != null ? dto.getOpenId() : "");
			} else {
				jsonObject.put("returnCode", "1");
				jsonObject.put("returnMsg", "查询失败");
				logger.warn("调用远程api接口返回为空");
			}
		} catch (Exception e1) {
			logger.error("调用获取openid异常",e1);
		}
		return jsonObject.toString();
	}
	
	/**
	 * 调用微信转发接口
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/investor/wxForward.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST,RequestMethod.GET })
	public String wxForward(HttpServletRequest request,HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		String url = request.getParameter("url");
		logger.info("ActivityController[wxForward]微信转发开始>>>>>url:{}", url);
		if(null == url || "".equals(url)) {
			jsonObject.put("returnCode", "1");
			jsonObject.put("returnMsg", "url为空");
			return jsonObject.toString();
		}
		String jsapi_ticket = WeixinUtil.getJsapiTicket();
		logger.info("得到的jsapi_ticket：{}",jsapi_ticket);
		Map<String, String> map = WeixinUtil.sign(jsapi_ticket, url);
		jsonObject.put("returnCode", "0");
		jsonObject.put("returnMsg", "请求成功");
		jsonObject.put("data", map);
		logger.info("ActivityController[wxForward]微信转发结束>>>>>jsonObject:{}", jsonObject.toString());
		return jsonObject.toString();
	}
	
	/**
	 * 用户转发回调接口
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/investor/userForward.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST,RequestMethod.GET })
	public String userForward(HttpServletRequest request,HttpServletResponse response) {
		String openId = (String) request.getSession().getAttribute("openId");
		String type = request.getParameter("type");
		JSONObject jsonObject = activityManager.userForward(openId,type);
		return jsonObject.toString();
	}
	
	/**
	 * 用户答题接口
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/investor/userAnswer.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST,RequestMethod.GET })
	public String userAnswer(HttpServletRequest request,HttpServletResponse response) {
		String openId = (String) request.getSession().getAttribute("openId");
		String score = request.getParameter("score");
		JSONObject jsonObject = activityManager.userAnswer(openId, score);
		return jsonObject.toString();
	}
	
	/**
	 * 用户抽奖接口
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/investor/userLuckDraw.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST,RequestMethod.GET })
	public String userLuckDraw(HttpServletRequest request,HttpServletResponse response) {
		String openId = (String) request.getSession().getAttribute("openId");
		JSONObject jsonObject = activityManager.userLuckDraw(openId);
		return jsonObject.toString();
	}
	
	/**
	 * 用户兑奖接口
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/investor/userGetAward.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST,RequestMethod.GET })
	public String userGetAward(HttpServletRequest request,HttpServletResponse response) {
		InvestorUserDto investorUserDto = new InvestorUserDto();
		investorUserDto.setOpenId((String) request.getSession().getAttribute("openId"));
		investorUserDto.setUserFullName(request.getParameter("userName"));
		investorUserDto.setMobile(request.getParameter("userTel"));
		investorUserDto.setAddress(request.getParameter("address"));
		JSONObject jsonObject = activityManager.userGetAward(investorUserDto);
		return jsonObject.toString();
	}
	
	/**
	 * 查询用户奖品接口
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/investor/queryUserAward.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST,RequestMethod.GET })
	public String queryUserAward(HttpServletRequest request,HttpServletResponse response) {
		String openId = (String) request.getSession().getAttribute("openId");
		JSONObject jsonObject = activityManager.queryUserAward(openId);
		return jsonObject.toString();
	}
	
	/**
	 * 验证用户是否可以抽奖接口
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/investor/checkUserIsCanLuckDraw.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST,RequestMethod.GET })
	public String checkUserIsCanLuckDraw(HttpServletRequest request,HttpServletResponse response) {
		String openId = (String) request.getSession().getAttribute("openId");
		JSONObject jsonObject = activityManager.checkUserIsCanLuckDraw(openId);
		return jsonObject.toString();
	}
	
	/**
	 * 查询投教活动的开关配置接口
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/investor/queryInvestorEduActParameter.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST,RequestMethod.GET })
	public String queryInvestorEduActParameter(HttpServletRequest request,HttpServletResponse response) {
		String pmst = request.getParameter("pmst");
		String pmky = request.getParameter("pmky");
		String pmnm = request.getParameter("pmnm");
		return activityManager.queryInvestorEduActParameter(pmst,pmky,pmnm);
	}
	
	
	/**
	 * @param data
	 * @param request
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	@RequestMapping(value="/setUp/registerWithDrainage.xhtml",produces = "text/html;charset=UTF-8",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String registerWithDrainage(SinoUserInfoDto data,HttpServletRequest request) throws NoSuchAlgorithmException {
		JSONObject result = new JSONObject();
		if(StringUtils.isBlank(data.getAppId())) {
			logger.info("当前用户来源信息丢失,拒绝注册");
			result.put("returnCode", "9999");
			result.put("returnMsg","该用户不具有参与活动的资格");
			return result.toString();
		}
		if(!WXConstants.DRAINAGE_REGISTERCHANNEL_HABX.equals(data.getAppId())) {
			result.put("returnCode", "9999");
			result.put("returnMsg","该用户不具有参与活动的资格");
			return result.toString();
		}
		logger.info("当前用户引流来源为："+data.getAppId());
		logger.info("开始验证签名");
		try {
			String sign = sign(data);
			if(sign == null || !sign.equals(data.getSign())) { 
				logger.info("验证签名失败，拒绝注册");
				result.put("returnCode", "9999");
				result.put("returnMsg","验证签名失败");
				return result.toString();
			}
			DrainageUserInfoDto existsDrainageUser = drainageUserInfoManager.queryUserInfoByIdCardAndOwnerName(data.getIdCard(),data.getOwnerName());
			if(existsDrainageUser != null) {
				String userMobile = existsDrainageUser.getUserMobile();
				UserBaseInfoDto existsUserBaseInfo = waecuserInfoDao.queryUserBaseInfoByMobile(userMobile);
				if(existsUserBaseInfo != null) {
					result.put("returnCode", "9999");
					result.put("returnMsg","当前用户已经注册,请刷新页面");
					return result.toString();
				}
			}
		} catch(UnsupportedEncodingException e2){
			logger.error("加密签名时捕获异常:",e2);
			result.put("returnCode", "9999");
			result.put("returnMsg","内部服务器异常");
			return result.toString();
		} catch(NoSuchAlgorithmException e1){
			logger.error("加密签名时捕获异常:",e1);
			result.put("returnCode", "9999");
			result.put("returnMsg","内部服务器异常");
			return result.toString();
		} catch (Exception e) {
			logger.error("查询引流用户信息是否存在捕获异常，",e);
			result.put("returnCode", "9999");
			result.put("returnMsg","内部服务器异常");
			return result.toString();
		}
		
		
		
		logger.info("验证签名成功,开始验证用户投保省份");
		List<ParameterDto> queryParameter = queryServiceClient.queryParameter("SYSTEM", "SINOPROVINCE", null, null);
		if(queryParameter == null || queryParameter.size() == 0) {
			logger.info("未配置有效投保省份，不做校验");
		} else {
			boolean isValidProvince = false;
			List<String> provinces = Arrays.asList(queryParameter.get(0).getPmco().split(","));
			String userProvince = data.getProvince();
			for (int i = 0; i < provinces.size(); i++) {
				String temp = provinces.get(i);
				if(userProvince.indexOf(temp) >= 0) {
					isValidProvince = true;
					break;
				}
			}
			if(!isValidProvince) {
				result.put("returnCode", "9995");
				result.put("returnMsg", "该用户不具有参与活动的资格");
				return result.toString();
			}
		}
		/* register start */
		// 将引流用户信息入库
		String mobile = request.getParameter("mobile");
		String passWord = request.getParameter("passWord");
		String sessionID = request.getParameter("sessionID");
		String smsCode = request.getParameter("smsCode");
		String userName = request.getParameter("userName");
		String seqId = request.getSession().getId();
		JSONObject tempResult = new JSONObject();
		
		logger.info("注册参数为： 手机号：{},用户名为：{}",mobile,userName);
		
		// 校验参数空值
		if(mobile == null || passWord == null || sessionID == null || seqId == null || smsCode == null) {
			tempResult.put("returnCode", "9999");
			tempResult.put("returnMsg", "关键参数丢失");
			return tempResult.toString();
		}
		if(!data.getOwnerName().equals(userName)){
			tempResult.put("returnCode", "9999");
			tempResult.put("returnMsg", "该用户不具有参与活动的资格");
			return tempResult.toString();
		}
		
		// 验证发送手机短信验证码是保存在session中的手机号码
		String smsMobile = (String) request.getSession(true).getAttribute(SessionValue.SESSION_USERMOBILE);
		if (smsMobile == null) {
			tempResult.put("returnCode", "9998");// 未获取到发送验证码时，session中保存的手机号码
			return tempResult.toString();
		} else if (!smsMobile.equals(mobile)) {
			tempResult.put("returnCode", "9997");// 用户验证手机验证码，提交的手机号码和获取短信验证码的手机号码不一致
			return tempResult.toString();
		}

		// 日志信息
		Context context = ContextUtils.setContext(WXConstants.USER_SERVICE_001, WXConstants.WEIXIN_CHANNEL, "", System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

		// 验证短信验证码
		String returnStr = messageManager.checkVrfCode(context, sessionID, mobile, smsCode);

		if (returnStr == null || !WXConstants.COMMON_SUCCESS.equals(returnStr)) {
			tempResult.put("returnCode", "9996");// 手机短信验证码验证失败
			return tempResult.toString();
		}

		MD5 md5 = new MD5();
		if (passWord != null) {
			passWord = md5.getMD5ofStr(passWord);
		}

		tempResult = userInfoexManager.registerNormalUserWithT(request, context, mobile, "1", passWord, WXConstants.LOGIN_CHANEL_03, WXConstants.LOGIN_NMARK_05);
		
		String returnCode = tempResult.getString("returnCode");
		logger.info("user服务返回值为： {}",tempResult);
		try{
			if ("0000".equals(returnCode)) {// 删除session中保存的发送过手机短信验证码的手机号码
				request.getSession(true).removeAttribute(SessionValue.SESSION_USERMOBILE);
				String msg = userInfoexManager.bindWeixin(request, context);
				logger.info("注册成功后，绑定微信，结果：" + msg);
				result.put("returnCode", "0000");
				result.put("returnMsg", "注册成功");
				DrainageUserInfoDto drainageUserInfo = prepareDrainageUserInfo(data,mobile);
				tempResult = drainageUserInfoManager.insertUserInfo(drainageUserInfo);
				if(!"0000".equals(tempResult.get("returnCode"))) {
					logger.warn("引流用户信息入库失败,但用户已经注册成功,当前引流用户信息为 ："+data + "，注册号码为："+mobile);
				}
				// 将引流信息存入session
				request.getSession().setAttribute(SessionValue.SESSION_SINO_USERINFO, data);
			} else {
				logger.warn("用户注册失败，当前用户信息为： "+data);
				result.put("returnCode", tempResult.get("returnCode"));
				result.put("returnMsg", "注册失败");
			}
		}catch(Exception e){
			logger.error("注册成功之后的绑定微信操作或插入引流用户信息失败，",e);
			result.put("returnCode", "9999");
			result.put("returnMsg", "注册失败");
		}
		return result.toString();
		/* register end*/
	}
	
	
	/**
	 * 验证签名
	 * @param data
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	@RequestMapping(value="/setUp/verifySign.xhtml",produces = "text/html;charset=UTF-8",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String verifySign(HttpServletRequest request) throws NoSuchAlgorithmException {
		SinoUserInfoDto data = new SinoUserInfoDto();
		try{
			data.setAppId(request.getParameter("appId"));
			data.setBrandName(request.getParameter("brandName"));
			data.setIdCard(request.getParameter("idCard"));
			data.setOwnerName(request.getParameter("ownerName"));
			data.setPlateNo(request.getParameter("plateNo"));
			data.setPurchasePrice(request.getParameter("purchasePrice"));
			data.setSign(request.getParameter("sign"));
			data.setSinoUserId(request.getParameter("sinoUserId"));
			data.setTimestamp(Long.parseLong(request.getParameter("timestamp")));
			
			String getSign = data == null ? null : data.getSign();
			String sign = sign(data);
			boolean flag = sign.equals(data.getSign());
			logger.info("接受到的签名为:{},生成的签名为:{},验签结果为：{},当前用户身份证为：{}，姓名为：{}",getSign,sign,flag,data.getIdCard(),data.getOwnerName());
			return String.valueOf(flag);
		}catch(NumberFormatException e) {
			logger.info("验证签名timestamp丢失，返回false");
			return Boolean.FALSE.toString();
		} catch(Exception e) {
			logger.error("签名时捕获异常,",e);
			return Boolean.FALSE.toString();
		}
	}
	
	/**
	 *
	 * 华安保险通道发送验证码
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/setUp/getMobileVerifyCodeForHABX.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST})
	@ResponseBody
	public String getMobileVerifyCodeForHABX(HttpServletRequest request) {

		String busiChannel = RequestHelper.verfiyIEChannel(request);

		// 手机号码
		String mobile = request.getParameter("mobile").trim();

		String seqId = request.getSession().getId();

		logger.debug("【ActivityController】getMobileVerifyCodeForHABX()开始>>>seqId=" + seqId + ">>>busiChannel+" + busiChannel + ">>>mobile=" + mobile + ">>>timestamp" + System.currentTimeMillis());

		JSONObject returnJsonObject = new JSONObject();
		// 日志信息
		Context context = ContextUtils.setContext(WXConstants.MESSAGE_SERVICE_801, busiChannel, "", System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

		returnJsonObject = userInfoexManager.verifyMobileAndGetVerifyCode(context, mobile, WXConstants.MOBILE_TYPE_0, RequestHelper.getIpAddr(request), seqId);

		String returnCode = returnJsonObject.getString("errorCode");

		// 验证码发送成功，删除验证图片验证码时保存的randomChannel，
		// 将用户手机号码保存到session中
		if (returnCode != null && returnCode.equals("0000")) {
			request.getSession(true).setAttribute(SessionValue.SESSION_USERMOBILE, mobile);
		}
		logger.debug("【ActivityController】getMobileVerifyCodeForHABX()结束>>>returnJsonObject=" + returnJsonObject.toString() + ">>>timestamp" + System.currentTimeMillis());
		returnJsonObject.put("returnCode", returnJsonObject.get("errorCode"));
		returnJsonObject.put("returnMsg", returnJsonObject.get("errorMsg"));
		returnJsonObject.remove("errorCode");
		returnJsonObject.remove("errorMsg");
		return returnJsonObject.toString();
	}
	
	
	/**
	 * 用户信息鉴权，开户
	 */
	@RequestMapping(value="/setUp/openAccountForActivity.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST,RequestMethod.GET})
	@TracingInfo(authority="10")
	@ResponseBody
	public String openAccountForActivity(HttpServletResponse response,HttpServletRequest request)throws Exception{
                logger.info("用户进入到活动开户接口");
		JSONObject returnJsonObject = new JSONObject();
		SinoUserInfoDto sinoUserInfo = (SinoUserInfoDto) request.getSession().getAttribute(SessionValue.SESSION_SINO_USERINFO);
		String busiChannel = RequestHelper.verfiyIEChannel(request);
		Object obj = request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
		if(sinoUserInfo == null) {
			logger.info("引流用户信息丢失 ，返回错误信息");
			returnJsonObject.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
			returnJsonObject.put("returnMsg", "该用户不具有参与活动的资格");
			return returnJsonObject.toString();
		}
		if(obj == null){
			returnJsonObject.put("returnCode", WXConstants.COMMON_ERROR_LOGINTIMEOUTCODE);
			returnJsonObject.put("returnMsg", WXConstants.COMMON_ERROR_LOGINTIMEOUTMSG);
			return returnJsonObject.toString();
		}
		List<ParameterDto> paramList = queryServiceClient.queryParameter("SYSTEM", "huaanActSwitch", null, null);
		String huaanSwitch = paramList != null && !paramList.isEmpty() ? paramList.get(0).getPmco() : null;
		if(StringUtils.isNotBlank(huaanSwitch) && "OFF".equals(huaanSwitch)) {
			logger.info("华安引流活动结束");
			returnJsonObject.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
			returnJsonObject.put("returnMsg", "活动已结束");
			return returnJsonObject.toString();
		}
		Context context=ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, busiChannel,"", System.currentTimeMillis(), System.currentTimeMillis(), "", "");
		UserBaseInfoDto userInfo = (UserBaseInfoDto)obj;
		String cmfUserId = userInfo.getCmfUserId();
		// fixed at 20181219 by hezk
		String mobile = request.getParameter("mobile");
		String userName = request.getParameter("name");
		userName = URLDecoder.decode(userName, "utf-8");
		String idNo = request.getParameter("idNo");
		String idType = "0";//证件类型 ：身份证 0
		String bankNumber = request.getParameter("bankNumber");
		String bankName = request.getParameter("bankName");
		bankName = URLDecoder.decode(bankName, "utf-8");
		String channelNo = request.getParameter("channelNo");//渠道代码
		String bankNo = request.getParameter("bankNo");//银行代码
		String awardType = request.getParameter("awardType");//奖品类型
		if(!sinoUserInfo.getOwnerName().equals(userName) || !sinoUserInfo.getIdCard().equals(idNo)){
			returnJsonObject = new JSONObject();
			returnJsonObject.put("returnCode","9999");
			returnJsonObject.put("returnMsg", "身份证或者姓名信息不一致");
			return returnJsonObject.toString();
		}
		UserServiceMessage queryUserAndAccoRlaById = userServiceClient.queryUserAndAccoRlaById(context, cmfUserId);
		UserBaseInfoDto userBaseInfoDto = queryUserAndAccoRlaById.getUserBaseInfoDto();
		UserAcctDto userAcctDto = new UserAcctDto();
		userAcctDto.setCmfUserId(cmfUserId);
		userAcctDto.setInvName(userName);
		userAcctDto.setBankMobile(mobile);
		userAcctDto.setIdNo(idNo);
		userAcctDto.setIdType(idType);
		userAcctDto.setBankName(bankName);
		userAcctDto.setBankAcct(bankNumber);
		userAcctDto.setChannelNo(channelNo);
		userAcctDto.setBankNo(bankNo);
		userAcctDto.setAddr(userBaseInfoDto.getNationNM()==null?"":userBaseInfoDto.getNationNM()+userBaseInfoDto.getProvinceNM()==null?"":userBaseInfoDto.getProvinceNM()+userBaseInfoDto.getCityNM()==null?"":userBaseInfoDto.getCityNM()+userBaseInfoDto.getAddr()==null?"":userBaseInfoDto.getAddr());
		if (userAcctDto.getAddr()==null || "".equals(userAcctDto.getAddr().trim())) {
			userAcctDto.setAddr("**");
		}
		userAcctDto.setVoccode(userBaseInfoDto.getVocCode());
		if (userAcctDto.getVoccode()==null || "".equals(userAcctDto.getVoccode().trim())) {
			userAcctDto.setVoccode("15");
		}
		//日志封装类
		logger.info("活动页面用户实名信息为:",userAcctDto);
		returnJsonObject = tradeManager.openAccountForActivity(context, request,userAcctDto);
		Object returnCode = returnJsonObject.get("returnCode");
		if(returnCode!=null && WXConstants.COMMON_SUCCESS.equals(returnCode.toString())){
			//验证是否是微信浏览器发起请求
			String ieType = request.getHeader("user-agent").toLowerCase();
			if(ieType.indexOf("micromessenger")<0){//其他浏览器
				logger.info("openAccount()方法，其他浏览器发起鉴权请求，不做分组处理");
			}else{//微信浏览器
				Object obj1 = request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
				if(obj1!=null){
					//修改用户分组为已鉴权分组
					userInfoexManager.modifyUserGroup(obj1.toString(), WXConstants.AUTHORITYGROUPID);
				}
			}
		} else {
			return returnJsonObject.toString();
		}
		String openid = (String) request.getSession().getAttribute(SessionValue.SESSION_OPENID);
		if("phoneCard".equals(awardType)) { // 如果是选择电话卡充值
			mobile = request.getParameter("assignMobile");//充值手机
		}
		executor.execute(getThread(sinoUserInfo, mobile, openid, awardType));
		returnJsonObject.put("returnCode", "0000");
		returnJsonObject.put("returnMsg", "添加成功");
		logger.info("开户返回信息为：{}",returnJsonObject);
		return returnJsonObject.toString();
	}

	/**
	 * 提供于登陆时查询用户信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/setUp/queryDrainageUserActivityPeriod.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String queryDrainageUserActivityPeriod(HttpServletRequest request) {
		// period = 1 未注册  period =2 已注册未实名 period =3 已实名
		JSONObject obj = new JSONObject();
		String idcard = request.getParameter("idCard");
		String ownerName = request.getParameter("ownerName");
		if(StringUtils.isBlank(idcard) || StringUtils.isBlank(ownerName)) {
			logger.info("查询用户信息时,缺失关键参数,不查询");
			obj.put("returnCode", "9999");
			obj.put("returnMsg", "关键参数缺失");
			return obj.toString();
		}
		logger.info("开始根据身份证和用户姓名查询用户注册信息，idcard:{},ownerName:{}",idcard,ownerName);
		try{
			obj.put("returnCode","0000");
			obj.put("returnMsg", "success");
			DrainageUserInfoDto result = drainageUserInfoManager.queryUserInfoByIdCardAndOwnerName(idcard,ownerName);
			logger.info("根据参数查询到用户引流信息为： {}",result == null ? null : result.getUserId());
			if(result == null) {
				 obj.put("period", "1");
				 logger.info("用户当前引流信息为空，返回period值为1");
			} else {
				SinoUserInfoDto sinoUserInfo = preparedSinoUserInfoDto(result);
				request.getSession().setAttribute(SessionValue.SESSION_SINO_USERINFO, sinoUserInfo);
				UserBaseInfoDto userBaseInfo = waecuserInfoDao.queryUserBaseInfoByMobile(result.getUserMobile());
				if(userBaseInfo == null) {
					logger.warn("用户信息已存入引流用户信息表内，但查询不到用户注册信息，当前查询信息使用参数， idCard:{},ownerName:{},mobile:{}",
							idcard,ownerName,result.getUserMobile());
					obj.put("period", "1");
					logger.info("用户无注册信息，返回period值为1");
				} else {
					UserServiceMessage userServiceResult = userServiceClient.queryUserAndAccoRlaById(null, userBaseInfo.getCmfUserId());
					UserAccoRlaDto userAccoRlaDto = userServiceResult.getUserAccoRlaDto();
					if(userAccoRlaDto != null) {
						request.getSession().setAttribute(SessionValue.SESSION_USERACCORLA, userAccoRlaDto);
					}
					request.getSession().setAttribute(SessionValue.SESSION_USERBASEINFO, userBaseInfo);
					String userType = userBaseInfo.getUserType();
					logger.info("当前用户类型为: {}",userType);
					if(!StringUtils.isBlank(userType) && "30".equals(userType)) {
						obj.put("period", "3");
					} else {
						obj.put("period", "2");
					}
					logger.info("返回period值为： {}",obj.get("period"));
				}
			}
		}catch(Exception e) {
			logger.error("根据身份证和用户姓名查询引流信息时捕获异常，",e);
			obj.put("returnCode", "9999");
			obj.put("returnMsg", "系统内部服务器异常");
		}
		return obj.toString();
	}
	
	
	/**
	 * 根据银行卡号查找该卡号的具体银行信息 即卡号校验
	 * 
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value = "/setUp/queryBankInfoByBankNumber.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryBankInfoByBankNumber(HttpServletResponse response, HttpServletRequest request) throws Exception {
		JSONObject returnJsonObject = new JSONObject();
		SinoUserInfoDto sinoUserInfo = (SinoUserInfoDto) request.getSession().getAttribute(SessionValue.SESSION_SINO_USERINFO);
		if(sinoUserInfo == null) {
			logger.info("引流用户信息丢失 ，返回错误信息");
			returnJsonObject.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
			returnJsonObject.put("returnMsg", "该用户不具有参与活动的资格");
			return returnJsonObject.toString();
		}
		String busiChannel = RequestHelper.verfiyIEChannel(request);

		String bankNumber = request.getParameter("bankNumber");

		String sessionId = request.getSession().getId();
		String cmfUserId = "";
		UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
		if (userBaseInfoDto != null) {
			cmfUserId = userBaseInfoDto.getCmfUserId();
		}

		Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");

		returnJsonObject = queryManager.queryBankInfoByBankNumber(context, bankNumber);

		return returnJsonObject.toString();
	}
	
	@RequestMapping(value = "/setUp/queryBankLogoList.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String queryBankLogoList(HttpServletRequest request) {
		return activityManager.queryBankLogoList().toString();
	}
	
	public String sign(SinoUserInfoDto data) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		String charSet = "UTF-8";
		logger.info("生成签名字符串，参数为： "+data);
		StringBuffer signBuffer = new StringBuffer();
		signBuffer.append(URLDecoder.decode(data.getPlateNo(),charSet));
		signBuffer.append(URLDecoder.decode(data.getIdCard(),charSet));
		signBuffer.append(URLDecoder.decode(data.getOwnerName(),charSet));
		signBuffer.append(URLDecoder.decode(data.getBrandName(),charSet));
		signBuffer.append(URLDecoder.decode(data.getPurchasePrice(),charSet));
		signBuffer.append(URLDecoder.decode(data.getSinoUserId(),charSet));
		signBuffer.append(URLDecoder.decode(""+data.getTimestamp(),charSet));
		signBuffer.append(SECRECT);
		if(StringUtils.isBlank(signBuffer.toString())) {
			logger.info("参数为空，不生成签名，返回空字符串");
			return "";
		}
		MessageDigest digest = MessageDigest.getInstance("MD5");
		try {
			digest.update(URLEncoder.encode(signBuffer.toString(), "UTF-8").getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error("生成签名加密时捕获异常",e);
			return "";
		}
		String sign = DESUtils.bytes2Hex(digest.digest());
		logger.info("生成的签名为："+sign);
		return sign;
	}

	
	private DrainageUserInfoDto prepareDrainageUserInfo(SinoUserInfoDto userInfo,String mobile) {
		DrainageUserInfoDto returnDto = new DrainageUserInfoDto();
		returnDto.setAppid(userInfo.getAppId());
		returnDto.setBrandName(userInfo.getBrandName());
		returnDto.setPurchasePrice(userInfo.getPurchasePrice());
		returnDto.setSinoUserid(userInfo.getSinoUserId());
		returnDto.setUserIdcard(userInfo.getIdCard());
		returnDto.setUserName(userInfo.getOwnerName());
		returnDto.setUserPlateno(userInfo.getPlateNo());
		returnDto.setUserMobile(mobile);
		returnDto.setUserProvince(userInfo.getProvince());
		return returnDto;
	}
	
	private SinoUserInfoDto preparedSinoUserInfoDto(DrainageUserInfoDto data) {
		SinoUserInfoDto result = new SinoUserInfoDto();
		result.setAppId(data.getAppid());
		result.setBrandName(data.getBrandName());
		result.setIdCard(data.getUserIdcard());
		result.setOwnerName(data.getUserName());
		result.setPlateNo(data.getUserPlateno());
		result.setPurchasePrice(data.getPurchasePrice());
		result.setSinoUserId(data.getSinoUserid());
		return result;
	}
	
	private SendMsgAndRedirectThread getThread(SinoUserInfoDto dto,String mobile,String openid,String awardType) {
		MobileMessagePushModel param = new MobileMessagePushModel(dto.getIdCard(),openid,null,mobile, null, awardType);
		return new SendMsgAndRedirectThread(param, dto, this.restTemplate);
	}
	
	@Bean
	public ThreadPoolExecutor threadPoolTaskExecutor() {
		ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
		threadPool.setKeepAliveTime(300, TimeUnit.SECONDS);
		threadPool.setRejectedExecutionHandler(new CallerRunsPolicy());
		return threadPool;
	}
	
	@Bean(name="restTemplate")
	public RestTemplate getRestTemplate(){
		return new RestTemplate();
	}
	
}
