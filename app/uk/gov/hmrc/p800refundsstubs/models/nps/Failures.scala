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
import uk.gov.hmrc.p800refundsstubs.models.{Nino, P800Reference}

final case class Failures(
    failures: List[Failure]
)

object Failures {
  implicit val format: OFormat[Failures] = Json.format[Failures]
  def apply(failure: Failure): Failures = Failures(failures = List(failure))

  val accountNotLive: Failures = Failures(Failure.accountNotLive)
  val overpaymentAlreadyClaimed: Failures = Failures(Failure.overpaymentAlreadyClaimed)
  val overpaymentSuspended: Failures = Failures(Failure.overpaymentSuspended)
  val overpaymentNoLongerAvailable: Failures = Failures(Failure.overpaymentNoLongerAvailable)

  val badRequestAsPerScenario: Failures = Failures(Failure(reason = "Bad Request as per scenario", code = "00003"))
  val forbiddenAsPerScenario: Failures = Failures(Failure(reason = "Forbidden as per scenario", code = "403.2"))
}

final case class Failure(reason: String, code: String)

object Failure {

  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  implicit val format: OFormat[Failure] = Json.format[Failure]

  //Failures from documentation
  val accountNotLive: Failure = Failure("Account is not Live", "63477")
  val overpaymentAlreadyClaimed: Failure = Failure("Overpayment has already been claimed", "63480")
  val overpaymentSuspended: Failure = Failure("Overpayment is suspended", "63481")
  val overpaymentNoLongerAvailable: Failure = Failure("Overpayment is no longer available", "63483")

  //Made up failures:
  def invalidPaymentNumber(paymentNumber: P800Reference): Failure = Failure(reason = s"Invalid paymentNumber ${paymentNumber.toString}", code = "000001")
  def invalidIdentifier(identifier: Nino): Failure = Failure(reason = s"Invalid identifier ${identifier.toString}", code = "000002")

}

