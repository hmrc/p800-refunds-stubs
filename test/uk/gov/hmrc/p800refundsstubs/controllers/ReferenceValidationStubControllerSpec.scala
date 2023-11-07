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

package uk.gov.hmrc.p800refundsstubs.controllers

import akka.stream.Materializer
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.http.HeaderNames.CONTENT_TYPE
import play.api.http.Status
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.p800refundsstubs.models.ReferenceValidationRequest

import scala.concurrent.Future

class ReferenceValidationStubControllerSpec extends AnyWordSpec with Matchers with GuiceOneAppPerSuite {

  private val controller = app.injector.instanceOf[ReferenceValidationStubController]
  implicit lazy val materializer: Materializer = app.materializer

  "POST /" should {
    "Valid reference responds with isValid true" in {
      val fakeRequest = FakeRequest()
        .withBody(ReferenceValidationRequest("VALLIDREF1"))
        .withHeaders(CONTENT_TYPE -> JSON)

      val result: Future[Result] = controller.validateReference()(fakeRequest)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.parse("""{ "isValid": true} """)
    }

    "Invalid reference responds with isValid false" in {
      val fakeRequest = FakeRequest()
        .withBody(ReferenceValidationRequest("INVALIDREF1"))
        .withHeaders(CONTENT_TYPE -> JSON)

      val result: Future[Result] = controller.validateReference()(fakeRequest)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.parse("""{ "isValid": false} """)
    }
  }
}
