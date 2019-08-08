package com.cmwa.ecc.business.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmwa.ecc.business.commonVo.DictionaryVo;
import com.cmwa.ecc.business.exception.RepositoryException;
import com.cmwa.ecc.business.exception.ValidateFailedException;
import com.cmwa.ecc.business.service.dictionary.DictionaryService;
import com.cmwa.ecc.business.utils.DictionaryUtil;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.WaConstants;

@Controller("dictionaryController")
@RequestMapping("/service/dictionary")
public class DictionaryController extends BaseController {

	private final static String PAGE_PATH = "jsp/dictionary/";  //设置jsp的访问路径  by  ex—huangch
	@Resource
	private DictionaryUtil dictUtil;
	
	@Resource
	private DictionaryService dictionaryService;

	/**
	 * 刷新数据字典缓存
	 */
	@RequestMapping("refresh")
	public String refresh(Model model) {
		dictUtil.reload();
		int count=null==dictUtil.getDictionarys()?0:dictUtil.getDictionarys().size();
		model.addAttribute("successInfo", "本次共刷新"+count+"条数据。");
		return redirectSuccess();
	}
	
	/**
	 * 跳转到数据字典列表页面
	 * @author ex-huangch 2016.5.3
	 */
	@RequestMapping("/dictionaryListView")
	public String dictionaryListView(ModelMap model) {
		return PAGE_PATH+"dictionaryList";
	}
	
	 /**
	  * 数据字典列表分页数据
	  * @author ex-huangch 2016.5.3
	  * @param sp
	  * @return
	  */
	 @RequestMapping("/dictionaryListPage")
	 @ResponseBody
	 public Page<DictionaryVo> dictionaryListPage(SearchParam sp) {
		 Page<DictionaryVo> list = dictionaryService.dictionaryListPage(sp);
		  return list;
	 }	
	
	 /**
	  * 打开新增数据字典页面
	  * @author ex-huangch 2016.5.3
	  * @return
	  */
	 @SuppressWarnings({ "static-access", "rawtypes" })
     @RequestMapping("/dictionaryAddView")
	 public String dictionaryAddView(ModelMap model) {
    	 List dctDat = DictionaryUtil.getInstance().getDictionaryByType(WaConstants.DCTTYPE_DAT_YES_NOX);
    	 model.addAttribute("dctDat", dctDat);
    	 return PAGE_PATH+"dictionaryAdd";
	 }
     
    /**
 	 * 打开修改系统数据字典页面
 	 * @param dctId
 	 * @param model
 	 * @author ex-huangch
 	 */
 	@SuppressWarnings({ "static-access", "rawtypes" })
	@RequestMapping("/dictionaryUpdateView")
 	public String dictionaryUpdateView(@RequestParam(value = "dctId") String dctId, ModelMap model) throws ValidateFailedException{
 		List dctDat = DictionaryUtil.getInstance().getDictionaryByType(WaConstants.DCTTYPE_DAT_YES_NOX);
 		model.addAttribute("dctDat", dctDat);
 		DictionaryVo dictionaryVo = dictionaryService.queryDictionaryByDctId(dctId);
 		model.addAttribute("dictionaryVo", dictionaryVo);
 		return PAGE_PATH+"dictionaryUpdate";
 	}

 	 /**
 	 * 数据库新增系统数据字典
 	 * @param dictionaryVo
 	 * @param model
 	 * @throws RepositoryException
 	 * @author ex-huangch   2016.5.4
 	 */
 	@RequestMapping("/saveDictionary")
 	public String saveDictionary(DictionaryVo dictionaryVo, ModelMap model) throws ValidateFailedException {
 		dictionaryService.saveDictionary(dictionaryVo);
 		return redirectSuccess();
 	}
 	
 	/**
 	 * 删除系统数据字典
 	 * @author ex-huangch 2016.5.4
 	 * @param dctId
 	 * @return
 	 */
 	@RequestMapping("/deleteDictionary")
 	public String deleteDictionary(@RequestParam(value = "dctId") String dctId, ModelMap model) {
 		dictionaryService.deleteDictionary(dctId);
 		return redirectSuccess();
 	}
 	
 	/**
 	 * 更新系统数据字典信息到数据库
 	 * @param dictionaryVo
 	 * @param model
 	 * @return
 	 * @author ex-huangch
 	 */
 	@RequestMapping("/updateDictionary")
 	public String updateDictionary(DictionaryVo dictionaryVo, ModelMap model) throws ValidateFailedException {
 		dictionaryService.updateDictionary(dictionaryVo);
 		return redirectSuccess();
 	   }
 		
 	/**
 	 * 打开查看字典详情信息页面
 	 * @author ex-huangch
 	 * @return
 	 */
 	@RequestMapping("/dictionaryDetailView")
 	public String dictionaryDetailView(@RequestParam(value = "dctId") String dctId, ModelMap model) throws ValidateFailedException {
 		DictionaryVo dictionaryVo = dictionaryService.queryDictionaryByDctId(dctId);
 		model.addAttribute("dictionaryVo", dictionaryVo);
 		return PAGE_PATH+"dictionaryDetail";
 	}
 	/**
 	 * @author ex-lix
 	 * U盾管理 Jqgrid 下拉框
 	 * @param dictRootType
 	 * @param dictLeftType
 	 * @return
 	 */
 	@SuppressWarnings("rawtypes")
	@RequestMapping("/dictionaryAjaxView")
 	@ResponseBody
 	public Object dictionaryAjaxView(@RequestParam(value="dictRootType",required=false) String dictRootType,
 									 @RequestParam(value="dictLeftType",required=false) String dictLeftType,
 									@RequestParam(value="dictFathType",required=false) String dictFathType){
 		if(dictFathType == null) dictFathType = dictRootType;
 		if(dictLeftType == null) dictLeftType = dictRootType; 
 		List list = DictionaryUtil.getDictionaryByType(dictRootType, dictFathType, dictLeftType);
 		StringBuffer strb = new StringBuffer();
 		for(int i = 0;i < list.size(); i++){
 			DictionaryVo dto = (DictionaryVo)list.get(i);
 			strb.append(dto.getDctValue());
 			strb.append(":");
 			strb.append(dto.getDctName());
 			if( i != list.size()-1){//去掉最后一个;
 				strb.append(";");
 			}
 		}
 		return strb.toString();
 	}
     
}
