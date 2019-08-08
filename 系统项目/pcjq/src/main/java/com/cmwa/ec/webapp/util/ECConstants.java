package com.cmwa.ec.webapp.util;


import net.sf.json.JSONObject;

public class ECConstants {
	 
    
    
    
    
    
    
    public static final String COMMON_ERROR_SYSERRMSG = "系统繁忙，稍后再试";
    public static final String COMMON_SUCCESS = "0000";
    public static final String COMMON_ERROR_SYSERRCODE = "9999";
    
    /**
     * 查询用户公告    每页展示的信息条数    从A到B中的B
     */
    public static final String PAGE_LIST_AMOUNT="11";
    
    /**
     * 查询产品净值    每页展示的信息条数    从A到B中的B
     */
    public static final String PAGE_LIST_NETVAL="5";
    /**
     * 查询用户公告    最新公告展示的条数
     */
    public static final String PAGE_NEWS_AMOUNT="7";
    /**
     * 根据产品id查询该产品的相关公告    产品公告展示的条数
     */
    public static final String PAGE_USER_PRODUCT_AMOUNT="7";
    
    /***
     * 自定义错误码返回
     */
    public static String  MSG_ERROR_CODE="0001";
    
    /***
     * 手机号码已使用返回码
     */
    public static String CODE_MOBILE_USE="USR-A017";
    /***
     * 手机号码已验证
     */
    public static String MOBILE_SUCCEED="1";
    
    /***
     *手机号码已使用返回信息
     */
    public static String MSG_MOBILE_USE="手机号码已被使用";
    
    /***
     * 验证手机号码的业务类型 0表示 注册
     * 
     */
    public static String MOBILE_TYPE_0="0";
    
    /***
     * 验证手机号码的业务类型 4表示 修改密码
     * 
     */
    public static String MOBILE_TYPE_4="4";
    public static String MOBILE_TYPE_1="1";
    /**
     * 线下汇款通知
     */
    public static String MOBILE_TYPE_11="11";
    /***
     * 验证手机号码的业务类型 5表示 银行卡号，身份证，手机号，姓名 鉴权（开户））
     * 
     */
    public static String MOBILE_TYPE_5="5";
    
     /***
      * 返回手机验证自定义错误信息
      */
    public static String MSG_MOBILE_ERROR="验证手机号码错误！";
    
    /**
     * 交易标识
     */
    public static String  TRADE_NMARK_01="01";
    
    /***
     * 登录标识	标示：01-WEB，02-APP，03-WEIXIN
     */
    public static String  LOGIN_NMARK_02="02";
    
    public static String  LOGIN_NMARK_01="01";
    
    /***
     * 通过三要素找到两个以上的未注册代销用户,  通过三要素找到两个以上的已注册代销或已注册直销用户
     */
    public static String   REGISTER_USER_CODE_A016="USR-A016";
    
    /***
     * 登录类型 X
     */
    public static String LOGIN_TYPE_X="X";
    /***
     * 登录类型 0
     */
    public static String LOGIN_TYPE_0="0";
    
    /**
     * 密码错误返回码
     */
    public static String USER_CODE_A009="USR-A009";
    
    /***
     * 001 账户服务接口
     */
    public static String USER_SERVICE_001="001";
    
    /**
     * 601 交易服务接口
     */
    public static String TRADE_SERVICE_601="601";
    
    /***
     * 701 查询服务接口
     */
    public static String QUERY_SERVICE_701="701";
    
    /***
     * 801 消息服务接口
     */
    public static String MESSAGE_SERVICE_801="801";
    
     /***
      * 接口通道 调用各类服务
      */
    /*public static String SERVICE_CHANNEL_APP="APP";*/
    public static String SERVICE_CHANNEL_APP="ECAPP";
    
    /***
     * 查询活动的栏目ID 
     */
   public static String QUERY_CATALOG_ID="2918";
   
   /**
    * 3个字段找到对应的产品参数--产品系列
    */
   public static final String PMST = "SYSTEM";
   public static final String PMKY = "PRODUCT_DEP";
   public static final String PMCO = "HOT_DEP";
   public static final String RISK_TERMS = "RISK_TERMS";//风险揭示函标识
   /**
    * 用户登录图片验证码标识
    */
   public static final String LOGIN_RETURNCODE="USR-A099";
   public static final String LOGIN_RETURNMSG="图片验证码过期，请重新验证。";
   // 获取手机短信验证码时保存在session中的手机号码 
	public static final String  SESSION_USERMOBILE = "userMobile";
	
	public static final String  SESSION_USERTYPE = "abs";
	/**
	 * 常用常量字符串
	 */
	
	public static final String RETURN_CODE_8000 = "8000";
	public static final String RETURN_MSG_8000 = "您太久没有操作了，需要重新登录哦";
	
	public static final String RETURN_CODE_9001 = "9001";
	public static final String RETURN_MSG_9001 = "返回数据为空";
	
	public static final String RETURN_CODE_9003 = "9003";
	public static final String RETURN_MSG_9003 = "非法请求";
    
    public static final String RETURN_CODE_0000 = "0000";
    public static final String RETURN_MSG_0000 = "成功";
    
    public static final String RETURN_CODE_9999 = "9999";
    public static final String RETURN_MSG_9999 = "网络繁忙";
    /*手机号码未注册*/
    public static final String RETURN_CODE_9300 = "9300";
    public static final String RETURN_MSG_9300 = "手机号码未注册";
    /*验证码错误*/
    public static final String RETURN_CODE_9301 = "9301";
    /*参数缺失*/
    public static final String RETURN_CODE_9000 = "9000";
    public static final String RETURN_MSG_9000 = "关键参数缺失";
	/* 手机号码已使用返回码 */
	public static final String RETURN_CODE_USRA017 = "USR-A017";
	/* 手机号码已使用返回信息 */
	public static final String RETURN_MSG_USRA017 = "手机号码已被使用";    
	/* 注册用户 */
	public static final String RETURN_CODE_USRA000 = "USR-A000";
	/* 注册用户 */
	public static final String RETURN_MSG_USRA000 = "注册用户";   
	/* 支付密码格式不正确 返回码 */
	public static final String RETURN_CODE_USRA018 = "USR-A018";
	/* 支付密码格式不正确 返回信息 */
	public static final String RETURN_MSG_USRA018 = "支付密码格式不正确";    
	/* 登录密码格式不正确 返回码 */
	public static final String RETURN_CODE_USRA019 = "USR-A019";
	/* 登录密码格式不正确 返回信息 */
	public static final String RETURN_MSG_USRA019 = "登录密码格式不正确";    
	
	/* 当前手机号码不是获取验证码的手机号码 */
	public static final String RETURN_CODE_9002 = "9002";
	public static final String RETURN_MSG_9002 = "当前手机号码不是获取验证码的手机号码";
	/**/
	public static final String RETURN_CODE_9004 = "9004";
	public static final String RETURN_MSG_9004 = "用户未鉴权";
	/**/
	public static final String RETURN_CODE_9005 = "9005";
	public static final String RETURN_MSG_9005 = "用户暂未实名认证";
	public static final String RETURN_CODE_9006 = "9006";
	public static final String RETURN_MSG_9006 = "暂无数据";
	/**/
	public static final String RETURN_CODE_9007 = "9007";
	public static final String RETURN_MSG_9007 = "操作太频繁，请稍后再试";
	/**/
	public static final String RETURN_CODE_9008 = "9008";
	public static final String RETURN_MSG_9008 = "无效参数或参数格式不正确";
	
	  // 查询信批   分页使用  每页展示条数
    public static final int MESSAGE_AMOUNT = 10;
    // 招聘信息查询 每页展示条数
    public static final int RECRUITMENT_AMOUNT = 4;
    // 校园宣讲查询  每页展示条数
    public static final int CAMPUSTALK_AMOUNT = 4;
    // 问答中心  每页展示数据
    public static final int FAQ_AMOUNT = 10;
	/***
	 * 交易标识： 标示：01-WEB，02-APP，03-WEIXIN
	 */
	public static String TRADE_CHANEL_01 = "01";
	public static String TRADE_CHANEL_02 = "02";
	public static String TRADE_CHANEL_03 = "03";

	public static String TRADE_MARK_01 = "01";
	/***
	 * 登录标识 标示：01-WEB，02-APP，03-WEIXIN，04-WEIXIN-WEB
	 */
	public static String LOGIN_CHANEL_03 = "03";
	public static String LOGIN_CHANEL_04 = "04";
	public static String LOGIN_NMARK_03 = "03";
	public static String LOGIN_NMARK_04 = "04";
	public static String LOGIN_CHANEL_01 = "01";
	public static String USER_CHANEL_01 = "01";
	public static String LOGIN_CHANEL_90 = "90";
	
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
     * 通用错误码键值
     */
    public static final String ERR_CODE = "errcode";

    /**
     * 通用错误信息建值
     */
    public static final String ERR_MSG = "errmsg";

    /**
     * 通用错误返回
     */
    public static final JSONObject ERR_JSON_RESULT = new JSONObject()
        .accumulate(ECConstants.ERR_CODE, ECConstants.RETURN_CODE_9999)
        .accumulate(ECConstants.ERR_MSG, ECConstants.RETURN_MSG_9999);
    
    /**
	 * 职业代码  其他职业
	 */
	public static final String VOCCODE_15 = "15";
	
	
	
	/**
	 * 业务代码 认购
	 */
	public static final String APKIND_020 = "020";
	/**
	 * 业务代码 申购
	 */
	public static final String APKIND_022 = "022";
	/**
	 * 业务代码 赎回
	 */
	public static final String APKIND_024 = "024";
	/**
	 * 业务代码 预约赎回
	 */
	public static final String APKIND_025 = "025";
}
