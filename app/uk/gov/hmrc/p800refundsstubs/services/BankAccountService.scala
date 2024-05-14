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

import cats.data.OptionT
import com.google.inject.{Inject, Singleton}
import uk.gov.hmrc.p800refundsstubs.models.bankconsent.{BankConsentEntry, BankConsent}
import uk.gov.hmrc.p800refundsstubs.models.{BankAccountSummaryResponse, BankAccountSummary, BankAccountType, BankAccountSubType, BankAccountParty, BankPartyName, BankPartyFullLegalName, BankAccountFormat}
import uk.gov.hmrc.p800refundsstubs.repo.BankConsentRepo
import uk.gov.hmrc.p800refundsstubs.models.{AccountIdentification, AccountOwnerName, CalculatedOwnerName, DisplayName}

import java.time.LocalDateTime
import java.util.UUID
import java.util.Currency
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BankAccountService @Inject() (bankConsentRepo: BankConsentRepo)(implicit executionContext: ExecutionContext) {

  def getAccountSummary(consentId: UUID): OptionT[Future, BankAccountSummaryResponse] = {
    bankConsentRepo
      .findBankConsentEntry(consentId)
      .map { bankConsentEntry: BankConsentEntry =>
        BankAccountSummaryResponse(fromBankConsent(bankConsentEntry.bankConsent))
      }
  }

  private def fromBankConsent(bankConsent: BankConsent): List[BankAccountSummary] =
    List(BankAccountSummary(
      id                    = UUID.fromString(bankConsent.id),
      bankId                = bankConsent.bankId,
      merchantId            = None,
      merchantUserId        = None,
      ttype                 = BankAccountType.Personal,
      subType               = BankAccountSubType.CurrentAccount,
      currency              = Currency.getInstance("GBP"),
      accountFormat         = BankAccountFormat.SortCode,
      accountIdentification = AccountIdentification("22334410002333"),
      calculatedOwnerName   = CalculatedOwnerName("Alice Crawford"),
      accountOwnerName      = AccountOwnerName("Alice Crawford"),
      displayName           = DisplayName("Alice B Crawford"),
      balance               = 123.7,
      lastUpdateTime        = LocalDateTime.now(),
      parties               = List(BankAccountParty(
        name          = BankPartyName("Alice Crawford"),
        fullLegalName = BankPartyFullLegalName("Alice Crawford")
      ))
    ))

}
