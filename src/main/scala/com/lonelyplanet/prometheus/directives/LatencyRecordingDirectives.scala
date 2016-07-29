package com.lonelyplanet.prometheus.directives

import akka.http.scaladsl.server.ExceptionHandler
import akka.http.scaladsl.server.directives.{BasicDirectives, ExecutionDirectives}
import com.lonelyplanet.prometheus.LatencyRecorder

import scala.concurrent.duration
import scala.concurrent.duration.FiniteDuration
import scala.util.control.NonFatal

trait LatencyRecordingDirectives {
  this: LatencyRecorderProvider =>

  /**
   * Directive that will record request latencies in a prometheus histogram
   *
   * @param endpoint the endpoint label value in the histogram
   * @return a new directive that records request latencies in a prometheus histogram
   */
  def recordRequestLatency(endpoint: String) = BasicDirectives.extractRequestContext.flatMap { ctx =>
    val requestStartTime = System.nanoTime()
    BasicDirectives.mapResponse { resp =>
      record(endpoint, requestStartTime)
      resp
    } & ExecutionDirectives.handleExceptions {
      requestLatencyRecordingExceptionHandler(endpoint, requestStartTime)
    }
  }

  private def requestLatencyRecordingExceptionHandler(endpoint: String, requestStartTime: Long) = ExceptionHandler {
    case NonFatal(e) =>
      record(endpoint, requestStartTime)

      // Rethrow the exception to allow proper handling
      // from handlers higher ip in the hierarchy
      throw e
  }

  private def record(endpoint: String, requestStartTime: Long): Unit = {
    val requestEndTime = System.nanoTime()
    val total = new FiniteDuration(requestEndTime - requestStartTime, duration.NANOSECONDS)

    recorder.recordRequestLatency(endpoint, total)
  }
}

object LatencyRecordingDirectives {
  def apply(r: LatencyRecorder) = {
    new LatencyRecordingDirectives with LatencyRecorderProvider {
      override def recorder: LatencyRecorder = r
    }
  }
}

trait LatencyRecorderProvider {
  def recorder: LatencyRecorder
}