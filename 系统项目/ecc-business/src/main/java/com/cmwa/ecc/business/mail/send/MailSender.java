package com.cmwa.ecc.business.mail.send;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.cmwa.ec.base.util.SpringUtil;
import com.cmwa.ecc.business.constant.MailConfigConstant;
import com.cmwa.ecc.business.entity.mail.MailAppendDetail;
import com.cmwa.ecc.business.entity.mail.MailAuthenticator;
import com.cmwa.ecc.business.entity.mail.MailConfig;
import com.cmwa.ecc.business.entity.mail.MailMessage;
import com.cmwa.ecc.business.entity.mail.MailSendRecord;
import com.cmwa.ecc.business.entity.mail.SimpleAttachEntry;
import com.cmwa.ecc.business.service.impl.mail.MailServiceImpl;
import com.cmwa.ecc.business.utils.SpringApplicationContextUtil;

/**
 * 业务代码请在子类中实现,禁止加入此类,更不要随意添加抽象方法到此类</br>
 * eg:</br>
 * 1.{@link #getMailSender()} 拿到本类实例 </br>
 * 2.{@link #add(MailMessage)} 或 {@link #add(List)} 放置需要发送的邮件即可</br>
 * 默认发件人是系统配置的
 * 
 * @author ex-luoxy@cmfchina.com
 */
public final class MailSender implements InitializingBean {
	
	private static final LinkedBlockingDeque<MailMessage>	MAIL_DEQUE		= new LinkedBlockingDeque<MailMessage>();
	
	private static final LinkedBlockingDeque<Runnable>		THREAD_QUEUE	= new LinkedBlockingDeque<Runnable>();
	
	private static MailAuthenticator						authenticator	= null;
	
	private static final Properties							prop			= new Properties();
	
	private static Transport								ts				= null;
	
	private static final Logger								logger			= LogManager.getLogger(MailSender.class);
	
	@Resource(name = "threadPoolTaskExecutor")
	private ThreadPoolTaskExecutor exec;
	
	static{
		try{
			prop.setProperty("mail.smtp.host", SpringUtil.getProperty("mail.smtp.host"));
			prop.setProperty("mail.transport.protocol", SpringUtil.getProperty("mail.transport.protocol"));
			prop.setProperty("mail.smtp.auth", SpringUtil.getProperty("mail.smtp.auth"));
			
			authenticator = new MailAuthenticator(SpringUtil.getProperty("mail.smtp.host"), SpringUtil.getProperty("mail.from"), SpringUtil.getProperty("mail.user.username"), SpringUtil.getProperty("mail.user.password"));
		}catch(Exception ex){
			logger.error("init Properties error", ex);
			ex.printStackTrace();
			throw new Error("综合营销平台邮件模块初始化失败!");
		}
		
	}
	
	
	private MailSender() {
		
	}
	
	
	public static MailSender getMailSender() {
		return SpringApplicationContextUtil.getBean(MailSender.class);
	}
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		_bulidTransport();
		_startThread();
		
		//MailMessage m = new MailMessage(new String [] {"ex-luoxy@cmfchina.com"}, new String [] {}, "招财通测试邮件", "这是一封测试邮件", "1");
		// m.setAffixList(Arrays.asList(new SimpleAttachEntry("测试中文.pdf", "D://usr//local//tomcat//downfile//AABB130220160526063726.pdf")));
		 	//this.add(m);	 
	}
	
	
	private void _bulidTransport() throws NoSuchProviderException {
		// 1、创建session
		Session session = Session.getInstance(prop);
		// 开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
		session.setDebug(false);
		// 2、通过session得到transport对象
		ts = session.getTransport();
	}
	
	
	private void _startThread() {
		// 线程池开启
		final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 5, TimeUnit.MINUTES, THREAD_QUEUE) {
			
			protected void afterExecute(Runnable r, Throwable t) {
				super.afterExecute(r, t);
				// 异常统一处理
				try{
					_disposeException(r, t);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		};
		
		// FIXME 改为线程池管理
		exec.submit(new Thread() {
			@Override
			public void run() {
				MailMessage mailMessage = null;
				ProcesserThread processerThread = null;
				while(true){
					try{
						mailMessage = MAIL_DEQUE.take();
						if(mailMessage != null){
							processerThread = new ProcesserThread(mailMessage);
							threadPoolExecutor.submit(processerThread);
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	
	/**
	 * 异常处理
	 * 
	 * @param r
	 * @param t
	 * @throws InterruptedException
	 */
	private void _disposeException(Runnable r, Throwable t) throws InterruptedException {
		if(t == null && r instanceof Future<?>){
			try{
				Future<?> future = (Future<?>) r;
				if(future.isDone())
					t = (Throwable) future.get();
			}catch(ExecutionException ee){
				t = ee.getCause();
			}catch(Exception ie){
				t = ie;
			}
		}
		if(t != null){
			logger.error("thread throw exception [_disposeException] ...", t);
		}
	}
	
	
	// abstract void createMail();
	
	/**
	 * 添加邮件
	 * 
	 * @param mailMessage
	 * @throws InterruptedException
	 */
	public final void add(MailMessage mailMessage) throws InterruptedException {
		try{
			MAIL_DEQUE.put(mailMessage);
		}catch(Exception e){
			logger.info("----MailSender-add-MAIL_DEQUE.put-exce:",e);
		}
	}
	
	/**
	 * 发件人和收件人后台配置
	 * @param configId
	 * @param mailMessage
	 * @throws Exception
	 */
	public final void add(String configId, MailMessage mailMessage) throws Exception{
		MailConfig mailConfig = SpringApplicationContextUtil.getBean(MailServiceImpl.class).queryMailConfig(configId);
		if(null == mailConfig || (null != mailConfig && MailConfigConstant.MAIL_CON_STOP.equalsIgnoreCase(mailConfig.getRunStatus()))){
			return;
		}
		List<MailAppendDetail> toList = SpringApplicationContextUtil.getBean(MailServiceImpl.class).queryMailAppendList(configId, "to");
		List<MailAppendDetail> ccList = SpringApplicationContextUtil.getBean(MailServiceImpl.class).queryMailAppendList(configId, "cc");
		mailMessage.setSubject(mailConfig.getHead());
		mailMessage.setContent(mailConfig.getBody());
		String[] to = new String [toList.size()];
		String[] cc = new String [ccList.size()];
		for(int i = 0; i < toList.size(); i++){
			to[i] = toList.get(i).getMailPath();
		}
		mailMessage.setTo(to);
		for(int i = 0; i < ccList.size(); i++){
			cc[i] = ccList.get(i).getMailPath();
		}
		mailMessage.setCc(cc);
		this.add(mailMessage);
	}
	
	/**
	 * 批量添加注释
	 * 
	 * @param mailMessageList
	 * @throws InterruptedException
	 */
	public final void add(List<MailMessage> mailMessageList) throws InterruptedException {
		for(MailMessage message : mailMessageList){
			try{
				MAIL_DEQUE.put(message);
			}catch(Exception e){
				logger.info("----MailSender-add-MAIL_DEQUE.put-exce:",e);
			}
		}
	}
	
	
	private static class ProcesserThread implements Callable<Void> {
		
		private MailMessage	entity;
		
		
		public ProcesserThread(MailMessage entity) {
			this.entity = entity;
		}
		
		
		private MimeMessage _buildMessage() throws Exception {
			Session session = Session.getInstance(prop);
			MimeMessage message = new MimeMessage(session);
			
			message.setFrom(new InternetAddress(entity.getFrom()));
			// 收件人,抄送人
			if(entity.getTo().length != 0){
				message.setRecipients(Message.RecipientType.TO, _strToAdr(entity.getTo()));
			}
			if(entity.getCc().length != 0){
				message.setRecipients(Message.RecipientType.CC, _strToAdr(entity.getCc()));
			}
			//密送人
			if(entity.getScc().length != 0){
				message.setRecipients(Message.RecipientType.BCC, _strToAdr(entity.getScc()));
			}
			
			message.setSubject(entity.getSubject());
			
			MimeBodyPart text = new MimeBodyPart();
			text.setContent(entity.getContent(), "text/html;charset=UTF-8");
			
			MimeMultipart mp = new MimeMultipart();
			mp.addBodyPart(text);
			
			// 附件
			if(entity.getAffixList().size() > 0){
				for(SimpleAttachEntry entry : entity.getAffixList()){
					MimeBodyPart attach = new MimeBodyPart();
					DataHandler dh = new DataHandler(new FileDataSource(entry.getPath()));
					attach.setDataHandler(dh);
					attach.setFileName(MimeUtility.encodeText(entry.getName()));
					mp.addBodyPart(attach);
				}
			}
			
			mp.setSubType("mixed");
			
			message.setContent(mp);
			message.saveChanges();
			// message.writeTo(new FileOutputStream(SpringUtil.getProperty("mail.path")));
			return message;
		}
		
		
		private InternetAddress[] _strToAdr(String[] arr) throws AddressException {
			if(arr == null || arr.length == 0){
				return new InternetAddress [0];
			}
			/*InternetAddress[] iasTo = new InternetAddress [arr.length];
			int index = 0, length = arr.length;
			for(; index < length; index++){
				iasTo[index] = new InternetAddress(null);
			}*/
			
			List<InternetAddress> iasToList = new ArrayList<InternetAddress>();
			for (int i = 0; i < arr.length; i++) {
				String intAdd = arr[i];
				if(!StringUtils.isEmpty(intAdd)){
					InternetAddress address = null;
					try{
						address = new InternetAddress(intAdd);
						iasToList.add(address);
					}catch(AddressException e){
						logger.info("----_strToAdr-new InternetAddress-AddressExce:",e);
					}
				}
			}
			InternetAddress[] iasTo = new InternetAddress[iasToList.size()];
			try{
				iasTo = iasToList.toArray(iasTo);
			}catch(Exception e){
				logger.error("----_strToAdr-iasToList.toArray-Exception:",e);
			}
			return null == iasTo ? new InternetAddress[0] : iasTo;
		}
		
		
//		@Override
//		public Void call() {
//			// 业务处理 ...
//			String result = "success";
//			String id = null;
//			try{
//				if(ts != null && ts.isConnected()){
//					ts.close();
//				}
//				// 发送人的验证,用户是否自己配置了发件人
//				if(entity.getAuthenticator() != null){
//					ts.connect(entity.getAuthenticator().getHost(), entity.getAuthenticator().getUsername(), entity.getAuthenticator().getPassword());
//					this.entity.setFrom(entity.getAuthenticator().getFrom());
//				}else{
//					ts.connect(authenticator.getHost(), authenticator.getUsername(), authenticator.getPassword());
//					this.entity.setFrom(authenticator.getFrom());
//				}
//				
//				MimeMessage message = _buildMessage();
//				// 添加发送记录
//				id = SpringApplicationContextUtil.getBean(MailServiceImpl.class).addRecord(new MailSendRecord(entity));
//				ts.sendMessage(message, message.getAllRecipients());
//				ts.close();
//			}catch(Exception e){
//				logger.error("thread throw exception ...", e);
//				result = e.toString();
//				e.printStackTrace();
//			}
//			if(id != null)
//				try{
//					// 邮件的结果保存
//					SpringApplicationContextUtil.getBean(MailServiceImpl.class).setResult(id, result);
//				}catch(Exception e){
//					logger.error("thread throw exception [setResult] ...", e);
//					e.printStackTrace();
//				}
//			return null;
//		}
		
		@Override
		// 业务处理 ...
		public Void call() {
			logger.info("start call。。。。。。");
			String result = "success";
			String id = null;
			try{
				if(ts != null && ts.isConnected()){
					ts.close();
				}
				// 发送人的验证,用户是否自己配置了发件人
				if(entity.getAuthenticator() != null){
					ts.connect(entity.getAuthenticator().getHost(), entity.getAuthenticator().getUsername(), entity.getAuthenticator().getPassword());
					this.entity.setFrom(entity.getAuthenticator().getFrom());
				}else if( !StringUtils.isBlank(entity.getPrefix())){
					String prefix = entity.getPrefix();
					String hostkey =     prefix +  ".mail.smtp.host";
					String usernamekey = prefix +  ".mail.user.username";
					String passwordkey = prefix +  ".mail.user.password";
					String fromkey     = prefix +  ".mail.from";
					ts.connect(SpringUtil.getProperty(hostkey),SpringUtil.getProperty(usernamekey),SpringUtil.getProperty(passwordkey));
					this.entity.setFrom(SpringUtil.getProperty(fromkey));
				}else{
					ts.connect(authenticator.getHost(), authenticator.getUsername(), authenticator.getPassword());
					this.entity.setFrom(authenticator.getFrom());
				}
				
				MimeMessage message = _buildMessage();
				// 添加发送记录
				id = SpringApplicationContextUtil.getBean(MailServiceImpl.class).addRecord(new MailSendRecord(entity));
//				id = ((MailManager) SpringUtil.getBean("mailManager")).addRecord(new MailSendRecord(entity));
				ts.sendMessage(message, message.getAllRecipients());
				ts.close();
			}catch(Exception e){
				logger.error("thread throw exception ...", e);
				result = e.toString();
				e.printStackTrace();
			}
			if(id != null)
				try{
					// 邮件的结果保存
					SpringApplicationContextUtil.getBean(MailServiceImpl.class).setResult(id, result);
				}catch(Exception e){
					logger.error("thread throw exception [setResult] ...", e);
					e.printStackTrace();
				}
			return null;
		}
		
	}
}
