package com.cmwa.ecc.business.controller.accountManger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.cmwa.ec.base.util.Sequences;
import com.cmwa.ec.base.util.StringUtil;
import com.cmwa.ecc.business.commonVo.Employee;
import com.cmwa.ecc.business.entity.accountManger.BakCustDto;
import com.cmwa.ecc.business.entity.accountManger.CbpOffLineOTOInvestDto;
import com.cmwa.ecc.business.entity.accountManger.FundBalanceDto;
import com.cmwa.ecc.business.entity.accountManger.OpenAccountDto;
import com.cmwa.ecc.business.entity.accountManger.OpenAccountExcelDto;
import com.cmwa.ecc.business.entity.bank.BankBnkbaseVo;
import com.cmwa.ecc.business.service.accountManger.OpenAccountService;
import com.cmwa.ecc.business.service.bank.BankInfoService;
import com.cmwa.ecc.business.service.widget.WidgetService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.ParameterConstant;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;
import com.cmwa.ecc.business.utils.excel.ReadExcelUtil;

@Controller("openAccountController")
@RequestMapping(value = "/service/accountManager")
public class OpenAccountController {

	@Autowired
	private WidgetService widgetService;
	
	@Autowired
	private BankInfoService bankInfoService;
	
	@Autowired
	private OpenAccountService accountService;
	
	/**
	 * 打开开户页面
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/openAccountView")
	public ModelAndView openAccountView(ModelAndView model) throws IOException {
		//委托方式
		List<Map<String, Object>> trustTypeArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_TRUSTTP);
		model.addObject("trustTypeArray", trustTypeArray);
		//机构类型
		List<Map<String, Object>> organType = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM,"INSTTYPE");
		model.addObject("organType", organType);
		//证件类型
		SearchParam idtpSp = new SearchParam();
		idtpSp.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
		idtpSp.getSp().put("pmky", ParameterConstant.PARAM_PMKY_IDTP);
		idtpSp.getSp().put("pmv1", "1");
		List<Map<String, Object>> idtpArray = widgetService.queryMatchApkindList(idtpSp);
		model.addObject("idtpArray", idtpArray);
		
		//国籍代码
		List<Map<String, Object>> nationArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_NATION);
		model.addObject("nationArray", nationArray);
		
		//风险等级
		List<Map<String, Object>> riskLevelArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_FUNDRISKLEVEL);
		model.addObject("riskLevelArray", riskLevelArray);
		
		// 投资者类别
		List<Map<String, Object>> invtpArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_INVTP);
		model.addObject("invtpArray", invtpArray);
		
		//证件类别或CMF证件类别
		SearchParam seatidtpSp = new SearchParam();
		seatidtpSp.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
		seatidtpSp.getSp().put("pmky", ParameterConstant.PARAM_PMKY_IDTP);
		seatidtpSp.getSp().put("pmv1", "0");
		List<Map<String, Object>> seatidtpArray = widgetService.queryMatchApkindList(seatidtpSp);
		model.addObject("seatidtpArray", seatidtpArray);
		
		//业务类型
		List<Map<String, Object>> businesstpArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_BUSINESSTP);
		model.addObject("businesstpArray", businesstpArray);
		
		//公司
		List<Map<String, Object>> cmptpArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_COMPANYTP);
		model.addObject("cmptpArray", cmptpArray);
		
		//区域代码
		List<Map<String, Object>> regiontpArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_REGIONCODE);
		model.addObject("regiontpArray", regiontpArray);
		
		//基金风险等级
		List<Map<String, Object>> fxqArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_FUNDRISKLEVEL);
		model.addObject("fxqArray", fxqArray);
		
		//基金风险等级
		List<Map<String, Object>> docbusitpArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_DOCBUSINESSTP);
		model.addObject("docbusitpArray", docbusitpArray);
		
		//性别
		List<Map<String, Object>> sexArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_SEX);
		model.addObject("sexArray", sexArray);
		
		//学历代码
		List<Map<String, Object>> edlevelArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_EDLEVEL);
		model.addObject("edlevelArray", edlevelArray);
		
		// 职业代码
		List<Map<String, Object>> vacodeArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_VOCCODE);
		model.addObject("vacodeArray", vacodeArray);
		
		//年收入
		List<Map<String, Object>> incomeArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_ANNUALINCOME);
		model.addObject("incomeArray", incomeArray);
		
		//客户风险承受能力
		List<Map<String, Object>> custrisklevelArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_CUSTRISKLEVEL);
		model.addObject("custrisklevelArray", custrisklevelArray);
		
		//经办人授权范围
		List<Map<String, Object>> contprivArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_BROKERPRIV);
		model.addObject("contprivArray", contprivArray);
		
		//查询客户分组信息
		List<Map<String, Object>> custFirst = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DSCUSTGROUP,ParameterConstant.PARAM_PMKY_FIRSTGROUP);
		model.addObject("custFirst", custFirst);
		
		//银行卡类型 
		SearchParam bankSp = new SearchParam();
		List<BankBnkbaseVo> bankBaseList = bankInfoService.queryDsBankBase(bankSp);
		model.addObject("bankBaseList", bankBaseList);

		//获取省份集合
		List<Map<String, Object>> provinces = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM,ParameterConstant.PARAM_PMKY_DS_PROVINCE);
		model.addObject("provinces", provinces);
		
		//机构类型
		List<Map<String, Object>> investProInstArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM,"INVPROINSTTYPE");
		model.addObject("investProInstArray", investProInstArray);
		
		//近一年末净资产
		List<Map<String, Object>> oneYearEndNetAssetArray = widgetService.queryParamListByStAndKy("OTHERCORPORATE","NEARYEARNETASS");
		model.addObject("oneYearEndNetAssetArray", oneYearEndNetAssetArray);
		//近一年末金融资产
		List<Map<String, Object>> oneYearEndFinAssetArray = widgetService.queryParamListByStAndKy("OTHERCORPORATE","NEARYEARFINASS");
		model.addObject("oneYearEndFinAssetArray", oneYearEndFinAssetArray);
		//投资经历
		List<Map<String, Object>> investExperienceArray = widgetService.queryParamListByStAndKy("OTHERCORPORATE","INVESTEXP");
		model.addObject("investExperienceArray", investExperienceArray);
		//个人专业投资者
		//金融资产
		List<Map<String, Object>> financialAssetArray = widgetService.queryParamListByStAndKy("INDINVESTPRO","FINANCIALASSET");
		model.addObject("financialAssetArray", financialAssetArray);
		//近三年年均收入
		List<Map<String, Object>> threeAnnualIncomeArray = widgetService.queryParamListByStAndKy("INDINVESTPRO","NEARAVGINCOME");
		model.addObject("threeAnnualIncomeArray", threeAnnualIncomeArray);
		//投资经历
		List<Map<String, Object>> indInvExperienceArray = widgetService.queryParamListByStAndKy("INDINVESTPRO","INVESTEXP");
		model.addObject("indInvExperienceArray", indInvExperienceArray);
		//金融职业
		List<Map<String, Object>> finProfessionsArray = widgetService.queryParamListByStAndKy("INDINVESTPRO","FINPROFESSIONS");
		model.addObject("finProfessionsArray", finProfessionsArray);
		//相关工作经历
		List<Map<String, Object>> relatedWorkExpArray = widgetService.queryParamListByStAndKy("INDINVESTPRO","RELATEDWORKEXP");
		model.addObject("relatedWorkExpArray", relatedWorkExpArray);
		//税收居民身份
		List<Map<String, Object>> taxTypeArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM,"INVESTTAXTYPE");
		model.addObject("taxTypeArray", taxTypeArray);
		
		Employee employee = SessionUtils.getEmployee();
		model.addObject("permissionId", "8001");
		model.addObject("operatorId", null != employee ? employee.getID() : "");
		model.setViewName("jsp/accountManger/custAccountMgr/openAccount");
		return model;
	}
	
	/**
	 * 查询最大的文件编号
	 * @return
	 */
	@RequestMapping("/getMaxFileNo")
	@ResponseBody
	public JSONObject getMaxFileNo(){
		JSONObject jsonObject = new JSONObject();
		String fileNo = "";
		String maxKeepAddress = "";
		String returnCode = "0000";
		String returnMsg = "成功";
		
		String graphemeArray[]={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		
		try {
			fileNo = accountService.getMaxFileNo();
		} catch (Exception e) {
			e.printStackTrace();
			returnCode="9999";
			returnMsg="查询最大的文件编号异常";
		}
		
		try {
			maxKeepAddress = accountService.getMaxKeepAddress();
		} catch (Exception e) {
			e.printStackTrace();
			returnCode="9999";
			returnMsg="查询最大的存档位置异常";
		}
		
		//获取每个存档位置存放多少文件
		List<Map<String, Object>> maxKeepAddressArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, "KEEPADDRESIZE");
		int keepAddressNum=0;
		try {
			keepAddressNum=Integer.parseInt((String) maxKeepAddressArray.get(0).get("PMCO"));
		} catch (Exception e) {
			e.printStackTrace();
			returnCode="9999";
			returnMsg="获取每个存档位置存放多少文件转换错误";
		}
		
		if("".equals(maxKeepAddress)){
			//初始化存档位置
			List<Map<String, Object>> keepAddressArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, "KEEPADDRESS");
			if(keepAddressArray!=null && keepAddressArray.size() > 0){
				maxKeepAddress=(String) keepAddressArray.get(0).get("PMCO");
			}
		}else{
			
			try {
				String [] splitArr=maxKeepAddress.split(",");
				String keepAddressStr=splitArr[0];
				//最大存档 总条数
				int keepNum=Integer.parseInt(splitArr[1]);
				//截取存档位置前字符
				String kaddress=keepAddressStr.substring(0, 1);
				//截取存档数字 A16 截取16
				int kanum=Integer.parseInt(keepAddressStr.substring(1, keepAddressStr.length()));
		
		        String nextkaddress="";
		        System.out.print("keepNum:"+keepNum+",kaddress:"+kaddress+",kanum:"+kanum);
		        int k=0;
				//如果当前条数+1大于数据库设置参数中总条数 
				if(keepNum+1>keepAddressNum){
					for(int i=0;i<graphemeArray.length;i++){
						if(kaddress.equals(graphemeArray[i])){
							k=i;
							break;
						}
					}
					if(k+1<26){
						nextkaddress=graphemeArray[k+1];
						maxKeepAddress=nextkaddress+kanum;
					}else{
						int num=kanum+1;
						maxKeepAddress="A"+num;
					}
					
				}else{
					//否则继续用改存档位置
					maxKeepAddress=keepAddressStr;
				}
			} catch (Exception e) {
				e.printStackTrace();
				returnCode="9999";
				returnMsg="初始化存档位置异常";
			}
		}
		
		jsonObject.put("fileNo", fileNo);
		jsonObject.put("keepAddress", maxKeepAddress);
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		return jsonObject;
	}
	
	/**
	 * 根据文件编号查询文件数量
	 * @return
	 */
	@RequestMapping("/getFileNo")
	@ResponseBody
	public JSONObject getFileNo(@RequestParam("fileNo")String fileNo){
		JSONObject object = new JSONObject();
		int fileCount = 0;
		try {
			fileCount = accountService.getFileNo(fileNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		object.put("fileCount", fileCount);
		return object;
	}
	
	/**
	 * 备案客户信息查询
	 * @return
	 */
	@RequestMapping(value = "/queryBakCust",method={RequestMethod.GET,RequestMethod.POST} )
	@ResponseBody
	public JSONArray queryBakCust(@RequestParam("custno")String custno , @RequestParam("role")String role){
		JSONArray jsonArray = new JSONArray();
		SearchParam param = new SearchParam();
		try {
			param.getSp().put("custno", custno);
			param.getSp().put("role", role);
			List<BakCustDto> list = accountService.queryBakCust(param);
			jsonArray = JSONArray.fromObject(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonArray;
	}
	
	
	/**
	 * 开户信息查询
	 * @param fundAcc
	 * @param tradeAcc
	 * @return
	 */
	@RequestMapping(value = "/queryAccoByfundAcc",method={RequestMethod.GET,RequestMethod.POST} )
	@ResponseBody
	public OpenAccountDto queryAccoByfundAcc(@RequestParam("fundAcc")String fundAcc , @RequestParam("tradeAcc")String tradeAcc){
		SearchParam param = new SearchParam();
		OpenAccountDto openAccountDto = new OpenAccountDto();
		try {
			param.getSp().put("fundAcc", fundAcc);
			param.getSp().put("tradeAcc", tradeAcc);
			openAccountDto = accountService.queryAccoByfundAcc(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return openAccountDto;
	}
	
	
	/**
	 * 查询综合业务平台 银行信息
	 * @param accName
	 * @return
	 */
	@RequestMapping(value = "/queryWaspUserBankInfo",method={RequestMethod.GET,RequestMethod.POST} )
	@ResponseBody
	public List<OpenAccountDto> queryWaspUserBankInfo(SearchParam param){
		List<OpenAccountDto> openAccountDto = new ArrayList<OpenAccountDto>();
		try {
			openAccountDto = accountService.queryWaspUserBankInfo((String)param.getSp().get("accName"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return openAccountDto;
	}
	
	
	/**
	 * 查询综合业务平台 银行信息
	 * @param accName
	 * @return
	 */
	@RequestMapping(value = "/queryOpenUserInfo",method={RequestMethod.GET,RequestMethod.POST} )
	@ResponseBody
	public JSONObject queryOpenUserInfo(SearchParam param){
		List<OpenAccountDto> list = new ArrayList<OpenAccountDto>();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("returnMsg", "0");
		try {
			list = accountService.queryOpenUserInfo(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != list && list.size() > 0) {
			jsonObject.put("returnMsg", "1");
		}
		return jsonObject;
	}

	/**
	 * 查询线下一对一 客户资料信息
	 * @param accName
	 * @return
	 */
	@RequestMapping(value = "/queryCbpOffLineOTOInvInfo",method={RequestMethod.GET,RequestMethod.POST} )
	@ResponseBody
	public List<CbpOffLineOTOInvestDto> queryCbpOffLineOTOInvInfo(SearchParam param){
		List<CbpOffLineOTOInvestDto> list = new ArrayList<CbpOffLineOTOInvestDto>();
		try {
			list = accountService.queryCbpOffLineOTOInvInfo(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 个人账户/机构开户
	 * @param baseInfo
	 * @return
	 */
	@RequestMapping(value = "/openCustom",method={RequestMethod.GET,RequestMethod.POST} )
	@ResponseBody
	public OpenAccountDto openCustom(@RequestParam("baseInfo")String baseInfo){
		OpenAccountDto dto = new OpenAccountDto();
		baseInfo = StringEscapeUtils.unescapeHtml4(baseInfo);
		try {
				dto = accountService.convertBean(baseInfo);
				if (!dto.getErrcode().equals("0000")) {
					return dto;
				}
				dto = accountService.openCustom(dto);
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrcode("9999");
			dto.setErrmsg(e.getMessage());
		}
		return dto;
	}
	

	/**
	 * 基金余额查询页面
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/openFundBalanceView",method={RequestMethod.GET,RequestMethod.POST} )
	public ModelAndView openFundBalanceView(HttpServletRequest request){
		ModelAndView model = new ModelAndView();
		String fundacct = request.getParameter("fundacct");
		model.addObject("fundacct",fundacct);
		model.setViewName("jsp/accountManger/fundBalanceMgr/fundBalanceList");
		return model;
	}

	/**
	 * 基金余额查询
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/queryFundBalance",method={RequestMethod.GET,RequestMethod.POST} )
	@ResponseBody
	public Page<FundBalanceDto> queryFundBalance(SearchParam param){
		Page<FundBalanceDto> list = new Page<FundBalanceDto>();
		try {
			param.getSp().put("invnm", "");
			param.getSp().put("regioncode", "");
			param.getSp().put("tano", "");
			list = accountService.queryFundBalance(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 反洗钱校验
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/antiMoneyLaunValid",method={RequestMethod.GET,RequestMethod.POST} )
	@ResponseBody
	public Map<String, String> antiMoneyLaunValid(SearchParam param){
		Map<String, String> result = new HashMap<String, String>();
		result = accountService.antiMoneyLaunValid(param);
		return result;
	}
	
	/**
	 * 批量开户
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/batchOpenAccount",method={RequestMethod.GET,RequestMethod.POST} )
	@ResponseBody
	public JSONObject batchOpenAccount(HttpServletRequest request) throws IOException{
		List<String> resultList = new ArrayList<String>();
		StringBuffer stringBuffer = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
        JSONObject jsonObject = new JSONObject();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				MultipartFile multipartFile = multiRequest.getFile(iter.next().toString());
				InputStream is = multipartFile.getInputStream();
		        try {
		            XSSFWorkbook workbook = (XSSFWorkbook) WorkbookFactory.create(is);
		            XSSFSheet sheet = workbook.getSheetAt(0);
		            if(sheet.getLastRowNum() < 1){
		            	jsonObject.put("resultCode", "9999");
		            	jsonObject.put("resultMsg", "模板数据为空，请检查后重新上传！");
		                return jsonObject;
		            }
		            XSSFRow headRow = sheet.getRow(0);
		            String rowNum = "";
		            for (int i = 1; i < sheet.getLastRowNum() + 1 ; i++){
		            	stringBuffer = new StringBuffer();
		                XSSFRow row = sheet.getRow(i);
		                rowNum = ("第"+(i+1)+"行：");
		                Object t = OpenAccountExcelDto.class.newInstance();
		                String invprtp = "";
		                for(int j = 0; j < headRow.getLastCellNum(); j++){
		                    XSSFCell cellHeadFiled = headRow.getCell(j);
		                    String cellFiledName = null;
		                    if(cellHeadFiled != null){
		                        cellFiledName = cellHeadFiled.getRichStringCellValue().getString();
		                    }
		                    XSSFCell cell = row.getCell(j);
		                    String cellFiledValue = ReadExcelUtil.getCellValue(cell);//String.valueOf(cellFiled.getRichStringCellValue());
		                    if(cellFiledName.equals("机构类型(专业)") && invprtp.equals("1")){
		                    	continue;
		                    }
		                    if (StringUtil.isEmpty(cellFiledValue)) {
	                    		stringBuffer.append("第"+(j+1)+"列"+cellFiledName+"不能为空；");
	                    		continue;
							}
		                    
		                    if (cellFiledName.equals("注册登记证件类型")) {
		                    	//证件类型
		                		SearchParam idtpSp = new SearchParam();
		                		idtpSp.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
		                		idtpSp.getSp().put("pmky", ParameterConstant.PARAM_PMKY_IDTP);
		                		idtpSp.getSp().put("pmnm", cellFiledValue);
		                		List<Map<String, Object>> idtpArray = widgetService.queryMatchApkindList(idtpSp);
		                		if (null != idtpArray && idtpArray.size() > 0) {
		                			Map<String, Object> idMap = idtpArray.get(0);
		                			ReadExcelUtil.setFiledValue(j, t, "证件类型ID", idMap.get("PMCO").toString());
		                			ReadExcelUtil.setFiledValue(j, t, cellFiledName, cellFiledValue);
								}else{
									stringBuffer.append("第"+(j+1)+"列"+cellFiledName+"不存在；");
		                    		continue;
								}
		                    }else if (cellFiledName.equals("开户银行")) {
		                		//银行卡类型 
		                		SearchParam bankSp = new SearchParam();
		                		bankSp.getSp().put("bankName", cellFiledValue);
		                		List<BankBnkbaseVo> bankBaseList = bankInfoService.queryDsBankBase(bankSp);
		                		if (null != bankBaseList && bankBaseList.size() > 0) {
		                			BankBnkbaseVo bnkbaseVo = bankBaseList.get(0);
		                			ReadExcelUtil.setFiledValue(j, t, "开户银行ID", bnkbaseVo.getBnkNo());
		                			ReadExcelUtil.setFiledValue(j, t, cellFiledName, cellFiledValue);
								}else{
									stringBuffer.append("第"+(j+1)+"列"+cellFiledName+"不存在；");
		                    		continue;
								}
		                    }else if (cellFiledName.equals("注册登记证件有效期") || cellFiledName.equals("法定代表人证件有效期") || cellFiledName.equals("经办人证件有效期")) {
		                    	if("长期".equals(cellFiledValue)){
		                    		cellFiledValue = "20991231";
		                    	}else{
		                    		cellFiledValue = cellFiledValue.replace(".", "").replace("/", "").replace("-", "");
		                    	}
		                    	ReadExcelUtil.setFiledValue(j, t, cellFiledName, cellFiledValue);
		                    }else if (cellFiledName.equals("预留银行开户地")) {
		                    	SearchParam openAddr = new SearchParam();
		                    	openAddr.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
		                    	openAddr.getSp().put("pmky", ParameterConstant.PARAM_PMKY_DS_PROVINCE);
		                    	openAddr.getSp().put("pmnm", cellFiledValue);
		                		List<Map<String, Object>> openAddrArray = widgetService.queryMatchApkindList(openAddr);
		                		if (null != openAddrArray && openAddrArray.size() > 0) {
		                			Map<String, Object> idMap = openAddrArray.get(0);
		                			ReadExcelUtil.setFiledValue(j, t, "预留银行开户地ID", idMap.get("PMCO").toString());
		                			ReadExcelUtil.setFiledValue(j, t, cellFiledName, cellFiledValue);
		                			
		                			//开户市
		                			SearchParam openAddrcity = new SearchParam();
		                			openAddrcity.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
		                			openAddrcity.getSp().put("pmky", ParameterConstant.PARAM_PMKY_DS_CITYCODE);
		                			openAddrcity.getSp().put("pmv1", idMap.get("PMCO").toString());
		                			List<Map<String, Object>> openCityArray = widgetService.queryMatchApkindList(openAddrcity);
		                			if (null != openCityArray && openCityArray.size() > 0) {
		                				Map<String, Object> cityMap = openAddrArray.get(0);
		                				ReadExcelUtil.setFiledValue(j, t, "银行市所在地", cityMap.get("PMCO").toString());
		                			}
								}else{
									stringBuffer.append("第"+(j+1)+"列"+cellFiledName+"不存在；");
		                    		continue;
								}
		                    }else if (cellFiledName.equals("机构类型")) {
		                    	SearchParam instTypePram = new SearchParam();
		                    	instTypePram.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
		                    	instTypePram.getSp().put("pmky", "INSTTYPE");
		                    	instTypePram.getSp().put("pmnm", cellFiledValue);
		                		List<Map<String, Object>> instTypeArray = widgetService.queryMatchApkindList(instTypePram);
		                		if (null != instTypeArray && instTypeArray.size() > 0) {
		                			Map<String, Object> instTypeMap = instTypeArray.get(0);
		                			ReadExcelUtil.setFiledValue(j, t, "机构类型ID", instTypeMap.get("PMCO").toString());
		                			ReadExcelUtil.setFiledValue(j, t, cellFiledName, cellFiledValue);
		                		}else{
									stringBuffer.append("第"+(j+1)+"列"+cellFiledName+"不存在；");
		                    		continue;
								}
		                    }else if (cellFiledName.equals("投资者类型")) {
		                    	if(cellFiledValue.equals("专业投资者")){
		                    		cellFiledValue = "0";
		                    		invprtp = "0";
		                    	}else if(cellFiledValue.equals("普通投资者")) {
		                    		cellFiledValue = "1";
		                    		invprtp = "1";
		                    	}else{
		                    		stringBuffer.append("第"+(j+1)+"列"+cellFiledName+"不存在；");
		                    		continue;
		                    	}
	                			ReadExcelUtil.setFiledValue(j, t, cellFiledName, cellFiledValue);
		                    }else if (cellFiledName.equals("业务类型")) {
		                    	SearchParam busTypePram = new SearchParam();
		                    	busTypePram.getSp().put("pmst", ParameterConstant.PARAM_PMST_DS);
		                    	busTypePram.getSp().put("pmky", ParameterConstant.PARAM_PMKY_BUSINESSTP);
		                    	busTypePram.getSp().put("pmnm", cellFiledValue);
		                		List<Map<String, Object>> busTypeArray = widgetService.queryMatchApkindList(busTypePram);
		                		if (null != busTypeArray && busTypeArray.size() > 0) {
		                			Map<String, Object> instTypeMap = busTypeArray.get(0);
		                			ReadExcelUtil.setFiledValue(j, t, "业务类型ID", instTypeMap.get("PMCO").toString());
		                			ReadExcelUtil.setFiledValue(j, t, cellFiledName, cellFiledValue);
		                		}else{
									stringBuffer.append("第"+(j+1)+"列"+cellFiledName+"不存在；");
		                    		continue;
								}
		                    }else if (cellFiledName.equals("机构类型(专业)")) {
		                    	//专业投资者才需要选择机构类型
		                    	if(invprtp.equals("0")){
		                    		SearchParam busTypePram = new SearchParam();
			                    	busTypePram.getSp().put("pmst", "INVPROINSTTYPE");
			                    	busTypePram.getSp().put("pmky", "FINPRODUCT");
			                    	busTypePram.getSp().put("pmnm", cellFiledValue);
			                		List<Map<String, Object>> busTypeArray = widgetService.queryMatchApkindList(busTypePram);
			                		if (null != busTypeArray && busTypeArray.size() > 0) {
			                			Map<String, Object> instTypeMap = busTypeArray.get(0);
			                			ReadExcelUtil.setFiledValue(j, t, "机构类型(专业)ID", instTypeMap.get("PMCO").toString());
			                			ReadExcelUtil.setFiledValue(j, t, cellFiledName, cellFiledValue);
			                		}else{
										stringBuffer.append("第"+(j+1)+"列"+cellFiledName+"不存在；");
			                    		continue;
									}
		                    	}
		                    }else{
		                    	ReadExcelUtil.setFiledValue(j, t, cellFiledName, cellFiledValue);
		                    }
		                }
		                if(!StringUtil.isEmpty(stringBuffer.toString())) {
		                	resultList.add(rowNum+stringBuffer.toString());
		                }
		                list.add(t);
		            }
		            jsonObject.put("resultCode", "0000");
		        	jsonObject.put("resultMsg", "模板导入成功！");
		        	jsonObject.put("data", list);
		        	jsonObject.put("resultList", resultList);
		        } catch (Exception e) {
		            e.printStackTrace();
		            jsonObject.put("resultCode", "9999");
		        	jsonObject.put("resultMsg", "模板解析异常："+e.getMessage());
		        	jsonObject.put("resultList", resultList);
		            return jsonObject;
		        }
			}
		}
		return jsonObject;
	}
	
	/**
	 * 批量开户
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/batchOpenAccountSave",method={RequestMethod.GET,RequestMethod.POST} )
	@ResponseBody
	public JSONObject batchOpenAccountSave(@RequestParam("accountData") String accountData, HttpServletRequest request) throws IOException {
		JSONObject jsonObject = new JSONObject();
		List<String> errorList = new ArrayList<String>();
		try {
			accountData = StringEscapeUtils.unescapeHtml4(accountData);
			JSONArray jsonArray = JSONArray.fromObject(accountData);
			@SuppressWarnings({ "unchecked", "deprecation" })
			List<OpenAccountExcelDto> openAccountList = JSONArray.toList(jsonArray,OpenAccountExcelDto.class);
			for (int i = 0; i < openAccountList.size(); i++) {
				OpenAccountExcelDto entity = openAccountList.get(i);
				Employee userVo = SessionUtils.getEmployee();
				if(null != userVo){
					entity.setOpid(userVo.getID());
				}else{
					entity.setOpid("SYSTEM");
				}
				entity.setSerialno(Sequences.getPK());
				accountService.batchOpenAccountSave(entity);
				if (!entity.getErrCode().equals("0000")) {
					errorList.add("第"+(i+1)+"行"+entity.getInvnm()+"开户失败："+entity.getErrMsg());
				}
			}
			if(errorList.size() > 0){
				jsonObject.put("resultCode", "0001");
				jsonObject.put("resultMsg", "开户失败");
				jsonObject.put("errorList", errorList);
			}else{
				jsonObject.put("resultCode", "0000");
				jsonObject.put("resultMsg", "批量开户成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resultCode", "9999");
			jsonObject.put("resultMsg", "批量开户异常："+e.getMessage());
		}
		return jsonObject;
	}
	
	/**
	 * 批量开户模板下载
	 */
	@RequestMapping(value = "downLoadTemp.xhtml", method = { RequestMethod.GET, RequestMethod.POST })
	public String downLoadTemp(HttpServletResponse response,HttpServletRequest request) throws IOException { 
	    String projectPath = request.getSession().getServletContext().getRealPath("/");
	    try {    
            String path = projectPath + "template/openAccount.xlsx";    
            File file = new File(path); 
            if (file.exists()) { 
            	String name = "批量开户模板";
                InputStream ins = new FileInputStream(path);    
                BufferedInputStream bins = new BufferedInputStream(ins);// 放到缓冲流里面    
                OutputStream outs = response.getOutputStream();// 获取文件输出IO流    
                BufferedOutputStream bouts = new BufferedOutputStream(outs); 
                response.setContentType("application/vnd.ms-excel;charset=UTF-8");
                name= new String(name.getBytes("GBK"), "ISO8859-1");
                response.setHeader("Content-disposition","Attachment;filename="+name+".xlsx");// 设置头部信息   
                int bytesRead = 0;    
                byte[] buffer = new byte[8192];    
                 //开始向网络传输文件流    
                while ((bytesRead = bins.read(buffer, 0, 8192)) != -1) {    
                   bouts.write(buffer, 0, bytesRead);    
               }    
                bouts.flush();// 这里一定要调用flush()方法    
                ins.close();    
                bins.close();    
                outs.close();    
                bouts.close(); 
            } else {    
                response.sendRedirect("../error.jsp");    
            }    
        } catch (IOException e) {    
            e.printStackTrace();  
        }  
       return null;
    }
}
