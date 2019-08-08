package com.cmwa.ecc.business.utils;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;



public class Utils {

	private static int TIME_OUT_CONNECTION = 10000;
	private static int TIME_OUT_SOCKET = 10000;
	protected static Logger logger = LoggerFactory.getLogger(Utils.class.getName());

	/**
	 * 获取当前时间，精确到微秒
	 * 
	 * @return
	 */
	public static String getNowTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");//
		return df.format(new Date());
	}

	public static String subStr(String sources, String str1, String str2) {
		int position1 = sources.indexOf(str1);
		int position2 = sources.indexOf(str2);
		if (position1 < 0 || position2 < 0) {
			return sources;
		}
		return sources.substring(position1 + str1.length(), position2);
	}

	/**
	 * 获取当前时间，精确到秒
	 * 
	 * @return
	 */
	public static String getNowTimeForSeconds() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//
		return df.format(new Date());
	}

	/**
	 * 获取当前时间字符串
	 * 
	 * @return
	 */
	public static String getNowTimeStr() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//
		return df.format(new Date());
	}

	/**
	 * 获取当前日期字符串
	 * 
	 * @return
	 */
	public static String getNowDateStr() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//
		return df.format(new Date());
	}

	/**
	 * 获取当前日期字符串2
	 * 
	 * @return
	 */
	public static String getNowDateStr2() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//
		return df.format(new Date());
	}
	

	/**
	 * 格式化日期字符串2
	 * 
	 * @return
	 */
	public static String formatDateStr2(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//
		return df.format(date);
	}
	
	public static Date parseDate(String str) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//
		Date d = null;
		try {
			d = df.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return d;
	}
	
	/**
	 * 将字符串解析格式化成指定日期
	 * @param str
	 * @param format
	 * @return
	 */
	public static Date parseDateFmt(String str,String format) {
		if(StringUtils.isEmpty(str)){
			return null;
		}
		SimpleDateFormat df = new SimpleDateFormat(format);//
		Date d = null;
		try {
			d = df.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return d;
	}
	
	/**
	 * 按照格式yyyy/MM/dd解析字符串
	 * @param dateStr
	 * @return 返回1为正确解析 -1为格式不对
	 */
	public static int parseDateString(String dateStr){
		SimpleDateFormat sdf;
		try {
			sdf = new SimpleDateFormat("yyyy/MM/dd");
			sdf.setLenient(false);
			sdf.parse(dateStr);
		} catch (Exception e) {
			return -1;
		}
		return 1;
	}
	
	/**
	 * 获取当天指定日期的时间
	 * 
	 * @return
	 */
	public static Date getFormatHourDate(Date date, int hour, int minite) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minite);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();

	}

	public static Date getFormatMinitue(Date date, int hour, int minite,
			int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minite);
		calendar.set(Calendar.SECOND, second);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();

	}

	/**
	 * 指定日期增加数
	 * 
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date addDay(Date date, int day) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, day);

		return calendar.getTime();

	}

	public static String addDayString(String date, int day) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//
		Date d = null;
		Calendar calendar = Calendar.getInstance();
		try {
			d = df.parse(date);
			calendar.setTime(d);
			calendar.add(Calendar.DAY_OF_MONTH, day);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return df.format(calendar.getTime());

	}
	/**
	 * 日期格式化
	 * 
	 * @param str
	 * @return yyyyMMdd
	 */
	public static String dateFormat(String str) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//
		Date d = null;
		try {
			d = df.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return df.format(d);
	}
	
	/**
	 * 日期格式化
	 * 
	 * @param str
	 * @return yyyyMMdd
	 */
	public static String dateFormatNoWilp(String str) {
		str = str.replace("/","").replace("-", "");
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//
		Date d = null;
		try {
			d = df.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return df.format(d);
	}
	/**
	 * 日期格式化
	 * 
	 * @param str
	 * @return yyyy/MM/dd
	 */
	public static String dateFormat2(String str) {
		str = str.replace("/","").replace("-", "");
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy/MM/dd");//
		Date d = null;
		try {
			d = df.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return df2.format(d);
	}
	/**
	 * 日期格式化
	 * 
	 * @param str
	 * @return yyyy-MM-dd
	 */
	public static String dateFormat3(String str) {
		str = str.replace("/","").replace("-", "");
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");//
		Date d = null;
		try {
			d = df.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return df2.format(d);
	}
	
	/**
	 * 获取当前时间戳
	 * 
	 * @return
	 */
	public static long getTimestamp() {
		long timstamp = 0;
		try {
			timstamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
					.parse(getNowTime()).getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return timstamp;
	}

	public static String getFormatDouble(Double d, String format) {
		DecimalFormat df = new DecimalFormat("0");

		return df.format(d);

	}

	/**
	 * 文件上传
	 * 
	 * @return
	 */
	public static String fileUpload(String requestUrl, MultipartEntity reqEntity) {
		StringBuilder sb = new StringBuilder();
		try {
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					TIME_OUT_CONNECTION);
			HttpConnectionParams.setSoTimeout(httpParameters, TIME_OUT_SOCKET);
			HttpClient httpClient = new DefaultHttpClient(httpParameters);
			HttpPost httpPost = new HttpPost(requestUrl);

			httpPost.setEntity(reqEntity);
			HttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {

				BufferedReader bufferedReader2 = new BufferedReader(
						new InputStreamReader(response.getEntity().getContent()));
				for (String s = bufferedReader2.readLine(); s != null; s = bufferedReader2
						.readLine()) {
					sb.append(s);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * post请求
	 * 
	 * @param requestUrl
	 * @param nameValuePair
	 * @return
	 */
	public static String postRequest(String requestUrl,
			List<NameValuePair> nameValuePair) {
		String result = null;
		StringBuilder sb = new StringBuilder();
		try {
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					TIME_OUT_CONNECTION);
			HttpConnectionParams.setSoTimeout(httpParameters, TIME_OUT_SOCKET);
			HttpClient httpClient = new DefaultHttpClient(httpParameters);
			HttpPost httpPost = new HttpPost(requestUrl);
			if (null != nameValuePair) {
				MultipartEntity reqEntity = new MultipartEntity();
				for (NameValuePair nvp : nameValuePair) {
					if (nvp != null) {
						if (!StringUtils.isEmpty(nvp.getValue())) {
							StringBody sby = new StringBody(nvp.getValue(),
									Charset.forName("utf-8"));
							reqEntity.addPart(nvp.getName(), sby);
						}
					}

				}
				httpPost.setEntity(reqEntity);
			}
			HttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				BufferedReader bufferedReader2 = new BufferedReader(
						new InputStreamReader(
								response.getEntity().getContent(), "utf-8"));
				for (String s = bufferedReader2.readLine(); s != null; s = bufferedReader2
						.readLine()) {
					sb.append(s);
				}
			}
			result = sb.toString();
		} catch (Exception e) {
			result = "error";
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * post请求https
	 * 
	 * @param requestUrl
	 * @return
	 */
	public static String sendSSLPostRequest(String reqURL,
			List<NameValuePair> nameValuePair) {
		long responseLength = 0; // 响应长度
		String responseContent = null; // 响应内容
		HttpClient httpClient = new DefaultHttpClient(); // 创建默认的httpClient实例
		X509TrustManager xtm = new X509TrustManager() { // 创建TrustManager
			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		try {
			// TLS1.0与SSL3.0基本上没有太大的差别，可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext
			SSLContext ctx = SSLContext.getInstance("TLS");

			// 使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用
			ctx.init(null, new TrustManager[] { xtm }, null);

			// 创建SSLSocketFactory
			SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);

			// 通过SchemeRegistry将SSLSocketFactory注册到我们的HttpClient上
			httpClient.getConnectionManager().getSchemeRegistry()
					.register(new Scheme("https", 443, socketFactory));

			HttpPost httpPost = new HttpPost(reqURL); // 创建HttpPost
			// List<NameValuePair> formParams = new ArrayList<NameValuePair>();
			// //构建POST请求的表单参数
			// for(Map.Entry<String,String> entry : params.entrySet()){
			// formParams.add(new BasicNameValuePair(entry.getKey(),
			// entry.getValue()));
			// }
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));

			HttpResponse response = httpClient.execute(httpPost); // 执行POST请求
			HttpEntity entity = response.getEntity(); // 获取响应实体

			if (null != entity) {
				responseLength = entity.getContentLength();
				responseContent = EntityUtils.toString(entity, "UTF-8");
				EntityUtils.consume(entity); // Consume response content
			}
			System.out.println("请求地址: " + httpPost.getURI());
			System.out.println("响应状态: " + response.getStatusLine());
			System.out.println("响应长度: " + responseLength);
			System.out.println("响应内容: " + responseContent);
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown(); // 关闭连接,释放资源
			return responseContent;
		}
	}

	/**
	 * 是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * MD5
	 * 
	 * @param source
	 * @return
	 */
	public static String md5(String source) {
		StringBuffer sb = new StringBuffer(32);
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] array = md.digest(source.getBytes("utf-8"));

			for (int i = 0; i < array.length; i++) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
						.toUpperCase().substring(1, 3));
			}
		} catch (Exception e) {
			return null;
		}
		return sb.toString();
	}

	/**
	 * 格式化double 为小数两位数
	 * 
	 * @param sum
	 * @return
	 */
	public static String formatDouble(Double sum) {
		DecimalFormat df = new DecimalFormat("#.00");
		return df.format(sum);
	}

	/**
	 * 格式化double 为小数两位数，返回double
	 * 
	 * @param sum
	 * @return
	 */
	public static Double formatDoubleForDouble(Double sum) {
		DecimalFormat df = new DecimalFormat("#.00");
		return Double.parseDouble(df.format(sum));
	}

	/**
	 * 获取从今天开始到下几个月的下一天
	 * 
	 * @param monthSum
	 *            取下几个月
	 * @return
	 */
	public static String getNextMonthNextDay(int monthSum) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Date curDate = new Date();
		calendar.setTime(curDate);
		// 取得现在时间
		// System.out.println(sdf.format(curDate));
		// 取得上一个时间
		calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY) + monthSum);
		// 取得下一个月的下一天
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.get(Calendar.DAY_OF_MONTH) + 1);
		// System.out.println(sdf.format(calendar.getTime()));
		return sdf.format(calendar.getTime());
	}

	/**
	 * 获取某天的下个月的下一天
	 * 
	 * @param monthSum
	 * @return
	 */
	public static String getDayNextMonthNextDay(String date, int monthSum) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		try {
			// Date curDate = new Date();
			calendar.setTime(sdf.parse(date));
			// 取得现在时间
			// System.out.println(sdf.format(curDate));
			// 取得上一个时间
			calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY)
					+ monthSum);
			// 取得下一个月的下一天
			calendar.set(Calendar.DAY_OF_MONTH,
					calendar.get(Calendar.DAY_OF_MONTH) + 1);
			// System.out.println(sdf.format(calendar.getTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sdf.format(calendar.getTime());
	}

	/**
	 * 获取某天的下几个月的某天
	 * 
	 * @param date
	 * @param monthSum
	 * @return
	 */
	public static String getNextMonthCurrentDay(String date, int monthSum) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		try {
			// Date curDate = new Date();
			calendar.setTime(sdf.parse(date));
			// 取得现在时间
			// System.out.println(sdf.format(curDate));
			// 取得上一个时间
			// calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY) +
			// monthSum);
			// 取得下一个月的当天
			// calendar.set(Calendar.DAY_OF_MONTH,
			// calendar.get(Calendar.DAY_OF_MONTH));
			// System.out.println(sdf.format(calendar.getTime()));

			calendar.add(Calendar.MONTH, monthSum);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sdf.format(calendar.getTime());
	}
	
	
	
	public static String getProjectEndDate(String date, int monthSum) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		try {
			// Date curDate = new Date();
			calendar.setTime(sdf.parse(date));
			// 取得现在时间
			// System.out.println(sdf.format(curDate));
			// 取得上一个时间
			// calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY) +
			// monthSum);
			// 取得下一个月的当天
			// calendar.set(Calendar.DAY_OF_MONTH,
			// calendar.get(Calendar.DAY_OF_MONTH));
			// System.out.println(sdf.format(calendar.getTime()));

			calendar.add(Calendar.MONTH, monthSum);
			
			calendar.add(Calendar.DAY_OF_MONTH, -1);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sdf.format(calendar.getTime());
	}
	
	

	/**
	 * 获取从某天开始到下几天
	 * 
	 * @param daySum
	 * @return
	 */
	public static String getDayNextDay(String date, int daySum) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		try {
			// Date curDate = new Date();
			calendar.setTime(sdf.parse(date));
			calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + daySum);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sdf.format(calendar.getTime());
	}

	/**
	 * 获取从今天开始到下一天
	 * 
	 * @param monthSum
	 *            取下几天
	 * @return
	 */
	public static String getNextDay(int daySum) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Date curDate = new Date();
		calendar.setTime(curDate);
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + daySum);
		return sdf.format(calendar.getTime());
	}

	public static String addPoint(String str) {
		String a = str;
		if (str.substring(str.lastIndexOf(".") + 1, str.length()).length() == 1) {
			a = str + "0";
		}
		return a;
	}

	/**
	 * 获取两日期的相隔天数
	 * 
	 * @param start
	 *            yyyy-MM-dd
	 * @param end
	 *            yyyy-MM-dd
	 * @return
	 */
	public static Long getDaysBetween(String start, String end) {
		long betweenDate = 0;
		try {
			java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(
					"yyyy-MM-dd");
			java.util.Date startDate = df.parse(start);
			java.util.Date endDate = df.parse(end);
			betweenDate = (endDate.getTime() - startDate.getTime())
					/ (24 * 60 * 60 * 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return betweenDate;
	}

	/**
	 * 获取两日期相隔月数
	 * 
	 * @param begin
	 * @param end
	 * @return
	 */
	public static int getDiffer(String begin, String end) {
		int difMonth = 0;
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date beginDate = df.parse(begin);
			Date endDate = df.parse(end);
			int beginYear = beginDate.getYear();
			int beginMonth = beginDate.getMonth();
			int endYear = endDate.getYear();
			int endMonth = endDate.getMonth();
			difMonth = (endYear - beginYear) * 12 + (endMonth - beginMonth);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return difMonth;
	}

	/**
	 * 获取某日期的最后一天日期
	 * 
	 * @param invoiceMonth
	 *            格式：201108
	 * @return
	 */
	public static String getInvoiceMonth(String invoiceMonth) {// invoiceMonth
																// 的格式为 201108
		String str = "";
		try {
			String year = invoiceMonth.substring(0, 4);
			String month = invoiceMonth.substring(4, invoiceMonth.length());
			Calendar date = Calendar.getInstance();
			int yeari = Integer.parseInt(year);
			int monthi = Integer.parseInt(month);
			// if(monthi==1){
			// monthi=12;
			// }
			date.set(yeari, monthi - 1, 1);
			int maxDayOfMonth = date.getActualMaximum(Calendar.DAY_OF_MONTH);
			str = year + "-" + monthi + "-" + maxDayOfMonth;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
	public static String getInvoiceMonth1(String invoiceMonth) {// invoiceMonth
		// 的格式为 201108
		String str = "";
			try {
				String year = invoiceMonth.substring(0, 4);
				String month = invoiceMonth.substring(4, invoiceMonth.length());
				Calendar date = Calendar.getInstance();
				int yeari = Integer.parseInt(year);
				int monthi = Integer.parseInt(month);
				String monthiStr = "";
			// if(monthi==1){
			// monthi=12;
			// }
				date.set(yeari, monthi - 1, 1);
				int maxDayOfMonth = date.getActualMaximum(Calendar.DAY_OF_MONTH);
				if(monthi < 10){
					monthiStr = ("0" + Integer.valueOf(monthi).toString());
				}else{
					monthiStr = Integer.valueOf(monthi).toString();
				}
				str = year + "-" + monthiStr + "-" + maxDayOfMonth;
			} catch (Exception e) {
				e.printStackTrace();
			}
		return str;
	}
	
	
	/**
	 * 获取某日期的第一天日期
	 * 
	 * @param invoiceMonth
	 *            格式：201108
	 * @return
	 */
	public static String getInvoiceMonthFirstDay(String invoiceMonth) {// invoiceMonth
		// 的格式为 201108
		String str = "";
		try {
			String year = invoiceMonth.substring(0, 4);
			String month = invoiceMonth.substring(4, invoiceMonth.length());
			Calendar date = Calendar.getInstance();
			int yeari = Integer.parseInt(year);
			int monthi = Integer.parseInt(month);
			String monthiStr;
			if(monthi < 10){
				monthiStr = ("0" + Integer.valueOf(monthi).toString());
			}else{
				monthiStr = Integer.valueOf(monthi).toString();
			}
			date.set(yeari, monthi - 1, 1);
			int maxDayOfMonth = date.getActualMinimum(Calendar.DAY_OF_MONTH);
			str = year + "-" + monthiStr + "-" + maxDayOfMonth;
		} catch (Exception e) {
			e.printStackTrace();
		}
			return str;
	}

	/**
	 * 当月第一天
	 * 
	 * @return
	 */
	public static String getFirstDay() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Date theDate = calendar.getTime();
		GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
		gcLast.setTime(theDate);
		gcLast.set(Calendar.DAY_OF_MONTH, 1);
		String day_first = df.format(gcLast.getTime());
		// StringBuffer str = new
		// StringBuffer().append(day_first).append(" 00:00:00");
		System.out.println("当月第一天：" + day_first);
		return day_first;
	}

	/**
	 * 当月最后一天
	 * 
	 * @return
	 */
	public static String lastDayOfMonth(String da) {
		String d = null;
		try {
			Date date = new SimpleDateFormat("yyyy-MM-dd").parse(da);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.roll(Calendar.DAY_OF_MONTH, -1);
			// d = cal.getTime();
			d = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return d;
	}

	/**
	 * 两日期时间相差的秒数
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static Long getDateBetweenSecond(String start, String end) {
		long betweenDate = 0;
		try {
			java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			java.util.Date startDate = df.parse(start);
			java.util.Date endDate = df.parse(end);
			betweenDate = (endDate.getTime() - startDate.getTime()) / 1000;
		} catch (Exception e) {
			logger.info("**** ERROR 两日期时间相差的秒数 Utils.getDateBetweenSecond "
					+ e.getMessage());
			e.printStackTrace();
		}
		return betweenDate;
	}

	/**
	 * 生成唯一码
	 * 
	 * @return
	 */
	public static String getUUID() {
		return java.util.UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * jsp静态化
	 * 
	 * @param sUrl
	 *            请求路径
	 * @param charset
	 *            字符编码
	 * @param sSavePath
	 *            html文件保存路径
	 * @param sHtmlFile
	 *            html文件名
	 */
	public static int convert2Html(String sUrl, String charset,
			String sSavePath, String sHtmlFile) {
		int result = 0;
		try {
			int HttpResult;
			URL url = new URL(sUrl);
			URLConnection urlconn = url.openConnection();
			urlconn.connect(); // 使用 connect 方法建立到远程对象的实际连接
			HttpURLConnection httpconn = (HttpURLConnection) urlconn;
			HttpResult = httpconn.getResponseCode();
			if (HttpResult != HttpURLConnection.HTTP_OK) {

			} else {
				InputStreamReader isr = new InputStreamReader(
						httpconn.getInputStream(), charset);
				BufferedReader in = new BufferedReader(isr);
				String inputLine;
				if (!sSavePath.endsWith("/")) {
					sSavePath += "/";
				}
				FileOutputStream fout = new FileOutputStream(sSavePath
						+ sHtmlFile);
				while ((inputLine = in.readLine()) != null) {
					fout.write((inputLine).getBytes());
				}
				in.close();
				fout.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = -1;
		}
		return result;
	}


	public static String getCommand(String httpUrl, String pdfPath) {

		String system = System.getProperty("os.name");

		if (system.indexOf("Windows") != -1) {
			return "c:/test/wkhtmltopdf.exe " + httpUrl + " " + pdfPath;
		} else {
			return "/home/tools/pdf/wkhtmltox-0.12.1-61cda93/bin/wkhtmltopdf "
					+ httpUrl + " " + pdfPath;
		}

	}
	/**
	 *  获取流水号，不足补零
	 * @param 基数	
	 * @param 取多少位
	 * @return
	 */
	public static String getFlowNum(int baseNumber,int len){
		String zero = "0000000000000000000000000000";
		String baseNumStr = zero+String.valueOf(baseNumber);
		if(len < baseNumStr.length()){
			baseNumStr = baseNumStr.substring(baseNumStr.length()-len, baseNumStr.length());
		}
		return baseNumStr;
	}
	
	/**
	 * 获取合同上传，下载目录
	 * 
	 * @return
	 */
	public static String getContractPath() {

		String system = System.getProperty("os.name");

		if (system.indexOf("Windows") != -1) {
			return "c:/contract/";
		} else {
			return SysConf.get(
					"investor.contract.pdf.path");

		}
	}

	 public static String genRandomNum(int pwd_len){
		  //35是因为数组是从0开始的，26个字母
		  final int  maxNum = 26;
		  int i;  //生成的随机数
		  int count = 0; //生成的密码的长度
		  char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
		    'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
		    'x', 'y', 'z' };
		  
		  StringBuffer pwd = new StringBuffer("");
		  Random r = new Random();
		  while(count < pwd_len){
		   //生成随机数，取绝对值，防止生成负数，
		   
		   i = Math.abs(r.nextInt(maxNum));  //生成的数最大为36-1
		   
		   if (i >= 0 && i < str.length) {
		    pwd.append(str[i]);
		    count ++;
		   }
		  }
		  
		  return pwd.toString();
		 }
	 
	 
	/**
	 * 字符串转MAP
	 * @param str  入参格式: key1:val1,key2:val2,key3:val3
	 * @return
	 */
	 public static Map<String, String> stringToMap(String str){
		 Map<String,String> map = new HashMap<String,String>(); 
		 if(str != null && str.length()>0){
			 String[] strArr = str.split(",");
			 for(int i=0;i<strArr.length;i++){
				 String[] kvs = strArr[i].split(":");	//拆成key 和value 的组合
				 if(kvs[0] != null){
					 map.put(kvs[0], kvs.length>1?kvs[1]:"");
				 }
			 }
		 }
		 return map;
	 }
	 
	 /**
		 * 格式化数字
		 * @param month
		 * @return
		 */
		public static String toFormatNum(String number){
			String value =null;
			if(WaStringUtil.IsNotNullAndEmpty(number) == true){
				try {
				 DecimalFormat df1 = new DecimalFormat("###.00");  //四舍五入保留两位小数点。
				 DecimalFormat df2 = new DecimalFormat(",###");   //每三位逗号隔开
				 String frt  = df1.format(Double.parseDouble(number));
				 //进行分割，转换成每三位逗号隔开
				 String [] spl = frt.split("\\."); //.与| 需转义。
				 value = df2.format(Long.parseLong("".equals(spl[0])?"0":spl[0]))+"."+spl[1];
				} catch (Exception e) {
						return number;
				}
			}
			return value;
		}
		
		/**
		 * 返回两个日期之间的天数
		 * @return
		 * @throws java.text.ParseException 
		 */
		public static String getDayQtysBetweenTwoDate(String beginDate, String endDate) throws java.text.ParseException {
			if (org.apache.commons.lang.StringUtils.isBlank(beginDate) || 
					org.apache.commons.lang.StringUtils.isBlank(endDate)) {
				return "0";
			}
			
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			String beginDateStr = beginDate.replaceAll("\\D", "");
			String endDateStr = endDate.replaceAll("\\D", "");
			Date newBeginDate = format.parse(beginDateStr);
			Date newEndDate = format.parse(endDateStr);
			return String.valueOf((newEndDate.getTime() - newBeginDate.getTime()) /1000/60/60/24);
		}
		
		
		/**
		 * 两个double类型相加
		 * @param num1            被加数 
		 * @param num2            加数
		 * @param precision 小数位数，-1为无精度要求
		 * @return
		 */
		public static double addDouble(double num1, double num2, int precision){
			return calculateDouble(num1, num2, precision, "add");
		}
		
		/**
		 * 两个double类型相减
		 * @param num1            被减数
		 * @param num2            减数
		 * @param precision 小数位数，-1为无精度要求
		 * @return
		 */
		public static double subtractDouble(double num1, double num2, int precision){
			return calculateDouble(num1, num2, precision, "subtract");
		}
		
		/**
		 * 两个double类型相乘
		 * @param num1             被乘数
		 * @param num2             乘数
		 * @param precision 小数位数，-1为无精度要求
		 * @return
		 */
		public static double multiplyDouble(double num1, double num2, int precision){
			return calculateDouble(num1, num2, precision, "multiply");
		}
		
		/**
		 * 两个double类型相加除
		 * @param num1             被除数
		 * @param num2             除数
		 * @param precision 小数位数（必须填写）
		 * @return
		 */
		public static double divideDouble(double num1, double num2, int precision){
			return calculateDouble(num1, num2, precision, "divide");
		}
		
		/**
		 * double类型计算方法
		 * @param num1
		 * @param num2
		 * @param precision
		 * @param method
		 * @return
		 */
		private static double calculateDouble(double num1, double num2, int precision, String method) {
			BigDecimal bd1 = new BigDecimal(num1);
			BigDecimal bd2 = new BigDecimal(num2);
			BigDecimal result = null;
			if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(method, "add")) {
				result = bd1.add(bd2);
			} else if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(method, "subtract")) {
				result = bd1.subtract(bd2);
			} else if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(method, "multiply")) {
				result = bd1.multiply(bd2);
			} else if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(method, "divide")) {
				result = bd1.divide(bd2, precision, BigDecimal.ROUND_HALF_UP);
			} else {
				return 0.0;
			}
			if (precision == -1) {
				return result.doubleValue();
			}
			return result.setScale(precision, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		
		
		/**
		 * 日期计算
		 * @param calDate  需要计算的基准日期（无格式要求）
		 * @param addDays  增量
		 * @param addType  date：增加天数，month：增加月份，year：增加年份
		 * @param format   需要返回的日期的格式
		 * @return
		 * @throws java.text.ParseException
		 */
		public static String addDays(String calDate, int addDays, String addType, String format) throws java.text.ParseException {
			SimpleDateFormat simpleFormat1 = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat simpleFormat2 = new SimpleDateFormat(format);
			if (org.apache.commons.lang.StringUtils.isBlank(calDate) || addDays == 0 || 
					org.apache.commons.lang.StringUtils.isBlank(addType)) {
				return "";
			}
			if (org.apache.commons.lang.StringUtils.isBlank(format)) {
				format = "yyyy-MM-dd";
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(simpleFormat1.parse(calDate.replaceAll("\\D", "")));
			if (org.apache.commons.lang.StringUtils.equals(addType, "date")) {
				cal.add(Calendar.DATE, addDays);
			} else if (org.apache.commons.lang.StringUtils.equals(addType, "month")) {
				cal.add(Calendar.MONTH, addDays);
			} else if (org.apache.commons.lang.StringUtils.equals(addType, "year")) {
				cal.add(Calendar.YEAR, addDays);
			}
			
			Date date = cal.getTime();
			return simpleFormat2.format(date);
		}
		
		public static void main(String[] args) {
			System.out.println(getFlowNum(89,8));
		}
		
		/**
		 * 计算两个日期相隔的天数  计算是第一个参数减去第二个参数<br/>
		 * firstString - secondString<br/>
		 * @param firstString 开始日期 yyyy/MM/dd
		 * @param secondString 结束日期 yyyy/MM/dd
		 * @return 返回null则运行异常
		 */
	    public static Integer nDaysBetweenTwoDate(String firstString,String secondString){  
	        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");  
	        Date firstDate=null;  
	        Date secondDate=null;  
	        try{  
	            firstDate = df.parse(firstString);  
	            secondDate = df.parse(secondString);  
	        }  
	        catch(Exception e){  
	        	return null; 
	        }  
	 
	        int nDay=(int)((firstDate.getTime()-secondDate.getTime())/(24*60*60*1000));  
	        return nDay;
	    }
	    
	 
	    
	    public static Integer nDaysBetweenTwoDateNew(String firstString,String secondString){  
	        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
	        Date firstDate=null;  
	        Date secondDate=null;  
	        try{  
	            firstDate = df.parse(firstString);  
	            secondDate = df.parse(secondString);  
	        }  
	        catch(Exception e){  
	        	return null; 
	        }  
	 
	        int nDay=(int)((firstDate.getTime()-secondDate.getTime())/(24*60*60*1000));  
	        return nDay;
	    }
	    
	    public static Integer nDaysBetweenTwoDateNewOne(String firstString,String secondString){  
	        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");  
	        Date firstDate=null;  
	        Date secondDate=null;  
	        try{  
	            firstDate = df.parse(firstString);  
	            secondDate = df.parse(secondString);  
	        }  
	        catch(Exception e){  
	        	return null; 
	        }  
	 
	        int nDay=(int)((firstDate.getTime()-secondDate.getTime())/(24*60*60*1000));  
	        return nDay;
	    } 
	    
	    public static boolean IsNotNullAndEmpty(Object obj){
			if(obj == null )			
				return false;
			if(obj.toString().trim().length() == 0)
				return false;
			return true;
		}	
	    
	    /**
		 * 金额格式转换大些
		 * 
		 * @param value
		 * @return
		 */
		public static String changeToBig(double value) {
			if (value == 0) {
				return "零元整";
			}
			char[] hunit = { '拾', '佰', '仟' }; // 段内位置表示
			//char[] vunit = { '万', '亿', '万亿' }; // 段名表示
			String[] vunit = new String[] { "万", "亿", "兆"}; 
			char[] digit = { '零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖' }; // 数字表示
			long midVal = (long) (value * 100); // 转化成整形
			String valStr = String.valueOf(midVal); // 转化成字符串
			if(valStr.length()==1){
			   valStr="0"+valStr;
			}
			String head = valStr.substring(0, valStr.length() - 2); // 取整数部分
			String rail = valStr.substring(valStr.length() - 2); // 取小数部分

			String prefix = ""; // 整数部分转化的结果
			String suffix = ""; // 小数部分转化的结果

			if (valStr.length() > 17) {
				return "数值过大！";// 解决问题1,超过千亿的问题。
			}
			
			// 处理小数点后面的数
			if (rail.equals("00")) { // 如果小数部分为0
				suffix = "整";
			}else if( "0".equals(rail.substring(0,1))){
				suffix =  digit[rail.charAt(1) - '0'] + "分"; // 否则把角分转化出来
			} else {
				suffix = digit[rail.charAt(0) - '0'] + "角"
						+ digit[rail.charAt(1) - '0'] + "分"; // 否则把角分转化出来
			}

			// 处理小数点前面的数
			char[] chDig = head.toCharArray(); // 把整数部分转化成字符数组
			char zero = '0'; // 标志'0'表示出现过0
			byte zeroSerNum = 0; // 连续出现0的次数
			for (int i = 0; i < chDig.length; i++) { // 循环处理每个数字
				int idx = (chDig.length - i - 1) % 4; // 取段内位置
				int vidx = (chDig.length - i - 1) / 4; // 取段位置
				if (chDig[i] == '0') { // 如果当前字符是0
					zeroSerNum++; // 连续0次数递增
					if (zero == '0' && idx != 0) { // 标志 ,连续零，仅读一次零，
						zero = digit[0]; // 解决问题2,当一个零位于第0位时，不输出“零”，仅输出“段名”.
					} else if (idx == 0 && vidx > 0 && zeroSerNum < 4) {
						prefix += vunit[vidx - 1];
						zero = '0';
					}
					continue;
				}
				zeroSerNum = 0; // 连续0次数清零
				if (zero != '0') { // 如果标志不为0,则加上,例如万,亿什么的
					prefix += zero;
					zero = '0';
				}

				// 取到该位对应数组第几位。
				int position = chDig[i] - '0';
				if (position == 1 && i == 0 && idx == 1) { // 解决问题3
					// ,即处理10读"拾",而不读"壹拾"
				} else {
					prefix += digit[position]; // 转化该数字表示
				}

				if (idx > 0) { // 段内位置表示的值
					prefix += hunit[idx - 1];
				}
				if (idx == 0 && vidx > 0) { // 段名表示的值
					prefix += vunit[vidx - 1]; // 段结束位置应该加上段名如万,亿
				}
			}

			if (prefix.length() > 0) {
				prefix += '元'; // 如果整数部分存在,则有圆的字样
			}

			return prefix + suffix; // 返回正确表示
		}
		
		/*
		 * 将yyyy-MM-dd或yyyy/MM/dd或yyyyMMdd转换成yyyy年MM月dd日
		 */
		public static String convertChineseDate(String dateStr, String type) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			if("-".equals(type)){
				 sdf = new SimpleDateFormat("yyyy-MM-dd");
			}else if("/".equals(type)){
				 sdf = new SimpleDateFormat("yyyy/MM/dd");
			}else if("".equals(type)){
				 sdf = new SimpleDateFormat("yyyyMMdd");
			}
			 
			Date date;
			int year = 0;
			int month = 0;
			int day = 0;
			try {
				date = sdf.parse(dateStr);
				Calendar cl = Calendar.getInstance();
				cl.setTime(date);
				year = cl.get(Calendar.YEAR);
				month = cl.get(Calendar.MONTH)+1;
				day = cl.get(Calendar.DAY_OF_MONTH);
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return year+"年"+month+"月" +day+"日";

		}
		
		 /**
	     * 根据月份,得到季度数
	     * @param month(月份)
	     * @return season(季度)
	     */
	    public static int getSeason(int month){
	    	int intMon = month;
	    	int season = 0 ;
	    	if(month<0 || month>12){
	    		return season;
	    	}
	    	if(intMon%3==0){					//当月份可以整除3,则当月为3,6,9
	    		season = intMon/3;				//月份/3 =季度
			}else{
				season = (intMon+(3-intMon%3))/3;
			}
	    	return season;
	    }
	  
		  /*
		   * 将数字转化成中文
		   * 
		   */
	    
        public static String convertNumberToChinese(String num) 
		    { 
			  
			    String[] numArr = new String[] {"零", "一","二","三","四","五","六","七","八","九"}; 
	            //位 数组 
	            String[] digitArr = new String[] {"", "十", "百", "千"}; 
	            //单位 数组 
	            String[] unitArr = new String[] {"", "万", "亿", "万亿"}; 

		        String str = ""; //返回值 
		        int p = 0; //字符位置指针 
		        int m = num.length() % 4; //取模 
		 
		        // 四位一组得到组数 
		        int k = (m > 0 ? num.length() / 4 + 1 : num.length() / 4); 
		 
		        // 外层循环在所有组中循环 
		        // 从左到右 高位到低位 四位一组 逐组处理 
		        // 每组最后加上一个单位: "[万亿]","[亿]","[万]" 
		        for (int i = k; i > 0; i--) 
		        { 
		            int L = 4; 
		            if (i == k && m != 0) 
		            { 
		                L = m; 
		            } 
		            // 得到一组四位数 最高位组有可能不足四位 
		            String s = num.substring(p, p + L); 
		            int l = s.length(); 
		 
		            // 内层循环在该组中的每一位数上循环 从左到右 高位到低位 
		            for (int j = 0; j < l; j++) 
		            { 
		                //处理改组中的每一位数加上所在位: "仟","佰","拾",""(个) 
		                int n = java.lang.Integer.parseInt(s.substring(j, j+1)); 
		                if (n == 0) 
		                { 
		                    if ((j < l - 1) 
		                        && (java.lang.Integer.parseInt(s.substring(j + 1, j + 1+ 1)) > 0) //后一位(右低) 
		                        && !str.endsWith(numArr[n])) 
		                    { 
		                    	str += numArr[n]; 
		                    } 
		                } 
		                else 
		                { 
		                    //处理 1013 一千零"十三", 1113 一千一百"一十三" 
		                    if (!(n == 1 && (str.endsWith(numArr[0]) | str.length() == 0) && j == l - 2)) 
		                    { 
		                    	str += numArr[n]; 
		                    } 
		                    str +=  digitArr[l - j - 1]; 
		                } 
		            } 
		            p += L; 
		            // 每组最后加上一个单位: [万],[亿] 等 
		            if (i < k) //不是最高位的一组 
		            { 
		                if (java.lang.Integer.parseInt(s) != 0) 
		                { 
		                    //如果所有 4 位不全是 0 则加上单位 [万],[亿] 等 
		                	str += unitArr[i - 1]; 
		                } 
		            } 
		            else 
		            { 
		                //处理最高位的一组,最后必须加上单位 
		            	str += unitArr[i - 1]; 
		            } 
		        } 
		        return str; 
		   } 
        
       
        
        /**
         * 把"a,b,c"转化成"'a','b','c'"
         * @param str
         * @return
         */
        public static String formatStr(String parameter){
        	if(org.apache.commons.lang.StringUtils.isBlank(parameter)){
        		return "";
        	}
        	String str [] = parameter.split(",");
    		boolean flag = false;
    		StringBuilder builder = new StringBuilder();
    		for (int i = 0; i < str.length; i++) {
    			if(org.apache.commons.lang.StringUtils.isNotBlank(str[i].trim())){
    				flag = true;
    				builder.append("'").append(str[i]).append("',");
    			}
    		}
    		if(flag){
    			return builder.substring(0, builder.length()-1);
    		}
    		return "";
        }
        
        public static String divisionPO(String value){
        	if (value.startsWith("PO_")) {
				return value.substring(3);
			}
			return value;
        }
        
        public static int compareStrDate(String sd1, String sd2)
        {
          Date d1 = parseDate(sd1);
          Date d2 = parseDate(sd2);
          return d1.compareTo(d2);
        }

        
        
        public static Date strToDate(String pstrValue, String pstrDateFormat)
        {
          if ((pstrValue == null) || (pstrValue.equals("")))
          {
            return null;
          }
          Date dttDate = null;
          try
          {
            SimpleDateFormat oFormatter = new SimpleDateFormat(pstrDateFormat);
            dttDate = oFormatter.parse(pstrValue);
            oFormatter = null;
          }
          catch (Exception e)
          {
            e.printStackTrace();
          }

          return dttDate;
        }
        
        //千分位数据转换
        public static String numberFormatThousand(Double d){
        	DecimalFormat df = new DecimalFormat();
    		df.applyPattern("#,##0.00");
    		return df.format(d);
        }
        
        //保留两位小数
        public static String numberFormatDouble(String str){
        	DecimalFormat df = new DecimalFormat();
    		df.applyPattern("0.00");
    		double d = Double.parseDouble(str);
    		return df.format(d);
        }
        
        //查询几个月后的日期 days类型 ：yyyymmdd
        public static String LaterMonth(String days,int i){
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    		Date now = null;
    		try {
    			try {
					now = sdf.parse(days);
				} catch (java.text.ParseException e) {
					e.printStackTrace();
				}
    		} catch (ParseException e) {
    			e.printStackTrace();
    		}
    		Calendar calendar = Calendar.getInstance();
    		calendar.setTime(now);
    		calendar.add(Calendar.MONTH, i);
    		return sdf.format(calendar.getTime());
    	}
        /**
         * 得到两个日期之间的每一天(包括起始日和结束日)
         * @param beginTime
         * @param endTIme
         * @return
         */
        public static List<String> getDaysBetweenTime(String beginTime,String endTIme){
	    	//日期 集合
			List<String> listDate = new ArrayList<String>();
	        try{
	        	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        	Calendar startDay = Calendar.getInstance();  
	        	Calendar endDay = Calendar.getInstance();  
	        	startDay.setTime(df.parse(beginTime));  
	        	endDay.setTime(df.parse(endTIme));
	        	long betweenDays = Utils.getDaysBetween(df.format(startDay.getTime()),df.format(endDay.getTime()));
	        	//给出的起始日比结束日大则不执行
	        	if (startDay.compareTo(endDay) > 0) {  
	        		   return null;
	        	}  
	        	Calendar currentPrintDay = startDay;
	        	String curDay = df.format(currentPrintDay.getTime());
        		for(int i = 0 ;i <= betweenDays ;i++){
		        	listDate.add(curDay);
        			// 日期加一  
	        		currentPrintDay.add(Calendar.DATE, 1);
	        		curDay = df.format(currentPrintDay.getTime());
	        	}

	        }catch (Exception e) {
				e.printStackTrace();
	        }
	        return listDate;
	    }
        
        public static String unescape(String src) {
    		StringBuffer tmp = new StringBuffer();
    		tmp.ensureCapacity(src.length());
    		int lastPos = 0, pos = 0;
    		char ch;
    		while (lastPos < src.length()) {
    			pos = src.indexOf("%", lastPos);
    			if (pos == lastPos) {
    				if (src.charAt(pos + 1) == 'u') {
    					ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
    					tmp.append(ch);
    					lastPos = pos + 6;
    				} else {
    					ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
    					tmp.append(ch);
    					lastPos = pos + 3;
    				}
    			} else {
    				if (pos == -1) {
    					tmp.append(src.substring(lastPos));
    					lastPos = src.length();
    				} else {
    					tmp.append(src.substring(lastPos, pos));
    					lastPos = pos;
    				}
    			}
    		}
    		return tmp.toString();
    	}
        
        /**
    	 * 验证是否为金额
    	 */
    	public static boolean isMoney(String str) {
    		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
    		java.util.regex.Matcher match = pattern.matcher(str);
    		if (match.matches() == false) {
    			return false;
    		} else {
    			return true;
    		}
    	}
    	
    	public static String getEncoding(String str) {
    	       String encode = "GB2312";      
    	      try {      
    	          if (str.equals(new String(str.getBytes(encode), encode))) {      //判断是不是GB2312
    	               String s = encode;      
    	              return s;      //是的话，返回“GB2312“，以下代码同理
    	           }      
    	       } catch (Exception exception) {      
    	       }      
    	       encode = "ISO-8859-1";      
    	      try {      
    	          if (str.equals(new String(str.getBytes(encode), encode))) {      //判断是不是ISO-8859-1
    	               String s1 = encode;      
    	              return s1;      
    	           }      
    	       } catch (Exception exception1) {      
    	       }      
    	       encode = "UTF-8";      
    	      try {      
    	          if (str.equals(new String(str.getBytes(encode), encode))) {   //判断是不是UTF-8
    	               String s2 = encode;      
    	              return s2;      
    	           }      
    	       } catch (Exception exception2) {      
    	       }      
    	       encode = "GBK";      
    	      try {      
    	          if (str.equals(new String(str.getBytes(encode), encode))) {      //判断是不是GBK
    	               String s3 = encode;      
    	              return s3;      
    	           }      
    	       } catch (Exception exception3) {      
    	       }      
    	      return "";        //如果都不是，说明输入的内容不属于常见的编码格式。
    	}
}

