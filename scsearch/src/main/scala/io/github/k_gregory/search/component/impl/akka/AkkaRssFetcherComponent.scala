package io.github.k_gregory.search.component.impl.akka

import java.nio.charset.StandardCharsets

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.stream.ActorMaterializer
import akka.util.ByteString
import io.github.k_gregory.search.component.decl.{ActorSystemProviderComponent, RssFetcherComponent, RssXmlConverterComponent}
import io.github.k_gregory.search.model.RssChannel

import scala.concurrent.Future

class InvalidRssResourceException(msg: String) extends RuntimeException(msg)

class InvalidRssResponceException(httpResponse: HttpResponse, msg: String) extends InvalidRssResourceException(msg)

trait AkkaRssFetcherComponent extends RssFetcherComponent {
  this: AkkaRssFetcherComponent
    with ActorSystemProviderComponent
    with RssXmlConverterComponent
  =>

  class AkkaRssFetcher extends RssFetcher {

    override def fetch(uri: String): Future[RssChannel] = {
      implicit val system = actorSystem
      implicit val materializer = ActorMaterializer()
      implicit val ec = system.dispatcher

      Http().singleRequest(HttpRequest(uri = uri)).flatMap({
        case HttpResponse(StatusCodes.OK, _, entity, _) =>
          entity.dataBytes.runFold(ByteString(""))(_ ++ _)
        case r@HttpResponse(_, _, _, _) =>
          r.discardEntityBytes()
          throw new InvalidRssResponceException(r, "Server sent illegal responce")
      })
        .map(bs => bs.decodeString(StandardCharsets.UTF_8))
        .map(rssXmlDocumentConverter.convert)
    }
  }

}
