package scsearch

import scala.collection.SortedSet

/**
* Created by grego on 17.05.2017.
*/
class SearchService {
  private[scsearch] var termFrequency = Map[String, Long]()
  private[scsearch] var termDocumentFrequency = Map[String, SortedSet[(Document, Long)]]().withDefaultValue(SortedSet[(Document, Long)]())

  def index(d: Document): Unit = {
    val (newTF, newTDF) =  d.termCount.foldLeft((termFrequency, termDocumentFrequency))
    { case ((tf, tdf), (term, count)) =>
      val stringToTuples = tdf + (term -> (tdf(term) + ((d, count))))
      (
        tf + (term -> (tf.getOrElse(term, 0L) + 1l)),
        stringToTuples
      )
    }
    termFrequency = newTF
    termDocumentFrequency = newTDF
  }

  def getTfIdfVec(doc: Document) = {
    val termTFIDF = doc.termCount.map {case (term, count) =>
      (term,count.toDouble / termFrequency(term))
    }
    TermVector(termTFIDF)
  }

  def query(q: String) = {
    val query = Document(q)
    val queryTermCount = query.termCount
    val qvec = getTfIdfVec(query).normalized
    val map = queryTermCount.keys.flatMap(k=>termDocumentFrequency(k)).map({case (doc, c)=>getTfIdfVec(doc).normalized})
    map.toSeq.sortWith{(v1,v2)=> qvec * v1 > qvec * v2}
  }
}
