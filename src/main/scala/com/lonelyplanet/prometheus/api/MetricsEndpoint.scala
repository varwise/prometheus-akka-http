package com.lonelyplanet.prometheus.api

import akka.http.scaladsl.server.Directives._
import io.prometheus.client.CollectorRegistry

class MetricsEndpoint(registry: CollectorRegistry) {

  val routes = {
    get {
      path("metrics") {
        complete {
          MetricFamilySamplesEntity.fromRegistry(registry)
        }
      }
    }
  }

}
