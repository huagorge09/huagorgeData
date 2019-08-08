	package com.cmwa.ecc.business.service.impl.query;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.controller.query.AccountSearchController;
import com.cmwa.ecc.business.dao.customerDataMgr.CustomerDataManagerDao;
import com.cmwa.ecc.business.dao.dsTrade.DsTradeDao;
import com.cmwa.ecc.business.dao.query.AccountQueryDao;
import com.cmwa.ecc.business.dao.query.MultipleQueryDao;
import com.cmwa.ecc.business.dao.query.TradeQueryDao;
import com.cmwa.ecc.business.entity.accountManger.RiskLevelDto;
import com.cmwa.ecc.business.entity.dsTrade.DsTradeDataDto;
import com.cmwa.ecc.business.entity.multiple.FundBalanceSrhVo;
import com.cmwa.ecc.business.entity.multiple.FundCustInfoVo;
import com.cmwa.ecc.business.entity.query.AcctAckClearHisVo;
import com.cmwa.ecc.business.entity.query.AcctAppHisVo;
import com.cmwa.ecc.business.entity.query.AcctAppTodayVo;
import com.cmwa.ecc.business.entity.trade.TradeAckClearHisVo;
import com.cmwa.ecc.business.entity.trade.TradeAppHisVo;
import com.cmwa.ecc.business.entity.trade.TradeAppTodayVo;
import com.cmwa.ecc.business.service.query.ExportDataService;
import com.cmwa.ecc.business.utils.ExcelUtil;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * @author ex-wuh2
 *
 */
@Service
public class ExportDataServiceImpl implements ExportDataService {
	
	private static Logger logger = Logger.getLogger(AccountSearchController.class);	

	@Autowired
	private AccountQueryDao accountQueryDao;
	
	@Autowired
	private TradeQueryDao tradeQueryDao;
	
	@Autowired
	private MultipleQueryDao multipleQueryDao;
	
	@Autowired
	private CustomerDataManagerDao customerDataManagerDao;
	
	@Autowired
	private DsTradeDao dsTradeDao;
	
	/* 
	 * 当前账户导出
	 */
	@Override
	public void exportAccountApp(SearchParam sp, HttpServletResponse response) {
		
		try{
			String appDate = sp.getSp().get("appDate")+"";
			if(!"".equals(appDate) && null != appDate) {
				appDate = appDate.replace("-", "");
			}
			sp.getSp().put("appDate", appDate);
	        String fileName = URLEncoder.encode("当前账户申请流水.xls","UTF-8");
	        //存放表头信息
			List<AcctAppTodayVo> queryAcctAppTodayListPage =accountQueryDao.queryAcctAppTodayList(sp);
	        LinkedHashMap<String,String> alias = new LinkedHashMap<String,String>();
	        alias.put("fundacct","基金账号");
	        alias.put("tradeacco","交易账号");
	        alias.put("netPoint","网点代码");
	        alias.put("apkindName","业务类型");
	        alias.put("apdt","申请日期");
	        alias.put("aptm","申请时间");
	        alias.put("invtpName","投资者类型");
	        alias.put("invnm","投资者名称");
	        alias.put("idtpName","证件类型");
	        alias.put("idno","证件号码");
	        alias.put("mobileno","手机号码");
	        alias.put("addr","联系地址");
	        alias.put("postcode","邮政编码");
	        alias.put("email","电子邮箱");
	        alias.put("faxno","传真号码");
	        alias.put("delivertype","对账单寄送方式");
	        alias.put("melonmdName","分红方式");
	        alias.put("regioncodeName","交易方式");
	        alias.put("invname","投资者简称");
	        alias.put("brokername","经办人姓名");
	        alias.put("officetel","经办人办公电话");
	        alias.put("brokerfax","经办人传真号码");
	        alias.put("checkflagName","是否有效");
	        alias.put("operatorcode","操作员");
	        alias.put("checker","复核员");
	        alias.put("serialno","申请流水号");
	        alias.put("appno","申请编号");
	        HSSFWorkbook wb = ExcelUtil.pojo2ExcelNotWrite(queryAcctAppTodayListPage, alias);
	        OutputStream output = response.getOutputStream();
	        try {
		        response.reset();//必须reset，否则会乱码，且提示文件格式不对
		        response.setHeader("content-disposition", "attachment; filename=" + fileName);		//设置默认文件名，不设会默认为Action的名称
		        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
				wb.write(output);
	        } catch (Exception e) {
				logger.error("----ExportDataServiceImpl-exportAccountApp-Exception:",e);
			}
		}catch(Exception e){
			logger.error("当前账户申请流水导出异常："+e);
			e.printStackTrace();
		}
	 
	}
	
	/*
	 *  历史账户导出
	 */
	@Override
	public void exportAccountAppHis(SearchParam sp, HttpServletResponse response) {
		try {
			sp = changeDatePattern(sp);
			List<AcctAppHisVo> queryAcctAppHisListPage =accountQueryDao.queryAcctAppHisList(sp);
	        LinkedHashMap<String,String> alias = new LinkedHashMap<String,String>();
	        alias.put("fundacct", "基金账号");
	        alias.put("tradeacco", "交易账号");
	        alias.put("netPoint", "网点代码");
	        alias.put("apkindName", "业务类型");
	        alias.put("apdt", "申请日期");
	        alias.put("invtpName", "投资者类型");
	        alias.put("invnm", "投资者名称");
	        alias.put("idtpName", "证件类型");
	        alias.put("idno", "证件号码");
	        alias.put("mobileno", "手机号码");
	        alias.put("addr", "联系地址");
	        alias.put("postcode", "邮政编码");
	        alias.put("email", "电子邮箱");
	        alias.put("faxno", "传真号码");
	        alias.put("delivertype", "对账单寄送方式");
	        alias.put("melonmdName", "分红方式");
	        alias.put("regioncodeName", "交易方式");
	        alias.put("invname", "投资者简称");
	        alias.put("brokername", "经办人姓名");
	        alias.put("officetel", "经办人办公电话");
	        alias.put("brokerfax", "经办人传真号码");
	        alias.put("checkflagName", "是否有效");
	        alias.put("operatorcode", "操作员");
	        alias.put("checker", "复核员");
	        alias.put("serialno", "申请流水号");
	        alias.put("appno", "申请编号");
	        HSSFWorkbook wb = ExcelUtil.pojo2ExcelNotWrite(queryAcctAppHisListPage, alias);
	        OutputStream out = response.getOutputStream();
	        try {
	        	String fileName = URLEncoder.encode("历史账户申请流水.xls","UTF-8");
		        response.reset();//必须reset，否则会乱码，且提示文件格式不对
		        response.setHeader("content-disposition", "attachment; filename=" + fileName);		//设置默认文件名，不设会默认为Action的名称
		        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		        wb.write(out);
			} catch (Exception e) {
				logger.error("----ExportDataServiceImpl-exportAccountAppHis-Exception:",e);
			}
		} catch (Exception e) {
			logger.error(e + "历史账户导出异常");		
		}
		
	}

	/* 
	 * 历史二级账户导出
	 */
	@Override
	public void exportAccountAckHis(SearchParam sp, HttpServletResponse response) {
		try {
			sp = changeDatePattern(sp);
	        List<AcctAckClearHisVo> queryAcctAckClearHisListPage =accountQueryDao.queryAcctAckClearHisList(sp);
	        LinkedHashMap<String,String> alias = new LinkedHashMap<String,String>();
	        alias.put("fundacct", "基金账号");
	        alias.put("tradeacco", "交易账号");
	        alias.put("netPoint", "网点代码");
	        alias.put("idtpName", "证件类型");
	        alias.put("idno", "证件号码");
	        alias.put("ackdt", "确认日期");
	        alias.put("invtpName", "投资者类型");
	        alias.put("invnm", "投资者名称");
	        alias.put("apkindName", "业务类型");
	        alias.put("melonmdName", "分红方式");
	        alias.put("retcode", "返回代码");
	        alias.put("retmsg", "备注");
	        alias.put("regioncodeName", "交易方式");
	        alias.put("invname", "投资者简称");
	        alias.put("brokername", "经办人姓名");
	        alias.put("brokertel", "经办人办公电话");
	        alias.put("brokerfax", "经办人传真号码");
	        alias.put("serialno", "申请流水号");
	        alias.put("ackno", "确认流水号");
	        HSSFWorkbook wb = ExcelUtil.pojo2ExcelNotWrite(queryAcctAckClearHisListPage,alias);
	        OutputStream out = response.getOutputStream();
	        try {
	        	String fileName = URLEncoder.encode("历史二级清算账户确认流水.xls","UTF-8");
		        response.reset();//必须reset，否则会乱码，且提示文件格式不对
		        response.setHeader("content-disposition", "attachment; filename=" + fileName);		//设置默认文件名，不设会默认为Action的名称
		        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		        wb.write(out);
			} catch (Exception e) {
				logger.error("----ExportDataServiceImpl-exportAccountAckHis-Exception:",e);
			}
		} catch (Exception e) {
			logger.error(e + "历史二级清算账户导出异常");		
		}
		
	}
	
	/* 起止日期更换格式 */
	public SearchParam  changeDatePattern(SearchParam sp) {
		//起止日期更换格式
		String startDate = null;
		String endDate = null;
		if(sp.getSp().get("startDate") != null) {
			startDate =  sp.getSp().get("startDate").toString().replace("-", "");
			sp.getSp().put("startDate", startDate);
		}
		if(sp.getSp().get("endDate") != null) {
			endDate =  sp.getSp().get("endDate").toString().replace("-", "");
			sp.getSp().put("endDate", endDate);
		}
		return sp;
	}
	
	/* 起止日期更换格式 */
	public SearchParam  changeDatePatternForCustData(SearchParam sp) {
		//起止日期更换格式
		String startDate = null;
		String endDate = null;
		String executedStartDate = "";
		String executedEndDate = "";
		String spStartDate = "";
		String spEndDate = "";
		int syear = 0;
		int iyear = 0;
		if(sp.getSp().get("startDate") != null) {
			spStartDate = sp.getSp().get("startDate").toString();

		}
		if(sp.getSp().get("endDate") != null) {
			spEndDate = sp.getSp().get("endDate").toString();
		}
		if(sp.getSp().get("startDate") != null && !"".equals(spStartDate)) {
			startDate =  sp.getSp().get("startDate").toString().replace("-", "");
			executedStartDate = startDate.substring(0,4);
			syear =Integer.parseInt(executedStartDate) - 1;
			String beginDate = syear + startDate.substring(4);
			sp.getSp().put("startDate", beginDate);
		}
		if(sp.getSp().get("endDate") != null && !"".equals(spEndDate)) {
			endDate =  sp.getSp().get("endDate").toString().replace("-", "");
			executedEndDate = endDate.substring(0,4);
			iyear =Integer.parseInt(executedEndDate) - 1;
			String eDate = iyear + endDate.substring(4);
			sp.getSp().put("endDate", eDate);
		}
		return sp;
	}
	
	//当前交易
	@Override
	public void exportTradeApp(SearchParam sp, HttpServletResponse response) {
		try {
			String appDate = sp.getSp().get("appDate") + "";
			if(appDate != null && !"".equals(appDate)) {
				appDate = appDate.replace("-", "");
			}
			sp.getSp().put("appDate", appDate);
	        List<TradeAppTodayVo> queryTradeAppToday =tradeQueryDao.queryTradeAppToday(sp);
	        LinkedHashMap<String,String> alias = new LinkedHashMap<String,String>();
	        alias.put("apdt", "申请日期");
	        alias.put("fundacct", "基金账号");
	        alias.put("invnm", "投资者名称");
	        alias.put("tradeacco", "交易账号");
	        alias.put("bankacco", "银行账号");
	        alias.put("bankno", "交易渠道");
	        alias.put("netpoint", "网点代码");
	        alias.put("apkindName", "业务类型");
	        alias.put("fundid", "基金代码");
	        alias.put("subamt", "申请金额");
	        alias.put("subquty", "申请份额");
	        alias.put("fundname", "基金名称");
	        alias.put("commro", "折扣率");
	        alias.put("ofundid", "对方基金代码");
	        alias.put("ofundname", "对方基金名称");
	        alias.put("melonmdName", "分红方式");
	        alias.put("dividendrate", "分红比例");
	        alias.put("invtpName", "投资者类型");
	        alias.put("oldappno", "原申请合同号");
	        alias.put("oseatno", "对方销售机构代码");
	        alias.put("oseatnm", "对方销售机构名称");
	        alias.put("broker", "经办人名称");
	        alias.put("brokertel", "经办人办公电话");
	        alias.put("brokerfax", "经办人传真号码");
	        alias.put("checkflag", "是否资金复核通过");
	        alias.put("applyst", "申请状态");
	        alias.put("serialno", "申请流水号");
	        alias.put("appno", "申请编号");
	        HSSFWorkbook wb = ExcelUtil.pojo2ExcelNotWrite(queryTradeAppToday,alias);
	        OutputStream out = response.getOutputStream();
	        try {
	        	String fileName = URLEncoder.encode("当前交易申请流水.xls","UTF-8");
		        response.reset();//必须reset，否则会乱码，且提示文件格式不对
		        response.setHeader("content-disposition", "attachment; filename=" + fileName);		//设置默认文件名，不设会默认为Action的名称
		        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		        wb.write(out);
			} catch (Exception e) {
				logger.error("----ExportDataServiceImpl-exportTradeApp-Exception:",e);
			}
		} catch (Exception e) {
			logger.error(e + "当前交易流水导出异常");		
		}
	}

	@Override
	public void exportTradeAppHis(SearchParam sp, HttpServletResponse response) {
		try {
			sp = changeDatePattern(sp);
	        List<TradeAppHisVo> queryTradeAppHis =tradeQueryDao.queryTradeAppHis(sp);
	        LinkedHashMap<String,String> alias = new LinkedHashMap<String,String>();
	        alias.put("fundacct", "基金账户");
	        alias.put("tradeacco", "交易账号");
	        alias.put("bankacco", "银行账号");
	        alias.put("bankno", "交易渠道");
	        alias.put("netpoint", "网点代码");
	        alias.put("apdt", "申请日期");
	        alias.put("invnm", "投资者名称");
	        alias.put("fundid", "基金代码");
	        alias.put("fundname", "基金名称");
	        alias.put("apkindName", "业务类型");
	        alias.put("subamt", "申请金额");
	        alias.put("subquty", "申请份额");
	        alias.put("invtpName", "投资者类型");
	        alias.put("commro", "折扣率");
	        alias.put("ofundid", "对方基金代码");
	        alias.put("ofundname", "对方基金名称");
	        alias.put("melonmdName", "分红方式");
	        alias.put("dividendrate", "分红比例");
	        alias.put("oldappno", "原申请合同号");
	        alias.put("oseatno", "对方销售机构代码");
	        alias.put("oseatnm", "对方销售机构名称");
	        alias.put("broker", "经办人名称");
	        alias.put("brokertel", "经办人办公电话");
	        alias.put("brokerfax", "经办人传真号码");
	        alias.put("checkflag", "是否资金复核通过");
	        alias.put("applyst", "申请状态");
	        alias.put("serialno", "申请流水号");
	        alias.put("appno", "申请编号");
	        HSSFWorkbook wb = ExcelUtil.pojo2ExcelNotWrite(queryTradeAppHis,alias);
	        OutputStream out = response.getOutputStream();
	        try {
	        	String fileName = URLEncoder.encode("历史交易申请流水.xls","UTF-8");
		        response.reset();//必须reset，否则会乱码，且提示文件格式不对
		        response.setHeader("content-disposition", "attachment; filename=" + fileName);		//设置默认文件名，不设会默认为Action的名称
		        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		        wb.write(out);
			} catch (Exception e) {
				logger.error("----ExportDataServiceImpl-exportTradeAppHis-Exception:",e);
			}
	        
		} catch (Exception e) {
			logger.error(e + "历史交易流水导出异常");		
		}
	}

	@Override
	public void exportTradeAckHis(SearchParam sp, HttpServletResponse response) {
		try {
			sp = changeDatePattern(sp);
	        List<TradeAckClearHisVo> queryTradeAckClearHis =tradeQueryDao.queryTradeAckClearHis(sp);
	        LinkedHashMap<String,String> alias = new LinkedHashMap<String,String>();
	        alias.put("fundacct", "基金账号");
	        alias.put("fundid", "基金代码");
	        alias.put("tradeacco", "交易账户");
	        alias.put("netpoint","网点代码");
	        alias.put("apdt", "申请日期");
	        alias.put("invtpName", "投资者类型");
	        alias.put("ackdt", "确认日期");
	        alias.put("invnm", "投资人姓名");
	        alias.put("apkindName", "业务类型");
	        alias.put("subquty", "申请份额");
	        alias.put("subamt", "申请金额");
	        alias.put("ackquty", "确认份额");
	        alias.put("ackamt", "确认金额");
	        alias.put("fee", "手续费");
	        alias.put("retcode", "返回代码");
	        alias.put("retmsg", "备注");
	        alias.put("regioncode", "交易方式");
	        alias.put("acknav", "基金净值");
	        alias.put("commro", "折扣率");
	        alias.put("sharetype", "收费方式");
	        alias.put("fundname", "基金名称");
	        alias.put("oseatno", "对方销售机构代码");
	        alias.put("oseatnm", "对方销售机构名称");
	        alias.put("ofundid", "对方基金代码");
	        alias.put("ofundacct", "对方基金账户");
	        alias.put("otradeacco", "对方交易账户");
	        alias.put("oldappno", "原申请流水号");
	        alias.put("melonmdName", "分红方式");
	        alias.put("dividendrate", "分红比率");
	        alias.put("frozencause", "冻结原因");
	        alias.put("appno", "申请编号");
	        alias.put("ackno", "确认编号");
	        HSSFWorkbook wb = ExcelUtil.pojo2ExcelNotWrite(queryTradeAckClearHis,alias);
	        OutputStream out = response.getOutputStream();
	        try {
	        	String fileName = URLEncoder.encode("历史二级清算交易确认流水.xls","UTF-8");
		        response.reset();//必须reset，否则会乱码，且提示文件格式不对
		        response.setHeader("content-disposition", "attachment; filename=" + fileName);		//设置默认文件名，不设会默认为Action的名称
		        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		        wb.write(out);
			} catch (Exception e) {
				logger.error("----ExportDataServiceImpl-exportTradeAckHis-Exception:",e);
			}
		} catch (Exception e) {
			logger.error(e + "历史二级清算交易确认流水导出异常");		
		}
	}

	@Override
	public void exportAccountInfo(SearchParam sp, HttpServletResponse response) {
		try {
	        List<FundCustInfoVo> queryFundCustInfoList =multipleQueryDao.queryFundCustInfoList(sp);
	        LinkedHashMap<String,String> alias = new LinkedHashMap<String,String>();
	        alias.put("bankname", "银行名称");
	        alias.put("fundacct", "基金账号");
	        alias.put("tradeacco", "交易账号");
	        alias.put("netpoint", "网点代码");
	        alias.put("custtpName", "客户类型");
	        alias.put("idtpName", "证件类型");
	        alias.put("idno", "证件号码");
	        alias.put("invtpName", "投资者类型");
	        alias.put("invnm", "投资者姓名");
	        alias.put("melonmdName", "分红方式");
	        alias.put("tradeaccostName", "交易账户状态");
	        alias.put("fdacstName", "基金账户状态");
	        alias.put("opendt", "开户日期");
	        HSSFWorkbook wb =ExcelUtil.pojo2ExcelNotWrite(queryFundCustInfoList,alias);
	        OutputStream out = response.getOutputStream();
	        try {
				String fileName = URLEncoder.encode("账户信息.xls","UTF-8");
	        	response.reset();//必须reset，否则会乱码，且提示文件格式不对
		        response.setHeader("content-disposition", "attachment; filename=" + fileName);		//设置默认文件名，不设会默认为Action的名称
		        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		        wb.write(out);
			} catch (Exception e) {
				logger.error("----ExportDataServiceImpl-exportAccountInfo-Exception:",e);
			}
		} catch (Exception e) {
			logger.error(e + "账户信息导出异常");		
		}
	}

	@Override
	public void exportFundBalance(SearchParam sp, HttpServletResponse response) {
		try {
	        List<FundBalanceSrhVo> queryFundBalanceList =multipleQueryDao.queryFundBalanceList(sp);
	        LinkedHashMap<String,String> alias = new LinkedHashMap<String,String>();
	        alias.put("fundid", "基金代码");
	        alias.put("fundnm", "基金名称");
	        alias.put("bankno", "银行代码");
	        alias.put("netpoint", "网点代码");
	        alias.put("fundacct", "基金账号");
	        alias.put("tradeacco", "交易账号");
	        alias.put("invnm", "投资者姓名");
	        alias.put("balance", "实际份额");
	        alias.put("available", "可用份额");
	        alias.put("frozen", "未上传申请冻结份额");
	        alias.put("hfrozen", "已上传申请的冻结份额");
	        alias.put("abnmfrozen", "异常冻结份额");
	        alias.put("nav", "基金净值");
	        HSSFWorkbook wb = ExcelUtil.pojo2ExcelNotWrite(queryFundBalanceList,alias);
	        OutputStream out = response.getOutputStream();
	        try {
	        	String fileName = URLEncoder.encode("基金余额.xls","UTF-8");
		        response.reset();//必须reset，否则会乱码，且提示文件格式不对
		        response.setHeader("content-disposition", "attachment; filename=" + fileName);		//设置默认文件名，不设会默认为Action的名称
		        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		        wb.write(out);
			} catch (Exception e) {
				logger.error("----ExportDataServiceImpl-exportFundBalance-Exception:",e);
			}
		} catch (Exception e) {
			logger.error(e + "基金余额导出异常");		
		}
	}

	@Override
	public void exportCustomerData(SearchParam sp, HttpServletResponse response) {
		try {
			sp = changeDatePatternForCustData(sp);
	        List<RiskLevelDto> getRiskLevelInfoList =customerDataManagerDao.getRiskLevelInfoList(sp);
	        LinkedHashMap<String,String> alias = new LinkedHashMap<String,String>();
	        alias.put("invnm", "客户名称");
	        alias.put("invtp", "客户类型");
	        alias.put("fundacc", "基金账号");
	        alias.put("risklevelName", "风险等级");
	        //alias.put("oldRiskLevelName", "风险等级(旧)");
	        alias.put("custriskdate", "评估日期");
	        alias.put("outdate", "过期时间");
	        alias.put("invprtp", "投资者类型");
	        alias.put("regioncodeName", "交易方式");
	        //alias.put("voicerecord", "录音文件编号");
	        //alias.put("appst", "网上客户转换申请");
	        //alias.put("apdt", "转换申请日期");
	        alias.put("opnm", "操作用户");
	        HSSFWorkbook wb = ExcelUtil.pojo2ExcelNotWrite(getRiskLevelInfoList,alias);
	        OutputStream out = response.getOutputStream();
	        try {
	        	String fileName = URLEncoder.encode("客户评估数据.xls","UTF-8");
		        response.reset();//必须reset，否则会乱码，且提示文件格式不对
		        response.setHeader("content-disposition", "attachment; filename=" + fileName);		//设置默认文件名，不设会默认为Action的名称
		        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		        wb.write(out);
			} catch (Exception e) {
				logger.error("----ExportDataServiceImpl-exportCustomerData-Exception:",e);
			}
		} catch (Exception e) {
			logger.error(e + "客户评估数据导出异常");		
		}
	}

	@Override
	public void exportTradeData(SearchParam sp, HttpServletResponse response) {
		try {
			sp = changeDatePattern(sp);
	        List<DsTradeDataDto> queryDsTradeDataList =dsTradeDao.queryDsTradeDataList(sp);
	        LinkedHashMap<String,String> alias = new LinkedHashMap<String,String>();
	        alias.put("fileno", "文件编号");
	        alias.put("apdt", "申请时间");
	        alias.put("invnm", "客户名称");
	        alias.put("invprtpName", "投资者类型");
	        alias.put("fundname", "产品名称");
	        alias.put("subamt", "交易金额");
	        alias.put("subquty", "交易份额");
	        alias.put("contractsignName", "合同签署");
	        alias.put("contractdevolveName", "合同是否移交");
	        alias.put("isoriginalName", "合同是否原件");
	        alias.put("istradeformName", "交易表单是否原件");
	        alias.put("filedName", "是否归档");
	        alias.put("risksignName", "二次风险揭示是否签署");
	        alias.put("voicerecord", "录音文件编号");
	        alias.put("remark", "备注");
	        HSSFWorkbook wb = ExcelUtil.pojo2ExcelNotWrite(queryDsTradeDataList,alias);
	        OutputStream out = response.getOutputStream();
	        try {
	        	String fileName = URLEncoder.encode("交易资料管理.xls","UTF-8");
		        response.reset();//必须reset，否则会乱码，且提示文件格式不对
		        response.setHeader("content-disposition", "attachment; filename=" + fileName);		//设置默认文件名，不设会默认为Action的名称
		        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		        wb.write(out);
			} catch (Exception e) {
				logger.error("----ExportDataServiceImpl-exportTradeData-Exception:",e);
			}
		} catch (Exception e) {
			logger.error(e + "交易资料导出异常");		
		}
	}

}
