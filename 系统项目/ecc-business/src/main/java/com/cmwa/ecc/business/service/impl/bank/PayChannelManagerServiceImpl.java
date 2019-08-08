package com.cmwa.ecc.business.service.impl.bank;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.cmwa.ecc.business.commonVo.Employee;
import com.cmwa.ecc.business.controller.bankManager.PayChannelManagerController;
import com.cmwa.ecc.business.dao.bank.BankBaseDao;
import com.cmwa.ecc.business.dao.bank.PayChannelDao;
import com.cmwa.ecc.business.dao.user.UserDao;
import com.cmwa.ecc.business.entity.bank.BankBnkbaseVo;
import com.cmwa.ecc.business.entity.bank.BankPayChannelVo;
import com.cmwa.ecc.business.entity.user.User;
import com.cmwa.ecc.business.service.bank.PayChannelManagerService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.ResCodeConstant;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;

@Service
public class PayChannelManagerServiceImpl implements PayChannelManagerService {
	
	
	private static Logger logger = Logger.getLogger(PayChannelManagerController.class);	

	@Autowired
	private PayChannelDao payChannelDao;

	@Autowired
	private BankBaseDao bankBaseDao;
	
	@Autowired
	private UserDao useroDao;
	
	
	@Override
	public Page<BankPayChannelVo> queryPayChannelListPage(SearchParam sp){
		List<BankPayChannelVo> queryPayChannelList = payChannelDao.queryPayChannelListPage(sp);
		sp.setStart((sp.getPageNo() - Page.FIRST_PAGE) * sp.getLimit());
		sp.setEnd(sp.getStart() + sp.getLimit());
		sp.setTotal(payChannelDao.queryPayChannelListTotal(sp));
		return Page.create(queryPayChannelList, sp.getStart(), sp.getLimit(),
				sp.getTotal());
	}



	@Override
	public ModelAndView openDialog(HttpServletRequest request) {
		String method = request.getParameter("method");
		ModelAndView view = new ModelAndView();
		if (null == method || "".equals(method)) {
			view.setViewName("jsp/404");
		}
		if (null != method && "add".equals(method)) {
			view.setViewName("jsp/bankMgr/payChannelAdd");
		}

		if (null != method && "update".equals(method)) {
				String bankNO=request.getParameter("bankNO");
				String thirdChannel=request.getParameter("thirdChannel");
				if (bankNO == null || "".equals(bankNO)) {
					view.setViewName("jsp/hint/error");
					view.addObject("ResultCode", ResCodeConstant.SystemConstant.CODE7402);
					view.addObject("ResultDesc", "支持银行卡号不能为空");
					return view;
				}
				if (thirdChannel == null || "".equals(thirdChannel)) {
					view.setViewName("jsp/hint/error");
					view.addObject("ResultCode", ResCodeConstant.SystemConstant.CODE7401);
					view.addObject("ResultDesc", "第三方支付渠道代码不能为空");
					return view;
				}
				SearchParam sp = new SearchParam();
				sp.setSp(new HashMap<String, Object>());
				sp.getSp().put("bnkNo", bankNO);
				sp.getSp().put("thirdChannel", thirdChannel);
				BankPayChannelVo bankPayChannelVo = payChannelDao.queryPayChannelListPage(sp).get(0);
				sp.getSp().put("bnkNo",thirdChannel);
				BankBnkbaseVo bankBnkbaseVo = bankBaseDao.queryBankBaseInfoListPage(sp).get(0);
				bankPayChannelVo.setThirdChannelName(bankBnkbaseVo.getBnkNm());
				SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
				bankPayChannelVo.setModifyDate(sFormat.format(new Date()));
				sFormat = new SimpleDateFormat("HH:mm:ss");
				bankPayChannelVo.setModifyTime(sFormat.format(new Date()));
				//获取创建用户的用户信息
				User user = new User(bankPayChannelVo.getCreateOpid());
				useroDao.getUserById(user);
				bankPayChannelVo.setCreateOpName(user.getUsernm());
				String dateTemp = bankPayChannelVo.getCreateDate();
				if(null != dateTemp && dateTemp.length() == 8){
					bankPayChannelVo.setCreateDate(dateTemp.substring(0,4) + "-" + dateTemp.substring(4,6) + "-" + dateTemp.substring(6,8));
				}

				dateTemp = bankPayChannelVo.getCreateTime();
				if(null != dateTemp && dateTemp.length() == 6){
					bankPayChannelVo.setCreateTime(dateTemp.substring(0,2) + ":" + dateTemp.substring(2,4) + ":" + dateTemp.substring(4,6));
				}
				//获取修改用户的用户信息
				Employee emp = SessionUtils.getEmployee();
				bankPayChannelVo.setModifyOpid(emp.getID());
				bankPayChannelVo.setModifyOpName(emp.getName());
				
				if (bankPayChannelVo.getStatus().equals("Y")) 
					bankPayChannelVo.setStatusName("有效");
				else if(bankPayChannelVo.getStatus().equals("N"))
					bankPayChannelVo.setStatusName("无效");
				
				if ("0".equals(bankPayChannelVo.getPayMode())) 
					bankPayChannelVo.setPayModeName("B2B委托代扣");
				else if ("1".equals(bankPayChannelVo.getPayMode()))
					bankPayChannelVo.setPayModeName("B2C网银支付");
				
				if ("Y".equals(bankPayChannelVo.getMipFlag())) 
					bankPayChannelVo.setMipFlagName("是");
				else if ("N".equals(bankPayChannelVo.getMipFlag()))
					bankPayChannelVo.setMipFlagName("否");
				
				if ("Y".equals(bankPayChannelVo.getRecommend())) 
					bankPayChannelVo.setMipFlagName("是");
				else if ("N".equals(bankPayChannelVo.getRecommend()))
					bankPayChannelVo.setRecommend("否");
				view.setViewName("jsp/bankMgr/payChannelEdit");
				view.addObject("payChannelVo", bankPayChannelVo);
		}
		return view;
	}



	@Override
	public JSONObject addPayChannel(BankPayChannelVo payChannelVo) {
		JSONObject result = new JSONObject();
		String thirdChannel = payChannelVo.getThirdChannel();
		String bankNO = payChannelVo.getBankNO();
		if (bankNO == null || "".equals(bankNO)) {
			result.put("ResultCode", ResCodeConstant.SystemConstant.CODE7402);
			result.put("ResultDesc", "支持银行卡号不能为空");
			return result;
		}
		if (thirdChannel == null || "".equals(thirdChannel)) {
			result.put("ResultCode", ResCodeConstant.SystemConstant.CODE7401);
			result.put("ResultDesc", "第三方支付渠道代码不能为空");
			return result;
		}
		if(existsThridChannelWithBankNO(result, thirdChannel, bankNO,payChannelVo.getOldBankNo())){
			return result;
		}
		payChannelDao.insertPayChannel(payChannelVo);
		result.put("ResultCode", ResCodeConstant.SUCCESS_CODE);
		result.put("ResultDesc", "新增成功");
		return  result;
	}



	@Override
	public JSONObject delPayChannel(HttpServletRequest request) {
		
		String bnkNo = request.getParameter("bankNO");
		String thirdChannel = request.getParameter("thirdChannel");
		JSONObject result = new JSONObject();
		if (bnkNo == null || "".equals(bnkNo)) {
			result.put("ResultCode", ResCodeConstant.SystemConstant.CODE7402);
			result.put("ResultDesc", "支持银行卡号不能为空");
			return result;
		}
		if (thirdChannel == null || "".equals(thirdChannel)) {
			result.put("ResultCode", ResCodeConstant.SystemConstant.CODE7401);
			result.put("ResultDesc", "第三方支付渠道代码不能为空");
			return result;
		}
		payChannelDao.delPayChannel(thirdChannel, bnkNo);
		result.put("ResultCode",ResCodeConstant.SUCCESS_CODE);
		result.put("ResultDesc", "成功");
		
		return result;
	}



	@Override
	public JSONObject updatePayChannel(BankPayChannelVo payChannelVo) {
		JSONObject result = new JSONObject();
		String thirdChannel = payChannelVo.getThirdChannel();
		String bankNO = payChannelVo.getBankNO();
		String oldBankNo = payChannelVo.getOldBankNo();
		
		if (bankNO == null || "".equals(bankNO)) {
			result.put("ResultCode", ResCodeConstant.SystemConstant.CODE7402);
			result.put("ResultDesc", "支持银行卡号不能为空");
			return result;
		}
		if (thirdChannel == null || "".equals(thirdChannel)) {
			result.put("ResultCode", ResCodeConstant.SystemConstant.CODE7401);
			result.put("ResultDesc", "第三方支付渠道代码不能为空");
			return result;
		}

		if(existsThridChannelWithBankNO(result, thirdChannel, bankNO,oldBankNo)){
			return result;
		}
		payChannelDao.updatePayChannel(payChannelVo);
		result.put("ResultCode", ResCodeConstant.SUCCESS_CODE);
		result.put("ResultDesc", "新增成功");
		return  result;
	}



	private boolean existsThridChannelWithBankNO(JSONObject result, String thirdChannel, String bankNO,String oldBankNo) {
		SearchParam sp = new SearchParam();
		sp.setSp(new HashMap<String, Object>());
		sp.getSp().put("bnkNo", bankNO);
		sp.getSp().put("thirdChannel", thirdChannel);
	     List<BankPayChannelVo> queryPayChannelList = payChannelDao.queryPayChannelListPage(sp);
	    if((!queryPayChannelList.isEmpty())){
	    	if (queryPayChannelList.get(0).getBankNO().equals(bankNO)&&queryPayChannelList.get(0).getBankNO().equals(oldBankNo)) {
				return false;
			}
	    	result.put("ResultCode", ResCodeConstant.SystemConstant.CODE7403);
	    	result.put("ResultDesc", "支付渠道代码与支持银行已存在,不能重复添加");
	    	return true;
	    }
	    return false;
	}
}
