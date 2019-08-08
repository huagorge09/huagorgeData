package com.cmwa.ecc.business.controller.privilege;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmwa.ecc.business.commonVo.Employee;
import com.cmwa.ecc.business.commonVo.ResourceVo;
import com.cmwa.ecc.business.commonVo.RoleVo;
import com.cmwa.ecc.business.controller.BaseController;
import com.cmwa.ecc.business.service.resource.ResourceService;
import com.cmwa.ecc.business.service.roleinfo.RoleInfoService;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;

@RequestMapping("/service/privilege")
@Controller
public class PrivilegeController extends BaseController {
	
	@Autowired
	private RoleInfoService roleInfoService;
	@Autowired
	private ResourceService resourceService;
	
	
	@RequestMapping("/privilegeListView")
	public String goPrivilegeListPage(){
		return "jsp/privilege/privilegeList";
	}
	
	@RequestMapping("/privilegeUpdateView")
	public String goPrivilegeUpdateView(@RequestParam("roleId")String roleId,ModelMap model){
		RoleVo roleVo = new RoleVo();
		List<RoleVo> shareRoleList = new ArrayList<RoleVo>();
		//加载角色 分享角色信息
		if(!StringUtils.isEmpty(roleId)){
			roleVo = roleInfoService.queryRoleInfoById(roleId);
			shareRoleList = roleInfoService.queryShareRoleListByRoleId(roleVo);
		}
		
		//加载 资源信息
		
		//已选列表
		List<ResourceVo>  resourceList = resourceService.queryResourceListByRoleEmps(roleId);
		model.put("roleVo", roleVo);
		model.put("shareRoleList", shareRoleList);
		model.put("resourceList", resourceList);
		return "jsp/privilege/privilegeUpdate";
	}
	
	@RequestMapping("/privilegeDetailView")
	public String goPrivilegeDetailView(@RequestParam("roleId")String roleId,ModelMap model){
		RoleVo roleVo = new RoleVo();
		List<RoleVo> shareRoleList = new ArrayList<RoleVo>();
		//加载角色 分享角色信息
		if(!StringUtils.isEmpty(roleId)){
			roleVo = roleInfoService.queryRoleInfoById(roleId);
			shareRoleList = roleInfoService.queryShareRoleListByRoleId(roleVo);
		}
		
		//加载 资源信息
		//已选列表
		List<ResourceVo>  resourceList = resourceService.queryResourceListByRoleEmps(roleId);
		model.put("roleVo", roleVo);
		model.put("shareRoleList", shareRoleList);
		model.put("resourceList", resourceList);
		return "jsp/privilege/privilegeDetail";
	}
	
	@RequestMapping("/queryNotAuthResListByRoleEmps")
	@ResponseBody
	public List<ResourceVo> queryNotAuthResListByRoleEmps(@RequestParam("roleId")String roleId){
		List<ResourceVo> resourceList = resourceService.queryNotAuthResListByRoleEmps(roleId);
		return resourceList;
	}
	
	/**
	 * 更新权限设置
	 * @TODO	
	 * @author ex-liuy
	 * @param model
	 * @param roleId 角色id
	 * @param resId	 资源id
	 * @return
	 */
	@RequestMapping("/updatePrivilege")
	public String updatePrivilege(ModelMap model, @RequestParam(value = "roleId") String roleId, @RequestParam(value = "resId") String resId){
		SearchParam sp = new SearchParam();
		Employee emp = SessionUtils.getEmployee();
		sp.getSp().put("roleId", roleId);
		sp.getSp().put("resIds", resId);
		sp.getSp().put("currEmpId", emp.getID());
		//先删除 该角色所有的 资源关系
		resourceService.deleteResourceByResOrRole("", roleId);
		resourceService.insertResourceByRoleAndResIsNotExist(sp);
		return redirectSuccess();
	}
	
	/**
	 * @TODO	更新查询权限
	 * @author ex-liuy
	 */
	@RequestMapping("/updatePrivilegeAuthRel")
	@ResponseBody
	public void updatePrivilegeAuthRel(){
		resourceService.quartzResetResourceEmpDataByRoleList();
	}
}

