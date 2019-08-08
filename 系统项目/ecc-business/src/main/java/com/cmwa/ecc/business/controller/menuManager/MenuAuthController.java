package com.cmwa.ecc.business.controller.menuManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cmwa.ecc.business.commonVo.MenuVo;
import com.cmwa.ecc.business.commonVo.OrganizationVo;
import com.cmwa.ecc.business.commonVo.TreeElem;
import com.cmwa.ecc.business.commonVo.UserInfoVo;
import com.cmwa.ecc.business.service.menu.MenuAuthService;
import com.cmwa.ecc.business.service.menu.MenuService;
import com.cmwa.ecc.business.service.menu.OrganizationService;
import com.cmwa.ecc.business.service.userInfo.UserInfoService;
import com.cmwa.ecc.business.utils.NumberComparator;

@Controller("menuAuthController")
@RequestMapping(value = "/service/menuAuth")
public class MenuAuthController {
	@Resource
	private OrganizationService organizationService;
	@Resource
	private UserInfoService userInfoService;
	@Resource
	private MenuService menuService;
	@Resource
	private MenuAuthService menuAuthService;

	private final String STATION_PREFIX = "ST-";
	private final String EMP_PREFIX = "EMP-";
	private final String SPILT = "-";

	/**
	 * ajax 根据父ID查询子列表
	 * 
	 * @see ST-217cmhkOP-cmfHGJC 岗位（orgId-titleId）
	 * @see EMP-2176006 人员（empId）
	 * @param orgId
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/ajaxOrganizationList.do")
	public void ajaxOrganizationList(@RequestParam("orgId") String orgId, HttpServletResponse response) throws IOException {
		List<TreeElem> elems = new ArrayList<TreeElem>();

		if (!orgId.startsWith(STATION_PREFIX)) {
			// 根据组织的父ID查询子列表，根节点的父节点为空
			List<OrganizationVo> list = organizationService.queryOrganizationListByParentId(orgId);
			for (OrganizationVo vo : list) {
				TreeElem elem = new TreeElem();
				elem.getState().setDisabled(true);
				elem.setId(vo.getOrgId());
				elem.setText(vo.getOrgName());
				elems.add(elem);
			}

			// 根据组织ID查询岗位列表
			if (StringUtils.isNotBlank(orgId)) {
				List<OrganizationVo> list2 = organizationService.queryStationListByOrgId(orgId);
				for (OrganizationVo vo : list2) {
					TreeElem elem = new TreeElem();
					elem.setId(STATION_PREFIX + orgId + SPILT + vo.getTitleId());
					elem.setText(vo.getTitleName());
					elems.add(elem);
				}
			}
		}

		// 根据岗位ID查询成员列表
		if (StringUtils.isNotBlank(orgId) && orgId.startsWith(STATION_PREFIX)) {
			String[] orgIds = orgId.substring(STATION_PREFIX.length()).split(SPILT);
			String orgIdx = orgIds[0];
			String stationId = orgIds[1];
			List<UserInfoVo> userList = userInfoService.queryEmpListByStationId(orgIdx, stationId);
			for (UserInfoVo vo : userList) {
				TreeElem elem = new TreeElem();
				elem.getState().setLoaded(true).setOpened(true);
				elem.setId(EMP_PREFIX + vo.getEmpID());
				elem.setText(vo.getEmpName());
				elems.add(elem);
			}
		}

		PrintWriter out = response.getWriter();
		response.setContentType("application/json;charset=UTF-8");
		out.write(JSONArray.fromObject(elems).toString());
	}

	/**
	 * 查询某个用户的权限菜单
	 * 
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/ajaxMenuList.do")
	public void ajaxMenuList(@RequestParam("authId") String authId, HttpServletResponse response) throws Exception {
		// 查询默认选中的菜单
		Set<String> selectedMenu = null;
		if (checkIsEmpLevel(authId)) {
			selectedMenu = menuAuthService.queryEmpDefaultSelectedMenu(checkResolving(authId));
		} else {
			selectedMenu = menuAuthService.queryOrmemberDefaultSelectedMenu(checkResolving(authId));
		}

		// 查询三级的菜单树
		List<MenuVo> list = menuService.queryThreeLevelMenuList();
		sortTheMenu(list);
		List<TreeElem> elems = new ArrayList<TreeElem>();
		// 构建一棵带有默认选中的菜单树
		for (MenuVo vo : list) {
			TreeElem elem = new TreeElem();
			elem.getState().setLoaded(true).setOpened(true);
			setDefaultSelected(selectedMenu, vo, elem);
			elem.setId(vo.getMenuId());
			elem.setText(vo.getName());
			if (vo.getChilds() != null) {
				sortTheMenu(vo.getChilds());
				// 二级菜单
				for (MenuVo second : vo.getChilds()) {
					TreeElem secElem = new TreeElem();
					secElem.getState().setLoaded(true).setOpened(true);
					setDefaultSelected(selectedMenu, second, secElem);
					secElem.setId(second.getMenuId());
					secElem.setText(second.getName());
					elem.getChildren().add(secElem);
					// 三级菜单
					if (second.getChilds() != null) {
						sortTheMenu(second.getChilds());
						for (MenuVo third : second.getChilds()) {
							TreeElem thirdElem = new TreeElem();
							thirdElem.getState().setLoaded(true).setOpened(true);
							setDefaultSelected(selectedMenu, third, thirdElem);
							thirdElem.setId(third.getMenuId());
							thirdElem.setText(third.getName());
							secElem.getChildren().add(thirdElem);
							// 四级菜单
							if (third.getChilds() != null) {
								sortTheMenu(third.getChilds());
								for (MenuVo third2 : third.getChilds()) {
									TreeElem thirdElem2 = new TreeElem();
									thirdElem2.getState().setLoaded(true).setOpened(true);
									setDefaultSelected(selectedMenu, third2, thirdElem2);
									thirdElem2.setId(third2.getMenuId());
									thirdElem2.setText(third2.getName());
									thirdElem.getChildren().add(thirdElem2);
								}
							}
						}
					}
				}
			}
			elems.add(elem);
		}
		PrintWriter out = response.getWriter();
		response.setContentType("application/json;charset=UTF-8");
		out.write(JSONArray.fromObject(elems).toString());
	}

	/**
	 * 根据sequence排序菜单
	 * 
	 * @param list
	 */
	private void sortTheMenu(List<MenuVo> list) {
		Collections.sort(list, new NumberComparator());
	}

	/**
	 * 设置默认选中
	 * 
	 * @param selectedMenu
	 * @param menuVo
	 * @param elem
	 */
	private void setDefaultSelected(Set<String> selectedMenu, MenuVo menuVo, TreeElem elem) {
		// 不是叶子节点，不设置为选中状态
		if (selectedMenu.contains(menuVo.getMenuId()) && menuVo.getChilds().size() == 0) {
			elem.getState().setSelected(true);
		}
	}

	/**
	 * 操作对象是否为员工还是岗位
	 * 
	 * @param authId
	 * @return
	 * @throws Exception
	 */
	private boolean checkIsEmpLevel(String authId) throws Exception {
		if (authId != null && authId.startsWith(STATION_PREFIX)) {
			return false;
		} else if (authId != null && authId.startsWith(EMP_PREFIX)) {
			return true;
		}
		throw new Exception("不支持该类型进行权限操作");
	}

	/**
	 * 检查并解析操作对象：
	 * 
	 * @see ST-217cmhkOP-cmfHGJC 岗位（orgId-titleId）
	 * @see EMP-2176006 人员（empId）
	 * @param authId
	 * @return
	 * @throws Exception
	 */
	private String checkResolving(String authId) throws Exception {
		if (authId == null) {
			throw new Exception("进行权限操作人员不能为空");
		}
		if (authId.startsWith(STATION_PREFIX)) {
			if (authId.split(SPILT).length != 3) {
				throw new Exception("不支持该类型进行权限操作");
			}
			return authId.substring(STATION_PREFIX.length());
		} else if (authId.startsWith(EMP_PREFIX)) {
			if (authId.split(SPILT).length != 2) {
				throw new Exception("不支持该类型进行权限操作");
			}
			return authId.substring(EMP_PREFIX.length());
		}
		throw new Exception("不支持该类型进行权限操作");
	}

	/**
	 * 菜单权限管理页面
	 * 
	 * @param orgId
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/menuAuthManageView")
	public String menuAuthManageView() throws IOException {
		return "jsp/menu/menuAuthManage";
	}

	/**
	 * 保存按钮进行保存选中的菜单
	 * 
	 * @param authId
	 * @param menuIds
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/ajaxSaveMenuAuth.do")
	public void ajaxSaveMenuAuth(@RequestParam("authId") String authId, @RequestParam("menuIds") String menuIds, HttpServletResponse response) throws Exception {
		if (checkIsEmpLevel(authId)) {
			menuAuthService.saveEmpMenu(checkResolving(authId), menuIds);
		} else {
			menuAuthService.saveOrmemberMenu(checkResolving(authId), menuIds);
		}
		PrintWriter out = response.getWriter();
		response.setContentType("application/json;charset=UTF-8");
		JSONObject object = new JSONObject();
		object.put("result", "success");
		out.write(object.toString());
	}

	/**
	 * 复制岗位权限给用户
	 * 
	 * @param empId
	 * @param stId
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/copyOrmemberMenuForEmp.do")
	public void copyOrmemberMenuForEmp(@RequestParam("empId") String empId, @RequestParam("stId") String stId,@RequestParam("merge") Boolean merge, HttpServletResponse response) throws Exception {
		String ormemberMenu = menuAuthService.queryOrmemberDefaultSelectedMergeEmpMenuWithComma(checkResolving(stId),checkResolving(empId),merge);
		menuAuthService.saveEmpMenu(checkResolving(empId), ormemberMenu);
		PrintWriter out = response.getWriter();
		response.setContentType("application/json;charset=UTF-8");
		JSONObject object = new JSONObject();
		object.put("result", "success");
		out.write(object.toString());
	}

	/**
	 * 复制一个岗位权限菜单给另一个岗位
	 * 
	 * @param empId
	 * @param stId
	 * @param response
	 * @param merge  是否粘贴覆盖权限菜单
	 * @throws Exception
	 */
	@RequestMapping("/copyOrmemberMenuToNext.do")
	public void copyOrmemberMenuToNext(@RequestParam("srcId") String srcId, @RequestParam("destId") String destId,@RequestParam("merge") Boolean merge, HttpServletResponse response) throws Exception {
		String ormemberMenu = menuAuthService.queryOrmemberDefaultSelectedMenuWithComma(checkResolving(srcId),checkResolving(destId),merge);
		menuAuthService.saveOrmemberMenu(checkResolving(destId), ormemberMenu);
		PrintWriter out = response.getWriter();
		response.setContentType("application/json;charset=UTF-8");
		JSONObject object = new JSONObject();
		object.put("result", "success");
		out.write(object.toString());
	}

	/**
	 * 清空权限菜单
	 * 
	 * @param empId
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/clearMenu.do")
	public void clearMenu(@RequestParam("authId") String authId, HttpServletResponse response) throws Exception {
		if (checkIsEmpLevel(authId)) {
			menuAuthService.saveEmpMenu(checkResolving(authId), "");
		} else {
			menuAuthService.saveOrmemberMenu(checkResolving(authId), "");
		}
		PrintWriter out = response.getWriter();
		response.setContentType("application/json;charset=UTF-8");
		JSONObject object = new JSONObject();
		object.put("result", "success");
		out.write(object.toString());
	}

	/**
	 * 展示按钮菜单列表数据
	 * 
	 * @param authId
	 * @param menuIds
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/ajaxDisplayButtonMenu.do")
	public void ajaxDisplayButtonMenu(@RequestParam("authId") String authId, @RequestParam("menuIds") String menuIds, HttpServletResponse response) throws Exception {
		try {
			List<MenuVo> allMenuVos = menuService.queryAllButtonMenuByMenuIds(menuIds);
			List<MenuVo> selectedMenuVos = null;
			if (checkIsEmpLevel(authId)) {
				selectedMenuVos = menuService.queryEmpDefaultSelectedButtonMenu(checkResolving(authId));
			} else {
				selectedMenuVos = menuService.queryOrmemberDefaultSelectedButtonMenu(checkResolving(authId));
			}
			Set<String> selectedButton = asSet(selectedMenuVos);
			List<TreeElem> elems = new ArrayList<TreeElem>();
			// 构建一棵带有默认选中的菜单树
			for (MenuVo vo : allMenuVos) {
				TreeElem elem = new TreeElem();
				elem.getState().setLoaded(true).setOpened(true);
				setDefaultSelected(selectedButton, vo, elem);
				elem.setId(vo.getMenuId());
				elem.setText(vo.getName());
				if (vo.getChilds() != null) {
					sortTheMenu(vo.getChilds());
					// 二级菜单
					for (MenuVo second : vo.getChilds()) {
						TreeElem secElem = new TreeElem();
						secElem.getState().setLoaded(true).setOpened(true);
						setDefaultSelected(selectedButton, second, secElem);
						secElem.setId(second.getMenuId());
						secElem.setText(second.getName()+"("+second.getDesc()+")");
						elem.getChildren().add(secElem);

					}
				}
				elems.add(elem);
			}

			PrintWriter out = response.getWriter();
			response.setContentType("application/json;charset=UTF-8");
			out.write(JSONArray.fromObject(elems).toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Set<String> asSet(List<MenuVo> list) {
		Set<String> set = new HashSet<String>();
		if (list != null) {
			for (MenuVo m : list) {
				set.add(m.getMenuId());
			}
		}
		return set;
	}



}
