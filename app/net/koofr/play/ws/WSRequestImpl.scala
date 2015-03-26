package net.koofr.play.ws

import scala.collection.JavaConversions._
import java.io.InputStream
import java.net.InetSocketAddress
import java.util.{ List => JList }
import play.api.mvc.RequestHeader

case class WSRequestImpl(request: RequestHeader, body: InputStream) extends WSRequest {

  def method() = request.method

  def uri() = request.uri

  def query() = request.rawQueryString

  def headers() = request.headers.toMap.map { case (k, v) => (k, v: JList[String]) }

  def remoteAddress() = new InetSocketAddress(request.remoteAddress, 0)

}
