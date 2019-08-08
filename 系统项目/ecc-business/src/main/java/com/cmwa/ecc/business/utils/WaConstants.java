package com.cmwa.ecc.business.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 定义系统常用变量
 * 
 * @author fangdb
 * @date 2013-08-13
 */
public class WaConstants {

	
	/**
	 *投资线下使用判断证券市场
	 */
	@SuppressWarnings("serial")
	public final static Map<String, Object> map = new HashMap<String, Object>() {{    
		put("A","直接投资 ");
		put("B","资管计划");
		put("C","收益权");
		put("D","股票");
		put("F","资产支持证券 ");
		put("L","现金类资产 ");
		put("E","债券");
		put("G","基金");
		put("H","新三板");
		put("I","衍生品");
		put("J","有限合伙");
		put("K","结构化票据");
		put("M","其他");
		put("A3","委托理财");
	}}; 
	
	/* 银行 */
	public final static String INSTTYPE_BANK = "BANK";

	/* 保险 */
	public final static String INSTTYPE_INSURE = "INSURE";

	/* 信托 */
	public final static String TRUST = "TRUST";

	/* 证券 */
	public final static String INSTTYPE_SEC = "SEC";

	/* 其他 */
	public final static String INSTTYPE_OTHER = "OTHER";

	public final static String YES = "Y";

	public final static String NO = "N";

	/* 电子指令划款状态*/
	public final static String DCTTYPE_ELEC_TRANSFER_STATUS = "ELEC_TRANSFER_STATUS";
	
	/* T_PRJ_NOTER_CONF.NOTEITEM 通知事项类型 */
	public final static String DCTTYPE_NOT_ITM_TYP = "NOT_ITM_TYP";

	/* T_PRJ_NOTER_CONF.NOTEMODE 通知模式 */
	public final static String DCTTYPE_NOT_XXX_MOD = "NOT_XXX_MOD";

	/* 是否状态 */
	public final static String DCTTYPE_DAT_YES_NOX = "DAT_YES_NOX";

	/* 权益类型 */
	public final static String RIGHTSTYPE = "RHT_XXX_TYP";

	public final static String DCTTYPE_STOCKTYPE = "STK_PLD_TYP";

	public final static String DCTTYPE_IST_XXX_TYP = "IST_XXX_TYP";
	
	/* 附件保存类型 */
	public final static String ATT_TYP_BLOB = "BLOB";
	public final static String ATT_TYP_CLOB = "CLOB";
	public final static String ATT_TYP_FILE = "FILE";
	
	public final static String PROJECT_TYPE_SPL = "SPL";
	
	/* 正常 */
	public final static String MONSTAT_NORAML = "NORMAL";
	
	/* 低于预警线 */
	public final static String MONSTAT_WARN = "WARN";

	/* 低于平仓线 */
	public final static String MONSTAT_FATAL = "FATAL";
	
	public final static String DCTTYPE_SMS = "SMS";
	
	public final static String DCTTYPE_EMAIL = "EMAIL";
	
	public final static String FAIL = "FAIL";
	
	public final static String DCTTYPE_S = "S";
	public final static String DCTTYPE_I = "I";
	public final static String DCTTYPE_C = "C";
	public final static String DCTTYPE_D = "D";
	
	//整理合同类型
	public final static String DCTC_ARRANGE_TYPE = "CONTRACT_FTH_TYP";

	//整理一级分类
	public final static String DCTC_FTH_TYPE = "CONTRACT_FTH_TYP";
	
	//角色类型
	public final static String DCTTYPE_ROLE_XXX_TYPE = "ROLE_XXX_TYPE";
	
	//签报类型
    public final static String DCTTYPE_SIGN_TYPE = "SIGN_TYPE";
	
    /**签报类型：销售决策签报*/
	public static final String 	DCTTYPE_SIGN_TYPE_XSJC = "XSJC";
	
	//投资方式
	public final static String DCTTYPE_INVEST_MODE = "INVEST_MODE";
	//合作方类型
	public final static String DCTTYPE_NSC_JOINT_WORK = "NSC_JOINT_WORK";
	//票据类型
	public final static String DCTTYPE_NOTE_TYPE = "NOTE_TYPE";
	//信贷资产类型
	public final static String DCTTYPE_CREDIT_ASSET_TYP = "CREDIT_ASSET_TYP";
	//回购保证金支付方式
	public final static String DCTTYPE_BACK_CASH_WAY = "BACK_CASH_WAY";
	//回购价款
	public final static String DCTTYPE_BACK_PRICE = "BACK_PRICE";
	//违约金类型
	public final static String DCTTYPE_BREAK_CASH_TYPE = "BREAK_CASH_TYPE";
	//信托类型
	public final static String DCTTYPE_TRUST_TYPE = "TRUST_TYPE";
	//信托份额类别
	public final static String DCTTYPE_TRUST_CLASS = "TRUST_CLASS";
	
	//理财产品类型
	public final static String DCTTYPE_NSC_PRODUCT_TYPE = "NSC_PRODUCT_TYPE";
	
	//存款类型
	public final static String DCTTYPE_DEPOSIT_TYPE = "DEPOSIT_TYPE";
	
	//期权类型
	public final static String DCTTYPE_FUTURES_TYPE = "FUTURES_TYPE";
	
 
	//账户导出类型
	public final static String DICACC_EXP_TYPE = "ACC_EXP_TYPE";
	
	public final static String DCTTYPE_PAY_CYC_TYP = "PAY_CYC_TYP";
	
	public final static String DCTTYPE_PAY_DAY_TYP = "PAY_DAY_TYP";
	
	public final static String DCTTYPE_DAT_CHK_SAT = "DAT_CHK_SAT";
	
	/* 长拆短类型 */
	public final static String DCTTYPE_LNG_SHT_TYP = "LNG_SHT_TYP";
	
	/* 费用类型 */
	public final static String DCTTYPE_FEE_XXX_TYP = "FEE_XXX_TYP";
	
	/* 货币类型 */
	public final static String DCTTYPE_CNY_XXX_TYP = "CNY_XXX_TYP";
	
	/* 计划类型 */
	public final static String DCTTYPE_PLN_BIZ_TYP = "PLN_BIZ_TYP";
	
	/* 业务类型  */
	public final static String DCTTYPE_CAH_BIZ_TYP = "CAH_BIZ_TYP";
	
	/* 产品业务类型 */
	public final static String PRODUCT_PRD_SETT_TYP = "PRD_SETT_TYP";
	
	/* 产品业务类型H00 */
	public final static String PRODUCT_H00 = "H00";
	
	/* 信息披露签报类型  */
	public final static String DISC_XXX_TYP = "DISC_XXX_TYP";
	
	/* 项目过程状态 立项 */
	public final static String DCTTYPE_PRJ_PRC_SAT_SETPRJ = "500";
	
	/* 项目过程状态 待审中 */
	public final static String DCTTYPE_PRJ_PRC_SAT_VERFIY = "501";
	
	/* 项目过程状态 驳回 */
	public final static String DCTTYPE_PRJ_PRC_SAT_REJECT = "502" ;
	
	/* 项目过程状态 存续 */
	public final static String DCTTYPE_PRJ_PRC_SAT_CONTINUE = "600" ;
	
	/* t_sys_ta_parameter.pmky  基金类别*/
	public final static String PMKY_FUNDTP = "FUNDTP";
	
	/* t_sys_ta_parameter.pmky  货币类别*/
	public final static String PMKY_CURRENCYTYPE = "CURRENCYTYPE";
	
	/* t_sys_ta_parameter.pmky  投资区域*/
	public final static String PMKY_INVESTAREA = "INVESTAREA";
	
	/* t_sys_ta_parameter.pmky  发行方式*/
	public final static String PMKY_BOOKTYPE = "BOOKTYPE";
	
	/* t_sys_ta_parameter.pmky  投资方向*/
	public final static String PMKY_FUNDPT = "FUNDPT";
	
	/* t_sys_ta_parameter.pmky  募集方式*/
	public final static String PMKY_RAISETYPE = "RAISETYPE";
	
	/* t_sys_ta_parameter.pmky  收费方式*/
	public final static String PMKY_CHARGETYPE = "CHARGETYPE";
	
	/* t_sys_ta_parameter.pmky  赎回规则*/
	public final static String PMKY_REDEMTP = "REDEMTP";
	
	/* t_sys_ta_parameter.pmky  募集期利息处理方式*/
	public final static String PMKY_INTMODE = "INTMODE";
	
	/* t_sys_ta_parameter.pmky  转换选择标志*/
	public final static String PMKY_CONVST = "CONVST";
	
	/* 消息大类   产品发行阶段 */
	public final static String MSGCAT = "A";
	
	/* 一次报备提醒 */
	public final static String MSGSUBCAT_FIRSTREPORT = "A001";
	
	/* 二次报备提醒 */
	public final static String MSGSUBCAT_SECONDREPORT = "A002";
	
	/*文件挂网提醒  */
	public final static String MSGSUBCAT_A003 = "A003";
	
	/* 募集开始日提醒 */
	public final static String MSGSUBCAT_RAISEBEGDATE = "A004";
	
	/* 募集结束日提醒 */
	public final static String MSGSUBCAT_RAISEENDDATE = "A005";
	
	/* 验资日提醒 */
	public final static String MSGSUBCAT_VERIFYDATE = "A006";
	
	/* 起息日提醒 */
	public final static String MSGSUBCAT_INTBEGDATE = "A007";

	/* 预计募集开始日提醒 */
	public final static String MSGSUBCAT_PRERAISEBEGDATE = "A008";
	
	/* 预计募集结束日提醒 */
	public final static String MSGSUBCAT_PRERAISEENDDATE = "A009";	
	
	/* 日常收息日 */
	public final static String MSGSUBCAT_PAYINT = "B001";
	
	/*买入返售收费日提醒 */
	public final static String MSGSUBCAT_BBSA = "B002";
	
	/* 收益分配日 */
	public final static String MSGSUBCAT_FIT = "B003";
	
	/* 到期清盘日  */
	public final static String MSGSUBCAT_CLSENDDATE = "B004";
	
	/* 长拆短到期日提醒 */
	public final static String MSGSUBCAT_LTSENDDATE = "B005";
	
	/* 开放期开始日提醒 */
	public final static String MSGSUBCAT_BEGDATE = "B006";
	
	/* 开放期结束日提醒 */
	public final static String MSGSUBCAT_ENDDATE = "B007";
	
	//产品过程状态 发行中
	public final static String PRD_PRC_SAT_ISSUEING = "200";
	
	//产品过程状态 存续中
	public final static String PRD_PRC_SAT_RUNING = "300";
	
	//产品过程状态 清盘中
	public final static String PRD_PRC_SAT_CLEAR = "900";
	
	//费用类型  管理费
	public final static String FEE_MANAGE_TYP = "F10";
	
	//费用类型  托管费
	public final static String FEE_DEPOSIT_TYP = "F20";
	
	//费用类型  销售管理费
	public final static String FEE_SALES_TYP = "F30";
	
	//货币类型   人民币
	public final static String CNY_RMB_TYP = "RMB";
	
	/* 业务类型  收管理费/收托管费/收销售服务费  */
	public final static String CAH_BIZ_TYP = "F00";
	
	/*业务类型   收管理费  */
	public final static String CAH_BIZ_TYP_MANAGE = "F10";
	
	/*业务类型   收托管费  */
	public final static String CAH_BIZ_TYP_DEPOSIT = "F20";
	
	/*业务类型   收销售服务费  */
	public final static String CAH_BIZ_TYP_SERVICE = "F30";
	
	/*业务类型   付息  */
	public final static String CAH_BIZ_TYP_INTEREST = "A10";
	
	/*业务类型   还本*/
	public final static String CAH_BIZ_TYP_PRINCIPAL = "A20";
	
	/*业务类型   收益分配  */
	public final static String CAH_BIZ_TYP_FIT = "A30";
	
	/*计划类型   付息 */
    public final static String PLN_BIZ_TYP_INTEREST = "10";
    
	/*计划类型   还本 */
    public final static String PLN_BIZ_TYP_PRINCIPAL = "10";
    
	/*计划类型   收益分配 */
    public final static String PLN_BIZ_TYP_FIT = "10";
    
    public final static String SRC_XXX_TYP_FORM = "A10";
    
    //add by zhangy
    /** 1、等待提交 500*/
	public static final String PRJ_STARTUP_NEW = "500";

	/** 2、等待立项 501*/
	public static final String PRJ_STARTUP_AUDIT = "501";

	/** 3、等待上会 300*/
	public static final String PRJ_STARTUP_CHECK = "300";
	
	/** 立项通过 503*/
	public static final String PRJ_STARTUP_CHECK_PASS = "503";

	/** 4、立项驳回 502*/
	public static final String PRJ_STARTUP_AUDIT_BACK = "502";

	/** 5、上会驳回 301*/
	public static final String PRJ_STARTUP_CHECK_BACK = "301";
	
	/** 6、上会通过 999*/
	public static final String PRJ_STARTUP_PASS	= "999";
	
	/** 7、产品发起终止 000*/
	public static final String PRJ_STARTUP_END = "000";
	
	/** 全部状态 */
	public static final String PRJ_STARTUP_STAT_ALL = "500,501,300,502,301,999";

	/** 等待提交 */
	public static final String PRJ_STARTUP_NEW_NM = "等待提交";

	/** 等待立项 */
	public static final String PRJ_STARTUP_AUDIT_NM = "等待立项";

	/** 等待上会  */
	public static final String PRJ_STARTUP_CHECK_NM = "等待上会";

	/** 立项驳回 */
	public static final String PRJ_STARTUP_AUDIT_BACK_NM = "立项驳回";
	
	/** 立项通过*/
	public static final String PRJ_STARTUP_AUDIT_PASS_NM = "立项通过";

	/** 上会驳回 */
	public static final String PRJ_STARTUP_CHECK_BACK_NM = "上会驳回";
	
	/** 上会通过 */
	public static final String PRJ_STARTUP_PASS_NM	= "上会通过";
	
	/** 产品发起终止 */
	public static final String PRJ_STARTUP_END_NM = "发起终止";
	
	/** 全部状态 */
	public static final String PRJ_STARTUP_STAT_ALL_NM = "全部";
	
	/** T_PRJ_APPROVAL_COMMENT立项审批意见表    审核通过 10 */
	public static final String PRJ_APPR_ACTION_AUDIT_PASS = "10";
	
	/** T_PRJ_APPROVAL_COMMENT立项审批意见表    审核驳回 11 */
	public static final String PRJ_APPR_ACTION_AUDIT_BACK = "11";
	
	/** T_PRJ_APPROVAL_COMMENT立项审批意见表    上会通过 20 */
	public static final String PRJ_APPR_ACTION_CHECK_PASS = "20";
	
	/** T_PRJ_APPROVAL_COMMENT立项审批意见表    上会驳回 21*/
	public static final String PRJ_APPR_ACTION_CHECK_BACK = "21";
	
	/** T_PRJ_APPROVAL_COMMENT立项审批意见表    补充说明 30*/
	public static final String PRJ_APPR_ACTION_COMMENT = "30";
    
	/** 10 立项附件 */
	public static final String PRJ_ATTACH_STARTUP = "10";
	
	/** 20 立项附件 */
	public static final String PRJ_ATTACH_MEET = "20";
	
	/**
	 *  <b>买入</b>票据交易导入模板表头定义<br/>
	 *  {"No.","序号","期数"},0<br/>
		{"Notes No.","票据号",""},1<br/>
		{"Face Value","票据金额",""},2<br/>
		{"Issue Date","出票日",""},3 选填<br/>
		{"Trade Date","转让日",""},4<br/>
		{"End Date","到期日",""},5<br/>
		{"Issuer Bank","银票承兑银行或商票付款人开户银行",""},6   选填<br/>
		{"Value Date","止息日",""},7<br/>
		{"Rates","年利率",""},8<br/>
		{"Interest","实付利息",""},9<br/>
		{"Principle Amount","实付金额",""}10<br/>
	 */
	public static final String [][]IMPORT_NOTE_TXNS_HEAD_NAME_FP = {
		{"No.","序号","期数"},
		{"Notes No.","票据号","票据号"},
		{"Face Value","票据金额","票据金额"},
		{"Issue Date","出票日","出票日"},
		{"Trade Date","转让日","转让日"},
		{"End Date","到期日","到期日"},
		{"Issuer Bank","银票承兑银行或商票付款人开户银行","Issuer Bank"},
		{"Value Date","止息日","止息日"},
		{"Rates","年利率","年利率"},
		{"Interest","实付利息","实付利息"},
		{"Principle Amount","实付金额","实付金额"}
	};
	
	/**
	 *  <b>回售</b>票据交易导入模板表头定义<br/>
	 *  {"No.","期数","序号"},0<br/>
		{"Notes No.","票据号",""},1<br/>
		{"Face Value","票据金额",""},2<br/>
		{"Issue Date","出票日",""},3 选填<br/>
		{"Trade Date","转让日",""},4<br/>
		{"End Date","到期日",""},5<br/>
		{"Interest","实付利息",""},6<br/>
		{"Principle Amount","实付金额",""},7<br/>
		{"Clear Amount","清算金额",""},8 选填<br/>
		{"Clear Date","收益核算日",""}9<br/>
	 */
	public static final String [][]IMPORT_NOTE_TXNS_HEAD_NAME_FS = {
		{"No.","期数","序号"},
		{"Notes No.","票据号","票据号"},
		{"Face Value","票据金额","票据金额"},
		{"Issue Date","出票日","出票日"},
		{"Trade Date","转让日","转让日"},
		{"End Date","到期日","到期日"},
		{"Interest","实付利息","实付利息"},
		{"Principle Amount","实付金额","实付金额"},
		{"Clear Amount","清算金额","清算金额"},
		{"Clear Date","收益核算日","收益核算日"}
	};
	
	/**
	 * 数据导入验证不合法数据的条数
	 */
	public static final int IMPORT_NOTE_TXNS_COUNT = 30;
	/**
	 * 交易类型FP	买入
	 */
	public static final String TXN_TYPE_CODE_FP = "FP";
	/**
	 * 交易类型FS	回售
	 */
	public static final String TXN_TYPE_CODE_FS = "FS";
	/**
	 * 交易类型SE	卖出/赎回
	 */
	public static final String TXN_TYPE_CODE_SE = "SE";
	/**
	 * 交易类型BC	红利转投
	 */
	public static final String TXN_TYPE_CODE_BC = "BC";
	
    public static String getOperStr(String operType){
    	if("P".equals(operType)){
    		return "复核";
    	}else if("R".equals(operType)){
    		return "驳回";
    	}else if("U".equals(operType)){
    		return "反复核";
    	}else {
    		return "";
    	}
    }
    
    
	/**
	 * 银行账户类型	托管户
	 */
	public static final String ACC_TYPE_ID_FA01 = "FA01";
	
	/**
	 * 是否采用电子合同列名: 对应表T_BB_PRODUCT_INFO_EXT的字段IS_E_CONTRACT‘是否采用电子合同’(如果‘是否采用电子合同’的字段名称修改需要在这里修改相应的值)
	 */
	public static final String ISECONTRACT = "IS_E_CONTRACT";
	
	/**
	 * 资金来源列名: 对应表T_BB_PRODUCT_INFO_EXT的字段CAPITALSRC‘资金来源’(如果‘资金来源’的字段名称修改需要在这里修改相应的值)
	 */
	public static final String CAPITALSRC = "CAPITALSRC";
	/**
	 * 增信安排列名: 对应表T_BB_PRODUCT_INFO的字段GUARANTEETYPE‘增信安排’(如果‘增信安排’的字段名称修改需要在这里修改相应的值)
	 */
	public static final String GUARANTEETYPE = "GUARANTEETYPE";
	/**
	 * 是否有自有资金参与列名: 对应表T_BB_PRODUCT_INFO_EXT的字段IS_PRIVATE_CASH‘是否有自有资金参与’(如果‘是否有自有资金参与’的字段名称修改需要在这里修改相应的值)
	 */
	public static final String IS_PRIVATE_CASH_EXT = "IS_PRIVATE_CASH";
	/**
	 * 是否有业绩比较基准列名: 对应表T_BB_PRODUCT_INFO_EXT的字段IS_PERFORMANCE_COMP_BASE‘是否有业绩比较基准’(如果‘是否有业绩比较基准’的字段名称修改需要在这里修改相应的值)
	 */
	public static final String IS_PERFORMANCE_COMP_BASE_EXT = "IS_PERFORMANCE_COMP_BASE";
	
	/**
	 * 附件状态(ATT_XXX_SAT)用印稿   03 
	 */
	public static final String PRJ_ATTSTAT_FORMAL = "03";
	/**
	 * 地方融资平台种类扩展信息标示
	 */
	public static final String LOCAL_FINANCE_PLAT_TYPE_EXT = "LOCAL_FINANCE_PLAT_TYPE";   //地方融资平台种类扩展信息标示
	/**
	 * 地方融资平台种类扩展信息标示 扩展信息类型
	 */
	public static final String LOCAL_FINANCE_PLAT_TYPE     = "5";                         //扩展信息类型
	/**
	 * 还款来源扩展信息标示
	 */
	public static final String REFUND_SOURCE_EXT           = "REFUND_SOURCE";             //还款来源扩展信息标示
	/**
	 * 还款来源扩展信息标示   扩展信息类型
	 */
	public static final String REFUND_SOURCE_TYPE          = "3";                         //扩展信息类型
	/**
	 * 房地产种类扩展信息标示
	 */
	public static final String ESTATE_REAL_TYPE_EXT        = "ESTATE_REAL_TYPE";          //房地产种类扩展信息标示
	/**
	 * 房地产种类扩展信息标示  扩展信息类型
	 */
	public static final String ESTATE_REAL_TYPE            = "4";                         //扩展信息类型
	/**
	 * 交易对手方情况扩展信息标示
	 */
	public static final String COUNTERPARTY_INFO_EXT       = "COUNTERPARTY_INFO";         //交易对手方情况扩展信息标示
	/**
	 * 交易对手方情况扩展信息标示  扩展信息类型
	 */
	public static final String COUNTERPARTY_INFO_TYPE      = "5";                         //扩展信息类型
	/**
	 * 合作机构类型扩展信息标示
	 */
	public static final String COOPERATE_ORG_TYPE_EXT      = "COOPERATE_ORG_TYPE";        //合作机构类型扩展信息标示
	/**
	 * 合作机构类型扩展信息标示 扩展信息类型
	 */
	public static final String COOPERATE_ORG_TYPE          = "5";                         //扩展信息类型
	/**
	 * 投资资产种类扩展信息标示
	 */
	public static final String INVEST_ASSET_TYPE_EXT       = "INVEST_ASSET_TYPE";         //投资资产种类扩展信息标示
	/**
	 * 投资资产种类扩展信息标示  扩展信息类型
	 */
	public static final String INVEST_ASSET_TYPE           = "8";                         //扩展信息类型
	/**
	 * 合作机构尽调情况扩展信息标示
	 */
	public static final String COOPERATE_ORG_INFO_EXT      = "COOPERATE_ORG_INFO";        //合作机构尽调情况扩展信息标示
	/**
	 * 合作机构尽调情况扩展信息标示  扩展信息类型
	 */
	public static final String COOPERATE_ORG_INFO_TYPE     = "3";                         //扩展信息类型
	/**
	 * 销售机构保有量扩展信息标示
	 */
	public static final String SALE_ORG_RESERVES_EXT       = "SALE_ORG_RESERVES";         //销售机构保有量扩展信息标示
	/**
	 * 定增基金中的定增类型
	 */
	public static final String DCTTYPE_DAI_XXX_TYPE = "DCTTYPE_DAI_TYPE";
	
	/**
	 * 定增基金的状态
	 */
	public static final String DCTTYPE_DAI_XXX_STAT = "DCTTYPE_DAI_STAT";
	
	/**
	 * 定增基金的预警级别
	 */
	public static final String DCTTYPE_WARN_LEVEL = "DCTTYPE_WARNLEVEL";
	
	
	/**
	 * 家业常青系列编码ID
	 */
	public static final String JIAYE_SERIALCODE = "75";
	
	/**
	 * 业务分类
	 */
	public static final String PRJ_BIZ_TYP = "PRJ_BIZ_TYP";
	
	/**
	 * 业务产品类型
	 */
	public static final String PRD_BIZ_TYP = "PRD_BIZ_TYP";

	/**
	 * 合同子类型
	 */
	public static final String DCTC_CONTRACT_SON_TYPE = "CONTRACT_SON_TYP";

	/**
	 * 附件状态
	 */
	public static final String ATT_XXX_SAT = "ATT_XXX_SAT";
	
	/**
	 * 投资指令附件来源类型 ：包括合同资料、放款资料
	 */
	public static final String INVEST_ATTACH_TYPE = "1,2";
	/**
	 * 投资指令附件来源类型 ：包括合同资料、放款资料
	 */
	public static final String INVEST_ATTACH_FK_TYPE = "1,4";
	
	
	/** FAPM状态  初始数据*/
	public static final String DCTTYPE_I_NM = "初始数据";
	
	/** FAPM状态 已提交*/
	public static final String DCTTYPE_S_NM = "已提交";
	
	/** FAPM状态  已复核*/
	public static final String DCTTYPE_C_NM = "已复核";
	
	/** FAPM状态  已删除*/
	public static final String DCTTYPE_D_NM = "已删除";
	
	/** 签报中*/
	public static final String DCTTYPE_QBZ_NM = "签报中";
	
	/** 待发审批通过*/
	public static final String DCTTYPE_DSPTG_NM = "待发审批通过";
	
	/** 已披露*/
	public static final String DCTTYPE_YPL_NM = "已披露";
	
	/** 数据字典类型：临时投后事项类型 */
	public static final String DCTTYPE_INVEST_MATTER_TYPE = "INVEST_MATTER_TYPE";

	/** 数据字典类型：子产品过程状态 */
	public static final String DCTTYPE_SUB_PRD_STAT = "SUB_PRD_STAT";
	/**
	 * 来源类型为投资
	 */
	public static final String SOUCE_TYPE_INVEST = "A60";
	
	/** 数据字典类型：挂牌转让状态 */
	public static final String DCTTYPE_LIST_TRANSFER_STATUS = "LIST_TRANSFER_STATUS";
	
	/** 数据字典类型：挂牌转让方式 */
	public static final String DCTTYPE_LIST_TRANSFER_TYPE = "LIST_TRANSFER_TYPE";
	
	/**机构角色：代销机构*/
	public static final String INSTITUTION_ROL_TYPE="05";
	
	/** 数据字典类型：销售签报接口对应配置 */
	public static final String XSJC_NEXT_NODE_PARTICIPANTS = "nextNodeParticipants";
	
	public static final String XSJC_WK_PRDID = "wkPrdid";
	
	public static final String XSJC_CURR_NODE_ID = "currNodeID";
	
	public static final String XSJC_FLOW_UID_NUMB = "flowUidNumb";
	
	public static final String XSJC_NEXT_NODES = "nextNodes";
	
	
	/** 数据字典类型：费率复核状态 */
	public final static String DCTTYPE_FEE_CHK_SAT = "FEE_CHK_SAT";
	/** 数据字典类型：费率复核状态(PO) */
	public final static String DCTTYPE_FEE_CHK_PO_SAT = "FEE_CHK_PO_SAT";
	/** 数据字典类型：费率复核状态(FA) */
	public final static String DCTTYPE_FEE_CHK_FA_SAT = "FEE_CHK_FA_SAT";

	
	/** 数据字典类型：核心信息表分类 */
	public final static String REPORTTYPE_KERNEL_INFO = "KERNEL_INFO";
	
	/** 数据字典类型：资管月报分类 */
	public final static String REPORTTYPE_MANAGE_MONTHLY = "MANAGE_MONTHLY";
	
	/** 数据字典类型：资产明细表分类 */
	public final static String REPORTTYPE_CAPITAL_DETAIL = "CAPITAL_DETAIL";

}
