package com.cmwa.ecc.business.controller.param;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cmwa.ecc.business.entity.paramvo.ParameterVo;
import com.cmwa.ecc.business.service.param.ParameterManagerService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

import net.sf.json.JSONObject;



@Controller
@RequestMapping(value = "service/parameterManager" )
public class ParameterController {
	private static Logger logger = Logger.getLogger(ParameterController.class);
	
	@Autowired
	private ParameterManagerService parameterService;
	
	@RequestMapping(value = "/parameterMgr.do" ,method = RequestMethod.GET)
	public String goParameterManagerView() {
		return "jsp/param/paraManageIndex";
	}
	
	/**
	 * 业务管理 下 参数管理
	 * @return
	 */
	@RequestMapping(value = "/businParaManageIndexView.do" ,method = RequestMethod.GET)
	public String goBusinParaManageIndexView() {
		return "jsp/businessMgr/param/businParaManageIndex";
	}
	
	@RequestMapping(value = "/parameter.xhtml" ,method={RequestMethod.GET})
	@ResponseBody
	public Page<ParameterVo> queryAllParameter(SearchParam sp){
		return parameterService.queryAllParameter(sp);
	}
	
	/**
	 * 业务管理-参数管理
	 * 限制查询
	 * @param sp
	 * @return
	 */
	@RequestMapping(value = "/businParameterListPage.xhtml" ,method={RequestMethod.GET})
	@ResponseBody
	public Page<ParameterVo> queryBusinParameterListPage(SearchParam sp){
		return parameterService.queryBusinParameterListPage(sp);
	}
	
	@RequestMapping(value="/getParameter.xhtml",method={RequestMethod.GET})
	@ResponseBody
	public Page<ParameterVo> querySunParameterList(SearchParam sp){
		return parameterService.querySunParameterListPage(sp);
	}
	
	
	@RequestMapping(value="/openDialog.xhtml",method={RequestMethod.GET})
	public ModelAndView openDialog(@Param("pmnm")String pmnm,@Param("ppmnm")String ppmnm,HttpServletRequest request){
		return parameterService.openDialog(request,pmnm,ppmnm);
	}
	
	@RequestMapping(value="/add.xhtml",method={RequestMethod.POST})
	public void addParameter(ParameterVo parameterVo,HttpServletRequest request,HttpServletResponse response) throws IOException{
		JSONObject result=parameterService.addParameter(parameterVo);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().print(result.toString());
	}
	@RequestMapping(value="/update.xhtml",method={RequestMethod.POST})
	public void modParameter(ParameterVo parameterVo,HttpServletRequest request,HttpServletResponse response) throws IOException{
		JSONObject result=parameterService.modParameter(parameterVo);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().print(result.toString());
	}
		
	@RequestMapping(value="/del.xhtml",method={RequestMethod.POST})
	public void delParameter(ParameterVo parameterVo,HttpServletRequest request,HttpServletResponse response) throws IOException{
		JSONObject result=parameterService.delParameter(parameterVo);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().print(result.toString());
	}
	
	
}
