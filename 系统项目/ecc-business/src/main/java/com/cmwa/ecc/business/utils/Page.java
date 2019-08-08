package com.cmwa.ecc.business.utils;

import java.util.Collections;
import java.util.List;


public class Page<T> {

	/**
	 * 默认第一页页码
	 */
	public static final int FIRST_PAGE = 1;// 第一页，从1开始
	/**
	 * 默认每页记录数
	 */
	public static final int PAGE_SIZE = 50;// 默认一页记录数

	/**
	 * 从第一条记录开始查询
	 */
	public static final int START = 0;

	/**
	 * 查询所有记录
	 */
	public static final int ALL = -1;

	private List<T> items = Collections.emptyList();// 当前页记录
	private int pageNo = FIRST_PAGE;// 当前页码
	private int pageSize = PAGE_SIZE;// 每页记录数
	private int total = 0;// 记录总条数
	private boolean success = true;
	private String errorMsg;
	
	@SuppressWarnings("rawtypes")
	public static Page error(){
		Page page = new Page();
		page.setSuccess(false);
		return page;
	}
	
	@SuppressWarnings("rawtypes")
	public static Page error(String errorMsg){
		Page page = new Page();
		page.setSuccess(false);
		page.setErrorMsg(errorMsg);
		return page;
	}
	
	public static <T> Page<T> empty() {
		Page<T> page = new Page<T>();
		return page;
	}

	public static <T> Page<T> create(List<T> items,int total){
		Page<T> page = new Page<T>();
		page.setItems(items);
		page.setTotal(total);
		return page;
	}
	
	public static <T> Page<T> create(List<T> items, int start, int limit, int total) {
		Page<T> page = new Page<T>();
		page.setItems(items);
		page.setPageNo(FIRST_PAGE + start / limit);
		page.setPageSize(limit);
		page.setTotal(total);
		return page;
	}
	
	public static <T> Page<T> create(List<T> items, int start, int limit, int total,boolean isPage) {
		Page<T> page = new Page<T>();
		page.setItems(items);
		page.setPageNo(FIRST_PAGE + start / limit);
		page.setPageSize(limit);
		page.setTotal(total);
		return page;
	}


	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * 获取共有多少页
	 * 
	 * @return
	 */
	public int getPageCount() {
		int pageCount = this.total / this.pageSize;
		if (this.total % this.pageSize > 0) {
			pageCount++;
		}
		return pageCount;
	}

	/**
	 * 获取第一页页码
	 * 
	 * @return
	 */
	public int getFirstPageNo() {
		return FIRST_PAGE;
	}

	/**
	 * 获取最后一页页码
	 * 
	 * @return
	 */
	public int getLastPageNo() {
		return this.getPageCount() + FIRST_PAGE - 1;
	}

	/**
	 * 获取当前页记录条数
	 * 
	 * @return
	 */
	public int getItemSize() {
		return this.items.size();
	}
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("pageNo:").append(getPageNo()).append(",");
		builder.append("pageSize:").append(getPageSize()).append(",");
		builder.append("totalCount:").append(getTotal()).append(",");
		builder.append("itemSize:").append(getItemSize()).append(",");
		builder.append("items:").append(getItems());
		return builder.toString();
	}

}
