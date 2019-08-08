package com.cmwa.ecc.business.utils;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 对在线用户的管理
 */
public class ClientManager {
	private static final Log log = LogFactory.getLog(ClientManager.class);
	private static ClientManager instance = new ClientManager();
	private ConcurrentHashMap<String, Client> onlineClientMap = new ConcurrentHashMap<String, Client>();

	private ClientManager() {
	}

	public static ClientManager getInstance() {
		return instance;
	}

	/**
	 * 
	 * @param sessionId
	 * @param client
	 */
	public void addClinet(String sessionId, Client client) {
		Client old = onlineClientMap.get(sessionId);
		if (old == null) {
			onlineClientMap.putIfAbsent(sessionId, client);
			log.info("The client sesionId="+sessionId +"  add ,successfully");
		} else {
			onlineClientMap.replace(sessionId, old, client);
			log.info("The client sesionId="+sessionId +"  replace ,successfully");
		}
	}

	/**
	 * sessionId
	 */
	public void removeClinet(String sessionId) {
		if (onlineClientMap.containsKey(sessionId)) {
			onlineClientMap.remove(sessionId);
			log.info("The client sesionId="+sessionId +" will be removed , still has "+onlineClientMap.size() +" client online!");
		}else{
			log.info("The client sesionId="+sessionId +" will be removed , but no exists ? please check, still has "+onlineClientMap.size() +" client online!");
		}
	}

	/**
	 * 
	 * @param sessionId
	 * @return
	 */
	public Client getClient(String sessionId) {
		return StringUtils.isEmpty(sessionId) ? null : onlineClientMap.get(sessionId);
	}

	/**
	 * 
	 * @return
	 */
	public Client getClient() {
		return onlineClientMap.get(ContextHolderUtil.getSession().getId());
	}

	/**
	 * 
	 * @return
	 */
	public Collection<Client> getAllClient() {
		return onlineClientMap.values();
	}

	/**
	 * 清空客户端数据
	 */
	public void clearAllClientData() {
		for (Client client : getAllClient()) {
			client.clearAll();
		}
	}

}
