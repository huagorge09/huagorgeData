package com.cmwa.ecc.business.controller.account;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cmwa.ecc.business.entity.fundinfo.FundInfoVo;
import com.cmwa.ecc.business.entity.paramvo.ParameterVo;
import com.cmwa.ecc.business.service.common.CommonService;
import com.cmwa.ecc.business.service.fundinfo.FundInfoService;
import com.cmwa.ecc.business.utils.ParameterConstant;

/**
 * 交易管理页面跳转
 * 前端入口
 * @author ex-liuy
 *
 */
@Controller
@RequestMapping(value="service/tradeManager")
public class TradeManagerPageController {
	@Autowired
	private CommonService commonService;
	@Autowired
	private FundInfoService fundInfoService;
	/**
	 * 认购页面
	 * @return
	 */
	@RequestMapping("/subscribeView.do")
	public String goSubscribeView(){
		return "jsp/tradeManager/subscribe";
	}
	
	/**
	 * 申购页面
	 * @return
	 */
	@RequestMapping("/purchaseView.do")
	public String goPurchaseView(){
		return "jsp/tradeManager/purchase";
	}
	
	/**
	 * 赎回页面
	 * @return
	 */
	@RequestMapping("/redeemView.do")
	public String goRedeemView(){
		return "jsp/tradeManager/redeem";
	}
	
	/**
	 * 批量设置页面
	 * @return
	 */
	@RequestMapping("/batchhandleView.do")
	public String goBatchhandleView(){
		return "jsp/tradeManager/batchhandle";
	}
	
	/**
	 * 转换页面
	 * @return
	 */
	@RequestMapping("/convertView.do")
	public String goConvertView(){
		return "jsp/tradeManager/convert";
	}
	
	/**
	 * 修改分红方式页面
	 * @return
	 */
	@RequestMapping("/melonView.do")
	public String goMelonView(){
		return "jsp/tradeManager/melon";
	}
	
	/**
	 * 修改分红方式页面
	 * @return
	 */
	@RequestMapping("/melonmdSetView.do")
	public ModelAndView goMelonmdSetView(@RequestParam("tradeAccos")String tradeAccos,ModelAndView model){
		//委托方式
		List<ParameterVo> trustTypeArray = commonService.getParameterListPage(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_ACCPTMD);
		model.addObject("trustTypeArray", trustTypeArray);
		
		//分红方式
		List<ParameterVo>  melonmdArray = commonService.getParameterListPage(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_MELONMD);
		model.addObject("melonmdArray", melonmdArray);
		
		//获取所有基金
		List<FundInfoVo> fundNmArray = fundInfoService.getAllFundInfo();
		
		List<FundInfoVo> fundNmArray1 = new ArrayList<FundInfoVo>();
		List<FundInfoVo> fundNmArray2 = new ArrayList<FundInfoVo>();
		
		if (fundNmArray != null && melonmdArray.size() > 0) {
			int count = fundNmArray.size();
   			int firstdiv = count/2 + count%2;
   			for (int i = 0; i < firstdiv; i++) {
   				FundInfoVo dto = fundNmArray.get(i);
   				fundNmArray1.add(dto);
			}
   			for (int j = firstdiv; j < count; j++) {
   				FundInfoVo dto2 = fundNmArray.get(j);
   				fundNmArray2.add(dto2);
			}
		}
		
		model.addObject("fundNmArray1", fundNmArray1);
		model.addObject("fundNmArray2", fundNmArray2);
		model.addObject("tradeAccos", tradeAccos);
		
		model.setViewName("jsp/tradeManager/melonmdSet");
		return model;
	}
	
	/**
	 * 单步转托管页面
	 * @return
	 */
	@RequestMapping("/simpManagedSwitchView.do")
	public String goSimpManagedSwitchView(){
		return "jsp/tradeManager/simpManagedSwitch";
	}
	
	/**
	 * 转托管转出
	 * @return
	 */
	@RequestMapping("/managedSwitchOutView.do")
	public String goManagedSwitchOutView(){
		return "jsp/tradeManager/managedSwitchOut";
	}
	
	/**
	 * 转托管转入
	 * @return
	 */
	@RequestMapping("/managedSwitchInView.do")
	public String goManagedSwitchInView(){
		return "jsp/tradeManager/managedSwitchIn";
	}
	
	/**
	 * 内部转托管页面
	 * @return
	 */
	@RequestMapping("/managedSwitchSelfView.do")
	public String goManagedSwitchSelfView(){
		return "jsp/tradeManager/managedSwitchSelf";
	}
	
	/**
	 * 交易复核页面
	 * @return
	 */
	@RequestMapping("/tradeQryView.do")
	public String goTradeQryView(){
		return "jsp/tradeManager/tradeQry";
	}
	
	/**
	 * 交易批量复核页面
	 * @return
	 */
	@RequestMapping("/batchhandleCheckListView.do")
	public String goBatchhandleCheckListView(){
		return "jsp/tradeManager/batchhandleCheckList";
	}
	
	/**
	 * 交易驳回修改页面
	 * @return
	 */
	@RequestMapping("/tradeModifyQryView.do")
	public String goTradeModifyQryView(){
		return "jsp/tradeManager/tradeModifyQry";
	}
	
	/**
	 * 交易撤单页面
	 * @return
	 */
	@RequestMapping("/cancelView.do")
	public String goCancelView(){
		return "jsp/tradeManager/cancel";
	}
}
