package com.cmwa.ecc.business.service.impl.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.commonVo.ParameterDto;
import com.cmwa.ecc.business.dao.widget.WidgetDao;
import com.cmwa.ecc.business.service.widget.WidgetService;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 页面插件
 * 公共插件-交互接口
 * @author ex-liuy
 *
 */
@Service
public class WidgetServiceImpl implements WidgetService {
	private final Logger logger = LoggerFactory.getLogger(getClass()); 
	
	@Autowired
	private WidgetDao widgetDao;
	
	@Override
	public List<Map<String, Object>> queryMatchApkindList(SearchParam sp) {
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		try{
			result = widgetDao.queryMatchApkindList(sp);
		}catch(Exception e){
			logger.error("----WidgetServiceImpl-queryMatchApkindList-Exception:"+sp.toString(),e);
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> queryParamListByStAndKy(String pmst,
			String pmky) {
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		SearchParam sp = new SearchParam();
		try{
			sp.getSp().put("pmst", pmst);
			sp.getSp().put("pmky", pmky);
			result = widgetDao.queryMatchApkindList(sp);
		}catch(Exception e){
			logger.error("----WidgetServiceImpl-queryMatchApkindList-Exception:"+sp.toString(),e);
		}
		return result;
	}

	@Override
	public ParameterDto getParameterByPmstPmkyPmco(String pmst, String pmky,
			String pmco) {
		ParameterDto dto = new ParameterDto();
		try {
			dto = widgetDao.getParameterByPmstPmkyPmco(pmst, pmky, pmco);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询数据字典异常：",e);
		}
		return dto;
	}
}
