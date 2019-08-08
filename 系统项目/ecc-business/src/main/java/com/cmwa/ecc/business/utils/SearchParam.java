package com.cmwa.ecc.business.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.type.Alias;

@Alias("sparam")
public class SearchParam {

	private int start = 0;

	private int limit = 20;

	private int total = 0;
	
	private int pageNo = 1;
	
	private int end;

	
	public void setEnd(int end) {
		this.end = end;
	}

	public int getEnd() {
		return start + limit;
	}
	
	/**
	 * 在拦截器中是否自动计算count值
	 */
	public boolean isAutoTotal = true;
	
	private Map<String, Object> sp = new HashMap<String, Object>();

	public int getStart() {
		start = (pageNo - 1) * limit;
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public Map<String, Object> getSp() {
		return sp;
	}

	public void setSp(Map<String, Object> sp) {
		this.sp = sp;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	@Override
	public String toString() {
		return "SearchParam [start=" + start + ", limit=" + limit + ", total="
				+ total + ", pageNo=" + pageNo + ", sp=" + sp + "]";
	}

	/**
	 * 在拦截器中是否自动计算count值
	 * @return
	 */
	public boolean isAutoTotal() {
		return isAutoTotal;
	}

	public void setAutoTotal(boolean isAutoTotal) {
		this.isAutoTotal = isAutoTotal;
	}
	
}
