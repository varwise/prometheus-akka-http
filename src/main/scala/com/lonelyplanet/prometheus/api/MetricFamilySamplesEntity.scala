package com.lonelyplanet.prometheus.api

import java.io.{StringWriter, Writer}
import java.util

import akka.http.scaladsl.marshalling.{Marshaller, ToEntityMarshaller}
import akka.http.scaladsl.model._
import io.prometheus.client.Collector.MetricFamilySamples
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.exporter.common.TextFormat

case class MetricFamilySamplesEntity(samples: util.Enumeration[MetricFamilySamples])

object MetricFamilySamplesEntity {
  val version = "0.0.4"

  private val mediaType = MediaType.customWithFixedCharset(
    mainType = "text",
    subType = "plain",
    charset = HttpCharsets.`UTF-8`,
    params = Map("version" -> version)
  )

  def fromRegistry(collectorRegistry: CollectorRegistry): MetricFamilySamplesEntity = {
    MetricFamilySamplesEntity(collectorRegistry.metricFamilySamples())
  }

  def toPrometheusTextFormat(e: MetricFamilySamplesEntity): String = {
    val writer: Writer = new StringWriter()
    TextFormat.write004(writer, e.samples)
    writer.toString
  }

  implicit val metricsFamilySamplesMarshaller: ToEntityMarshaller[MetricFamilySamplesEntity] = {
    Marshaller.withFixedContentType(mediaType) { s =>
      HttpEntity(mediaType, toPrometheusTextFormat(s))
    }
  }

}
