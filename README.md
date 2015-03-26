# play-jax-ws

Expose your JAX-WS SOAP service with Play Framework.

## Example

See `example-scala` and `example-java`.

## Setup

Add following lines to your `build.sbt` file:

```
resolvers += "Koofr Github repo" at "http://koofr.github.com/repo/maven/"

libraryDependencies += "net.koofr" %% "play-jax-ws" % "0.1.0"
```

Add following to your `conf/play.plugins` file:

```
1100:net.koofr.play.ws.WSPlugin
```

Create your service `controllers/MyService.java`:

```
package controllers;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(name = "MyService", targetNamespace = "http://example.com/xmlns/myservice", endpointInterface = "controllers.MyService")
public class MyService {
  @WebMethod
  public String ping(@WebParam(name = "msg") String msg) {
    return msg;
  }
}
```

Create your controller `controllers/MyServiceController.scala`:

```
package controllers

import net.koofr.play.ws.WSController

object MyServiceController extends WSController {
  def getService() = new MyService()
  def getScheme() = "http"
  def getHost() = "localhost"
  def getPort() = 9000
  def getContextPath() = "my-service/"
}
```

Add following lines to your `conf/routes` file:

```
GET     /my-service*path          controllers.MyServiceController.get(path)
POST    /my-service*path          controllers.MyServiceController.post(path)
```

## Authors

Crafted by highly motivated engineers at http://koofr.net and, hopefully, making your day just a little bit better.
