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

package uk.gov.hmrc.p800refundsstubs.models

import enumeratum._
import play.api.libs.json._
import uk.gov.hmrc.p800refundsstubs.util.CurrencyFormat

import java.util.{UUID, Currency}
import java.time.LocalDateTime

final case class BankAccountSummaryResponse(value: List[BankAccountSummary]) extends AnyVal

object BankAccountSummaryResponse {
  implicit val format: Format[BankAccountSummaryResponse] = Json.valueFormat[BankAccountSummaryResponse]
}

final case class BankAccountSummary(
    id:                    UUID,
    bankId:                Option[String],
    merchantId:            Option[String],
    merchantUserId:        Option[String],
    `type`:                BankAccountType,
    subType:               BankAccountSubType,
    currency:              Currency,
    accountFormat:         BankAccountFormat,
    accountIdentification: Option[AccountIdentification],
    calculatedOwnerName:   Option[CalculatedOwnerName],
    accountOwnerName:      AccountOwnerName,
    displayName:           Option[DisplayName],
    balance:               Double,
    lastUpdateTime:        LocalDateTime,
    parties:               Option[List[BankAccountParty]]
)

@SuppressWarnings(Array("org.wartremover.warts.Any"))
object BankAccountSummary {
  implicit val currencyFormat: Format[Currency] = CurrencyFormat.format

  implicit val format: OFormat[BankAccountSummary] = {
    implicit val config: JsonConfiguration = JsonConfiguration(JsonNaming.SnakeCase)

    Json.format[BankAccountSummary]
  }
}

final case class BankAccountParty(
    name:          Option[BankPartyName],
    fullLegalName: Option[BankPartyFullLegalName]
)

@SuppressWarnings(Array("org.wartremover.warts.Any"))
object BankAccountParty {
  implicit val format: OFormat[BankAccountParty] = {
    implicit val config: JsonConfiguration = JsonConfiguration(JsonNaming.SnakeCase)

    Json.format[BankAccountParty]
  }
}

final case class BankPartyName(value: String) extends AnyVal
object BankPartyName {
  implicit val format: Format[BankPartyName] = Json.valueFormat[BankPartyName]
}

final case class BankPartyFullLegalName(value: String) extends AnyVal
object BankPartyFullLegalName {
  implicit val format: Format[BankPartyFullLegalName] = Json.valueFormat[BankPartyFullLegalName]
}

sealed trait BankAccountType extends EnumEntry
object BankAccountType extends Enum[BankAccountType] with PlayJsonEnum[BankAccountType] {
  val values = findValues

  case object Business extends BankAccountType
  case object Personal extends BankAccountType
  case object SoleTrader extends BankAccountType
  case object Joint extends BankAccountType
  case object BankingAsAService extends BankAccountType
}

sealed trait BankAccountSubType extends EnumEntry
object BankAccountSubType extends Enum[BankAccountSubType] with PlayJsonEnum[BankAccountSubType] {
  val values = findValues

  case object ChargeCard extends BankAccountSubType
  case object CreditCard extends BankAccountSubType
  case object CurrentAccount extends BankAccountSubType
  case object EMoney extends BankAccountSubType
  case object Loan extends BankAccountSubType
  case object Mortgage extends BankAccountSubType
  case object PrePaidCard extends BankAccountSubType
  case object Savings extends BankAccountSubType
  case object Primary extends BankAccountSubType
  case object Additional extends BankAccountSubType
  case object FixedTermDeposit extends BankAccountSubType
}

sealed trait BankAccountFormat extends EnumEntry
object BankAccountFormat extends Enum[BankAccountFormat] with PlayJsonEnum[BankAccountFormat] {
  val values = findValues

  case object SortCode extends BankAccountFormat
  case object Iban extends BankAccountFormat
  case object Bban extends BankAccountFormat
  case object Pan extends BankAccountFormat
}

final case class AccountIdentification(value: String) extends AnyVal
object AccountIdentification {
  implicit val format: Format[AccountIdentification] = Json.valueFormat[AccountIdentification]
}

final case class CalculatedOwnerName(value: String) extends AnyVal
object CalculatedOwnerName {
  implicit val format: Format[CalculatedOwnerName] = Json.valueFormat[CalculatedOwnerName]
}

final case class AccountOwnerName(value: String) extends AnyVal
object AccountOwnerName {
  implicit val format: Format[AccountOwnerName] = Json.valueFormat[AccountOwnerName]
}

final case class DisplayName(value: String) extends AnyVal
object DisplayName {
  implicit val format: Format[DisplayName] = Json.valueFormat[DisplayName]
}
