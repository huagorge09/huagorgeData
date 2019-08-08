package com.cmwa.ecc.business.utils;

import java.util.ArrayList;
import java.util.List;


/**
 * 分页函数
 *
 */
public class PageUtil<T> {

	/*public List<T> getPageList(SearchParam sp,List<T> list){
		int number = sp.getPageNo();
		int pageSize = sp.getLimit();
		int firstNum = sp.getStart();
		List<T> pageList=Collections.emptyList();
		for (int i = 0; i <(number*pageSize); i++) {
			if(i>(number-firstNum)*pageSize){
				pageList.add(list.get(i));
			}
		}
		return pageList;
	}*/
	
	public List<T> getPageList(SearchParam sp,List<T> list){
		int number = sp.getPageNo();
		int pageSize = sp.getLimit();
		int firstNum = sp.getStart();
//		List<T> pageList=Collections.emptyList();
		List<T> pageList = new ArrayList<T>();
		for (int i = 0; i < list.size(); i++) {
			if(i >= firstNum ){
				pageList.add(list.get(i));
			}
			if(pageList.size() >= (number * pageSize)){
				break;
			}
		}
		return pageList;
	}
}
