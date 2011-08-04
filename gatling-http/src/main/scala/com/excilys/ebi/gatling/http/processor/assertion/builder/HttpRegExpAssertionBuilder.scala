package com.excilys.ebi.gatling.http.processor.assertion.builder

import com.excilys.ebi.gatling.http.phase.HttpPhase
import com.excilys.ebi.gatling.http.phase.CompletePageReceived
import com.excilys.ebi.gatling.http.phase.HeadersReceived
import com.excilys.ebi.gatling.http.processor.assertion.HttpAssertion
import com.excilys.ebi.gatling.http.processor.assertion.HttpRegExpAssertion

object HttpRegExpAssertionBuilder {
  class HttpRegExpAssertionBuilder(expression: Option[String], expected: Option[String], attrKey: Option[String], httpPhase: Option[HttpPhase])
      extends HttpAssertionBuilder(expression, expected, attrKey, httpPhase) {
    def in(attrKey: String) = new HttpRegExpAssertionBuilder(expression, expected, Some(attrKey), httpPhase)

    def build: HttpAssertion = new HttpRegExpAssertion(expression.get, expected.get, attrKey, httpPhase.get)
  }

  def assertRegexp(expression: String, expected: String) = new HttpRegExpAssertionBuilder(Some(expression), Some(expected), None, Some(new CompletePageReceived))
}