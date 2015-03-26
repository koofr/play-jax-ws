package net.koofr.play.ws

import scala.collection.JavaConversions._
import scala.concurrent._
import java.io._
import java.net.InetSocketAddress
import play.api._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.iteratee._
import play.api.mvc._
import net.koofr.play.ws.util.ResponseOutputStream

abstract class WSController() extends Controller {

  def getService(): Object

  def getScheme(): String

  def getHost(): String

  def getPort(): Int

  def getContextPath(): String

  def getFilters(): Seq[EssentialFilter] = Seq()

  protected lazy val service = getService()

  protected lazy val contextPath = getContextPath()

  protected lazy val filters = getFilters()

  protected lazy val context = {
    val plugin = Play.current.plugin[WSPlugin] getOrElse {
      throw new Exception("WSPlugin is not registered.")
    }

    val ctx = new WSHttpContext(contextPath)

    plugin.register(getService(), ctx)

    ctx
  }

  protected def pipingBodyParser(requestBodyPromise: Promise[WSRequest]): BodyParser[Unit] = BodyParser("pipingBodyParser") { request =>
    val pos = new PipedOutputStream
    val pis = new PipedInputStream(pos, 64 * 1024)

    requestBodyPromise.success(WSRequestImpl(request, pis))

    Iteratee.foreach[Array[Byte]](bytes => pos.write(bytes)) map { _ =>
      pos.close()
      Right(())
    }
  }

  protected def handle(path: String): EssentialAction = {
    val requestPromise = Promise[WSRequest]()
    val requestFuture = requestPromise.future

    val resultPromise = Promise[Unit]()
    val resultFuture = resultPromise.future

    val responseOutputStream = new ResponseOutputStream(resultPromise)
    val responseInputStream = new PipedInputStream(responseOutputStream)

    val exchangeFuture = requestFuture map { req =>
      val ex = new WSHttpExchange(responseOutputStream, context, req,
        getScheme(), getHost(), getPort(), getContextPath())

      Future {
        context.getHandler().handle(ex)
      }

      ex
    }

    val action = Action.async(pipingBodyParser(requestPromise)) { request =>
      for {
        ex <- exchangeFuture
        _ <- resultFuture
      } yield {
        val status = ex.getStatus
        val content = Enumerator.fromStream(responseInputStream)
        val headers = ex.getResponseHeaders()
          .toMap.toSeq.flatMap { case (k, v) => v.map(k -> _) }

        Status(status).chunked(content).withHeaders(headers: _*)
      }
    }

    Filters(action, filters: _*)
  }

  def get(path: String): EssentialAction = handle(path)

  def post(path: String): EssentialAction = handle(path)

}
