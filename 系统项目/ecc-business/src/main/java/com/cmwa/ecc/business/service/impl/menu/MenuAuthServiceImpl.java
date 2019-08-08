package com.cmwa.ecc.business.service.impl.menu;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.commonVo.MenuAuthVo;
import com.cmwa.ecc.business.dao.menu.MenuAuthDao;
import com.cmwa.ecc.business.exception.RepositoryException;
import com.cmwa.ecc.business.service.menu.MenuAuthService;

@Service
public class MenuAuthServiceImpl implements MenuAuthService {
	@Resource
	private MenuAuthDao menuAuthDao;

	@Override
	public void saveOrmemberMenu(String unionId, String menuIds) throws RepositoryException {
		String[] mIds = menuIds.split(",");
		String[] unionIds = unionId.split("-");
		List<MenuAuthVo> list = new ArrayList<MenuAuthVo>();
		for (int i = 0; i < mIds.length; i++) {
			String mid = mIds[i];
			if (StringUtils.isNotBlank(mid)) {
				MenuAuthVo menuAuthVo = new MenuAuthVo();
				menuAuthVo.setOrgId(unionIds[0]);
				menuAuthVo.setMenuId(mid);
				menuAuthVo.setTitleId(unionIds[1]);
				menuAuthVo.setStatus("1");
				list.add(menuAuthVo);
			}
		}
		// 先删除stationId对应关联表中的记录，再更新
		deleteOrmemberMenuByUnionId(unionIds[0], unionIds[1]);
		// 批量保存岗位和菜单的关联关系
		if (!list.isEmpty()) {
			menuAuthDao.insertBatchOrmemberMenu(list);
		}
	}

	@Override
	public void deleteOrmemberMenuByUnionId(String orgId, String titleId) {
		menuAuthDao.deleteOrmemberMenuByUnionId(orgId, titleId);
	}

	@Override
	public void saveEmpMenu(String empId, String menuIds) throws RepositoryException {
		String[] mIds = menuIds.split(",");
		List<MenuAuthVo> list = new ArrayList<MenuAuthVo>();
		for (int i = 0; i < mIds.length; i++) {
			String mid = mIds[i];
			if (StringUtils.isNotBlank(mid)) {
				MenuAuthVo menuAuthVo = new MenuAuthVo();
				menuAuthVo.setMenuId(mid);
				menuAuthVo.setEmpId(empId);
				menuAuthVo.setStatus("1");
				list.add(menuAuthVo);
			}
		}
		// 先删除empId对应关联表中的记录，再更新
		deleteEmpMenuByEmpId(empId);
		// 批量保存员工和菜单的关联关系
		if (!list.isEmpty()) {
			menuAuthDao.insertBatchEmpMenu(list);
		}
	}

	@Override
	public void deleteEmpMenuByEmpId(String empId) {
		menuAuthDao.deleteEmpMenuByEmpId(empId);
	}

	@Override
	public Set<String> queryEmpDefaultSelectedMenu(String empId) {
		List<String> menuList = menuAuthDao.queryEmpDefaultSelectedMenu(empId);
		return asSet(menuList);
	}

	@Override
	public Set<String> queryOrmemberDefaultSelectedMenu(String unionId) {
		String[] unionIds = unionId.split("-");
		List<String> menuList = menuAuthDao.queryOrmemberDefaultSelectedMenu(unionIds[0], unionIds[1]);
		return asSet(menuList);
	}

	/**
	 * list 转换为 set
	 * 
	 * @param list
	 * @return
	 */
	private Set<String> asSet(List<String> list) {
		Set<String> set = new HashSet<String>();
		if (list != null) {
			for (String value : list) {
				set.add(value);
			}
		}
		return set;
	}

	@Override
	public Integer queryEmpMenuCountByPath(String empId, String requestPath) {
		return menuAuthDao.queryEmpMenuCountByPath(empId, requestPath);
	}

	@Override
	public Integer queryOrmemberMenuCountByPath(String empId, String requestPath) {
		return menuAuthDao.queryOrmemberMenuCountByPath(empId, requestPath);
	}

	/**
	 * 查询选中的岗位菜单，支持合并原岗位菜单，以逗号（,）分开。
	 */
	@Override
	public String queryOrmemberDefaultSelectedMenuWithComma(String srcUnionId, String destUnionId, Boolean merge) {
		// 复制的源头岗位
		String[] sunionIds = srcUnionId.split("-");
		List<String> menuList = menuAuthDao.queryOrmemberDefaultSelectedMenu(sunionIds[0], sunionIds[1]);
		// 粘贴到的目的岗位
		if (merge && destUnionId != null) {
			String[] dunionIds = destUnionId.split("-");
			List<String> dmenuList = menuAuthDao.queryOrmemberDefaultSelectedMenu(dunionIds[0], dunionIds[1]);
			menuList.addAll(dmenuList);
		}
		Set<String> memuSet = asSet(menuList);
		String result = "";
		for (String val : memuSet) {
			result += val + ",";
		}
		return result;
	}

	/**
	 * 查询选中的岗位菜单，支持合并原用户菜单，以逗号（,）分开。
	 */
	@Override
	public String queryOrmemberDefaultSelectedMergeEmpMenuWithComma(String srcUnionId, String empId, Boolean merge) {
		// 复制的源头岗位
		String[] sunionIds = srcUnionId.split("-");
		List<String> menuList = menuAuthDao.queryOrmemberDefaultSelectedMenu(sunionIds[0], sunionIds[1]);
		// 源用户菜单
		if (merge && empId != null) {
			List<String> emenuList = menuAuthDao.queryEmpDefaultSelectedMenu(empId);
			menuList.addAll(emenuList);
		}
		Set<String> memuSet = asSet(menuList);
		String result = "";
		for (String val : memuSet) {
			result += val + ",";
		}
		return result;
	}

}
