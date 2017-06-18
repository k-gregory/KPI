package scsearch

/**
* Created by grego on 17.05.2017.
*/
case class TermVector(private var termCount: Map[String, Double]) {
  def apply(term: String): Double = termCount(term)

  def *(that: TermVector): Double = termCount.map({ case (t, c) => that.termCount.getOrElse(t, 0d) * c }).sum

  def normalized = {
    val normCoef = 1 / Math.sqrt(termCount.values.map(c => c * c).sum)
    TermVector(termCount mapValues (_ * normCoef))
  }
}
