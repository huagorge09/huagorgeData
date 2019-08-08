package com.cmwa.ecc.business.mybatis.annotation;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public abstract class BaseDaoImpl<T> extends SqlSessionDaoSupport implements BaseDao<T> {

	@Autowired
	public void setSqlSessionFactory0(SqlSessionFactory sqlSessionFactory) {
		super.setSqlSessionFactory(sqlSessionFactory);
	}

	/**
	 * 获取泛型对应的class
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Class<T> getModelClass() {
		ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
		return (Class<T>) type.getActualTypeArguments()[0];
	}
	
	protected Class getDaoInterface() {
		return this.getClass().getInterfaces()[0];
	}
 
	@Override
	public T get(long id) {
		return (T)getSqlSession().selectOne(getDaoInterface().getName()+".get", id);
	}
	@Override
	public List<T> list(Map<String, Object> query) {
		return getSqlSession().selectList(getDaoInterface().getName()+".list", query);
	}
	@Override
	public int count(Map<String, Object> query) {
		return (Integer)getSqlSession().selectOne(getDaoInterface().getName()+".count", query);
	}

	@Override
	public int create(T model) {
		return getSqlSession().insert(getDaoInterface().getName()+".create", model);
	}

	@Override
	public int update(T model) {
		return getSqlSession().insert(getDaoInterface().getName()+".update", model);
	}

	@Override
	public int delete(Long id) {
		return getSqlSession().delete(getDaoInterface().getName()+".delete", id);
	}
	
	@Override
	public int deletes(Long... ids) {
		return getSqlSession().delete(getDaoInterface().getName()+".deletes", ids);
	}
}
