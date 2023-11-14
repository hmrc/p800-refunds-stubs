/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.p800refundsstubs.testsupport

import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.{DefaultTestServerFactory, RunningServer}
import play.api.{Application, Mode}
import play.core.server.ServerConfig
import testsupport.RichMatchers
import testsupport.wiremock.WireMockSupport

trait ItSpec extends AnyFreeSpecLike
  with RichMatchers
  with GuiceOneServerPerSuite
  with WireMockSupport
  with Matchers {

  private val testServerPort = 19001
  private val databaseName: String = "p800-refunds-frontend-it"

  protected lazy val configMap: Map[String, Any] = Map[String, Any](
    "mongodb.uri" -> s"mongodb://localhost:27017/$databaseName",
    "play.http.router" -> "testOnlyDoNotUseInAppConf.Routes",
    "auditing.consumer.baseUri.port" -> WireMockSupport.port,
    "auditing.enabled" -> false,
    "auditing.traceRequests" -> false
  )

  override def fakeApplication(): Application = new GuiceApplicationBuilder()
    .configure(configMap).build()

  override implicit protected lazy val runningServer: RunningServer =
    TestServerFactory.start(app)

  object TestServerFactory extends DefaultTestServerFactory {
    override protected def serverConfig(app: Application): ServerConfig = {
      val sc = ServerConfig(port    = Some(testServerPort), sslPort = Some(0), mode = Mode.Test, rootDir = app.path)
      sc.copy(configuration = sc.configuration.withFallback(overrideServerConfiguration(app)))
    }
  }
}
