package com.cmwa.ec.weixin.manager.wxevent.event;

import org.apache.log4j.Logger;

public class EventFactory {
	private static Logger logger = Logger.getLogger(EventFactory.class);
	
	public static BaseEvent create(String eventObjectName) {
		logger.debug("EventFactory answer: " + eventObjectName);
		BaseEvent event = null;
		try {
			if(eventObjectName!=null){
				event = (BaseEvent)Class.forName(eventObjectName).newInstance();
			}
		} catch (Exception e) {
			logger.error("EventFactory.create异常：",e);
		} finally{
			event=null;
		}
		
		if(event==null){
			logger.info("事件调用处理类为空， event=null");
			event = new DefaultEvent();
		}
		
		return event;
		
	}
}
