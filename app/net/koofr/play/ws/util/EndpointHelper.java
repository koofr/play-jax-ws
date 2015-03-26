package net.koofr.play.ws.util;

import javax.xml.ws.Endpoint;

public class EndpointHelper {
  private EndpointHelper() {
  }

  public static Endpoint create(Object implementor) {
    return Endpoint.create(implementor);
  }
}
