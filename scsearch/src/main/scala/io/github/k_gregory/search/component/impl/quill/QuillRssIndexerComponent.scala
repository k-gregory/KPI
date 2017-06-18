package io.github.k_gregory.search.component.impl.quill

import java.util.Date

import io.github.k_gregory.search.component.decl.{IndexStorageComponent, QuillDbComponent, RssIndexerComponent, TextNormalizerComponent}
import io.github.k_gregory.search.model.RssChannel
import io.github.k_gregory.search.model.db.{RssItem, RssItemTerms, Term}
import org.jsoup.Jsoup
import org.jsoup.nodes.{Node, TextNode}
import org.jsoup.select.{NodeTraversor, NodeVisitor}
import org.slf4j.LoggerFactory

trait QuillRssIndexerComponent extends RssIndexerComponent
  with IndexStorageComponent
  with TextNormalizerComponent
  with QuillDbComponent {

  class SimpleRssIndexer extends RssIndexer {
    private val log = LoggerFactory.getLogger(this.getClass)


    override def index(origin: String, channel: RssChannel): Unit = synchronized {
      log.info(s"Indexing $origin")
      val itemsWithWordCount = channel.items
        .map(i => i.copy(pubTime = Some(i.pubTime.getOrElse(new Date()))))
        .map(removeHtmlFromDescription)
        .map(calculateItemWords)

      val cleanChannel = channel.copy(
        items = itemsWithWordCount.map(_._1)
      )

      dbCtx.transaction({
        val savedIds = indexStorage.mergeChannel(origin, cleanChannel)
        val savedItems = savedIds.toList
          .zip(itemsWithWordCount)
          .filter(_._1.nonEmpty)
          .map({
            case (newId, (item, wordCount)) =>
              (item.copy(id = Some(newId.get)), wordCount)
          })

        val totalDF = calculateTotalWordUsage(savedItems.map(_._2))
          .map({ case (t, f) => Term(t, f) })

        indexStorage.mergeTerms(totalDF)

        val itemTermses = savedItems.flatMap({ case (item, termFreqs) =>
          for ((term, frequency) <- termFreqs)
            yield RssItemTerms(item.id.get, term, frequency)
        })

        indexStorage.saveRssItemTerms(itemTermses)

        log.info(s"Indexing $origin finished,saved: ${savedItems.size} items")
      })
    }

    private def calculateItemWords(item: RssItem): (RssItem, Map[String, Int]) = {
      val wordsCount = countWordSeqSum(
        Seq(item.description, item.title)
      )
      val tdLength = math.sqrt(wordsCount.values.map(c => c * c).sum)
      var cleanItem = item.copy(
        tdLength = Some(tdLength)
      )
      (cleanItem, wordsCount)
    }

    private def countWordSeqSum(words: Seq[Option[String]]) = {
      words.foldLeft(Map.empty[String, Int])((res, wordOpt) => {
        wordOpt.map(word => countWordsSum(word, res)).getOrElse(res)
      })
    }

    private def countWordsSum(text: String, init: Map[String, Int]) = {
      textNormalizer.normalize(text).foldLeft(init)((res, word) => {
        res + (word -> (res.getOrElse(word, 0) + 1))
      })
    }

    private def calculateTotalWordUsage(wordUsages: Traversable[Map[String, Int]]): Map[String, Int] = {
      def sumUsageMaps(m1: Map[String, Int],
                       m2: Map[String, Int]): Map[String, Int] = {
        val (small, big) = if (m1.size < m2.size) (m1, m2) else (m2, m1)
        small.foldLeft(big)({ case (done, (word, usages)) =>
          done + (word -> (done.getOrElse(word, 0) + usages))
        })
      }

      wordUsages
        .map(_.mapValues(_ => 1))
        .foldLeft(Map.empty[String, Int])(sumUsageMaps)
    }

    private def removeHtmlFromDescription(item: RssItem): RssItem = {
      item.copy(description = item.description.map(removeHtml))
    }

    private def removeHtml(text: String): String = {
      class CleaningVisitor extends NodeVisitor {
        private val text = StringBuilder.newBuilder

        override def tail(node: Node, depth: Int): Unit = {}

        override def head(node: Node, depth: Int): Unit = {
          node match {
            case textNode: TextNode =>
              text.append(textNode.text())
            case _ =>
          }
        }

        override def toString: String = text.toString()
      }

      val document = Jsoup.parse(text)
      val cleaningVisitor = new CleaningVisitor
      new NodeTraversor(cleaningVisitor).traverse(document)
      cleaningVisitor.toString
    }

    class WordVector(frequencies: Map[String, Double]) {

      lazy val normalized: WordVector = {
        val len = length
        new WordVector(
          frequencies.mapValues(_ / length)
        )
      }
      lazy val length: Double = math.sqrt(
        frequencies.values.map(d => d * d).sum
      )

      def this() = {
        this(Map.empty[String, Double])
      }

      def apply(word: String): Double = frequencies(word)
    }

  }

}
