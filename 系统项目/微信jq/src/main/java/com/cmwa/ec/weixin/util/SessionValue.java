package com.cmwa.ec.weixin.util;


public class SessionValue {
	
	// 区分微信和mecc MECC为从mecc跳转过来，空值或者WEIXIN则是微信这边      WEIXIN/MECC
	public static final String  SESSION_REQCHANNEL = "reqChannel";
	// 区分微信IE和其他IE，  WXIE/OTHERIE
	public static final String  SESSION_CHANNEL = "channel";
	// 0:未设置或修改初始的支付密码/1:已设置或修改初始的支付密码 
	public static final String  SESSION_ISSETTRADEPASSWORD = "isSetTradePassword";
	// 用户基本信息 
	public static final String  SESSION_USERBASEINFO = "userBaseInfo";
	// 用户账户对应表dto 
	public static final String  SESSION_USERACCORLA = "userAccoRla";
	// cmfUserId 
	public static final String  SESSION_CMFUSERID = "cmfUserId";
	// 分享相关  保存在session中的值 
	public static final String  SESSION_SECID = "secId";
	// 图片验证码  保存在session中的验证码值 
	public static final String  SESSION_MNU = "com.cmwa.ec.weixin.session_mnu";
	// 登录页面前的请求页面地址 
	public static final String  SESSION_REQUESTPATH = "requestPath";
	// 图片验证码验证成功保存值 时间戳+300 
	public static final String  SESSION_TIMESTAMP = "timeStamp";
	// 图片验证码验证成功保存值 图片验证码页面 regRandom：注册页面图片验证码;resetRandom:重置登录密码页面图片验证码 
	public static final String  SESSION_RANDOMCHANNEL = "randomChannel";
	// 获取手机短信验证码时保存在session中的手机号码 
	public static final String  SESSION_USERMOBILE = "userMobile";
	// 手机短信验证码验证通过  将手机号码保存在session中的key 
	public static final String  SESSION_MSGMOBILE = "msgMobile";
	// 是否验证支付密码    1：已验证， 
	public static final String  SESSION_HASCHECKEDTPSW = "hasCheckedTpsw";
	// 鉴权，获取手机短信验证码时保存在session中的手机号码 
	public static final String  SESSION_AUTHMOBILE = "authMobile";
	// 鉴权，手机短信验证码验证通过  将手机号码保存在session中的key 
	public static final String  SESSION_AUTHVERIFYMOBILE = "authVerifyMobile";
	// 交易，获取手机短信验证码时保存在session中的手机号码 
	public static final String  SESSION_TRADEMOBILE = "tradeMobile";
	// 交易，手机短信验证码验证通过  将手机号码保存在session中的key 
	public static final String  SESSION_TRADEVERIFYMOBILE = "tradeVerifyMobile";

	public static final String SESSION_OPENID = "openId";
	public static final String SESSION_USERID = "userid";
	
	// 保存总页数    现在只用于保存查询信批(我的消息)的总页数
	public static final String SESSION_TOTALAMOUNT = "totalAmount";
	
	// 是否需要设置登录密码，用于修改手机号码的时候用到 Y-需要设置支付密码；N-不需要设置支付密码
	public static final String SESSION_HASSETLPSW = "hasSetLPsw";
	
	// 保存产品费率，和SESSION_SESSIONFUNDID一起使用
	public static final String SESSION_QUERYFEERATEMAP = "sessionQueryFeeRateMap";
	// 保存产品id，和SESSION_QUERYFEERATEMAP一起使用
	public static final String SESSION_SESSIONFUNDID = "sessionfundId";
	
	public static final String SESSION_BANKAUTHSTATUS = "bankAuthResult";
	//活动id
	public static final String SESSION_ACTIVITYID = "activityId";
	
	//找回登录密码时 图形码验证 标识
	public static final String SESSION_RESETLPWVERFLAG = "resetLPWVerFlag";
	//找回登录密码时 最近一次短信发送时间
	public static final String SESSION_RESETLPWMSGTIME = "resetLPWMsgTime";
	// 华安保险引流用户信息
	public static final String SESSION_SINO_USERINFO = "sinoUserInfo";
	// 华安保险引流用户id
	public static final String SESSION_SINO_USERID = "userId";
}
