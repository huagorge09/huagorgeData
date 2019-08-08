package com.cmwa.ec.weixin.manager.business.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.cmf.weixin.message.dto.req.EventReqMsgDto;
import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.user.facade.dto.UserBaseInfoDto;
import com.cmwa.ec.user.facade.dto.UserServiceMessage;
import com.cmwa.ec.weixin.client.UserServiceClient;
import com.cmwa.ec.weixin.constants.WXConstants;
import com.cmwa.ec.weixin.dao.UserChannelDao;
import com.cmwa.ec.weixin.dao.UserInfoDao;
import com.cmwa.ec.weixin.dao.UserInfoexDao;
import com.cmwa.ec.weixin.dto.CmwaWxUserInfoDto;
import com.cmwa.ec.weixin.dto.UserInfoDto;
import com.cmwa.ec.weixin.dto.UserInfoexDto;
import com.cmwa.ec.weixin.manager.business.UserInfoManager;
import com.cmwa.ec.weixin.manager.business.UserInfoexManager;
import com.cmwa.ec.weixin.util.ContextUtils;
import com.cmwa.ec.weixin.util.DateUtils;
import com.cmwa.ec.weixin.util.HttpPostUtil;
import com.cmwa.ec.weixin.util.JsonStringToObjectUtil;
import com.cmwa.ec.weixin.util.SpringContextUtil;
import com.cmwa.ec.weixin.util.StringUtils;
import com.cmwa.ec.weixin.util.UUIDKeyGenerator;
import com.cmwa.ec.weixin.util.socket.SocketUtil;

public class UserInfoManagerImpl implements UserInfoManager {
	
	private Logger logger = Logger.getLogger(UserInfoManagerImpl.class);

	@Autowired
	private UserInfoDao userInfoDao;
	
	@Autowired
	private UserInfoexDao userInfoexDao;
	
	@Autowired
	private UserChannelDao userChannelDao;
	
	@Autowired
	private UserInfoexManager userInfoexManager;
	
	@Autowired
	private UserServiceClient userServiceClient;
	
	@Override
	public boolean syncUserInfo(String openid) {
		  this.logger.info("同步微信用户信息openid=" + openid);
		    boolean ret = false;
		    try
		    {
		    	UserInfoDto userDto = null;
		      try {
		        userDto = syncUserInfoToSocket(openid);
		        this.logger.info("同步微信用户信息userDto=" + (userDto == null ? "null" : userDto.toString()));
		      } catch (Exception e) {
		        this.logger.error("同步微信用户信息异常openid=" + openid, e);
		      }

		      if ((userDto != null) && (userDto.getOpenid() != null) && (userDto.getOpenid().trim().length() > 0))
		      {
		    	//先查询是否存在，不存在则新增
		    	UserInfoDto udto=queryByOpenId(openid);
		    	if(udto!=null){
		    	userDto.setStatus("1");
		        ret = updateUserInfo(userDto) > 0;
		        	
		    	}
		      }else{
		    	  	UserInfoDto insertDto = new UserInfoDto();
					insertDto.setOpenid(openid);
					UUIDKeyGenerator uuidGen = new UUIDKeyGenerator();
					String userid = uuidGen.generateUUIDKey();
					insertDto.setUserid(userid);
					insertDto.setStatus("1"); 
					try {
						ret = userInfoDao.createUserInfo(insertDto) > 0;
					} catch (Exception e) {
						logger.error("创建用户信息异常",e);
					}
		      }
		      /*20180528新增同步数据到cmwa_wx_userino表*/
		      String tempStr = JSONObject.fromObject(userDto).toString();
		      String result = HttpPostUtil.sendPostJsonFormat(SpringContextUtil.getProperty("DECRYPTSERVICE_URL")+"/"+"wxuserinfo",  "application/json;charset=utf-8", tempStr);
		      if(StringUtils.isEmptyString(result)){
		    	  logger.info("调用远程api失败");
		      }
		      this.logger.info("同步微信用户信息openid=" + openid + ",结果：" + ret);
		      return ret;
		    } catch (Exception ee) {
		      this.logger.error("同步微信用户信息异常openid=" + openid, ee);
		    }
		    return ret;
	}
	
	  public UserInfoDto syncUserInfoToSocket(String openid)
			    throws Exception
			  {
			    this.logger.info("syncUserInfoToSocket openid:" + openid);
			    try
			    {
			      String reqXml = StringUtils.toXmlMessage("syncuser", openid);
			      String resStr = SocketUtil.sendSocketMessage(reqXml);
			      this.logger.info("syncUserInfoToSocket resStr:" + resStr);
//			      UserInfoDto wxDto = (UserInfoDto)new ObjectMapper().readValue(resStr, UserInfoDto.class);
			      UserInfoDto wxDto = (UserInfoDto)JsonStringToObjectUtil.jsonStringToObject(resStr, "com.cmwa.ec.weixin.dto.UserInfoDto");
			      if (wxDto != null) {
			        return wxDto;
			      }
			      return null;
			    }
			    catch (Exception e) {
			      this.logger.error("syncUserInfoToSocket authorAPI:", e);
			    }
			    return null;
			  }

	@Override
	public int updateUserInfo(UserInfoDto userInfo) {
		return userInfoDao.updateUserInfo(userInfo);
	}

	/**
	 * 根据用户信息
	 */
	@Override
	public UserInfoDto queryByOpenId(String openid) {
		UserInfoDto userInfo = userInfoDao.queryByOpenId(openid);
		if(userInfo == null){
			UserInfoDto insertDto = new UserInfoDto();
			insertDto.setOpenid(openid);
			UUIDKeyGenerator uuidGen = new UUIDKeyGenerator();
			String userid = uuidGen.generateUUIDKey();
			insertDto.setUserid(userid);
			insertDto.setStatus("1");
			//insertDto.setSubscribeTime("2014-08-26 16:03:03");
			int ret=0;
			try {
				ret = userInfoDao.createUserInfo(insertDto);
			} catch (Exception e) {
				logger.error("queryByOpenId异常",e);
			}
			
			if(ret > 0 ){
				return insertDto;
			}else{
				logger.info("queryUserInfoByOpenId, 插入用户数据失败openId="+openid);
				return null;
			}
		}
		return userInfo;
	 
	}

	/**
	 * 根据openid status 查询用户信息
	 */
	@Override
	public void updateUserInfoStatus(String openid, String status) {
		 userInfoDao.updateUserInfoStatus(openid, status);
	}

	@Override
	public UserInfoDto queryByOpenIdAndStatus(String openid, String status) {
		return userInfoDao.queryByOpenIdAndStatus(openid, status);
	}

	/**
	 * 方法说明：此方法用于用户关注公众号时，执行的操作
	 * 如果用户在pc交易平台注册并鉴权，然后关注我们的公众号
	 * 这个时候，要根据用户类型，修改对应分组
	 * @param openid
	 * @param seqId
	 * @return
	 */
	public String modifyUserGroupByUserType(String openid,String seqId) throws Exception{
		String returnStr = null;
		logger.info("进入修改分组方法（）。。。");
		UserInfoexDto userInfo = userInfoexDao.queryByOpenId(openid, "");//statu设置为空的时候，就按照openId查询所有
		if(userInfo!=null){//判断用户是否之前已经注册过
			logger.info("已注册过的用户。。。");
			String cmfUserId = userInfo.getCmfuserid();
			if(cmfUserId!=null ||!"".equals(cmfUserId)){
				//根据cmfUserId查询用户信息，确认用户类型  （注册用户、鉴权用户）
				UserServiceClient userClient = (UserServiceClient)SpringContextUtil.getBean("userServiceClient");
				Context context=ContextUtils.setContext(WXConstants.USER_SERVICE_001,WXConstants.SERVICE_CHANNEL_WEIXIN,"", System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
				UserServiceMessage userServiceMessage = userClient.queryUserAndAccoRlaById(context, cmfUserId);
				if(null!=userServiceMessage && "0000".equals(userServiceMessage.getReturnCode())){
					UserBaseInfoDto userBaseInfo = userServiceMessage.getUserBaseInfoDto();
					if(userBaseInfo!=null){
						if(userBaseInfo.getUserType()!=null && userBaseInfo.getUserType().equals("30")){
							logger.info("已鉴权过的用户。。。");
							//鉴权用户
							returnStr = userInfoexManager.modifyUserGroup(openid, WXConstants.AUTHORITYGROUPID);
						}else{
							//注册用户
							returnStr = userInfoexManager.modifyUserGroup(openid, WXConstants.REGISTERGROUPID);
						}
					}
				}
			}
		}
		logger.info("返回结果。"+returnStr);
		return returnStr;
	}
	
	public void updateUserCode(String openid,String md5Code)
	{
		userInfoDao.updateUserCode(openid, md5Code);
	}
	
	
	public int queryValidUserCode(String openid,String md5Code)
	{
		return userInfoDao.queryValidUserCode(openid, md5Code);
	}
	
	public String queryUserCode(String openid)
	{
		return userInfoDao.queryUserCode(openid);
	}

	/**
	 * 推送template消息或者service消息
	 * @param msg
	 * @param openId
	 * @param msgType
	 */
	@Override
	public void pushWXMessage(String msg, String openId, String msgType) {
		logger.info("---------->>serviceMessage:"+msg);
		msg = msg.replace("{openid}", openId);
		logger.info("---------->>serviceMessage_new:"+msg);
		String resStr = null;
		try {
			String xmlPost = StringUtils.toXmlMessage(msgType, msg);
		
			resStr = SocketUtil.sendSocketMessage(xmlPost);
			logger.info("发送socket请求发送模板消息，resStr："+resStr);
		} catch (Exception e) {
			logger.info("发送socket请求发送模板消息，抛出异常，resStr："+resStr,e);
		}
	}
	
	@Override
	public int insertUserChannel(String openId, String channel,String eventType) {
		return userChannelDao.insertUserChannel(openId, channel,eventType);
		
	}

	@Override
	public List<UserInfoDto> queryUserInfoByStatus(String status) {
		List<UserInfoDto> userInfoDtos = new ArrayList<UserInfoDto>();
		try {
			userInfoDtos = userInfoDao.queryUserInfoByStatus(status);
			logger.info("根据状态："+status+"查询用户信息成功！");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("根据状态："+status+"查询用户信息异常：",e);
			new Exception(e);
		}
		return userInfoDtos;
	}

	@Override
	public void saveOrUpdateCmwaWxUserInfo(EventReqMsgDto eventReqDto,String status) {
		try {
			CmwaWxUserInfoDto CmwaWxUserInfoDto = new CmwaWxUserInfoDto(eventReqDto.getFromUserName(),status,DateUtils.formatDate(new Date()));
			String json = JSONArray.toJSONString(CmwaWxUserInfoDto);
			String response = HttpPostUtil.sendPostJsonFormat(SpringContextUtil.getProperty("DECRYPTSERVICE_URL")+"/"+"wxuserinfo",  "application/json;charset=utf-8" ,json);
			JSONObject jsonObject = JSONObject.fromObject(response);
			if ("true".equals(String.valueOf(jsonObject.get("success")))) {//修改、新增成功
				logger.info("关注时候修改或者新增cmwa_wx_user_info纪录成功");
			} else {
				logger.warn("关注时候修改或者新增cmwa_wx_user_info纪录失败");
			}
		} catch (Exception e) {
			logger.error("关注时候修改或者新增cmwa_wx_user_info表发生异常：", e);
		}
		
	}
	
	@Override
	public CmwaWxUserInfoDto queryIsSubscribeByOpenIdOnCmwaWxUserInfo(String openId) {
		return userInfoDao.queryIsSubscribeByOpenIdOnCmwaWxUserInfo(openId);
	}

	/* (non-Javadoc)
	 * @see com.cmwa.ec.weixin.manager.business.UserInfoManager#queryUserAndAccoRlaById(java.lang.String)
	 */
	@Override
	public UserServiceMessage queryUserAndAccoRlaById(String cmfUserId) {
		UserServiceMessage queryUserAndAccoRlaById = userServiceClient.queryUserAndAccoRlaById(null, cmfUserId);
		return queryUserAndAccoRlaById;
	}
	
	
	
}
