# prometheus-akka-http

Collection of utilities to allow exposing prometheus metrics from akka-http endpoint using the prometheus java client

    "com.varwise" %% "prometheus-akka-http" % "0.6.0"

# Pekko support

[prometheus-pekko-http](https://github.com/varwise/prometheus-pekko-http) is a fork of this project that supports pekko-http.

### Example

Example project is available here: https://github.com/wlk/prometheus-akka-http-example

### Publishing

Artifacts are published to Maven central using the [sbt-sonatype](https://github.com/xerial/sbt-sonatype) plugin

Building howto:
```
sbt
; + publishSigned; sonatypeBundleRelease
```