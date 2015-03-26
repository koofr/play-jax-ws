package controllers;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(name = "ExampleService", targetNamespace = "http://example.com/xmlns/exampleservice", endpointInterface = "controllers.ExampleService")
public class ExampleService {

  @WebMethod
  public String ping(@WebParam(name = "msg") String msg) {
    return msg;
  }

}
