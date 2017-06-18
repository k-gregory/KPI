package io.github.k_gregory.search.component.impl.quill

import java.util.Date

import io.github.k_gregory.search.component.decl._
import io.github.k_gregory.search.model.db.{RssFeed, RssItem}
import org.slf4j.LoggerFactory

import scala.util.{Failure, Success}

/**
  * Created by gregory on 5/28/17.
  */
trait QuillSearchServiceComponent extends RssSearchServiceComponent
  with QuillDbComponent
with IndexStorageComponent
  with RssFetcherComponent
with TextNormalizerComponent
  with RssIndexerComponent
  with ActorSystemProviderComponent {

  class QuillSearchService extends RssSearchService {
    private implicit val ec = actorSystem.dispatcher

    import dbCtx._

    private val log = LoggerFactory.getLogger(getClass)

    override def rescanSources(scanFrom: Date): Unit = {
      implicit class DateQuotes(left: Date) {
        def >(right: Date) = quote(infix"$left > $right".as[Boolean])

        def <(right: Date) = quote(infix"$left < $right".as[Boolean])
      }

      log.info(s"Rescaning sources older than $scanFrom")
      val oldFeeds = dbCtx.run(quote {
        val v = lift(scanFrom.getTime)
        query[RssFeed].filter(_.lastUpdate < lift(scanFrom))
      })
      oldFeeds.foreach(s => addSource(s.url))
      log.info("Rescanned sources")
    }

    override def addSource(origin: String): Unit = {
      log.info(s"Adding source $origin")
      rssFetcher.fetch(origin).map(chan => {
        indexer.index(origin, chan)
        log.info(s"Added source $origin")
      }).onComplete({
        case Success(_) =>
          log.info(s"Indexed $origin")
        case Failure(e) =>
          log.warn(s"Can't index $origin: ${e.getMessage}")
      })
    }

    override def search(query: String): Iterable[RssItem] = {
      textNormalizer
        .normalize(query)
        .toList
        .headOption
        .map(indexStorage.search)
        .getOrElse(List.empty)
    }
  }

}
