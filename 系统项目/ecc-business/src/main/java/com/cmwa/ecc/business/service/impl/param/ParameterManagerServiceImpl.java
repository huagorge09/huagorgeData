package com.cmwa.ecc.business.service.impl.param;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.cmwa.ecc.business.entity.paramvo.ParameterVo;
import com.cmwa.ecc.business.service.common.CommonService;
import com.cmwa.ecc.business.service.param.ParameterManagerService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.ResCodeConstant;
import com.cmwa.ecc.business.utils.SearchParam;

import net.sf.json.JSONObject;

@Service
public class ParameterManagerServiceImpl implements ParameterManagerService {

	@Autowired
	private CommonService commonService;
	
	@Override
	public Page<ParameterVo> queryAllParameter(SearchParam sp) {
		List<ParameterVo> parameterList = commonService.queryAllParameterListPage(sp);
		sp.setStart((sp.getPageNo() - Page.FIRST_PAGE) * sp.getLimit());
		sp.setEnd(sp.getStart() + sp.getLimit());
		sp.setTotal(commonService.queryAllParameterTotal(sp));
		return Page.create(parameterList, sp.getStart(), sp.getLimit(),
				sp.getTotal());
	}

	@Override
	public Page<ParameterVo> queryBusinParameterListPage(SearchParam sp) {
		List<ParameterVo> parameterList = commonService.queryBusinParameterListPage(sp);
		return Page.create(parameterList, sp.getStart(), sp.getLimit(),sp.getTotal());
	}
	
	@Override
	public Page<ParameterVo> querySunParameterListPage(SearchParam sp) {
		List<ParameterVo> parameterList = commonService.getParameterListPage(sp);
		sp.setStart((sp.getPageNo() - Page.FIRST_PAGE) * sp.getLimit());
		sp.setEnd(sp.getStart() + sp.getLimit());
		sp.setTotal(commonService.getParameterListTotal(sp));
		return Page.create(parameterList, sp.getStart(), sp.getLimit(),
				sp.getTotal());
	}

	@Override
	public ModelAndView openDialog(HttpServletRequest request,String opmnm,String ppmnm) {
		String method = request.getParameter("method");
		String pmst=request.getParameter("pmst");
		String pmky=request.getParameter("pmky");
		String pmco=request.getParameter("pmco");
		ModelAndView view = new ModelAndView();
		if (null == method || "".equals(method)) {
			view.setViewName("jsp/404");
		}
		if (null != method && "add".equals(method)) {
			view.setViewName("jsp/param/parameterAdd");
		}

		if (null != method && "update".equals(method)) {
			view.setViewName("jsp/param/parameterUpdate");
		}
		view.addObject("opmnm",opmnm);
		view.addObject("pmst",pmst);
		view.addObject("pmky", pmky);
		view.addObject("pmco",pmco);
		view.addObject("oldPmco",pmco);
		view.addObject("pmnm",ppmnm);
		return view;
	}

	@Override
	public JSONObject addParameter(ParameterVo parameterVo) {
		JSONObject result=new JSONObject();
		String pmco = parameterVo.getPmco();
		String pmky = parameterVo.getPmky();
		String pmst = parameterVo.getPmst();
		
		SearchParam sp = new SearchParam();
		sp.setSp(new HashMap<String, Object>());
		sp.getSp().put("Qpmco", pmco);
		sp.getSp().put("pmky", pmky);
		sp.getSp().put("pmst", pmst);
		sp.getSp().put("pmco", pmco);
		List<ParameterVo> parameterList = commonService.getParameterListPage(sp);
		if (parameterList.isEmpty()) {
			commonService.addParameter(parameterVo);
			result.put("ResultCode", ResCodeConstant.SUCCESS_CODE);
			result.put("ResultDesc", "新增成功");
		}else{
			result.put("ResultCode", ResCodeConstant.SystemConstant.CODE7501);
			result.put("ResultDesc", "重复参数值");
		}
		
		return result;
	}

	@Override
	public JSONObject modParameter(ParameterVo parameterVo) {
		JSONObject result=new JSONObject();
		String pmco = parameterVo.getPmco();
		String pmky = parameterVo.getPmky();
		String pmst = parameterVo.getPmst();
		
		SearchParam sp = new SearchParam();
		sp.setSp(new HashMap<String, Object>());
		sp.getSp().put("Qpmco", pmco);
		sp.getSp().put("pmky", pmky);
		sp.getSp().put("pmst", pmst);
		sp.getSp().put("pmco", pmco);
		List<ParameterVo> parameterList = commonService.getParameterListPage(sp);
		if (parameterList.isEmpty()) {
			commonService.updateParameter(parameterVo);
			result.put("ResultCode", ResCodeConstant.SUCCESS_CODE);
			result.put("ResultDesc", "修改成功");
		}else{
			if (parameterList.get(0).getPmco().equals(parameterVo.getOldPmco())) {
				commonService.updateParameter(parameterVo);
				result.put("ResultCode", ResCodeConstant.SUCCESS_CODE);
				result.put("ResultDesc", "修改成功");
			}else{
				result.put("ResultCode", ResCodeConstant.SystemConstant.CODE7501);
				result.put("ResultDesc", "重复参数值");
			}
		}
		
		return result;
	}

	@Override
	public JSONObject delParameter(ParameterVo parameterVo) {
		JSONObject result =new JSONObject();
		commonService.delParameter(parameterVo);
		result.put("ResultCode",ResCodeConstant.SUCCESS_CODE);
		result.put("ResultDesc", "成功");
		return result;
	}

}
