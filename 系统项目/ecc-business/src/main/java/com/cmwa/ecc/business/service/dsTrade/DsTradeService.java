package com.cmwa.ecc.business.service.dsTrade;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.cmwa.ecc.business.entity.dsTrade.DsTradeDataDto;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

public interface DsTradeService {
	public Page<DsTradeDataDto> queryDsTradeDataListPage(SearchParam sp);
	public ModelAndView openDialog(HttpServletRequest request);
	public void updateTradeDate(DsTradeDataDto dto);
}
