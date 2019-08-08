package com.cmwa.ec.weixin.webapp.test.starter;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * 内嵌jetty容器的启动器
 * 
 * @author nevynzheng
 * 
 */
public class WebAppStarter {
	public static void main(String[] args) throws Exception {

		JettyWebServer server = new WebAppStarter.JettyWebServer();
		server.start();
	}

	static class JettyWebServer {

		public void start() throws Exception {

			String rootPath = System.getProperty("user.dir");
			String sysSep = System.getProperty("file.separator");

			System.setProperty("user.timezone", "Asia/Shanghai");

			// 开发环境的appHome默认取conf-properties的路径
			System.setProperty("appHome", rootPath + sysSep + "src" + sysSep + "main" + sysSep + "resources" + sysSep
					+ "conf-properties");

			Server server = new Server();

			Connector connector = new SelectChannelConnector();
			connector.setPort(8080);
			server.addConnector(connector);

			WebAppContext webapp = new WebAppContext();
			webapp.setDescriptor(rootPath + sysSep + "src" + sysSep + "main" + sysSep + "webapp" + sysSep + "WEB-INF"
					+ sysSep + "web.xml");
			webapp.setContextPath("/");
			webapp.setResourceBase(rootPath + sysSep + "src" + sysSep + "main" + sysSep + "webapp");
			server.setHandler(webapp);

			server.start();
			server.join();
		}
	}

}
