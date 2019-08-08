package com.cmwa.ec.weixin.manager.wxevent.impl;

import java.util.HashMap;
import java.util.Map;

import com.cmwa.ec.weixin.manager.wxevent.ActivityEventHandler;


public class ActivityEventHandlerFactory {
	
	static Map<String, ActivityEventHandler> activityObjectMaps = new HashMap<String, ActivityEventHandler>();

	public static  ActivityEventHandler getEventHanlder(String activityId)
	{
		if(activityObjectMaps.containsKey(activityId))
		{
			return activityObjectMaps.get(activityId);
		}
		return null;
	}

	
	
	public static Map<String, ActivityEventHandler> getActivityObjectMaps() {
		return activityObjectMaps;
	}

	public static void setActivityObjectMaps(
			Map<String, ActivityEventHandler> activityObjectMaps) {
		ActivityEventHandlerFactory.activityObjectMaps = activityObjectMaps;
	}
	
}
