package controllers;

import play.api.mvc.EssentialAction;
import net.koofr.play.ws.WSController;

public class ExampleServiceController extends WSController {

  private static final ExampleServiceController instance = new ExampleServiceController();

  private ExampleServiceController() {
    super();
  }

  public Object getService() {
    return new ExampleService();
  }

  public String getScheme() {
    return "http";
  }

  public String getHost() {
    return "localhost";
  }

  public int getPort() {
    return 9000;
  }

  public String getContextPath() {
    return "example-service/";
  }

  public static EssentialAction handleGet(String path) {
    return instance.get(path);
  }

  public static EssentialAction handlePost(String path) {
    return instance.post(path);
  }

}
