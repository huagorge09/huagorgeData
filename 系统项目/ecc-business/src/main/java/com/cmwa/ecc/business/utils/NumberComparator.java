package com.cmwa.ecc.business.utils;

import java.util.Comparator;

import com.cmwa.ecc.business.commonVo.MenuVo;

/**
 * @ClassName: NumberComparator
 * @Description: 数值比较器
 * 
 */
public class NumberComparator implements Comparator<Object> {

	public NumberComparator() {
	}

	public int compare(Object obj1, Object obj2) {
		MenuVo m1 = (MenuVo) obj1;
		MenuVo m2 = (MenuVo) obj2;
		Integer s1 = m1.getSequence() == null ? 0 : m1.getSequence();
		Integer s2 = m2.getSequence() == null ? 0 : m2.getSequence();
		return s1 - s2;
	}

}
