package com.cmwa.ec.weixin.constants;

import net.sf.json.JSONObject;

/**
 * 常量类
 * @author caolp
 *
 */
public class WXConstants {
	
	//用户Session关键字
    public static final String SESSION_MNU="com.cmwa.ec.weixin.session_mnu";
	/** 请求消息类型 文本 */
    public static final String MSGTYPE_REQUEST_TEXT = "text";
    
    /** 请求消息类型 图片 */
    public static final String MSGTYPE_REQUEST_IMAGE = "image";
    
    /** 请求消息类型 语音 */
    public static final String MSGTYPE_REQUEST_VOICE = "voice";
    
    /** 请求消息类型 视频*/
    public static final String MSGTYPE_REQUEST_VIDEO = "video";
    
    /** 请求消息类型 视频*/
    public static final String MSGTYPE_REQUEST_SHORTVIDEO = "shortvideo";
    
    /** 请求消息类型 地理位置 */
    public static final String MSGTYPE_REQUEST_LOCATION = "location";
    
    /** 请求消息类型 链接 */
    public static final String MSGTYPE_REQUEST_LINK = "link";
    
    /** 请求消息类型 事件 */
    public static final String MSGTYPE_REQUEST_EVENT = "event";
    
    
    /** 事件添加关注 */
    public static final String EVENT_SUBCRIBE= "subscribe";
    /** 已关注用户扫码关注 */
    public static final String EVENT_SCAN= "scan";
    /** 事件取消关注  */
    public static final String EVENT_UNSUBCRIBE= "unsubscribe";    
    /** 上报地理位置事件 */
    public static final String EVENT_LOCATION= "location";
    /** 点击自定义菜单 click类型 事件 */
    public static final String EVENT_CLICK= "click";
    /** 点击自定义菜单，跳转链接事件 */
    public static final String EVENT_VIEW= "view";
    
    
    //内容分隔符
    public static final String EVENT_TEXT_SPLIT = "#";
    
    //add by fangdb
    public static final String PMST_TEXT = "TEXT";
    public static final String PMST_MENU = "MENU";
    public static final String PMST_CONFIG = "CONFIG";
    public static final String PMST_MESSAGE = "MESSAGE";
    
    public static final String PMKY_MUSICANSWER = "MUSICANSWER";
    public static final String PMKY_VOICEANSWER = "VOICEANSWER";
    public static final String PMKY_VIDEOANSWER = "VIDEOANSWER";
    public static final String FATHER_DAY = "FATHER_DAY";//父亲节活动
    public static final String MAGPIEFESTIVAL = "MAGPIEFESTIVAL";//七夕节活动
    
    
    public static final String PMKY_TEXTANSWER = "TEXTANSWER";
    public static final String PMKY_IMAGEANSWER = "IMAGEANSWER";
    public static final String PMKY_LOACTIONANSWER = "LOCATIONANSWER";
    public static final String PMKY_VIEWANSWER = "VIEWANSWER";
    public static final String PMKY_EVENT = "EVENT";
    public static final String PMKY_CUST_SERVICE = "CUST_SERVICE";
    public static final String PMKY_PRDTEMPLATE = "PRDTEMPLATE";
    public static final String PMKY_TRADEBANK = "TRADEBANK";
    public static final String PMKY_TRADEOPER = "TRADEOPER";
    public static final String PMKY_PUBLIC = "PUBLIC";
    public static final String PMKY_WHITELIST = "WHITELIST";
    public static final String PMKY_URL = "URL";
    public static final String PMKY_BIND_ACC = "BIND_ACC"; //账号查询绑定
    public static final String PMKY_OPEN_TRADE = "OPEN_TRADE"; //账号查询绑定
    /**20180315 add start by ex-wulj*/
    public static final String PMKY_SERVICE_PUSH = "SERVICE_PUSH";
    /**20180315 add end by ex-wulj*/
    
    public static final String PMCO_BIND_TEMPLATEMESSAGE = "BIND_TEMPLATEMESSAGE";//绑定后推送的模板消息
    public static final String PMCO_BIND_SERVICEMESSAGE = "BIND_SERVICEMESSAGE";//绑定后推送的模板消息
    public static final String PMCO_BIND_SERVICEMESSAGE_WK = "BIND_SERVICEMESSAGE_WK";//绑定后推送的模板消息
    public static final String PMCO_SERVICWELCOME = "SERVICEWELCOME";//关注后推送的图文消息
    public static final String PMCO_SERVICWELCOME_WK = "WELCOME_WK";//关注后推送的图文消息
    public static final String PMCO_SERVICWELCOME_WK_NO = "WELCOME_WK_NO";//关注后推送的图文消息
    public static final String FATHER_MOVIE_TICKET = "FATHER_MOVIE_TICKET";//父亲节关注后推送消息
    public static final String MAGPIEFESTIVAL_TICKET = "MAGPIEFESTIVAL_TICKET";//七夕节关注后推送消息
    public static final String FATHER_MOVIE_TICKET_SUCCESS = "FATHER_MOVIE_TICKET_SUCCESS";//父亲节电影券成功后消息
    public static final String MAGPIEFESTIVAL_SUCCESS = "MAGPIEFESTIVAL_SUCCESS";//七夕节电影券成功后消息
    
    public static final String PMCO_BIND_TEMPLATEMESSAGEURL = "BIND_TEMPLATEMESSAGEURL";//绑定后推送消息里的详情链接
    public static final String PMCO_PURCHURSE = "PURCHURSE"; //存
    public static final String PMCO_REDEEM = "REDEEM"; //取
        
    //url
    public static final String URL_REGISTER = "<registerUrl>"; 
    public static final String URL_FUNDCENTER = "<fundCenterUrl>"; 
    public static final String URL_BINDACC = "<bindAccUrl>"; 
    public static final String URL_UNBINDACC = "<unBindAccUrl>"; 
    public static final String URL_OPENTRADE = "<openTradeUrl>";
    public static final String URL_CLOSETRADE = "<closeTradeUrl>"; 
    public static final String URL_PURCHURSE = "<purchurseUrl>"; 
    public static final String URL_REDEEM = "<redeemUrl>";
    public static final String URL_PURCHURSENOTICE = "<purchurseNoticeUrl>"; 
    public static final String URL_REDEEMNOTICE = "<redeemNoticeUrl>"; 
    public static final String URL_CREATEMENU = "<createMenuUrl>";
    public static final String URL_DELETEMENU = "<deleteMenuUrl>";
    public static final String URL_QUERYMENU = "<queryMenuUrl>";
    public static final String URL_ACCESSTOKEN = "<accessTokenUrl>";
    public static final String URL_NET_EASE_INDEX="<NetEaseIndexUrl>";
    public static final String URL_NET_EASE_DISPATHER="<NetEaseDispatherUrl>";
    public static final String URL_SET_DEFAULT_TRADE_ACCO = "<setDefaultTradeAccoUrl>";
    public static final String URL_DEFAULT_TRADE_ACCO = "<bankNoInfo>";
    
    
    public static final String URL_GUESS_ANNUAL_CONTROL_URL="<GuessAnnualEntryUrl>";
    public static final String URL_GUESS_ANNUAL_302_RECEIVER="Wx302ReceiverUrl";

    public static final String MESSAGE_PMKY_BIND_ACC = "BIND_ACC"; //账号查询绑定
    public static final String MESSAGE_PMCO_NOBIND = "NOBIND"; //没有绑定账号查询
    public static final String MESSAGE_PMCO_BINDED = "BINDED"; //绑定账号查询
    
    public static final String MESSAGE_PMCO_OPENED = "OPENED"; //开通交易
    public static final String MESSAGE_PMCO_NOOPEN = "NOOPEN"; //没有开通交易
    
    public static final String MESSAGE_PMCO_NOBANKCARD = "NOBANKCARD"; //没有可用交易账号
    
    public static final String MESSAGE_PMCO_NORESULT = "NORESULT"; //没有结果
    
    
     //  用户扩展信息表，绑定状态
    public static final String USERINFOEX_BINDSTAT_T = "T"; //绑定交易
    public static final String USERINFOEX_BINDSTAT_Q = "Q"; //绑定查询
    /***
     * R 表示已绑定
     */
    public static  String USERINFOEX_BINDSTAT_R = "R"; //已绑定
    
	public static final String AUTHOR2_URL_KEY="AUTHOR2";
	public static final String AUTHOR2_URL_OPENID="AUTHOR2_OPENID";
	
	
	
	
	/**
	 * 常用常量字符串
	 */
	
	
	public static final String COMMON_SUCCESS = "0000";
	
	public static final String COMMON_SUCCESS_CODE = "0";
	public static final String COMMON_SUCCESS_MSG = "success";
	
	public static final String COMMON_ERROR_LOGINTIMEOUTCODE = "8000";
	public static final String COMMON_ERROR_LOGINTIMEOUTMSG = "您太久没有操作了，需要重新登录哦";
	
	public static final String COMMON_ERROR_BINDCODE = "8001";
	public static final String COMMON_ERROR_BINDMSG = "您还没有绑定微信哦,请先去注册";
	
	
	public static final String COMMON_ERROR_PARAMISNULLCODE = "9000";
	public static final String COMMON_ERROR_PARAMISNULLMSG = "关键参数为空";
	
	public static final String COMMON_ERROR_REDATAISNULLCODE = "9001";
	public static final String COMMON_ERROR_REDATAISNULLMSG = "返回数据为空";
	
	public static final String COMMON_ERROR_ERRORMOBILECODE = "9002";
	public static final String COMMON_ERROR_ERRORMOBILEMSG = "当前手机号码不是获取验证码的手机号码";
	
	public static final String COMMON_ERROR_ILLEGALREQCODE = "9003";
	public static final String COMMON_ERROR_ILLEGALREQMSG = "非法请求";
	
	public static final String COMMON_ERROR_NOTAUTHCODE = "9004";
	public static final String COMMON_ERROR_NOTAUTHMSG = "用户未鉴权";
	
	/**/
	public static final String RETURN_CODE_9007 = "9007";
	public static final String RETURN_MSG_9007 = "操作太频繁，请稍后再试";
	/**/
	public static final String RETURN_CODE_9008 = "9008";
	public static final String RETURN_MSG_9008 = "无效参数或参数格式不正确";
	
	public static final String COMMON_ERROR_SYSERRCODE = "9999";
	public static final String COMMON_ERROR_SYSERRMSG = "系统繁忙，稍后再试";
	
	/**
     * 密码错误返回码
     */
    public static final String USER_CODE_A009="USR-A009";
    public static final String USER_MSG_A009="用户名或密码不正确";
    
    /**
     * 是否设置支付密码  0:未设置支付密码   1：已设置
     */
    public static final String COMMON_NO_SETTPASS = "0";
    public static final String COMMON_SETTPASS = "1";
    
    /***
     * 发送短信通知的业务类型     
     * 0、注册 1、交易 2、绑定 3、账户手机验证 4、修改密码 
     * 5、银行卡号，身份证，手机号，姓名 鉴权（开户） 
     * 6、修改绑定手机号码短信验证
     * 11、线下汇款通知信息	12、修改手机号码通知信息
     */
    public static String MOBILE_TYPE_0="0";
    public static String MOBILE_TYPE_1="1";
    public static String MOBILE_TYPE_3="3";
    public static String MOBILE_TYPE_4="4";
    public static String MOBILE_TYPE_5="5";
    public static String MOBILE_TYPE_6="6";
    public static String MOBILE_TYPE_11="11";
    public static String MOBILE_TYPE_12="12";
    
    
    /***
     * 交易标识： 标示：01-WEB，02-APP，03-WEIXIN
     */
    public static String TRADE_CHANEL_01="01";
    public static String TRADE_CHANEL_02="02";
    public static String TRADE_CHANEL_03="03";
    
    /***
     * 登陆标识	标示：01-WEB，02-APP，03-WEIXIN，04-WEIXIN-WEB,05-华安保险
     */
    public static String LOGIN_CHANEL_03="03";
    public static String LOGIN_CHANEL_04="04";
    public static String LOGIN_NMARK_03="03";
    public static String LOGIN_NMARK_04="04";
    public static String LOGIN_NMARK_05="05";
    public static String LOGIN_CHANEL_90="90";
    
    /***
     * 接口通道 微信调用各类服务
     */
    public static String SERVICE_CHANNEL_WEIXIN="WEIXIN";
	  
    /**
     * 微信浏览器请求
     */
    public static final String WEIXIN_CHANNEL = "WXIE";
    /**
     * 其他浏览器请求
     */
    public static final String OTHER_CHANNEL = "OTHERIE";
    
    
    
    
    /***
     * 登录类型 X 手机号码
     */
    public static String LOGIN_TYPE_X="X";
    
    /***
     * 登录类型 0 身份证号
     */
    public static String LOGIN_TYPE_0="0";
    
    
    /***
     * 001 账户服务接口
     */
    public static String USER_SERVICE_001="001";
    /**
     * 601 交易服务接口
     */
    public static String TRADE_SERVICE_601 = "601";
    /***
     * 701 查询服务接口
     */
    public static String QUERY_SERVICE_701="701";
    /***
     * 801 消息服务接口
     */
    public static String MESSAGE_SERVICE_801="801";
    
    
    /**
     * 修改用户分组
     */
    public static final String PMCO_GROUP_MODIRYUSERGROUP = "MODIRYUSERGROUP";
    
    public static final String PMKY_GROUP = "GROUP";
    //已注册用户组
    public static final String REGISTERGROUPID = "100";
    //已鉴权用户组
    public static final String AUTHORITYGROUPID = "101";
    //重置交易密码：959
    public static final String APKIND_RESETTPASS = "959";
    
    // 查询信批   分页使用  每页展示条数
    public static final int MESSAGE_AMOUNT = 10;
    //默认处理器
    public static final String DEFAULT_EVENT_MANAGER = "000000";
    //万科活动处理器
    public static final String WK_EVENT_MANAGER = "000010";
    //父亲节处理器
    public static final String FATHER_DAY_MANAGER = "H0002";
    //七夕节处理器
    public static final String MAGPIEFESTIVAL_EVENT_MANAGER = "H0003";
    /**
     * 查询产品净值    每页展示的信息条数    从A到B中的B
     */
    public static final String PAGE_LIST_NETVAL="5";
    
    //登录手机号与修改手机号不匹配
  	public static final String MODIFY_LOGIN_PWD_MOBILE_NOT_MATCH = "9101";
  	
  	//未验证手机短信验证码或者验证的手机短信验证码的手机号码和传递过来的手机号码不一致
  	public static final String SMS_VERIFY_CODE_FAIL = "9102";
  	
  	//参数校验不通过的返回码
  	public static final String PARAMETER_CHECK_NOT_PASS = "9103";
  	
    /**
     * 对应的产品参数--产品系列
     * 风险揭示函标识
     */
    public static final String PMST = "SYSTEM";
    public static final String RISK_TERMS = "RISK_TERMS";
  	
	/**
	 * 基金产品类型标识
	 * 0100固定收益  0110固定收益(净值) 0210 开放性净值类产品 0220封闭净值 0300浮动收益 0400 7天14天管家产品 0500现金管理产品
	 */
	public static final String FUND_TYPE_0100 = "0100";
	public static final String FUND_TYPE_0110 = "0110";
	public static final String FUND_TYPE_0210 = "0210";
	public static final String FUND_TYPE_0220 = "0220";
	public static final String FUND_TYPE_0300 = "0300";
	public static final String FUND_TYPE_0400 = "0400";
	public static final String FUND_TYPE_0500 = "0500";

  	/**
  	 * 华安保险引流用户注册渠道标识
  	 */
  	public static final String DRAINAGE_REGISTERCHANNEL_HABX = "HABX";

  	/**
  	 * 保行天下引流用户注册渠道标识
  	 */
  	public static final String DRAINAGE_REGISTERCHANNEL_BXTX = "BXTX";

    /**
     * 错误码
     */
    public static final String ERR_CODE = "errcode";

    /**
     * 错误信息
     */
    public static final String ERR_MSG = "errmsg";

    /**
     * 通用错误返回
     */
    public static final JSONObject ERR_JSON_RESULT = new JSONObject()
        .accumulate(WXConstants.ERR_CODE, WXConstants.COMMON_ERROR_SYSERRCODE)
        .accumulate(WXConstants.ERR_MSG, WXConstants.COMMON_ERROR_SYSERRMSG);
    
    /**
     * 通用参数为空返回
     */
    public static final JSONObject PARAMISNULL_JSON_RESULT = new JSONObject()
            .accumulate(WXConstants.ERR_CODE, WXConstants.COMMON_ERROR_PARAMISNULLCODE)
            .accumulate(WXConstants.ERR_MSG, WXConstants.COMMON_ERROR_PARAMISNULLMSG);

  	
  	 /**
     * 合格投资者状态 审核失败
     */
	public static final String QULIFIEDUSER_STATUS_F = "F";
    /**
     * 合格投资者状态 未审核
     */
    public static final String QULIFIEDUSER_STATUS_N = "N";
    /**
     * 合格投资者状态 通过
     */
    public static final String QULIFIEDUSER_STATUS_S = "S";
    /**
     * 合格投资者状态 未确认
     */
    public static final String QULIFIEDUSER_STATUS_U = "U";
    
    /**
     * 合格投资者状态 重新提交
     */
    public static final String QULIFIEDUSER_STATUS_R = "R";
    
    /**
     * 合格投资者上传文件类型 身份证肖像面
     */
    public static final String QUALIFIED_FILETYPE_IDCARDPORTRAIT = "idCardPortrait";
    
    /**
     * 合格投资者上传文件类型 身份证国徽面
     */
    public static final String QUALIFIED_FILETYPE_IDCARDNATIONALEMBLEM = "idCardNationalEmblem";
    
    /**
     * 职业代码 其他职业
     */
    public static final String VOCCODE_15 = "15";
}
