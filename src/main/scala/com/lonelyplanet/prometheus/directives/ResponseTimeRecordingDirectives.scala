package com.lonelyplanet.prometheus.directives

import akka.http.scaladsl.server.ExceptionHandler
import akka.http.scaladsl.server.directives.{BasicDirectives, ExecutionDirectives}
import com.lonelyplanet.prometheus.ResponseTimeRecorder

import scala.concurrent.duration
import scala.concurrent.duration.FiniteDuration
import scala.util.control.NonFatal

trait ResponseTimeRecordingDirectives {
  this: ResponseTimeRecorderProvider =>

  /**
   * Directive that will record response time in a prometheus histogram
   *
   * @param endpoint the endpoint label value in the histogram
   * @return a new directive that records request latencies in a prometheus histogram
   */
  def recordResponseTime(endpoint: String) = BasicDirectives.extractRequestContext.flatMap { ctx =>
    val requestStartTime = System.nanoTime()
    BasicDirectives.mapResponse { resp =>
      record(endpoint, requestStartTime)
      resp
    } & ExecutionDirectives.handleExceptions {
      responseTimeRecordingExceptionHandler(endpoint, requestStartTime)
    }
  }

  private def responseTimeRecordingExceptionHandler(endpoint: String, requestStartTime: Long) = ExceptionHandler {
    case NonFatal(e) =>
      record(endpoint, requestStartTime)

      // Rethrow the exception to allow proper handling
      // from handlers higher ip in the hierarchy
      throw e
  }

  private def record(endpoint: String, requestStartTime: Long): Unit = {
    val requestEndTime = System.nanoTime()
    val total = new FiniteDuration(requestEndTime - requestStartTime, duration.NANOSECONDS)

    recorder.recordResponseTime(endpoint, total)
  }
}

object ResponseTimeRecordingDirectives {
  def apply(r: ResponseTimeRecorder) = {
    new ResponseTimeRecordingDirectives with ResponseTimeRecorderProvider {
      override def recorder: ResponseTimeRecorder = r
    }
  }
}

trait ResponseTimeRecorderProvider {
  def recorder: ResponseTimeRecorder
}
