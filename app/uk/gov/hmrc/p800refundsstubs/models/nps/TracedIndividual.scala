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

package uk.gov.hmrc.p800refundsstubs.models.nps

import play.api.libs.json.{Json, OFormat}
import uk.gov.hmrc.p800refundsstubs.models.Nino

final case class TracedIndividual(
    identifier:      Nino,
    title:           Option[String] = None,
    firstForename:   Option[String] = Some("Alice"),
    secondForename:  Option[String] = Some("B"),
    surname:         Option[String] = Some("Crawford"),
    sex:             Option[String] = None,
    dateOfBirth:     String         = "1978-01-02",
    addressType:     Option[String] = Some("RESIDENTIAL"),
    addressLine1:    Option[String] = Some("Flat 1 Rose House"),
    addressLine2:    Option[String] = Some("Worthing"),
    locality:        Option[String] = None,
    postalTown:      Option[String] = None,
    county:          Option[String] = None,
    addressPostcode: Option[String] = Some("BN12 4XL"),
    country:         Option[String] = Some("ENGLAND")
)

object TracedIndividual {
  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  implicit val format: OFormat[TracedIndividual] = Json.format[TracedIndividual]
}
