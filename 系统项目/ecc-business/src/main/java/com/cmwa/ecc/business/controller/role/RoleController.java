package com.cmwa.ecc.business.controller.role;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmwa.ecc.business.commonVo.RoleVo;
import com.cmwa.ecc.business.controller.BaseController;
import com.cmwa.ecc.business.exception.ValidateFailedException;
import com.cmwa.ecc.business.service.roleinfo.RoleInfoService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * @TODO	角色操作 
 * @author ex-liuy
 * @createDate 2017年6月5日
 */
@Controller
@RequestMapping("/service/role")
public class RoleController extends BaseController {

	@Autowired
	private RoleInfoService roleInfoService;
	
	@RequestMapping("/roleListView")
	public String goRoleListIndexPageView(){
		return "jsp/role/roleList";
	}
	
	@RequestMapping("/roleOperationView")
	public String goRoleInfoAddPageView(@RequestParam("method")String method,
				@RequestParam(value="roleId",required=false)String roleId,ModelMap model){
		RoleVo roleVo = new RoleVo();
		if(!StringUtils.isEmpty(roleId)){
			roleVo = roleInfoService.queryRoleInfoById(roleId);
			if(!StringUtils.isEmpty(roleVo.getShareRoleIds())){
				String [] shareIds =  roleVo.getShareRoleIds().split(",");
				model.put("shareIds", shareIds);
			}
		}
		model.put("method", method);
		model.put("roleVo", roleVo);
		
		return "jsp/role/roleOperation";
	}
	
	
	@RequestMapping("/roleListPage")
	@ResponseBody
	public Page<RoleVo> queryRoleInfoListPage(SearchParam sp){
		return roleInfoService.queryRoleInfoListPage(sp);
	}
	
	/**
	 * 查找角色
	 * 
	 * @param sp
	 * @return
	 */
	@RequestMapping("/queryRole")
	@ResponseBody
	public List<RoleVo> queryRole(RoleVo role) {
		List<RoleVo> roles= roleInfoService.queryRole(role);
		return  roles;
	}
	
	/**
	 * @TODO	保存新增角色
	 * @author ex-liuy
	 * @param role
	 * @param model
	 * @return
	 * @throws ValidateFailedException
	 */
	@RequestMapping("/roleInfoSave")
	public String roleInfoSave(RoleVo role,@RequestParam("method")String method, ModelMap model) throws ValidateFailedException {
		roleInfoService.insertRoleInfoByRole(role);
		return redirectSuccess();
	}
	
	/**
	 * @TODO	修改角色
	 * @author ex-liuy
	 * @param role
	 * @param model
	 * @return
	 * @throws ValidateFailedException
	 */
	@RequestMapping("/roleInfoUpdate")
	public String roleInfoUpdate(RoleVo role,@RequestParam("method")String method, ModelMap model) throws ValidateFailedException {
		roleInfoService.updateRoleInfoByRole(role);
		return redirectSuccess();
	}
	
	@RequestMapping("/matchRoleInfoIsNotExist")
	@ResponseBody
	public boolean matchRoleInfoIsNotExist(@RequestParam("roleName")String roleName,@RequestParam("roleId")String roleId){
		boolean flag = false;
		RoleVo roleVo = new RoleVo();
		roleVo.setRoleName(roleName.trim());
		roleVo.setRoleId(roleId.trim());
		List<RoleVo> roles = roleInfoService.matchRoleInfoIsNotExist(roleVo);
		if(roles.size() <= 0){
			flag = true;
		}
		return flag;
	}
}
