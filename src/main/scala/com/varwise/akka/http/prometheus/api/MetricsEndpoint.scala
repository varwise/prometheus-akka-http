package com.varwise.akka.http.prometheus.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import io.prometheus.client.CollectorRegistry

class MetricsEndpoint(registry: CollectorRegistry) {

  val routes: Route = {
    get {
      path("metrics") {
        complete {
          MetricFamilySamplesEntity.fromRegistry(registry)
        }
      }
    }
  }

}
