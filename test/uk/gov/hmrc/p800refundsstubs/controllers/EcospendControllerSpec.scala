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

import play.api.libs.json._
import uk.gov.hmrc.p800refundsstubs.models._
import uk.gov.hmrc.p800refundsstubs.testsupport.ItSpec

import java.util.{Currency, UUID}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EcospendControllerSpec extends ItSpec {

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
      calculatedOwnerName   = CalculatedOwnerName("Alice Crawford"),
      accountOwnerName      = AccountOwnerName("Alice Crawford"),
      displayName           = DisplayName("Alice B Crawford"),
      balance               = 123.7,
      lastUpdateTime        = localDateTime,
      parties               = List(BankAccountParty(
        name          = BankPartyName("Alice Crawford"),
        fullLegalName = BankPartyFullLegalName("Alice Crawford")
      ))
    )))

    implicitly[Format[BankAccountSummaryResponse]].reads(input) shouldBe JsSuccess(expectedBankAccountSummaryResponse)
  }
}
