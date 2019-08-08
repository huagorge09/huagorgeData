package com.cmwa.ecc.business.commonVo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 树元素的初始化状态
 * 
 * @author 
 */
public class TreeElemState {
	/**
	 * 初始化加载状态，否：一般用于ajax
	 */
	private Boolean loaded;
	/**
	 * 默认是否展开
	 */
	private Boolean opened;
	/**
	 * 默认是否选中
	 */
	private Boolean selected;
	/**
	 * 默认是否可用
	 */
	private Boolean disabled;

	public TreeElemState() {
		this.loaded = false;
		this.opened = false;
		this.selected = false;
		this.disabled = false;
	}
	
	public Boolean getLoaded() {
		return loaded;
	}

	public TreeElemState setLoaded(Boolean loaded) {
		this.loaded = loaded;
		return this;
	}

	public Boolean getOpened() {
		return opened;
	}

	public TreeElemState setOpened(Boolean opened) {
		this.opened = opened;
		return this;
	}

	public Boolean getSelected() {
		return selected;
	}

	public TreeElemState setSelected(Boolean selected) {
		this.selected = selected;
		return this;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public TreeElemState setDisabled(Boolean disabled) {
		this.disabled = disabled;
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	
}
