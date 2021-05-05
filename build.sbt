name := "prometheus-akka-http"

organization := "com.varwise"

publishTo := sonatypePublishToBundle.value

version := "0.5.2-SNAPSHOT"

scalaVersion := "2.13.5"

libraryDependencies ++= {
  val simpleclientVersion = "0.8.0"
  val akkaVersion = "2.6.0"
  val akkaHttpVersion = "10.1.11"
  val scalaTestVersion = "3.1.0"

  Seq(
    "com.typesafe.akka" %% "akka-actor"           % akkaVersion % Provided,
    "com.typesafe.akka" %% "akka-stream"          % akkaVersion % Provided,
    "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion % Provided,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion % Provided,
    "io.prometheus"     % "simpleclient"          % simpleclientVersion,
    "io.prometheus"     % "simpleclient_common"   % simpleclientVersion,
    "org.scalamock"     %% "scalamock"            % "4.3.0" % Test,
    "com.typesafe.akka" %% "akka-testkit"         % akkaVersion % Test,
    "com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpVersion % Test,
    "org.scalatest"     %% "scalatest"            % scalaTestVersion % Test
  )
}

lazy val `root` = (project in file("."))
  .settings(
    addCommandAlias("testAll", ";test"),
    addCommandAlias("formatAll", ";scalafmt;test:scalafmt;scalafmtSbt"),
    addCommandAlias("compileAll", ";compile;test:compile")
  )
