package com.cmwa.ecc.business.entity.mail;

/**
 * 附件
 * 
 * @author ex-luoxy@cmfchina.com
 */
public class SimpleAttachEntry {
	
	/**
	 * 
	 */
	public SimpleAttachEntry() {
		super();
	}
	
	
	/**
	 * @param name
	 * @param path
	 */
	public SimpleAttachEntry(String name, String path) {
		super();
		this.name = name;
		this.path = path;
	}
	
	/**
	 * 附件名称
	 */
	private String	name;
	
	/**
	 * 附件路径
	 */
	private String	path;
	
	
	public String getName() {
		return name;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	public String getPath() {
		return path;
	}
	
	
	public void setPath(String path) {
		this.path = path;
	}
	
	
	@Override
	public String toString() {
		return "SimpleAttachEntry [name=" + name + ", path=" + path + ", getName()=" + getName() + ", getPath()=" + getPath() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	
}
