package com.cmwa.ecc.business.entity.fundinfo;

import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;


@Alias("fundManagerVo")
public class FundManagerVo {
	/**
	 * 
	 */
private static final long serialVersionUID = 1L;
	
	private String fundId; // 基金代码
	private String fundNm; // 基金名称
	private String fundChineseNm; // 基金名称
	private String fundShortNm; // 基金简称 //Added on 2008-07-09
	private String fundEnglishNm; // 基金英文名称
	private String investType; // 基金英文名称
	private String taNo; // 注册登记机构代码
	private String taNm; // 注册登记机构名称
	private String managerId; // 管理人代码
	private String trusteeId; // 托管人代码
	private String sponsorId; // 发起人代码
	private String managerNm; // 管理人名称
	private String trusteeNm; // 托管人名称
	private String sponsorNm; // 发起人名称
	private String inverstDirect; // 投资方向
	private String motherFundId; // 母基金代码
	private double inMotherFundRate; // 占母基金比例
	private String currencyType; // 货币类型
	private String currencyTypeNm; // 货币类型
	private double nav; // 基金单位净值(最新净值)
	private String navDate; // 净值日期
	private String fundSt; // 基金状态
	private String changeToSign; // 转换选择标志
	private double fundTotalSum; // 基金总份数
	private String fundType; // 基金类别
	private String fundTypeNm; // 基金类别
	private String fundDispType; // 基金显示类别
	private String parentFundid; // 母基金代码
	private String fundRiskLevel; // 基金风险等级 //Added By Lijh on 2008/06/06
	private String fundEvalDate; // 基金评级日期 //Added By Lijh on 2008/09/29
	private double fundPreScale; // 基金预定规模
	private double fundDenomina; // 基金面值
	private String insertTime; // 记录生成时间
	private String updateTime; // 记录更新时间
	//private FundIssueSetDto issueSet; // 基金发行设置
	private String cycletp; // 周期类型
	private int cyclelen; // 周期长度
	
	//private FundLimitDto fundLimit; // 基金限额
	private String minSubAmt; // 最低认购金额
	private String minBidAmt; // 最低申购金额
	private String minRedAmt; // 最低赎回份额

	private String minConvAmt; // 最低转换份额
	private String minRspAmt; // 最低定投金额
	
	private String keepLimit; //最低持有份额 //Added By liaojj on 2014/01/14
	private String fundSize;//产品预订规模 Added By Liaojj on 2014/01/24
	private String ecmaxPurchase;//网上交易最高认申购限额 Added By liaojj on 2014/03/29
	
	private String chargeType; // 收费方式
	private double managerRatio; // 管理费率
	private String indiMaxPurchase; //最大申购额度
	private String navFracNum; // 基金净值小数位数
	private String navFracMode; // 基金净值小数处理方式 1-四舍五入,2-舍位处理
	
	// 下一开放日 add by liaojj 20140421
	private String nextIssueDate;
	// 产品特点add by liaojj 20140421
	private String feature;
	
	private String processor;       //经办人
	private Timestamp checktime;    //复核时间
	private String checker;         //复核人
	private String checkstatus;     //复核状态
	
	private String fundStNm;        //基金状态名称
	private String fundDispTypeNm;  //基金显示类别名称
	private String fundRiskLevelNm; //基金分险级别名称

	private String isUnFund;//参数限制 added by pengl 20140807
	
	private String displayOrder;
	
	private String errcode; // 执行存储过程返回代码
	private String errmsg; // 执行存储过程返回信息


	public FundManagerVo() {
	}
	
	public String getFundStNm() {
		return fundStNm;
	}

	public String getFundChineseNm() {
		return fundChineseNm;
	}

	public String getFundTypeNm() {
		return fundTypeNm;
	}

	public void setFundTypeNm(String fundTypeNm) {
		this.fundTypeNm = fundTypeNm;
	}

	public String getCurrencyTypeNm() {
		return currencyTypeNm;
	}

	public void setCurrencyTypeNm(String currencyTypeNm) {
		this.currencyTypeNm = currencyTypeNm;
	}

	public void setFundChineseNm(String fundChineseNm) {
		this.fundChineseNm = fundChineseNm;
	}

	public String getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}

	public void setFundStNm(String fundStNm) {
		this.fundStNm = fundStNm;
	}

	public String getFundDispTypeNm() {
		return fundDispTypeNm;
	}


	public void setFundDispTypeNm(String fundDispTypeNm) {
		this.fundDispTypeNm = fundDispTypeNm;
	}


	public String getFundRiskLevelNm() {
		return fundRiskLevelNm;
	}


	public void setFundRiskLevelNm(String fundRiskLevelNm) {
		this.fundRiskLevelNm = fundRiskLevelNm;
	}


	public String getParentFundid() {
		return parentFundid;
	}

	public void setParentFundid(String parentFundid) {
		this.parentFundid = parentFundid;
	}

	public String getFundDispType() {
		return fundDispType;
	}

	public void setFundDispType(String fundDispType) {
		this.fundDispType = fundDispType;
	}

	public String getCycletp() {
		return cycletp;
	}

	public void setCycletp(String cycletp) {
		this.cycletp = cycletp;
	}

	public int getCyclelen() {
		return cyclelen;
	}

	public void setCyclelen(int cyclelen) {
		this.cyclelen = cyclelen;
	}

	/**
	 * ��ȡת��ѡ���־
	 * 
	 * @return String
	 */
	public String getChangeToSign() {
		return changeToSign;
	}

	public void setChangeToSign(String changeToSign) {
		this.changeToSign = changeToSign;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return String
	 */
	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	/**
	 * ��ȡִ�д洢���̷��ش���
	 * 
	 * @return String
	 */
	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	/**
	 * ��ȡִ�д洢���̷�����Ϣ
	 * 
	 * @return String
	 */
	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	/**
	 * ��ȡ������ֵ
	 * 
	 * @return double
	 */
	public double getFundDenomina() {
		return fundDenomina;
	}

	public void setFundDenomina(double fundDenomina) {
		this.fundDenomina = fundDenomina;
	}

	/**
	 * ��ȡ����Ӣ������
	 * 
	 * @return String
	 */
	public String getFundEnglishNm() {
		return fundEnglishNm;
	}

	public void setFundEnglishNm(String fundEnglishNm) {
		this.fundEnglishNm = fundEnglishNm;
	}

	/**
	 * ��ȡ�������
	 * 
	 * @return String
	 */
	public String getFundId() {
		return fundId;
	}

	public void setFundId(String fundId) {
		this.fundId = fundId;
	}

	/**
	 * ��ȡע��ǼǴ���
	 * 
	 * @return String
	 */
	public String getTaNo() {
		return taNo;
	}

	public void setTaNo(String taNo) {
		this.taNo = taNo;
	}

	/**
	 * ��ȡ�����޶�DTO
	 * 
	 * @return double
	 */

	/**
	 * ��ȡ��������
	 * 
	 * @return String
	 */
	public String getFundNm() {
		return fundNm;
	}

	public void setFundNm(String fundNm) {
		this.fundNm = fundNm;
	}

	/**
	 * ��ȡ������
	 * 
	 * @return String
	 */
	public String getFundShortNm() {
		return fundShortNm;
	}

	public void setFundShortNm(String fundShortNm) {
		if (fundShortNm == null) {
			fundShortNm = "";
		}
		this.fundShortNm = fundShortNm;
	}

	/**
	 * ��ȡ����Ԥ����ģ
	 * 
	 * @return double
	 */
	public double getFundPreScale() {
		return fundPreScale;
	}

	public void setFundPreScale(double fundPreScale) {
		this.fundPreScale = fundPreScale;
	}

	/**
	 * ��ȡ����״̬
	 * 
	 * @return double
	 */
	public String getFundSt() {
		return fundSt;
	}

	public void setFundSt(String fundSt) {
		this.fundSt = fundSt;
	}

	/**
	 * ��ȡ�����ܷ���
	 * 
	 * @return double
	 */
	public double getFundTotalSum() {
		return fundTotalSum;
	}

	public void setFundTotalSum(double fundTotalSum) {
		this.fundTotalSum = fundTotalSum;
	}

	/**
	 * ��ȡ�������
	 * 
	 * @return String
	 */
	public String getFundType() {
		return fundType;
	}

	public void setFundType(String fundType) {
		this.fundType = fundType;
	}

	/**
	 * ��ȡ������յȼ�
	 * 
	 * @return String
	 */
	public String getFundRiskLevel() {
		return fundRiskLevel;
	}

	public void setFundRiskLevel(String fundRiskLevel) {
		this.fundRiskLevel = fundRiskLevel == null ? "" : fundRiskLevel;
	}

	/**
	 * ��ȡ������������
	 * 
	 * @return String
	 */
	public String getFundEvalDate() {
		return fundEvalDate;
	}

	public void setFundEvalDate(String fundEvalDate) {
		this.fundEvalDate = fundEvalDate;
	}

	/**
	 * ��ȡռĸ�������
	 * 
	 * @return double
	 */
	public double getInMotherFundRate() {
		return inMotherFundRate;
	}

	public void setInMotherFundRate(double inMotherFundRate) {
		this.inMotherFundRate = inMotherFundRate;
	}

	/**
	 * ��ȡͶ�ʷ���
	 * 
	 * @return String
	 */
	public String getInverstDirect() {
		return inverstDirect;
	}

	public void setInverstDirect(String inverstDirect) {
		this.inverstDirect = inverstDirect;
	}

	/**
	 * ��ȡ����������DTO
	 * 
	 * @return FundIssueSetDto
	 */

	/**
	 * ��ȡ�����˴���
	 * 
	 * @return String
	 */
	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	/**
	 * ��ȡ����������
	 * 
	 * @return String
	 */
	public String getManagerNm() {
		return managerNm;
	}

	public void setManagerNm(String managerNm) {
		this.managerNm = managerNm;
	}

	/**
	 * ��ȡĸ�������
	 * 
	 * @return String
	 */
	public String getMotherFundId() {
		return motherFundId;
	}

	public void setMotherFundId(String motherFundId) {
		this.motherFundId = motherFundId;
	}

	/**
	 * ��ȡ����λ��ֵ
	 * 
	 * @return double
	 */
	public double getNav() {
		return nav;
	}

	public void setNav(double nav) {
		this.nav = nav;
	}

	/**
	 * ��ȡ�����˴���
	 * 
	 * @return String
	 */
	public String getSponsorId() {
		return sponsorId;
	}

	public void setSponsorId(String sponsorId) {
		this.sponsorId = sponsorId;
	}

	/**
	 * ��ȡ����������
	 * 
	 * @return String
	 */
	public String getSponsorNm() {
		return sponsorNm;
	}

	public void setSponsorNm(String sponsorNm) {
		this.sponsorNm = sponsorNm;
	}

	/**
	 * ��ȡ�й��˴���
	 * 
	 * @return String
	 */
	public String getTrusteeId() {
		return trusteeId;
	}

	public void setTrusteeId(String trusteeId) {
		this.trusteeId = trusteeId;
	}

	/**
	 * ��ȡ������ֵ
	 * 
	 * @return double
	 */
	public String getTrusteeNm() {
		return trusteeNm;
	}

	public void setTrusteeNm(String trusteeNm) {
		this.trusteeNm = trusteeNm;
	}

	/**
	 * ��ȡע��Ǽǻ�������
	 * 
	 * @return String
	 */
	public String getTaNm() {
		return taNm;
	}

	public void setTaNm(String taNm) {
		this.taNm = taNm;
	}

	/**
	 * ��ȡ��¼����ʱ��
	 * 
	 * @return Timestamp
	 */
	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}

	/**
	 * ��ȡ��¼����ʱ��
	 * 
	 * @return Timestamp
	 */

	/**
	 * ��ȡ��ֵ����
	 * 
	 * @return String
	 */
	public String getNavDate() {
		return navDate;
	}

	public void setNavDate(String navDate) {
		this.navDate = navDate;
	}

	public String getInvestType() {
		return investType;
	}

	public void setInvestType(String investType) {
		this.investType = investType;
	}

	public String getMinSubAmt() {
		return minSubAmt;
	}

	public void setMinSubAmt(String minSubAmt) {
		this.minSubAmt = minSubAmt;
	}

	public String getMinBidAmt() {
		return minBidAmt;
	}

	public void setMinBidAmt(String minBidAmt) {
		this.minBidAmt = minBidAmt;
	}

	public String getMinRedAmt() {
		return minRedAmt;
	}

	public void setMinRedAmt(String minRedAmt) {
		this.minRedAmt = minRedAmt;
	}

	public String getMinConvAmt() {
		return minConvAmt;
	}

	public void setMinConvAmt(String minConvAmt) {
		this.minConvAmt = minConvAmt;
	}

	public String getMinRspAmt() {
		return minRspAmt;
	}

	public void setMinRspAmt(String minRspAmt) {
		this.minRspAmt = minRspAmt;
	}

	public String getKeepLimit() {
		return keepLimit;
	}

	public void setKeepLimit(String keepLimit) {
		this.keepLimit = keepLimit;
	}

	public String getFundSize() {
		return fundSize;
	}

	public void setFundSize(String fundSize) {
		this.fundSize = fundSize;
	}

	public String getEcmaxPurchase() {
		return ecmaxPurchase;
	}

	public void setEcmaxPurchase(String ecmaxPurchase) {
		this.ecmaxPurchase = ecmaxPurchase;
	}

	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public double getManagerRatio() {
		return managerRatio;
	}

	public void setManagerRatio(double managerRatio) {
		this.managerRatio = managerRatio;
	}

	public String getIndiMaxPurchase() {
		return indiMaxPurchase;
	}

	public void setIndiMaxPurchase(String indiMaxPurchase) {
		this.indiMaxPurchase = indiMaxPurchase;
	}

	public String getProcessor() {
		return processor;
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}

	public Timestamp getChecktime() {
		return checktime;
	}

	public void setChecktime(Timestamp checktime) {
		this.checktime = checktime;
	}

	public String getChecker() {
		return checker;
	}

	public void setChecker(String checker) {
		this.checker = checker;
	}

	public String getCheckstatus() {
		return checkstatus;
	}

	public void setCheckstatus(String checkstatus) {
		this.checkstatus = checkstatus;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getIsUnFund() {
		return isUnFund;
	}

	public void setIsUnFund(String isUnFund) {
		this.isUnFund = isUnFund;
	}
	
	
	    private String issueDate; //����������
	    private String issueEndDate; //�����н�������
	    private String issueWay; //�����з�ʽ
	    private double issuePrice; //���м۸�
	    private int purchasePeriod; //�����Ϲ�����(��)
	    private double minHolderShare; //������ͳ��з���(��)
	    private double purchasePrice; //�Ϲ��۸�(Ԫ)
	    private double managerRates; //�������
	    private String raiseHaveInterestBeginDate; //ļ���ڼ�Ϣ��ʼ����
	    private String tradeUnits; //���׵�λ(��)
	    private double minHolderClient; //������ͳ��л���(��)
	    private double minNav; //��ͻ���ֵ(Ԫ)
	    private double hugeRedeemRate; //�޶���ر���
	    private int RedeemToAcctDays; //��ؿ������(��)
	    private int redMelonDays; //�ֺ컮������(��) //add by wangxl 20100118
	    private int subDays;     //���깺�ۿ������
	    private String productTimeLimit;//��Ʒ����  add by wanggang 20140715
	    
	    public int getSubDays() {
			return subDays;
		}

		public void setSubDays(int subDays) {
			this.subDays = subDays;
		}

		public int getRedMelonDays() {
			return redMelonDays;
		}

		public void setRedMelonDays(int redMelonDays) {
			this.redMelonDays = redMelonDays;
		}

		public String getInsertTime() {
			return insertTime;
		}


		private String setUpDate; //�����������
	    private String terminaDate; //������ֹ����
	    private String raiseInterestTreatWay; //ļ������Ϣ����ʽ
	    private String reviewState; //����״̬



	    /**
	     * ��ȡ�޶���ر���
	     * @return double
	     */
	    public double getHugeRedeemRate() {
	        return hugeRedeemRate;
	    }

	    public void setHugeRedeemRate(double hugeRedeemRate) {
	        this.hugeRedeemRate = hugeRedeemRate;
	    }

	    /**
	     * ��ȡ����������
	     * @return String
	     */
	    public String getIssueDate() {
	        return issueDate;
	    }

	    public void setIssueDate(String issueDate) {
	        this.issueDate = issueDate;
	    }

	    /**
	     * ��ȡ�����н�������
	     * @return String
	     */
	    public String getIssueEndDate() {
	        return issueEndDate;
	    }

	    public void setIssueEndDate(String issueEndDate) {
	        this.issueEndDate = issueEndDate;
	    }

	    /**
	     * ��ȡ�����з�ʽ
	     * @return String
	     */
	    public String getIssueWay() {
	        return issueWay;
	    }

	    public void setIssueWay(String issueWay) {
	        this.issueWay = issueWay;
	    }

	    /**
	     * ��ȡ������ͳ��л���(��)
	     * @return double
	     */
	    public double getMinHolderClient() {
	        return minHolderClient;
	    }

	    public void setMinHolderClient(double minHolderClient) {
	        this.minHolderClient = minHolderClient;
	    }

	    /**
	     * ��ȡ������ͳ��з���(��)
	     * @return double
	     */
	    public double getMinHolderShare() {
	        return minHolderShare;
	    }

	    public void setMinHolderShare(double minHolderShare) {
	        this.minHolderShare = minHolderShare;
	    }

	    /**
	     * ��ȡ��ͻ���ֵ
	     * @return double
	     */
	    public double getMinNav() {
	        return minNav;
	    }

	    public void setMinNav(double minNav) {
	        this.minNav = minNav;
	    }

	    /**
	     * ��ȡ�����Ϲ�����(��)
	     * @return int
	     */
	    public int getPurchasePeriod() {
	        return purchasePeriod;
	    }

	    public void setPurchasePeriod(int purchasePeriod) {
	        this.purchasePeriod = purchasePeriod;
	    }

	    /**
	     * ��ȡ�Ϲ��۸�
	     * @return double
	     */
	    public double getPurchasePrice() {
	        return purchasePrice;
	    }

	    public void setPurchasePrice(double purchasePrice) {
	        this.purchasePrice = purchasePrice;
	    }

	    /**
	     * ��ȡļ���ڼ�Ϣ��ʼ����
	     * @return String
	     */
	    public String getRaiseHaveInterestBeginDate() {
	        return raiseHaveInterestBeginDate;
	    }

	    public void setRaiseHaveInterestBeginDate(String raiseHaveInterestBeginDate) {
	        this.raiseHaveInterestBeginDate = raiseHaveInterestBeginDate;
	    }

	    /**
	     * ��ȡļ������Ϣ����ʽ
	     * @return String
	     */
	    public String getRaiseInterestTreatWay() {
	        return raiseInterestTreatWay;
	    }

	    public void setRaiseInterestTreatWay(String raiseInterestTreatWay) {
	        this.raiseInterestTreatWay = raiseInterestTreatWay;
	    }

	    /**
	     * ��ȡ����״̬
	     * @return String
	     */
	    public String getReviewState() {
	        return reviewState;
	    }

	    public void setReviewState(String reviewState) {
	        this.reviewState = reviewState;
	    }

	    /**
	     * ��ȡ�����������
	     * @return String
	     */
	    public String getSetUpDate() {
	        return setUpDate;
	    }

	    public void setSetUpDate(String setUpDate) {
	        this.setUpDate = setUpDate;
	    }

	    /**
	     * ��ȡ������ֹ����
	     * @return String
	     */
	    public String getTerminaDate() {
	        return terminaDate;
	    }

	    public void setTerminaDate(String terminaDate) {
	        this.terminaDate = terminaDate;
	    }

	    /**
	     * ��ȡ���׵�λ
	     * @return String
	     */
	    public String getTradeUnits() {
	        return tradeUnits;
	    }

	    public void setTradeUnits(String tradeUnits) {
	        this.tradeUnits = tradeUnits;
	    }

	    /**
	     * ��ȡ���м۸�
	     * @return double
	     */
	    public double getIssuePrice() {
	        return issuePrice;
	    }

	    public void setIssuePrice(double issuePrice) {
	        this.issuePrice = issuePrice;
	    }

	    /**
	     * ��ȡ��ؿ������
	     * @return int
	     */
	    public int getRedeemToAcctDays() {
	        return RedeemToAcctDays;
	    }

	    public void setRedeemToAcctDays(int redeemToAcctDays) {
	        RedeemToAcctDays = redeemToAcctDays;
	    }

	    /**
	     * ��ȡ�������
	     * @return double
	     */
	    public double getManagerRates() {
	        return managerRates;
	    }

	    public void setManagerRates(double managerRates) {
	        this.managerRates = managerRates;
	    }

	    /**
	     * ��ȡ����ֵС��λ��
	     * @return String
	     */
	    public String getNavFracNum() {
	        return navFracNum;
	    }

	    public void setNavFracNum(String navFracNum) {
	        this.navFracNum = navFracNum;
	    }

	    /**
	     * ��ȡ����ֵС������ʽ 1-��������,2-��λ����
	     * @return String
	     */
	    public String getNavFracMode() {
	        return navFracMode;
	    }

	    public void setNavFracMode(String navFracMode) {
	        this.navFracMode = navFracMode;
	    }

	    public String getProductTimeLimit()
		{
			return productTimeLimit;
		}

		public void setProductTimeLimit(String productTimeLimit)
		{
			this.productTimeLimit = productTimeLimit;
		}

		public String toString() {
	        StringBuffer strb = new StringBuffer();
	        strb.append("fundId:" + fundId + "\r\n");
	        strb.append("issueDate:" + issueDate + "\r\n");
	        strb.append("issueEndDate:" + issueEndDate + "\r\n");
	        strb.append("issueWay:" + issueWay + "\r\n");
	        strb.append("purchasePeriod:" + String.valueOf(purchasePeriod) + "\r\n");
	        strb.append("minHolderShare:" + String.valueOf(minHolderShare) + "\r\n");
	        strb.append("purchasePrice:" + String.valueOf(purchasePrice) + "\r\n");
	        strb.append("raiseHaveInterestBeginDate:" + raiseHaveInterestBeginDate + "\r\n");
	        strb.append("tradeUnits:" + tradeUnits + "\r\n");
	        strb.append("minHolderClient:" + String.valueOf(minHolderClient) + "\r\n");
	        strb.append("minNav:" + String.valueOf(minNav) + "\r\n");
	        strb.append("hugeRedeemRate:" + String.valueOf(hugeRedeemRate) + "\r\n");
	        strb.append("setUpDate:" + setUpDate + "\r\n");
	        strb.append("terminaDate:" + terminaDate + "\r\n");
	        strb.append("raiseInterestTreatWay:" + raiseInterestTreatWay + "\r\n");
	        strb.append("reviewState:" + reviewState + "\r\n");
	        return strb.toString();
	    }

		public String getNextIssueDate() {
			return nextIssueDate;
		}

		public void setNextIssueDate(String nextIssueDate) {
			this.nextIssueDate = nextIssueDate;
		}

		public String getFeature() {
			return feature;
		}

		public void setFeature(String feature) {
			this.feature = feature;
		}

		
		private double personFirstSubscribeMinAmount;//�����״��Ϲ���ͽ��(Ԫ)
		private double personAdditionSubscribeMinAmount;//����׷���Ϲ���ͽ��(Ԫ)
		private double personFirstPurchaseMinAmount;//�����״��깺��ͽ��(Ԫ)
		private double personAdditionPurchaseMinAmount;//����׷���깺��ͽ��(Ԫ)
		private double personRegularPurchaseMinAmount;//���˶��ڶ����깺��ͽ��(Ԫ)
		private double personBuyMaxShare;//�����Ϲ�/�깺��߷���(��)
		private double personBuyMaxAmount;//�����Ϲ�/�깺��߽��(Ԫ)
		private double personBuyShareUnits;//�����Ϲ�/�깺������λ(��)
		private double personBuyAmountUnits;//�����Ϲ�/�깺��λ(Ԫ)

		private double corporateFirstSubscribeMinAmount;//�����״��Ϲ���ͽ��(Ԫ)
		private double corporateAdditionSubscribeMinAmount;//����׷���Ϲ���ͽ��(Ԫ)
		private double corporateFirstPurchaseMinAmount;//�����״��깺��ͽ��(Ԫ)
		private double corporateAdditionPurchaseMinAmount;//����׷���깺��ͽ��(Ԫ)
		private double corporateRegularPurchaseMinAmount;//���˶��ڶ����깺��ͽ��(Ԫ)
		private double corporateBuyMaxShare;//�����Ϲ�/�깺��߷���(��)
		private double corporateBuyMaxAmount;//�����Ϲ�/�깺��߽��(Ԫ)
		private double corporateBuyShareUnits;//�����Ϲ�/�깺������λ(��)
		private double corporateBuyAmountUnits;//�����Ϲ�/�깺��λ(Ԫ)

		private double fundConversionMinShare;//����ת����ͷݶ�(��)
		private double fundTrusteeMinShare;//����ת�й���ͷݶ�(��)
		private double fundRedeemMinShare;//���������ͷݶ�(��)
		private double fundRedeemMaxShare;//���������߷ݶ�(��)
		private double amountHavaMinShare;//�ʻ�(����/����)��ͳ��зݶ�(��)
		private double amountHavaMaxShare;//�ʻ�(����/����)��߳��зݶ�(��)
		private double amountHavaMaxRate;//�ʻ�(����/����)��߳��б���
		private String regularPurchaseDate;//�����ڶ����깺����
		private String effectiveDate;//������Ч����
		private double effectiveDays;//��Ч����(��)

		//���ݹ�˾FUNDINFO,FUNDINFOEX���ű���ı���
		private double minPurAmt;//����깺���
		private double minRedShare;//�����طݶ�
		private double minConvShare;//���ת���ݶ�
		private double minRegAmt;//��Ͷ�Ͷ���
		private double inconvertinbyinst; //���ת���޶�  add kouyd by 20090828

		/**
		 *
		 * ��ȡ���ת���޶�
		 * @return double
		 */
		
		public double getInconvertinbyinst() {
			return inconvertinbyinst;
		}

		public void setInconvertinbyinst(double inconvertinbyinst) {
			this.inconvertinbyinst = inconvertinbyinst;
		}
		/**
		 * ��ȡ�ʻ�(����/����)��߳��б���
		 * @return double
		 */
		public double getAmountHavaMaxRate() {
			return amountHavaMaxRate;
		}

		public void setAmountHavaMaxRate(double amountHavaMaxRate) {
			this.amountHavaMaxRate = amountHavaMaxRate;
		}

		/**
		 * ��ȡ�ʻ�(����/����)��߳��зݶ�(��)
		 * @return double
		 */
		public double getAmountHavaMaxShare() {
			return amountHavaMaxShare;
		}

		public void setAmountHavaMaxShare(double amountHavaMaxShare) {
			this.amountHavaMaxShare = amountHavaMaxShare;
		}

		/**
		 * ��ȡ�ʻ�(����/����)��ͳ��зݶ�(��)
		 * @return double
		 */
		public double getAmountHavaMinShare() {
			return amountHavaMinShare;
		}

		public void setAmountHavaMinShare(double amountHavaMinShare) {
			this.amountHavaMinShare = amountHavaMinShare;
		}

		/**
		 * ��ȡ����׷���깺��ͽ��
		 * @return double
		 */
		public double getCorporateAdditionPurchaseMinAmount() {
			return corporateAdditionPurchaseMinAmount;
		}

		public void setCorporateAdditionPurchaseMinAmount(
				double corporateAdditionPurchaseMinAmount) {
			this.corporateAdditionPurchaseMinAmount = corporateAdditionPurchaseMinAmount;
		}

		/**
		 * ��ȡ����׷���Ϲ���ͽ��
		 * @return double
		 */
		public double getCorporateAdditionSubscribeMinAmount() {
			return corporateAdditionSubscribeMinAmount;
		}

		public void setCorporateAdditionSubscribeMinAmount(
				double corporateAdditionSubscribeMinAmount) {
			this.corporateAdditionSubscribeMinAmount = corporateAdditionSubscribeMinAmount;
		}

		/**
		 * ��ȡ�����Ϲ�/�깺��λ(Ԫ)
		 * @return double
		 */
		public double getCorporateBuyAmountUnits() {
			return corporateBuyAmountUnits;
		}

		public void setCorporateBuyAmountUnits(double corporateBuyAmountUnits) {
			this.corporateBuyAmountUnits = corporateBuyAmountUnits;
		}

		/**
		 * ��ȡ�����Ϲ�/�깺��߽��(Ԫ)
		 * @return double
		 */
		public double getCorporateBuyMaxAmount() {
			return corporateBuyMaxAmount;
		}

		public void setCorporateBuyMaxAmount(double corporateBuyMaxAmount) {
			this.corporateBuyMaxAmount = corporateBuyMaxAmount;
		}

		/**
		 * ��ȡ�����Ϲ�/�깺��߷���(��)
		 * @return double
		 */
		public double getCorporateBuyMaxShare() {
			return corporateBuyMaxShare;
		}

		public void setCorporateBuyMaxShare(double corporateBuyMaxShare) {
			this.corporateBuyMaxShare = corporateBuyMaxShare;
		}

		/**
		 * ��ȡ�����Ϲ�/�깺������λ(��)
		 * @return double
		 */
		public double getCorporateBuyShareUnits() {
			return corporateBuyShareUnits;
		}

		public void setCorporateBuyShareUnits(double corporateBuyShareUnits) {
			this.corporateBuyShareUnits = corporateBuyShareUnits;
		}

		/**
		 * ��ȡ�����״��깺��ͽ��
		 * @return double
		 */
		public double getCorporateFirstPurchaseMinAmount() {
			return corporateFirstPurchaseMinAmount;
		}

		public void setCorporateFirstPurchaseMinAmount(
				double corporateFirstPurchaseMinAmount) {
			this.corporateFirstPurchaseMinAmount = corporateFirstPurchaseMinAmount;
		}

		/**
		 * ��ȡ�����״��Ϲ���ͽ��
		 * @return double
		 */
		public double getCorporateFirstSubscribeMinAmount() {
			return corporateFirstSubscribeMinAmount;
		}

		public void setCorporateFirstSubscribeMinAmount(
				double corporateFirstSubscribeMinAmount) {
			this.corporateFirstSubscribeMinAmount = corporateFirstSubscribeMinAmount;
		}

		/**
		 * ��ȡ���˶��ڶ����깺��ͽ��
		 * @return double
		 */
		public double getCorporateRegularPurchaseMinAmount() {
			return corporateRegularPurchaseMinAmount;
		}

		public void setCorporateRegularPurchaseMinAmount(
				double corporateRegularPurchaseMinAmount) {
			this.corporateRegularPurchaseMinAmount = corporateRegularPurchaseMinAmount;
		}

		/**
		 * ��ȡ������Ч����
		 * @return String
		 */
		public String getEffectiveDate() {
			return effectiveDate;
		}

		public void setEffectiveDate(String effectiveDate) {
			this.effectiveDate = effectiveDate;
		}

		/**
		 * ��ȡ��Ч����(��)
		 * @return double
		 */
		public double getEffectiveDays() {
			return effectiveDays;
		}

		public void setEffectiveDays(double effectiveDays) {
			this.effectiveDays = effectiveDays;
		}


		/**
		 * ��ȡ����ת����ͷݶ�(��)
		 * @return double
		 */
		public double getFundConversionMinShare() {
			return fundConversionMinShare;
		}

		public void setFundConversionMinShare(double fundConversionMinShare) {
			this.fundConversionMinShare = fundConversionMinShare;
		}


		/**
		 * ��ȡ���������߷ݶ�(��)
		 * @return double
		 */
		public double getFundRedeemMaxShare() {
			return fundRedeemMaxShare;
		}

		public void setFundRedeemMaxShare(double fundRedeemMaxShare) {
			this.fundRedeemMaxShare = fundRedeemMaxShare;
		}

		/**
		 * ��ȡ���������ͷݶ�(��)
		 * @return double
		 */
		public double getFundRedeemMinShare() {
			return fundRedeemMinShare;
		}

		public void setFundRedeemMinShare(double fundRedeemMinShare) {
			this.fundRedeemMinShare = fundRedeemMinShare;
		}

		/**
		 * ��ȡ����ת�й���ͷݶ�(��)
		 * @return double
		 */
		public double getFundTrusteeMinShare() {
			return fundTrusteeMinShare;
		}

		public void setFundTrusteeMinShare(double fundTrusteeMinShare) {
			this.fundTrusteeMinShare = fundTrusteeMinShare;
		}

		/**
		 * ��ȡ����׷���깺��ͽ��
		 * @return double
		 */
		public double getPersonAdditionPurchaseMinAmount() {
			return personAdditionPurchaseMinAmount;
		}

		public void setPersonAdditionPurchaseMinAmount(
				double personAdditionPurchaseMinAmount) {
			this.personAdditionPurchaseMinAmount = personAdditionPurchaseMinAmount;
		}

		/**
		 * ��ȡ����׷���Ϲ���ͽ��
		 * @return double
		 */
		public double getPersonAdditionSubscribeMinAmount() {
			return personAdditionSubscribeMinAmount;
		}

		public void setPersonAdditionSubscribeMinAmount(
				double personAdditionSubscribeMinAmount) {
			this.personAdditionSubscribeMinAmount = personAdditionSubscribeMinAmount;
		}

		/**
		 * ��ȡ�����Ϲ�/�깺��λ(Ԫ)
		 * @return double
		 */
		public double getPersonBuyAmountUnits() {
			return personBuyAmountUnits;
		}

		public void setPersonBuyAmountUnits(double personBuyAmountUnits) {
			this.personBuyAmountUnits = personBuyAmountUnits;
		}

		/**
		 * ��ȡ�����Ϲ�/�깺��߽��(Ԫ)
		 * @return double
		 */
		public double getPersonBuyMaxAmount() {
			return personBuyMaxAmount;
		}

		public void setPersonBuyMaxAmount(double personBuyMaxAmount) {
			this.personBuyMaxAmount = personBuyMaxAmount;
		}

		/**
		 * ��ȡ�����Ϲ�/�깺��߷���(��)
		 * @return double
		 */
		public double getPersonBuyMaxShare() {
			return personBuyMaxShare;
		}

		public void setPersonBuyMaxShare(double personBuyMaxShare) {
			this.personBuyMaxShare = personBuyMaxShare;
		}

		/**
		 * ��ȡ�����Ϲ�/�깺������λ(��)
		 * @return double
		 */
		public double getPersonBuyShareUnits() {
			return personBuyShareUnits;
		}

		public void setPersonBuyShareUnits(double personBuyShareUnits) {
			this.personBuyShareUnits = personBuyShareUnits;
		}

		/**
		 * ��ȡ�����״��깺��ͽ��
		 * @return double
		 */
		public double getPersonFirstPurchaseMinAmount() {
			return personFirstPurchaseMinAmount;
		}

		public void setPersonFirstPurchaseMinAmount(double personFirstPurchaseMinAmount) {
			this.personFirstPurchaseMinAmount = personFirstPurchaseMinAmount;
		}

		/**
		 * ��ȡ�����״��Ϲ���ͽ��
		 * @return double
		 */
		public double getPersonFirstSubscribeMinAmount() {
			return personFirstSubscribeMinAmount;
		}

		public void setPersonFirstSubscribeMinAmount(
				double personFirstSubscribeMinAmount) {
			this.personFirstSubscribeMinAmount = personFirstSubscribeMinAmount;
		}

		/**
		 * ��ȡ���˶��ڶ����깺��ͽ��
		 * @return double
		 */
		public double getPersonRegularPurchaseMinAmount() {
			return personRegularPurchaseMinAmount;
		}

		public void setPersonRegularPurchaseMinAmount(
				double personRegularPurchaseMinAmount) {
			this.personRegularPurchaseMinAmount = personRegularPurchaseMinAmount;
		}

		/**
		 * ��ȡ�����ڶ����깺����
		 * @return String
		 */
		public String getRegularPurchaseDate() {
			return regularPurchaseDate;
		}

		public void setRegularPurchaseDate(String regularPurchaseDate) {
			this.regularPurchaseDate = regularPurchaseDate;
		}

		/**
		 * ��ȡ���ת���ݶ�
		 * @return double
		 */
		public double getMinConvShare() {
			return minConvShare;
		}

		public void setMinConvShare(double minConvShare) {
			this.minConvShare = minConvShare;
		}

		/**
		 * ��ȡ����깺���
		 * @return double
		 */
		public double getMinPurAmt() {
			return minPurAmt;
		}

		public void setMinPurAmt(double minPurAmt) {
			this.minPurAmt = minPurAmt;
		}

		/**
		 * ��ȡ�����طݶ�
		 * @return double
		 */
		public double getMinRedShare() {
			return minRedShare;
		}

		public void setMinRedShare(double minRedShare) {
			this.minRedShare = minRedShare;
		}

		/**
		 * ��ȡ��Ͷ�Ͷ���
		 * @return double
		 */
		public double getMinRegAmt() {
			return minRegAmt;
		}

		public void setMinRegAmt(double minRegAmt) {
			this.minRegAmt = minRegAmt;
		}
}
