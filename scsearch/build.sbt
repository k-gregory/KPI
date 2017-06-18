scalaVersion := "2.12.2"

flywayUrl:="jdbc:h2:file:/home/gregory/Documents/programming/scsearch/data/db"

libraryDependencies ++= {
  val akkaVersion = "2.4.18"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-http" % "10.0.7",
    "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.7",

    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "ch.qos.logback" % "logback-classic" % "1.2.3",

    "io.getquill" %% "quill" % "1.2.1",
    "io.getquill" %% "quill-jdbc" % "1.2.1",
    "com.h2database" % "h2" % "1.4.195",

    "org.jsoup" % "jsoup" % "1.10.2",
    "org.scala-lang.modules" %% "scala-xml" % "1.0.6"
  )
}