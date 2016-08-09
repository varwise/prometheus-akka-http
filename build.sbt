import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys

name := "prometheus-akka-http"

organization := "com.lonelyplanet"

version := "0.2.1"

scalaVersion := "2.11.8"

resolvers += "Sonatype release repository" at "https://oss.sonatype.org/content/repositories/releases/"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaVersion = "2.4.8"
  val scalaTestVersion      = "3.0.0-M15"

  Seq(
    "com.typesafe.akka"    %% "akka-actor"                           % akkaVersion % "provided",
    "com.typesafe.akka"    %% "akka-stream"                          % akkaVersion % "provided",
    "com.typesafe.akka"    %% "akka-http-core"                       % akkaVersion % "provided",
    "com.typesafe.akka"    %% "akka-http-experimental"               % akkaVersion % "provided",
    "com.typesafe.akka"    %% "akka-http-spray-json-experimental"    % akkaVersion % "provided",
    "io.prometheus"        %  "simpleclient"                         % "0.0.15",
    "io.prometheus"        %  "simpleclient_common"                  % "0.0.15",
    "org.scalamock"        %% "scalamock-scalatest-support"          % "3.2.2" % "test",
    "com.typesafe.akka"    %% "akka-http-testkit"                    % akkaVersion % "test",
    "org.scalatest"        %% "scalatest"                            % scalaTestVersion % "test"
  )
}

fork := true

SbtScalariform.scalariformSettings

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(SpacesAroundMultiImports, false)
  .setPreference(CompactControlReadability, false)

bintrayOrganization := Some("lonelyplanet")

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

val doNotPublishSettings = Seq(publish := {})

val publishSettings =
  if (version.toString.endsWith("-SNAPSHOT"))
    Seq(
      publishTo := Some("Artifactory Realm" at "http://oss.jfrog.org/artifactory/oss-snapshot-local"),
      bintrayReleaseOnPublish := false,
      credentials := List(Path.userHome / ".bintray" / ".artifactory").filter(_.exists).map(Credentials(_))
    )
  else
    Seq(
      organization := "com.lonelyplanet",
      pomExtra := <scm>
        <url>https://github.com/lonelyplanet/prometheus-akka-http</url>
        <connection>https://github.com/lonelyplanet/prometheus-akka-http</connection>
      </scm>
        <developers>
          <developer>
            <id>toddkazakov</id>
            <name>Todd Kazakov</name>
            <url>https://github.com/toddkazakov</url>
          </developer>
        </developers>,
      publishArtifact in Test := false,
      homepage := Some(url("https://github.com/lonelyplanet/prometheus-akka-http")),
      publishMavenStyle := false,
      resolvers += Resolver.url("lonelyplanet ivy resolver", url("http://dl.bintray.com/lonelyplanet/maven"))(Resolver.ivyStylePatterns)
    )
