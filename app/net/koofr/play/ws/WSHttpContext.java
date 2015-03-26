package net.koofr.play.ws;

import java.util.Set;

import javax.xml.ws.spi.http.HttpContext;
import javax.xml.ws.spi.http.HttpHandler;

public class WSHttpContext extends HttpContext {
  private String path;

  public WSHttpContext(String path) {
    this.path = path;
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
  public String getPath() {
    return path;
  }

  protected HttpHandler getHandler() {
    return handler;
  }

}
