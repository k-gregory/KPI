package io.github.k_gregory.search.component.impl.akka

import java.text.{DateFormat, SimpleDateFormat}
import java.util.Date

import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import io.github.k_gregory.search.component.decl.{ActorSystemProviderComponent, RssFetcherComponent, RssSearchServiceComponent, WebServerComponent}
import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import io.github.k_gregory.search.model.db.RssItem

import scala.concurrent.Future
import scala.util.{Failure, Success}

trait AkkaWebServerComponent extends WebServerComponent {
  this: AkkaWebServerComponent
    with ActorSystemProviderComponent
  with RssSearchServiceComponent
  =>

  class AkkaWebServer(private val interface: String, private val port: Int) extends WebServer with JsonSupport{
    private implicit val system = actorSystem
    private implicit val materializer = ActorMaterializer()
    private implicit val ec = system.dispatcher

    private val log = Logging(system.eventStream, getClass.getName)

    private val route = parameter("q"){query=>
      complete(searchService.search(query))
    }

    override def run(): Future[Http.ServerBinding] = Http().bindAndHandle(route, interface, port)
  }

}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val dateFormat = new RootJsonFormat[Date] {
    val format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
    override def write(obj: Date): JsValue = {
      JsString(format.format(obj))
    }

    override def read(json: JsValue): Date = {
      format.parse(json.toString())
    }
  }

  implicit val itemFormat: RootJsonFormat[RssItem] = jsonFormat7(RssItem)
}