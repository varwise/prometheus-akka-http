package com.lonelyplanet.prometheus

import io.prometheus.client.{Counter, CollectorRegistry}

trait EventObserver {
  def observe(eventName: String, eventDetails: String): Unit
}

/**
 * Records observed events in a prometheus counter
 *
 * @param metricName the metric name
 * @param metricHelp the metric help message
 * @param eventLabelName the event label name that will be applied to the counter when recording events
 * @param eventDetailsLabelName the event details label name that will be applied to the counter when recording events
 * @param registry a prometheus registry to which the counter will be registered
 */
class PrometheusEventObserver(
    metricName: String,
    metricHelp: String,
    eventLabelName: String,
    eventDetailsLabelName: String,
    registry: CollectorRegistry) extends EventObserver {

  val counter = buildCounter.register(registry)

  private def buildCounter = Counter
    .build()
    .name(metricName)
    .help(metricHelp)
    .labelNames(eventLabelName, eventDetailsLabelName)

  override def observe(eventName: String, eventDetails: String): Unit = {
    counter.labels(eventName, eventDetails).inc()
  }
}

object PrometheusEventObserver {
  private val SuccessfulOperationMetricName = "operation_success"
  private val SuccessfulOperationMetricHelp = "The number of observed successful operations"
  private val FailedOperationMetricName = "operation_failure"
  private val FailedOperationMetricHelp = "The number of observed failed operations"
  private val DefaultEventLabelName = "event"
  private val DefaultEventDetailsLabelName = "details"
  private val DefaultRegistry = CollectorRegistry.defaultRegistry

  // Common event observers used in scala projects in Open Planet micro-services
  lazy val SuccessfulOperations = withDefaultsFromMetricNameAndHelp(SuccessfulOperationMetricName, SuccessfulOperationMetricHelp)
  lazy val FailedOperations = withDefaultsFromMetricNameAndHelp(FailedOperationMetricName, FailedOperationMetricHelp)

  private def withDefaultsFromMetricNameAndHelp(metricName: String, metricHelp: String) = {
    new PrometheusEventObserver(
      metricName,
      metricHelp,
      DefaultEventLabelName,
      DefaultEventDetailsLabelName,
      DefaultRegistry)
  }
}

class NoOpEventObserver extends EventObserver {
  def observe(eventName: String, eventDetails: String): Unit = ()
}
