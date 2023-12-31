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

package uk.gov.hmrc.p800refundsstubs.models

import play.api.libs.json.{Format, Json}

//assumption that it's amount in pence, this may need to change when we get api specs
final case class IdentityVerificationRequest(nino: NationalInsuranceNumber)

object IdentityVerificationRequest {
  implicit val format: Format[IdentityVerificationRequest] = Json.format[IdentityVerificationRequest]
}

final case class IdentityVerificationResponse(identityVerified: IdentityVerified, amount: AmountInPence)

object IdentityVerificationResponse {
  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  implicit val format: Format[IdentityVerificationResponse] = Json.format[IdentityVerificationResponse]
}
