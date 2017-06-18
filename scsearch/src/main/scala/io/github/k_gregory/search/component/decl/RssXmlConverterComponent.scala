package io.github.k_gregory.search.component.decl

import io.github.k_gregory.search.model.RssChannel


trait RssXmlConverterComponent {
  val rssXmlDocumentConverter: RssXmlConverter

  trait RssXmlConverter {
    def convert(text: String): RssChannel
  }

}
