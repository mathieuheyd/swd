name := "swd"
version := "0.1"
scalaVersion := "2.12.4"

lazy val global = project
  .in(file("."))
  .settings(settings)
  .aggregate(
    engine,
    webServer
  )

lazy val engine = project
  .settings(
    name := "engine",
    settings,
    libraryDependencies ++= commonDependencies
  )

lazy val webServer = project
  .settings(
    name := "web-server",
    settings,
    libraryDependencies ++= commonDependencies ++ Seq(
      dependencies.akkahttp,
      dependencies.akkastream
    )
  )

lazy val settings = Seq()

lazy val commonDependencies = Seq(
  dependencies.scalatest
)

lazy val dependencies =
  new {
    val scalatest = "org.scalatest" % "scalatest_2.12" % "3.0.4" % "test"
    val akkahttp = "com.typesafe.akka" %% "akka-http"   % "10.1.0"
    val akkastream = "com.typesafe.akka" %% "akka-stream" % "2.5.11"
  }