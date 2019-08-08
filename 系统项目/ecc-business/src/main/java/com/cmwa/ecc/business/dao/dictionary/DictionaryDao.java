package com.cmwa.ecc.business.dao.dictionary;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ecc.business.commonVo.DictionaryVo;
import com.cmwa.ecc.business.exception.ValidateFailedException;
import com.cmwa.ecc.business.mybatis.annotation.BaseDao;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

@MybatisDao
public interface DictionaryDao extends BaseDao<DictionaryVo> {
	
	public List<DictionaryVo> listDictionary(DictionaryVo dictionaryVo);
	
	public List<DictionaryVo> dictionarys(DictionaryVo dictionaryVo);

	/**
	 * 分页查询产品费用信息
	 * @param param
	 * @author ex-huangch 2016.5.3
	 */
	List<DictionaryVo> dictionaryListPage(SearchParam param);
	
	/**
	 * 根据字典ID查询系统数据字典信息
	 * @param dctId
	 * @author ex-huangch 2016.4.27
	 */
	DictionaryVo queryDictionaryByDctId(@Param("dctId")String dctId) throws ValidateFailedException;
	
 	/**
	 * 添加系统数据字典信息
	 * @author ex-huangch 2016.5.4
	 * @param dictionaryVo
	 */
	void addDictionary(DictionaryVo dictionaryVo) throws ValidateFailedException;
	
	/**
 	 * 删除系统数据字典信息
 	 * @author ex-huangch 2016.5.4
 	 * @param dctId
 	 * @return
 	 */
	void deleteDictionary(DictionaryVo dictionaryVo);
	
	/**
	 * 更新系统数据字典信息
	 * @author ex-huangch 2016.4.27
	 * @param dictionaryVo
	 */
	public void updateDictionary(DictionaryVo dictionaryVo);
	
	/**
	 * 校验系统数据字典信息是否已存在
	 * @author ex-huangch 2016.4.27
	 * @param dictionaryVo
	 */
	public int checkDictionary(DictionaryVo dictionaryVo);
	
	/**
	 * 通过字典ID查询原有字典值
	 * @author ex-huangch 2016.4.27
	 * @param dictionaryVo
	 */
	public String queryDctValueByDctId(DictionaryVo dictionaryVo);

	
	
}
