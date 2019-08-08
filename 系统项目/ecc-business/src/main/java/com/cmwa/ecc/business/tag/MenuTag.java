package com.cmwa.ecc.business.tag;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.cmwa.ecc.business.commonVo.MenuVo;
import com.cmwa.ecc.business.utils.SpringApplicationContextUtil;
import com.cmwa.ecc.business.utils.SysConstant;
import com.cmwa.ecc.business.utils.VelocityEngineTool;

/**
 * 类描述：菜单标签
 */
public class MenuTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	protected Map<Integer, List<MenuVo>> menuMap;// 菜单Map

	public int doStartTag() throws JspTagException {
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspTagException {
		try {
			JspWriter out = this.pageContext.getOut();
			String menu = (String) this.pageContext.getSession().getAttribute(SysConstant.BUSIN_MENU_CACHE);
			HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
			String context = request.getContextPath();
			context = context.equals("/")? context : context + "/";
			if (menu != null) {
				out.print(menu);
			} else {
				menu = end(context);
				this.pageContext.getSession().setAttribute(SysConstant.BUSIN_MENU_CACHE, menu);
				out.print(menu);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public String end(String context) {
		try {
			VelocityEngine velocityEngine = SpringApplicationContextUtil.getBean(VelocityEngineTool.class).getVelocityEngine();
			Template t = velocityEngine.getTemplate("menu/template/INDEX_MENU.vm", "UTF-8");
			// 设置初始化数据
			VelocityContext velocityContext = new VelocityContext();
			velocityContext.put("menuList", buildMenuTree(context));
			// 设置输出
			StringWriter writer = new StringWriter();
			// 将环境数据转化输出
			t.merge(velocityContext, writer);
			return writer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 构建左边菜单树
	 * @return
	 */
	public List<MenuVo> buildMenuTree(String context) {
		List<MenuVo> menus = new ArrayList<MenuVo>();
		if (menuMap == null) {
			return menus;
		}
		int level = 1;
		List<MenuVo> list = getNotNullList(level);
		for (MenuVo rootMenu : list) {
			menus.add(rootMenu);
			recuNextLevelMenu(rootMenu,context);
		}
		return menus;
	}

	/**
	 * 递归构建菜单树
	 * 
	 * @param currentMenu
	 */
	public void recuNextLevelMenu(MenuVo currentMenu,String context) {
		if (currentMenu.hasSubMenu(menuMap)) {
			List<MenuVo> list2 = getNotNullList(currentMenu.getLevel() + 1);
			for (MenuVo nextMenu : list2) {
				if (currentMenu.getMenuId().equals(nextMenu.getParentId())) {
					nextMenu.setUrl(context + nextMenu.getUrl());
					currentMenu.addIfAbsent(nextMenu);
					recuNextLevelMenu(nextMenu,context);
				}
			}
		}
	}

	/**
	 * 得到不为空的list
	 * 
	 * @param level
	 * @return
	 */
	private List<MenuVo> getNotNullList(int level) {
		if (menuMap.get(level) == null) {
			return new ArrayList<MenuVo>(0);
		}
		return menuMap.get(level);
	}

	public void setMenuMap(Map<Integer, List<MenuVo>> menuMap) {
		this.menuMap = menuMap;
	}

}
