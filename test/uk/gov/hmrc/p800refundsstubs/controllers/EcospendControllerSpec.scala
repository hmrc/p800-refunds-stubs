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

import play.api.http.HeaderNames.CONTENT_TYPE
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.p800refundsstubs.models.bankverification._
import uk.gov.hmrc.p800refundsstubs.testsupport.ItSpec

class EcospendControllerSpec extends ItSpec {

  private val ecospendController = app.injector.instanceOf[EcospendController]

  private def fakeRequest(identifier: String): FakeRequest[BankVerificationRequest] = FakeRequest()
    .withBody(BankVerificationRequest(identifier))
    .withHeaders(CONTENT_TYPE -> JSON, AUTHORIZATION -> "Bearer 1234567890")

  private def prepMongo(identifier: String, verificationStatus: VerificationStatus) =
    bankVerificationRepo
      .insert(BankVerificationEntry(java.time.Instant.now(), BankVerification(identifier, verificationStatus)))
      .futureValue

  "POST /notification" - {
    "return 402 (PaymentRequired) if no entry in mongo" in {
      val result = ecospendController.notification(fakeRequest("AB123456A"))
      status(result) shouldBe PAYMENT_REQUIRED
    }

    "return Successful when that is the value in mongo" in {
      val identifier: String = "LM001014C"
      prepMongo(identifier, VerificationStatus.Successful)
      val result = ecospendController.notification(fakeRequest(identifier))
      status(result) shouldBe 200
      //language=JSON
      val expectedJsonResponse = """{"identifier":"LM001014C","verificationStatus":"Successful"}"""
      contentAsString(result) shouldBe expectedJsonResponse
    }

    "return UnSuccessful when that is the value in mongo" in {
      val identifier: String = "MA000003B"
      prepMongo(identifier, VerificationStatus.UnSuccessful)
      val result = ecospendController.notification(fakeRequest(identifier))
      status(result) shouldBe 200
      //language=JSON
      val expectedJsonResponse = """{"identifier":"MA000003B","verificationStatus":"UnSuccessful"}"""
      contentAsString(result) shouldBe expectedJsonResponse
    }

    "update to a status when called a second time by the frontend" in {
      val identifier: String = "AB123456C"
      val firstCall = ecospendController.notification(fakeRequest(identifier))
      status(firstCall) shouldBe PAYMENT_REQUIRED
      val secondCall = ecospendController.notification(fakeRequest(identifier))
      status(secondCall) shouldBe 200
      //language=JSON
      val expectedJsonResponse = """{"identifier":"AB123456C","verificationStatus":"Successful"}"""
      contentAsString(secondCall) shouldBe expectedJsonResponse
    }
  }
}
