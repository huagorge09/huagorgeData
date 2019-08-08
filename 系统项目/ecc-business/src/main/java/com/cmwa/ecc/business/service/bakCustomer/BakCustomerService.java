package com.cmwa.ecc.business.service.bakCustomer;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.cmwa.ecc.business.entity.accountManger.BakCustDto;
import com.cmwa.ecc.business.utils.SearchParam;




public interface BakCustomerService {
	
	public ModelAndView openDialog(HttpServletRequest request);
	
	public BakCustDto addBakCustomer(BakCustDto dto);
	
	public BakCustDto convertBean(String str) ;
	
	public BakCustDto delBakCustomer(BakCustDto dto);
	
	public BakCustDto updateBakCustomer(BakCustDto dto);
	
	public List<BakCustDto> checkBakCustomerListPage(SearchParam sp);
	
	public BakCustDto checkBakCustomer(SearchParam sp);

}
