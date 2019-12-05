import scalariform.formatter.preferences._

name := "prometheus-akka-http"

organization := "com.lonelyplanet"

version := "0.4.0"

crossScalaVersions := Seq("2.12.10", "2.11.12")

resolvers += "Sonatype release repository" at "https://oss.sonatype.org/content/repositories/releases/"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val simpleclientVersion = "0.8.0"
  val akkaVersion         = "2.6.0"
  val akkaHttpVersion     = "10.1.11"
  val scalaTestVersion    = "3.0.5"

  Seq(
    "com.typesafe.akka"    %% "akka-actor"                           % akkaVersion % Provided,
    "com.typesafe.akka"    %% "akka-stream"                          % akkaVersion % Provided,
    "com.typesafe.akka"    %% "akka-http"                            % akkaHttpVersion % Provided,
    "com.typesafe.akka"    %% "akka-http-spray-json"                 % akkaHttpVersion % Provided,
    "io.prometheus"        %  "simpleclient"                         % simpleclientVersion,
    "io.prometheus"        %  "simpleclient_common"                  % simpleclientVersion,
    "org.scalamock"        %% "scalamock-scalatest-support"          % "3.6.0" % Test,
    "com.typesafe.akka"    %% "akka-testkit"                         % akkaVersion % Test,
    "com.typesafe.akka"    %% "akka-http-testkit"                    % akkaHttpVersion % Test,
    "org.scalatest"        %% "scalatest"                            % scalaTestVersion % Test,
  )
}

fork := true

scalariformAutoformat := true

scalariformPreferences := scalariformPreferences.value
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(SpacesAroundMultiImports, false)
  .setPreference(CompactControlReadability, false)

bintrayOrganization := Some("lonelyplanet")

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

val publishSettings =
  if (version.toString.endsWith("-SNAPSHOT"))
    Seq(
      publishTo := Some("Artifactory Realm" at "http://oss.jfrog.org/artifactory/oss-snapshot-local"),
      bintrayReleaseOnPublish := false,
      credentials := List(Path.userHome / ".bintray" / ".artifactory").filter(_.exists).map(Credentials(_))
    )
  else
    Seq(
      pomExtra :=
        <scm>
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
