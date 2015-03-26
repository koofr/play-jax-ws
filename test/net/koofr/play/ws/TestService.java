package net.koofr.play.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(name = "TestService", targetNamespace = "http://example.com/xmlns/testservice", endpointInterface = "net.koofr.play.ws.TestService")
public class TestService {

  @WebMethod
  public String ping(@WebParam(name = "msg") String msg) {
    return msg;
  }

}
