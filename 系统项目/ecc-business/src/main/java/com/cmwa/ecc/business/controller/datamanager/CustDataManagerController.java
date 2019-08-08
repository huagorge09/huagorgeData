package com.cmwa.ecc.business.controller.datamanager;

import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.cmwa.ecc.business.commonVo.Employee;
import com.cmwa.ecc.business.controller.BaseController;
import com.cmwa.ecc.business.entity.accountManger.DocumentDto;
import com.cmwa.ecc.business.entity.accountManger.OpenAccountDto;
import com.cmwa.ecc.business.service.datamanager.CustDataDocUploadService;
import com.cmwa.ecc.business.service.datamanager.CustDataManagerService;
import com.cmwa.ecc.business.utils.DateUtils;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.ResultConstant;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;

/**
 * 资料管理
 * 客户资料管理-前端交互接口
 * @author ex-liuy
 *
 */
@Controller
@RequestMapping(value="/service/custDataManager")
public class CustDataManagerController extends BaseController {
	@Autowired
	private CustDataManagerService custDataManagerService;
	@Autowired
	private CustDataDocUploadService custDataDocUploadService;
	
	@RequestMapping("/custDataManagerIndexView.do")
	public String goCustDataManagerIndexView(ModelMap map){
		Calendar cal = Calendar.getInstance();
		
		Date date = cal.getTime();
		String endDate  = DateUtils.formatDate(date,"yyyy-MM-dd");
		
		cal.add(Calendar.MONTH, -1);
		date = cal.getTime();
		String strDate = DateUtils.formatDate(date,"yyyy-MM-dd");
		
		
		map.put("strDate",strDate);
		map.put("endDate",endDate);
		return "jsp/dataManager/custDataManager/custDataManagerIndex";
	}
	
	@RequestMapping("/updateCustDataManagerView.do")
	public String goUpdateCustDataManagerView(ModelMap map,@RequestParam("appserialno")String appserialno,
			@RequestParam(value="fundacct",required=false)String fundacct, @RequestParam(value="custno",required=false)String custno){
		SearchParam sp = new SearchParam();
		sp.getSp().put("appserialno", appserialno);
		OpenAccountDto dto = custDataManagerService.queryCustDataInfoByCondtion(sp);
		dto.setFundacct(fundacct);
		map.addAttribute("dto", dto);
		return "jsp/dataManager/custDataManager/updateCustDataManager";
	}
	
	@RequestMapping("/uploadCustDataDocView.do")
	public String goUploadCustDataDocView(ModelMap map,@RequestParam("appserialno")String appserialno,
			@RequestParam(value="fundacct",required=false)String fundacct, @RequestParam(value="custno",required=false)String custno){
		map.addAttribute("appserialno", appserialno);
		map.addAttribute("fundacct", fundacct);
		map.addAttribute("custno", custno);
		return "jsp/dataManager/custDataManager/uploadCustDataDoc";
	}
	
	/**
	 * 查询客户资料列表
	 * @param sp
	 * @return
	 */
	@RequestMapping("/queryCustDataInfoListPage.do")
	@ResponseBody
	public Page<OpenAccountDto> queryCustDataInfoListPage(SearchParam sp){
		Page<OpenAccountDto> result = custDataManagerService.queryCustDataInfoListPage(sp);
		return result;
	};
	
	/**
	 * 查询导出-客户资料列表
	 * @param sp
	 * @return
	 */
	@RequestMapping("/queryExportCustDataInfo.do")
	public void queryExportCustDataInfo(HttpServletRequest request,HttpServletResponse response){
		custDataManagerService.queryExportCustDataInfo(request, response);
	};
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateCustDataManagerInfo.do")
	public String updateCustDataManagerInfo(OpenAccountDto openAccountDto, HttpServletRequest request,HttpServletResponse response,ModelMap map){
		Employee emp =  SessionUtils.getEmployee(request);
		//修改人
		openAccountDto.setModifylist(emp.getID());
		JSONObject result = custDataManagerService.updateCustDataManagerInfo(openAccountDto);
		if(!ResultConstant.C_RESULT_SUCCESS.equals(result.get(ResultConstant.C_RESULT_CODE))){
			map.addAttribute("operatorId", emp.getID());
			map.addAttribute("exception", result.get(ResultConstant.C_RESULT_MSG));
			return redirectExecFaild();
		}
		return redirectSuccess();
	}
	
	@RequestMapping("/uploadCustDataDoc.do")
	@ResponseBody
	public JSONObject uploadCustDataDoc(HttpServletRequest request,HttpServletResponse response,@RequestParam("lefile")MultipartFile multipartFile){
		JSONObject result = custDataDocUploadService.insertCustDataDoc(request,response,multipartFile);
		return result;
	}
	
	@RequestMapping("/queryCustDataDocAllList.do")
	@ResponseBody
	public Page<DocumentDto> queryCustDataDocAllList(SearchParam sp){
		return custDataDocUploadService.queryCustDataDocAllList(sp);
	}
	
	@RequestMapping("/custDataDocDel.do")
	@ResponseBody
	public JSONObject custDataDocDel(DocumentDto document){
		return custDataDocUploadService.custDataDocDel(document);
	}
	
	@RequestMapping("/custDataDocDownload.do")
	public void custDataDocDownload(HttpServletRequest request,HttpServletResponse response,DocumentDto document){
		custDataDocUploadService.custDataDocDownload(request, response, document);
	}
}
