name := "swd"
version := "0.1"
scalaVersion := "2.12.4"

lazy val global = project
  .in(file("."))
  .settings(settings)
  .aggregate(
    engine,
    matchMaking,
    webServer
  )

lazy val engine = project
  .in(file("./engine"))
  .settings(
    name := "engine",
    settings,
    libraryDependencies ++= commonDependencies
  )

lazy val matchMaking = project
  .in(file("./match-making"))
  .settings(
    name := "match-making",
    settings,
    libraryDependencies ++= commonDependencies
  )

lazy val webServer = project
  .in(file("./web-server"))
  .settings(
    name := "web-server",
    settings,
    libraryDependencies ++= commonDependencies ++ Seq(
      dependencies.akkahttp,
      dependencies.akkastream,
      dependencies.argonaut,
      dependencies.argonautscalaz,
      dependencies.argonautMonocle,
      dependencies.argonautCats
    )
  )
  .dependsOn(matchMaking, engine)

lazy val settings = Seq()

lazy val commonDependencies = Seq(
  dependencies.scalatest
)

lazy val dependencies =
  new {
    val scalatest = "org.scalatest" % "scalatest_2.12" % "3.0.4" % "test"
    val akkahttp = "com.typesafe.akka" %% "akka-http"   % "10.1.0"
    val akkastream = "com.typesafe.akka" %% "akka-stream" % "2.5.11"
    val argonaut = "io.argonaut" %% "argonaut" % "6.2"
    val argonautscalaz = "io.argonaut" %% "argonaut-scalaz" % "6.2"
    val argonautMonocle = "io.argonaut" %% "argonaut-monocle" % "6.2"
    val argonautCats = "io.argonaut" %% "argonaut-cats" % "6.2"
  }