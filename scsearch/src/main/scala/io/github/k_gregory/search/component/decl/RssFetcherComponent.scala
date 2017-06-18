package io.github.k_gregory.search.component.decl


import io.github.k_gregory.search.model.RssChannel

import scala.concurrent.Future


trait RssFetcherComponent {
  val rssFetcher: RssFetcher

  trait RssFetcher {
    def fetch(uri: String): Future[RssChannel]
  }

}
