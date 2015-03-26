package controllers

import net.koofr.play.ws._

object ExampleServiceController extends WSController {

  def getService() = {
    new ExampleService()
  }

  def getScheme() = {
    "http"
  }

  def getHost() = {
    "localhost"
  }

  def getPort() = {
    9000
  }

  def getContextPath() = {
    "example-service/"
  }

}
