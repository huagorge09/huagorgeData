package com.cmwa.ec.weixin.manager.business.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.message.facade.dto.MsgSmsRecordDto;
import com.cmwa.ec.trade.facade.dto.OrderResult;
import com.cmwa.ec.user.facade.dto.UserAccoRlaDto;
import com.cmwa.ec.user.facade.dto.UserBaseInfoDto;
import com.cmwa.ec.user.facade.dto.UserServiceMessage;
import com.cmwa.ec.weixin.client.MessageServiceClient;
import com.cmwa.ec.weixin.client.QueryServiceClient;
import com.cmwa.ec.weixin.client.TradeServiceClient;
import com.cmwa.ec.weixin.client.UserServiceClient;
import com.cmwa.ec.weixin.constants.WXConstants;
import com.cmwa.ec.weixin.dao.ActivityDao;
import com.cmwa.ec.weixin.dao.BankLogoDao;
import com.cmwa.ec.weixin.dto.BankLogoDto;
import com.cmwa.ec.weixin.dto.InvectorUserInfoexDto;
import com.cmwa.ec.weixin.dto.InvestorAnswerDto;
import com.cmwa.ec.weixin.dto.InvestorAwardDto;
import com.cmwa.ec.weixin.dto.InvestorForwardDto;
import com.cmwa.ec.weixin.dto.InvestorUserDto;
import com.cmwa.ec.weixin.dto.ParameterDto;
import com.cmwa.ec.weixin.dto.UserInfoexDto;
import com.cmwa.ec.weixin.manager.business.ActivityManager;
import com.cmwa.ec.weixin.manager.business.UserInfoManager;
import com.cmwa.ec.weixin.manager.business.UserInfoexManager;
import com.cmwa.ec.weixin.util.ContextUtils;
import com.cmwa.ec.weixin.util.RequestHelper;
import com.cmwa.ec.weixin.util.SessionValue;
import com.cmwa.ec.weixin.util.SpringContextUtil;
import com.cmwa.ec.weixin.util.StringUtils;
import com.cmwa.ec.weixin.util.cache.ParameterCache;

/**
 * 活动管理
 * 
 * @author luos
 * 
 */
public class ActivityManagerImpl implements ActivityManager {

	@Autowired
	private UserInfoManager userInfoManager;
	@Autowired
	private TradeServiceClient tradeServiceClient;
	@Autowired
	private MessageServiceClient messageServiceClient;
	@Autowired
	private ActivityDao activityDao;
	@Autowired
	private QueryServiceClient queryServiceClient;
	@Autowired
	private BankLogoDao bankLogoDao;
	
	private static Logger logger = Logger.getLogger(ActivityManagerImpl.class.getName());

	@Override
	public void pushRefflePrizeSuccMsg(String openId, String ticket1, String ticket2) {
		logger.info("ActivityManagerImpl[pushRefflePrizeSuccMsg]奖品领取成功后推送图文消息开始>>>>>ticket1:"+ticket1+">>>>>ticket2"+ticket2);
		//获取参数字典里的对应key的推送消息
		String key = WXConstants.PMST_MESSAGE + "#" + WXConstants.FATHER_DAY + "#" + WXConstants.FATHER_MOVIE_TICKET_SUCCESS;
		ParameterDto paramDto = (ParameterDto) ParameterCache.getData().get(key);
		String serviceMessage = paramDto.getPmv1();
		//替换消息指定内容
		serviceMessage = serviceMessage.replace("{openid}", openId);
		serviceMessage = serviceMessage.replace("{ticket1}", ticket1);
		serviceMessage = serviceMessage.replace("{ticket2}", ticket2);
		logger.info("ActivityManagerImpl[pushRefflePrizeSuccMsg]消息内容:" + serviceMessage);
		//开始推送
		userInfoManager.pushWXMessage(serviceMessage, openId, "service");
		logger.info("ActivityManagerImpl[pushRefflePrizeSuccMsg]奖品领取成功后推送图文消息结束");
	}

	@Override
	public JSONObject fatherDayDrawTicket(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String seqId = request.getSession(true).getId();
		String busiChannel = RequestHelper.verfiyIEChannel(request);
		String cmfUserId = "";
		String mobile = "";
		String openId = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
		UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
		if (userBaseInfoDto == null) {
			cmfUserId = queryCmfUserIdByOpenId(openId);
			// 判断用户是否绑定微信
	        if (null != cmfUserId && !"".equals(cmfUserId)) {
	        	autoLogin(request);
	        	userBaseInfoDto=(UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
	        	cmfUserId = userBaseInfoDto.getCmfUserId();
				mobile = userBaseInfoDto.getMobile();
	        } else {
	            // 未绑定微信，调转到微信注册页面
	        	jsonObject.put("returnCode", WXConstants.COMMON_ERROR_BINDCODE);
	        	jsonObject.put("returnMsg", WXConstants.COMMON_ERROR_BINDMSG);
	        	return jsonObject;
	        }
		} else {
			cmfUserId = userBaseInfoDto.getCmfUserId();
			mobile = userBaseInfoDto.getMobile();
		}
		Context context = ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		//获取微信的openId
		//调用领奖接口，得到返回结果
		//0000：领取成功，返回消息：电影券，以逗号隔开
		//0010：奖品已领完，返回消息：奖品已领完
		//0020：已经领取，返回消息：电影券，以逗号隔开
		//9999：系统异常，提示网络繁忙即可
		//9998:关键参数为空
		OrderResult orderResult = tradeServiceClient.activityGet(context, openId, WXConstants.FATHER_DAY_MANAGER, "");
		jsonObject.put("returnCode", orderResult.getResultCode());
		jsonObject.put("returnMsg", orderResult.getResultMsg());
		String ticket1 = "";
		String ticket2 = "";
		StringBuilder sms = new StringBuilder();
		//如果领取成功或者已经领取过，把券码接收给推送的消息
		if ("0000".equals(orderResult.getResultCode())) {
			String[] strarray = orderResult.getResultMsg().split(",");
			ticket1 = strarray[0];
			ticket2 = strarray[1];
			//推送消息
			pushRefflePrizeSuccMsg(openId, ticket1, ticket2);
			//如果领取成功，发送消息至用户
			sms.append("感谢参加招商财富·父亲节活动，送上格瓦拉电影抵用券2张，卡密分别为").append(orderResult.getResultMsg()).append("。让我们约上老爸一起看电影，祝天下的父亲们节日快乐！携手百年招商，共享财富人生！");
			MsgSmsRecordDto msgRecord = getMsgSmsRecord(mobile, sms.toString());
			messageServiceClient.justSendMsg(msgRecord);
		}
		return jsonObject;
	}
    /**
     * 通过openId获取cmfUserId 判断用户是否绑定微信（即是否注册）
     * 
     * @param openId
     * @return
     */
    public String queryCmfUserIdByOpenId(String openId) {

        UserInfoexManager userInfoexManager = (UserInfoexManager) SpringContextUtil.getBean("userInfoexManager");
        UserInfoexDto dto = userInfoexManager.queryUserinfoexQueryRelation(openId, WXConstants.USERINFOEX_BINDSTAT_R);
        String cmfUserId = null;
        if (dto != null) {
            cmfUserId = dto.getCmfuserid();
        }

        return cmfUserId;
    }
    /**
     * 自动登录，用于不需要登录的页面，进入页面自动登录 仅限于微信端
     * 
     * @param openId
     * @param request
     *            void
     * @author maj
     */
    @Override
    public void autoLogin(HttpServletRequest request) {
        // 获取访问的浏览器来源
        String channel = (String) request.getSession(true).getAttribute(SessionValue.SESSION_CHANNEL);

        // 如果不是其他浏览器 即微信浏览器 则去获取openid
        if (!WXConstants.OTHER_CHANNEL.equals(channel)) {

            String openId = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
            if (openId != null && !openId.equals("")) {
                // 通过openid获取cmfUserId
                String cmfUserId = queryCmfUserIdByOpenId(openId);
                // 判断用户是否绑定微信
                if (null != cmfUserId && !"".equals(cmfUserId)) {

                    UserBaseInfoDto userBaseInfo = null;
                    UserAccoRlaDto userAcco = null;
                    UserServiceMessage userServiceMessage = queryUserInfoByCmfUserId(cmfUserId, request);

                    if (null != userServiceMessage && "0000".equals(userServiceMessage.getReturnCode())) {
                        userBaseInfo = userServiceMessage.getUserBaseInfoDto();
                        userAcco = userServiceMessage.getUserAccoRlaDto();
                    }

                    if (userBaseInfo != null) {
                        request.getSession(true).setAttribute(SessionValue.SESSION_USERBASEINFO, userBaseInfo);
                        request.getSession(true).setAttribute(SessionValue.SESSION_CMFUSERID, userBaseInfo.getCmfUserId());
                    }
                    if (userAcco != null) {
                        logger.info("VerifyUserInfoFilter,if{userAcco!=null},fffffffffffffk,根据cmfUserId获取userAcco,cmfUserId=" + cmfUserId + ",custNo=" + userAcco.getEcCustNo());
                        request.getSession(true).setAttribute(SessionValue.SESSION_USERACCORLA, userAcco);
                    } else {
                        logger.info("VerifyUserInfoFilter,if{userAcco=null},fffffffffffffk,根据cmfUserId获取的userAcco为空,cmfUserId=" + cmfUserId);
                    }
                }
            }
        }
    }
    /**
     * 通过cmfUserId获取用户基本信息
     * 
     * @param cmfUserId
     * @param request
     * @return
     */
    public UserServiceMessage queryUserInfoByCmfUserId(String cmfUserId, HttpServletRequest request) {
        UserServiceClient userClient = (UserServiceClient) SpringContextUtil.getBean("userServiceClient");
        String seqId = request.getSession().getId();
        Context context = ContextUtils.setContext(WXConstants.USER_SERVICE_001, WXConstants.SERVICE_CHANNEL_WEIXIN, "", System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
        UserServiceMessage userServiceMessage = userClient.queryUserAndAccoRlaById(context, cmfUserId);
        return userServiceMessage;
    }

	@Override
	public JSONObject magpieFestivalQA(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String answer = request.getParameter("answer");
		if (answer == null || "".equals(answer)) {
			jsonObject.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
			jsonObject.put("returnMsg", "答案不能为空");
		} else {
			String[] strs = answer.split(",");
			double blNum = 0d;
			for (String s : strs) {
				blNum += Double.valueOf(s);
			}
			if (blNum > 0) {
				jsonObject.put("returnCode", WXConstants.COMMON_SUCCESS);
				jsonObject.put("returnMsg", "问卷回答成功，去领奖吧！");
				jsonObject.put("answer", Math.round(blNum));
			} else {
				jsonObject.put("returnCode", WXConstants.COMMON_ERROR_REDATAISNULLCODE);
				jsonObject.put("returnMsg", WXConstants.COMMON_ERROR_REDATAISNULLMSG);
				jsonObject.put("answer", "0");
			}
		}
		return jsonObject;
	}
	@Override
	public JSONObject magpieFestivalDrawTicket(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String mobile = "";
		String openId = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
		//获取微信的openId
		//调用领奖接口，得到返回结果
		//0000：领取成功，返回消息：电影券，以逗号隔开
		//0010：奖品已领完，返回消息：奖品已领完
		//0020：已经领取，返回消息：电影券，以逗号隔开
		//9999：系统异常，提示网络繁忙即可
		//9998:关键参数为空
		OrderResult orderResult = tradeServiceClient.activityGet(new Context(), openId, WXConstants.MAGPIEFESTIVAL_EVENT_MANAGER, "");
		jsonObject.put("returnCode", orderResult.getResultCode());
		jsonObject.put("returnMsg", orderResult.getResultMsg());
		StringBuilder sms = new StringBuilder();
		String ticket1 = "";
		String ticket2 = "";
		UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
		if (userBaseInfoDto != null) {
			mobile = userBaseInfoDto.getMobile();
		}
		//如果领取成功或者已经领取过，把券码接收给推送的消息
		if ("0000".equals(orderResult.getResultCode())) {
			String[] strarray = orderResult.getResultMsg().split(",");
			ticket1 = strarray[0];
			ticket2 = strarray[1];
			//推送消息
			magpieFestivalDrawTicketPushRefflePrizeSuccMsg(openId, ticket1,ticket2);
			//如果session取到用户对象了，就发送短信通知用户
			if (userBaseInfoDto != null) {
				//如果领取成功，发送消息至用户
				sms.append("尊敬的客户:感谢您参与保护投资者权益七夕有奖活动，恭喜您获得格瓦拉电影券两张,您的券号为").append(orderResult.getResultMsg()).append("。您的私人财富管家【招商财富】！");
				MsgSmsRecordDto msgRecord = getMsgSmsRecord(mobile, sms.toString());
				messageServiceClient.justSendMsg(msgRecord);
			}
		}
		return jsonObject;
	}
	@Override
	public void magpieFestivalDrawTicketPushRefflePrizeSuccMsg(String openId, String ticket1,String ticket2) {
		logger.info("ActivityManagerImpl[pushRefflePrizeSuccMsg]奖品领取成功后推送图文消息开始>>>>>ticket1:"+ticket1+">>>>>ticket2:"+ticket2);
		//获取参数字典里的对应key的推送消息
		String key = WXConstants.PMST_MESSAGE + "#" + WXConstants.MAGPIEFESTIVAL + "#" + WXConstants.MAGPIEFESTIVAL_SUCCESS;
		ParameterDto paramDto = (ParameterDto) ParameterCache.getData().get(key);
		String serviceMessage = paramDto.getPmv1();
		//替换消息指定内容
		serviceMessage = serviceMessage.replace("{openid}", openId);
		serviceMessage = serviceMessage.replace("{ticket1}", ticket1);
		serviceMessage = serviceMessage.replace("{ticket2}", ticket2);
		logger.info("ActivityManagerImpl[pushRefflePrizeSuccMsg]消息内容:" + serviceMessage);
		//开始推送
		userInfoManager.pushWXMessage(serviceMessage, openId, "service");
		logger.info("ActivityManagerImpl[pushRefflePrizeSuccMsg]奖品领取成功后推送图文消息结束>>>>>timestamp:" + System.currentTimeMillis());
	}

	@Override
	public JSONObject nationalDayQA(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String answer = request.getParameter("answer");
		if (answer == null || "".equals(answer)) {
			jsonObject.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
			jsonObject.put("returnMsg", "答案不能为空");
		} else {
			String[] strs = answer.split(",");
			double blNum = 0d;
			for (String s : strs) {
				blNum += Double.valueOf(s);
			}
			if (blNum > 0) {
				jsonObject.put("returnCode", WXConstants.COMMON_SUCCESS);
				jsonObject.put("returnMsg", "问卷回答成功！");
				jsonObject.put("answer", Math.round(blNum));
			} else {
				jsonObject.put("returnCode", WXConstants.COMMON_ERROR_REDATAISNULLCODE);
				jsonObject.put("returnMsg", WXConstants.COMMON_ERROR_REDATAISNULLMSG);
				jsonObject.put("answer", "0");
			}
		}
		return jsonObject;
	}

	@Override
	public JSONObject userForward(String openId,String type) {
		logger.info("ActivityManagerImpl[userForward]用户转发开始>>>>>openId:"+openId);
		int result = 0;
		JSONObject jsonObject = new JSONObject();
		InvestorAwardDto investorAwardDto = null;
		InvestorForwardDto investorForwardDto = null;
		openId = StringUtils.isEmptyString(openId)?"visitor":openId;
		jsonObject.put("returnCode", "0");
		jsonObject.put("returnMsg", "请求成功");
		investorForwardDto = activityDao.queryUserForwardRecord(openId);
		if(investorForwardDto == null) {
			try {
				//创建用户分享记录
				result = activityDao.createUserForwardRecord(openId,type);
			} catch (Exception e) {
				logger.error("用户转发创建用户转发记录捕获异常",e);
			}
		}else {
			try {
				//更新用户分享记录
				result = activityDao.updateUserForwardRecord(openId,type);
			} catch (Exception e) {
				logger.error("用户转发更新用户转发记录捕获异常",e);
			}
		}
		if(result == 0) {
			jsonObject.put("returnCode", "1");
			jsonObject.put("returnMsg", "网络繁忙");
		}else {
			result = 0;
			//查询判断用户是否获奖且奖品状态为 1
			investorAwardDto = activityDao.queryUserAwardRecord(openId);
			if(null != investorAwardDto && investorAwardDto.getStatus().equals("1")) {
				try {
					//将用户奖品状态改为 2
					result = activityDao.updateUserAwardRecord(openId, "2");
				} catch (Exception e) {
					logger.error("用户转发更新用户奖品记录捕获异常",e);
				}
				if(result == 0) {
					jsonObject.put("returnCode", "1");
					jsonObject.put("returnMsg", "网络繁忙");
				}
			}
		}
		logger.info("ActivityManagerImpl[userForward]用户转发结束>>>>>jsonObject:"+jsonObject.toString());
		return jsonObject;
	}

	@Override
	public JSONObject userAnswer(String openId, String score) {
		logger.info("ActivityManagerImpl[userAnswer]用户答题开始>>>>>openId:"+openId+">>>>>score:"+score);
		int result = 0;
		JSONObject jsonObject = new JSONObject();
		InvestorAnswerDto investorAnswerDto = null;
		if(StringUtils.isEmptyString(openId)||StringUtils.isEmptyString(score)) {
			jsonObject.put("returnCode", "1");
			jsonObject.put("returnMsg", "关键参数为空");
		}else {
			jsonObject.put("returnCode", "0");
			jsonObject.put("returnMsg", "请求成功");
			int newScore = Integer.parseInt(score);
			investorAnswerDto = activityDao.queryUserAnswerRecord(openId);
			if(investorAnswerDto == null) {
				//创建用户答题记录
				try {
					result = activityDao.createUserAnswerRecord(openId, newScore);
				} catch (Exception e) {
					logger.error("用户答题创建用户答题记录捕获异常",e);
				}
				if(result == 0) {
					jsonObject.put("returnCode", "1");
					jsonObject.put("returnMsg", "网络繁忙");
				}
			}else {
				//更新用户答题记录
				try {
					result = activityDao.updateUserAnswerRecord(openId, newScore);
				} catch (Exception e) {
					logger.error("用户答题更新用户答题记录捕获异常",e);
				}
				if(result == 0) {
					jsonObject.put("returnCode", "1");
					jsonObject.put("returnMsg", "网络繁忙");
				}
			}
		}
		logger.info("ActivityManagerImpl[userAnswer]用户答题结束>>>>>jsonObject:"+jsonObject.toString());
		return jsonObject;
	}
	
	@Override
	public JSONObject userLuckDraw(String openId) {
		logger.info("ActivityManagerImpl[userLuckDraw]用户抽奖开始>>>>>openId:"+openId);
		int awardNumber = 0;//获奖人数
		int awardChance = 0;//获奖概率
		int result = 0;
		String isWinPrize = "0";//获奖状态  0 没奖  1 获奖
		JSONObject jsonObject = new JSONObject();
		InvestorAwardDto investorAwardDto = null;
		if(!StringUtils.isEmptyString(openId)) {
		    jsonObject.put("returnCode", "0");
		    jsonObject.put("returnMsg", "请求成功");
			//查询判断用户是否已经获奖
			investorAwardDto = activityDao.queryUserAwardRecord(openId);
			if(investorAwardDto != null) {
				try {
					result = activityDao.createUserAwardRecord(openId,isWinPrize);
				} catch (Exception e) {
				    logger.error("用户抽奖创建用户抽奖记录捕获异常",e);
				}
			}else {
			   //获取投教活动的开关数据
			   awardNumber = Integer.parseInt(queryInvestorEduActParameter("SYSTEM","TJLUCKDRAW","AWARD_NUMBER"));
			   awardChance = (int) (Double.parseDouble(queryInvestorEduActParameter("SYSTEM","TJLUCKDRAW","AWARD_CHANCE")) * 100);
			   //使用随机数模拟获奖概率
			   int random = new Random().nextInt(100);
			   logger.info("获奖人数:"+awardNumber+">>>>>获奖概率:"+awardChance+">>>>>随机数:"+random);
			   synchronized (this) {
				  //判断是否在当天获奖人数之内且随机数在概率之内
				  if(activityDao.countTodayAwardRecord() < awardNumber && random < awardChance) {
					  //获奖
					  isWinPrize = "1";
				  }
				  try {
				     result = activityDao.createUserAwardRecord(openId,isWinPrize);
				  } catch (Exception e) {
				     logger.error("用户抽奖创建用户抽奖记录捕获异常",e);
				  }
			   }
			}
			if(result == 0) {
				jsonObject.put("returnCode", "1");
				jsonObject.put("returnMsg", "网络繁忙");
			}else {
				jsonObject.put("isWinPrize", isWinPrize);
			}
		}else {
		    jsonObject.put("returnCode", "1");
		    jsonObject.put("returnMsg", "openId为空");
		}
		logger.info("ActivityManagerImpl[userLuckDraw]用户抽奖结束>>>>>jsonObject:"+jsonObject.toString());
		return jsonObject;
	}
	
	@Override
	public JSONObject userGetAward(InvestorUserDto investorUserDto) {
		logger.info("ActivityManagerImpl[userGetAward]用户兑奖开始>>>>>openId:"+investorUserDto.getOpenId()+">>>>>userFullName:"+investorUserDto.getUserFullName()+">>>>>mobile:"+investorUserDto.getMobile()+">>>>>address:"+investorUserDto.getAddress());
		int result = 0;
		JSONObject jsonObject = new JSONObject();
		if(StringUtils.isEmptyString(investorUserDto.getOpenId()) || 
				StringUtils.isEmptyString(investorUserDto.getUserFullName()) ||
				StringUtils.isEmptyString(investorUserDto.getMobile()) || 
				StringUtils.isEmptyString(investorUserDto.getAddress())) {
			jsonObject.put("returnCode", "1");
			jsonObject.put("returnMsg", "关键参数为空");
		}else {
			jsonObject.put("returnCode", "0");
			jsonObject.put("returnMsg", "请求成功");
			try {
				// 记录用户填写的信息
				result = activityDao.createActivityUserRecord(investorUserDto);
			} catch (Exception e) {
				logger.error("用户兑奖创建用户信息记录捕获异常 "+e);
			}
			if(result == 0) {
				jsonObject.put("returnCode", "1");
				jsonObject.put("returnMsg", "网络繁忙");
			}else {
				result = 0;
				try {
					// 修改用户的奖品状态
					result = activityDao.updateUserAwardRecord(investorUserDto.getOpenId(),"3");
				} catch (Exception e) {
					logger.error("用户兑奖更新用户奖品记录捕获异常",e);
				}
				if(result == 0) {
					jsonObject.put("returnCode", "1");
					jsonObject.put("returnMsg", "网络繁忙");
				}
			}
		}
		logger.info("ActivityManagerImpl[userGetAward]用户兑奖结束>>>>>jsonObject:"+jsonObject.toString());
		return jsonObject;
	}

	@Override
	public JSONObject queryUserAward(String openId) {
		logger.info("ActivityManagerImpl[queryUserAward]查询用户奖品开始>>>>>openId:"+openId);
		JSONObject jsonObject = new JSONObject();
		InvestorAwardDto investorAwardDto = null;
		int endDate = 0; //领奖结束时间
		// 当天时间
		int todayDate = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(new Date()));
		if(!StringUtils.isEmptyString(openId)) {
			jsonObject.put("returnCode", "0");
			jsonObject.put("returnMsg", "请求成功");
			endDate = Integer.parseInt(queryInvestorEduActParameter("SYSTEM", "TJDATE", "AWARD_ENDDATE"));
			logger.info("当天时间:"+todayDate+">>>>>领奖结束时间:"+todayDate);
			investorAwardDto = activityDao.queryUserAwardRecord(openId);
			//	判断用户是否获奖
			if(null != investorAwardDto) {
				// 判断奖品是否已过期
				if(todayDate > endDate) {
					jsonObject.put("status", "4");
				}else {
					jsonObject.put("status", investorAwardDto.getStatus());
				}
			}else {
				jsonObject.put("status", "0");
			}
		}else {
			jsonObject.put("returnCode", "1");
			jsonObject.put("returnMsg", "openId为空");
		}
		logger.info("ActivityManagerImpl[queryUserAward]查询用户奖品结束>>>>>jsonObject:"+jsonObject.toString());
		return jsonObject;
	}
	
	@Override
	public InvectorUserInfoexDto queryUserInfoByUerId(String userId) {
		InvectorUserInfoexDto invectorUserInfoexDto=activityDao.queryUserInfoByUerId(userId);
		logger.info("ActivityManagerImpl[queryUserInfoByUerId]查询用户openId开始>>>>>:"+ (invectorUserInfoexDto != null ? 
				invectorUserInfoexDto.getOpenId() : ""));
		return invectorUserInfoexDto;
	}

	@Override
	public JSONObject checkUserIsCanLuckDraw(String openId) {
		logger.info("ActivityManagerImpl[checkUserIsCanLuckDraw]验证用户是否可以抽奖开始>>>>>openId:"+openId);
		JSONObject jsonObject = new JSONObject();
		InvestorAnswerDto investorAnswerDto = null;
		InvestorAwardDto investorAwardDto = null;
		int awardScore = 0;
		if(!StringUtils.isEmptyString(openId)) {
			jsonObject.put("returnCode", "0");
			jsonObject.put("returnMsg", "请求成功");
			//查询用户的答题记录，验证是否有80分以上
			investorAnswerDto = activityDao.queryUserAnswerRecord(openId);
			awardScore = Integer.parseInt(queryInvestorEduActParameter("SYSTEM","TJLUCKDRAW","LUCKDRAW_SCORE"));
			logger.info("用户分数:"+ (investorAnswerDto==null ? null :
					investorAnswerDto.getScore()) + ">>>>>抽奖资格分数:"+awardScore);
			if(investorAnswerDto == null || investorAnswerDto.getScore() < awardScore) {
				jsonObject.put("isLuckDraw", "0");// 不够抽奖资格
			}else {
				//查询用户当天的抽奖记录，验证是否已经抽过奖了
				investorAwardDto = activityDao.queryUserTodayLuckDrawRecord(openId);
				if(investorAwardDto == null) {
					jsonObject.put("isLuckDraw", "1");// 可以抽奖
				}else {
					jsonObject.put("isLuckDraw", "2");// 不能再抽奖
				}
			}
		}else {
			jsonObject.put("returnCode", "1");
			jsonObject.put("returnMsg", "openId为空");
		}
		logger.info("ActivityManagerImpl[checkUserIsCanLuckDraw]验证用户是否可以抽奖结束>>>>>jsonObject:"+jsonObject.toString());
		return jsonObject;
	}

	@Override
	public String queryInvestorEduActParameter(String pmst, String pmky, String pmnm) {
		List<ParameterDto> parameter = queryServiceClient.queryParameter(pmst, pmky, null, null);
		for (ParameterDto dto : parameter) {
			if(dto.getPmnm().equals(pmnm)) {
				return dto.getPmco();
			}
		}
		return "";
	}
	
	@Override
	public JSONObject queryBankLogoList() {
		JSONObject json = new JSONObject();
		try {
			List<BankLogoDto> list = bankLogoDao.queryBankLogoList();
			for (BankLogoDto dto : list) {
				String bankLogoCode = dto.getBankLogo() != null ? new String(dto.getBankLogo()) : null;
				dto.setBankLogoCode(bankLogoCode);
			}
			json.put("data", list);
			json.put("resultCode", "0000");
			json.put("resultMsg", "成功");
		} catch (Exception e) {
			logger.error("查询银行徽标异常", e);
			json.put("resultCode", "9999");
			json.put("resultMsg", "系统异常");
		}
		return json;
	}
	
	public MsgSmsRecordDto getMsgSmsRecord(String mobile, String msg) {
		MsgSmsRecordDto msgRecord = new MsgSmsRecordDto();
		msgRecord.setMobile(mobile);
		msgRecord.setContent(msg);
		msgRecord.setMethod("A");
		msgRecord.setSendUser("system");
		return msgRecord;
	}
}
