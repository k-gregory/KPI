package io.github.k_gregory.search.component.decl

import io.github.k_gregory.search.model.RssChannel
import io.github.k_gregory.search.model.db.{RssItem, RssItemTerms, Term}

trait IndexStorageComponent {
  val indexStorage: IndexStorage

  trait IndexStorage {
    def mergeChannel(originUrl: String, rssChannel: RssChannel): Traversable[Option[Long]]

    def saveRssItemTerms(values: List[RssItemTerms]): Int

    def mergeTerms(values: Traversable[Term]): Unit

    def search(word: String): List[RssItem]
  }

}
