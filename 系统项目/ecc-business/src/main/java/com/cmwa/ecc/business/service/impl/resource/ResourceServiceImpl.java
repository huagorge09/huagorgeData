package com.cmwa.ecc.business.service.impl.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.commonVo.Employee;
import com.cmwa.ecc.business.commonVo.ResourceRoleRelationsVo;
import com.cmwa.ecc.business.commonVo.ResourceVo;
import com.cmwa.ecc.business.commonVo.RoleEmployeeRelationsVo;
import com.cmwa.ecc.business.commonVo.RoleVo;
import com.cmwa.ecc.business.dao.resource.ResourceDao;
import com.cmwa.ecc.business.dao.resource.ResourceEmployeeRelationsDao;
import com.cmwa.ecc.business.dao.resource.ResourceRoleRelationsDao;
import com.cmwa.ecc.business.service.resource.ResourceService;
import com.cmwa.ecc.business.service.roleinfo.RoleEmployeeRelationsService;
import com.cmwa.ecc.business.service.roleinfo.RoleInfoService;
import com.cmwa.ecc.business.utils.DictionaryUtil;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;

/**
 * @TODO	用户资源操作接口
 * @author ex-liuy
 * @createDate 2017年6月6日
 */
@Service
public class ResourceServiceImpl implements ResourceService {
	private static final Log logge = LogFactory.getLog(ResourceServiceImpl.class);
	
	@Autowired
	private ResourceDao resourceDao;
	@Autowired
	private RoleInfoService roleInfoService;
	@Autowired
	private RoleEmployeeRelationsService roleEmployeeRelationsService; 
	@Autowired
	private ResourceEmployeeRelationsDao resourceEmployeeRelationsDao;
	@Autowired
	private ResourceRoleRelationsDao resourceRoleRelationsDao;
	
	@Override
	public void insertResourceByLoginEmpAndResIsNotExist() {
		Employee emp = SessionUtils.getEmployee();
		DictionaryUtil dictUtil = DictionaryUtil.getInstance();
		String resTemp = dictUtil.getDictionaryName("RES_ADD_TEMP", "RES_ADD_TEMP", "RES_ADD_TEMP", "RESOURCE");
		//申明一个资源
		ResourceVo resVo = new ResourceVo();
		resVo.setResName(resTemp.format(resTemp, emp.getName()));
		resVo.setResOwner(emp.getID());
		//查询资源是否存在
		ResourceVo checkResvo =  resourceDao.queryResourceInfoByParam(resVo);
		if(null != checkResvo){
			return ;
		}
		SearchParam sp = new SearchParam();
		List<RoleVo> roleList = new ArrayList<RoleVo>();
		
		//查询当前人 所有角色
		roleList = roleInfoService.queryRoleInfoListByParam(sp);
		
		//遍历角色查询 所有角色 分享的角色
		/*for (RoleVo roleVo : roleList) {
			shareRoleList.addAll(roleInfoService.queryShareRoleListByRoleIdAndRecursion(roleVo));
		}*/
		//整合所有角色
//		shareRoleList.addAll(roleList);
		//获取所有角色ids
		List<String> roleIds = new ArrayList<String>();
		
		for (RoleVo roleVo : roleList) {
			roleIds.add(roleVo.getRoleId());
		}
		
//		String roleEmp = roleEmployeeRelationsService.queryRoleEmpRelByRoleId(shareRoleList);
		//将 角色记录资源关系
//		resVo.setEmpIds(roleEmp);
		resVo.setRoleIds(StringUtils.join(roleIds, ","));
		resVo.setCreateId(emp.getID());
		resVo.setModifyId(emp.getID());
		this.saveResource(resVo);
	}

	@Override
	public void saveResource(ResourceVo resourceVo) {
		ResourceVo checkResvo =  resourceDao.queryResourceInfoByParam(resourceVo);
		Employee emp = SessionUtils.getEmployee();
		//无资源则新增入库
		if(null == checkResvo){
			resourceDao.saveResource(resourceVo);
		}else{
			resourceVo.setResId(checkResvo.getResId());
		}
		
		resourceRoleRelationsDao.deleteResourceRoleRelationsByResOrRoleId(resourceVo.getResId(), "");
		//保存 资源角色关系
		String roleIds = resourceVo.getRoleIds();
		if(!StringUtils.isEmpty(roleIds)){
			String[] roleIdList = roleIds.split(",");
			SearchParam param= new SearchParam();
			param.getSp().put("createId", emp.getID());
			param.getSp().put("resId", resourceVo.getResId());
			param.getSp().put("roleIds",roleIdList);
			//批量新增角色关系列表
			resourceRoleRelationsDao.batchInsert(param);
		}
	}

	@Override
	public List<ResourceVo> queryNotAuthResListByRoleEmps(String roleId) {
		return resourceDao.queryNotAuthResListByRoleEmps(roleId);
	}

	@Override
	public List<ResourceVo> queryResourceListByRoleEmps(String roleId) {
		return resourceDao.queryResourceListByRoleEmps(roleId);
	}

	@Override
	public void insertResourceByRoleAndResIsNotExist(SearchParam sp) {
		String roleId = (String)sp.getSp().get("roleId");
		String resIds = (String)sp.getSp().get("resIds");
		String currEmpId = (String)sp.getSp().get("currEmpId");
		
		//保存 资源角色关系
		if(!StringUtils.isEmpty(resIds)){
			String[] resIdList = resIds.split(",");
			SearchParam param= new SearchParam();
			param.getSp().put("createId", currEmpId);
			param.getSp().put("resIds", resIdList);
			param.getSp().put("roleId",roleId);
			//批量新增员工关系列表
			resourceRoleRelationsDao.batchInsertByResIds(param);
		}
	}

	@Override
	public void quartzResetResourceEmpDataByRoleList() {
		logge.error("----quartzResetResourceEmpDataByRoleList----start...");
		//查询所有资源
		List<ResourceVo> allResList= resourceDao.queryAllResourceList();
		logge.error("----quartzResetResourceEmpDataByRoleList----allResList:" +allResList.size());
		for (ResourceVo resourceVo : allResList) {
			SearchParam sp = new SearchParam();
			
			List<RoleVo> shareRoleList = new ArrayList<RoleVo>();
			sp.getSp().put("resId", resourceVo.getResId());
			
			//该资源下所有角色
			List<ResourceRoleRelationsVo>  resRoleList = resourceRoleRelationsDao.queryAllResRoleList(sp);
			
			if(resRoleList.size() <= 0){
				continue;
			}
			
			//角色下所有分享角色
			for (ResourceRoleRelationsVo resRoleVo : resRoleList) {
				RoleVo roleVo = new RoleVo();
				roleVo.setRoleId(resRoleVo.getRoleId());
				//添加所有角色+分享角色
				shareRoleList.addAll(roleInfoService.queryShareRoleListByRoleIdAndRecursion(roleVo));
				shareRoleList.add(roleVo);
			}
			
			//获取所有角色 - 员工ids
			String roleEmp = roleEmployeeRelationsService.queryRoleEmpRelByRoleId(shareRoleList);
			
			//先删除 该资源所有 员工关系
			resourceEmployeeRelationsDao.deleteRelByResOrEmpId(resourceVo.getResId(), "");
			
			//将 员工记录资源关系
			if(!StringUtils.isEmpty(roleEmp)){
				String[] roleEmpList = roleEmp.split(",");
				sp.getSp().clear();
				sp.getSp().put("createId", "SYSTEM");
				sp.getSp().put("resId", resourceVo.getResId());
				sp.getSp().put("empIds",roleEmpList);
				//批量新增员工关系列表
				resourceEmployeeRelationsDao.batchInsert(sp);
			}
			
		}
		logge.error("----quartzResetResourceEmpDataByRoleList----end...");
	}

	@Override
	public void deleteResourceByResOrRole(String resId, String roleId) {
		resourceDao.deleteResourceByResOrRole(resId, roleId);
	}
}
