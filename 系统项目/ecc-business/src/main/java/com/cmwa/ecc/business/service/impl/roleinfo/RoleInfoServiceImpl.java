package com.cmwa.ecc.business.service.impl.roleinfo;

import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.driver.DiagnosabilityMXBean;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.commonVo.Employee;
import com.cmwa.ecc.business.commonVo.RoleVo;
import com.cmwa.ecc.business.commonVo.UserInfoVo;
import com.cmwa.ecc.business.dao.roleinfo.RoleEmployeeRelationsDao;
import com.cmwa.ecc.business.dao.roleinfo.RoleInfoDao;
import com.cmwa.ecc.business.exception.ValidateFailedException;
import com.cmwa.ecc.business.service.roleinfo.AuthorityGroupService;
import com.cmwa.ecc.business.service.roleinfo.RoleEmployeeRelationsService;
import com.cmwa.ecc.business.service.roleinfo.RoleInfoService;
import com.cmwa.ecc.business.service.userInfo.UserInfoService;
import com.cmwa.ecc.business.utils.DictionaryUtil;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;

@Service
public class RoleInfoServiceImpl implements RoleInfoService {
	
	private static final Log logger = LogFactory.getLog(RoleInfoServiceImpl.class);
	
	@Autowired
	private RoleInfoDao	roleInfoDao;
	@Autowired
	private RoleEmployeeRelationsService roleEmployeeRelationsService;
	@Autowired
	private AuthorityGroupService authorityGroupService;
	@Autowired
	private UserInfoService userInfoService;
	
	
	@Override
	public Page<RoleVo> queryRoleInfoListPage(SearchParam sp) {
		Employee emp = SessionUtils.getEmployee();
		sp.getSp().put("currEmpId", emp.getID());
		List<RoleVo> roleInfoList = roleInfoDao.queryRoleInfoListPage(sp);
		return Page.create(roleInfoList, sp.getStart(), sp.getLimit(), sp.getTotal());
	}

	@Override
	public List<RoleVo> queryRoleInfoListByParam(SearchParam sp) {
		Employee emp = SessionUtils.getEmployee();
		sp.getSp().put("currEmpId", emp.getID());
		return roleInfoDao.queryRoleInfoListByParam(sp);
	}

	@Override
	public RoleVo queryRoleInfoById(String roleId) {
		return roleInfoDao.queryRoleInfoById(roleId);
	}

	@Override
	public RoleVo queryRoleInfoByParam(RoleVo roleVo) {
		return roleInfoDao.queryRoleInfoByParam(roleVo);
	}

	@Override
	public void insertRoleInfoByRole(RoleVo roleVo)  throws ValidateFailedException{
		//查询角色是否存在
		RoleVo checkRoleVo = roleInfoDao.queryRoleInfoByParam(roleVo);
		Employee emp = SessionUtils.getEmployee();
		roleVo.setCreateId(emp.getID());
		roleVo.setModifyId(emp.getID());
		if(checkRoleVo == null ){
			//新增角色入库
			roleInfoDao.insertRoleInfoByRole(roleVo);
		}else{
			roleVo.setRoleId(checkRoleVo.getRoleId());
		}
		
		//插入到 角色-员工关系表
		String empIds = roleVo.getEmpIds();
		if (!StringUtils.isEmpty(empIds)) {
			String[] empIdList=empIds.split(",");
			SearchParam param=new SearchParam();
			param.getSp().put("createId", emp.getID());
			param.getSp().put("roleId", roleVo.getRoleId());
			param.getSp().put("empIds",empIdList);
			//批量新增员工关系列表
			roleEmployeeRelationsService.batchInsert(param);
		}
		//先删除角色分享
		authorityGroupService.deleteByRoleId(roleVo.getRoleId());
		//插入数据到角色分享表
		String shareRoleIds = roleVo.getShareRoleIds();
		if (!StringUtils.isEmpty(shareRoleIds)) {
			String[] shareRoleIdList=shareRoleIds.split(",");
			SearchParam param=new SearchParam();
			param.getSp().put("roleId", roleVo.getRoleId());
			param.getSp().put("shareRoleIds",shareRoleIdList);
			//批量新增角色分享表数据
			authorityGroupService.batchInsert(param);
		}
		
		//分享闭环校验
		String clShareRole = roleInfoDao.getClShareNamesByRoleId(roleVo.getRoleId());
		if(!StringUtils.isEmpty(clShareRole)){
			throw new ValidateFailedException("共享下列角色出现闭环：" + clShareRole);
		}
	}

	@Override
	public void insertRoleInfoByLoginEmpAndRoleIsNotExist() {
		RoleVo roleVo = new RoleVo();
		Employee emp = SessionUtils.getEmployee();
		DictionaryUtil dictUtil = DictionaryUtil.getInstance();
		String salesMgrTemp = dictUtil.getDictionaryName("ROLE_ADD_TEMP", "ROLE_ADD_TEMP", "ROLE_ADD_TEMP", "ROLESALESMGR");
//		String deptMgrTemp = dictUtil.getDictionaryName("ROLE_ADD_TEMP", "ROLE_ADD_TEMP", "ROLE_ADD_TEMP", "ROLEDEPTMGR");
//		String companyMgrTemp = dictUtil.getDictionaryName("ROLE_ADD_TEMP", "ROLE_ADD_TEMP", "ROLE_ADD_TEMP", "ROLECOMPANYMGR");
//		List<String> shareRoleIds = new ArrayList<String>();
		
		//组装角色名
		roleVo.setRoleName(salesMgrTemp.format(salesMgrTemp, emp.getName()));
		roleVo.setEmpIds(emp.getID());
		//查询角色是否存在
		RoleVo checkRoleVo = roleInfoDao.queryRoleInfoByParam(roleVo);
		if(checkRoleVo != null ){
			//结束角色设置
			return ;
		}
		/*
		//获取用户部门
		List<UserInfoVo> userInfo =  userInfoService.searchUserInfoById(emp.getID());
		//获取部门负责人角色id
		for (UserInfoVo userInfoVo : userInfo) {
			String deptMgrRoleName = deptMgrTemp.format(deptMgrTemp, userInfoVo.getSecondOrgname());
			RoleVo companyMgrRole =  new RoleVo();
			companyMgrRole.setRoleName(deptMgrRoleName);
			companyMgrRole = roleInfoDao.queryRoleInfoByParam(companyMgrRole);
			if(null != companyMgrRole){
				shareRoleIds.add(companyMgrRole.getRoleId());
			}
		}
		
		//获取公司领导角色id
		RoleVo companyMgrRole = new RoleVo();
		companyMgrRole.setRoleName(companyMgrTemp);
		companyMgrRole =  roleInfoDao.queryRoleInfoByParam(companyMgrRole);
		if(null != companyMgrRole){
			shareRoleIds.add(companyMgrRole.getRoleId());
		}*/
		
		//设置分享id
//		roleVo.setShareRoleIds(StringUtils.join(shareRoleIds,","));
		this.insertRoleInfoByRole(roleVo);
	}

	@Override
	public List<RoleVo> queryRole(RoleVo role) {
		return roleInfoDao.queryRole(role);
	}

	@Override
	public void updateRoleInfoByRole(RoleVo roleVo) throws ValidateFailedException{
		//查询角色是否存在
		Employee emp = SessionUtils.getEmployee();
		roleVo.setModifyId(emp.getID());
		//修改角色入库
		roleInfoDao.updateRoleInfoByRole(roleVo);
		
		//先删除角色下 所有员工关系
		roleEmployeeRelationsService.deleteByRoleId(roleVo.getRoleId());
		//插入到 角色-员工关系表
		String empIds = roleVo.getEmpIds();
		if (!StringUtils.isEmpty(empIds)) {
			String[] empIdList=empIds.split(",");
			SearchParam param=new SearchParam();
			param.getSp().put("createId", emp.getID());
			param.getSp().put("roleId", roleVo.getRoleId());
			param.getSp().put("empIds",empIdList);
			//批量新增员工关系列表
			roleEmployeeRelationsService.batchInsert(param);
		}
		
		//先删除角色分享
		authorityGroupService.deleteByRoleId(roleVo.getRoleId());
		//插入数据到角色分享表
		String shareRoleIds = roleVo.getShareRoleIds();
		if (!StringUtils.isEmpty(shareRoleIds)) {
			String[] shareRoleIdList=shareRoleIds.split(",");
			SearchParam param=new SearchParam();
			param.getSp().put("roleId", roleVo.getRoleId());
			param.getSp().put("shareRoleIds",shareRoleIdList);
			//批量新增角色分享表数据
			authorityGroupService.batchInsert(param);
		}
		
		//分享闭环校验
		String clShareRole = roleInfoDao.getClShareNamesByRoleId(roleVo.getRoleId());
		if(!StringUtils.isEmpty(clShareRole)){
			throw new ValidateFailedException("共享下列角色出现闭环：" + clShareRole);
		}
	}

	@Override
	public List<RoleVo> matchRoleInfoIsNotExist(RoleVo role) {
		return roleInfoDao.matchRoleInfoIsNotExist(role);
	}

	@Override
	public List<RoleVo> queryShareRoleListByRoleId(RoleVo role) {
		return roleInfoDao.queryShareRoleListByRoleId(role);
	}

	@Override
	public List<RoleVo> queryShareRoleListByRoleIdAndRecursion(RoleVo role) {
		List<RoleVo> allShareRoleList = new ArrayList<RoleVo>();
		List<RoleVo> currRoleList = this.queryShareRoleListByRoleId(role);
		allShareRoleList.addAll(currRoleList);
		for (RoleVo roleVo : currRoleList) {
			allShareRoleList.addAll(this.queryShareRoleListByRoleId(roleVo));
		}
		return allShareRoleList;
	}
	
}
