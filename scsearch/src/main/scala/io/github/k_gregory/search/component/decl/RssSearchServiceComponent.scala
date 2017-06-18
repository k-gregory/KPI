package io.github.k_gregory.search.component.decl

import java.util.Date

import io.github.k_gregory.search.model.db.RssItem

/**
  * Created by gregory on 5/28/17.
  */
trait RssSearchServiceComponent {
  val searchService: RssSearchService

  trait RssSearchService {
    def addSource(origin: String)
    def search(query: String): Iterable[RssItem]
    def rescanSources(scanFrom: Date)
  }

}
