name := "swd"
version := "0.1"
scalaVersion := "2.12.4"

lazy val global = project
  .in(file("."))
  .settings(settings)
  .aggregate(
    engine
  )

lazy val engine = project
  .settings(
    name := "engine",
    settings,
    libraryDependencies ++= commonDependencies
  )

lazy val settings = Seq()

lazy val commonDependencies = Seq(
  dependencies.scalatest
)

lazy val dependencies =
  new {
    val scalatest = "org.scalatest" % "scalatest_2.12" % "3.0.4" % "test"
  }