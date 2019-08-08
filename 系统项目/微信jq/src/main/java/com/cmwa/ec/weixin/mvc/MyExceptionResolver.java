package com.cmwa.ec.weixin.mvc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLIntegrityConstraintViolationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class MyExceptionResolver implements HandlerExceptionResolver {

	private Logger logger = Logger.getLogger(this.getClass());

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

		// 工作台显示
		System.out.println("==============异常开始=============");
		ex.printStackTrace();
		System.out.println("==============异常结束=============");

		// 打印日志
		logger.error(ex.toString(), ex);
		// 界面显示堆栈
		StringBuffer sb = new StringBuffer();
		sb.append(ex.toString()).append("\n");
		StackTraceElement[] trace = ex.getStackTrace();
		for (int i = 0; i < trace.length; i++) {
			sb.append("\tat ").append(trace[i]).append("\n");
		}

		ModelAndView mv = null;
		String exception = null;
		
		// 处理ajax请求异常
		if ("XMLHttpRequest".equals(request.getHeader("x-requested-with"))) {
			if (ex.getCause() != null) {
				exception = ex.getCause().getMessage();
			}else{//其他异常取异常消息
				exception = ex.getMessage();
			}
			//做出417响应输出异常信息
			responseAjaxFailedException(response, exception);
			return null;
		}
		
		if (ex instanceof ValidateFailedException) {
			// 自行封装的校验异常信息
			mv = new ModelAndView("system/hint/validateFailed");
				mv.addObject("operatorId", "");
			exception = ex.getMessage();
		} else if (ex.getCause() != null && ex.getCause() instanceof SQLIntegrityConstraintViolationException) {
			// 删除数据，触发外键约束时的提示信息
			mv = new ModelAndView("/ECCAPP/ECCFramework/validateFailed");
				mv.addObject("operatorId", "");
			exception = "唯一约束校验失败：" + ex.getCause().getMessage();
		} else {
			mv = new ModelAndView("/ECCAPP/ECCFramework/error");
			exception = sb.toString().replaceAll("\n", "<br/>");
		}

		mv.addObject("exception", exception);
		return mv;
	}

	public void responseAjaxFailedException(HttpServletResponse response, String msg) {
		try {
			PrintWriter out = response.getWriter();
			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			response.setContentType("application/json;charset=UTF-8");
			out.write(msg);
			out.flush();
		} catch (IOException e) {
			logger.error(e.toString(), e);
			throw new RuntimeException(e);
		}
	}

}
