package com.cmwa.ecc.business.service.dictionary;

import java.util.List;

import com.cmwa.ecc.business.commonVo.DictionaryVo;
import com.cmwa.ecc.business.exception.RepositoryException;
import com.cmwa.ecc.business.exception.ValidateFailedException;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

public interface DictionaryService {
	
	/**
	 * 更具条件查询数据字典
	 * @param dictionaryVo
	 * @return
	 */
	List<DictionaryVo> listDictionary(DictionaryVo dictionaryVo);
	
	
	List<DictionaryVo> dictionarys(DictionaryVo dictionaryVo);
	
	/**
	 * 分页查询数据字典信息
	 * @author ex-huangch  2016.5.4
	 * @param  
	 * @return
	 */
	Page<DictionaryVo> dictionaryListPage(SearchParam param);
	
	/**
	 * 新增数据字典信息
	 * @param DictionaryVo
	 * @throws RepositoryException
	 * @author ex-huangch   2016.5.4
	 */
	void saveDictionary(DictionaryVo dictionaryVo) throws ValidateFailedException;
	
	/**
	 * 根据字典ID查询数据字典信息
	 * @param dctId
	 * @author ex-huangch 2016.5.4
	 */
	public DictionaryVo queryDictionaryByDctId(String dctId) throws ValidateFailedException;
	
	/**
 	 * 删除数据字典
 	 * @author ex-huangch 2016.5.4
 	 * @param dctId
 	 * @return
 	 */
	public void deleteDictionary(String dctId);
	
	/**
	 * 更新产品费用类型
	 * @param dictionaryVo
	 * @author ex-huangch   2016.4.27
	 */
	public void updateDictionary(DictionaryVo dictionaryVo) throws ValidateFailedException;
	
}
