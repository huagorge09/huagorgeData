package com.cmwa.ecc.business.mybatis.annotation;

import java.util.List;
import java.util.Map;

public interface BaseDao<T> {
	
	 T get(long id);
	
	List<T> list(Map<String, Object> query);
	
	int count(Map<String, Object> query);
	
	int create(T model);

	int update(T model);

	int delete(Long id);
	
	int deletes(Long... ids);
}
