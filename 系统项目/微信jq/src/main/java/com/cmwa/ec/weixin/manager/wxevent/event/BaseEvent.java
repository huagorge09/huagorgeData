package com.cmwa.ec.weixin.manager.wxevent.event;

import com.cmf.weixin.message.dto.BaseMsgDto;
import com.cmwa.ec.weixin.dto.ParameterDto;

/**
 * 每个实现类都要保留无参构造方法，以供EventFactory类中  通过类名反射获取实例对象 使用
 * 若没有添加其他构造方法，则不用提供无参构造方法
 * 若添加了其他构造方法，则必须提供无参构造方法
 * @author liury
 *
 */

public abstract class BaseEvent {
	
	/**
	 * 事件类型处理方法，返回处理结果
	 * @param reqDto
	 * @param parDto
	 * @return
	 */
	public abstract String getRes(BaseMsgDto reqDto, ParameterDto parDto);
	
}
