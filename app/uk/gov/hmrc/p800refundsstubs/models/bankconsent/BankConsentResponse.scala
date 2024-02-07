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

package uk.gov.hmrc.p800refundsstubs.models.bankconsent

import play.api.libs.json._

import java.time.LocalDateTime

final case class BankConsentResponse(
    id:                String,
    bankReferenceId:   String,
    bankConsentUrl:    String,
    bankId:            String,
    status:            ConsentStatus,
    redirectUrl:       String,
    consentEndDate:    LocalDateTime,
    consentExpiryDate: LocalDateTime,
    permissions:       List[ConsentPermission]
)

@SuppressWarnings(Array("org.wartremover.warts.Any"))
object BankConsentResponse {
  implicit val format: OFormat[BankConsentResponse] = {
    implicit val config: JsonConfiguration = JsonConfiguration(JsonNaming.SnakeCase)

    Json.format[BankConsentResponse]
  }
}
