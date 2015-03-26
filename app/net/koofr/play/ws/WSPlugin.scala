package net.koofr.play.ws

import javax.xml.ws.Endpoint
import javax.xml.ws.spi.http.HttpContext
import play.api._
import net.koofr.play.ws.util.EndpointHelper

class WSPlugin(application: Application) extends Plugin {
  private[this] var endpoints = Seq[Endpoint]()

  def register(serviceImpl: Object, context: HttpContext) = synchronized {
    val endpoint = EndpointHelper.create(serviceImpl)
    endpoint.publish(context)

    endpoints = endpoint +: endpoints
  }

  def onStop(app: Application) = synchronized {
    endpoints foreach (_.stop())
  }

}
