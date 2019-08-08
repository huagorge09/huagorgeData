package com.cmwa.ecc.business.utils;

import java.util.List;

import org.apache.commons.lang.StringUtils;

public class SQLUtil {
	
	/**
	 * 构建IN SQL
	 * @param ids
	 * @param count
	 * @param field
	 * @return
	 */
	public static String buildOracleSQLIn(List<?> ids, int count, String field) {
		count = Math.min(count, 1000);
		int len = ids.size();
		int size = len % count;
		if (size == 0) {
			size = len / count;
		} else {
			size = (len / count) + 1;
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < size; i++) {
			int fromIndex = i * count;
			int toIndex = Math.min(fromIndex + count, len);
			String objId = StringUtils.defaultIfEmpty(StringUtils.join(ids.subList(fromIndex, toIndex), "','"), "");
			if (i != 0) {
				builder.append(" OR ");
			}
			builder.append(field).append(" IN ('").append(objId).append("')");
		}
		return StringUtils.defaultIfEmpty(builder.toString(), field + " IN ('')");
	}

}
