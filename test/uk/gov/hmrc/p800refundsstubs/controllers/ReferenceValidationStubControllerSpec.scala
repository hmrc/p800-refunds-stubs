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
import play.api.http.HeaderNames.CONTENT_TYPE
import play.api.http.Status
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.p800refundsstubs.models.{ReferenceValidationRequest, ReferenceValidationResponse}
import uk.gov.hmrc.p800refundsstubs.testsupport.ItSpec

import scala.concurrent.Future

class ReferenceValidationStubControllerSpec extends ItSpec {

  private val controller = app.injector.instanceOf[ReferenceValidationStubController]
  implicit lazy val materializer: Materializer = app.materializer

  "POST /" - {
    "Valid reference responds with isValid true" in {
      val fakeRequest = FakeRequest()
        .withBody(ReferenceValidationRequest("VALIDREF1"))
        .withHeaders(CONTENT_TYPE -> JSON)

      val result: Future[Result] = controller.validateReference()(fakeRequest)

      status(result) shouldBe Status.OK
      contentAsJson(result).as[ReferenceValidationResponse].isValid shouldBe true
    }

    "Invalid reference responds with isValid false" in {
      val fakeRequest = FakeRequest()
        .withBody(ReferenceValidationRequest("INVALIDREF1"))
        .withHeaders(CONTENT_TYPE -> JSON)

      val result: Future[Result] = controller.validateReference()(fakeRequest)

      status(result) shouldBe Status.OK
      contentAsJson(result).as[ReferenceValidationResponse].isValid shouldBe false
    }
  }
}
