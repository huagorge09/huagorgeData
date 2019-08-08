package com.cmwa.ecc.business.utils.cached;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.cmwa.ecc.business.commonVo.DictionaryVo;
import com.cmwa.ecc.business.exception.CachedException;
import com.cmwa.ecc.business.service.cached.ShareCachedService;
import com.cmwa.ecc.business.service.dictionary.DictionaryService;

/**
 * 字典缓存类
 * 
 * @author pangtf
 */
@Component
public class DictionaryCached implements ShareCachedService, InitializingBean {
	@Resource
	private DictionaryService dictionaryService;

	private static Map<DictEntry, String> dictCached = new HashMap<DictEntry, String>(1024);

	@Override
	public void afterPropertiesSet() throws Exception {
		loadMappingCached();
	}

	@Override
	public void loadMappingCached() throws CachedException {
		dictCached.clear();
		DictionaryVo dctDto = new DictionaryVo();
//		dctDto.setStat(Constant.C_STATUS_D);
		List<DictionaryVo> dictionaryVos = dictionaryService.listDictionary(dctDto);
		for (DictionaryVo dictionaryVo : dictionaryVos) {
			DictEntry entry = new DictEntry();
			entry.setDctRootType(dictionaryVo.getDctRootType());
			entry.setDctFathType(dictionaryVo.getDctFathType());
			entry.setDctLeftType(dictionaryVo.getDctLeftType());
			entry.setDctValue(dictionaryVo.getDctValue());
			dictCached.put(entry, dictionaryVo.getDctName());
		}
	}

	/**
	 * 代替存储过程中的 IBF_GET_DCTNAME_EX()函数
	 * 
	 * @param dctType
	 *            --根类型
	 * @param dctValue
	 *            --字典值
	 * @return
	 */
	public static String getDictName(String dctType, String dctValue) {
		return getDictName(dctType, dctType, dctType, dctValue);
	}
	
	/**
	 * 多个字典值取值(例如：字典值dctValue=1,2需取值=张三,李四)
	 * @param dctType	字典类型
	 * @param dctValue	字典值
	 * @param splitType	多个值的分符
	 * @return
	 */
	public static String getDictNameByArry(String dctType, String dctValue,String splitType) {
		StringBuffer dctNameArry = new StringBuffer();
		if (dctValue != null) {
			String[] dctArry = dctValue.split(splitType);
			for (int i = 0; i < dctArry.length; i++) {
				String dict = dctArry[i];
				dctNameArry.append(getDictName(dctType, dict));
				if (i < dctArry.length - 1) 
					dctNameArry.append(splitType);
			}
		}
		return dctNameArry.toString();
	}
	
	/**
	 * 多个字典值取值(例如：字典值dctValue=1,2需取值=张三,李四)
	 * @param dctType	字典类型
	 * @param dctValue	字典值
	 * @param splitType	多个值的分符
	 * @return、DCTROOTTYPE、DCTFATHTYPE、DCTLEFTTYPE三种类型不一样   INVEST_PATH_TYPE
	 */
	public static String getDictNameByArrys(String dctType, String dctValue,String splitType) {
		StringBuffer dctNameArry = new StringBuffer();
		if (dctValue != null) {
			String[] dctArry = dctValue.split(splitType);
			for (int i = 0; i < dctArry.length; i++) {
				String dict = dctArry[i];
				String subdict = dict.substring(0, 1);
				String dctFathType = dctType+"_"+subdict;
				String dctLeftType = dctType+"_"+dict;
				if(dict.length()>1){
					dctNameArry.append(getDictName(dctType, dctFathType, dctLeftType, dict));
				}else{
					dctNameArry.append(getDictName(dctType, dctType, dctLeftType, dict));
				}
				
				if (i < dctArry.length - 1) 
					dctNameArry.append(splitType);
			}
		}
		return dctNameArry.toString();
	}

	/**
	 * 代替存储过程中的 IBF_GET_DCTNAME_EX()函数
	 * @param dctType 字典根类型,父类型
	 * @param dctLeftType 字典子类型
	 * @param dctValue 字典值
	 * @return
	 */
	public static String getDictName(String dctType, String dctLeftType, String dctValue) {
		return getDictName(dctType, dctType, dctLeftType, dctValue);
	}

	/**
	 * 代替存储过程中的 e：IBF_GET_DCTNAME('CAH_BIZ_TYP','CAH_BIZ_TYP','PRD_SETT_TYP', TPCF.BNSTYPE)函数
	 * @param dctRootType 字典根类型
	 * @param dctFathType 字典父类型
	 * @param dctLeftType 字典子类型
	 * @param dctValue  字典值
	 * @return
	 */
	public static String getDictName(String dctRootType, String dctFathType, String dctLeftType, String dctValue) {
		DictEntry entry = new DictEntry(dctRootType, dctFathType, dctLeftType, dctValue);
		return dictCached.get(entry);
	}
	
	/**
	 * 代替存储过程中的 e：IBF_GET_DCTNAME('CAH_BIZ_TYP','CAH_BIZ_TYP','', TPCF.BNSTYPE)函数<br>
	 * 用于子类型不确定的情况
	 * @param dctRootType 字典根类型
	 * @param dctFathType 字典父类型
	 * @param dctValue  字典值
	 * @return
	 */
	public static String getDictNameNoLeftPath(String dctRootType, String dctFathType, String dctValue) {
		for (DictEntry entry:dictCached.keySet()) {
			if (entry.dctRootType.equals(dctRootType) && entry.dctFathType.equals(dctFathType) && entry.dctValue.equals(dctValue)) {
				return dictCached.get(entry);
			}
		}
		return "";
	}

	/**
	 * 字典内部类
	 */
	private static class DictEntry {
		private String dctRootType; // 字典根类型
		private String dctFathType; // 字典父类型
		private String dctLeftType; // 字典子类型
		private String dctValue; // 字典值

		public DictEntry() {
		}

		public DictEntry(String dctRootType, String dctFathType, String dctLeftType, String dctValue) {
			super();
			this.dctRootType = dctRootType;
			this.dctFathType = dctFathType;
			this.dctLeftType = dctLeftType;
			this.dctValue = dctValue;
		}

		public String getDctRootType() {
			return dctRootType;
		}

		public void setDctRootType(String dctRootType) {
			this.dctRootType = dctRootType;
		}

		public String getDctFathType() {
			return dctFathType;
		}

		public void setDctFathType(String dctFathType) {
			this.dctFathType = dctFathType;
		}

		public String getDctLeftType() {
			return dctLeftType;
		}

		public void setDctLeftType(String dctLeftType) {
			this.dctLeftType = dctLeftType;
		}

		public String getDctValue() {
			return dctValue;
		}

		public void setDctValue(String dctValue) {
			this.dctValue = dctValue;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((dctFathType == null) ? 0 : dctFathType.hashCode());
			result = prime * result + ((dctLeftType == null) ? 0 : dctLeftType.hashCode());
			result = prime * result + ((dctRootType == null) ? 0 : dctRootType.hashCode());
			result = prime * result + ((dctValue == null) ? 0 : dctValue.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			DictEntry other = (DictEntry) obj;
			if (dctFathType == null) {
				if (other.dctFathType != null)
					return false;
			} else if (!dctFathType.equals(other.dctFathType))
				return false;
			if (dctLeftType == null) {
				if (other.dctLeftType != null)
					return false;
			} else if (!dctLeftType.equals(other.dctLeftType))
				return false;
			if (dctRootType == null) {
				if (other.dctRootType != null)
					return false;
			} else if (!dctRootType.equals(other.dctRootType))
				return false;
			if (dctValue == null) {
				if (other.dctValue != null)
					return false;
			} else if (!dctValue.equals(other.dctValue))
				return false;
			return true;
		}

	}

}