package io.github.k_gregory.search.component.impl

import java.text.SimpleDateFormat
import java.util.Date

import io.github.k_gregory.search.component.decl.RssXmlConverterComponent
import io.github.k_gregory.search.model.RssChannel
import io.github.k_gregory.search.model.db.RssItem

import scala.util.Try
import scala.xml.{Node, NodeSeq, XML}


trait SimpleRssXmlConverterComponent extends RssXmlConverterComponent {

  class SimpleRssXmlConverter extends RssXmlConverter {

    private implicit class XmlNodeOps(n: Node) {
      def \?(that: String): Option[String] = (n \ that).headOption.map(_.text.trim)
    }

    private implicit class XmlNodeSeqOps(n: NodeSeq) {
      def \!(that: String): String = (n \ that).headOption.get.text.trim
    }

    private val format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z")

    override def convert(text: String): RssChannel = {
      val channel = XML.loadString(text) \ "channel"

      RssChannel(
        title = channel \! "title",
        link = channel \! "link",
        description = channel \! "description",
        items = channel \ "item" map itemFromXml
      )
    }

    private def itemFromXml(n: Node): RssItem = {

      RssItem(
        id = None,
        title = n \? "title",
        link = n \? "link",
        description = n \? "description",
        pubTime = (n \? "pubDate").flatMap(parseDate),
        tdLength = None,
        feedId = None
      )
    }

    private def parseDate(rawDate: String): Option[Date] = {
      Try(format.parse(rawDate)).toOption
    }
  }

}
