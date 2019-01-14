package com.lonelyplanet.prometheus

import com.lonelyplanet.prometheus.Utils._
import io.prometheus.client.CollectorRegistry
import org.scalatest.{FlatSpec, Matchers}

class PrometheusEventObserverSpec extends FlatSpec with Matchers {

  "PrometheusEventObserver" should "record observed events in a counter" in {
    val registry                    = new CollectorRegistry()
    val randomMetricName            = generateRandomString
    val randomMetricHelp            = generateRandomString
    val randomEventLabelName        = generateRandomString
    val randomEventDetailsLabelName = generateRandomString

    val randomEventName    = generateRandomString
    val randomEventDetails = generateRandomString

    val eventObserver = new PrometheusEventObserver(
      randomMetricName,
      randomMetricHelp,
      randomEventLabelName,
      randomEventDetailsLabelName,
      registry
    )

    def getCounterValue = {
      registry.getSampleValue(
        randomMetricName,
        Array(randomEventLabelName, randomEventDetailsLabelName),
        Array(randomEventName, randomEventDetails)
      )
    }

    getCounterValue shouldBe null

    eventObserver.observe(randomEventName, randomEventDetails)

    getCounterValue should not be null
    getCounterValue.intValue() shouldBe 1

  }
}
