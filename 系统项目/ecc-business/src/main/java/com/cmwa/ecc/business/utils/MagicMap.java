package com.cmwa.ecc.business.utils;

import java.util.HashMap;

/**
 * HashMap的扩展<br>
 * 提供一个简便的构造函数
 * @author wanggang
 *
 */
@SuppressWarnings("rawtypes")
public class MagicMap extends HashMap
{
	private static final long serialVersionUID = 1L;
	
	public MagicMap(){}
	
	@SuppressWarnings("unchecked")
	public MagicMap(Object[][] params)
	{
		if(params == null)
			return;
		
		for(Object[] param : params)
		{
			if(param == null || param.length < 2)
				continue;
			
			this.put(param[0], param[1]);
		}
	}
	
}
