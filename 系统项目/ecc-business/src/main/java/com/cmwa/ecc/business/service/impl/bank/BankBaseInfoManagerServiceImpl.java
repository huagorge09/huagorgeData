package com.cmwa.ecc.business.service.impl.bank;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.cmwa.ecc.business.commonVo.Employee;
import com.cmwa.ecc.business.dao.bank.BankBaseDao;
import com.cmwa.ecc.business.dao.user.UserDao;
import com.cmwa.ecc.business.entity.bank.BankBnkbaseVo;
import com.cmwa.ecc.business.entity.user.User;
import com.cmwa.ecc.business.service.bank.BankBaseInfoManagerService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.ResCodeConstant;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;

@Service
public class BankBaseInfoManagerServiceImpl implements BankBaseInfoManagerService {
	
	@Autowired
	private BankBaseDao bankBaseDao;

	@Autowired
	private UserDao userDao;

	@Override
	public Page<BankBnkbaseVo> queryBankBaseInfoListPage(SearchParam sp) {
		List<BankBnkbaseVo> queryBankBaseInfoList = bankBaseDao
				.queryBankBaseInfoListPage(sp);
		sp.setStart((sp.getPageNo() - Page.FIRST_PAGE) * sp.getLimit());
		sp.setEnd(sp.getStart() + sp.getLimit());
		sp.setTotal(bankBaseDao.queryBankBaseInfoListTotal(sp));
		return Page.create(queryBankBaseInfoList, sp.getStart(), sp.getLimit(),
				sp.getTotal());
	}

	@Override
	public JSONObject delBankBase(HttpServletRequest request) {
		String bnkNo = request.getParameter("bnkNo");
		String operator = SessionUtils.getEmployee().getID();
		JSONObject result = new JSONObject();
		if (bnkNo == null) {
			result.put("ResultCode", ResCodeConstant.SystemConstant.CODE7101);
			result.put("ResultDesc", "银行卡号为空");
			return result;
		}
		bankBaseDao.delBankBase(bnkNo);
		result.put("ResultCode", ResCodeConstant.SUCCESS_CODE);
		result.put("ResultDesc", "删除成功");
		return result;
	}

	@Override
	public ModelAndView openDialog(HttpServletRequest request) {
		String method = request.getParameter("method");
		ModelAndView view = new ModelAndView();
		if (null == method || "".equals(method)) {
			view.setViewName("jsp/404");
		}
		if (null != method && "add".equals(method)) {
			view.setViewName("jsp/bankMgr/bnkBaseAdd");
		}

		if (null != method && "update".equals(method)) {
			view.setViewName("jsp/bankMgr/bnkBaseEdit");
			SearchParam sp = new SearchParam();
			String bnkNo = request.getParameter("bnkNo");
			sp.setSp(new HashMap<String, Object>());
			sp.getSp().put("bnkNo", bnkNo);
			BankBnkbaseVo bankBnkbaseVo = bankBaseDao.queryBankBaseInfoListPage(sp).get(0);
			String createDate = bankBnkbaseVo.getcTime();
			if(createDate != null && createDate.length() > 11 ) {
				createDate =  createDate.substring(0, 10);
			}
			bankBnkbaseVo.setcTime(createDate);
			String cMan = bankBnkbaseVo.getCMan();
			// 获取创建者用户信息
			//User user = new User("2171186");
			User user = new User(cMan);
			userDao.getUserById(user);
			SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
			String eTime = sFormat.format(new Date());
			view.addObject("bankbaseVo", bankBnkbaseVo);
			view.addObject("userVo", user);
			view.addObject("eTime", eTime);
		}
		return view;
	}

	@Override
	public JSONObject addBankBaseInfo(BankBnkbaseVo bankBaseInfo) {
		JSONObject result = new JSONObject();
		if (bankBaseInfo.getDisOrder() == null) {
			bankBaseInfo.setDisOrder("0");
		}
		SearchParam sp = new SearchParam();
		sp.setSp(new HashMap<String, Object>());
		sp.getSp().put("bnkNo", bankBaseInfo.getBnkNo());
		List<BankBnkbaseVo> queryBankBaseInfoList = bankBaseDao.queryBankBaseInfoListPage(sp);
		if ((!queryBankBaseInfoList.isEmpty())) {
			result.put("ResultCode", ResCodeConstant.SystemConstant.CODE7102);
			result.put("ResultDesc", "银行代码已存在");
			return result;
		}
		bankBaseDao.addBankBaseInfo(bankBaseInfo);
		result.put("ResultCode", ResCodeConstant.SUCCESS_CODE);
		result.put("ResultDesc", "新增成功");
		return result;
	}

	@Override
	public JSONObject modBankBaseInfo(BankBnkbaseVo bankBaseInfo) {
		JSONObject result = new JSONObject();
		if (bankBaseInfo.getDisOrder() == null) {
			bankBaseInfo.setDisOrder("0");
		}
		bankBaseDao.updateBankBaseInfo(bankBaseInfo);
		result.put("ResultCode", ResCodeConstant.SUCCESS_CODE);
		result.put("ResultDesc", "修改成功");
		return result;
	}
}
