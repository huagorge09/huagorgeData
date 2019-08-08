package com.cmwa.ecc.business.controller.account;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.cmwa.ec.base.util.Sequences;
import com.cmwa.ecc.business.commonVo.Employee;
import com.cmwa.ecc.business.entity.accountManger.ContactDto;
import com.cmwa.ecc.business.entity.accountManger.ContractSignDto;
import com.cmwa.ecc.business.entity.accountManger.OpenAccountDto;
import com.cmwa.ecc.business.entity.accountManger.SeatDto;
import com.cmwa.ecc.business.entity.fundinfo.FundInfoVo;
import com.cmwa.ecc.business.entity.fundinfo.ProductInfoDto;
import com.cmwa.ecc.business.entity.paramvo.ParameterVo;
import com.cmwa.ecc.business.entity.trade.EleContractDto;
import com.cmwa.ecc.business.entity.trade.TradeCheckDto;
import com.cmwa.ecc.business.entity.trade.TradeDto;
import com.cmwa.ecc.business.entity.workdate.WorkDateVo;
import com.cmwa.ecc.business.service.account.QueryManager;
import com.cmwa.ecc.business.service.business.EleContractManagerService;
import com.cmwa.ecc.business.service.common.CommonService;
import com.cmwa.ecc.business.service.fundinfo.FundInfoService;
import com.cmwa.ecc.business.service.trade.TradeManagerService;
import com.cmwa.ecc.business.utils.ExcelUtil;
import com.cmwa.ecc.business.utils.NumberToCN;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.ParameterConstant;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;
import com.cmwa.ecc.business.utils.TransCodeConstant;

/**
 * 交易管理
 * @author ex-liuy
 *
 */
@Controller
@RequestMapping(value="/capitalService/server")
public class TradeManagerController {
	private static Logger logger = LoggerFactory.getLogger(TradeManagerController.class);
	@Autowired
	private CommonService commonService;
	/*@Autowired
	private ECCProductService productService;*/
	@Autowired
	private EleContractManagerService eleService;
	@Autowired
	private TradeManagerService tradeManagerService;
	@Autowired
	private FundInfoService fundInfoService;
	@Autowired
	private QueryManager queryManager;
	
	/**
	 * 加载页面所需数据
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/loadTradeQryData.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public Map<String, Object> loadData(HttpServletRequest request) throws IOException{
		//委托方式
		Map<String, Object> result = new HashMap<String, Object>();
		List<ParameterVo> trustTypeArray = commonService.getParameterListPage(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_ACCPTMD);
		result.put("trustTypeArray", trustTypeArray);
		//业务类型
		SearchParam sp = new SearchParam();
		sp.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
		sp.getSp().put("allPmky", ParameterConstant.PARAM_PMKY_APKIND);
		sp.getSp().put("pmv2", "T");
		List<ParameterVo> dsapkindArray = commonService.getParameterListPage(sp);
		result.put("dsapkindArray", dsapkindArray);
		//复核状态
		List<ParameterVo> checkstArray = commonService.getParameterListPage(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_DSCHKFLAG);
		result.put("checkstArray", checkstArray);
		Employee emp = SessionUtils.getEmployee();
		String currentUserId =emp.getID();
		result.put("currentUserId", currentUserId);
		
		WorkDateVo WorkDateVo = new WorkDateVo();
		commonService.getWorkDates(WorkDateVo);
		if(!WorkDateVo.getWorkDate().equals("") && WorkDateVo.getWorkDate() != null){
			WorkDateVo.setWorkDate(WorkDateVo.getWorkDate().substring(0, 4) + "-" + WorkDateVo.getWorkDate().substring(4, 6) + "-" + WorkDateVo.getWorkDate().substring(6, WorkDateVo.getWorkDate().length()));
		}
		result.put("workDateDto", WorkDateVo);
		return result;
	}
	
	
	/**
	 * 提交认购数据
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/submitSubscribe.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public JSONObject submitSubscribe(HttpServletRequest request) throws IOException{
		JSONObject jsonObject = new JSONObject();
		try {
			HttpSession session = request.getSession();
			String permissionId = request.getParameter("permissionId");	//页面权限
			if(permissionId == null || permissionId.length()==0){
				permissionId = "";
			}
			
			Employee emp = SessionUtils.getEmployee();
			String operatorId = emp.getID();
			String trustType = request.getParameter("trustType"); // 委托方式
			String custno = request.getParameter("custno"); // 客户号
			String tradeacco = request.getParameter("hidtradeacco"); // 交易账号
			String fundid = request.getParameter("fundid"); // 基金代码
			String subAmt = request.getParameter("subAmt"); // 认购金额
			String currencytype = request.getParameter("currencytype"); // 币种
			String contact = request.getParameter("hidcontact"); // 经办人
			String contidno = request.getParameter("hidcontidno"); // 经办人证件号码
			String contidtp = request.getParameter("hidcontidtp"); // 经办人证件类型
			String checkno = request.getParameter("checkno"); // 主管编号
			String contractsign = request.getParameter("contractsign");// 是否归档
			String isoriginal = request.getParameter("isoriginal");// 是否原件
			if (!"1".equals(contractsign)) {
				isoriginal = "";
			}
			// 交易表单原件
			String istradeform = request.getParameter("istradeform") == null ? ""
					: request.getParameter("istradeform");

			String riskSign = request.getParameter("riskSign");
			String voiceRecord = request.getParameter("voicerecord");

			String serialNo = Sequences.getPK();// 流水号
			
			TradeDto dto = new TradeDto();
			TradeDto returnDto = new TradeDto();
			
			dto.setOperatorId(operatorId);
			dto.setPermissionId(permissionId);
			dto.setSerialno(serialNo);
			dto.setTrustType(trustType);
			dto.setCustno(custno);
			dto.setTradeacco(tradeacco);
			dto.setFundid(fundid);
			dto.setSubAmt(subAmt);
			dto.setCurrencytype(currencytype);
			dto.setContact(contact);
			dto.setContidno(contidno);
			dto.setContidtp(contidtp);
			dto.setCheckno(checkno);
			
			
			ProductInfoDto prodto=new ProductInfoDto();
			prodto.setFuncode(fundid);
			
			SearchParam sp =new SearchParam();
			sp.getSp().put("fundid", fundid);
			Page<ProductInfoDto> proList = fundInfoService.getOrderFundInfoList(sp);
			String contractversion="";		
			if(proList!=null && proList.getItems().size()>0){
				ProductInfoDto pro=(ProductInfoDto)proList.getItems().get(0);
				contractversion=pro.getContractversion()==null?"":pro.getContractversion();
			}		
					
					
			Page<EleContractDto> eleContractDto = eleService.getEleContractList(sp);
			//获取IP地址
			String ip = request.getHeader("x-forwarded-for");     
			if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
				ip = request.getHeader("Proxy-Client-IP");        
			}        
			if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
				ip = request.getHeader("WL-Proxy-Client-IP");       
			}        
			if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
				ip = request.getRemoteAddr();        
			}
			
			ContractSignDto contractSignDto = new ContractSignDto();
			if(eleContractDto != null && eleContractDto.getItems().size() > 0){
				//合同版本号
				contractversion = eleContractDto.getItems().get(0).getContractversion();
				contractSignDto.setSerialNo(Sequences.getPK());//序列号
				contractSignDto.setCustNo(custno);//客户号
				contractSignDto.setTradeAcco(tradeacco);//交易账号
				contractSignDto.setFundId(fundid);
				contractSignDto.setContractVer(contractversion);//合同版本
				contractSignDto.setContractTp("2");//合同类型
				contractSignDto.setSignChannel("0");//签署途径
				contractSignDto.setSignMachine(ip);//合同签署机器IP
				
				contractSignDto = tradeManagerService.contractSign(contractSignDto);
			}else{
				if(!"".equals(contractversion)){
					contractSignDto.setSerialNo(Sequences.getPK());//序列号
					contractSignDto.setCustNo(custno);//客户号
					contractSignDto.setTradeAcco(tradeacco);//交易账号
					contractSignDto.setFundId(fundid);
					contractSignDto.setContractVer(contractversion);//合同版本
					contractSignDto.setContractTp("2");//合同类型
					contractSignDto.setSignChannel("0");//签署途径
					contractSignDto.setSignMachine(ip);//合同签署机器IP
					contractSignDto = tradeManagerService.contractSign(contractSignDto);
				}
			}
			
			try{
				TradeDto datadto=new TradeDto();
				datadto.setSerialno(Sequences.getPK());
				datadto.setOldserialno(serialNo);
				datadto.setCustno(custno);
				datadto.setContractsign(contractsign);
				datadto.setIsoriginal(isoriginal);
				datadto.setIstradeform(istradeform);
				datadto.setRisksign(riskSign);
				datadto.setVoicerecord(voiceRecord);
				tradeManagerService.insertTradeData(datadto);
			}catch(Exception e){
				System.out.print("插入交易资料异常："+e);
				e.printStackTrace(); 
			}
		 
			//认购
			tradeManagerService.subscribe(dto);
			
			returnDto = dto;
			
			if(returnDto != null){
				jsonObject.put("serialno", returnDto.getSerialno());
				jsonObject.put("errCode", returnDto.getErrcode());
				jsonObject.put("errMsg", returnDto.getErrmsg());
			}
			 
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("errCode", "9999");
			jsonObject.put("errMsg", e.toString());
		}
		return jsonObject;
	}
	
	/**
	 * 提交申购数据
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/submitPurchase.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public JSONObject submitPurchase(HttpServletRequest request) throws IOException{
		JSONObject jsonObject = new JSONObject();
		try {
			HttpSession session = request.getSession();
			String permissionId = request.getParameter("permissionId");	//页面权限
			if(permissionId == null || permissionId.length()==0){
				permissionId = "";
			}
			Employee emp = SessionUtils.getEmployee();
			String operatorId = emp.getID();
			String trustType = request.getParameter("trustType"); // 委托方式
			String custno = request.getParameter("custno"); // 客户号
			String tradeacco = request.getParameter("hidtradeacco"); // 交易账号
			String fundid = request.getParameter("fundid"); // 基金代码
			String subAmt = request.getParameter("subAmt"); // 认购金额
			String currencytype = request.getParameter("currencytype"); // 币种
			String contact = request.getParameter("hidcontact"); // 经办人
			String contidno = request.getParameter("hidcontidno"); // 经办人证件号码
			String contidtp = request.getParameter("hidcontidtp"); // 经办人证件类型
			String checkno = request.getParameter("checkno"); // 主管编号
			String contractsign = request.getParameter("contractsign");// 是否归档
			String isoriginal = request.getParameter("isoriginal");// 是否原件
			if (!"1".equals(contractsign)) {
				isoriginal = "";
			}
			// 交易表单原件
			String istradeform = request.getParameter("istradeform") == null ? ""
					: request.getParameter("istradeform");
			
			String riskSign = request.getParameter("riskSign");
			String voiceRecord = request.getParameter("voicerecord");
			
			String serialNo = Sequences.getPK();// 流水号
			
			TradeDto dto = new TradeDto();
			TradeDto returnDto = new TradeDto();
			
			dto.setOperatorId(operatorId);
			dto.setPermissionId(permissionId);
			dto.setSerialno(serialNo);
			dto.setTrustType(trustType);
			dto.setCustno(custno);
			dto.setTradeacco(tradeacco);
			dto.setFundid(fundid);
			dto.setSubAmt(subAmt);
			dto.setCurrencytype(currencytype);
			dto.setContact(contact);
			dto.setContidno(contidno);
			dto.setContidtp(contidtp);
			dto.setCheckno(checkno);
			
			
			ProductInfoDto prodto=new ProductInfoDto();
			prodto.setFuncode(fundid);
			
			SearchParam sp =new SearchParam();
			sp.getSp().put("fundid", fundid);
			Page<ProductInfoDto> proList = fundInfoService.getOrderFundInfoList(sp);
			String contractversion="";		
			if(proList!=null && proList.getItems().size()>0){
				ProductInfoDto pro=(ProductInfoDto)proList.getItems().get(0);
				contractversion=pro.getContractversion()==null?"":pro.getContractversion();
			}		
			
			
			Page<EleContractDto> eleContractDto = eleService.getEleContractList(sp);
			//获取IP地址
			String ip = request.getHeader("x-forwarded-for");     
			if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
				ip = request.getHeader("Proxy-Client-IP");        
			}        
			if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
				ip = request.getHeader("WL-Proxy-Client-IP");       
			}        
			if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
				ip = request.getRemoteAddr();        
			}
			
			ContractSignDto contractSignDto = new ContractSignDto();
			if(eleContractDto != null && eleContractDto.getItems().size() > 0){
				//合同版本号
				contractversion = eleContractDto.getItems().get(0).getContractversion();
				contractSignDto.setSerialNo(Sequences.getPK());//序列号
				contractSignDto.setCustNo(custno);//客户号
				contractSignDto.setTradeAcco(tradeacco);//交易账号
				contractSignDto.setFundId(fundid);
				contractSignDto.setContractVer(contractversion);//合同版本
				contractSignDto.setContractTp("2");//合同类型
				contractSignDto.setSignChannel("0");//签署途径
				contractSignDto.setSignMachine(ip);//合同签署机器IP
				
				contractSignDto = tradeManagerService.contractSign(contractSignDto);
			}else{
				if(!"".equals(contractversion)){
					contractSignDto.setSerialNo(Sequences.getPK());//序列号
					contractSignDto.setCustNo(custno);//客户号
					contractSignDto.setTradeAcco(tradeacco);//交易账号
					contractSignDto.setFundId(fundid);
					contractSignDto.setContractVer(contractversion);//合同版本
					contractSignDto.setContractTp("2");//合同类型
					contractSignDto.setSignChannel("0");//签署途径
					contractSignDto.setSignMachine(ip);//合同签署机器IP
					contractSignDto = tradeManagerService.contractSign(contractSignDto);
				}
			}
			
			try{
				TradeDto datadto=new TradeDto();
				datadto.setSerialno(Sequences.getPK());
				datadto.setOldserialno(serialNo);
				datadto.setCustno(custno);
				datadto.setContractsign(contractsign);
				datadto.setIsoriginal(isoriginal);
				datadto.setIstradeform(istradeform);
				datadto.setRisksign(riskSign);
				datadto.setVoicerecord(voiceRecord);
				tradeManagerService.insertTradeData(datadto);
			}catch(Exception e){
				System.out.print("插入交易资料异常："+e);
				e.printStackTrace(); 
			}
			
			//申购
			tradeManagerService.purchase(dto);
			
			returnDto = dto;
			
			if(returnDto != null){
				jsonObject.put("serialno", returnDto.getSerialno());
				jsonObject.put("errCode", returnDto.getErrcode());
				jsonObject.put("errMsg", returnDto.getErrmsg());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("errCode", "9999");
			jsonObject.put("errMsg", e.toString());
		}
		return jsonObject;
	}
	
	/**
	 * 提交赎回申请
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/submitRedeem.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public JSONObject submitRedeem(HttpServletRequest request) throws IOException{
		JSONObject jsonObject = new JSONObject();
		try {
			HttpSession session = request.getSession();
			String permissionId = request.getParameter("permissionId");	//页面权限
			if(permissionId == null || permissionId.length()==0){
				permissionId = "";
			}
			Employee emp = SessionUtils.getEmployee();
			String operatorId = emp.getID();
			String trustType = request.getParameter("trustType"); // 委托方式
			String custno = request.getParameter("custno"); // 客户号
			String tradeacco = request.getParameter("hidtradeacco"); // 交易账号
			String fundid = request.getParameter("fundid"); // 基金代码
			String subAmt = request.getParameter("subAmt"); // 认购金额
			String largeflag = request.getParameter("largeflag");
			String contact = request.getParameter("hidcontact"); // 经办人
			String contidno = request.getParameter("hidcontidno"); // 经办人证件号码
			String contidtp = request.getParameter("hidcontidtp"); // 经办人证件类型
			String checkno = request.getParameter("checkno"); // 主管编号
			// 交易表单原件
			String istradeform = request.getParameter("istradeform") == null ? ""
					: request.getParameter("istradeform");
			
			String serialNo = Sequences.getPK();// 流水号
			
			TradeDto dto = new TradeDto();
			TradeDto returnDto = new TradeDto();
			
			dto.setOperatorId(operatorId);
			dto.setPermissionId(permissionId);
			dto.setSerialno(serialNo);
			dto.setTrustType(trustType);
			dto.setCustno(custno);
			dto.setTradeacco(tradeacco);
			dto.setFundid(fundid);
			dto.setSubAmt(subAmt);
			dto.setLargeflag(largeflag);
			dto.setContact(contact);
			dto.setContidno(contidno);
			dto.setContidtp(contidtp);
			dto.setCheckno(checkno);
			
			
			try{
				TradeDto datadto=new TradeDto();
				datadto.setSerialno(Sequences.getPK());
				datadto.setOldserialno(serialNo);
				datadto.setCustno(custno);
				datadto.setIstradeform(istradeform);
				tradeManagerService.insertTradeData(datadto);
			}catch(Exception e){
				System.out.print("插入交易资料异常："+e);
				e.printStackTrace(); 
			}
			
			//赎回
			tradeManagerService.redeem(dto);
			
			returnDto = dto;
			
			if(returnDto != null){
				jsonObject.put("serialno", returnDto.getSerialno());
				jsonObject.put("errCode", returnDto.getErrcode());
				jsonObject.put("errMsg", returnDto.getErrmsg());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("errCode", "9999");
			jsonObject.put("errMsg", e.toString());
		}
		return jsonObject;
	}
	
	/**
	 * 提交基金转换数据
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/submitConvert.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public JSONObject submitConvert(HttpServletRequest request) throws IOException{
		JSONObject jsonObject = new JSONObject();
		try {
			HttpSession session = request.getSession();
			String permissionId = request.getParameter("permissionId");	//页面权限
			if(permissionId == null || permissionId.length()==0){
				permissionId = "";
			}
			
			Employee emp = SessionUtils.getEmployee();
			String operatorId = emp.getID();
			String trustType = request.getParameter("trustType"); // 委托方式
			String custno = request.getParameter("custno"); // 客户号
			String tradeacco = request.getParameter("hidtradeacco"); // 交易账号
			String fundid = request.getParameter("fundid"); // 基金代码
			String ofundid = request.getParameter("ofundid"); // 基金代码
			String subAmt = request.getParameter("subAmt"); // 申请份额
			String largeflag = request.getParameter("largeflag");	//巨额赎回标志
			String contact = request.getParameter("hidcontact"); // 经办人
			String contidno = request.getParameter("hidcontidno"); // 经办人证件号码
			String contidtp = request.getParameter("hidcontidtp"); // 经办人证件类型
			String checkno = request.getParameter("checkno"); // 主管编号
			String contractsign = request.getParameter("contractsign");// 是否归档
			String isoriginal = request.getParameter("isoriginal");// 是否原件
			if (!"1".equals(contractsign)) {
				isoriginal = "";
			}
			// 交易表单原件
			String istradeform = request.getParameter("istradeform") == null ? ""
					: request.getParameter("istradeform");
			
			String serialNo = Sequences.getPK();// 流水号
			
			TradeDto dto = new TradeDto();
			TradeDto returnDto = new TradeDto();
			
			dto.setOperatorId(operatorId);
			dto.setPermissionId(permissionId);
			dto.setSerialno(serialNo);
			dto.setTrustType(trustType);
			dto.setCustno(custno);
			dto.setTradeacco(tradeacco);
			dto.setFundid(fundid);
			dto.setOfundid(ofundid);
			dto.setSubAmt(subAmt);
			dto.setLargeflag(largeflag);
			dto.setContact(contact);
			dto.setContidno(contidno);
			dto.setContidtp(contidtp);
			dto.setCheckno(checkno);
			
			
			ProductInfoDto prodto=new ProductInfoDto();
			prodto.setFuncode(fundid);
			
			SearchParam sp =new SearchParam();
			sp.getSp().put("fundid", fundid);
			Page<ProductInfoDto> proList = fundInfoService.getOrderFundInfoList(sp);
			String contractversion="";		
			if(proList!=null && proList.getItems().size()>0){
				ProductInfoDto pro=(ProductInfoDto)proList.getItems().get(0);
				contractversion=pro.getContractversion()==null?"":pro.getContractversion();
			}		
			
			
			Page<EleContractDto> eleContractDto = eleService.getEleContractList(sp);
			//获取IP地址
			String ip = request.getHeader("x-forwarded-for");     
			if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
				ip = request.getHeader("Proxy-Client-IP");        
			}        
			if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
				ip = request.getHeader("WL-Proxy-Client-IP");       
			}        
			if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
				ip = request.getRemoteAddr();        
			}
			
			ContractSignDto contractSignDto = new ContractSignDto();
			if(eleContractDto != null && eleContractDto.getItems().size() > 0){
				//合同版本号
				contractversion = eleContractDto.getItems().get(0).getContractversion();
				contractSignDto.setSerialNo(Sequences.getPK());//序列号
				contractSignDto.setCustNo(custno);//客户号
				contractSignDto.setTradeAcco(tradeacco);//交易账号
				contractSignDto.setFundId(fundid);
				contractSignDto.setContractVer(contractversion);//合同版本
				contractSignDto.setContractTp("2");//合同类型
				contractSignDto.setSignChannel("0");//签署途径
				contractSignDto.setSignMachine(ip);//合同签署机器IP
				
				contractSignDto = tradeManagerService.contractSign(contractSignDto);
			}else{
				if(!"".equals(contractversion)){
					contractSignDto.setSerialNo(Sequences.getPK());//序列号
					contractSignDto.setCustNo(custno);//客户号
					contractSignDto.setTradeAcco(tradeacco);//交易账号
					contractSignDto.setFundId(fundid);
					contractSignDto.setContractVer(contractversion);//合同版本
					contractSignDto.setContractTp("2");//合同类型
					contractSignDto.setSignChannel("0");//签署途径
					contractSignDto.setSignMachine(ip);//合同签署机器IP
					contractSignDto = tradeManagerService.contractSign(contractSignDto);
				}
			}
			
			try{
				TradeDto datadto=new TradeDto();
				datadto.setSerialno(Sequences.getPK());
				datadto.setOldserialno(serialNo);
				datadto.setCustno(custno);
				datadto.setContractsign(contractsign);
				datadto.setIsoriginal(isoriginal);
				datadto.setIstradeform(istradeform);
				tradeManagerService.insertTradeData(datadto);
			}catch(Exception e){
				System.out.print("插入交易资料异常："+e);
				e.printStackTrace(); 
			}
			
			//基金转换
			tradeManagerService.convert(dto);
			
			returnDto = dto;
			
			if(returnDto != null){
				jsonObject.put("serialno", returnDto.getSerialno());
				jsonObject.put("errCode", returnDto.getErrcode());
				jsonObject.put("errMsg", returnDto.getErrmsg());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("errCode", "9999");
			jsonObject.put("errMsg", e.toString());
		}
		return jsonObject;
	}
	
	/**
	 * 认购、申购、赎回交易批量导入
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/importData.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public JSONObject importData(@RequestParam(value="lefile",required=true)MultipartFile multipartFile , HttpServletRequest request ,HttpServletResponse response, ModelAndView model) throws IOException{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("resultCode","0000");
		jsonObject.put("resultMsg","正在导入");
		jsonObject.put("importTotal", 0);
		jsonObject.put("successTotal", 0);
		jsonObject.put("errorTotal",0);
		jsonObject.put("errorList", null);
		int ret = 0;
		TradeDto dto = null;
		try {
			HSSFWorkbook wb = new HSSFWorkbook(multipartFile.getInputStream());
			HSSFSheet sheet = wb.getSheetAt(0);
			int rowCount = sheet.getLastRowNum()+1;
			int cellCount = sheet.getRow(0).getPhysicalNumberOfCells();
			if ((cellCount != 14 && cellCount != 15) || rowCount <= 1) {
				jsonObject.put("resultCode","9999");
				jsonObject.put("resultMsg","导入模板格式不对！");
				return jsonObject;
			}else{
				TradeDto[] dtos = new TradeDto[rowCount-1];
				for(int i=1;i<rowCount;i++){
					dto=new TradeDto();
					int count=0;//判断如果用户是清除内容,而不是删除数据,则不用读取
					for(int j=0;j<cellCount;j++){
						HSSFRow row5 = sheet.getRow(i); // 获得工作薄的第五行
						HSSFCell cel = row5.getCell(j);// 获得第五行的第四个单元格
						if (cel == null) {
							count++;
							continue;
						}
						
						String cellValue = ExcelUtil.getCellValue(cel);
						if(StringUtils.isEmpty(cellValue)){
							cellValue="";
						}else{
							cellValue = cellValue.trim();
						}
						
						if(j==0){//基金账号
//							String fundacct= cel.getStringCellValue() == null ? "" : cel.getStringCellValue();
							dto.setFundacco(cellValue);
						}else if(j==1){//交易账号
//							String tradeacco = cel.getStringCellValue() == null ? "" : cel.getStringCellValue();
							dto.setTradeacco(cellValue);
							
						}else if(j==2){//客户名称
//							String custname = cel.getStringCellValue() == null ? "" : cel.getStringCellValue();
							dto.setCustname(cellValue);
							
						}else if(j==3){//产品名称
//							String fundname = cel.getStringCellValue() == null ? "" : cel.getStringCellValue();
							dto.setFundname(cellValue);
							
						}else if(j==4){//产品代码
//							String fundcode = cel.getStringCellValue() == null ? "" : cel.getStringCellValue();
							dto.setFundid(cellValue);
						}else if(j==5){//交易类型
							String dsapkind = cellValue;
							dto.setDsapkindName(dsapkind);
							if("认购".equals(dsapkind.trim())){
								dsapkind="020";
							}else if("申购".equals(dsapkind.trim())){
								dsapkind="022";
							}else if("赎回".equals(dsapkind.trim())){
								dsapkind="024";
							} 
							dto.setDsapkind(dsapkind);
							
						}else if(j==6){//经办人
//								String contact = cel.getStringCellValue() == null ? "" : cel.getStringCellValue();
								dto.setContact(cellValue);
						} else if(j==7){//申请金额
							String subAmt = cellValue;
							dto.setStrSubAmt(subAmt.trim());
							dto.setStrSubAmtNum(subAmt.trim());
							try {
								dto.setSubAmt(subAmt.replaceAll(",",""));
								dto.setSubAmtNum(Double.parseDouble(subAmt.replaceAll(",","")));
							} catch (NumberFormatException e) {
								dto.setSubAmt("0");
								dto.setSubAmtNum(0);
							}
							
							 
						}else if(j==8){//赎回份额
							String subQuty = cellValue;
							dto.setStrsubQuty(subQuty.trim());
							try {
								dto.setSubQuty((subQuty.replaceAll(",", "")));
								dto.setSubQutyNum(Double.parseDouble(subQuty.replaceAll(",","")));
							} catch (Exception e) {
								dto.setSubQuty("0");
								dto.setSubQutyNum(0);
							} 
							
						}else if(j==9){//巨额赎回
							String largeflag = cellValue;
							dto.setStrlargeflag(largeflag.trim());
							if("".equals(largeflag)){
								dto.setLargeflag(largeflag);
							}else if("取消".equals(largeflag.trim())){
								dto.setLargeflag("0");
							}else if("顺延".equals(largeflag.trim())){
								dto.setLargeflag("1");
							}else{
								dto.setLargeflag(largeflag);
							}
							 
						}else if(j==10){//合同签署
//							String contractsign = cel.getStringCellValue() == null ? "" : cel.getStringCellValue();
							dto.setContractsign(cellValue);
							dto.setContractsignName(cellValue);
							 
						}else if(j==11){//合同是否原件
//							String isoriginal = cel.getStringCellValue() == null ? "" : cel.getStringCellValue();
							dto.setIsoriginal(cellValue);
							dto.setIsoriginalName(cellValue);
							 
							 
						}else if(j==12){//交易表单是否原件
//							String istradeform = cel.getStringCellValue() == null ? "" : cel.getStringCellValue();
							dto.setIstradeform(cellValue);
							dto.setIstradeformName(cellValue);
							 
						}else if(j==13){//授权主管
//							String checkno = cel.getStringCellValue() == null ? "" : cel.getStringCellValue();
							dto.setCheckno(cellValue);
						}
						
						//判断当前这一行的任意一列有数据都会进入条件,则在下面的判断则会进入判断
						/*if(!"".equals(cel.getStringCellValue()) && cel.getStringCellValue() != null && cel.getStringCellValue().length()>0){
							count++;
						}*/
						if(!StringUtils.isEmpty(cellValue) && cellValue.length() > 0){
							count++;
						}
					}
					
					if(count>0){//判断当前这一行的任意一列有数据都会添加到dto,否则把当前的dto把添加到dtos中
						dto.setExcelrowid((i+1)+"");
						dtos[ret] = dto;
						dto = null;
						ret++;
					}
				}
				
				//1、验证EXCEL关键数据是否为空
				jsonObject.putAll(blisempty(dtos,request,response));
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resultCode","9999");
			jsonObject.put("resultMsg", "模板格式或数据不正确，请检查！");
			return jsonObject;
		}
//		String returnErrorMsg[]=retrunMsg.split("#");
//		jsonObject.put("errorMsg",returnErrorMsg[0]);
//		jsonObject.put("fileNmae",returnErrorMsg[1]);
		return jsonObject;  
	}
	
	

    //验证excel关键数据是否为空
    public JSONObject  blisempty(TradeDto[] dtos,HttpServletRequest request,HttpServletResponse response) throws java.text.ParseException{
    	String retMsg="";
    	String errCode="";
    	String errMsg="";
    	String tano="";
    	String invtp="";
    	List list=new ArrayList();
    	JSONObject resultObj = new JSONObject();
    	resultObj.put("importTotal", 0);
    	resultObj.put("successTotal", 0);
    	resultObj.put("errorTotal",0);
    	resultObj.put("errorList", null);
    	
    	int total=0;
    	
        String operatorId="";
		
		List<ParameterVo> paradto=commonService.getParameterListPage(ParameterConstant.PARAM_PMST_SYSTEM,"TRADEBOPERATOR");
		if(paradto!=null&&paradto.size()>0){
			operatorId=paradto.get(0).getPmco();
		}
    	for(int i=0;i<dtos.length;i++){
    		TradeDto dto = dtos[i];
    		if(null!=dto){
	    		TradeDto returnDto = new TradeDto();
				FundInfoVo funddto = fundInfoService.getFundInfo(dto.getFundid());
	    		String msg="";
	    		String fundacco=dto.getFundacco()==null?"":dto.getFundacco();
	    		String tradeacco=dto.getTradeacco()==null?"":dto.getTradeacco();
	    		if(!"".equals(tradeacco)){//判断如果导入文件的交易账号为空,则失败导出时,交易账号也是为空
	    			dto.setTradeIsNull("N");
	    		}
	    		if((fundacco==null||"".equals(fundacco)) &&
	    			(null==tradeacco ||"".equals(tradeacco))){
	    		   if("".equals(msg)){
	    			msg="基金账号或者交易账号为空";
	    			dto.setIsErrrow("Y");
	    		  }
	    		}else{
	    			//同时不为空才取交易账号
	    			if((fundacco!=null &&!"".equals(fundacco)) &&
	    	    		(null!=tradeacco &&!"".equals(tradeacco))){
	    				msg="基金账号,交易账号只能填写一个";
	    				dto.setIsErrrow("Y");
	    			}else{
	    			
	    			//根据交易账号或者基金账号查询客户资料
	    			OpenAccountDto openAccdto = queryManager.tradeaccoQry(tradeacco,fundacco);
	    			List contList = null;	//经办人列表
	    			String contvalidate="";
	    			if(openAccdto != null){
	    				errCode = openAccdto.getErrcode();
	    				if(errCode != null && errCode.equals("0000")){
	    					dto.setCustno(openAccdto.getCustno());
	    					dto.setTradeacco(openAccdto.getTradeacco());
	    				
	    					
	    					
	    					//经办人信息 
	    					contList = openAccdto.getContList();
	    					int count=0;
	    					
	    					if(contList!=null&&contList.size()>0){
	    						for(int k=0;k<contList.size();k++){
	    							ContactDto contactDto = (ContactDto)contList.get(k);
	    							if(null == contactDto){
	    								continue;
	    							}
	    							logger.info("contact:"+contactDto.getContact()+",excelcontact:"+dto.getContact());
	    							if(contactDto.getContact().equals(dto.getContact())){
	    								String contact = contactDto.getContact();
	    				   				if(contact == null || contact.length() == 0){
	    				   					contact = "";
	    				   				}
	    				   				String contidno = contactDto.getContidno();
	    				   				if(contidno == null || contidno.length() == 0){
	    				   					contidno = "";
	    				   				}
	    				   				String contidtp = contactDto.getContidtp();
	    				   				if(contidtp == null || contidtp.length() == 0){
	    				   					contidtp = "";
	    				   				}
	    				   				
	    				   				dto.setContact(contact);
	    	    						dto.setContidno(contidno);
	    	    						dto.setContidtp(contidtp);
	    	    						contvalidate=contactDto.getContvalidate();
	    	    						count++;
	    							}
	    						}
	    						 
	    						
	    					} 
	    					logger.info("经办人信息：contact："+dto.getContact()+",contidno:"+dto.getContidno()+",contidtp:"+dto.getContidtp()
	    							+",contvalidate:"+contvalidate+",第"+dto.getExcelrowid()+"行"+",count:"+count);
	    					
	    					tano = openAccdto.getTano();
	    					if(tano == null)tano = "";
	    					
	    					invtp=openAccdto.getInvtp();
	    					logger.info("客户类型："+invtp);
	    					
	    					if(count==0&&"0".equals(invtp)){
	    						msg="机构客户经办人不存在或者为空";
								dto.setIsErrrow("Y");
	    					}else{
	    					
								if(count>1&&"0".equals(invtp)){
									msg="机构客户存在多个名字相同的经办人";
									dto.setIsErrrow("Y");
								}else if("0".equals(invtp)&&count==1){
									SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
									Date d=new Date();
									Date vidate=null;
									vidate = sdf.parse(contvalidate);
									if(d.getTime()>vidate.getTime()){
										msg="经办人证件已过期";
										dto.setIsErrrow("Y");
									}
								}
	    					}
	    					//如果是个人用户
	    					if("1".equals(invtp)){
	    						dto.setContact("");
	    					}
	    					//姓名匹配
	    					logger.info("Excel custname:"+dto.getCustname()+",数据库 custname:"+openAccdto.getInvnm());
	    					String custname=dto.getCustname()==null?"":dto.getCustname().trim();
	    					if(!openAccdto.getInvnm().equals(custname)){
	    						if("".equals(msg)){
	    			   				msg="EXCEL中客户名称与数据库中客户名称不匹配";
	    			   				dto.setIsErrrow("Y");
	    			   			 }else{
	    			   				msg=msg+"、EXCEL中客户名称与数据库中客户名称不匹配";
	    			   				dto.setIsErrrow("Y");
	    			   			 }
	    					}
	    					
	    				}else if(errCode != null && errCode.equals("9999")){
	    					errMsg = openAccdto.getErrmsg();
	    					if(errMsg == null || errMsg.length() == 0){
	    						errMsg = "基金账号,或者交易账号查询不存在";
	    						
	    					}
	    					dto.setIsErrrow("Y");
	    					msg=errMsg;
	    				} else {
	    					errMsg = openAccdto.getErrmsg();
	    					if(errMsg == null || errMsg.length() == 0){
	    						errMsg = "基金账号,或者交易账号查询不存在";
	    					}
	    					dto.setIsErrrow("Y");
	    					msg=errMsg;
	    				}
	    				
	    			}else{
	    				msg="基金账号或者交易账号不存在";
	    				dto.setIsErrrow("Y");
	    			}
	    			
	    			}
	    		}
	    		
	    		
	    		if(dto.getFundid()==null||"".equals(dto.getFundid())){
	   			 if("".equals(msg)){
	   				msg="基金代码为空";
	   				dto.setIsErrrow("Y");
	   			 }else{
	   				msg=msg+"、基金代码为空";
	   				dto.setIsErrrow("Y");
	   			 }
	    		}else{
	    			 
	    			if(funddto == null){
	    				if("".equals(msg)){
	    	   				msg="基金代码不存在";
	    	   				dto.setIsErrrow("Y");
	    	   			 }else{
	    	   				msg=msg+"、基金代码不存在";
	    	   				dto.setIsErrrow("Y");
	    	   			 }
	    			}
	    		}
	    		
	    		if(!"020".equals(dto.getDsapkind())
	    				&&!"022".equals(dto.getDsapkind())&&!"024".equals(dto.getDsapkind())){
	      			 if("".equals(msg)){
	      				msg="交易类型错误或者为空";
	      				dto.setIsErrrow("Y");
	      			 }else{
	      				msg=msg+"、交易类型错误或者为空";
	      				dto.setIsErrrow("Y");
	      			 }
	       		}
	    		if("020".equals(dto.getDsapkind())||"022".equals(dto.getDsapkind())||"024".equals(dto.getDsapkind())){
	    			if("020".equals(dto.getDsapkind())||"022".equals(dto.getDsapkind())){
	        			 if(dto.getSubAmtNum()==0){
	        				 if("".equals(msg)){
	        	      				msg="申请金额错误或者为空";
	        	      				dto.setIsErrrow("Y");
	        	      			 }else{
	        	      				msg=msg+"、申请金额错误或者为空";
	        	      				dto.setIsErrrow("Y");
	        	      			 } 
	        			 }else{
	        				 if(funddto != null){
	        	    				if("020".equals(dto.getDsapkind())){//认购
	        	    					if(dto.getSubAmtNum() < funddto.getMinSubAmt()){
	        	    						if("".equals(msg)){
	        	           	      				msg="认购金额不能小于最低限额:"+funddto.getMinSubAmt();
	        	           	      				dto.setIsErrrow("Y");
	        	           	      			 }else{
	        	           	      				msg=msg+"、认购金额不能小于最低限额:"+funddto.getMinSubAmt();
	        	           	      				dto.setIsErrrow("Y");
	        	           	      			 }
	        	        				}
	        	    				}else if("022".equals(dto.getDsapkind())){//申购
	        	    					if(dto.getSubAmtNum()<funddto.getMinBidAmt()){
	        	    						if("".equals(msg)){
	        	           	      				msg="申购金额不能小于最低限额:"+funddto.getMinBidAmt();
	        	           	      				dto.setIsErrrow("Y");
	        	           	      			 }else{
	        	           	      				msg=msg+"、申购金额不能小于最低限额:"+funddto.getMinBidAmt();
	        	           	      				dto.setIsErrrow("Y");
	        	           	      			 }
	        	        				}
	        	    				}
	        	    				
	        	    			}
	        			 }
	        		}
	    			if("024".equals(dto.getDsapkind())){
	       			 if(dto.getSubQutyNum() == 0){
	       				 if("".equals(msg)){
	       	      				msg="申请份额错误或者为空";
	       	      				dto.setIsErrrow("Y");
	       	      			 }else{
	       	      				msg=msg+"、申请份额错误或者为空";
	       	      				dto.setIsErrrow("Y");
	       	      			 } 
	       			 }
	       		  }
	    			
	    		}
	    		if("024".equals(dto.getDsapkind())){
					if(!"0".equals(dto.getLargeflag())&&!"1".equals(dto.getLargeflag())){
						if("".equals(msg)){
			 				msg="巨额赎回类型为空或者错误";
			 				dto.setIsErrrow("Y");
			 			 }else{
			 				msg=msg+"、巨额赎回类型为空或者错误";
			 				dto.setIsErrrow("Y");
			 			 }
					}
	    		}
	    		
	    		if("020".equals(dto.getDsapkind())||"022".equals(dto.getDsapkind())){
	    		
		    		if(dto.getContractsign()==null||"".equals(dto.getContractsign())){
		    			if("".equals(msg)){
			 				msg="合同签署类型为空";
			 				dto.setIsErrrow("Y");
			 			 }else{
			 				msg=msg+"、合同签署类型为空";
			 				dto.setIsErrrow("Y");
			 			 }
		    		}else{
		    			String contractsign=dto.getContractsign();
		    			if("无需".equals(contractsign)){
		    				 dto.setContractsign("0");
		    			}else if("纸质合同".equals(contractsign)){
		    				dto.setContractsign("1");
		    			}else if("电子合同".equals(contractsign)){
		    				dto.setContractsign("2");
		    			}else{
		    				if("".equals(msg)){
		    	 				msg="合同签署类型错误";
		    	 				dto.setIsErrrow("Y");
		    	 			 }else{
		    	 				msg=msg+"、合同签署类型错误"; 
		    	 				dto.setIsErrrow("Y");
		    	 			 }
		    			}
		    		}
	    		
	    			//如果是纸质合同
	    			if("1".equals(dto.getContractsign())){
	    				if(dto.getIsoriginal()==null||"".equals(dto.getIsoriginal())){
	    					if("".equals(msg)){
	    		 				msg="纸质合同需要填写合同是否原件列";
	    		 				dto.setIsErrrow("Y");
	    		 			 }else{
	    		 				msg=msg+"、纸质合同需要填写合同是否原件列";
	    		 				dto.setIsErrrow("Y");
	    		 			 }
	    				}else{
	    					if("是".equals(dto.getIsoriginal())){
	    	    				 dto.setIsoriginal("Y");
	    	    			}else if("否".equals(dto.getIsoriginal())){
	    	    				dto.setIsoriginal("N");
	    	    			}else{
	    	    				if("".equals(msg)){
	    	    	 				msg="合同是否原件值错误";
	    	    	 				dto.setIsErrrow("Y");
	    	    	 			 }else{
	    	    	 				msg=msg+"、合同是否原件值错误";
	    	    	 				dto.setIsErrrow("Y");
	    	    	 			 }
	    	    			}
	    				}
	    			}
	    		}
	    		
	    		if(dto.getIstradeform()==null||"".equals(dto.getIstradeform())){
					if("".equals(msg)){
		 				msg="交易表单是否原件列为空";
		 				dto.setIsErrrow("Y");
		 			 }else{
		 				msg=msg+"、交易表单是否原件列为空";
		 				dto.setIsErrrow("Y");
		 			 }
	    		}else{
	    			if("是".equals(dto.getIstradeform())){
	   				 dto.setIstradeform("Y");
	   			}else if("否".equals(dto.getIstradeform())){
	   				dto.setIstradeform("N");
	   			}else{
	   				if("".equals(msg)){
	   	 				msg="交易表单是否原件值错误";
	   	 				dto.setIsErrrow("Y");
	   	 			 }else{
	   	 				msg=msg+"、交易表单是否原件值错误";
	   	 				dto.setIsErrrow("Y");
	   	 			 }
	   			}
	    		}
	    		
	    		
	    		if(!"".equals(msg)){
	    			if("".equals(retMsg)){
	    				retMsg="第"+dto.getExcelrowid()+"行："+msg+";<br>";
	    				
	    			}else{
	    				retMsg=retMsg+"第"+dto.getExcelrowid()+"行："+msg+";<br>";
	    			}
	    		}
	    		String excelMsg=msg;
	    		 
	    		if(!"Y".equals(dto.getIsErrrow())){
	    			dto.setOperatorId(operatorId);
	    			returnDto=subinfo(dto,request);
	    			if(returnDto != null){
	    				errCode = returnDto.getErrcode();
	    				errMsg = returnDto.getErrmsg();
	    			}
	    			if(!"0000".equals(errCode)){
	    				dto.setIsErrrow("Y");
	    				
	    			
		    			if("".equals(retMsg)){
		    				retMsg="第"+dto.getExcelrowid()+"行："+errCode+"-"+errMsg+";<br>";
		    				
		    			}else{
		    				retMsg=retMsg+"第"+dto.getExcelrowid()+"行："+errCode+"-"+errMsg+";<br>";
		    			}
		    			//在导出错误EXCEL中增加错误信息列
		    			if("".equals(excelMsg)){
		    				excelMsg=errCode+"-"+errMsg;
		    			}else{
		    				excelMsg=excelMsg+"、"+errCode+"-"+errMsg;
		    			}
		    			
	    			}else{
	    				total++;
	    			}
	    		}
	    		
	    		if("Y".equals(dto.getIsErrrow())){
	    			dto.setErrmsg(excelMsg);
	    			list.add(dto);
	    		} 
    		}
    	
    	}
    	
    	String filePath="";
    	if(list.size()>0){
    		filePath=exportExcel(response,list);
    	}
    	
    	resultObj.put("importTotal", (total+list.size()));
    	resultObj.put("successTotal", total);
    	resultObj.put("errorTotal", list.size());
    	resultObj.put("errorList", list);
    	resultObj.put("resultMsg", "导入操作完成");
    	resultObj.put("filePath",filePath);
    	/*String allmsg="导入操作完成。<br>导入成功："+total+"条,导入失败："+list.size()+"条";
    	if(!"".equals(retMsg)){
    		allmsg=allmsg+"<br><br><br>错误信息：<br>"+retMsg+"#"+filePath;
    	}
    	//retMsg="导入操作完成。<br>导入成功："+total+"条,导入失败："+list.size()+"条<br><br><br>错误信息：<br>"+retMsg;
    	logger.info("返回的错误信息："+allmsg);*/
    	logger.info("返回的错误信息："+resultObj.toString());
    	return resultObj;
    }
    
    public TradeDto subinfo(TradeDto dto,HttpServletRequest request){

 		TradeDto subdto=new TradeDto();
 		
 		TradeDto returnDto = new TradeDto();
 
 		String serialNo = Sequences.getPK();//流水号
		//认购
		if("020".equals(dto.getDsapkind())||"022".equals(dto.getDsapkind())||"024".equals(dto.getDsapkind())){
			subdto.setOperatorId(dto.getOperatorId().trim());
			subdto.setPermissionId(TransCodeConstant.TRANS_CODE_8021);
			subdto.setSerialno(serialNo);
			subdto.setTrustType("3");
			subdto.setCustno(dto.getCustno().trim());
			subdto.setTradeacco(dto.getTradeacco().trim());
			subdto.setFundid(dto.getFundid().trim());
			subdto.setSubAmt(dto.getSubAmt());
			 
			subdto.setContact(dto.getContact()==null?"":dto.getContact().trim());
			subdto.setContidno(dto.getContidno()==null?"":dto.getContidno().trim());
			subdto.setContidtp(dto.getContidtp()==null?"":dto.getContidtp().trim());
			subdto.setCheckno(dto.getCheckno());
			
		 
			
			if("024".equals(dto.getDsapkind())){
				subdto.setSubAmt(dto.getSubQuty());
				subdto.setLargeflag(dto.getLargeflag());
			}else{
 			 
				//签署电子合同
				//查询新版电子合同
				ProductInfoDto prodto=new ProductInfoDto();
				prodto.setFuncode(dto.getFundid());
				
				SearchParam sp =new SearchParam();
				sp.getSp().put("fundid", dto.getFundid());
				Page<ProductInfoDto> proList = fundInfoService.getOrderFundInfoList(sp);
				String contractversion="";		
				if(proList!=null && proList.getItems().size()>0){
					ProductInfoDto pro=(ProductInfoDto)proList.getItems().get(0);
					contractversion=pro.getContractversion()==null?"":pro.getContractversion();
				}		
						
				Page<EleContractDto> eleContractDto = eleService.getEleContractList(sp);
				//获取IP地址
				String ip = request.getHeader("x-forwarded-for");     
				if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
					ip = request.getHeader("Proxy-Client-IP");        
				}        
				if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
					ip = request.getHeader("WL-Proxy-Client-IP");       
				}        
				if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
					ip = request.getRemoteAddr();        
				}
				
				ContractSignDto contractSignDto = new ContractSignDto();
				if(eleContractDto != null && eleContractDto.getItems().size() > 0){
					//合同版本号
					contractversion = eleContractDto.getItems().get(0).getContractversion();
					contractSignDto.setSerialNo(Sequences.getPK());//序列号
					contractSignDto.setCustNo(dto.getCustno());//客户号
					contractSignDto.setTradeAcco(dto.getTradeacco());//交易账号
					contractSignDto.setFundId(dto.getFundid());
					contractSignDto.setContractVer(contractversion);//合同版本
					contractSignDto.setContractTp("2");//合同类型
					contractSignDto.setSignChannel("0");//签署途径
					contractSignDto.setSignMachine(ip);//合同签署机器IP
					
					contractSignDto = tradeManagerService.contractSign(contractSignDto);
				}else{
					if(!"".equals(contractversion)){
						contractSignDto.setSerialNo(Sequences.getPK());//序列号
						contractSignDto.setCustNo(dto.getCustno());//客户号
						contractSignDto.setTradeAcco(dto.getTradeacco());//交易账号
						contractSignDto.setFundId(dto.getFundid());
						contractSignDto.setContractVer(contractversion);//合同版本
						contractSignDto.setContractTp("2");//合同类型
						contractSignDto.setSignChannel("0");//签署途径
						contractSignDto.setSignMachine(ip);//合同签署机器IP
						contractSignDto = tradeManagerService.contractSign(contractSignDto);
					}
				}
			}
			
			try{
				TradeDto datadto=new TradeDto();
				datadto.setSerialno(Sequences.getPK());
				datadto.setOldserialno(serialNo);
				datadto.setCustno(dto.getCustno());
				if(!"024".equals(dto.getDsapkind())){
					datadto.setContractsign(dto.getContractsign());
					datadto.setIsoriginal(dto.getIsoriginal());
				}
				datadto.setIstradeform(dto.getIstradeform());
				tradeManagerService.insertTradeData(datadto);
			}catch(Exception e){
				e.printStackTrace();
				System.out.print("插入交易资料异常："+e);
			}
			
			if ("020".equals(dto.getDsapkind())) {
				//认购
				tradeManagerService.subscribe(subdto);
				returnDto = subdto;
			} else if ("022".equals(dto.getDsapkind())) {
				tradeManagerService.purchase(subdto);
				returnDto = subdto;
			}else if ("024".equals(dto.getDsapkind())) {
				tradeManagerService.redeem(subdto);
				returnDto = subdto;
			}
		}
		return returnDto;
 		 
    }
    
    /**
	 * 交易类复核查询
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/queryTradeDate.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public Page<TradeCheckDto> queryTradeDate(SearchParam sp,HttpServletRequest request) throws IOException{
		HttpSession session = request.getSession();
		Employee emp = SessionUtils.getEmployee();
		String operatorId = emp.getID();
		sp.getSp().put("operatorId", operatorId);
		String begindate = (String)sp.getSp().get("begindate") == null ? "" : (String)sp.getSp().get("begindate");
		String enddate = (String)sp.getSp().get("enddate") == null ? "" : (String)sp.getSp().get("enddate");;
		sp.getSp().put("begindate", begindate.replace("-", ""));
		sp.getSp().put("enddate", enddate.replaceAll("-", ""));
		//交易类复核查询
		Page<TradeCheckDto> list = new Page<TradeCheckDto>();
		try {
			list = queryManager.tradeCheckQryListPage(sp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 交易类复核查询批量处理列表
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/queryBatchList.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public Page<TradeCheckDto> queryBatchList(SearchParam sp,HttpServletRequest request) throws IOException{
		SearchParam sParam = new SearchParam();
    	sParam.setSp(new HashMap<String,Object>());
    	sParam.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
    	sParam.getSp().put("allPmky", "TRADEBOPERATOR");
    	List<ParameterVo> paradto = commonService.getParameterListPage(sParam);
		if(paradto != null && paradto.size() > 0){
			sp.getSp().put("operatorId", paradto.get(0).getPmco());
		}
		String begindate = (String)sp.getSp().get("begindate") == null ? "" : (String)sp.getSp().get("begindate");
		String enddate = (String)sp.getSp().get("enddate") == null ? "" : (String)sp.getSp().get("enddate");;
		sp.getSp().put("begindate", begindate.replace("-", ""));
		sp.getSp().put("enddate", enddate.replaceAll("-", ""));
		
		Page<TradeCheckDto> list = new Page<TradeCheckDto>();
		try {
			//交易类复核查询批量处理列表
			list = queryManager.queryBatchListPage(sp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 交易类复核记录数查询
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/getTradeCheckCount.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public TradeCheckDto getTradeCheckCount(HttpServletRequest request) throws IOException{
		TradeCheckDto tradeCheckDto = null;
		try {
			tradeCheckDto = queryManager.getTradeCheckCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tradeCheckDto;
	}
	
	/**
	 * 交易类复核记录数查询
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/getBatchTradeCheckCount.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public TradeCheckDto getBatchTradeCheckCount(HttpServletRequest request,SearchParam sp) throws IOException{
		TradeCheckDto tradeCheckDto = null;
		try {
			tradeCheckDto = queryManager.getBatchTradeCheckCount(sp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tradeCheckDto;
	}
	
	/**
	 * 交易类复核查询详情页面
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/openTradeCheckDetailQryPage.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ModelAndView openTradeCheckDetailQryPage(ModelAndView model,@RequestParam("serialno")String serialno) throws IOException{
		TradeCheckDto tradeCheckDto = queryManager.tradeCheckDetailQry(serialno);
		BigDecimal subAmt = new BigDecimal(tradeCheckDto.getSubamt().replaceAll(",", ""));
        String subAmtNm = NumberToCN.number2CNMontrayUnit(subAmt);
        tradeCheckDto.setSubamtnm(subAmtNm);
        BigDecimal subQuty = new BigDecimal(tradeCheckDto.getSubquty().replaceAll(",", ""));
        String subQutyNm = NumberToCN.number2CNMontrayUnit(subQuty);
        tradeCheckDto.setSubqutynm(subQutyNm);
		model.addObject("tradeCheckDto", tradeCheckDto);
		model.setViewName("jsp/tradeManager/tradeCheckDetail");
		return model;
	}
	
	
	/**
	 * 交易类复核
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/tradeCheck.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public TradeCheckDto tradeCheck(HttpServletRequest request , @RequestParam("serialno")String serialno,@RequestParam("checkst")String checkst,@RequestParam("permissionId")String permissionId) throws IOException{
		TradeCheckDto dto = new TradeCheckDto();
		HttpSession session = request.getSession();
		Employee emp = SessionUtils.getEmployee();
		String operatorId = emp.getID();
		dto.setSerialno(serialno);
		dto.setCheckst(checkst);
		dto.setPermissionId(permissionId);
		dto.setOperatorId(operatorId);
		try {
			tradeManagerService.tradeCheck(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
	
	
	/**
	 * 交易类复核
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/openTradeBatchQryPage.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ModelAndView openTradeBatchQryPage(ModelAndView model,
											  HttpServletRequest request ,
											  @RequestParam("dsapkind")String dsapkind,
											  @RequestParam("fundid")String fundid,
											  @RequestParam("trustType")String trustType,
											  @RequestParam("begindate")String begindate,
											  @RequestParam("enddate")String enddate) throws IOException{
		
		HttpSession session = request.getSession();
		Employee emp = SessionUtils.getEmployee();
		String operatorId = emp.getID();
		String  batchoperatorId= "";
		SearchParam sParam = new SearchParam();
    	sParam.setSp(new HashMap<String,Object>());
    	sParam.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
    	sParam.getSp().put("allPmky", "TRADEBOPERATOR");
    	List<ParameterVo> paradto = commonService.getParameterListPage(sParam);
		if(paradto != null && paradto.size() > 0){
			batchoperatorId = paradto.get(0).getPmco();
		}
		try {
			model.addObject("dsapkind",dsapkind);
			model.addObject("fundid",fundid);
			model.addObject("trustType",trustType);
			model.addObject("begindate",begindate);
			model.addObject("enddate",enddate);
			model.addObject("opid",batchoperatorId);
			model.addObject("operatorId",operatorId);
			model.setViewName("jsp/tradeManager/tradeBatchQry");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	
	/**
	 * 交易类驳回修改
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/tradeModifyDetailPage.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ModelAndView tradeModifyDetailPage(ModelAndView model,HttpServletRequest request ,@RequestParam("serialno")String serialno) throws IOException{
		try {
			model.addObject("serialno",serialno);
			TradeCheckDto tradeCheckDto = queryManager.tradeCheckDetailQry(serialno);
			BigDecimal subAmt = new BigDecimal(tradeCheckDto.getSubamt().replaceAll(",", ""));
	        String subAmtNm = NumberToCN.number2CNMontrayUnit(subAmt);
	        tradeCheckDto.setSubamtnm(subAmtNm);
	        BigDecimal subQuty = new BigDecimal(tradeCheckDto.getSubquty().replaceAll(",", ""));
	        String subQutyNm = NumberToCN.number2CNMontrayUnit(subQuty);
	        tradeCheckDto.setSubqutynm(subQutyNm);
	        //交易类详细信息
			model.addObject("tradeCheckDto", tradeCheckDto);
			//基金数据
			List<FundInfoVo> fundNameList = fundInfoService.getAllSubFundsArray("", "");
			model.addObject("fundNameList", fundNameList);
			//巨额赎回标志
			List<ParameterVo> largeflagArray = commonService.getParameterListPage(ParameterConstant.PARAM_PMST_SYSTEM,ParameterConstant.PARAM_PMKY_LARGEREDEEMFLAG);
			model.addObject("largeflagArray", largeflagArray);
			model.setViewName("jsp/tradeManager/tradeModifyDetail");
			//分红方式
			List<ParameterVo>  melonmdArray = commonService.getParameterListPage(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_MELONMD);
			model.addObject("melonmdArray", melonmdArray);
			//获取所有销售机构
			List<SeatDto> seatList = queryManager.getSeats();
			model.addObject("seatList", seatList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	

	/**
	 * 交易类驳回修改
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/tradeModify.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public JSONObject tradeModify(HttpServletRequest request) throws IOException{
		JSONObject jsonObject = new JSONObject();
		try {
			Employee emp = SessionUtils.getEmployee();
			
			String operatorId = request.getParameter("operatorId");	//操作员
			if(operatorId == null || operatorId.length()==0){
				operatorId = emp.getID();
			}
				
			String permissionId = request.getParameter("permissionId");	//页面权限
			if(permissionId == null || permissionId.length()==0){
				permissionId = "";
			}
			String custno = request.getParameter("custno");	//客户号
			if(custno == null || custno.length()==0){
				custno = "";
			}
			String tradeacco = request.getParameter("tradeacco");
			if(tradeacco == null || tradeacco.length()==0){
				tradeacco = "";
			}
			String serialno = request.getParameter("serialno");	//流水号
			if(serialno == null || serialno.length()==0){
				serialno = "";
			}
			String modifytype = request.getParameter("modifytype");	//修改状态
			if(modifytype == null || modifytype.length()==0){
				modifytype = "";
			}
			String fundid = request.getParameter("fundid");	//基金代码
			if(fundid == null || fundid.length()==0){
				fundid = "";
			}
			String subamt = request.getParameter("subamt");	//申请金额
			if(subamt == null || subamt.length()==0){
				subamt = "0";
			}
			String subquty = request.getParameter("subquty");	//申请份额
			if(subquty == null || subquty.length()==0){
				subquty = "0";
			}
			String ofundid = request.getParameter("ofundid");	//对方基金代码
			if(ofundid == null || ofundid.length()==0){
				ofundid = "";
			}
			String largeflag = request.getParameter("largeflag");	//巨额赎回标志
			if(largeflag == null || largeflag.length()==0){
				largeflag = "";
			}
			String melonmd = request.getParameter("melonmd");	//分红方式
			if(melonmd == null || melonmd.length()==0){
				melonmd = "";
			}
			String melonpercent = request.getParameter("melonpercent");	//分红比例
			if(melonpercent == null || melonpercent.length()==0){
				melonpercent = "0.00";
			}
			String oldserialno = request.getParameter("oldserialno");	//原申请编号
			if(oldserialno == null || oldserialno.length()==0){
				oldserialno = "";
			}
			String onetpoint = request.getParameter("onetpoint");	//对方网点
			if(onetpoint == null || onetpoint.length()==0){
				onetpoint = "";
			}
			String oseatno = request.getParameter("oseatno");	//对方销售机构代码
			if(oseatno == null || oseatno.length()==0){
				oseatno = "";
			}
			
			String dsapkind=request.getParameter("dsapkind");
			if(dsapkind == null || dsapkind.length()==0){
				dsapkind = "";
			}
			
			
			TradeCheckDto dto = new TradeCheckDto();
			TradeCheckDto returnDto = new TradeCheckDto();
			dto.setOperatorId(operatorId);
			dto.setPermissionId(permissionId);
			dto.setCustno(custno);
			dto.setTradeacco(tradeacco);
			dto.setSerialno(serialno);
			dto.setFundid(fundid);
			dto.setSubamt(subamt);
			dto.setSubquty(subquty);
			dto.setOfundid(ofundid);
			dto.setLargeflag(largeflag);
			dto.setMelonmd(melonmd);
			dto.setMelonpercent(melonpercent);
			dto.setOldserialno(oldserialno);
			dto.setOseatno(oseatno);
			dto.setOnetpoint(onetpoint);
			dto.setModifytype(modifytype);
			
			
			ProductInfoDto prodto=new ProductInfoDto();
			prodto.setFuncode(fundid);
			
			SearchParam sp =new SearchParam();
			sp.getSp().put("fundid", fundid);
			Page<ProductInfoDto> proList = fundInfoService.getOrderFundInfoList(sp);
			String contractversion="";		
			if(proList!=null && proList.getItems().size()>0){
				ProductInfoDto pro=(ProductInfoDto)proList.getItems().get(0);
				contractversion=pro.getContractversion()==null?"":pro.getContractversion();
			}		
			
			
			Page<EleContractDto> eleContractDto = eleService.getEleContractList(sp);
			//获取IP地址
			String ip = request.getHeader("x-forwarded-for");     
			if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
				ip = request.getHeader("Proxy-Client-IP");        
			}        
			if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
				ip = request.getHeader("WL-Proxy-Client-IP");       
			}        
			if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
				ip = request.getRemoteAddr();        
			}
			
			ContractSignDto contractSignDto = new ContractSignDto();
			if(eleContractDto != null && eleContractDto.getItems().size() > 0){
				//合同版本号
				contractversion = eleContractDto.getItems().get(0).getContractversion();
				contractSignDto.setSerialNo(Sequences.getPK());//序列号
				contractSignDto.setCustNo(custno);//客户号
				contractSignDto.setTradeAcco(tradeacco);//交易账号
				contractSignDto.setFundId(fundid);
				contractSignDto.setContractVer(contractversion);//合同版本
				contractSignDto.setContractTp("2");//合同类型
				contractSignDto.setSignChannel("0");//签署途径
				contractSignDto.setSignMachine(ip);//合同签署机器IP
				
				contractSignDto = tradeManagerService.contractSign(contractSignDto);
			}else{
				if(!"".equals(contractversion)){
					contractSignDto.setSerialNo(Sequences.getPK());//序列号
					contractSignDto.setCustNo(custno);//客户号
					contractSignDto.setTradeAcco(tradeacco);//交易账号
					contractSignDto.setFundId(fundid);
					contractSignDto.setContractVer(contractversion);//合同版本
					contractSignDto.setContractTp("2");//合同类型
					contractSignDto.setSignChannel("0");//签署途径
					contractSignDto.setSignMachine(ip);//合同签署机器IP
					contractSignDto = tradeManagerService.contractSign(contractSignDto);
				}
			}
			
			//交易类驳回修改
			tradeManagerService.tradeModify(dto);
			
			returnDto = dto;
			
			if(returnDto != null){
				jsonObject.put("serialno", returnDto.getSerialno());
				jsonObject.put("errCode", returnDto.getErrcode());
				jsonObject.put("errMsg", returnDto.getErrmsg());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("errCode", "9999");
			jsonObject.put("errMsg", e.toString());
		}
		return jsonObject;
	}
	
	/**
	 * 加载基金名称数据
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/loadFundNameList.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public List<FundInfoVo> loadFundNameList() throws IOException{
		//基金名称
		List<FundInfoVo> fundNameList = fundInfoService.getAllSubFundsArray("", "");
		return fundNameList;
	}
	
	/**
	 * 交易撤单查询
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/cancelQryListPage.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public Page<TradeDto> cancelQryListPage(SearchParam sp,HttpServletRequest request) throws IOException{
		Page<TradeDto> list = new Page<TradeDto>();
		try {
			//交易撤单查询
			list = queryManager.cancelQryListPage(sp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 交易撤单提交
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/tradeCancel.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public TradeDto tradeCancel(HttpServletRequest request) throws IOException{
		TradeDto returnDto = new TradeDto();
		try {
			String permissionId = request.getParameter("permissionId");	//页面权限
			if(permissionId == null || permissionId.length()==0){
				permissionId = "";
			}
			Employee emp = SessionUtils.getEmployee();
			String operatorId = emp.getID();
			if(operatorId == null || operatorId.length()==0){
				operatorId = "";
			}
			String trustType = request.getParameter("trustType");	//委托方式
			if(trustType == null || trustType.length()==0){
				trustType = "";
			}
			String custno = request.getParameter("custno");	//客户号
			if(custno == null || custno.length()==0){
				custno = "";
			}
			String tradeacco = request.getParameter("hidtradeacco");	//交易账号
			if(tradeacco == null || tradeacco.length()==0){
				tradeacco = "";
			}
			String oldserialno = request.getParameter("oldserialno");	//原申请编号
			if(oldserialno == null || oldserialno.length()==0){
				oldserialno = "";
			}
			String checkno = request.getParameter("checkno");	//主管编号
			if(checkno == null || checkno.length()==0){
				checkno = "";
			}
			
			String serialNo = Sequences.getPK();//流水号
			
			TradeDto dto = new TradeDto();
			
			dto.setOperatorId(operatorId);
			dto.setPermissionId(permissionId);
			dto.setSerialno(serialNo);
			dto.setTrustType(trustType);
			dto.setCustno(custno);
			dto.setTradeacco(tradeacco);
			dto.setOldserialno(oldserialno);
			dto.setCheckno(checkno);
			
			tradeManagerService.cancel(dto);
			
			returnDto = dto;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnDto;
	}
	
	
	
	/**
	 * 根据权限代码查询是否需要授权
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/queryPermission.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public JSONObject queryPermission(SearchParam sp,HttpServletRequest request) throws IOException{
		JSONObject jsonObject = new JSONObject();
		try {
			queryManager.queryPermission(sp);
			jsonObject.put("isPermission", sp.getSp().get("isPermission"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	 /**
	 * 导出失败数据
	 */
	private String exportExcel(HttpServletResponse response,List list){
		String fileName="";
		String patch="";
		try{
			String title = "基金账号,交易账号,客户名称,产品名称,基金代码,交易类型,经办人,申请金额,申请份额,巨额赎回标志,合同签署,合同是否原件,交易表单是否原件,授权主管工号,错误信息 " ;
			Date date=new Date();
			SimpleDateFormat DF_yyyyMMdd = new SimpleDateFormat("yyyyMMddHHmmss");
			String fileDate=DF_yyyyMMdd.format(date);
		    fileName=fileDate+".xls";
			
			
			List<ParameterVo> paradto=commonService.getParameterAllList(ParameterConstant.PARAM_PMST_SYSTEM,"BATCHNEWPATH");
			if(paradto!=null&&paradto.size()>0){
				patch=paradto.get(0).getPmco();
			}
			
			// 新建一个文件在文件系统中
			File newFile = new File(patch+fileName);
			File newFiles = new File(patch);
			if(!newFiles.exists()){
				newFiles.mkdir();
			}
			
			newFile.createNewFile();
			
			// 第一步，创建一个webbook，对应一个Excel文件  
			HSSFWorkbook wb = new HSSFWorkbook();
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
	        HSSFSheet sheet = wb.createSheet("Sheet1"); 
	        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
	        HSSFRow row = sheet.createRow(0);
	        row.setHeight((short) (20*25));
			String[] titles = title.split(",");
			HSSFCellStyle titleStyle = wb.createCellStyle();
			titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
			HSSFFont titleFont = wb.createFont(); // 创建字体
			titleFont.setFontName("Arial"); // Arial
			titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 加粗
			titleStyle.setFont(titleFont);
			titleStyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
			titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			for (int i = 0; i < titles.length; i++) {
				sheet.setColumnWidth(i, 20*256);
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(titleStyle);
				cell.setCellValue(new HSSFRichTextString(titles[i]));
			}
			// 行数
			int rowCount = 1;
			// 流水号
			HSSFCellStyle contentStyle = wb.createCellStyle();
			contentStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
			for (int i = 0; i < list.size(); i++) {
				TradeDto dto = (TradeDto) list.get(i);
				HSSFRow rows = sheet.createRow(rowCount);
				rows.setHeight((short) (20*20));
				for (int j = 0; j < titles.length; j++) {
					sheet.setColumnWidth(j, 20*256);
					HSSFCell cell = rows.createCell(j);
					cell.setCellStyle(contentStyle);
					if (j == 0) {
						cell.setCellValue(dto.getFundacco()==null?"":dto.getFundacco());
					} else if (j == 1) {
						if("N".equals(dto.getTradeIsNull())){//判断如果导入文件的交易账号为空,则失败导出时,交易账号也是为空
							cell.setCellValue(dto.getTradeacco()==null?"":dto.getTradeacco());
						}
					} else if (j == 2) {
						cell.setCellValue(dto.getCustname()==null?"":dto.getCustname());
					} else if (j == 3) {
						cell.setCellValue(dto.getFundname()==null?"":dto.getFundname());
					} else if (j == 4) {
						cell.setCellValue(dto.getFundid()==null?"":dto.getFundid());
					} else if (j == 5) {
						cell.setCellValue(dto.getDsapkindName()==null?"":dto.getDsapkindName());
					} else if (j == 6) {
						cell.setCellValue(dto.getContact()==null?"":dto.getContact());
					} else if (j == 7) {
						cell.setCellValue(dto.getStrSubAmt()==null?"":dto.getStrSubAmt());
					} else if (j == 8) {
						cell.setCellValue(dto.getStrsubQuty()==null?"":dto.getStrsubQuty());
					} else if (j == 9) {
						cell.setCellValue(dto.getStrlargeflag()==null?"":dto.getStrlargeflag());
					} else if (j == 10) {
						cell.setCellValue(dto.getContractsignName()==null?"":dto.getContractsignName());
					} else if (j == 11) {
						cell.setCellValue(dto.getIsoriginalName()==null?"":dto.getIsoriginalName());
					} else if (j == 12) {
						cell.setCellValue(dto.getIstradeformName()==null?"":dto.getIstradeformName());
					} else if (j == 13) {
						cell.setCellValue(dto.getCheckno()==null?"":dto.getCheckno());
					} else if (j == 14) {
						cell.setCellValue(dto.getErrmsg()==null?"":dto.getErrmsg());
						sheet.setColumnWidth(j, 150*256);
					}
					
				}
				rowCount = rowCount + 1;
			}
			rowCount=1;
			FileOutputStream fout = new FileOutputStream(patch+fileName); 
			wb.write(fout);
		    wb.close();
		    fout.flush();
		    fout.close();
		    logger.info("----TradeManagerController-exportExcel-file-saved-"+patch+fileName);
		}catch(Exception e){
			logger.error("BatchHandleDelegate.exportExcel异常：",e);
		}
		return patch+fileName;
	}
	
	/**
	 * 下载批量导入错误数据模板
	 */
	@RequestMapping(value="/downloadErrorInfo.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	public void downloadErrorInfo(@RequestParam("fileName")String fileName,HttpServletRequest request, HttpServletResponse response){
		try {
			logger.info("----TradeManagerController-downloadErrorInfo-fileName-"+fileName);
            // path是指欲下载的文件的路径。
            File file = new File(fileName);
            logger.info("----TradeManagerController-downloadErrorInfo-file.exists-"+file.exists());
            // 取得文件名。
            String filename = file.getName();
            filename = filename.substring(0,filename.lastIndexOf("."));
            filename = filename+"_批量处理失败数据.xls";

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(fileName));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename="+new String(filename.getBytes(),"ISO8859-1"));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
        	logger.error("----TradeManagerController-downloadErrorInfo-IOException:",ex);
            ex.printStackTrace();
        }
	}
}