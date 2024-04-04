/*
 * Copyright 2024 HM Revenue & Customs
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

package uk.gov.hmrc.p800refundsstubs.models.casemanagement

import play.api.libs.json.{Json, OFormat, Format}
import uk.gov.hmrc.p800refundsstubs.models.Nino
import uk.gov.hmrc.p800refundsstubs.models.edh.{BankSortCode, BankAccountNumber, BankAccountName}
import uk.gov.hmrc.p800refundsstubs.models.nps.{TaxDistrictNumber, ReconciliationIdentifier, PayeNumber}
import uk.gov.hmrc.p800refundsstubs.util.CurrencyFormat

import java.util.Currency

final case class CaseManagementRequest(
    clientUId:             ClientUId,
    clientSystemId:        String,
    nino:                  Nino,
    bankSortCode:          BankSortCode,
    bankAccountNumber:     BankAccountNumber,
    bankAccountName:       BankAccountName,
    designatedAccountFlag: Int, // 0 or 1
    contact:               List[CaseManagementContact],
    currency:              Currency,
    paymentAmount:         BigDecimal,
    overallRiskResult:     Int,
    nameMatches:           Option[String],
    addressMatches:        Option[String],
    accountExists:         Option[String],
    subjectHasDeceased:    Option[String],
    nonConsented:          Option[String],
    ruleResults:           Option[List[CaseManagementRuleResult]],
    reconciliationId:      Option[ReconciliationIdentifier],
    taxDistrictNumber:     Option[TaxDistrictNumber],
    payeNumber:            Option[PayeNumber]
)

object CaseManagementRequest {
  implicit val currencyFormat: Format[Currency] = CurrencyFormat.format

  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  implicit val format: OFormat[CaseManagementRequest] = Json.format[CaseManagementRequest]
}

final case class CaseManagementRuleResult(
    ruleId:          Option[String],
    ruleInformation: Option[String],
    ruleScore:       Option[Int]
)

object CaseManagementRuleResult {
  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  implicit val format: OFormat[CaseManagementRuleResult] = Json.format[CaseManagementRuleResult]
}

