name := "prometheus-akka-http"

organization := "com.varwise"

publishTo := sonatypePublishToBundle.value

version := "0.5.2-SNAPSHOT"

scalaVersion := "2.13.6"

libraryDependencies ++= {
  val simpleclientVersion = "0.12.0"
  val akkaVersion = "2.6.16"
  val akkaHttpVersion = "10.2.6"
  val scalaTestVersion = "3.2.10"
  val scalamockVersion = "5.1.0"

  Seq(
    "com.typesafe.akka" %% "akka-actor"           % akkaVersion % Provided,
    "com.typesafe.akka" %% "akka-stream"          % akkaVersion % Provided,
    "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion % Provided,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion % Provided,
    "io.prometheus"     % "simpleclient"          % simpleclientVersion,
    "io.prometheus"     % "simpleclient_common"   % simpleclientVersion,
    "org.scalamock"     %% "scalamock"            % scalamockVersion % Test,
    "com.typesafe.akka" %% "akka-testkit"         % akkaVersion % Test,
    "com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpVersion % Test,
    "org.scalatest"     %% "scalatest"            % scalaTestVersion % Test
  )
}

lazy val `root` = (project in file("."))
  .settings(
    addCommandAlias("testAll", ";test"),
    addCommandAlias("formatAll", ";scalafmt;Test/scalafmt;scalafmtSbt"),
    addCommandAlias("compileAll", ";compile;Test/compile")
  )
