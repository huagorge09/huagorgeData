package com.cmwa.ecc.business.controller.account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmwa.ec.base.util.Sequences;
import com.cmwa.ecc.business.commonVo.Employee;
import com.cmwa.ecc.business.entity.accountManger.ContactDto;
import com.cmwa.ecc.business.entity.accountManger.FundBalanceDto;
import com.cmwa.ecc.business.entity.accountManger.OpenAccountDto;
import com.cmwa.ecc.business.entity.accountManger.SeatDto;
import com.cmwa.ecc.business.entity.fundinfo.FundInfoVo;
import com.cmwa.ecc.business.entity.paramvo.ParameterVo;
import com.cmwa.ecc.business.entity.trade.TradeDto;
import com.cmwa.ecc.business.service.account.QueryManager;
import com.cmwa.ecc.business.service.business.EleContractManagerService;
import com.cmwa.ecc.business.service.common.CommonService;
import com.cmwa.ecc.business.service.fundinfo.FundInfoService;
import com.cmwa.ecc.business.service.trade.TradeManagerService;
import com.cmwa.ecc.business.utils.ParameterConstant;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;

/**
 * 转托管控制器
 * @author ex-chenbq
 *
 */
@Controller
@RequestMapping(value="/capitalService/server")
public class TurnToHostConlltroller {
	
	@Autowired
	private CommonService commonService;
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
	@RequestMapping(value="/loadInitData.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public Map<String, Object> loadData() throws IOException{
		//委托方式
		Map<String, Object> result = new HashMap<String, Object>();
		List<ParameterVo> trustTypeArray = commonService.getParameterListPage(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_ACCPTMD);
		result.put("trustTypeArray", trustTypeArray);
		List<SeatDto> seatList = queryManager.getSeats();
		result.put("seatList", seatList);
		return result;
	}
	
	/**
	 * 根据交易账号或者基金账号查询客户资料
	 * @param tradeacco
	 * @param fundacco
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/tradeaccoQryBySimp.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public OpenAccountDto tradeaccoQryBySimp(@RequestParam("tradeacco")String tradeacco, @RequestParam("fundacco")String fundacco) throws IOException{
		OpenAccountDto dto = new OpenAccountDto();
		try {
			dto = queryManager.tradeaccoQry(tradeacco, fundacco);
			dto.setInvtpnm(dto.getInvtp());
			//投资者类别
			SearchParam invtpSp = new SearchParam();
			invtpSp.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
			invtpSp.getSp().put("allPmky", ParameterConstant.PARAM_PMKY_INVTP);
			invtpSp.getSp().put("Qpmco", dto.getInvtp());
			List<ParameterVo> invtpArray = commonService.getParameterListPage(invtpSp);
			dto.setInvtp(invtpArray.get(0).getPmnm());
			//证件类别或CMF证件类别
			SearchParam sp = new SearchParam();
			sp.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
			sp.getSp().put("allPmky", ParameterConstant.PARAM_PMKY_IDTP);
			sp.getSp().put("Qpmco", dto.getIdtp());
			List<ParameterVo> idtpArray = commonService.getParameterListPage(sp);
			dto.setIdtp(idtpArray.get(0).getPmnm());
			List<ContactDto> list = dto.getContList();
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					ContactDto contactDto = list.get(i);
					if(null == contactDto){
						continue;
					}
					SearchParam spParam = new SearchParam();
					spParam.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
					spParam.getSp().put("allPmky", ParameterConstant.PARAM_PMKY_IDTP);
					spParam.getSp().put("Qpmco", contactDto.getContidtp());
					List<ParameterVo> idtpArrays = commonService.getParameterListPage(spParam);
					contactDto.setContidtpName(contactDto.getContidtp());
					contactDto.setContidtp(idtpArrays.get(0).getPmnm());
				}
			}
			
			List<FundBalanceDto> banlanceList = new ArrayList<FundBalanceDto>();
			
			List<FundInfoVo> fundNmArray = fundInfoService.getAllSubFundsArray("", dto.getTano());
			List<FundBalanceDto> fundBalanceList = dto.getFundbalanceList();
			for (int i = 0; i < fundBalanceList.size(); i++) {
				FundBalanceDto fundBalanceDto = fundBalanceList.get(i);
				
				String fundid = fundBalanceDto.getFundid();
    			if(fundNmArray != null && fundNmArray.size()>0){
    				for(int f=0;f<fundNmArray.size();f++){
    					FundInfoVo FundInfoVo = fundNmArray.get(f);
    					if(FundInfoVo.getFundId().equals(fundid)){
    						fundBalanceDto.setFundnm(FundInfoVo.getFundShortNm());
    						banlanceList.add(fundBalanceDto);
						}
					}
   				}
			}
			dto.setFundbalanceList(banlanceList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
	
	/**
	 * 提交单步转托管申请
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/submitRollOut.xhtml",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public JSONObject submitRollOut(HttpServletRequest request) throws IOException{
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
			String subQuty = request.getParameter("subQuty")==null?"0.00":request.getParameter("subQuty"); //转出份额
			String seatno = request.getParameter("seatno");	//对方销售商代码
			String netpoint = request.getParameter("netpoint");	//对方网点号
			String oTradeacco = request.getParameter("oTradeacco");	//对方交易账号
			String oldserialno = request.getParameter("oldserialno")==null?"":request.getParameter("oldserialno");	//申请编号
			String managedSwitchType = request.getParameter("managedSwitchType");	//转托管类型：1，单步；2，转出；3，转入；4，内部
			String contact = request.getParameter("hidcontact"); // 经办人
			String contidno = request.getParameter("hidcontidno"); // 经办人证件号码
			String contidtp = request.getParameter("hidcontidtp"); // 经办人证件类型
			String checkno = request.getParameter("checkno"); // 主管编号
			
			String switchInShare = request.getParameter("switchInShare");//转入份额
			if(switchInShare == null || switchInShare.length()==0){
				switchInShare = "";
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
			dto.setFundid(fundid);
			dto.setSubQuty(subQuty);
			dto.setSeatno(seatno);
			dto.setNetpoint(netpoint);
			dto.setOldserialno(oldserialno);
			dto.setOTradeacco(oTradeacco);
			dto.setTradeacco(tradeacco);
			
			dto.setManagedSwitchType(managedSwitchType);
			dto.setContact(contact);
			dto.setContidno(contidno);
			dto.setContidtp(contidtp);
			dto.setCheckno(checkno);
			dto.setSwitchInShare(switchInShare);
			
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
			if(managedSwitchType.equals("1") || managedSwitchType.equals("4")){
				//单步转托管
				tradeManagerService.simpSwitch(dto);
			}else if(managedSwitchType.equals("2")){
				tradeManagerService.managedSwitchOut(dto);
			}else if(managedSwitchType.equals("3")){
				tradeManagerService.managedSwitchIn(dto);
			}
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
}