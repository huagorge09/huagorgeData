package com.cmwa.ecc.business.service.impl.menu;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.commonVo.Employee;
import com.cmwa.ecc.business.commonVo.MenuVo;
import com.cmwa.ecc.business.dao.menu.MenuDao;
import com.cmwa.ecc.business.exception.RepositoryException;
import com.cmwa.ecc.business.service.menu.MenuService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SQLUtil;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;
import com.cmwa.ecc.business.utils.SysConstant;


@Service("menuServiceImpl")
public class MenuServiceImpl implements MenuService {
	@Autowired
	private MenuDao menuDao;

	@Override
	public Page<MenuVo> menuListPage(SearchParam param) {

		Employee employee = SessionUtils.getEmployee();
		param.getSp().put("createId", employee.getID());
		List<MenuVo> items = menuDao.menuListPage(param);
		return Page.create(items, param.getStart(), param.getLimit(), param.getTotal());
	}

	@Override
	public void saveMenu(MenuVo menu) throws RepositoryException {
		if ("0".equals(menu.getParentId())) {
			menu.setParentId("0");
			menu.setLevel(0);
		} else {
			MenuVo parent = queryMenuById(menu.getParentId());
			menu.setLevel(parent.getLevel() + 1);
		}
		menuDao.saveMenu(menu);
	}

	public MenuVo queryMenuById(String menuId) {
		return menuDao.queryMenuById(menuId);
	}

	@Override
	public List<MenuVo> queryThreeLevelMenuList() {
		return menuDao.queryThreeLevelMenuList();
	}

	@Override
	public List<MenuVo> queryAllMenu() {
		return menuDao.queryAllMenu();
	}

	@Override
	public Integer queryNextSequence() {
		return menuDao.queryNextSequence();
	}

	@Override
	public List<MenuVo> queryMatchMenuList(String menuNM) {
		return menuDao.queryMatchMenuList(menuNM);
	}

	@Override
	public boolean checkRepeatMenuNM(String menuNM, String menuId) {
		return menuDao.queryRepeatMenuNMCount(menuNM,menuId) > 0;
	}

	@Override
	public List<MenuVo> queryAllButtonMenuByMenuIds(String menuIds) {
		String[] menuArrIds = menuIds.split(",");
		List<String> list = Arrays.asList(menuArrIds);
		String field = "M.PARENTID";
		return menuDao.queryAllButtonMenuByMenuIds(SQLUtil.buildOracleSQLIn(list, 1000, field));
	}

	@Override
	public List<MenuVo> queryMenuListByEmp(String empId) {
		return menuDao.queryMenuListByEmp(empId);
	}

	@Override
	public List<MenuVo> queryMenuListByOrmember(String empId) {
		return menuDao.queryMenuListByOrmember(empId);
	}

	@Override
	public Map<String, MenuVo> queryAllRealMenu() {
		Map<String, MenuVo> allMenuMap = new HashMap<String, MenuVo>();
		List<MenuVo> list = queryAllMenu();
		for (MenuVo menuVo : list) {
			if (SysConstant.MENU_TYPE.equals(menuVo.getType())) {
				allMenuMap.put(menuVo.getMenuId(), menuVo);
			}
		}
		return allMenuMap;
	}

	@Override
	public void updateMenu(MenuVo menu) {
		// 更新级数
		MenuVo parent = queryMenuById(menu.getParentId());
		if (parent != null) {
			menu.setLevel(parent.getLevel() + 1);
		}else{
			menu.setLevel(0);
		}
		menuDao.updateMenu(menu);
	}

	@Override
	public List<MenuVo> queryEmpDefaultSelectedButtonMenu(String empId) {
		return menuDao.queryEmpDefaultSelectedButtonMenu(empId);
	}

	@Override
	public List<MenuVo> queryOrmemberDefaultSelectedButtonMenu(String unionId) {
		String[] unionIds = unionId.split("-");
		return menuDao.queryOrmemberDefaultSelectedButtonMenu(unionIds[0], unionIds[1]);
	}

	@Override
	public boolean hasSubMenu(String parentId) {
		Integer count = menuDao.querySubMenuCount(parentId);
		if (count == null) {
			return false;
		}
		return count > 0;
	}

	@Override
	public void deleteMenuWithStatus(String menuId) {
		menuDao.deleteMenuWithStatus(menuId);
	}

	@Override
	public Page<MenuVo> buttonListPage(SearchParam param) {
		String menuId = (String) param.getSp().get("menuId");
		List<MenuVo> items = queryButtonListByMenuIds(menuId);
		return Page.create(items, param.getStart(), param.getLimit(), items.size());
	}


	public List<MenuVo> queryButtonListByMenuIds(String menuIds) {
		String[] menuArrIds = menuIds.split(",");
		List<String> list = Arrays.asList(menuArrIds);
		String field = "M.PARENTID";
		return menuDao.queryButtonListByMenuIds(SQLUtil.buildOracleSQLIn(list, 1000, field));
	}

	@Override
	public MenuVo queryMenuByCode(String code) {
		return menuDao.queryMenuByCode(code);
	}

	@Override
	public boolean checkRepeatURL(String url, String menuId) {
		if (url != null && !"0".equals(url) && (url.indexOf(".do") != -1 || url.indexOf(".jsp") != -1 || url.indexOf(".xhtml") != -1)) {
			return menuDao.queryRepeatURL(url,menuId) > 0;
		}
		return false;
	}

	@Override
	public MenuVo queryMenuByUrl(String url) {
		return menuDao.queryMenuByUrl(url);
	}
}
