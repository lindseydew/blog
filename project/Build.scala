import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "blog"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    "com.novus" %% "salat" % "1.9.2-SNAPSHOT",
    "org.mongodb" %% "casbah" % "2.6.2",
    "org.mongodb" % "mongo-java-driver" % "2.7.2",
    "org.planet42" %% "laika" % "0.2.0"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(defaultScalaSettings:_*).settings(
    // Add your own project settings here
    resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  )
}
