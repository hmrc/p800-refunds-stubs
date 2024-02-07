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

package uk.gov.hmrc.p800refundsstubs.services

import com.google.inject.{Inject, Singleton}
import uk.gov.hmrc.p800refundsstubs.config.AppConfig
import uk.gov.hmrc.p800refundsstubs.models.bankconsent.{BankConsentRequest, BankConsentResponse, BankConsentEntry, BankConsent, ConsentStatus}
import uk.gov.hmrc.p800refundsstubs.repo.BankConsentRepo

import java.time.{Instant, LocalDateTime}
import java.util.UUID
import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}
import java.time.ZoneId

@Singleton
class BankConsentService @Inject() (appConfig: AppConfig, bankConsentRepo: BankConsentRepo)(implicit executionContext: ExecutionContext) {

  def insertData(bankConsentRequest: BankConsentRequest): Future[BankConsentResponse] = {
    val bankConsent: BankConsent = bankConsentFromBankConsentRequest(bankConsentRequest)
    val bankEntry: BankConsentEntry = BankConsentEntry(Instant.now(), bankConsent)

    bankConsentRepo
      .insert(bankEntry)
      .map { _ =>
        bankConsentResponseFromBankConsent(bankConsent)
      }
  }

  private def bankConsentFromBankConsentRequest(bankConsentRequest: BankConsentRequest): BankConsent =
    BankConsent(
      id                = UUID.randomUUID().toString,
      bankReferenceId   = "MyBank-129781876126",
      bankConsentUrl    = s"${appConfig.p800RefundsFrontendBaseUrl}/get-an-income-tax-refund/test-only/bank-page",
      bankId            = bankConsentRequest.bankId,
      status            = ConsentStatus.AwaitingAuthorization,
      redirectUrl       = bankConsentRequest.redirectUrl,
      consentEndDate    = Instant.now().plusSeconds(10.minutes.toSeconds),
      consentExpiryDate = Instant.now().plusSeconds(20.minutes.toSeconds),
      permissions       = bankConsentRequest.permissions
    )

  private def bankConsentResponseFromBankConsent(bankConsent: BankConsent): BankConsentResponse =
    BankConsentResponse(
      id                = bankConsent.id,
      bankReferenceId   = bankConsent.bankReferenceId,
      bankConsentUrl    = bankConsent.bankConsentUrl,
      bankId            = bankConsent.bankId,
      status            = bankConsent.status,
      redirectUrl       = bankConsent.redirectUrl,
      consentEndDate    = LocalDateTime.ofInstant(bankConsent.consentEndDate, ZoneId.of("UTC")),
      consentExpiryDate = LocalDateTime.ofInstant(bankConsent.consentExpiryDate, ZoneId.of("UTC")),
      permissions       = bankConsent.permissions
    )
}
