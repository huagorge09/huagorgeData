package com.cmwa.ecc.business.controller.menuManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmwa.ecc.business.commonVo.MenuVo;
import com.cmwa.ecc.business.controller.BaseController;
import com.cmwa.ecc.business.exception.RepositoryException;
import com.cmwa.ecc.business.service.menu.MenuService;
import com.cmwa.ecc.business.utils.ClientManager;
import com.cmwa.ecc.business.utils.ContextHolderUtil;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;
import com.cmwa.ecc.business.utils.SysConstant;
import com.cmwa.ecc.business.utils.cached.MenuCached;


@Controller("menuController")
@RequestMapping(value="/service/menu")
public class MenuController extends BaseController{

	private static Logger logger = LoggerFactory.getLogger(MenuController.class);
	
	@Resource
	private MenuService menuService;
	@Resource
	private MenuCached menuCached;
	
	/**
	 * 跳转至菜单管理页面
	 * @return
	 */
	@RequestMapping(value = "/goMenuPage.xhtml")
	public String goMenuPage() {
		return "jsp/menu/menuList";
	};
	
	/**
	 * 列表页面数据
	 * @param sp
	 * @return
	 */
	@RequestMapping("/menuListPage.do")
	@ResponseBody
	public Page<MenuVo> menuListPage(SearchParam sp) {
		Page<MenuVo> list = menuService.menuListPage(sp);
		return list;
	}

	/**
	 * 菜单按钮列表页面
	 * @param sp
	 * @return
	 */
	@RequestMapping("/buttonListPage")
	@ResponseBody
	public Page<MenuVo> buttonListPage(SearchParam sp) {
		Page<MenuVo> list = menuService.buttonListPage(sp);
		return list;
	}
	/**
	 * 新增菜单页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/menuAddView")
	public String menuAddView(ModelMap model) {
		model.put("currentEmpId", SessionUtils.getEmployee().getID());
		Integer sequence = menuService.queryNextSequence();
		List<MenuVo> rootMenu = menuService.queryThreeLevelMenuList();
		model.put("rootMenu", rootMenu);
		model.put("sequence", sequence);
		return "jsp/menu/menuAdd";
	}

	/**
	 * 修改菜单页面
	 * @param menuId
	 * @param model
	 * @return
	 */
	@RequestMapping("/menuUpdateView")
	public String menuUpdateView(@RequestParam("menuId") String menuId, ModelMap model) {
		MenuVo menuVo = menuService.queryMenuById(menuId);
		List<MenuVo> rootMenu = menuService.queryThreeLevelMenuList();
		model.addAttribute("rootMenu", rootMenu);
		model.addAttribute("menuVo", menuVo);
		return "jsp/menu/menuUpdate";
	}
	
	/**
	 * 菜单详情页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/menuDetailView")
	public String menuDetailView(@RequestParam("menuId") String menuId,ModelMap model) {
		model.put("currentEmpId", SessionUtils.getEmployee().getID());
		MenuVo menuVo = menuService.queryMenuById(menuId);
		MenuVo parentVo = menuService.queryMenuById(menuVo.getParentId());
		List<MenuVo> buttonList=menuService.queryAllButtonMenuByMenuIds(menuId);
		model.addAttribute("parentVo", parentVo);
		model.addAttribute("menuVo", menuVo);
		model.addAttribute("buttonList", buttonList);
		return "jsp/menu/menuDetail";
	}


	/**
	 * 保存菜单数据
	 * @param menu
	 * @return
	 * @throws RepositoryException
	 */
	@RequestMapping("/saveMenu")
	public String saveMenu(MenuVo menu) throws RepositoryException {
		menuService.saveMenu(menu);
		menuCached.loadAllMenuCached();
		return redirectSuccess();
	}
	
	/**
	 * 更新保存菜单数据
	 * @param menu
	 * @return
	 * @throws RepositoryException
	 */
	@RequestMapping("/updateMenu")
	public String updateMenu(MenuVo menu) throws RepositoryException {
		menuService.updateMenu(menu);
		menuCached.loadAllMenuCached();
		return redirectSuccess();
	}

	/**
	 * 复制菜单
	 * 
	 * @param menuId
	 * @param response
	 */
	@RequestMapping("/copyMenu")
	public void copyMenu(@RequestParam("menuId") String menuId, HttpServletResponse response) {
		MenuVo menuVo = menuService.queryMenuById(menuId);
		try {
			PrintWriter out = response.getWriter();
			response.setContentType("application/json;charset=UTF-8");
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("result", menuVo);
			jsonObj.put("succeed", true);
			out.write(jsonObj.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 检查是否存在重复的菜单名称
	 * @param menuNM
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/checkRepeatMenuNM")
	public void checkRepeatMenuNM(@RequestParam("menuNM") String menuNM,@RequestParam(value="menuId",required=false) String menuId,  HttpServletResponse response) throws Exception {
		boolean isRepeat = menuService.checkRepeatMenuNM(menuNM,menuId);
		PrintWriter out = response.getWriter();
		response.setContentType("application/json;charset=UTF-8");
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("result", isRepeat);
		out.write(jsonObj.toString());
	
	}
	/**
	 * 检查菜单URL是否存在
	 * @param url
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/checkRepeatURL")
	public void checkRepeatURL(@RequestParam("url") String url,@RequestParam(value="menuId",required=false) String menuId, HttpServletResponse response) throws Exception {
		boolean isRepeat = menuService.checkRepeatURL(url,menuId);
		PrintWriter out = response.getWriter();
		response.setContentType("application/json;charset=UTF-8");
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("result", isRepeat);
		out.write(jsonObj.toString());
	
	}
	
	/**
	 * 查询是否有子菜单
	 * @param parentId
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/hasSubMenu")
	public void hasSubMenu(@RequestParam("parentId") String parentId, HttpServletResponse response) throws Exception {
		boolean subCount = menuService.hasSubMenu(parentId);
		PrintWriter out = response.getWriter();
		response.setContentType("application/json;charset=UTF-8");
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("result", subCount);
		out.write(jsonObj.toString());
	
	}
	
	/**
	 * 没有子菜单，则可以删除本菜单
	 * @param menuId
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/deleteMenu")
	public void deleteMenu(@RequestParam("menuId") String menuId, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		response.setContentType("application/json;charset=UTF-8");
		JSONObject object = new JSONObject();
		boolean subCount = menuService.hasSubMenu(menuId);
		if (subCount) {
			object.put("result", "reject");
		}else {
			menuService.deleteMenuWithStatus(menuId);
			object.put("result", "success");
		}
		out.write(object.toString());
	}

	/**
	 * 更新菜单数据缓存
	 * @param model
	 * @return
	 */
	@RequestMapping("/refreshMenuCache")
	public String refreshMenuCache(ModelMap model) {
		ClientManager.getInstance().clearAllClientData();
		ContextHolderUtil.getSession().removeAttribute(SysConstant.BUSIN_MENU_CACHE);
		menuCached.loadAllMenuCached();
		return redirectSuccess();
	}
}
