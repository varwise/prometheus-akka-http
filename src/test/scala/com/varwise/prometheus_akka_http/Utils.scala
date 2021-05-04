package com.varwise.prometheus_akka_http

import scala.util.Random

object Utils {
  def generateRandomString: String = generateRandomStringOfLength(16)

  def generateRandomStringOfLength(length: Int): String = {
    Random.alphanumeric.filter(_.isLetter).take(length).mkString("")
  }
}
