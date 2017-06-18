package io.github.k_gregory.search.component.decl

import akka.http.scaladsl.Http

import scala.concurrent.Future

/**
  * Created by gregory on 5/26/17.
  */
trait WebServerComponent {
  val webServer: WebServer

  trait WebServer {
    def run(): Future[Http.ServerBinding]
  }

}
