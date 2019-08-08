package com.cmwa.ecc.business.service.impl.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cmwa.ec.base.util.Encrypt;
import com.cmwa.ecc.business.dao.account.QueryDao;
import com.cmwa.ecc.business.dao.common.CommonDao;
import com.cmwa.ecc.business.dao.fundinfo.FundInfoDao;
import com.cmwa.ecc.business.entity.accountManger.AccountDto;
import com.cmwa.ecc.business.entity.accountManger.BakCustDto;
import com.cmwa.ecc.business.entity.accountManger.BrokerDto;
import com.cmwa.ecc.business.entity.accountManger.BrokerInfoDto;
import com.cmwa.ecc.business.entity.accountManger.CapitalCheckDto;
import com.cmwa.ecc.business.entity.accountManger.ContactDto;
import com.cmwa.ecc.business.entity.accountManger.FundBalanceDto;
import com.cmwa.ecc.business.entity.accountManger.OpenAccountDto;
import com.cmwa.ecc.business.entity.accountManger.RiskLevelDto;
import com.cmwa.ecc.business.entity.accountManger.SeatDto;
import com.cmwa.ecc.business.entity.accountManger.ValidateDto;
import com.cmwa.ecc.business.entity.bank.BankBnkbaseVo;
import com.cmwa.ecc.business.entity.fundinfo.FundInfoVo;
import com.cmwa.ecc.business.entity.paramvo.ParameterVo;
import com.cmwa.ecc.business.entity.trade.TradeCheckDto;
import com.cmwa.ecc.business.entity.trade.TradeDto;
import com.cmwa.ecc.business.service.account.QueryManager;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.ParameterConstant;
import com.cmwa.ecc.business.utils.SearchParam;

@Service
public class QueryManagerImpl implements QueryManager{
	
	private static final Log log = LogFactory.getLog(QueryManagerImpl.class);

	@Autowired
	private QueryDao queryDao;
	
	@Autowired
	private FundInfoDao fundInfoDao;
	
	@Autowired
	private CommonDao commonDao;
	
	@Override
	public OpenAccountDto queryAccoByCust(String custNo) {
		OpenAccountDto openAccountDto = new OpenAccountDto();
		try {
			openAccountDto = queryDao.queryAccoByCust(custNo);
		} catch (Exception e) {
			log.error("通过客户号查询客户资料异常",e);
		}
		return openAccountDto;
	}

	@Override
	public OpenAccountDto fundaccoQry(String tradeacco, String fundacco,
			String accountType) {
		OpenAccountDto resultDto = new OpenAccountDto();
        try {
        	OpenAccountDto dto = new OpenAccountDto();
        	dto.setTradeacco(tradeacco);
        	dto.setFundacc(fundacco);
        	dto.setAccountType(accountType);
        	resultDto = queryDao.fundaccoQry(dto);
        } catch (Exception e) {
            log.error("(DS-Exception)异常：",e);
        }
        return resultDto;
	}

	@Override
	public OpenAccountDto accountQryByAcco(String tradeacco, String fundacco,
			String accountType) {
		OpenAccountDto resultDto = new OpenAccountDto();
        try {
        	OpenAccountDto dto = new OpenAccountDto();
        	dto.setTradeacco(tradeacco);
        	dto.setFundacc(fundacco);
        	dto.setAccountType(accountType);
        	resultDto = queryDao.accountQryByAcco(dto);
        } catch (Exception e) {
            log.error("(DS-Exception)异常：", e);
        }
        return resultDto;
	}

	@Override
	public OpenAccountDto bankQryByAcco(String tradeacco, String fundacco,
			String accountType) {
		OpenAccountDto resultDto = new OpenAccountDto();
        try {
        	OpenAccountDto dto = new OpenAccountDto();
        	dto.setTradeacco(tradeacco);
        	dto.setFundacc(fundacco);
        	dto.setAccountType(accountType);
        	resultDto = queryDao.bankQryByAcco(dto);
        } catch (Exception e) {
            log.error("(DS-Exception)异常：",e);
        }
        return resultDto;
	}

	@Override
	public List getReceiveAccoList() {
		List resultList = null;
        try {
        	resultList = queryDao.getReceiveAccoList();
        } catch (Exception e) {
            log.error("(DS-Exception)异常：" ,e);
        }
        return resultList;
	}

	@Override
	public Page<TradeCheckDto> tradeCheckQryListPage(SearchParam sp) {
		List<TradeCheckDto> resultList = null;
        try {
        	SearchParam sParam = new SearchParam();
        	sParam.setSp(new HashMap<String,Object>());
        	sParam.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
        	sParam.getSp().put("allPmky", "TRADEBOPERATOR");
        	List<ParameterVo> paradto = commonDao.getParameterListPage(sParam);
    		if(paradto != null && paradto.size() > 0){
    			sp.getSp().put("batchoperatorId",paradto.get(0).getPmco());
    		}
        	resultList = queryDao.tradeCheckQryListPage(sp);
        } catch (Exception e) {
            log.error("(DS-Exception)异常：", e);
        }
        return Page.create(resultList, sp.getStart(), sp.getLimit(), sp.getTotal() , false);
	}
	
	@Override
	public Page<TradeCheckDto> queryBatchListPage(SearchParam sp) {
		List<TradeCheckDto> resultList = null;
		try {
			resultList = queryDao.queryBatchListPage(sp);
			for (int i = 0; i < resultList.size(); i++) {
				TradeCheckDto tradeCheckDto = resultList.get(i);
				tradeCheckDto.setSerialno(i+"");
			}
		} catch (Exception e) {
			log.error("(DS-Exception)异常：",e);
		}
		return Page.create(resultList, sp.getStart(), sp.getLimit(), sp.getTotal() , false);
	}
	
	@Override
	public TradeCheckDto tradeCheckDetailQry(String serialno) {
		TradeCheckDto resultDto = new TradeCheckDto();
        try {
        	resultDto = queryDao.tradeCheckDetailQry(serialno);
        } catch (Exception e) {
            log.error("(DS-Exception)异常：", e);
        }
        return resultDto;
	}

	@Override
	public List<CapitalCheckDto> capitalCheckQry(String tradeacco,
			String capitalno, String capitaltype, String checkst,
			String begindate, String enddate, String operatorId,
			String operatorType) {
		List<CapitalCheckDto> resultList = null;
        try {
        	CapitalCheckDto dto = new CapitalCheckDto();
        	dto.setTradeacco(tradeacco);
        	dto.setCapitalno(capitalno);
        	dto.setCapitaltype(capitaltype);
        	dto.setCheckst(checkst);
        	dto.setBegindate(begindate);
        	dto.setEnddate(enddate);
        	dto.setOperatorId(operatorId);
        	dto.setOperatorType(operatorType);
        	resultList = queryDao.capitalCheckQry(null);
        } catch (Exception e) {
            log.error("(DS-Exception)异常：",e);
        }
        return resultList;
	}

	@Override
	public CapitalCheckDto capitalCheckDetailQry(String capitalno) {
		 CapitalCheckDto resultDto = new CapitalCheckDto();
	        try {
	        	resultDto = queryDao.capitalCheckDetailQry(capitalno);
	        } catch (Exception e) {
	            log.error("(DS-Exception)异常：",e);
	        }
	        return resultDto;
	}

	@Override
	public SearchParam queryPermission(SearchParam sp) {
        try {
        	queryDao.queryPermission(sp);
        } catch (Exception e) {
            log.error("(DS-Exception)异常：", e);
        }
        return sp;
	}

	@Override
	public SearchParam checkPermission(SearchParam sp) {
        try {
        	//加密
           Encrypt encrypt = new Encrypt();
           String checkno = sp.getSp().get("checkno").toString();
           String encryptPassword = encrypt.passwordEncrypt(sp.getSp().get("checkpwd").toString());
           if(encryptPassword == null || encryptPassword.length() == 0){
        	   encryptPassword = "";
           }
           sp.getSp().put("checkno", checkno);
           sp.getSp().put("checkpwd", encryptPassword);
           queryDao.checkPermission(sp);
        } catch (Exception e) {
            log.error("(DS-Exception)异常：", e);
        }
        return sp;
	}

	@Override
	public List<CapitalCheckDto> storeQry(String tradeacco, String fundacco,
			String serialno, String checkst, String apdt) {
		 List<CapitalCheckDto> resultList = null;
	        try {
	        	CapitalCheckDto dto = new CapitalCheckDto();
	        	dto.setTradeacco(tradeacco);
	        	dto.setFundacco(fundacco);
	        	dto.setSerialno(serialno);
	        	dto.setCheckst(checkst);
	        	dto.setApdt(apdt);
	        	resultList = queryDao.storeQry(dto);
	        } catch (Exception e) {
	            log.error("(DS-Exception)异常：",e);
	        }
	        return resultList;
	}

	@Override
	public FundBalanceDto getFundBalanceInfo(FundBalanceDto dto) {
		FundBalanceDto resultDto = new FundBalanceDto();
        try {
        	resultDto = queryDao.getFundBalanceInfo(dto);
        } catch (Exception e) {
            log.error("(DS-Exception)异常：",e);
        }
        return resultDto;
	}

	@Override
	public List<ValidateDto> validateQry(String tradeacco, String fundacco,
			String idtp, String idno, String idnm, String begindate,
			String enddate, String invtp, String operatorType) {
		List<ValidateDto> resultList = null;
        try {
        	ValidateDto dto = new ValidateDto();
        	dto.setTradeacco(tradeacco);
        	dto.setFundacco(fundacco);
        	dto.setIdtp(idtp);
        	dto.setIdno(idno);
        	dto.setIdnm(idnm);
        	dto.setBegindate(begindate);
        	dto.setEnddate(enddate);
        	dto.setInvtp(invtp);
        	dto.setOperatorType(operatorType);
        	resultList = queryDao.validateQry(dto);
        } catch (Exception e) {
            log.error("(DS-Exception)异常：",e);
        }
        return resultList;
	}

	@Override
	public List<BankBnkbaseVo> getDSBanksList() {
		List<BankBnkbaseVo> resultList = null;
        try {
        	resultList = queryDao.getDSBanksList();
        } catch (Exception e) {
            log.error("(DS-Exception)异常：",e);
        }
        return resultList;
	}

	@Override
	public Page<TradeDto> cancelQryListPage(SearchParam sp) {
		 List<TradeDto> resultList = null;
	        try {
	        	resultList = queryDao.cancelQryListPage(sp);
	        } catch (Exception e) {
	            log.error("(DS-Exception)异常：", e);
	        }
	        return Page.create(resultList, sp.getStart(), sp.getLimit(), sp.getTotal() , false);
	}

	@Override
	public List<BrokerInfoDto> brokerModifyQry(String tradeacco,
			String fundacco, String custnm, String cltmno, String custno) {
		List<BrokerInfoDto> resultList = null;
        try {
        	BrokerInfoDto dto = new BrokerInfoDto();
        	dto.setTradeacco(tradeacco);
        	dto.setFundacco(fundacco);
        	dto.setCustnm(custnm);
        	dto.setCltmno(cltmno);
        	dto.setCustno(custno);
        	resultList = queryDao.brokerModifyQry(dto);
        } catch (Exception e) {
            log.error("(DS-Exception)异常：",e);
        }
        return resultList;
	}

	@Override
	public List<TradeDto> cancelTradeDetailQry(String tradeacco, String fundacco) {
		 List<TradeDto> resultList = null;
	        try {
	        	resultList = queryDao.cancelTradeDetailQry(tradeacco,fundacco);
	        } catch (Exception e) {
	            log.error("(DS-Exception)异常：",e);
	        }
	        return resultList;
	}

	@Override
	public List<BrokerInfoDto> brokerQryByCustno(String custno) {
		List<BrokerInfoDto> resultList = null;
        try {
        	resultList = queryDao.brokerQryByCustno(custno);
        } catch (Exception e) {
            log.error("(DS-Exception)异常：",e);
        }
        return resultList;
	}

	@Override
	public Page<TradeDto> melonListQry(SearchParam sp) {
			List<TradeDto> resultList = null;
        try {
        	queryDao.melonListQry(sp);
        	resultList = (List<TradeDto>) sp.getSp().get("tradeList");
        	if (resultList != null && resultList.size() > 0) {
				for (int i = 0; i < resultList.size(); i++) {
					TradeDto tradeDto = resultList.get(i);
					//投资者类别
					SearchParam invtpSp = new SearchParam();
					invtpSp.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
					invtpSp.getSp().put("allPmky", ParameterConstant.PARAM_PMKY_INVTP);
					invtpSp.getSp().put("Qpmco", tradeDto.getInvtp());
					List<ParameterVo> invtpArray = commonDao.getParameterListPage(invtpSp);
					tradeDto.setInvtp(invtpArray.get(0).getPmnm());
				}
			}
    		sp.setStart((sp.getPageNo() - Page.FIRST_PAGE) * sp.getLimit());
    		sp.setEnd(sp.getStart() + sp.getLimit());
    		sp.setTotal(resultList.size());
        	
        } catch (Exception e) {
            log.error("(DS-Exception)异常：", e);
        }
        return Page.create(resultList, sp.getStart(), sp.getLimit(),sp.getTotal());
	}

	@Override
	public List<TradeDto> capitalCancelQry(String tradeacco, String fundacco,
			String opid, String firstCustGroup, String secondCustGroup) {
		List<TradeDto> resultList = null;
        try {
        	TradeDto dto =new TradeDto();
        	dto.setTradeacco(tradeacco);
        	dto.setFundacco(fundacco);
        	dto.setFirstCustGroup(firstCustGroup);
        	dto.setSecondCustGroup(secondCustGroup);
        	resultList = queryDao.capitalCancelQry(dto);
        } catch (Exception e) {
            log.error("(DS-Exception)异常：" , e);
        }
        return resultList;
	}

	@Override
	public List<BrokerDto> brokerQry(String brokerno) {
		List<BrokerDto> resultList = null;
        try {
        	resultList = queryDao.brokerQry(brokerno);
        } catch (Exception e) {
            log.error("(DS-Exception)异常：", e);
        }
        return resultList;
	}

	@Override
	public List<RiskLevelDto> risklevelQry(String tradeacco, String fundacco,
			String risklevel, String firstGroup, String secondGroup) {
		List<RiskLevelDto> resultList = null;
        try {
        	RiskLevelDto dto = new RiskLevelDto();
        	dto.setTradeacco(tradeacco);
        	dto.setFundacco(fundacco);
        	dto.setRisklevel(risklevel);
        	dto.setFirstCustGroup(firstGroup);
        	dto.setSecondCustGroup(secondGroup);
        	resultList = queryDao.risklevelQry(dto);
        } catch (Exception e) {
            log.error("(DS-Exception)异常：",e);
        }
        return resultList;
	}

	@Override
	public List<BrokerDto> brokerCheckQry(String brokerno, String operatorId) {
		List<BrokerDto> resultList = null;
        try {
        	resultList = queryDao.brokerCheckQry(brokerno,operatorId);
        } catch (Exception e) {
            log.error("(DS-Exception)异常：",e);
        }
        return resultList;
	}

	@Override
	public List<TradeDto> accountCancelQry(String tradeacco, String fundacco,
			String operatorid) {
		List<TradeDto> resultList = null;
        try {
        	resultList = queryDao.accountCancelQry(tradeacco,fundacco,operatorid);
        } catch (Exception e) {
            log.error("(DS-Exception)异常：",e);
        }
        return resultList;
	}

	@Override
	public TradeDto accountCancelDetailQry(String serialno, String operatorid) {
		TradeDto dto = null;
        try {
        	dto = queryDao.accountCancelDetailQry(serialno,operatorid);
        } catch (Exception e) {
            log.error("(DS-Exception)异常：",e);
        }
        return dto;
	}

	@Override
	public List<TradeDto> accountCheckQry(String tradeacco, String fundacco,
			String dsapkind, String checkst, String type, String operatorid) {
		List<TradeDto> resultList = null;
		try {
			TradeDto dto = new TradeDto();
			dto.setTradeacco(tradeacco);
			dto.setFundacco(fundacco);
			dto.setDsapkind(dsapkind);
			dto.setCheckst(checkst);
			dto.setOperatorType(type);
			dto.setOperatorId(operatorid);
			resultList = queryDao.accountCheckQry(dto);
		} catch (Exception e) {
		    log.error("(DS-Exception)异常：", e);
		}
			return resultList;
		}

	@Override
	public OpenAccountDto accountCheckDetailQry(String serialno) {
		OpenAccountDto resultDto = new OpenAccountDto();
		try {
			resultDto = queryDao.accountCheckDetailQry(serialno);
		} catch (Exception e) {
			log.error("(DS-Exception)异常：",e);
		}
		return resultDto;
	}

	@Override
	public List<OpenAccountDto> accountModifyQry(String custType,
			String fundacco, String option, String custsimpnm,
			String instrepcode) {
	 	List<OpenAccountDto> resultList = null;
        try {
        	OpenAccountDto dto = new OpenAccountDto();
        	dto.setCustType(custType);
        	dto.setFundacco(fundacco);
        	dto.setOption(option);
        	dto.setCustsimpnm(custsimpnm);
        	dto.setInstrepcode(instrepcode);
        	resultList = queryDao.accountModifyQry(dto);
        } catch (Exception e) {
            log.error("(DS-Exception)异常：",e);
        }
        return resultList;
	}

	@Override
	public OpenAccountDto tradeaccoQry(String custno, String tradeacco,
			String fundacco) {
	 	OpenAccountDto resultDto = new OpenAccountDto();
	    try {
	    	resultDto = queryDao.tradeaccoQry(custno , tradeacco, fundacco);
	    } catch (Exception e) {
	        log.error("(DS-Exception)异常：",e);
	    }
	    return resultDto;
	}

	@Override
	public OpenAccountDto tradeaccoQry(String tradeacco, String fundacco) {
		OpenAccountDto resultDto = new OpenAccountDto();
        try {
        	SearchParam sp = new SearchParam();
        	sp.getSp().put("tradeacco", tradeacco);
        	sp.getSp().put("fundacco", fundacco);
        	queryDao.tradeaccoQry(sp);
        	
        	resultDto.setTradeacco((String)sp.getSp().get("tradeacco"));
        	resultDto.setFundacco((String)sp.getSp().get("fundacco"));
        	resultDto.setCustno((String)sp.getSp().get("custno"));
            resultDto.setInvnm((String)sp.getSp().get("invnm"));
            resultDto.setInvtp((String)sp.getSp().get("invtp"));
            resultDto.setIdtp((String)sp.getSp().get("idtp"));
            resultDto.setIdno((String)sp.getSp().get("idno"));
            resultDto.setCustrisklevl((String)sp.getSp().get("custrisklevl"));
            resultDto.setEmail((String)sp.getSp().get("email"));
            resultDto.setMobile((String)sp.getSp().get("email"));
            resultDto.setInstrepvalidate((String)sp.getSp().get("instrepvalidate"));
            resultDto.setTrustType4((String)sp.getSp().get("trusttype4"));
            resultDto.setTrustType1((String)sp.getSp().get("trusttype1"));
            resultDto.setTrustType2((String)sp.getSp().get("trustType2"));
            resultDto.setTrustType3((String)sp.getSp().get("trustType3"));
            resultDto.setTrustType6((String)sp.getSp().get("trustType6"));
            resultDto.setIdvalidate((String)sp.getSp().get("tdvalidate"));
            resultDto.setBnkNo((String)sp.getSp().get("bnkNo"));//银行编号
            resultDto.setOpenName((String)sp.getSp().get("openName"));//开户银行
            resultDto.setBankAcco((String)sp.getSp().get("bankAcco"));//银行账号
            resultDto.setBankAccoNm((String)sp.getSp().get("bankAccoNm"));//银行账户名
            resultDto.setTano((String)sp.getSp().get("tano"));//TA代码
            
            //added 20130517 新增两个输出参数 风险评估日期，风险评估时间
            resultDto.setCustriskdate((String)sp.getSp().get("custriskdate"));
            resultDto.setCustrisktime((String)sp.getSp().get("custrisktime"));
            
            resultDto.setInvprtp((String)sp.getSp().get("invprtp"));
            resultDto.setSpecriskLevel((String)sp.getSp().get("specriskLevel"));
            
            resultDto.setErrcode((String)sp.getSp().get("errcode"));
            resultDto.setErrmsg((String)sp.getSp().get("errmsg"));
        	
        	List<ContactDto> contList = (List<ContactDto>) sp.getSp().get("contList");
			resultDto.setContList(contList);
			List<FundBalanceDto> fundbalanceList = (List<FundBalanceDto>) sp.getSp().get("fundbalanceList");
			resultDto.setFundbalanceList(fundbalanceList);
        } catch (Exception e) {
            log.error("(DS-Exception)异常：",e);
        }
        return resultDto;
	}

	@Override
	public List<BakCustDto> queryBakCust(String custNo, String role) {
		List<BakCustDto> list = new ArrayList<BakCustDto>();
        try {
        	list = queryDao.queryBakCust(custNo,role);
        } catch (Exception e) {
            log.error("(DS-Exception)异常：" ,e);
        }
        return list;
	}

	@Override
	public OpenAccountDto queryAccoByfundAcc(String fundAcc, String tradeAcc) {
		OpenAccountDto resultDto = new OpenAccountDto();
        try {
        	resultDto = queryDao.queryAccoByfundAcc(fundAcc,tradeAcc);
        } catch (Exception e) {
            log.error("(DS-Exception)异常：", e);
        }
        return resultDto;
	}

	@Override
	public TradeDto getTradeCount(String tradeacco, String fundacco,
			String dsapkind, String checkst) {
		TradeDto resultDto = new TradeDto();
        try {
        	TradeDto dto = new TradeDto();
        	dto.setTradeacco(tradeacco);
        	dto.setFundacco(fundacco);
        	dto.setDsapkind(dsapkind);
        	dto.setCheckst(checkst);
        	resultDto = queryDao.getTradeCount(dto);
        } catch (Exception e) {
            log.error("(DS-Exception)异常：", e);
        }
        return resultDto;
	}

	@Override
	public CapitalCheckDto getCapitalCount(String tradeacco, String capitalno,
			String capitaltype, String checkst, String begindate,
			String enddate, String operatorId, String operatorType) {
		CapitalCheckDto resultDto = new CapitalCheckDto();
        try {
        	CapitalCheckDto dto = new CapitalCheckDto();
        	dto.setTradeacco(tradeacco);
        	dto.setCapitalno(capitalno);
        	dto.setCapitaltype(capitaltype);
        	dto.setCheckst(checkst);
        	dto.setBegindate(begindate);
        	dto.setEnddate(enddate);
        	dto.setOperatorId(operatorId);
        	dto.setOperatorType(operatorType);
        	resultDto = queryDao.getCapitalCount(dto);
        } catch (Exception e) {
            log.error("资金复核记录条数异常：",e);
        }
        return resultDto;
	}

	@Override
	public List<BakCustDto> queryBakCustCheck(String custno, String optype,
			String checkflag) {
	 	List<BakCustDto> list = new ArrayList<BakCustDto>();
	    try {
	    	list = queryDao.queryBakCustCheck(custno, optype, checkflag);
	    } catch (Exception e) {
	        log.error("备案客户复核查询异常：", e);
	    }
	    return list;
	}

	@Override
	public List<RiskLevelDto> queryCustRiskLevel(String custno,
			String fundacco, String invnm, String invtp, String begindate,
			String enddate, String risklevel, String invprtp) {
		List<RiskLevelDto> resultList = new ArrayList<RiskLevelDto>();
        try {
        	RiskLevelDto dto = new RiskLevelDto();
        	dto.setCustno(custno);
        	dto.setFundacco(fundacco);
        	dto.setInvnm(invnm);
        	dto.setInvtp(invtp);
        	dto.setBegindate(begindate);
        	dto.setEnddate(enddate);
        	dto.setRisklevel(risklevel);
        	dto.setInvprtp(invprtp);
        	resultList = queryDao.queryCustRiskLevel(dto);
        } catch (Exception e) {
            log.error("查询客户风险等级异常：",e);
        }
        return resultList;
	}

	@Override
	public List<BrokerInfoDto> brokerChannelModifyQry(String custnm,
			String custno, String cltmno) {
		List<BrokerInfoDto> resultList = new ArrayList<BrokerInfoDto>();
        try {
        	resultList = queryDao.brokerChannelModifyQry(custnm, custno, cltmno);
        } catch (Exception e) {
            log.error("客户经理 渠道关系 修改查询异常：",e);
        }
        return resultList;
	}

	@Override
	public AccountDto queryCustInfo(String idno, String fundacct) {
		String custno = queryDao.getCustnoByIdnoAndFundAcco(idno, fundacct);
    	return null;
	}

	@Override
	public float findDiscount(String tradeacco, String fundacco, String fundid,
			String apkind, String subAmt) {
		float disCount = queryDao.findDiscount(tradeacco, fundacco, fundid, apkind, subAmt);
		return disCount;
	}

	@Override
	public List<FundInfoVo> getAllSubFundsArray(String type, String tano) {
		List<FundInfoVo> list = new ArrayList<FundInfoVo>();
		String fundst = "";//基金状态
	   if("S".equals(type)){
		   fundst = "'" + ParameterConstant.PM_CO_SYSTEM$FUNDST$1 + "'";
	   }else if("P".equals(type)){
		   fundst = "'" + ParameterConstant.PM_CO_SYSTEM$FUNDST$0 + "'," +
		            "'" + ParameterConstant.PM_CO_SYSTEM$FUNDST$6 + "'," +
		            "'" + ParameterConstant.PM_CO_SYSTEM$FUNDST$7 + "'," +
		            "'" + ParameterConstant.PM_CO_SYSTEM$FUNDST$8 + "'," +
		            "'" + ParameterConstant.PM_CO_SYSTEM$FUNDST$b + "'" ;
	   }
	   try {
		   list = fundInfoDao.getAllSubFundsArray(fundst, tano);
	   } catch (Exception e) {
		   log.error("获取所有基金(包括限制基金)异常",e);
	   }
	   return list;
	}

	@Override
	public List<SeatDto> getSeats() {
		List<SeatDto> list = new ArrayList<SeatDto>();
		try {
			list = queryDao.getSeats();
		} catch (Exception e) {
			log.error("查询所有销售机构异常",e);
		}
		return list;
	}

	@Override
	public TradeCheckDto getTradeCheckCount() throws Exception {
		TradeCheckDto tradeCheckDto = null;
		try {
			SearchParam sParam = new SearchParam();
        	sParam.setSp(new HashMap<String,Object>());
        	sParam.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
        	sParam.getSp().put("allPmky", "TRADEBOPERATOR");
        	List<ParameterVo> paradto = commonDao.getParameterListPage(sParam);
        	sParam = new SearchParam();
    		if(paradto != null && paradto.size() > 0){
    			sParam.getSp().put("batchoperatorId",paradto.get(0).getPmco());
    		}
			tradeCheckDto = queryDao.getTradeCheckCount(sParam);
		} catch (Exception e) {
			log.error("交易类复核记录数查询异常",e);
		}
		return tradeCheckDto;
	}

	@Override
	public TradeCheckDto getBatchTradeCheckCount(SearchParam sp) throws Exception {
		TradeCheckDto tradeCheckDto = new TradeCheckDto();
		try {
			SearchParam sParam = new SearchParam();
        	sParam.setSp(new HashMap<String,Object>());
        	sParam.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
        	sParam.getSp().put("allPmky", "TRADEBOPERATOR");
        	List<ParameterVo> paradto = commonDao.getParameterListPage(sParam);
    		if(paradto != null && paradto.size() > 0){
    			sp.getSp().put("batchoperatorId",paradto.get(0).getPmco());
    		}
			tradeCheckDto = queryDao.getBatchTradeCheckCount(sp);
		} catch (Exception e) {
			log.error("交易类复核记录数查询异常",e);
		}
		return tradeCheckDto;
	}
}
