package com.cmwa.ec.weixin.util.cache;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmwa.ec.weixin.client.QueryServiceClient;
import com.cmwa.ec.weixin.client.QueryServiceClient;
import com.cmwa.ec.weixin.constants.WXConstants;
import com.cmwa.ec.weixin.dto.ParameterDto;
import com.cmwa.ec.weixin.manager.business.ParameterManager;
import com.cmwa.ec.weixin.util.SpringContextUtil;



public class ParameterCache {
	
	private static Logger logger = Logger.getLogger(ParameterCache.class);

	private static ParameterCache instance;
	private static Hashtable<String,ParameterDto> cache;
	private static ParameterDto tokenDto;
	
	@Autowired
	private ParameterManager delegate;
	@Autowired
	private QueryServiceClient queryServiceClient;
	
	private ParameterCache(){
		init();
	}
	
	public static ParameterCache getInstance() {
		if (instance == null) {
			instance = new ParameterCache();
			
		}
		return instance;
	}
	
	public static void refresh(){
		instance = new ParameterCache();
		
	}

	public void clear() {
		cache = new Hashtable();
	}
	
	public static Hashtable getData(){
		return getInstance().getCache();
	}
	
	public Hashtable getCache(){
		return cache;
	}
	
	public ParameterDto getToken(){
		return tokenDto;
	}
	
	public void setToken(ParameterDto tokenDto){
		this.tokenDto = tokenDto;
	}
	
	/**
	 * 初始化字典表记录
	 */
	private void init() {
		delegate = (ParameterManager)SpringContextUtil.getBean("parameterManager");
		queryServiceClient = (QueryServiceClient)SpringContextUtil.getBean("queryServiceClient");
		cache = new Hashtable<String, ParameterDto>();
		try {
			
			List<ParameterDto> parameterList1  = queryServiceClient.queryParameter("", "", "", "");
			for(int i = 0; i < parameterList1.size(); i++)
			{
				ParameterDto dto = (ParameterDto)parameterList1.get(i);				
				String cacheKey = dto.getPmst()  + "#" + dto.getPmky() +"#" + dto.getPmco() ;
				cache.put(cacheKey , dto);
			}
			
			List<ParameterDto> parameterList  = delegate.list();
			for(int i = 0; i < parameterList.size(); i++)
			{
				ParameterDto dto = (ParameterDto)parameterList.get(i);				
				String cacheKey = dto.getPmst()  + "#" + dto.getPmky() +"#" + dto.getPmco() ;
				cache.put(cacheKey , dto);
			}
			
			{
				String value = SpringContextUtil.getProperty("wx.config.token");
				if (value != null) {
					ParameterDto paraDto = new ParameterDto();
					paraDto.setPmst(WXConstants.PMST_CONFIG);
					paraDto.setPmky(WXConstants.PMKY_PUBLIC);
					paraDto.setPmco("token");
					paraDto.setPmv1(value);
					String tmpKey = paraDto.getPmst() + "#" + paraDto.getPmky() + "#" + paraDto.getPmco();
					cache.put(tmpKey, paraDto);
				}
			}
			
			{
				String value = SpringContextUtil.getProperty("wx.config.appid");
				if (value != null) {
					ParameterDto paraDto = new ParameterDto();
					paraDto.setPmst(WXConstants.PMST_CONFIG);
					paraDto.setPmky(WXConstants.PMKY_PUBLIC);
					paraDto.setPmco("appid");
					paraDto.setPmv1(value);
					String tmpKey = paraDto.getPmst() + "#" + paraDto.getPmky() + "#" + paraDto.getPmco();
					cache.put(tmpKey, paraDto);
				}
			}
			
			{
				String value = SpringContextUtil.getProperty("wx.config.name");
				if (value != null) {
					ParameterDto paraDto = new ParameterDto();
					paraDto.setPmst(WXConstants.PMST_CONFIG);
					paraDto.setPmky(WXConstants.PMKY_PUBLIC);
					paraDto.setPmco("weiXinUserName");
					paraDto.setPmv1(value);
					String tmpKey = paraDto.getPmst() + "#" + paraDto.getPmky() + "#" + paraDto.getPmco();
					cache.put(tmpKey, paraDto);
				}
			}
			
			{
				String value = SpringContextUtil.getProperty("wx.config.secret");
				if (value != null) {
					ParameterDto paraDto = new ParameterDto();
					paraDto.setPmst(WXConstants.PMST_CONFIG);
					paraDto.setPmky(WXConstants.PMKY_PUBLIC);
					paraDto.setPmco("secret");
					paraDto.setPmv1(value);
					String tmpKey = paraDto.getPmst() + "#" + paraDto.getPmky() + "#" + paraDto.getPmco();
					cache.put(tmpKey, paraDto);
				}
			}
			
			{
				String value = SpringContextUtil.getProperty("wx.config.socketIP");
				if (value != null) {
					ParameterDto paraDto = new ParameterDto();
					paraDto.setPmst(WXConstants.PMST_CONFIG);
					paraDto.setPmky(WXConstants.PMKY_PUBLIC);
					paraDto.setPmco("socketIP");
					paraDto.setPmv1(value);
					String tmpKey = paraDto.getPmst() + "#" + paraDto.getPmky() + "#" + paraDto.getPmco();
					cache.put(tmpKey, paraDto);
				}
			}
			
			{
				String value = SpringContextUtil.getProperty("wx.config.socketPort");
				if (value != null) {
					ParameterDto paraDto = new ParameterDto();
					paraDto.setPmst(WXConstants.PMST_CONFIG);
					paraDto.setPmky(WXConstants.PMKY_PUBLIC);
					paraDto.setPmco("socketPort");
					paraDto.setPmv1(value);
					String tmpKey = paraDto.getPmst() + "#" + paraDto.getPmky() + "#" + paraDto.getPmco();
					cache.put(tmpKey, paraDto);
				}
			}
			
			{
				String value = SpringContextUtil.getProperty("wx.config.wxHost");
				if (value != null) {
					ParameterDto paraDto = new ParameterDto();
					paraDto.setPmst(WXConstants.PMST_CONFIG);
					paraDto.setPmky(WXConstants.PMKY_PUBLIC);
					paraDto.setPmco("wxHost");
					paraDto.setPmv1(value);
					String tmpKey = paraDto.getPmst() + "#" + paraDto.getPmky() + "#" + paraDto.getPmco();
					cache.put(tmpKey, paraDto);
				}
			}
			
			{
				String value = SpringContextUtil.getProperty("wx.config.bindAccUrl");
				if (value != null) {
					ParameterDto paraDto = new ParameterDto();
					paraDto.setPmst(WXConstants.PMST_CONFIG);
					paraDto.setPmky(WXConstants.PMKY_URL);
					paraDto.setPmco("bindAccUrl");
					paraDto.setPmv1(value);
					String tmpKey = paraDto.getPmst() + "#" + paraDto.getPmky() + "#" + paraDto.getPmco();
					cache.put(tmpKey, paraDto);
				}
			}
			
			logger.info("初始化参数表 cache.size() = " +cache.size());
		} catch (Exception e) {
			logger.error("initDictionaryList 初始化数据字典失败",e);
		}
		
		tokenDto  = delegate.queryAccessToken();
	}
	
	public static boolean checkTextAnswer(String msg){
		String key = WXConstants.PMST_TEXT + "#" + WXConstants.PMKY_TEXTANSWER + "#"  + msg;
		return getData().containsKey(key);
	}
	
	public static boolean checkImageAnswer(String msg){
		String key = WXConstants.PMST_TEXT + "#" + WXConstants.PMKY_IMAGEANSWER + "#"  + msg;
		return getData().containsKey(key);
	}
	
	public static boolean checkTextEvent(String msg){
		String key = WXConstants.PMST_TEXT + "#" + WXConstants.PMKY_EVENT + "#"  + msg;
		return getData().containsKey(key);
	}
	/**
	 * 20131126 文本消息，语言回复
	 * @param msg
	 * @return
	 */
	public static boolean checkTextVoiceAnswer(String msg){
		String key = WXConstants.PMST_TEXT + "#" + WXConstants.PMKY_VOICEANSWER + "#"  + msg;
		return getData().containsKey(key);
	}
	/**
	 * 20131126 文本消息，音乐回复
	 * @param msg
	 * @return
	 */
	public static boolean checkTextMusicAnswer(String msg){
		String key = WXConstants.PMST_TEXT + "#" + WXConstants.PMKY_MUSICANSWER + "#"  + msg;
		return getData().containsKey(key);
	}
	/**
	 * 20131126 文本消息，视频回复
	 * @param msg
	 * @return
	 */
	public static boolean checkTextVideoAnswer(String msg){
		String key = WXConstants.PMST_TEXT + "#" + WXConstants.PMKY_VIDEOANSWER + "#"  + msg;
		return getData().containsKey(key);
	}
	
	public static boolean checkMenuTextAnswer(String msg){
		String key = WXConstants.PMST_MENU + "#" + WXConstants.PMKY_TEXTANSWER + "#"  + msg;
		return getData().containsKey(key);
	}
	
	public static boolean checkMenuImageAnswer(String msg){
		String key = WXConstants.PMST_MENU + "#" + WXConstants.PMKY_IMAGEANSWER + "#"  + msg;
		return getData().containsKey(key);
	}
	
	public static boolean checkMenuEvent(String msg){
		String key = WXConstants.PMST_MENU + "#" + WXConstants.PMKY_EVENT + "#"  + msg;
		return getData().containsKey(key);
	}
	public static boolean checkMenuCustService(String msg){
		String key = WXConstants.PMST_MENU + "#" + WXConstants.PMKY_CUST_SERVICE + "#"  + msg;
		return getData().containsKey(key);
	}
	public static boolean checkTextCustService(String msg){
		String key = WXConstants.PMST_TEXT + "#" + WXConstants.PMKY_CUST_SERVICE + "#"  + msg;
		return getData().containsKey(key);
	}
	
	public static boolean checkMenuLocation(String msg){
		String key = WXConstants.PMST_MENU + "#" + WXConstants.PMKY_LOACTIONANSWER + "#"  + msg;
		return getData().containsKey(key);
	}
	public static boolean checkMenuView(String msg){
		String key = WXConstants.PMST_TEXT + "#" + WXConstants.PMKY_VIEWANSWER + "#"  + msg;
		return getData().containsKey(key);
	}
	
	  /**20180315 add start  ex-wulj*/
	public static boolean checkTextService(String msg){
			String key = WXConstants.PMST_TEXT + "#" + WXConstants.PMKY_SERVICE_PUSH + "#"  + msg;
			return getData().containsKey(key);
	}
    /**20180315 add end ex-wulj*/
	
	/**
	 * 是不是白名单用户
	 * @param dto
	 * @return
	 */
	public static boolean isWhite(String openId){
		//"CONFIG#TRADEOPER"
		String prefix = WXConstants.PMST_CONFIG+"#"+WXConstants.PMKY_WHITELIST;
		Enumeration iter = getData().keys();
		while (iter.hasMoreElements()) { 
			String key = (String) iter.nextElement();
			if(key.startsWith(prefix)){
				String whiteUser = key.split("#")[2];
				if(openId.equals(whiteUser) || "*".equals(whiteUser)){
					return true;
				}
			} 
		}
		return false;
	}
	
	/**
	 * 无法匹配关键字
	 * @return
	 */
	public static String getDefaultRes(){
		return getValue(WXConstants.PMST_MESSAGE, WXConstants.PMKY_PUBLIC, "NOMATCHWORD");
	}
	
	public static String getUrl(String urlKey){
		return getValue(WXConstants.PMST_CONFIG, WXConstants.PMKY_URL, urlKey);
	}
	
	public static String getValue(String pmst, String pmky, String pmco){
		String key = pmst + "#" + pmky + "#" + pmco;
		Object o = getData().get(key);
		if (o != null) {
			ParameterDto dto = (ParameterDto) o;
			return dto.getPmv1();
		}
		
		return "";
	}
	
	
	/**
	 * 方法说明：判断自定义菜单事件是否需要验证绑定
	 * @param msg
	 */
	public static Map<String, Object> judgeMenu(String msg){
		String key = null;
		int type = 0;
		Map<String, Object> map = new HashMap<String, Object>();
		if(checkMenuTextAnswer(msg)){
			type = 1;
			key = WXConstants.PMST_MENU + "#" + WXConstants.PMKY_TEXTANSWER + "#"  + msg;
		}else if(checkMenuImageAnswer(msg)){
			type = 2;
			key = WXConstants.PMST_MENU + "#" + WXConstants.PMKY_IMAGEANSWER + "#"  + msg;
		}else if(checkMenuEvent(msg)){
			type = 3;
			key = WXConstants.PMST_MENU + "#" + WXConstants.PMKY_EVENT + "#"  + msg;
		}else if(checkMenuCustService(msg)){
			type = 4;
			key = WXConstants.PMST_MENU + "#" + WXConstants.PMKY_CUST_SERVICE + "#"  + msg;
		}else if(checkMenuLocation(msg)){
			type = 5;
			key = WXConstants.PMST_MENU + "#" + WXConstants.PMKY_LOACTIONANSWER + "#"  + msg;
		}else if(checkMenuView(msg)){
			type = 6;
			key = WXConstants.PMST_MENU + "#" + WXConstants.PMKY_VIEWANSWER + "#"  + msg;
		}else{
			logger.warn("未匹配到菜单项....");
			type = 7;
			//无匹配
			key = "other";
		}
		
		ParameterDto paramDto = (ParameterDto) ParameterCache.getData().get(key);
		map.put("type", type);
		map.put("paramDto", paramDto);
		return map;
	}
	
	/**
	 * 方法说明：判断文本事件是否需要验证绑定
	 * @param msg
	 * @return
	 */
	public static Map<String, Object> judgeText(String msg){
		String key = null;
		int type = 0;
		Map<String, Object> map = new HashMap<String, Object>();
		if(checkTextAnswer(msg)){
			type = 1;
			key = WXConstants.PMST_TEXT + "#" + WXConstants.PMKY_TEXTANSWER + "#"  + msg;
		}else if(checkTextCustService(msg)){
			type = 2;
			key = WXConstants.PMST_TEXT + "#" + WXConstants.PMKY_CUST_SERVICE + "#"  + msg;
		}else if(checkTextEvent(msg)){
			type = 3;
			key = WXConstants.PMST_TEXT + "#" + WXConstants.PMKY_EVENT + "#"  + msg;
		}else if(checkTextVoiceAnswer(msg)){
			type = 4;
			key = WXConstants.PMST_TEXT + "#" + WXConstants.PMKY_VOICEANSWER + "#"  + msg;
		}else if(checkTextMusicAnswer(msg)){
			type = 5;
			key = WXConstants.PMST_TEXT + "#" + WXConstants.PMKY_MUSICANSWER + "#"  + msg;
		}else if(checkTextVideoAnswer(msg)){
			type = 6;
			key = WXConstants.PMST_TEXT + "#" + WXConstants.PMKY_VIDEOANSWER + "#"  + msg;
		}else if(checkImageAnswer(msg)){
			type = 7;
			key = WXConstants.PMST_TEXT + "#" + WXConstants.PMKY_IMAGEANSWER + "#"  + msg;
			
		}else if (checkTextService(msg)){
			type = 10;
			key = WXConstants.PMST_TEXT + "#" + WXConstants.PMKY_SERVICE_PUSH + "#"  + (StringUtils.isEmpty(msg)?msg:msg.trim());
		}
		else{
			type = 9;
			//无匹配
			key = "other";
			logger.warn("未匹配到text项....");
		}
		
		ParameterDto paramDto = (ParameterDto) ParameterCache.getData().get(key);
		map.put("type", type);
		map.put("paramDto", paramDto);
		return map;
	}
	
	
}
