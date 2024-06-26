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

import play.api.libs.json.{Json, OFormat}
import uk.gov.hmrc.p800refundsstubs.models.edh.{AddressType, PersonType, Postcode}

final case class CaseManagementContact(
    `type`:    PersonType,
    firstName: String,
    surname:   String,
    address:   List[CaseManagementAddress]
)

object CaseManagementContact {
  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  implicit val format: OFormat[CaseManagementContact] = Json.format[CaseManagementContact]
}

final case class CaseManagementAddress(
    `type`:       AddressType,
    addressLine1: Option[String],
    addressLine2: Option[String],
    addressLine3: Option[String],
    addressLine4: Option[String],
    addressLine5: Option[String],
    postcode:     Option[Postcode]
)

object CaseManagementAddress {
  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  implicit val format: OFormat[CaseManagementAddress] = Json.format[CaseManagementAddress]
}
