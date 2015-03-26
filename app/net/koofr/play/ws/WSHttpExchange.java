package net.koofr.play.ws;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.ws.spi.http.HttpContext;
import javax.xml.ws.spi.http.HttpExchange;

public class WSHttpExchange extends HttpExchange {
  private OutputStream out;
  private WSHttpContext ctx;
  private WSRequest req;
  private String scheme;
  private String host;
  private int port;
  private String contextPath;

  private int status;
  private HashMap<String, List<String>> responseHeaders;

  public WSHttpExchange(OutputStream out, WSHttpContext ctx, WSRequest req,
      String scheme, String host, int port, String contextPath) {
    this.out = out;
    this.ctx = ctx;
    this.req = req;
    this.scheme = scheme;
    this.host = host;
    this.port = port;
    this.contextPath = contextPath;

    this.status = 500;
    this.responseHeaders = new HashMap<>();
  }

  @Override
  public Map<String, List<String>> getRequestHeaders() {
    return req.headers();
  }

  @Override
  public String getRequestHeader(String name) {
    List<String> values = req.headers().get(name);

    if (values == null) {
      return null;
    } else {
      return values.get(0);
    }
  }

  @Override
  public Map<String, List<String>> getResponseHeaders() {
    return responseHeaders;
  }

  @Override
  public void addResponseHeader(String name, String value) {
    List<String> values = responseHeaders.get(name);

    if (values == null) {
      values = new ArrayList<>();
      responseHeaders.put(name, values);
    }

    values.add(value);
  }

  @Override
  public String getRequestURI() {
    return scheme + "://" + host + ":" + port + getContextPath()
        + ctx.getPath() + req.uri();
  }

  @Override
  public String getContextPath() {
    /* all endpoints reside in root context */
    return "/";
  }

  @Override
  public String getRequestMethod() {
    return req.method();
  }

  @Override
  public HttpContext getHttpContext() {
    return ctx;
  }

  @Override
  public void close() throws IOException {
    /* output stream is closed by Play */
  }

  @Override
  public InputStream getRequestBody() throws IOException {
    return req.body();
  }

  @Override
  public OutputStream getResponseBody() throws IOException {
    return out;
  }

  public int getStatus() {
    return status;
  }

  @Override
  public void setStatus(int status) {
    this.status = status;
  }

  @Override
  public InetSocketAddress getRemoteAddress() {
    return req.remoteAddress();
  }

  @Override
  public InetSocketAddress getLocalAddress() {
    return new InetSocketAddress(host, port);
  }

  @Override
  public String getProtocol() {
    return "HTTP/1.1";
  }

  @Override
  public String getScheme() {
    return scheme;
  }

  @Override
  public String getPathInfo() {
    return contextPath;
  }

  @Override
  public String getQueryString() {
    return req.query();
  }

  @Override
  public Object getAttribute(String name) {
    return null;
  }

  @Override
  public Set<String> getAttributeNames() {
    return null;
  }

  @Override
  public Principal getUserPrincipal() {
    return null;
  }

  @Override
  public boolean isUserInRole(String role) {
    return false;
  }

}
