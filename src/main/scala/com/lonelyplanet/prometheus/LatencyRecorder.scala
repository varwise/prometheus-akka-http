package com.lonelyplanet.prometheus

import io.prometheus.client.{CollectorRegistry, Histogram}

import scala.concurrent.duration.{FiniteDuration, TimeUnit}

trait LatencyRecorder {
  def recordRequestLatency(endpoint: String, latency: FiniteDuration): Unit
}

/**
 * Records request latencies in Prometheus histogram.
 * Reported values will be automatically converted to the time unit set as constructor argument.
 *
 * @param metricName the metric name
 * @param metricHelp the metric help message
 * @param buckets the buckets that will be used in the histogram
 * @param endpointLabelName the endpoint label name that will be applied to the histogram when recording request latency
 * @param registry a prometheus histogram to which the histogram will be registered
 * @param timeUnit the time unit in which observed values will be recorded.
 */
class PrometheusLatencyRecorder(
    metricName: String,
    metricHelp: String,
    buckets: List[Double],
    endpointLabelName: String,
    registry: CollectorRegistry,
    timeUnit: TimeUnit
) extends LatencyRecorder {

  private val requestLatency = buildHistogram.register(registry)

  override def recordRequestLatency(endpoint: String, latency: FiniteDuration): Unit = {
    requestLatency.labels(endpoint).observe(latency.toUnit(timeUnit))
  }

  private def buildHistogram = Histogram.build()
    .name(metricName)
    .help(metricHelp)
    .labelNames(endpointLabelName)
    .buckets(buckets: _*)

}