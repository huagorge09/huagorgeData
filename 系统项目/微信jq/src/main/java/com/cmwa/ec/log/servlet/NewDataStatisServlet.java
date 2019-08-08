package com.cmwa.ec.log.servlet;

import com.cmwa.ec.log.asynlog.connector.AsynLogger;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class NewDataStatisServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(DataStatisServlet.class
			.getName());
	private static final long serialVersionUID = 8621514697972012686L;
	private static int logTimestamp = 0;
	private static int logAmount = 0;

	public static enum CHANNEL_TYPE {
		WEB("01"), WX("03");

		private String value;

		private CHANNEL_TYPE(String v) {
			this.value = v;
		}
	}

	public static enum DATA_TYPE {
		USER_BEHAVIOR("11"), BROSWER_TYPE("12");

		private String value;

		private DATA_TYPE(String v) {
			this.value = v;
		}
	}

	public void init() throws ServletException {
		super.init();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int curSeq = isLimited();
		if (curSeq != -1) {
			String curTimeStamp = request.getParameter("curTimeStamp");

			String path = request.getParameter("path");
			String subPath = request.getParameter("subPath");
			String urlParameter = request.getParameter("urlParameter");

			String userId = "";
			Object obj = request.getSession(true).getAttribute("cmfUserId");
			if (obj != null) {
				userId = (String) obj;
			}
			String channel = request.getParameter("channel");
			String type = request.getParameter("type");

			String others = request.getParameter("others");
			String ipAttr = getRemoteAddress(request);
			String sessionId = request.getSession(true).getId();

			String reqSource = request.getParameter("reqSource");
			if ((reqSource == null) || (reqSource.equals(""))) {
				reqSource = (String) request.getSession(true).getAttribute(
						"refererSession");
				if (reqSource == null) {
					reqSource = "";
				} else {
					request.getSession(true).removeAttribute("refererSession");
				}
				logger.info("【DataStatisServlet】的doGet()中页面没有获取到referrer，在session中获取：reqSource="
						+ reqSource);
			}
			StringBuffer urlS = request.getRequestURL();
			String url = "";
			if ((urlS == null) || (urlS.equals(""))) {
				url = "";
			} else {
				url = urlS.toString();
			}
			String uri = request.getRequestURI();
			String prefixPath = url.replace(uri, "");
			String fundAcco = (String) request.getSession(true).getAttribute(
					"fundAcco");

			logger.info("【【【>>>url" + url + ">>>uri" + uri + ">>>prefixPath"
					+ prefixPath + "】】】");

			logger.info("【DataStatisServlet】的doGet()中获取到的参数： " + ">>>path="
					+ path + ">>>subPath=" + subPath + ">>>urlParameter="
					+ urlParameter + ">>>userId=" + userId + ">>>channel="
					+ channel + ">>>type=" + type + ">>>others=" + others
					+ ">>>ipAttr=" + ipAttr + ">>>curSeq=" + curSeq
					+ ">>>sessionId=" + sessionId + ">>>reqSource=" + reqSource
					+ ">>>prefixPath=" + prefixPath + ">>>fundAcco" + fundAcco);

			Object[] objs = { curTimeStamp, path, subPath, urlParameter,
					userId, channel, type, others, ipAttr, "" + curSeq,
					sessionId, reqSource, prefixPath, fundAcco };

			AsynLogger.synWriteLog("BEHAVIOUR", objs);
		}
		response.getOutputStream().print("c");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private static int isLimited() {
		int curTimestamp = (int) (new Date().getTime() / 100000L);
		synchronized (DataStatisServlet.class) {
			if ((curTimestamp == logTimestamp) && (logAmount > 5000)) {
				return -1;
			}
			if (curTimestamp != logTimestamp) {
				logTimestamp = curTimestamp;
				logAmount = 0;
			}
			return ++logAmount;
		}
	}

	public String getRemoteAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if ((ip == null) || (ip.length() == 0)
				|| (ip.equalsIgnoreCase("unknown"))) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if ((ip == null) || (ip.length() == 0)
				|| (ip.equalsIgnoreCase("unknown"))) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if ((ip == null) || (ip.length() == 0)
				|| (ip.equalsIgnoreCase("unknown"))) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
