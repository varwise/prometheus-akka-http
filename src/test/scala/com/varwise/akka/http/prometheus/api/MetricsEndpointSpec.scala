package com.varwise.akka.http.prometheus.api

import akka.http.scaladsl.model.HttpCharsets
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.varwise.akka.http.prometheus.Utils._
import io.prometheus.client.exporter.common.TextFormat
import io.prometheus.client.{CollectorRegistry, Histogram}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.io.StringWriter
import scala.util.Random

class MetricsEndpointSpec extends AnyFlatSpec with Matchers with ScalatestRouteTest {

  "Metrics endpoint" should "return the correct media type and charset" in {
    val api = createEndpoint(CollectorRegistry.defaultRegistry)
    Get("/metrics") ~> api.routes ~> check {
      mediaType.subType shouldBe "plain"
      mediaType.isText shouldBe true
      mediaType.params shouldBe Map("version" -> "0.0.4")
      charset shouldBe HttpCharsets.`UTF-8`
    }
  }

  it should "return serialized metrics in the prometheus text format" in {
    val registry = new CollectorRegistry()
    val api = createEndpoint(registry)
    val hist = Histogram.build().name(RandomTestName).help(RandomTestHelp).linearBuckets(0, 1, 10).register(registry)

    hist.observe(Math.abs(Random.nextDouble()))

    Get("/metrics") ~> api.routes ~> check {
      val resp = responseAs[String]
      val writer = new StringWriter()
      TextFormat.write004(writer, registry.metricFamilySamples())

      resp shouldBe writer.toString
    }
  }

  private val RandomTestName = generateRandomStringOfLength(16)
  private val RandomTestHelp = generateRandomStringOfLength(16)

  private def createEndpoint(collectorRegistry: CollectorRegistry) =
    new MetricsEndpoint(collectorRegistry)

}
