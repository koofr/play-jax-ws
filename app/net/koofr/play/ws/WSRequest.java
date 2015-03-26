package net.koofr.play.ws;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

public interface WSRequest {

  public String method();

  public String uri();

  public String query();

  public Map<String, List<String>> headers();

  public InputStream body();

  public InetSocketAddress remoteAddress();

}
