package com.cmwa.ecc.business.service.impl.dictionary;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.commonVo.DictionaryVo;
import com.cmwa.ecc.business.dao.dictionary.DictionaryDao;
import com.cmwa.ecc.business.exception.ValidateFailedException;
import com.cmwa.ecc.business.service.dictionary.DictionaryService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;
import com.cmwa.ecc.business.utils.WaConstants;

@Service
public class DictionaryServiceImpl implements DictionaryService {

	@Resource
	private DictionaryDao dictionaryDao;
	
	@Override
	public List<DictionaryVo> listDictionary(DictionaryVo dictionaryVo) {

		return dictionaryDao.listDictionary(dictionaryVo);
	}

	@Override
	public List<DictionaryVo> dictionarys(DictionaryVo dictionaryVo) {
		// TODO Auto-generated method stub
		return dictionaryDao.dictionarys(dictionaryVo);
	}
	
	/**
	 * 分页查询产品费用类型信息
	 * @author ex-huangch 2016.4.26
	 */
	@Override
	public Page<DictionaryVo> dictionaryListPage(SearchParam param) {
		List<DictionaryVo> items = dictionaryDao.dictionaryListPage(param);
		return Page.create(items, param.getStart(), param.getLimit(), param.getTotal());
	}

	/**
	 * 新增系统数据字典
	 * @param dictionaryVo
	 * @author ex-huangch 2016.4.27
	 */
	@Override
	public void saveDictionary(DictionaryVo dictionaryVo) throws ValidateFailedException {
		// 用于校验停用状态的数据字典
		dictionaryVo.setStat(WaConstants.DCTTYPE_D);
		int count = dictionaryDao.checkDictionary(dictionaryVo);
		if (count > 0) {
			throw new ValidateFailedException("字典值["+ dictionaryVo.getDctValue() + "]已被使用");
		} else {
			// 获取新增新增数据字典初始状态
			dictionaryVo.setStat(WaConstants.DCTTYPE_C);
			// 获取登陆ID
			dictionaryVo.setCreateId(SessionUtils.getEmployee().getID());
			dictionaryDao.addDictionary(dictionaryVo);
		}
	}

	/**
	 * 根据字典ID查询系统数据字典信息
	 * @param dctId
	 * @author ex-huangch 2016.4.27
	 */
	 @Override
	 public DictionaryVo queryDictionaryByDctId(String dctId) throws ValidateFailedException {
		 if(null == dctId){
			 throw new ValidateFailedException("字典ID为空!");
		 }
		  return dictionaryDao.queryDictionaryByDctId(dctId); 
	 }

	 /**
 	 * 删除系统数据字典信息
 	 * @author ex-huangch 2016.5.4
 	 * @param dctId
 	 */
	 @Override 
	 public void deleteDictionary(String dctId) { 
         DictionaryVo dictionaryVo = new DictionaryVo(); 
         dictionaryVo.setDctId(dctId);
         dictionaryVo.setStat(WaConstants.DCTTYPE_D);//修改 为停用状态
         //dictionaryVo.setEmpId(SessionUtils.getEmployee().getID());
         dictionaryDao.deleteDictionary(dictionaryVo); 
	 }
	 
	 /**
	 * 修改产品费用信息
	 * @author ex-huangch 2016.4.27
	 */
	 @Override 
	 public void updateDictionary(DictionaryVo dictionaryVo) throws ValidateFailedException { 
		 System.out.println(dictionaryVo.getDctId());
		 String oldDctValue = dictionaryDao.queryDctValueByDctId(dictionaryVo);
		 dictionaryVo.setOldDctValue(oldDctValue);
		 dictionaryVo.setStat(WaConstants.DCTTYPE_D);
		 int count = dictionaryDao.checkDictionary(dictionaryVo);
		 if (count > 0) {
				throw new ValidateFailedException("字典值["+ dictionaryVo.getDctValue() + "]已被使用");
			} else {
				// 获取新增新增数据字典初始状态
				dictionaryVo.setStat(WaConstants.DCTTYPE_C);
				// 获取登陆ID
				dictionaryVo.setCheckId(SessionUtils.getEmployee().getID());
				System.out.println(dictionaryVo.getDctId());
				dictionaryDao.updateDictionary(dictionaryVo);
			}
	 }

}
