package cloud.zones.connect

import javax.servlet.http.HttpServletRequest
import org.eclipse.jetty.client.HttpClient
import org.eclipse.jetty.client.api.Request
import org.eclipse.jetty.http.HttpField
import org.eclipse.jetty.proxy.{ConnectHandler, ProxyServlet}
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.{ServletHandler, ServletHolder}

object ProxyMain {

  def main(args: Array[String]): Unit = {

    val httpClient = new HttpClient()
    val server = new Server(8080)

    try {
      httpClient.setFollowRedirects(false)
      httpClient.setUserAgentField(new HttpField("User-Agent", "curl/7.58.0"))
      httpClient.start

      server.insertHandler(new ConnectHandler)

      val holder = new ServletHolder(new ProxyServlet {
        override def getHttpClient: HttpClient = httpClient
        override def addProxyHeaders(clientRequest: HttpServletRequest, proxyRequest: Request): Unit = {}
      })
      holder.setAsyncSupported(true)
      holder.setInitParameter("maxThreads", "2")

      val handler = new ServletHandler()
      server.insertHandler(handler)
      handler.addServletWithMapping(holder, "/*")

      server.start
      server.join
    } finally {
      httpClient.stop
      server.stop
    }

  }

}
