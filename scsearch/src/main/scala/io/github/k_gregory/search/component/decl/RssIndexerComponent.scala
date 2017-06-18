package io.github.k_gregory.search.component.decl

import io.github.k_gregory.search.model.RssChannel

/**
  * Created by gregory on 5/26/17.
  */
trait RssIndexerComponent {
  val indexer: RssIndexer

  trait RssIndexer {
    def index(origin: String, channel: RssChannel): Unit
  }

}
