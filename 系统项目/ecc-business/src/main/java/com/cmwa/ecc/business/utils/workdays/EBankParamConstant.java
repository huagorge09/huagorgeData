package com.cmwa.ecc.business.utils.workdays;

/**
 * <p>Title: Legion EBank</p>
 *
 * <p>Description: Legion ���нӿ�</p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: Legion Technology</p>
 *
 * @author xuhw
 * @version 1.0
 */
/**
 * <p>Title: Legion EBank</p>
 *
 * <p>Description: Legion 银行接口</p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: Legion Technology</p>
 *
 * @author xuhw
 * @version 1.0
 */
public class EBankParamConstant {
    /**
     * PM_ST 银行接口
     */
    public static final String PM_ST_EBANK = "EBANK";

    /**
     * PM_KY 指令批量标记
     */
    public static final String PM_KY_EBANK$BAT_FLG = "BATFLG";
    /**
     * PM_CO 指令批量标记-汇总
     */
    public static final String PM_CO_EBANK$BAT_FLG$03 = "03";

    /**
     * PM_KY 基础数据状态
     */
    public static final String PM_KY_EBANK$STATUS = "STATUS";
    /**
     * PM_CO 基础数据状态-正常
     */
    public static final String PM_CO_EBANK$STATUS$Y = "Y";
    /**
     * PM_CO 基础数据状态-禁用
     */
    public static final String PM_CO_EBANK$STATUS$N = "N";

    /**
     * PM_KY 后台业务应答
     */
    public static final String PM_KY_EBANK$ERR_CODE = "ERRCODE";
    /**
     * PM_CO 后台业务应答-成功
     */
    public static final String PM_CO_EBANK$ERR_CODE$0000 = "0000";
    /**
     * PM_CO 后台业务应答-失败
     */
    public static final String PM_CO_EBANK$ERR_CODE$9999 = "9999";

    /**
     * 交易状态
     * N-待处理，Y-成功，F-失败，I-处理中，E-异常
     */
    /**
     * PM_KY 交易状态
     */
    public static final String PM_KY_EBANK$TRAN_ST = "TRANST";
    /**
     * PM_CO 交易状态-待处理
     */
    public static final String PM_CO_EBANK$TRAN_ST$N = "N";
    /**
     * PM_CO 交易状态-成功
     */
    public static final String PM_CO_EBANK$TRAN_ST$Y = "Y";
    /**
     * PM_CO 交易状态-失败
     */
    public static final String PM_CO_EBANK$TRAN_ST$F = "F";
    /**
     * PM_CO 交易状态-处理中
     */
    public static final String PM_CO_EBANK$TRAN_ST$I = "I";
    /**
     * PM_CO 交易状态-异常
     */
    public static final String PM_CO_EBANK$TRAN_ST$E = "E";

    /**
     * 商户应答代码
     * Y-成功，F-失败，I-处理中，E-异常
     */
    /**
     * PM_KY 商户应答代码
     */
    public static final String PM_KY_EBANK$MER_RESP_CO = "MERRESPCO";
    /**
     * PM_CO 商户应答代码-成功
     */
    public static final String PM_CO_EBANK$MER_RESP_CO$Y = "Y";
    /**
     * PM_NM 商户应答代码-成功
     */
    public static final String PM_NM_EBANK$MER_RESP_CO$Y = "成功";
    /**
     * PM_CO 商户应答代码-失败
     */
    public static final String PM_CO_EBANK$MER_RESP_CO$F = "F";
    /**
     * PM_NM 商户应答代码-失败
     */
    public static final String PM_NM_EBANK$MER_RESP_CO$F = "失败";
    /**
     * PM_CO 商户应答代码-处理中
     */
    public static final String PM_CO_EBANK$MER_RESP_CO$I = "I";
    /**
     * PM_NM 商户应答代码-处理中
     */
    public static final String PM_NM_EBANK$MER_RESP_CO$I = "处理中";
    /**
     * PM_CO 商户应答代码-异常
     */
    public static final String PM_CO_EBANK$MER_RESP_CO$E = "E";
    /**
     * PM_NM 商户应答代码-异常
     */
    public static final String PM_NM_EBANK$MER_RESP_CO$E = "异常";

    /**
     * PM_KY 交易引擎
     */
    public static final String PM_KY_EBANK$EXECUTE = "EXECUTE";
    /**
     * PM_KY 查询引擎
     */
    public static final String PM_KY_EBANK$QUERY = "QUERY";
    /**
     * PM_KY 监听引擎
     */
    public static final String PM_KY_EBANK$LISTENER = "LISTENER";

    /**
     * PM_KY 指令日志等级
     */
    public static final String PM_KY_EBANK$CMD_LOG_LVL = "CMDLOGLVL";
    /**
     * PM_CO 指令日志等级-INFO
     */
    public static final String PM_CO_EBANK$CMD_LOG_LVL$INFO = "INFO";
    /**
     * PM_CO 指令日志等级-DEBUG
     */
    public static final String PM_CO_EBANK$CMD_LOG_LVL$DEBUG = "DEBUG";
    /**
     * PM_CO 指令日志等级-ERROR
     */
    public static final String PM_CO_EBANK$CMD_LOG_LVL$ERROR = "ERROR";
    /**
     * PM_CO 指令日志等级-FATAL
     */
    public static final String PM_CO_EBANK$CMD_LOG_LVL$FATAL = "FATAL";

    /**
     * PM_KY 指令日志代码
     */
    public static final String PM_KY_EBANK$CMD_LOG_CO = "CMDLOGCO";
    /**
     * PM_CO 指令日志代码-MER_TRAN_MSG 商户请求，商户请求交易报文
     */
    public static final String PM_CO_EBANK$CMD_LOG_CO$M_MER_TRAN_MSG = "MMERTRANMSG";
    /**
     * PM_CO 指令日志代码-BNK_TRAN_MSG 商户请求，银行应答交易报文
     */
    public static final String PM_CO_EBANK$CMD_LOG_CO$M_BNK_TRAN_MSG = "MBNKTRANMSG";
    /**
     * PM_CO 指令日志代码-MER_TRAN_MSG 银行请求，商户应答交易报文
     */
    public static final String PM_CO_EBANK$CMD_LOG_CO$B_MER_TRAN_MSG = "BMERTRANMSG";
    /**
     * PM_CO 指令日志代码-BNK_TRAN_MSG 银行请求，银行请求交易报文
     */
    public static final String PM_CO_EBANK$CMD_LOG_CO$B_BNK_TRAN_MSG = "BBNKTRANMSG";


    public EBankParamConstant() {
    }
}
