package com.cmwa.ecc.business.utils.login;

import java.text.SimpleDateFormat;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.apache.log4j.Logger;

/**
 * <p>
 * Title: 招商基金清算系统AD域验证
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * 
 * <p>
 * Company: FIRSTDATA
 * </p>
 * 
 * @author ZHANGQ
 * @version 1.0
 */

public class ADcheck
{
	private boolean resultStatus;// 返回状态
	private String retcod;// 返回代码
	private String retmsg;// 返回信息
	private static Logger logger = Logger.getLogger(ADcheck.class.getName());
	
	/**
	 * AD域验证
	 * 
	 * @param pLoginid
	 * @param pPassword
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean winADLoginAction(String pLoginid, String pPasswd)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		
		try
		{
			String context_fatory = null;
			String winad_url = null;
			String winad_domain = null;
			
			// 初始化返回状态
			setDefault();// 注意这里，如果是给loginAction调用，则会清掉原来的状态信息
			
			// 读取配置信息
			ADConfig adConfig = new ADConfig();
			context_fatory = adConfig.getJNDI();
			winad_url = adConfig.getURL();
			logger.info("context_fatory:"+context_fatory+" winad_url:"+winad_url);
			winad_domain = adConfig.getDomain();
			//logger.info("pLoginid:"+pLoginid+" winad_domain:"+winad_domain +"  xx:"+pPasswd);
			@SuppressWarnings("rawtypes")
			Hashtable hashtable = new Hashtable();
			hashtable.put(Context.INITIAL_CONTEXT_FACTORY, context_fatory);
			hashtable.put(Context.PROVIDER_URL, winad_url);
			hashtable.put(Context.SECURITY_PRINCIPAL, pLoginid + winad_domain);
			hashtable.put(Context.SECURITY_CREDENTIALS, pPasswd);
			
			DirContext dirContext = new InitialDirContext(hashtable);
			dirContext.close();
			
			setResultStatus(true);
		}
		catch (Exception e)
		{
			retcod = "9999";
			retmsg = "用户名或密码不正确！详细原因：" + e.getMessage();
			
			setResultStatus(false);
			
			System.out.println("域验证失败，原因：" + e.getMessage());
		}
		finally
		{
			logger.info(pLoginid + " 域用户验证_结束：" + simpleDateFormat.format(new java.util.Date()));
		}
		
		return resultStatus;
	}
	
	public boolean isResultStatus()
	{
		return resultStatus;
	}
	
	public void setResultStatus(boolean resultStatus)
	{
		this.resultStatus = resultStatus;
	}
	
	public String getRetcod()
	{
		return retcod;
	}
	
	public void setRetcod(String retcod)
	{
		this.retcod = retcod;
	}
	
	public String getRetmsg()
	{
		return retmsg;
	}
	
	public void setRetmsg(String retmsg)
	{
		this.retmsg = retmsg;
	}
	
	public void setDefault()
	{
		setResultStatus(false);
		setRetcod("");
		setRetmsg("");
	}
}
