package com.cmwa.ecc.business.entity.fundinfo;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.type.Alias;

/***
 * 产品信息
 * @author cmfchina
 *
 */
@Alias("productInfoDto")
public class ProductInfoDto {
	
	private String name;//全名称
	private String abbreviation;//简名称
	private String funcode;//产品代码
	private String primaryKey; //前端主键
	private String typeId;//产品类型id
	private String typeName;//产品类型名称
	private String id;//产品系列id
	private String dName;//产品系列名称
	private String methods;//运作方式
	private String theTerm;//存续期限
	private String term;//期限
	private String termunit;//期限单位
	private String scale;//产品规模
	private String classification;//产品分级
	private String parValue;//初始份额面值
	private String money;//最低认购金额
	private String arrangement;//流动性安排
	private String cycle;//委托周期
	private String profit;//产品预期收益率
	private String distribution;//产品收益分配
	private String range;//投资范围
	private String strategy;//投资策略
	private String classifications;//份额的分级
	private String rate;//认购/参与费率
	private String exitRates;//退出费率
	private String defaultExitRate;//违约退出费率
	private String serviceRee;//客户服务费率
	private String managementFee;//管理费率
	private String trusteeFee;//托管费率
	private String remuneration;//业绩报酬
	private String features;//风险收益特征
	private String states;//产品状态
	private String statesPmnm;
	private String statename;//产品状态名称
	private String adname;//产品别名
	private String adtext;//广告语
	private String subdeadline;//认购截止
	private String interestdate;//起息日期
	private String maturitydate;//到期日期
	private String paymentinter;//到期日之后T+?到账
	private String moneyStep;//认购步长
	private String msgdate;//短信提醒提前天数
	private String texttitle1;//标题
	private String texttitle2;
	private String texttitle3;
	private String textcontent1;//内容
	private String textcontent2;
	private String textcontent3;
	
	private String textcontent1_div;//内容
	private String textcontent2_div;
	private String textcontent3_div;
	
	private String isAttention;//是否关注（用于前端根据关注表设置值）
	private String isHot;//是否热销
	private String isHotName;//是否热销
	private String isEcabale;//是否网上销售
	private String reserve;//剩余额度
	private String reserve2;
	private String reserve3;
	private String salesDate;//发售日
	private String flag;
	private String fundriskLevel;//风险等级
	
	private String fundriskLevelPmnm;
	
	/**
	 * 高风险产品说明
	 */
	private String highRiskExplain;
	
	/**
	 * 可售渠道（例：招赢通【ZYT】）
	 */
	private String vendChannels;
	
	
	private String appointdate;//预约开始日期
	private String displaylimit;//页面展示用剩余额度（业务可以修改）
	private String remainamt;//实际剩余额度
	private String templetid;//模板ID
	private String templetName;//
	private String contractversion;//合同版本
 
    private String offlinelimit;//线下销售额度
    private String onlinelimit;//线上销售额度
	private String offlinesalelimit;//线下销售额度(与ONLINELIMIT对立) 
    
    private String created_by;//增加者
    private String review_by;//复审者
    private String review_status;//复审状态；（N,待复审，Y复审通过，R驳回）
	private String review_statusPmnm;
    
	private String appointEndDate;//预约结束日期
	private String currentWorkdate;//当前工作日  
	
	private String returnCode;
	
	private String suspensionlimit;//挂起额度
	private String lastfreedate;//最新释放额度时间
	 
	private String	disorder;				//展现顺序
	private String prefix;//圈内简称
	private List<QuestionDto> question=new ArrayList<QuestionDto>();
	private List<FundElementDto> elementList =new ArrayList<FundElementDto>();
	
	
	
	private String fundState; // 预约期,认购期 只用于前端数据显示
	private String dateStep; // 同上  N预购期  Y认购期
	private String offlineFund;   //OFFLINEFUND  Y.线下  N。线上
	
	private String keyvalue;
	
	public String getKeyvalue() {
		return keyvalue;
	}
	public void setKeyvalue(String keyvalue) {
		this.keyvalue = keyvalue;
	}
	private int period; //期数
	
	private int oldPeriod;
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getDisorder() {
		return disorder;
	}
	public void setDisorder(String disorder) {
		this.disorder = disorder;
	}
	public String getPaymentinter() {
		return paymentinter;
	}
	public void setPaymentinter(String paymentinter) {
		this.paymentinter = paymentinter;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getTermunit() {
		return termunit;
	}
	public void setTermunit(String termunit) {
		this.termunit = termunit;
	}
	public String getContractversion() {
		return contractversion;
	}
	public void setContractversion(String contractversion) {
		this.contractversion = contractversion;
	}
	public String getLastfreedate() {
		return lastfreedate;
	}
	public void setLastfreedate(String lastfreedate) {
		this.lastfreedate = lastfreedate;
	}
	public String getOfflinesalelimit() {
		return offlinesalelimit;
	}
	public void setOfflinesalelimit(String offlinesalelimit) {
		this.offlinesalelimit = offlinesalelimit;
	}
	public String getSuspensionlimit() {
		return suspensionlimit;
	}
	public void setSuspensionlimit(String suspensionlimit) {
		this.suspensionlimit = suspensionlimit;
	}
	public String getTempletName() {
		return templetName;
	}
	public void setTempletName(String templetName) {
		this.templetName = templetName;
	}
	public String getTextcontent1_div() {
		return textcontent1_div;
	}
	public void setTextcontent1_div(String textcontent1_div) {
		this.textcontent1_div = textcontent1_div;
	}
	public String getTextcontent2_div() {
		return textcontent2_div;
	}
	public void setTextcontent2_div(String textcontent2_div) {
		this.textcontent2_div = textcontent2_div;
	}
	public String getTextcontent3_div() {
		return textcontent3_div;
	}
	public void setTextcontent3_div(String textcontent3_div) {
		this.textcontent3_div = textcontent3_div;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public String getReview_by() {
		return review_by;
	}
	public void setReview_by(String review_by) {
		this.review_by = review_by;
	}
	public String getReview_status() {
		return review_status;
	}
	public void setReview_status(String review_status) {
		this.review_status = review_status;
	}
	public String getOfflinelimit() {
		return offlinelimit;
	}
	public void setOfflinelimit(String offlinelimit) {
		this.offlinelimit = offlinelimit;
	}
	public String getOnlinelimit() {
		return onlinelimit;
	}
	public void setOnlinelimit(String onlinelimit) {
		this.onlinelimit = onlinelimit;
	}
 
	public String getAppointEndDate() {
		return appointEndDate;
	}
	public void setAppointEndDate(String appointEndDate) {
		this.appointEndDate = appointEndDate;
	}
	public String getCurrentWorkdate() {
		return currentWorkdate;
	}
	public void setCurrentWorkdate(String currentWorkdate) {
		this.currentWorkdate = currentWorkdate;
	}
 
	public String getTempletid() {
		return templetid;
	}
	public void setTempletid(String templetid) {
		this.templetid = templetid;
	}
	public String getAppointdate() {
		return appointdate;
	}
	public void setAppointdate(String appointdate) {
		this.appointdate = appointdate;
	}
	public String getDisplaylimit() {
		return displaylimit;
	}
	public void setDisplaylimit(String displaylimit) {
		this.displaylimit = displaylimit;
	}
	public String getRemainamt() {
		return remainamt;
	}
	public void setRemainamt(String remainamt) {
		this.remainamt = remainamt;
	}
 
	public String getMsgdate() {
		return msgdate;
	}
	public void setMsgdate(String msgdate) {
		this.msgdate = msgdate;
	}
	public String getFundriskLevel() {
		return fundriskLevel;
	}
	public void setFundriskLevel(String fundriskLevel) {
		this.fundriskLevel = fundriskLevel;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAbbreviation() {
		return abbreviation;
	}
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	public String getFuncode() {
		return funcode;
	}
	public void setFuncode(String funcode) {
		this.funcode = funcode;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getdName() {
		return dName;
	}
	public void setdName(String dName) {
		this.dName = dName;
	}
	public String getMethods() {
		return methods;
	}
	public void setMethods(String methods) {
		this.methods = methods;
	}
	public String getTheTerm() {
		return theTerm;
	}
	public void setTheTerm(String theTerm) {
		this.theTerm = theTerm;
	}
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	public String getParValue() {
		return parValue;
	}
	public void setParValue(String parValue) {
		this.parValue = parValue;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getArrangement() {
		return arrangement;
	}
	public void setArrangement(String arrangement) {
		this.arrangement = arrangement;
	}
	public String getCycle() {
		return cycle;
	}
	public void setCycle(String cycle) {
		this.cycle = cycle;
	}
	public String getProfit() {
		return profit;
	}
	public void setProfit(String profit) {
		this.profit = profit;
	}
	public String getDistribution() {
		return distribution;
	}
	public void setDistribution(String distribution) {
		this.distribution = distribution;
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public String getStrategy() {
		return strategy;
	}
	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}
	public String getClassifications() {
		return classifications;
	}
	public void setClassifications(String classifications) {
		this.classifications = classifications;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getExitRates() {
		return exitRates;
	}
	public void setExitRates(String exitRates) {
		this.exitRates = exitRates;
	}
	public String getDefaultExitRate() {
		return defaultExitRate;
	}
	public void setDefaultExitRate(String defaultExitRate) {
		this.defaultExitRate = defaultExitRate;
	}
	public String getServiceRee() {
		return serviceRee;
	}
	public void setServiceRee(String serviceRee) {
		this.serviceRee = serviceRee;
	}
	public String getManagementFee() {
		return managementFee;
	}
	public void setManagementFee(String managementFee) {
		this.managementFee = managementFee;
	}
	public String getTrusteeFee() {
		return trusteeFee;
	}
	public void setTrusteeFee(String trusteeFee) {
		this.trusteeFee = trusteeFee;
	}
	public String getRemuneration() {
		return remuneration;
	}
	public void setRemuneration(String remuneration) {
		this.remuneration = remuneration;
	}
	public String getFeatures() {
		return features;
	}
	public void setFeatures(String features) {
		this.features = features;
	}
	public String getStates() {
		return states;
	}
	public void setStates(String states) {
		this.states = states;
	}
	public String getStatename() {
		return statename;
	}
	public void setStatename(String statename) {
		this.statename = statename;
	}
	public String getAdname() {
		return adname;
	}
	public void setAdname(String adname) {
		this.adname = adname;
	}
	public String getAdtext() {
		return adtext;
	}
	public void setAdtext(String adtext) {
		this.adtext = adtext;
	}
	public String getSubdeadline() {
		return subdeadline;
	}
	public void setSubdeadline(String subdeadline) {
		this.subdeadline = subdeadline;
	}
	public String getInterestdate() {
		return interestdate;
	}
	public void setInterestdate(String interestdate) {
		this.interestdate = interestdate;
	}
	public String getMaturitydate() {
		return maturitydate;
	}
	public void setMaturitydate(String maturitydate) {
		this.maturitydate = maturitydate;
	}
	public String getMoneyStep() {
		return moneyStep;
	}
	public void setMoneyStep(String moneyStep) {
		this.moneyStep = moneyStep;
	}
	public String getTexttitle1() {
		return texttitle1;
	}
	public void setTexttitle1(String texttitle1) {
		this.texttitle1 = texttitle1;
	}
	public String getTexttitle2() {
		return texttitle2;
	}
	public void setTexttitle2(String texttitle2) {
		this.texttitle2 = texttitle2;
	}
	public String getTexttitle3() {
		return texttitle3;
	}
	public void setTexttitle3(String texttitle3) {
		this.texttitle3 = texttitle3;
	}
	public String getTextcontent1() {
		return textcontent1;
	}
	public void setTextcontent1(String textcontent1) {
		this.textcontent1 = textcontent1;
	}
	public String getTextcontent2() {
		return textcontent2;
	}
	public void setTextcontent2(String textcontent2) {
		this.textcontent2 = textcontent2;
	}
	public String getTextcontent3() {
		return textcontent3;
	}
	public void setTextcontent3(String textcontent3) {
		this.textcontent3 = textcontent3;
	}
	public String getIsAttention() {
		return isAttention;
	}
	public void setIsAttention(String isAttention) {
		this.isAttention = isAttention;
	}
	public String getIsHot() {
		return isHot;
	}
	public void setIsHot(String isHot) {
		this.isHot = isHot;
	}
	public String getIsEcabale() {
		return isEcabale;
	}
	public void setIsEcabale(String isEcabale) {
		this.isEcabale = isEcabale;
	}
 
	public String getReserve() {
		return reserve;
	}
	public void setReserve(String reserve) {
		this.reserve = reserve;
	}
	public String getReserve2() {
		return reserve2;
	}
	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}
	public String getReserve3() {
		return reserve3;
	}
	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}
	public String getSalesDate() {
		return salesDate;
	}
	public void setSalesDate(String salesDate) {
		this.salesDate = salesDate;
	}
	@Override
	public String toString() {
		return "ProductInfoDto [name=" + name + ", abbreviation="
				+ abbreviation + ", funcode=" + funcode + ", typeId=" + typeId
				+ ", typeName=" + typeName + ", id=" + id + ", dName=" + dName
				+ ", methods=" + methods + ", theTerm=" + theTerm + ", term="
				+ term + ", termunit=" + termunit + ", scale=" + scale
				+ ", classification=" + classification + ", parValue="
				+ parValue + ", money=" + money + ", arrangement="
				+ arrangement + ", cycle=" + cycle + ", profit=" + profit
				+ ", distribution=" + distribution + ", range=" + range
				+ ", strategy=" + strategy + ", classifications="
				+ classifications + ", rate=" + rate + ", exitRates="
				+ exitRates + ", defaultExitRate=" + defaultExitRate
				+ ", serviceRee=" + serviceRee + ", managementFee="
				+ managementFee + ", trusteeFee=" + trusteeFee
				+ ", remuneration=" + remuneration + ", features=" + features
				+ ", states=" + states + ", statename=" + statename
				+ ", adname=" + adname + ", adtext=" + adtext
				+ ", subdeadline=" + subdeadline + ", interestdate="
				+ interestdate + ", maturitydate=" + maturitydate
				+ ", paymentinter=" + paymentinter + ", moneyStep=" + moneyStep
				+ ", msgdate=" + msgdate + ", texttitle1=" + texttitle1
				+ ", texttitle2=" + texttitle2 + ", texttitle3=" + texttitle3
				+ ", textcontent1=" + textcontent1 + ", textcontent2="
				+ textcontent2 + ", textcontent3=" + textcontent3
				+ ", textcontent1_div=" + textcontent1_div
				+ ", textcontent2_div=" + textcontent2_div
				+ ", textcontent3_div=" + textcontent3_div + ", isAttention="
				+ isAttention + ", isHot=" + isHot + ", isEcabale=" + isEcabale
				+ ", reserve=" + reserve + ", reserve2=" + reserve2
				+ ", reserve3=" + reserve3 + ", salesDate=" + salesDate
				+ ", flag=" + flag + ", fundriskLevel=" + fundriskLevel
				+ ", appointdate=" + appointdate + ", displaylimit="
				+ displaylimit + ", remainamt=" + remainamt + ", templetid="
				+ templetid + ", templetName=" + templetName
				+ ", contractversion=" + contractversion + ", offlinelimit="
				+ offlinelimit + ", onlinelimit=" + onlinelimit
				+ ", offlinesalelimit=" + offlinesalelimit + ", created_by="
				+ created_by + ", review_by=" + review_by + ", review_status="
				+ review_status + ", appointEndDate=" + appointEndDate
				+ ", currentWorkdate=" + currentWorkdate + ", returnCode="
				+ returnCode + ", suspensionlimit=" + suspensionlimit
				+ ", lastfreedate=" + lastfreedate + ", disorder=" + disorder
				+ ", prefix=" + prefix + "]";
	}
	public List<QuestionDto> getQuestion() {
		return question;
	}
	public void setQuestion(List<QuestionDto> question) {
		this.question = question;
	}
	public String getStatesPmnm() {
		return statesPmnm;
	}
	public void setStatesPmnm(String statesPmnm) {
		this.statesPmnm = statesPmnm;
	}
	public String getFundriskLevelPmnm() {
		return fundriskLevelPmnm;
	}
	public void setFundriskLevelPmnm(String fundriskLevelPmnm) {
		this.fundriskLevelPmnm = fundriskLevelPmnm;
	}
	public String getIsHotName() {
		return isHotName;
	}
	public void setIsHotName(String isHotName) {
		this.isHotName = isHotName;
	}
	public String getReview_statusPmnm() {
		return review_statusPmnm;
	}
	public void setReview_statusPmnm(String review_statusPmnm) {
		this.review_statusPmnm = review_statusPmnm;
	}
	public String getFundState() {
		return fundState;
	}
	public void setFundState(String fundState) {
		this.fundState = fundState;
	}
	public String getDateStep() {
		return dateStep;
	}
	public void setDateStep(String dateStep) {
		this.dateStep = dateStep;
	}
	public String getOfflineFund() {
		return offlineFund;
	}
	public void setOfflineFund(String offlineFund) {
		this.offlineFund = offlineFund;
	}
	public List<FundElementDto> getElementList() {
		return elementList;
	}
	public void setElementList(List<FundElementDto> elementList) {
		this.elementList = elementList;
	}
	public int getPeriod() {
		return period;
	}
	public void setPeriod(int period) {
		this.period = period;
	}
	public int getOldPeriod() {
		return oldPeriod;
	}
	public void setOldPeriod(int oldPeriod) {
		this.oldPeriod = oldPeriod;
	}
	public String getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	public String getHighRiskExplain() {
		return highRiskExplain;
	}
	public void setHighRiskExplain(String highRiskExplain) {
		this.highRiskExplain = highRiskExplain;
	}
	public String getVendChannels() {
		return vendChannels;
	}
	public void setVendChannels(String vendChannels) {
		this.vendChannels = vendChannels;
	}
}
