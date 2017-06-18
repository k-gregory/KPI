package io.github.k_gregory.search.component.impl.noop

import io.github.k_gregory.search.component.decl.IndexStorageComponent
import io.github.k_gregory.search.model.RssChannel
import io.github.k_gregory.search.model.db.{RssItem, RssItemTerms, Term}
import org.slf4j.LoggerFactory

/**
  * Created by gregory on 5/28/17.
  */
trait NoopIndexStorageComponent extends IndexStorageComponent {

  class NoopIndexStorage extends IndexStorage {
    val log = LoggerFactory.getLogger(getClass)

    override def mergeChannel(originUrl: String, rssChannel: RssChannel): List[Option[Long]] = {
      log.debug(s"merging ${rssChannel.items.length}")
      List.empty
    }

    override def saveRssItemTerms(values: List[RssItemTerms]): Int = {
      log.debug(s"saving ${values.length}")
      values.length
    }

    override def mergeTerms(values: Traversable[Term]): Unit = {
      log.debug(s"meging ${values.count(_ => true)}")
    }

    override def search(word: String): List[RssItem] = List.empty
  }

}
