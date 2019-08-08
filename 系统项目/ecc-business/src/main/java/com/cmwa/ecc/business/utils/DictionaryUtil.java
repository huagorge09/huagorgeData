package com.cmwa.ecc.business.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmwa.ecc.business.commonVo.DictionaryVo;
import com.cmwa.ecc.business.exception.ValidateFailedException;
import com.cmwa.ecc.business.service.dictionary.DictionaryService;


/**
 * 数据字典查询公用方法类
 * @author wangzy
 *
 */
public class DictionaryUtil {
	private static Logger log = LoggerFactory.getLogger(DictionaryUtil.class.getName());
	
	
	/**
	 * 数据字典缓存集合
	 */
	private static List dictionarys = new Vector();
	
	@Resource
	private DictionaryService dictionaryService;
	
	// 同步锁
	private static byte[] lock = new byte[0];
	
	private static DictionaryUtil instance;
	
	private DictionaryUtil() {
	}
	public static DictionaryUtil getInstance(){
		if(instance == null){
			instance = new DictionaryUtil();
		}
		
		return instance;
	}
	
	/**
	 * 刷新缓存
	 */
	public void reload() {
		init();
	}

	/**
	 * 校验缓存的有效性
	 */
	public boolean validateCach() {
		if (CollectionUtils.isEmpty(dictionarys)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 缓存初始化
	 */
	private void init() {

		// 加同步锁
		synchronized (lock) {
			try {
				DictionaryVo dctDto = new DictionaryVo();
				dctDto.setStat(Constant.C_STATUS_D);
				dictionarys = dictionaryService.listDictionary(dctDto);
			} catch (Exception e) {
				log.error("数据字典集加载异常：", e);
				e.getStackTrace();
			}
		}
	}

	/**
	 * 获取所有数据字典
	 * 
	 * @return List
	 */
	public List getDictionarys() {
		return dictionarys;
	}

	/**
	 * 获取单个数据字典
	 * 
	 * @param dctId
	 *            String 数据字典代码
	 * @return DictionaryVo
	 */
	public DictionaryVo getDictionary(String dctId) {
		if (dictionarys == null) {
			return null;
		}
		if (dictionarys != null && dictionarys.size() > 0) {
			for (int i = 0; i < dictionarys.size(); i++) {
				DictionaryVo dtoTmp = (DictionaryVo) dictionarys.get(i);
				if (dtoTmp != null) {
					if (dctId.equals(dtoTmp.getDctId())) {
						return dtoTmp;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 根据数据字典代码获取数据字典名称
	 * 
	 * @param dctId
	 *            String 数据字典代码
	 * @return String
	 */
	public String getDictionaryName(String dctId) {

		DictionaryVo dto = getDictionary(dctId);
		if (dto != null) {
			return dto.getDctName();
		} else {
			return "";
		}
	}

	/**
	 * 根据数据字典类型和字典值获取数据字典名称
	 * 
	 * @param dctType
	 *            String 数据字典类型
	 * @param dctType
	 *            String 数据字典值
	 * @return String
	 */
	public String getDictionaryName(String dctRootType, String dctFathType,
			String dctLeftType, String dctValue) {

		if (dictionarys == null) {
			return "";
		} else {
			for (int i = 0; i < dictionarys.size(); i++) {
				DictionaryVo dto = (DictionaryVo) dictionarys.get(i);
				if (dctRootType.equals(dto.getDctRootType())
						&& dctFathType.equals(dto.getDctFathType())
						&& dctLeftType.equals(dto.getDctLeftType())
						&& dctValue.equals(dto.getDctValue())) {

					return dto.getDctName();
				}
			}
			return "";
		}
	}

	public String getDictionrySelHtml(String dctRootType, String dctFathType,
			String dctLeftType, String selName, String onChange, String defalt) {
		if (dictionarys == null) {
			return "<select name='" + selName + "' onChange='" + onChange
					+ "' class='form-text'><option value=''></option></select>";
		} else {
			String selHtml = "<select name='" + selName + "' id='" + selName + "' onChange='"
					+ onChange + "' class='form-text'>";
			for (int i = 0; i < dictionarys.size(); i++) {
				DictionaryVo dto = (DictionaryVo) dictionarys.get(i);
				if (dctRootType.equals(dto.getDctRootType())
						&& dctFathType.equals(dto.getDctFathType())
						&& dctLeftType.equals(dto.getDctLeftType())) {

					String selected = "";
					if (defalt != null && defalt.equals(dto.getDctValue())) {
						selected = "selected";
					}

					selHtml += "<option value='" + dto.getDctValue() + "' "
							+ selected + ">";
					selHtml += dto.getDctName();
					selHtml += "</option>";
				}
			}
			selHtml += "</select>";

			return selHtml;
		}
	}

	public String getDictionryAllSelHtml(String dctRootType, String dctFathType,
			String dctLeftType, String selName, String onChange, String defalt) {
		if (dictionarys == null) {
			return "<select name='" + selName + "' onChange='" + onChange
					+ "' class='form-text'><option value=''></option></select>";
		} else {
			String selHtml = "<select name='" + selName + "' id='" + selName + "' onChange='"
					+ onChange + "' class='form-text'>";
			selHtml += "<option value=''>全部</option>";
			for (int i = 0; i < dictionarys.size(); i++) {
				DictionaryVo dto = (DictionaryVo) dictionarys.get(i);
				if (dctRootType.equals(dto.getDctRootType())
						&& dctFathType.equals(dto.getDctFathType())
						&& dctLeftType.equals(dto.getDctLeftType())) {

					String selected = "";
					if (defalt != null && defalt.equals(dto.getDctValue())) {
						selected = "selected";
					}

					selHtml += "<option value='" + dto.getDctValue() + "' "
							+ selected + ">";
					selHtml += dto.getDctName();
					selHtml += "</option>";
				}
			}
			selHtml += "</select>";

			return selHtml;
		}
	}

	/**
	 * 根据数据字典类型和是否叶子节点字段
	 * 
	 * @param dctType
	 *            String 数据字典类型
	 * @param isLeaf
	 *            String 是否叶子节点
	 * @return String
	 */
	public static List getDictionaryByType(String dctType) {
		return getDictionaryByType(dctType, dctType, dctType);
	}	
	
	/**
	 * 根据数据字典类型获取列表，不含数据项目说明
	 * 
	 * @param dctRootType
	 *            String 数据字典根类型
	 * @param dctFathType
	 * 			  String 数据字典父类型
	 * @param dctLeftType
	 * 			  String 数据字典子类型
	 * @return List 返回该类型的列表
	 */
	public static List getDictionaryByType(String dctRootType, String dctFathType, String dctLeftType) {
		List vList = new Vector();
		if ((dictionarys == null || dictionarys.size() == 0)) {
			return vList;
		} else {
			for (int i = 0; i < dictionarys.size(); i++) {
				DictionaryVo dto = (DictionaryVo) dictionarys.get(i);
				if (dctRootType.equals(dto.getDctRootType())
						&& dctFathType.equals(dto.getDctFathType())
						&& dctLeftType.equals(dto.getDctLeftType())
						&& !dctRootType.equals(dto.getDctValue())
					) {
					vList.add(dto);
				}
			}
			return vList;
		}
	}
	
	/**
	 * 根据rootType获取
	 * @param dctRootType
	 * @return
	 */
	public static List getDictionaryByRootType(String dctRootType) {
		List vList = new Vector();
		if ((dictionarys == null || dictionarys.size() == 0)) {
			return vList;
		} else {
			for (int i = 0; i < dictionarys.size(); i++) {
				DictionaryVo dto = (DictionaryVo) dictionarys.get(i);
				if (dctRootType.equals(dto.getDctRootType())
						&&!dctRootType.equals(dto.getDctValue())) {
					vList.add(dto);
				}
			}
			return vList;
		}
	}
	
	/**
	 * 根据数据字典类型获取列表，不含数据项目说明
	 * 
	 * @param dctRootType
	 *            String 数据字典根类型
	 * @param dctFathType
	 * 			  String 数据字典父类型
	 * @return List 返回该类型的列表
	 */
	public static List getDictionaryByType(String dctRootType, String dctFathType) {
		List vList = new Vector();
		if ((dictionarys == null || dictionarys.size() == 0)) {
			return vList;
		} else {
			for (int i = 0; i < dictionarys.size(); i++) {
				DictionaryVo dto = (DictionaryVo) dictionarys.get(i);
				if (dctRootType.equals(dto.getDctRootType())
						&& dctFathType.equals(dto.getDctFathType())
						&& !dctRootType.equals(dto.getDctValue())
					) {
					vList.add(dto);
				}
			}
			return vList;
		}
	}

	public List getDictionaryByTypeAndValue(String dctType, String dctValue)
	{
		return getDictionaryByTypeAndValue(dctType, dctType, dctType, dctValue);
	}
	
	/**
	 * 
	 * @param dctRootType
	 * @param dctFathType
	 * @param dctLeftType
	 * @param dctValue
	 * @return
	 */
	public List getDictionaryByTypeAndValue(String dctRootType, String dctFathType, 
			String dctLeftType, String dctValue) {
		List vList = new Vector();
		if ((dictionarys == null || dictionarys.size() == 0)) {
			return vList;
		} else {
			for (int i = 0; i < dictionarys.size(); i++) {
				DictionaryVo dto = (DictionaryVo) dictionarys.get(i);
				if (dctRootType.equals(dto.getDctRootType())
						&& dctFathType.equals(dto.getDctFathType())
						&& dctLeftType.equals(dto.getDctLeftType())
						&& dctValue.equals(dto.getDctValue())
					) {
					vList.add(dto);
				}
			}
			return vList;
		}
	}

	public DictionaryService getDictionaryService() {
		return dictionaryService;
	}

	public void setDictionaryService(DictionaryService dictionaryService) {
		this.dictionaryService = dictionaryService;
	}


	/**
	 * 批量添加或修改
	 * @author ex-dingxq 2016-6-15
	 * @param dictList
	 * @throws ValidateFailedException 
	 */
	public void insertOrUpdateDict(List<DictionaryVo> dictList)throws ValidateFailedException {
		List<DictionaryVo> insetDictList = new ArrayList<DictionaryVo>();
		List<DictionaryVo> updateDictList = new ArrayList<DictionaryVo>();
		if (dictList != null) {
			for (int i = 0; i < dictList.size(); i++) {
				DictionaryVo dictDto = dictList.get(i);
				if (dictDto != null) {
					List tempList = getDictionaryByTypeAndValue(dictDto.getDctRootType(), dictDto.getDctFathType(),
							dictDto.getDctLeftType(), dictDto.getDctValue());
					
					// 判断是否需要新增、修改
					if (tempList != null && tempList.size() > 0) {
						DictionaryVo dictDtotmp = (DictionaryVo) tempList.get(0);
						if (dictDtotmp != null && !dictDto.getDctName().equals(dictDtotmp.getDctName())) {
							dictDto.setDctId(dictDtotmp.getDctId());
							updateDictList.add(dictDto);
						}
					} else {
						insetDictList.add(dictDto);
					}
				}
			}

			// 批量新增(后期修改成存储过程批量新增)
			for (int i = 0; i < insetDictList.size(); i++) {
				DictionaryVo dictDto = (DictionaryVo) insetDictList.get(i);
				dictionaryService.saveDictionary(dictDto);
			}

			// 批量修改(后期修改成存储过程批量新增)
			for (int i = 0; i < updateDictList.size(); i++) {
				DictionaryVo dictDto = (DictionaryVo) updateDictList.get(i);
				dictionaryService.updateDictionary(dictDto);
			}

			// 刷新缓存
			reload();
		}
	}
	
}
