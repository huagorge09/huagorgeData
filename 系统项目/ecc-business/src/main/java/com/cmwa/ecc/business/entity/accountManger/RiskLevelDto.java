package com.cmwa.ecc.business.entity.accountManger;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;
@Alias("riskLevelDto")
public class RiskLevelDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String operatorId; 		//操作员代码
	private String operatorType; 	//操作类型
	private String permissionId; 	//权限代码
	
	private String opnm;            //操作员名称

	private String serialno; 		//流水号
	private String custno; 			//客户号
	private String invnm; 			//客户名称
	private String invtp; 			//客户类型
	private String invtpName; 			//客户类型

	private String risklevel; 		//风险等级
	private String risklevelName; 		//风险等级
	
	private String answer; 			//测评答案
    private String scope;			//测评得分
	
	private String audit;//是否审核Y已审核，N未审核
	
	private String errcode;			//错误代码
	private String errmsg;			//错误信息
	
	private String custriskdate;   //测评日期 自然日，YYYYMMDD added 20130517
	private String outdate;   //评估过期时间
	private String custrisktime;   //测评时间 时分秒，HHMMSS added 20130517
	private String fundacc;//已有基金账号开户时用 added 20130517
	
	private String regioncode;//网上/网下区分字段
	private String regioncodeName;//网上/网下区分字段
	private String invprtp;//投资者专业程度
	private String invprtpName;//投资者专业程度
	private String voicerecord;//录音文件编号
	
	private String chkflag;		//复核状态 N 待处理  I 处理中 Y 申请成功 C 申请失败 
	private String apptp;		//申请类型  0  转换专业用户 1 转换普通
	private String apptpName;		//申请类型  0  转换专业用户 1 转换普通
	private String appst;		//申请状态 C 复核作废  N 未处理  R 复核驳回  Y复核成功   L为申请
	private String appstName;		//申请状态 C 复核作废  N 未处理  R 复核驳回  Y复核成功   L为申请
	private String apdt; // 转换申请日期
	private String evaldate;
	private String tradeacco; 		//交易账号
	private String fundacco; 		//基金账号
	private String begindate;//未处理数
	private String enddate;//未处理数
	private String firstCustGroup;//
	private String secondCustGroup;//
	private String oldRiskLevel;
	private String oldRiskLevelName;
	private String specriskLevel;
	private String valueOfDir;
	private String valueOfM;
	
	public String getValueOfDir() {
		return valueOfDir;
	}

	public void setValueOfDir(String valueOfDir) {
		this.valueOfDir = valueOfDir;
	}

	public String getValueOfM() {
		return valueOfM;
	}

	public void setValueOfM(String valueOfM) {
		this.valueOfM = valueOfM;
	}

	public String getSpecriskLevel() {
		return specriskLevel;
	}

	public void setSpecriskLevel(String specriskLevel) {
		this.specriskLevel = specriskLevel;
	}

	public String getInvprtpName() {
		return invprtpName;
	}

	public void setInvprtpName(String invprtpName) {
		this.invprtpName = invprtpName;
	}

	public String getApptpName() {
		return apptpName;
	}

	public void setApptpName(String apptpName) {
		this.apptpName = apptpName;
	}

	public String getAppstName() {
		return appstName;
	}

	public void setAppstName(String appstName) {
		this.appstName = appstName;
	}
	public String getRegioncodeName() {
		return regioncodeName;
	}

	public void setRegioncodeName(String regioncodeName) {
		this.regioncodeName = regioncodeName;
	}
	public String getRisklevelName() {
		return risklevelName;
	}

	public void setRisklevelName(String risklevelName) {
		this.risklevelName = risklevelName;
	}

	public String getOldRiskLevelName() {
		return oldRiskLevelName;
	}

	public void setOldRiskLevelName(String oldRiskLevelName) {
		this.oldRiskLevelName = oldRiskLevelName;
	}
	public String getOutdate() {
		return outdate;
	}

	public void setOutdate(String outdate) {
		this.outdate = outdate;
	}
	public String getEvaldate() {
		return evaldate;
	}

	public void setEvaldate(String evaldate) {
		this.evaldate = evaldate;
	}
	public String getInvtpName() {
		return invtpName;
	}

	public void setInvtpName(String invtpName) {
		this.invtpName = invtpName;
	}
	public String getOpnm() {
		return opnm;
	}

	public void setOpnm(String opnm) {
		this.opnm = opnm;
	}
	public String getCustriskdate() {
		return custriskdate;
	}

	public void setCustriskdate(String custriskdate) {
		this.custriskdate = custriskdate;
	}

	public String getCustrisktime() {
		return custrisktime;
	}

	public void setCustrisktime(String custrisktime) {
		this.custrisktime = custrisktime;
	}

	public String getFundacc() {
		return fundacc;
	}

	public void setFundacc(String fundacc) {
		this.fundacc = fundacc;
	}

	public RiskLevelDto() {
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}

	public String getSerialno() {
		return serialno;
	}

	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}

	public String getCustno() {
		return custno;
	}

	public void setCustno(String custno) {
		this.custno = custno;
	}

	public String getInvnm() {
		return invnm;
	}

	public void setInvnm(String invnm) {
		this.invnm = invnm;
	}

	public String getInvtp() {
		return invtp;
	}

	public void setInvtp(String invtp) {
		this.invtp = invtp;
	}

	public String getRisklevel() {
		return risklevel;
	}

	public void setRisklevel(String risklevel) {
		this.risklevel = risklevel;
	}

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getAudit() {
		return audit;
	}

	public void setAudit(String audit) {
		this.audit = audit;
	}

	public String getRegioncode() {
		return regioncode;
	}

	public void setRegioncode(String regioncode) {
		this.regioncode = regioncode;
	}

	public String getInvprtp() {
		return invprtp;
	}

	public void setInvprtp(String invprtp) {
		this.invprtp = invprtp;
	}

	public String getVoicerecord() {
		return voicerecord;
	}

	public void setVoicerecord(String voicerecord) {
		this.voicerecord = voicerecord;
	}

	public String getChkflag() {
		return chkflag;
	}

	public void setChkflag(String chkflag) {
		this.chkflag = chkflag;
	}

	public String getApptp() {
		return apptp;
	}

	public void setApptp(String apptp) {
		this.apptp = apptp;
	}

	public String getAppst() {
		return appst;
	}

	public void setAppst(String appst) {
		this.appst = appst;
	}

	public String getApdt() {
		return apdt;
	}

	public void setApdt(String apdt) {
		this.apdt = apdt;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getOldRiskLevel() {
		return oldRiskLevel;
	}

	public void setOldRiskLevel(String oldRiskLevel) {
		this.oldRiskLevel = oldRiskLevel;
	}

	public String getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}

	public String getTradeacco() {
		return tradeacco;
	}

	public void setTradeacco(String tradeacco) {
		this.tradeacco = tradeacco;
	}

	public String getFundacco() {
		return fundacco;
	}

	public void setFundacco(String fundacco) {
		this.fundacco = fundacco;
	}

	public String getBegindate() {
		return begindate;
	}

	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getFirstCustGroup() {
		return firstCustGroup;
	}

	public void setFirstCustGroup(String firstCustGroup) {
		this.firstCustGroup = firstCustGroup;
	}

	public String getSecondCustGroup() {
		return secondCustGroup;
	}

	public void setSecondCustGroup(String secondCustGroup) {
		this.secondCustGroup = secondCustGroup;
	}

	@Override
	public String toString() {
		return "RiskLevelDto [operatorId=" + operatorId + ", operatorType="
				+ operatorType + ", permissionId=" + permissionId
				+ ", serialno=" + serialno + ", custno=" + custno + ", invnm="
				+ invnm + ", invtp=" + invtp + ", risklevel=" + risklevel
				+ ", answer=" + answer + ", scope=" + scope + ", audit="
				+ audit + ", errcode=" + errcode + ", errmsg=" + errmsg
				+ ", custriskdate=" + custriskdate + ", custrisktime="
				+ custrisktime + ", fundacc=" + fundacc + ", regioncode="
				+ regioncode + ", invprtp=" + invprtp + ", voicerecord="
				+ voicerecord + ", chkflag=" + chkflag + ", apptp=" + apptp
				+ ", appst=" + appst + ", apdt=" + apdt + ", tradeacco="
				+ tradeacco + ", fundacco=" + fundacco + ", begindate="
				+ begindate + ", enddate=" + enddate + ", firstCustGroup="
				+ firstCustGroup + ", secondCustGroup=" + secondCustGroup
				+ ", oldRiskLevel=" + oldRiskLevel + "]";
	}
}
