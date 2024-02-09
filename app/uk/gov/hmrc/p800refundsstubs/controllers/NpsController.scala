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

package uk.gov.hmrc.p800refundsstubs.controllers

import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.p800refundsstubs.actions.Actions
import uk.gov.hmrc.p800refundsstubs.models.nps._
import uk.gov.hmrc.p800refundsstubs.models.{Nino, P800Reference}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.Inject

class NpsController @Inject() (actions: Actions, cc: ControllerComponents)
  extends BackendController(cc) {

  /**
   * NPS Interface to validate a P800 Reference and retrieve Payment Reference data.
   */
  def p800ReferenceCheck(_identifier: Nino, _paymentNumber: P800Reference): Action[AnyContent] = actions.npsAction{ _ =>
    val paymentNumber = _paymentNumber
    val identifier = _identifier
    if (!identifier.isValid) {
      logger.info(s"Not valid identifier: [${identifier.toString}], returning BadRequest [${request.correlationId.toString}]")
      BadRequest(Json.toJson(P800ReferenceCheckResultFailures(
        failures = List(
          Failure(reason = s"Invalid identifier ${identifier.toString}", code = "TODO3")
        )
      )))
    } else if (!paymentNumber.isValid) {
      logger.info(s"Not valid paymentNumber: [${paymentNumber.toString}], returning BadRequest [${request.correlationId.toString}]")
      BadRequest(Json.toJson(P800ReferenceCheckResultFailures(
        failures = List(
          Failure(reason = s"Invalid paymentNumber ${paymentNumber.toString}", code = "TODO3")
        )
      )))
    } else {
      P800ReferenceCheckScenario.selectScenario(identifier) match {
        case P800ReferenceCheckScenario.NinoAndP800RefNotMatched =>
          NotFound("")
        case P800ReferenceCheckScenario.RefundAlreadyTaken =>
          UnprocessableEntity(Json.toJson(P800ReferenceCheckResultFailures(
            failures = List(
              Failure(
                reason = "TODO Refund already taken",
                code   = "TODO-refund-already-taken"
              )
            )
          )))
        case P800ReferenceCheckScenario.UnprocessedEntity =>
          UnprocessableEntity(Json.toJson(P800ReferenceCheckResultFailures(
            failures = List(
              Failure(
                reason = "Something wrong at NPS",
                code   = "TODO2"
              )
            )
          )))
        case P800ReferenceCheckScenario.BadRequest =>
          BadRequest(Json.toJson(P800ReferenceCheckResultFailures(
            failures = List(
              Failure(reason = "Incorrect Identifier", code = "TODO3")
            )
          )))
        case P800ReferenceCheckScenario.Forbidden =>
          Forbidden(Json.toJson(Failure(reason = "Forbidden", code = "403.2")))
        case P800ReferenceCheckScenario.InternalServerError =>
          InternalServerError("Simulating Internal Server Error response...")
        case P800ReferenceCheckScenario.HappyPath =>
          Ok(Json.toJson(P800ReferenceCheckResult(
            reconciliationIdentifier = ReconciliationIdentifier("reconciliationIdentifier"),
            paymentNumber            = paymentNumber,
            payeNumber               = PayeNumber("payeNumber"),
            taxDistrictNumber        = TaxDistrictNumber("taxDistrictNumber"),
            paymentAmount            = BigDecimal("4321.09"),
            associatedPayableNumber  = AssociatedPayableNumber("associatedPayableNumber"),
            customerAccountNumber    = CustomerAccountNumber("customerAccountNumber"),
            currentOptimisticLock    = CurrentOptimisticLock(123)
          )))
      }
    }
  }

  private lazy val logger = Logger(this.getClass)
}
