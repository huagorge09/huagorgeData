package com.cmwa.ecc.business.commonVo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * jstree 数据单元，树元素
 * 
 * @author pangtf
 */
public class TreeElem {

	private String id;
	private String text;
	private String icon;
	private TreeElemState state;
	private List<TreeElem> children;

	public TreeElem() {
		state = new TreeElemState();
		children = new ArrayList<TreeElem>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public TreeElemState getState() {
		return state;
	}

	public void setState(TreeElemState state) {
		this.state = state;
	}

	public List<TreeElem> getChildren() {
		return children;
	}

	public void setChildren(List<TreeElem> children) {
		this.children = children;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	
}
