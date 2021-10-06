name := "prometheus-akka-http"

organization := "com.varwise"

publishTo := sonatypePublishToBundle.value

version := "0.5.2-SNAPSHOT"

val scala2Version = "2.13.6"
val scala3Version = "3.0.2"

scalaVersion := scala3Version
crossScalaVersions := Seq(scala3Version, scala2Version)

libraryDependencies ++= {
  val simpleclientVersion = "0.12.0"
  val akkaVersion = "2.6.16"
  val akkaHttpVersion = "10.2.6"
  val scalaTestVersion = "3.2.10"

  Seq(
    ("com.typesafe.akka" %% "akka-actor"           % akkaVersion     % Provided).cross(CrossVersion.for3Use2_13),
    ("com.typesafe.akka" %% "akka-stream"          % akkaVersion     % Provided).cross(CrossVersion.for3Use2_13),
    ("com.typesafe.akka" %% "akka-http"            % akkaHttpVersion % Provided).cross(CrossVersion.for3Use2_13),
    ("com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion % Provided).cross(CrossVersion.for3Use2_13),
    "io.prometheus" % "simpleclient"        % simpleclientVersion,
    "io.prometheus" % "simpleclient_common" % simpleclientVersion,
    ("com.typesafe.akka" %% "akka-testkit"      % akkaVersion     % Test).cross(CrossVersion.for3Use2_13),
    ("com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test).cross(CrossVersion.for3Use2_13),
    "org.scalatest" %% "scalatest" % scalaTestVersion % Test
  )
}

lazy val `root` = (project in file("."))
  .settings(
    addCommandAlias("testAll", ";test"),
    addCommandAlias("formatAll", ";scalafmt;Test/scalafmt;scalafmtSbt"),
    addCommandAlias("compileAll", ";compile;Test/compile")
  )
