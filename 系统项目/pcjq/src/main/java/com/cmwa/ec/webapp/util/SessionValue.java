package com.cmwa.ec.webapp.util;

public class SessionValue {
	// 用户Session关键字
	public static String SESSION_MNU = "com.thinkive.cms.system.admin.session_mnu";
	// 用户基本信息
	public static final String SESSION_USERBASEINFO = "userBaseInfo";
	// 用户保存在session中的cmfuserid
	public static final String SESSION_CMFUSERID = "cmfUserId";
	// 用户账户对应表dto
	public static final String SESSION_USERACCORLA = "userAccoRla";
	// 时间戳，主要用于图片验证码
	public static final String SESSION_TIMESTAMP = "timeStamp";
	// 验证图片验证码时 成功后保存的手机号码
	public static final String SESSION_MSGMOBILE = "msgMobile";
	// 获取手机短信验证码时保存在session中的手机号码 
	public static final String SESSION_USERMOBILE = "userMobile";
	// 验证手机短信验证码通过时保存在session中的手机号码
	public static final String  SESSION_VERIFYMOBILE = "verifyMobile";
	// 鉴权，获取手机短信验证码时保存在session中的手机号码 
	public static final String  SESSION_AUTHMOBILE = "authMobile";
	// 鉴权，手机短信验证码验证通过  将手机号码保存在session中的key 
	public static final String  SESSION_AUTHVERIFYMOBILE = "authVerifyMobile";
	
	public static final String SESSION_REQUESTURL = "requestUrl";
	// 保存总页数    现在只用于保存查询信批(我的消息)的总页数
	public static final String SESSION_TOTALAMOUNT = "totalAmount";
	// 是否验证支付密码：Y 已验证
	public static final String SESSION_CHECKEDTPASSWORD = "checkedTpassword";
	// 是否需要设置登录密码：Y需要;N不需要（用于修改用户注册手机号码页面）
	public static final String SESSION_HASCHECKSETLPASSWORD = "hasCheckSetLPassword";
	// 是否能设置支付密码：Y能；N不能(用于修改手机号码页面，新手机号码为注册账号时需要设置手机号码)
	public static final String SESSION_CANSETLPASSWORD = "canSetLPassword";
	// 保存产品费率，和SESSION_SESSIONFUNDID一起使用
	public static final String SESSION_QUERYFEERATEMAP = "sessionQueryFeeRateMap";
	// 保存产品id，和SESSION_QUERYFEERATEMAP一起使用
	public static final String SESSION_SESSIONFUNDID = "sessionfundId";
	//找回登录密码时 图形码验证 标识
	public static final String SESSION_RESETLPWVERFLAG = "resetLPWVerFlag";
	//找回登录密码时 最近一次短信发送时间
	public static final String SESSION_RESETLPWMSGTIME = "resetLPWMsgTime";
}
