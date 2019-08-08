package com.cmwa.ecc.business.utils;

public class ECCConstant {

    public static final String APP_ID = "businessmgr";
    
    public static final String OPERATORID="CurrentUserId";
    
    public static final String USER_AUTHEN="opertorUser";
    public static final String ERR_CODE_0000 = "0000";//存储过程是否成功执行标志
    public static final String ERR_CODE_99991 = "99991";
    public static final String ERR_MSG_99991 = "参数为空";
    public static final String ERR_CODE_99992 = "99992";
    public static final String ERR_MSG_99992 = "系统错误";
    
    public static final String SET_CAPITAL_REVIEWBACK = "0";//款项到帐设置--复核退回
    public static final String SET_CAPITAL_REVIEWCHECK = "1";//款项到帐设置--经办确认
    public static final String SET_CAPITAL_REVIEWPASS = "2";//款项到帐设置--复核通过
    
    public static final String SET_PAYBACK_REVIEWBACK = "0";//退款处理--复核退回
    public static final String SET_PAYBACK_REVIEWCHECK = "1";//退款处理--经办确认
    public static final String SET_PAYBACK_REVIEWPASS = "2";//退款处理--复核通过
    
    public static final String STEP_STATUS_CODE_DOING = "I";//清算步骤处理状态代码--正在处理
    public static final String STEP_STATUS_CODE_DONE = "O";//清算步骤处理状态代码--处理完成
    
    public static final String QUERY_SETTLE_PROCESS_TYPE_ACCOUNT = "2830";//"A";//查询清算流程类型代码--帐户清算
    public static final String QUERY_SETTLE_PROCESS_TYPE_TRANS = "2840";//"T";//查询清算流程类型代码--交易清算
    
    public static final String CHARGE_TYPE_A = "A"; //前端收费
    public static final String FUND_STATUS_0 = "0"; //正常
    public static final String FUND_STATUS_1 = "1"; //发行
    
    
    public ECCConstant() {
    }
}