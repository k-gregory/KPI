package io.github.k_gregory.search.component.decl

/**
  * Created by gregory on 5/27/17.
  */
trait TextNormalizerComponent {
  val textNormalizer: TextNormalizer

  trait TextNormalizer {
    def normalize(text: String): Iterator[String]
  }

}
