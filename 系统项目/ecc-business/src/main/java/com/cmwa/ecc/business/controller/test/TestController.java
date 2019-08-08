package com.cmwa.ecc.business.controller.test;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmwa.ecc.business.controller.BaseController;
import com.cmwa.ecc.business.service.synchronization.impl.SynEmpDataServiceImpl;

@Controller("testController")
@RequestMapping("/service/testController")
public class TestController extends BaseController {

	@Autowired
	private SynEmpDataServiceImpl dataServiceImpl;
	
	/**
	 * 同步用户，部门，角色信息
	 */
	@RequestMapping(value="/synData", produces="application/json;charset=utf-8")
	@ResponseBody
	public String synData(HttpServletResponse response) {
		Map<String, Object> map = dataServiceImpl.synEmpDataInfo();
		return map.toString();
	}
}
