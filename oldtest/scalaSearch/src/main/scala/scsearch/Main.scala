package scsearch

import scala.io.Source

/**
* Created by grego on 17.05.2017.
*/
object Main extends App {

  val resource = Source.fromResource("pap.txt").mkString
  private var search = new SearchService()

  private val s = resource.mkString
  val document = Document(s)
  for(i<-0 to 5)
  search.index(Document(s+i))
  search = new SearchService()

  var l = System.currentTimeMillis()
  search.index(Document(s))
  println(System.currentTimeMillis() - l)
  /*
  search.index(Document("hi there fuck"))
  search.index(Document("fuck you"))
  search.index(Document("fuck you, you ASShole"))
  search.index(Document("asshole in sight, lol"))
  */
  /*Seq(
    "Fock you",
    "How are you?",
    "Fock you, you asshole",
    "Are?,?HOW??"
  ) foreach (d=>search.index(Document(d)))
  */
  //println(search.query("are"))
  /*
  private val ss = search.termDocumentFrequency.map({case (word, usg)=>
    (word, usg.firstKey._2)
  }).toSeq.sortBy(_._2)

  println(s"Total: ${ss.size}")
  for(i<- 1 to 10) println(s"Met $i times: ${ss.count(_._2 == i)}")
  println(ss)
  */
}
