package scsearch

import java.util.concurrent.locks.ReentrantReadWriteLock

import ichi.bench.Thyme

/*
object Benchmark extends App {

  final val n = 2000

  private val lock = new ReentrantReadWriteLock()

  var javaMap = new java.util.HashMap[Int, Int]()

  @volatile
  var scalaMap = Map.empty[Int, Int]

  def fillJava() : Unit = {
    javaMap.clear()
    for (i <- 1 to n)
      javaMap.put(i, i + 1)
  }

  def fillScala(): Unit = {
    scalaMap = Map.empty[Int, Int]
    for(i <- 1 to n)
      scalaMap = scalaMap.updated(i, i + 1)
  }

  val th = ichi.bench.Thyme.warmed(verbose = println, warmth = Thyme.HowWarm.BenchOff)

  th.pbenchOffWarm("mutableJavaVsImmutableScala")(th.Warm(fillJava()))(th.Warm(fillScala()))
  //th.pbenchOffWarm("mutableJavaVsMutableLongScala")(th.Warm(lookupJava()))(th.Warm(lookupScalaMutableLong()))
}
*/