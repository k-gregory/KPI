package io.github.k_gregory.search.component.impl

import io.github.k_gregory.search.component.decl.TextNormalizerComponent
import io.github.k_gregory.search.component.impl.SimpleTextNormalizer.stopWords
import org.tartarus.snowball.ext.EnglishStemmer

import scala.io.Source

trait SimpleTextNormalizerComponent extends TextNormalizerComponent {
  class SimpleTextNormalizer extends TextNormalizer {
    private val regex = "\\w+".r

    override def normalize(text: String): Iterator[String] = {
      val stemmer = new EnglishStemmer
      regex
        .findAllIn(text.toLowerCase())
        .filter(!stopWords.contains(_))
        .map(s => {
        stemmer.setCurrent(s)
        stemmer.stem()
        stemmer.getCurrent
      })
    }
  }
}

object SimpleTextNormalizer{
  private val resource = Source.fromResource("./stopwords.txt")
  val stopWords: Set[String] = resource.getLines().toSet
}
