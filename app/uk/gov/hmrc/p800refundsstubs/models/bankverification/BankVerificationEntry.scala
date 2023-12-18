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

package uk.gov.hmrc.p800refundsstubs.models.bankverification

import play.api.libs.json.{Format, Json, OFormat}
import uk.gov.hmrc.mongo.play.json.formats.MongoJavatimeFormats

import java.time.Instant

//just simulating a dumb endpoint using this model atm, we need to update it more like ecospend
final case class BankVerificationEntry(createdAt: Instant, bankVerification: BankVerification)

object BankVerificationEntry {
  implicit val instantFormat: Format[Instant] = MongoJavatimeFormats.instantFormat
  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  implicit val format: OFormat[BankVerificationEntry] = Json.format[BankVerificationEntry]
}

final case class BankVerification(identifier: String, verificationStatus: VerificationStatus)

object BankVerification {
  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  implicit val format: OFormat[BankVerification] = Json.format[BankVerification]
}
