package com.cmwa.ecc.business.service.impl.directmanager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.dao.directmanager.ValidateDao;
import com.cmwa.ecc.business.entity.accountManger.ValidateDto;
import com.cmwa.ecc.business.service.directmanager.ValidateService;
import com.cmwa.ecc.business.utils.DateUtils;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.PageUtil;
import com.cmwa.ecc.business.utils.ResultConstant;
import com.cmwa.ecc.business.utils.SearchParam;
/**
 * 
 * @author ex-liuy
 *
 */
@Service
public class ValidateServiceImpl implements ValidateService {
	private static Logger logger = LoggerFactory.getLogger(ValidateServiceImpl.class);
	@Autowired
	private ValidateDao validateDao;
	
	@Override
	public Page<ValidateDto> queryValidateList(SearchParam sp) {
		validateDao.queryValidateList(sp);
		List<ValidateDto> resultList = new ArrayList<ValidateDto>();
		List<ValidateDto> paramList =  new ArrayList<ValidateDto>();
		if(null!=sp&&null!=sp.getSp()&&sp.getSp().containsKey("validateList")){
			try{
				paramList = (List<ValidateDto>)sp.getSp().get("validateList");
				PageUtil<ValidateDto> page = new PageUtil<ValidateDto>();
				resultList = page.getPageList(sp, paramList);
			}catch(Exception e){
				logger.error("----ValidateServiceImpl-queryValidateList-sp-resultList-Exception:",e);
			}
		}
		return Page.create(resultList, sp.getStart(), sp.getLimit(), paramList.size());
	}

	@Override
	public JSONObject updateValidate(ValidateDto dto) {
		JSONObject result = new JSONObject();
		result.put(ResultConstant.C_RESULT_CODE, ResultConstant.C_RESULT_SUCCESS);
		result.put(ResultConstant.C_RESULT_MSG, ResultConstant.resultMap.get(ResultConstant.C_RESULT_CODE));
		try{
			if(StringUtils.isEmpty(dto.getOperatorType()) || StringUtils.isEmpty(dto.getIdtp()) || StringUtils.isEmpty(dto.getIdno())
					||StringUtils.isEmpty(dto.getIdvalidate())){
				result.put(ResultConstant.C_RESULT_CODE, ResultConstant.C_RESULT_FAILE);
				result.put(ResultConstant.C_RESULT_MSG, "参数不能为空");
				return result;
			}
			String idvalidate = dto.getIdvalidate().replaceAll("-", "");
			SearchParam sp = new SearchParam();
			sp.getSp().put("operatorType", dto.getOperatorType());
			sp.getSp().put("idtp", dto.getIdtp());
			sp.getSp().put("idno", dto.getIdno());
			sp.getSp().put("idvalidate", idvalidate);
			validateDao.updateValidate(sp);
			if(sp.getSp().containsKey("errcode")){
				result.put(ResultConstant.C_RESULT_CODE, sp.getSp().get("errcode"));
				result.put(ResultConstant.C_RESULT_MSG,  sp.getSp().get("errmsg"));
			}else{
				result.put(ResultConstant.C_RESULT_CODE, ResultConstant.C_RESULT_FAILE);
				result.put(ResultConstant.C_RESULT_MSG, "未知返回");
				logger.error("----ValidateServiceImpl--updateValidate-未知返回(Unknown to return)-sp:"+sp.toString());
			}
		}catch(Exception e){
			logger.error("----ValidateServiceImpl--updateValidate-Exception:",e);
			result.put(ResultConstant.C_RESULT_CODE, ResultConstant.C_RESULT_FAILE);
			result.put(ResultConstant.C_RESULT_MSG, ResultConstant.resultMap.get(ResultConstant.C_RESULT_FAILE));
		}
		return result;
	}

	@Override
	public ValidateDto queryValidateDtoToUpdateView(SearchParam sp) {
		ValidateDto reusltDto =  new ValidateDto();
		validateDao.queryValidateList(sp);
		List<ValidateDto> resultList = new ArrayList<ValidateDto>();
		if(null!=sp&&null!=sp.getSp()&&sp.getSp().containsKey("validateList")){
			try{
				resultList = (List<ValidateDto>)sp.getSp().get("validateList");
				if(resultList.size() > 0){
					reusltDto = resultList.get(0);
				}
				SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
				String date = DateUtils.formatDate(sf.parse(reusltDto.getIdvalidate()), "yyyy-MM-dd") ;
				reusltDto.setIdvalidate(date);
			}catch(Exception e){
				logger.error("----ValidateServiceImpl-queryValidateDtoToUpdateView-sp-resultList-Exception:",e);
			}
		}
		return reusltDto;
	}
}

