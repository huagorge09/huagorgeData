package com.cmwa.ec.weixin.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http post请求工具类
 * @author ex-tuoy
 * 
 */
public class HttpPostUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(HttpPostUtil.class);
	
	 /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.info("1......发送GET请求出现异常！ ",e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                logger.info("2......httplient释放资源异常: ",e2);
            }
        }
        return result;
    }
    
    public static void main(String[] args) {
    	//http://mobsec-dianhua.baidu.com/dianhua_api/open/location?tel=13266826352
    	System.out.println(sendGet("http://mobsec-dianhua.baidu.com/dianhua_api/open/location", "tel=13266826352"));
	}
	
	public static String sendPost(String methodUrl,List<NameValuePair> param,String contentType) {
		String response = "";
		// 创建连接对象
		HttpClient client = new DefaultHttpClient();
		HttpPost post = null;
		try {
			// 建立连接url
			post = new HttpPost(""+SpringContextUtil.getProperty("DECRYPTSERVICE_URL")+"/" + methodUrl);
			post.setEntity(new UrlEncodedFormEntity(param));
			
			// 获得请求后的响应
			HttpResponse httpResponse = client.execute(post);
			// 判断返回的状态值
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				// 如果请求成功，获取报文的实体
				HttpEntity entity = httpResponse.getEntity();
				response = EntityUtils.toString(entity);
			}
		} catch (Exception e1) {
			logger.error("1......远程api调用异常",e1);
		} finally {
			try {
				//释放连接资源
				if (post != null) 
					post.getEntity().getContent().close();
			} catch (Exception e) {
				logger.info("1......httplient释放资源异常: ",e);
			}
			logger.info("1......HttpPostUtil调用api服务post接口返回结果：{}",response);
		}
		return response;
	}
	
	public static String sendPostJsonFormat(String methodUrl,String contentType,String json) {
		String response = "";
		// 创建连接对象
		HttpClient client = new DefaultHttpClient();
		HttpPost post = null;
		try {
			// 建立连接url
			post = new HttpPost(methodUrl);
			//设置content-type
			if(!StringUtils.isEmptyString(contentType)){
				post.addHeader("Content-Type",contentType);
				// 模拟请求
				post.setEntity(new StringEntity(json));
			}
			
			// 获得请求后的响应
			HttpResponse httpResponse = client.execute(post);
			// 判断返回的状态值
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				// 如果请求成功，获取报文的实体
				HttpEntity entity = httpResponse.getEntity();
				response = EntityUtils.toString(entity);
			}
		} catch (Exception e1) {
			logger.error("2......远程api调用异常",e1);
		} finally {
			try {
				//释放连接资源
				if (post != null) 
					post.getEntity().getContent().close();
			} catch (Exception e) {
				logger.info("2......httplient释放资源异常: ",e);
			}
			logger.info("2......HttpPostUtil调用api服务post接口返回结果：{}",response);
		}
		return response;
	}
	
	public static String sendPostToFront(String url,List<NameValuePair> param,String contentType) {
		String response = "";
		// 创建连接对象
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = null;
		try {
			enableSSL(client);
			// 建立连接url
			post = new HttpPost(url);
			UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(param);
			post.setEntity(urlEncodedFormEntity);
			
			// 获得请求后的响应
			HttpResponse httpResponse = client.execute(post);
			// 判断返回的状态值
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				// 如果请求成功，获取报文的实体
				HttpEntity entity = httpResponse.getEntity();
				response = EntityUtils.toString(entity);
			}
		} catch (Exception e) {
			logger.error("远程调用异常",e);
		} finally {
			try {
				//释放连接资源
				if (post != null) 
					post.getEntity().getContent().close();
			} catch (Exception e) {
				logger.error("httplient释放资源异常: ",e);
			}
			logger.info("返回结果:" + response);
		}
		return response;
	}
	
	/**
	 * 访问https的网站
	 * 
	 * @param httpclient
	 */
	private static void enableSSL(DefaultHttpClient httpclient) throws Exception {
		SSLContext sslcontext = SSLContext.getInstance("TLS");
		sslcontext.init(null, new TrustManager[] { truseAllManager }, null);
		SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
		sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		Scheme https = new Scheme("https", sf, 443);
		httpclient.getConnectionManager().getSchemeRegistry().register(https);
	}
	
	private static TrustManager truseAllManager = new X509TrustManager() {

		public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
				throws CertificateException {
			// TODO Auto-generated method stub

		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
				throws CertificateException {
			// TODO Auto-generated method stub

		}

		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			// TODO Auto-generated method stub
			return null;
		}

	};
	
	
	
}
