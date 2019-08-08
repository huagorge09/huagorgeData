package com.cmwa.ecc.business.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cmwa.ecc.business.utils.ClientManager;

/**
 * 客户端会话监听器
 * 
 * @author pangtf
 */
public class ClientSessionListener implements HttpSessionListener {
	private static final Log log = LogFactory.getLog(ClientSessionListener.class);

	@Override
	public void sessionCreated(HttpSessionEvent event) {
	}

	/**
	 * 会话失效调用
	 * 清除这个session的ClientManager缓存
	 * @param event
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		ClientManager.getInstance().removeClinet(session.getId());
	}

}
