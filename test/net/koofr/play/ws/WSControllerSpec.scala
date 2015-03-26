package net.koofr.play.ws

import scala.concurrent._
import scala.concurrent.duration.Duration
import org.specs2.mutable.Specification
import play.api.mvc._
import play.api.libs.ws._
import play.api.test._
import play.api.test.Helpers.running
import play.api.Play.current

class WSControllerSpec extends Specification {

  class TestController extends WSController {
    def getService() = new TestService()
    def getScheme() = "http"
    def getHost() = "localhost"
    def getPort() = 1234
    def getContextPath() = "svc/"
  }

  val ctrl = new TestController

  def app(controller: WSController = ctrl) = FakeApplication(additionalPlugins = Seq("net.koofr.play.ws.WSPlugin"), withRoutes = {
    case ("GET", path) if path.startsWith("/svc/") =>
      controller.get(path.substring(4))
    case ("POST", path) if path.startsWith("/svc/") =>
      controller.post(path.substring(4))
  })

  def withServer[T](block: => T, application: FakeApplication = app()) = {
    running(TestServer(port = 1234, application = application))(block)
  }

  "WSController" should {

    "handle GET request" in withServer {
      val res = Await.result(WS.url("http://localhost:1234/svc/").get(), Duration.Inf)

      res.status must equalTo(200)
      res.body must contain("http://localhost:1234/svc/?wsdl")
    }

    "return WSDL" in withServer {
      val res = Await.result(WS.url("http://localhost:1234/svc/?wsdl").get(), Duration.Inf)

      res.status must equalTo(200)
      res.body must contain("""<message name="ping">""")
    }

    "return schema" in withServer {
      val res = Await.result(WS.url("http://localhost:1234/svc/?xsd=1").get(), Duration.Inf)

      res.status must equalTo(200)
      res.body must contain("""<xs:element name="ping" type="tns:ping"></xs:element>""")
    }

    "handle request" in withServer {
      val body = """<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tes="http://example.com/xmlns/testservice">
   <soapenv:Header/>
   <soapenv:Body>
      <tes:ping>
         <msg>Msg</msg>
      </tes:ping>
   </soapenv:Body>
</soapenv:Envelope>"""

      val res = Await.result(WS.url("http://localhost:1234/svc/").withHeaders("SOAPAction" -> "\"\"", "Content-Type" -> "text/xml").post(body), Duration.Inf)

      res.status must equalTo(200)
      res.body must equalTo("""<?xml version="1.0" ?><S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/"><S:Body><ns2:pingResponse xmlns:ns2="http://example.com/xmlns/testservice"><return>Msg</return></ns2:pingResponse></S:Body></S:Envelope>""")
    }

    "apply filters" in withServer({
      val resFail = Await.result(WS.url("http://localhost:1234/svc/").get(), Duration.Inf)
      resFail.status must equalTo(401)

      val res = Await.result(WS.url("http://localhost:1234/svc/").withHeaders("X-Key" -> "secret").get(), Duration.Inf)
      res.status must equalTo(200)
    }, app(controller = new TestController {
      override def getFilters = Seq(new Filter {
        def apply(f: RequestHeader => Future[Result])(request: RequestHeader): Future[Result] = {
          if (request.headers.get("X-Key").getOrElse("") == "secret") {
            f(request)
          } else {
            Future.successful(Results.Unauthorized)
          }
        }
      })
    }))

  }

}
