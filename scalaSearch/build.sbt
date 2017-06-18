resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

updateOptions := updateOptions.value.withLatestSnapshots(false)

lazy val root = (project in file(".")).settings(
  name := "scala-search",
  version := "1.0.0-SNAPSHOT",
  scalaVersion := "2.12.2",
  libraryDependencies += "com.github.ichoran" %% "thyme" % "0.1.2-SNAPSHOT"
)