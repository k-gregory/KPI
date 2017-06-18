package scsearch

/**
  * Created by grego on 17.05.2017.
  */

case class Document(text: String) extends Ordered[Document] {
  def termCount: Map[String, Long] = {
    val left = text
      .toLowerCase()
      .replaceFirst("^[^\\w\\d]+", "")
      .split("[^\\w\\d]+")
      .foldLeft(Map.empty[String, Long]) { (countMap, term) =>
        countMap + (term -> (countMap.getOrElse(term, 0L) + 1L))
      }
    left
  }


  override def compare(that: Document): Int = text compareTo that.text
}
