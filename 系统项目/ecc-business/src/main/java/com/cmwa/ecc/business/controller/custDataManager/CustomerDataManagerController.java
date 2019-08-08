package com.cmwa.ecc.business.controller.custDataManager;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.cmwa.ec.base.util.Sequences;
import com.cmwa.ecc.business.entity.accountManger.OpenAccountDto;
import com.cmwa.ecc.business.entity.accountManger.RiskLevelDto;
import com.cmwa.ecc.business.entity.changeRecord.CustRisklvelInvprtpDetail;
import com.cmwa.ecc.business.entity.paramvo.ParameterVo;
import com.cmwa.ecc.business.service.accountManger.OpenAccountService;
import com.cmwa.ecc.business.service.changeRecord.CustRisklvelInvprtpDetailService;
import com.cmwa.ecc.business.service.common.CommonService;
import com.cmwa.ecc.business.service.customerDataManager.CustTradeService;
import com.cmwa.ecc.business.service.customerDataManager.CustomerDataManagerService;
import com.cmwa.ecc.business.service.query.ExportDataService;
import com.cmwa.ecc.business.service.widget.WidgetService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.TransCodeConstant;

@Controller
@RequestMapping(value = "service/customerDataManager")
public class CustomerDataManagerController {
	
	@Autowired
	private CustomerDataManagerService customerDataManagerService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private ExportDataService exportDataService;
	
	@Autowired
	private OpenAccountService openAccountService;
	
	@Autowired
	private CustTradeService custTradeService;
	
	@Autowired
	private CustRisklvelInvprtpDetailService custInfoChangeRecordService;
	
	@Autowired
	private WidgetService widgetService;
	
	private Logger logger = Logger.getLogger(CustomerDataManagerController.class.getName());

	
	@RequestMapping(value = "/custDataMgr.do",method=RequestMethod.GET)
	public String goCustomerDataManager() {
		return "jsp/customerDataMgr/customerDataManager";
	}
	/*@RequestMapping(value = "/updatePage.do",method=RequestMethod.GET)
	public String goUpdateDataManager() {
		return "jsp/customerDataMgr/updateDataManager";
	}*/
	@RequestMapping(value = "/queryRisklevlInfo.xhtml",method= {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Page<RiskLevelDto> getRiskLevelInfoListPage(SearchParam sp) throws Exception{
		return customerDataManagerService.getRiskLevelInfoListPage(sp);
	}
	
	@RequestMapping(value="/openDialog.xhtml",method={RequestMethod.GET})
	public ModelAndView openDialog(String method,String custNo){
		return customerDataManagerService.openDialog(method,custNo);
	}
	
	/*获取风险承担能力*/
	@RequestMapping(value ="/queryRisklevlArrayInfo.xhtml",method= {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public List<ParameterVo> queryRiskLevelParameter(SearchParam sp) throws Exception {
		return 	commonService.getParameterListPage(sp);
	}
	/*导出*/
	@RequestMapping(value ="/exportCustData.xhtml",method= {RequestMethod.GET,RequestMethod.POST})
	public void exportRiskLevelData(SearchParam sp,HttpServletResponse response) throws Exception {
		exportDataService.exportCustomerData(sp, response);
	}
	/*更新风险等级信息*/
	@RequestMapping(value="/updateCustData.xhtml",method= {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public JSONObject updateCustData(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String hidregioncode = request.getParameter("hidregioncode");
		String risklevel = request.getParameter("risklevel");
		String newRisklevel = request.getParameter("hidrisklevel");
		String specriskLevel = request.getParameter("specriskLevel");
		//String evalAnswer = request.getParameter("evalAnswer");
		String hidinvtp = request.getParameter("hidinvtp");
		String evalScope = request.getParameter("evalScope");
		String operatorId = request.getParameter("operatorId");
		String custno = request.getParameter("hidcustno");
		String invnm = request.getParameter("hidinvnm");
		String invtp = request.getParameter("hidinvtp");
		String hidfileno = request.getParameter("hidfileno");
		String newinvprtp = request.getParameter("hidinvprtp");
		String invprtp = request.getParameter("invprtp");
		RiskLevelDto rld = new RiskLevelDto();
		RiskLevelDto returnDto =null;
		rld.setOperatorId(operatorId);
		rld.setCustno(custno);
		rld.setRisklevel(newRisklevel);
		rld.setVoicerecord(hidfileno);
		String errCodeOfV2 = "";	//错误代码
		String errCodeOfVR = "";	//错误代码
		String errMsgOfV2 = "";
		String errMsgOfVR = "";
		String alertMsg = "";
		String iss="";
		String custdocument="";
		OpenAccountDto rDto=new OpenAccountDto();
		SearchParam param = new SearchParam();
		param.getSp().put("custno", custno);
		try{
			if(!invprtp.equals(newinvprtp)){ // 未修改投资者专业程度，不进行修改用户账号信息，不上传TA
				
					List<OpenAccountDto> documentlist = new ArrayList<OpenAccountDto>();
				    OpenAccountDto accountDto =openAccountService.tradeAccoQuery(param);
				    
				    if(accountDto!=null && accountDto.getErrcode().equals("0000")){
							iss+="111";
							String serialNo = Sequences.getPK();//流水号 
							accountDto.setSerialno(serialNo);
							accountDto.setInvprtp(newinvprtp);
							accountDto.setValueOfA("A");
							accountDto.setModifylist("A"); //证件修改类型
							accountDto.setOperatorId(operatorId);
							accountDto.setPermissionId(TransCodeConstant.TRANS_CODE_8006);
							accountDto.setTrustType("0");
							if(accountDto.getDocumentlist()!=null){
								documentlist=accountDto.getDocumentlist();
							}
							OpenAccountDto docDto=null;
							for(int i = 0;i<documentlist.size();i++){
								docDto=(OpenAccountDto)documentlist.get(i);
								custdocument+= docDto.getCustdocument()+"-"+docDto.getExistsflag()+",";
							}
							 accountDto.setCustdocument(custdocument);
							 accountDto.setNullStr("");
							 rDto = custTradeService.modifyCategoryV2(accountDto);
							 errCodeOfV2 =rDto.getErrcode();
							 errMsgOfV2 = rDto.getErrmsg();
					}else{
						iss+="555";
						errCodeOfV2 =accountDto.getErrcode();
						errMsgOfV2 = accountDto.getErrmsg();
					}
				
			}else{
				errCodeOfV2="0000";
			}
			
			if("0000".equals(errCodeOfV2)){
				if("0".equals(newinvprtp)){
					try {
						returnDto=custTradeService.updateVoiceRecord(rld);
						errCodeOfVR = "0000";	//错误代码
						errMsgOfVR = "成功";
					} catch (Exception e) {
						errCodeOfVR = "9999";	//错误代码
						errMsgOfVR = "更新录音文件失败";
						e.printStackTrace();
						logger.error(" -- CustomerDataManagerController -- updateCustData -- 录音文件修改异常,异常信息- " + e);
					}
				}	
				//网上不修改风险等级	 
				if(hidregioncode != null && !"".equals(hidregioncode.trim()) &&  hidregioncode.length()>0){
					
				}else{
					
					if("1".equals(newinvprtp)){
						String evalAnswer=null;
						if(invtp.equals("1")){
							evalAnswer= request.getParameter("evalAnswer").replace('2', 'A').replace('4', 'B').replace('6', 'C').replace('8', 'D');
							evalAnswer = evalAnswer.replaceAll("10", "E");
						}else if(invtp.equals("0")){
							evalAnswer= request.getParameter("evalAnswer").replace('5', 'A').replace('4', 'B').replace('3', 'C').replace('2', 'D').replace('1', 'E');
						}
						rld.setAnswer(evalAnswer);
						rld.setScope(request.getParameter("evalScope")==null?"":request.getParameter("evalScope"));
						rld.setSpecriskLevel(specriskLevel);
						rld.setValueOfDir("DIR");
						rld.setValueOfM("M");
						try {
							returnDto = custTradeService.auditCustRiskLevelV2(rld);
							errCodeOfVR = "0000";	//错误代码
							errMsgOfVR = "成功";
						} catch (Exception e) {
							e.printStackTrace();
							errCodeOfVR = "9999";	//错误代码
							errMsgOfVR = "更新风险等级失败";
							logger.error("CustomerDataManagerController -- updateCustData -- 更新风险等级异常,异常信息-" + e);
						}
					}
				}
				if(returnDto != null){
					if(!"0000".equals(errCodeOfV2) || !"0000".equals(errCodeOfV2)) {
						jsonObject.put("ResultCode", "9999");
						jsonObject.put("ResultMsg","客户类型修改---" + errMsgOfVR + " === " + "录音文件或风险等级修改---" + errCodeOfV2);
					}else {
						jsonObject.put("ResultCode", "0000");
						jsonObject.put("ResultMsg","成功");
					}
					
				}
			}else{
				jsonObject.put("ResultCode", "9999");
				jsonObject.put("ResultMsg",errMsgOfV2);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	@RequestMapping("/queryChangeRecord")
	@ResponseBody
	public Page<CustRisklvelInvprtpDetail> queryChangeRecord(SearchParam sp){
		return custInfoChangeRecordService.queryChangeRecord(sp);
	}
}
