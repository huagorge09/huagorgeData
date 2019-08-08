package com.cmwa.ecc.business.commonVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.type.Alias;

@Alias("menuVo")
public class MenuVo {
	/**
	 * 菜单ID
	 */
	private String menuId;
	/**
	 * 菜单级别
	 */
	private Integer level = 0;
	/**
	 * 菜单名称，展示在页面上的名字
	 */
	private String name;
	/**
	 * 序列号
	 */
	private Integer sequence;
	/**
	 * 菜单链接
	 */
	private String url;
	/**
	 * 父菜单ID
	 */
	private String parentId;
	/**
	 * 菜单描述
	 */
	private String desc;
	/**
	 * 菜单代号
	 */
	private String code;
	/**
	 * 图标样式代码
	 */
	private String icon;
	/**
	 * 0:普通菜单，1：按钮菜单
	 */
	private String type;
	/**
	 * 状态 1:生效，0：已删除
	 */
	private String status;

	private String createId;
	private String createTime;

	/**
	 * 菜单所属系统
	 */
	private String systemClassify;
	
	// TEMP

	/**
	 * 子节点的菜单列表
	 */
	private List<MenuVo> childs = new ArrayList<MenuVo>();
	/**
	 * 用于判断按钮菜单是否选中
	 */
	private boolean checked;

	/**
	 * 判断是否有子菜单
	 * 
	 * @param map
	 * @return
	 */
	public boolean hasSubMenu(Map<Integer, List<MenuVo>> map) {
		if (map.containsKey(this.getLevel() + 1)) {
			return hasSubMenu(map.get(this.getLevel() + 1));
		}
		return false;
	}

	/**
	 * 判断是否有子菜单
	 * 
	 * @param menus
	 * @return
	 */
	public boolean hasSubMenu(List<MenuVo> menus) {
		for (MenuVo m : menus) {
			if (m.getParentId() != null && m.getParentId().equals(this.getMenuId())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 不存在则添加
	 * 
	 * @param menu
	 * @return
	 */
	public boolean addIfAbsent(MenuVo menu) {
		if (menu == null) {
			return false;
		}
		for (MenuVo m : getChilds()) {
			if (m != null && m.getMenuId().equals(menu.getMenuId())) {
				return false;
			}
		}
		return getChilds().add(menu);
	}

	/**
	 * 显示转换过带图标的名称
	 * 
	 * @return
	 */
	public String getNameConvert() {
		if (icon != null) {
			return "<i class=\"" + icon + "\"></i>&nbsp;" + name;
		} else {
			return name;
		}
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<MenuVo> getChilds() {
		return childs;
	}

	public void setChilds(List<MenuVo> childs) {
		this.childs = childs;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getSystemClassify() {
		return systemClassify;
	}

	public void setSystemClassify(String systemClassify) {
		this.systemClassify = systemClassify;
	}

	@Override
	public String toString() {
		return "MenuVo [menuId=" + menuId + ", level=" + level + ", name="
				+ name + ", sequence=" + sequence + ", url=" + url
				+ ", parentId=" + parentId + ", desc=" + desc + ", code="
				+ code + ", icon=" + icon + ", type=" + type + ", status="
				+ status + ", createId=" + createId + ", createTime="
				+ createTime + ", childs=" + childs + ", checked=" + checked
				+ "]";
	}
	
}
