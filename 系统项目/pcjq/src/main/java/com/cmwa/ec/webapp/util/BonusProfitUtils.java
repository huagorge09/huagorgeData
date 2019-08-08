package com.cmwa.ec.webapp.util;

import java.util.List;

import com.cmwa.ec.query.facade.dto.fund.BonusProfitDto;
import com.cmwa.ec.query.facade.dto.fund.ProductEstimate;

public class BonusProfitUtils {

	/**
	 * 计算当期总分配金额
	 * @param i
	 * @param list
	 * @return
	 * 			String
	 * @author maj
	 */
	public static String eachAdd(int i,List<BonusProfitDto> list){
		String returnStr = "0";
		if(list.size() == 1){
			returnStr = Float.parseFloat(list.get(0).getfRealbalance()) + "";
		}else{
			for (int j = i; j < list.size(); j++) {
				returnStr = (Float.parseFloat(list.get(j).getfRealbalance()) + Float.parseFloat(returnStr)) + "";
			}
		}
		return returnStr;
	}
	
	/**
	 * 计算净值浮动率
	 * @param i
	 * @param list
	 * @return
	 * 			String
	 * @author maj
	 */
	public static String addAndDiv(int i,List<ProductEstimate> list){
		String returnStr = "0";
		if(list == null){
			returnStr = "0";
		}else{
			if(i == list.size()-1){
				returnStr = "0";
			}else{
				returnStr = ((Float.parseFloat(list.get(i).getNetValue()) - Float.parseFloat(list.get(i+1).getNetValue())) / Float.parseFloat(list.get(i+1).getNetValue())) +"";
			}
		}
		
		return returnStr;
	}
	
}
