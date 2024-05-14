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
import play.api.libs.json._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.p800refundsstubs.models._
import uk.gov.hmrc.p800refundsstubs.models.bankverification._
import uk.gov.hmrc.p800refundsstubs.testsupport.ItSpec

import java.util.{Currency, UUID}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

  "Validate custom JSON parsing for BankAccountSummaryResponse" in {
    //language=JSON
    val input = Json.parse(
      """
        [
          {
            "id": "cddd0273-b709-4ee7-b73d-7113dd7a7d66",
            "bank_id": "obie-barclays-personal",
            "type": "Personal",
            "sub_type": "CurrentAccount",
            "currency": "GBP",
            "account_format": "SortCode",
            "account_identification": "abc:123",
            "calculated_owner_name": "Alice Crawford",
            "account_owner_name": "Alice Crawford",
            "display_name": "Alice B Crawford",
            "balance": 123.7,
            "last_update_time": "2024-02-13T12:52:45.081236",
            "parties": [
              {
                "name": "Alice Crawford",
                "full_legal_name": "Alice Crawford"
              }
            ]
          }
        ]""".stripMargin
    )

    val localDateTime: LocalDateTime = {
      LocalDateTime.parse("2024-02-13T12:52:45.081236", DateTimeFormatter.ISO_DATE_TIME)
    }

    val expectedBankAccountSummaryResponse: BankAccountSummaryResponse = BankAccountSummaryResponse(List(BankAccountSummary(
      id                    = UUID.fromString("cddd0273-b709-4ee7-b73d-7113dd7a7d66"),
      bankId                = "obie-barclays-personal",
      merchantId            = None,
      merchantUserId        = None,
      ttype                 = BankAccountType.Personal,
      subType               = BankAccountSubType.CurrentAccount,
      currency              = Currency.getInstance("GBP"),
      accountFormat         = BankAccountFormat.SortCode,
      accountIdentification = AccountIdentification("abc:123"),
      calculatedOwnerName   = CalculatedOwnerName("Greg Greggson"),
      accountOwnerName      = AccountOwnerName("Greg Greggson"),
      displayName           = DisplayName("Greg G Greggson"),
      balance               = 123.7,
      lastUpdateTime        = localDateTime,
      parties               = List(BankAccountParty(
        name          = BankPartyName("Greg Greggson"),
        fullLegalName = BankPartyFullLegalName("Greg Greggory Greggson")
      ))
    )))

    implicitly[Format[BankAccountSummaryResponse]].reads(input) shouldBe JsSuccess(expectedBankAccountSummaryResponse)
  }
}
