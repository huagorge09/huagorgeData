package com.cmwa.ecc.business.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 常量
 * @author Lenovo
 *
 */
public class Constant {
	
	/**
	 * 配置投资时，手机是否必须
	 */
	public static final boolean PHONE_REQUIRED = false;
	/**
	 * 配置投资时，邮件是否必须
	 */
	public static final boolean EMAIL_REQUIRED = false;
	
	/**
	 * 邮件验证超时分钟数
	 */
	public static final int EMAIL_AUTH_TIME_OUT = 30;
	
	/**
	 * 成功状态
	 */
	public static final int SUCCESS = 1;
	/**
	 * 失败状态
	 */
	public static final int FAILURE = 0;
	/**
	 * 有效状态
	 */
	public static final int VALID = 1;
	/**
	 * 无效状态
	 */
	public static final int INVALID = 0;
	
	/**
	 * 是
	 */
	public static final int YES = 1;
	/**
	 * 否
	 */
	public static final int NO = 0;
	
	/**
	 * 消息-用户名或密码错误
	 */
	public static final String MESSAGE_NAME_OR_PASSWORD_ERROR = "用户名或密码错误";
	
	/**
	 * 常量-fail
	 */
	public static final String FAIL = "fail";
	
	/**
	 * 消息-新增成功
	 */
	public static final String MESSAGE_ADD_SUCCESS = "新增成功";
	
	/**
	 * 消息-修改成功
	 */
	public static final String MESSAGE_MODIFY_SUCCESS = "修改成功";
	
	/**
	 *  消息-删除成功
	 */
	public static final String MESSAGE_DELETE_SUCCESS = "删除成功";
	
	/**
	 * 服务器错误
	 */
	public static final String MESSAGE_SERVER_ERROR = "服务器繁忙";
	
	/**
	 * 消息-用户名已经存在
	 */
	public static final String MESSAGE_USER_NAME_ISEXISTS = "用户名已经存在";
	
	/**
	 * 消息-标题已经存在
	 */
	public static final String MESSAGE_TITLE_EXISTS = "标题已经存在";
	
	/**
	 * 消息-邮件标题-邮件验证
	 */
	public static final String MESSAGE_MAIL_TITLE_VERIFY = "后河车贷邮件验证";
	
	/**
	 * 消息-邮件标题-合同
	 */
	public static final String MESSAGE_MAIL_TITLE_CONTRACT = "后河车贷合同";
	
	/**
	 * 消息-邮件内容-附件是合同详细
	 */
	public static final String MESSAGE_MAIL_CONTENT_CONTRACT = "附件是合同详细";
	
	/**
	 * 消息-OK
	 */
	public static final String MESSAGE_OK = "ok";
	
	/**
	 * 消息-parameter error
	 */
	public static final String MESSAGE_PARAMETER_ERROR = "parameter error";
	
	/**
	 * 整型0
	 */
	public static final int INT_ZERO = 0;
	
	/**
	 * 整型1
	 */
	public static final int INT_ONE = 1;
	
	/**
	 * 整型2
	 */
	public static final int INT_TWO = 2;
	
	/**
	 * 整型3
	 */
	public static final int INT_THREE = 3;
	
	/**
	 * 整型4
	 */
	public static final int INT_FOUR = 4;
	
	/**
	 * 整型5
	 */
	public static final int INT_FIVE = 5;
	
	/**
	 * 整型-1
	 */
	public static final int NEGATIVE_ONE = -1;
	
	/**
	 * 整型-2
	 */
	public static final int NEGATIVE_TWO = -2;
	
	/**
	 * 整型-3
	 */
	public static final int NEGATIVE_THREE = -3;
	
	/**
	 * 整型20
	 */
	public static final int INT_TWENTY = 20;
	
	/**
	 * 整型10
	 */
	public static final int INT_TEN = 10;
	
	/**
	 * 整型1000
	 */
	public static final int THOUSAND = 1000;
	
	/**
	 * flot型0.00
	 */
	public static final double DOUBLE_ZERO = 0.00;
	
	/**
	 * 常量-字符串-start
	 */
	public static final String CONSTANT_STRING_START = "start";
	
	/**
	 * 常量-字符串-limit
	 */
	public static final String CONSTANT_STRING_LIMIT = "limit";
	
	/**
	 * 常量-字符串0
	 */
	public static final String CONSTANT_STRING_ZERO = "0";
	
	/**
	 * 常量-字符串1
	 */
	public static final String CONSTANT_STRING_ONE = "1";
	
	/**
	 * 常量-字符串2
	 */
	public static final String CONSTANT_STRING_TWO = "2";
	
	/**
	 * 常量-字符串3
	 */
	public static final String CONSTANT_STRING_THREE = "3";
	
	/**
	 * 常量-字符串4
	 */
	public static final String CONSTANT_STRING_FOUR = "4";
	/**
	 * 常量-字符串11
	 */
	public static final String CONSTANT_STRING_ELEVEN = "11";
	
	/**
	 * 常量-字符串12
	 */
	public static final String CONSTANT_STRING_TWELVE = "12";
	/**
	 * 常量-字符串13
	 */
	public static final String CONSTANT_STRING_THIRTEEN = "13";
	/**
	 * 常量-字符串14
	 */
	public static final String CONSTANT_STRING_FOURTEEN = "14";
	/**
	 * 常量-字符串15
	 */
	public static final String CONSTANT_STRING_FIFTEEN = "15";
	/**
	 * 常量-字符串16
	 */
	public static final String CONSTANT_STRING_SIXTEEN = "16";
	/**
	 * 常量-字符串17
	 */
	public static final String CONSTANT_STRING_SEVENTEEN = "17";
	/**
	 * 常量-字符串18
	 */
	public static final String CONSTANT_STRING_EIGHTEEN = "18";
	
	
	/**
	 * 常量-字符串-1
	 */
	public static final String CONSTANT_STRING_NEGATIVE_ONE = "-1";
	
	/**
	 * 常量-字符串-2
	 */
	public static final String CONSTANT_STRING_NEGATIVE_TWO = "-2";
	
	/**
	 * 常量-字符串-3
	 */
	public static final String CONSTANT_STRING_NEGATIVE_THREE = "-3";
	
	/**
	 * 常量-字符串-4
	 */
	public static final String CONSTANT_STRING_NEGATIVE_FOUR = "-4";
	
	/**
	 * 常量-字符串-5
	 */
	public static final String CONSTANT_STRING_NEGATIVE_FIVE = "-5";
	
	/**
	 * 常量-字符串-6
	 */
	public static final String CONSTANT_STRING_NEGATIVE_SIX = "-6";
	
	/**
	 * 常量-字符串-7
	 */
	public static final String CONSTANT_STRING_NEGATIVE_SEVEN = "-7";
	
	/**
	 * 常量-字符串-8
	 */
	public static final String CONSTANT_STRING_NEGATIVE_EIGHT = "-8";
	
	/**
	 * 常量-字符串-9
	 */
	public static final String CONSTANT_STRING_NEGATIVE_NINE = "-9";
	
	/**
	 * 返回码-成功
	 */
	public static final String RESPONSE_CODE_SUCCESS = "000"; 
	
	/**
	 * 返回码-处理中
	 */
	public static final String RESPONSE_CODE_DELING = "999"; 
	
	/**
	 * 返回码-处理失败
	 */
	public static final String RESPONSE_CODE_FAIL = "400"; 
	
	/**
	 * 常量-字符串loginPage
	 */
	public static final String CONSTANT_STRING_LOGINPAGE = "loginPage";
	
	/**
	 * 常量-字符串error
	 */
	public static final String CONSANT_STRING_ERROR = "error";
	
	/**
	 * 非登录
	 */
	public static final String CONSTANT_STRING_NOT_LOGIN = "NOT_LOGIN";
	
	/**
	 * 数据中心接口-推广提成统计
	 */
	public static final String DC_INTERFACE_SPREAD_COMMISSION_STATISTICS = "/spreadCommissionStatistics/spread_commission_statistics";
	
	/**
	 * 数据中心接口-投资大赛排名统计
	 */
	public static final String DC_INTEFACE_INVESTMENT_GAME_RANKING_STATISTICS = "/investmentGameRanking/ranking_statistics";
	
	/**
	 * 数据中心-文件上传接口
	 */
	public static final String DC_INTERFACE_FILE_UPLOAD = "/fileUpload/file_upload.jhtml";

	
	/**
	 * 数据状态值 已暂存
	 */
	public static final String C_STATUS_I="I";
   /**
    * 数据状态值 已提交
    */
	public static final String C_STATUS_S="S";
	/**
	 * 数据状态值 已复核
	 */
	public static final String C_STATUS_C="C";
	
	
	public static final String C_STATUS_A="A";
	
	/**
	 * 数据状态值1
	 */
	public static final String DATA_STATUS_1="1";
	/**
	 * 数据状态值0
	 */
	public static final String DATA_STATUS_0="0";
	/**
	 * 数据状态值 已删除
	 */
	public static final String C_STATUS_D="D";
	
	/**
	 * 数据状态值 二次复核前临时状态 初审状态
	 */
	public static final String C_STATUS_E="E";
	
	/**
	 * 数据状态值 无
	 */
	public static final String C_STATUS_NA="NA";
	/**
	 * N
	 */
	public static final String C_STATUS_N="N";
	/**
	 * Y
	 */
	public static final String C_STATUS_Y="Y";
	
	/**
	 * P
	 */
	public static final String C_STATUS_P="P";
	
	/**
	 * R
	 */
	public static final String C_STATUS_R="R";
	
	/**
	 * U
	 */
	public static final String C_STATUS_U="U";
	
	/**
	 * 系列为家业
	 */
	public static final String SERIES_ID="75";
	/**
	 * 立项权限类别
	 */
	public static final String STARTUP_AUTHORITY = "2";   
	/**
	 * 项目权限类别
	 */
	public static final String   PRJ_AUTHORITY = "3";   
	/**
	 * 产品权限类别
	 */
	public static final String   PRD_AUTHORITY  = "4";   
	/**
	 * 权限类型: 普通
	 */
	public static final String   AUTHORITY_TYPE1  = "1";  
	
	/**
	 * 划款状态 ：已处理
	 */
	public static final String TRANSFER_STATUS_Y = "Y";
	/**
	 * 划款状态 ：未处理
	 */
	public static final String TRANSFER_STATUS_N = "N";
	/**
	 * 划款状态 ：处理中
	 */
	public static final String TRANSFER_STATUS_M = "M";
	
	
	/**
	 * 证券类型：票据
	 */
	public static final  String  NSC_SPECIESID_33="33";
	
	/**
	 * 划款指令来源类型:投资指令表
	 */
	public static final String SOUCE_TYPE_INVEST_A10 = "A10";
	/**
	 * 划款指令来源类型：表单录入
	 */
	public static final String SOUCE_TYPE_INVEST_A20 = "A20";
	/**
	 * 划款指令来源类型：费用
	 */
	public static final String SOUCE_TYPE_INVEST_A30 = "A30";
	/**
	 * 划款指令来源类型：权益
	 */
	public static final String SOUCE_TYPE_INVEST_A40 = "A40";
	/**
	 * 划款指令来源类型：本金
	 */
	public static final String SOUCE_TYPE_INVEST_A50 = "A50";
	/**
	 * 划款指令来源类型：利息
	 */
	public static final String SOUCE_TYPE_INVEST_A51 = "A51";
	/**
	 * 划款指令来源类型：清盘
	 */
	public static final String SOUCE_TYPE_INVEST_A52 = "A52";
	/**
	 * 划款指令来源类型：投资
	 */
	public static final String SOUCE_TYPE_INVEST_A60 = "A60";
	
	/**
	 * 划款指令来源类型：CMB
	 */
	public static final String SOUCE_TYPE_INVEST_A70 = "A70";
	/**
	 * 收益分配
	 */
	public static final String SOUCE_TYPE_INVEST_A80 = "A80";
	/**
	 * 交易类型：买入类型
	 */
	public static final String TRANSFER_TYPE_FP = "FP";
	
	/**
	 * 交易类型：卖出类型
	 */
	public static final String TRANSFER_TYPE_SE = "SE";
	
	/**
	 * 交易类型：红利转投
	 */
	public static final String TRANSFER_TYPE_BC = "BC";
	
	/**
	 * 投资指令码头来源机构：手工
	 */
	public static final String TXN_ORDER_FROM_0="0";
	
	/**
	 * 投资指令码头来源机构：EXCEL导入
	 */
	public static final String TXN_ORDER_FROM_1="1";
	
	/**
	 * 投资指令码头来源机构：CMB
	 */
	public static final String TXN_ORDER_FROM_2="2";
	
	/**
	 *  投资指令码头缓冲原因:跨期
	 */
	public static final String BUFFER_REMARK_0="0";
	
	/**
	 * 投资指令码头缓冲原因：资金头寸不足
	 */
	public static final String BUFFER_REMARK_1="1";
	
	/**
	 *  投资指令码头缓冲原因：持仓头寸不足
	 */
	public static final String BUFFER_REMARK_2="2";
	
	/**
	 *  投资指令码头缓冲原因：无交易信息对手
	 */
	public static final String BUFFER_REMARK_3="3";
	
	
	/**
	 * KM环境
	 */
	public static final String DOMAIN = "http://192.168.80.253:8088";
	
	public static final String EMPTY_JSON_ARR_STR = "[]";
	public static final String SUB_PRD_PRC_STATUS_300 = "300";
	
	public static final String ORDER_FUNDCODE = "基金代码";
	
	public static final String ORDER_TRANSFERDATE = "实际交易日期";
	
	public static final String ORDER_TXNCODE = "交易方向";
	
	public static final String ORDER_SECURSPECIES = "标的类型";
	
	public static final String ORDER_CREATEID = "经办人";
	
	public static final String ORDER_CREATETIME = "经办时间";
	
	public static final String ORDER_TXNCODE_01 = "申购";
	
	public static final String ORDER_TXNCODE_02 = "赎回";
	
	public static final String ORDER_SECURSPECIES_01 = "货币式基金";
	
	public static final String ORDER_SECURSPECIES_02 = "固收型信托计划";
	
	public static final String ORDER_SECURSPECIES_03 = "股票型信托计划";
	
	public static final String ORDER_SECURSPECIES_04 = "净值型信托计划";
	
	public static final String ORDER_SECURSPECIES_05 = "净值型理财产品";
	
	public static final String ORDER_SECURSPECIES_06 = "利率型理财产品";
	
	// 取值范围为"INTEREST""NET"",分别对应“利率型”“净值型”;
	public static final String ORDER_INTEREST = "INTEREST";
	public static final String ORDER_NET = "NET";
	
	/**
	 * 银行账户类型	托管户
	 */
	public static final String ACC_TYPE_ID_FA01 = "FA01";
	/**
	 * 已接收
	 */
	public static final String ORDER_STATUS_R00 = "R00";
	/**
	 * 已驳回
	 */
	public static final String ORDER_STATUS_B00 = "B00";
	/**
	 * 已划款
	 */
	public static final String ORDER_STATUS_C00 = "C00";
	/**
	 * 失败退款
	 */
	public static final String ORDER_STATUS_B01 = "B01";
	/**
	 * 已确认
	 */
	public static final String ORDER_STATUS_D00 = "D00";
	
	/**
	 * 交割状态：未交割
	 */
	public static final String DELIVER_STATUS_0="0";
	
	/**
	 * 交割状态：待交割
	 */
	public static final String DELIVER_STATUS_1="1";
	/**
	 * 交割状态：、已交割
	 */
	public static final String DELIVER_STATUS_2="2";

	/**
	 * 费用期间一次性支付
	 */
	public static final String FEE_FEEFREQUENCY_002 ="002";
	
	
	/**
	 * 费用到期一次性支付
	 */
	public static final String FEE_FEEFREQUENCY_001 ="001";
	
	
	/**
	 * 费用不定期
	 */
	public static final String FEE_FEEFREQUENCY_5 ="5";

	
	/**
	 * 系统操作
	 */
	public static final String SYSTEM = "system";
	
	/**
	 * 家业业绩报酬费用编码   FS01
	 */
	public static final String T_ACCOUNT_TYPE_FS01 = "FA10";  
	//客户收益
	public static final String T_ACCOUNT_TYPE_CY01 = "CY01";  
	/**
	 * TA总清算户
	 */
	public static final String T_ACCOUNT_TYPE_TA02 = "TA02";
	/**
	 * 托管户
	 */
	public static final String T_ACCOUNT_TYPE_FA01 = "FA01";
	/**
	 * 客户权益账户
	 */
	public static final String T_ACCOUNT_TYPE_FA04 = "FA04";
	/**
	 * TA参数提交 时间   0：00至16:30 
	 */
	public static final String TIME_0_TO_1630 = "0TO1630";
	
	/**
	 * TA参数提交 时间   16:30至19:00 
	 */
	public static final String TIME_1630_TO_19 = "1630TO19";
	
	/**
	 * TA参数提交 时间   19:00至24 
	 */
	
	public static final String TIME_19_TO_24 = "19TO24";

	/**
	 * 管理费
	 */
	public static final String FEE_FC01 = "FC01";
	
	/**
	 * 托管费
	 */
	public static final String FEE_FM01 = "FM01";
	
	/**
	 * 销服费-招行对私
	 */
	public static final String FEE_FS01 = "FS01";
	
	/**
	 * 销服费-招行对公-总行
	 */
	public static final String FEE_FS02 = "FS02";
	
	/**
	 * 销服费-招行对公-分行
	 */
	public static final String FEE_FS03 = "FS03";
	
	/**
	 * 其他机构销服费
	 */
	public static final String FEE_FS04 = "FS04";
	
	public static final String FEE_HRXF = "HRXF";
	
	/**
	 * 转让安排费
	 */
	public static final String FEE_FA01 = "FA01";	
	
	/**
	 * 子产品到期日 为长期，则取2025-12-31日
	 */
	public static final String DATE_20251231 = "20251231";
	
	public static final String DATE_2025_12_31 = "2025-12-31";
	
	/**
	 * 子产品期限 为长期，则取999
	 */
	public static final String PRD_TERM_999 = "999";

	/**
	 * M
	 */
	public static final String C_STATUS_M="M";
	
	/**
	 * 产品报备(PM)上传附件标识
	 */
	public static final String CONSTANT_BSR ="BSR";
	
	/**
	 * 整型100000000
	 */
	public static final int NUM_100000000 = 100000000;
	
	/**
	 * 整型10000
	 */
	public static final int NUM_10000 = 10000;
	
	/**收益分配从估值系统获取数据的常量参数*/
	/**
	 * 资产净值判断值
	 */
	public static final String APPRAISE_NOV_FKMBM_VAL = "701基金资产净值：";	
	/**
	 * 实收资本数量判断值
	 */
	public static final String APPRAISE_QTY_FKMBM_VAL = "601——实收资本";		
	/**
	 * 未分配利润	代码
	 */
	public static final String APPRAISE_NOT_ALLOT_PROFIT_FACCTCODE = "410402";	
	/**
	 * 未分配利润-已实现  代码
	 */
	public static final String APPRAISE_REALIZE_PROFIT_FACCTCODE = "41040210";	
	
	/**
	 * 是否存在融资方
	 */
	public static final String T_IS_EXIST_FINANCING = "IS_EXIST_FINANCING";
	
	/**
	 * 产品报备(PO)使用的方法名
	 */
	public static final String PRD_REPORT_METHOD = "PRD_REPORT";

	/**
	 * 是否存在管理方
	 */
	public static final String T_IS_EXIST_MANAGER = "IS_EXIST_MANAGER";
	
	/**
	 * 是否存在员工参与
	 */
	public static final String T_IS_STAFF_PARTICIPATION = "IS_STAFF_PARTICIPATION";
	
	/**
	 * 预警线备注
	 */
	public static final String T_WARN_LINE = "WARN_LINE";
	
	/**
	 * 追加线备注
	 */
	public static final String T_SUPP_LINE = "SUPP_LINE";
	
	/**
	 * 平仓线备注
	 */
	public static final String T_OPEN_LINE = "OPEN_LINE";
	
	/**
	 * 是否存在投顾财顾备注
	 */
	public static final String T_IS_INVESTMENT_ADVISER = "IS_INVESTMENT_ADVISER";
	
	/**
	 * 是否存在投资顾问/财务顾问及其关联方认购劣后级份额情形 备注
	 */
	public static final String T_IS_INVEST_ADVISER_SITUATION = "IS_INVEST_ADVISER_SITUATION";
	
	/**
	 * 份额登记机构(全称) 备注  
	 */
	public static final String T_SHARE_REGISTER_INSTITUTION = "SHARE_REGISTER_INSTITUTION";
	

	/**
	 * 其他业绩报酬 备注  
	 */
	public static final String T_SHARE_REGISTER_OTHER_FEE = "IS_OTHER_FEE_RATE";
	
	
	/**
	 * 标的来源
	 */
	public static final String SECUR_SRC_FUND = "FUND";
	public static final String SECUR_SRC_FP = "FP";
	public static final String SECUR_SRC_OTHER = "OTHER";
	/**
	 * 支持系统下单  
	 */
	public static final String SYSORDER_SUP_Y = "Y";
	public static final String SYSORDER_SUP_N = "N";
	/**
	 * 线下募集户绑定账号
	 */
	public static final String BANK_OFFLINE_CODE = "755901654110704";
	/**
	 * 线上募集户绑定账号
	 */
	public static final String BANK_ONLINE_CODE = "755901660510929";
	/**
	 * 0.00
	 */
	public static final String DOUBLE_ZERO_PERCENT = "0.00";
	
	/**
	 * 开户进度初始状态  类型:银行账户
	 */
	public static final String SCHEDULE_YH = "YH";
	
	/**
	 * 开户进度初始状态  类型:证券账户
	 */
	public static final String SCHEDULE_ZQ = "ZQ";
	
	/**
	 * 开户进度初始状态  类型:证券资金账户
	 */
	public static final String SCHEDULE_ZQZJ = "ZQZJ";
	
	/**
	 * 开户进度初始状态  类型:期货账户
	 */
	public static final String SCHEDULE_QH = "QH";
	
	/**
	 * 开户进度初始状态  类型:银行间账户
	 */
	public static final String SCHEDULE_YHJ = "YHJ";
	
	 /**
	 * 开户进度初始状态  类型:定存户
	 */
	public static final String SCHEDULE_YHD = "YHD";
	
	/**
	 * 常量-字符串5
	 */
	public static final String CONSTANT_STRING_FIVE = "5";
	
	/**
	 * 常量-字符串6
	 */
	public static final String CONSTANT_STRING_SIX = "6";

	
	/**
	 *  文件上传最大值
	 */
	public static final long MAX_UPLOAD_FILE_SIZE = 10 * 1024 * 1024 * 3;// 30M

	/**
	 * 暂存方法
	 */
	public static final String SAVETEMP_METHOD = "saveTemp";

	/**
	 * 提交方法
	 */
	public static final String SUBMIT_METHOD = "submit";

	/**
	 * 回退方法
	 */
	public static final String ROLLBACK_METHOD = "rollback";

	/**
	 * 显示方法
	 */
	public static final String SHOW_METHOD = "show";

	/**
	 * 新增方法
	 */
	public static final String ADD_METHOD = "add";

	/**
	 * 修改方法
	 */
	public static final String UPDATE_METHOD = "update";
	
	/**
	 * 新增的中文
	 */
	public static final String ADD_WORD = "新增";
	
	/**
	 * 修改的中文
	 */
	public static final String UPDATE_WORD = "修改";
	
	/**
	 * 回退的中文
	 */
	public static final String BACK_WORD = "回退";
	
	/**
	 * 菜单基金检算部FA
	 */
	public static final String STR_TYPE_FA = "FA";
	
	/**
	 * 菜单基金事务部TA
	 */
	public static final String STR_TYPE_TA = "TA";
	
	/**
	 * 文件上传成功标志
	 */
	public static final String FILE_UPLOAD_SUCCESS = "success";
	/**
	 * 文件上传失败标志
	 */
	public static final String FILE_UPLOAD_ERROR = "err";
	
	/**
	 * 还本类型
	 */
	public static final String NSC_RETURN_TYPE = "2";   //还本类型
	/**
	 * 收益类型
	 */
	public static final String NSC_YIELD_TYPE = "1";    //收益类型
	
	/**
	 * 签报事项的长度
	 */
	 public static final int  BOSTTITLE_SIZE = 300;
	
	 /**
	 * 线上
	 */
	 public static final String  ONLINE = "ONLINE";
	 
	 /**
	  * 天弘A证券外部编码
	  */
	 public static final String SECURID_TIANHONGA="420006";
	 
	 /**
	  * 天弘B证券外部编码
	  */
	 public static final String SECURID_TIANHONGB="420106";
	 
	 /**
	  * 博时A证券外部编码
	  */
	 public static final String SECURID_BOSHIA="050003";
	 
	 /**
	  * 博时B证券外部编码
	  */
	 public static final String SECURID_BOSHIB="000665";
	 
	/**
	 * 操作日志数据状态IN 新增待办
	 */
	public static final String LOG_DATA_TYPE_IN = "IN";
	
	/**
	 * 操作日志数据状态OUT 结束待办
	 */
	public static final String LOG_DATA_TYPE_OUT = "OUT";
	
	/**
	 * 历史意见类型：信息披露FA
	 */
	public static final String HIS_TYPE_10="10";
	/**
	 * 历史意见类型：信息披露PM
	 */
	public static final String HIS_TYPE_6="6";
	/**
	 * 历史意见类型：信息披露CM
	 */
	public static final String HIS_TYPE_7="7";
	/**
	 * 历史意见类型：信息披露PO
	 */
	public static final String HIS_TYPE_8="8";
	/**
	 * 历史意见类型：信息披露PS
	 */
	public static final String HIS_TYPE_9="9";
	
	/**
	 * 历史意见操作类型
	 */
	public static final Map<String,String> HISTORY_TYPE; 
	static{
		HISTORY_TYPE=new HashMap<String, String>();
		HISTORY_TYPE.put("P", "通过");
		HISTORY_TYPE.put("R", "驳回");
		HISTORY_TYPE.put("A", "新增");
		HISTORY_TYPE.put("U", "修改");
		HISTORY_TYPE.put("B", "回退");
		
	}

		//强制赎回 FA
	public static final String CON_TYPE_FA = "FA";
	//强制赎回 PM
	public static final String CON_TYPE_PM = "PM";	
	
	/**
	 * 母子联动产品
	 */
	public static final String CUSTODIAN_TYPE_B="B";
	
	/**
	 * MOM定时任务同步母计划信息
	 */
	public static final String SYNC_PLAN_ON_AUTO_TASK = "1011";
	/**
	 * MOM同步母计划信息
	 */
	public static final String SYNC_PLAN_ON_SUB_PLAN_ADD = "1013";
	/**
	 * MOM同步子计划信息
	 */
	public static final String SYNC_SUB_PLAN = "1015";
	/**
	 * MOM同步新增子计划投资记录信息
	 */
	public static final String SYNC_SUB_PLAN_TXN = "1017";
	/**
	 * DTP同步系统CBP标识
	 */
	public static final String DTP_APP_ID = "CBP";
	/**
	 * DTP同步系统类型 同步数据到招财通
	 */
	public static final String DTP_APP_TYPE_4_WAT_MOM = "WAT-MOM";
	/**
	 * 环境标识
	 */
	public final static String C_EVNVAR_TEST = "DEV"; 
}

