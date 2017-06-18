package io.github.k_gregory.search

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import akka.actor.ActorSystem
import io.getquill._
import io.github.k_gregory.search.component.decl.{ActorSystemProviderComponent, QuillDbComponent}
import io.github.k_gregory.search.component.impl._
import io.github.k_gregory.search.component.impl.akka.{AkkaRssFetcherComponent, AkkaWebServerComponent}
import io.github.k_gregory.search.component.impl.quill.{QuillIndexStorageComponent, QuillRssIndexerComponent, QuillSearchServiceComponent}

import scala.concurrent.duration.DurationInt
import scala.io.StdIn
import scala.util.Try


object Application extends App
  with ActorSystemProviderComponent
  with AkkaRssFetcherComponent
  with AkkaWebServerComponent
  with SimpleRssXmlConverterComponent

  with QuillRssIndexerComponent
  with QuillDbComponent
  with QuillIndexStorageComponent
  with QuillSearchServiceComponent

  with SimpleTextNormalizerComponent {

  override lazy val actorSystem = ActorSystem("cake-system")
  override lazy val webServer = new AkkaWebServer("localhost", 8080)
  override lazy val rssFetcher = new AkkaRssFetcher
  override lazy val rssXmlDocumentConverter = new SimpleRssXmlConverter

  override lazy val indexer = new SimpleRssIndexer
  override lazy val dbCtx = new H2JdbcContext[SnakeCase]("h2ctx")
  override lazy val indexStorage = new H2IndexStorage

  override lazy val textNormalizer = new SimpleTextNormalizer

  override lazy val searchService = new QuillSearchService

  private implicit val ec = actorSystem.dispatcher

  actorSystem.scheduler.schedule(0.seconds, 1.minute, () => {
    Try({
      val calendar = Calendar.getInstance
      calendar.add(Calendar.SECOND, -59)
      searchService.rescanSources(calendar.getTime)
    })
  })


  searchService.addSource("https://www.theguardian.com/world/rss")


  println("Starting server...")
  private val serverBind = webServer.run()
  println("Press return to exit")
  StdIn.readLine()
  serverBind
    .flatMap(_.unbind())
    .onComplete(_ => actorSystem.terminate())


  StdIn.readLine("Press enter to terminate")
  actorSystem.terminate()
  dbCtx.close()
}
