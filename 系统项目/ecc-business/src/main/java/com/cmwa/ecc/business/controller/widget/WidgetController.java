package com.cmwa.ecc.business.controller.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmwa.ecc.business.commonVo.MenuVo;
import com.cmwa.ecc.business.commonVo.UserInfoVo;
import com.cmwa.ecc.business.controller.BaseController;
import com.cmwa.ecc.business.dao.bank.BankBaseDao;
import com.cmwa.ecc.business.entity.bank.BankBnkbaseVo;
import com.cmwa.ecc.business.entity.fundinfo.FundInfoVo;
import com.cmwa.ecc.business.service.bank.BankInfoService;
import com.cmwa.ecc.business.service.fundinfo.FundInfoService;
import com.cmwa.ecc.business.service.menu.MenuService;
import com.cmwa.ecc.business.service.userInfo.UserInfoService;
import com.cmwa.ecc.business.service.widget.WidgetService;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.UserInfoUtil;
/**
 *系统公共的控件、组件 调用的控制器
 *
 */

@Controller
@RequestMapping(value="/service/widget")
public class WidgetController extends BaseController {
	
	@Resource
	private MenuService menuService;
	@Resource
	private UserInfoService userInfoService;
	@Resource 
	private BankInfoService bankInfoService;
	@Resource
	private FundInfoService fundInfoService;
	@Resource
	private WidgetService widgetService;
	@Resource
	private BankBaseDao bankBaseDao;
	
	
	/**
	 * 查询用户信息
	 * 
	 * @param response
	 * @return
	 */
	@SuppressWarnings("static-access")
	@RequestMapping("/queryMatchUserInfoList")
	@ResponseBody
	public List<UserInfoVo> queryMatchUserInfoList(@RequestParam(value = "assistantNM") String assistantNM, @RequestParam(value = "duplicate", defaultValue = "false") String duplicate,
			@RequestParam(value = "queryDimission", defaultValue = "false")boolean queryDimission, HttpServletResponse response) {
		List<UserInfoVo> list = new ArrayList<UserInfoVo>();
		if (StringUtils.isNotBlank(assistantNM)) {
			if (Boolean.parseBoolean(duplicate)) {
				list = UserInfoUtil.getInstance().getDuplicateUserInfosByName(assistantNM, queryDimission);
			} else {
				list = UserInfoUtil.getInstance().getUserInfosByName(assistantNM, queryDimission);
			}
		}
		return list;
	}
	
	
	@RequestMapping("queryMatchMenu.do")
	@ResponseBody
	public List<MenuVo> queryMatchMenu(@RequestParam(value="menuNM",defaultValue = "") String menuNM) {
		List<MenuVo> list = menuService.queryMatchMenuList(menuNM);
		return list;
	}
	
	/**
	 * 匹配查询人员 KM
	 * @param title
	 * @param model
	 * @return
	 */
	@RequestMapping("/queryMatchKMEmployeeInfos")
	@ResponseBody
	public List<UserInfoVo> queryMatchKMEmployeeInfos(@RequestParam(value = "empName", defaultValue = "")String empName ,@RequestParam(value = "limit", required=false, defaultValue="100") Integer limit){
		List<UserInfoVo> employeeList =  userInfoService.searchUserInfoListByParam(empName,limit);
		return employeeList;
	};
	
	@RequestMapping("/queryMatchBankBaseInfoList")
	@ResponseBody
	public List<BankBnkbaseVo> queryMatchBankBaseInfoList(@RequestParam(value="bankName",defaultValue = "") String bankName) {
		SearchParam sp = new SearchParam();
		sp.getSp().put("bankName", bankName);
		List<BankBnkbaseVo> bankBaseList = bankInfoService.getBankBnkbaseList(sp);
		return bankBaseList;
	}
	
	@RequestMapping("/queryMatchFundInfoList")
	@ResponseBody
	public List<FundInfoVo> queryMatchFundInfoList(@RequestParam(value="fundNm",defaultValue = "") String fundNm,@RequestParam(value="limit",required=false,defaultValue="10")String limit) {
		SearchParam sp = new SearchParam();
		sp.getSp().put("fundNm", fundNm);
		sp.getSp().put("limit", limit);
		List<FundInfoVo> fundInfoVos = fundInfoService.queryMatchFundInfoList(sp);
		return fundInfoVos;
	}
	
	@RequestMapping("/queryMatchParamList")
	@ResponseBody
	public List<Map<String, Object>> queryMatchApkindList(ServletRequest req,
			@RequestParam(value="pmst",required= false) String pmst,@RequestParam(value="pmky",required= false) String pmky,
			@RequestParam(value="pmnm",required= false)String pmnm,@RequestParam(value="pmv1",required= false) String pmv1,
			@RequestParam(value="pmv2",required= false)String pmv2,@RequestParam(value="pmv3",required= false) String pmv3,
			@RequestParam(value="pmv4",required= false)String pmv4,@RequestParam(value="pmv5",required= false) String pmv5
			) {
		SearchParam sp = new SearchParam();
		sp.getSp().put("pmst", pmst);
		sp.getSp().put("pmky", pmky);
		sp.getSp().put("pmnm", pmnm);
		sp.getSp().put("pmv1", pmv1);
		sp.getSp().put("pmv2", pmv2);
		sp.getSp().put("pmv3", pmv3);
		sp.getSp().put("pmv4", pmv4);
		sp.getSp().put("pmv5", pmv5);
		List<Map<String, Object>> result = widgetService.queryMatchApkindList(sp);
		return result;
	}
	
	@RequestMapping(value="/bank/getMatchBankBaseInfo.xhtml",method={RequestMethod.POST})
	@ResponseBody
	public List<BankBnkbaseVo> getMatchBankBaseInfo(HttpServletRequest request){
		SearchParam sp = new SearchParam();
		sp.setSp(new HashMap<String,Object>());
		sp.getSp().put("bankName", request.getParameter("bankName"));
		return bankBaseDao.queryBankBaseInfoList(sp);
	}
	
	@RequestMapping(value="/bank/queryDsBankBase.xhtml",method={RequestMethod.POST})
	@ResponseBody
	public List<BankBnkbaseVo> queryDsBankBase(HttpServletRequest request){
		SearchParam sp = new SearchParam();
		sp.setSp(new HashMap<String,Object>());
		sp.getSp().put("bankName", request.getParameter("bankName"));
		return bankBaseDao.queryDsBankBase(sp);
	}
}
