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
import com.cmwa.ecc.business.dao.bank.DsBankBnkbaseDao;
import com.cmwa.ecc.business.dao.user.UserDao;
import com.cmwa.ecc.business.entity.bank.DsBankBnkbaseVo;
import com.cmwa.ecc.business.entity.user.User;
import com.cmwa.ecc.business.service.bank.DsBankBnkBaseService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.ResCodeConstant;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;
@Service
public class DsBankBnkBaseServiceImpl implements DsBankBnkBaseService{

	
	@Autowired
	private DsBankBnkbaseDao dsBankBnkbaseDao;
	
	@Autowired
	private UserDao userDao;
	
	@Override
	public JSONObject addDsBankBaseInfo(DsBankBnkbaseVo dsBankBnkbaseVo) throws Exception {
		JSONObject result = new JSONObject();
		if (dsBankBnkbaseVo.getDisOrder() == null) {
			dsBankBnkbaseVo.setDisOrder(0);
		}
		SearchParam sp = new SearchParam();
		sp.setSp(new HashMap<String, Object>());
		sp.getSp().put("bnkNo", dsBankBnkbaseVo.getBnkNo());
		List<DsBankBnkbaseVo> queryBankBaseInfoList =dsBankBnkbaseDao.queryBankBnkbaseListPage(sp);
		if ((!queryBankBaseInfoList.isEmpty())) {
			result.put("ResultCode", ResCodeConstant.SystemConstant.CODE7102);
			result.put("ResultDesc", "银行代码已存在");
			return result;
		}
		dsBankBnkbaseDao.addDsBankBaseInfo(dsBankBnkbaseVo);
		result.put("ResultCode", ResCodeConstant.SUCCESS_CODE);
		result.put("ResultDesc", "新增成功");
		return result;
	}

	@Override
	public ModelAndView openDialog(HttpServletRequest request) throws Exception {
		String method = request.getParameter("method");
		ModelAndView view = new ModelAndView();
		if (null == method || "".equals(method)) {
			view.setViewName("jsp/404");
		}
		if (null != method && "add".equals(method)) {
			view.setViewName("jsp/bankMgr/dsBankBnkBaseAdd");
		}

		if (null != method && "update".equals(method)) {
			view.setViewName("jsp/bankMgr/dsBankBnkBaseEdit");
			SearchParam sp = new SearchParam();
			String bnkNo = request.getParameter("bnkNo");
			sp.setSp(new HashMap<String, Object>());
			sp.getSp().put("bnkNo", bnkNo);
			DsBankBnkbaseVo dsBankBnkbaseVo = dsBankBnkbaseDao.queryBankBnkbaseListPage(sp).get(0);
			String cMan = dsBankBnkbaseVo.getCMan();
			// 获取创建者用户信息
			//User user = new User("2171186");
			User user = new User(cMan);
			userDao.getUserById(user);
			SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
			String eTime = sFormat.format(new Date());
			view.addObject("dsBankBnkbaseVo", dsBankBnkbaseVo);
			view.addObject("userVo", user);
			view.addObject("eTime", eTime);
		}
		return view;
	}

	@Override
	public DsBankBnkbaseVo getDsBankBaseInfo(String bnkno) throws Exception {
		return null;
	}

	@Override
	public Page<DsBankBnkbaseVo> queryBankBnkbaseListPage(SearchParam sp) throws Exception {
		List<DsBankBnkbaseVo> queryBankBnkbaseList = dsBankBnkbaseDao.queryBankBnkbaseListPage(sp);
		sp.setStart((sp.getPageNo() - Page.FIRST_PAGE) * sp.getLimit());
		sp.setEnd(sp.getStart() + sp.getLimit());
		sp.setTotal(dsBankBnkbaseDao.queryDsBankBnkbaseInfoListTotal(sp));
		return Page.create(queryBankBnkbaseList, sp.getStart(), sp.getLimit(),
				sp.getTotal());
	}

	@Override
	public JSONObject updateDsBankBaseInfo(DsBankBnkbaseVo dsBankBnkbaseVo) throws Exception {
		JSONObject result = new JSONObject();
		if (dsBankBnkbaseVo.getDisOrder() == null) {
			dsBankBnkbaseVo.setDisOrder(0);
		}
		dsBankBnkbaseDao.updateDsBankBaseInfo(dsBankBnkbaseVo);
		result.put("ResultCode", ResCodeConstant.SUCCESS_CODE);
		result.put("ResultDesc", "修改成功");
		return result;
	}

	@Override
	public JSONObject deleteDsBankBaseInfo(HttpServletRequest request) throws Exception {
		String bnkNo = request.getParameter("bnkNo");
		String operator = SessionUtils.getEmployee().getID();
		JSONObject result = new JSONObject();
		if (bnkNo == null) {
			result.put("ResultCode", ResCodeConstant.SystemConstant.CODE7101);
			result.put("ResultDesc", "银行卡号为空");
			return result;
		}
		dsBankBnkbaseDao.deleteDsBankBaseInfo(bnkNo);
		result.put("ResultCode", ResCodeConstant.SUCCESS_CODE);
		result.put("ResultDesc", "删除成功");
		return result;
	}

}
