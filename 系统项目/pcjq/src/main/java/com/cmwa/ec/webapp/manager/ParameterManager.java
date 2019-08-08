package com.cmwa.ec.webapp.manager;

import java.util.List;

import com.cmwa.ec.webapp.dto.ParameterDto;



public interface ParameterManager {
	
	public List list() ;
	public ParameterDto queryAccessToken();
}
