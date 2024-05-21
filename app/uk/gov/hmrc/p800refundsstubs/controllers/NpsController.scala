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
  def p800ReferenceCheck(_identifier: Nino, _paymentNumber: P800Reference): Action[AnyContent] =
    actions.npsActionValidated(_identifier, _paymentNumber) { _ =>
      val paymentNumber = _paymentNumber
      val identifier = _identifier
      Scenarios.selectScenario(identifier)._1 match {
        case Scenarios.CheckReference.NinoAndP800RefNotMatched =>
          NotFound("")
        case Scenarios.CheckReference.RefundAlreadyTaken =>
          UnprocessableEntity(Json.toJson(Failures.overpaymentAlreadyClaimed))
        case Scenarios.CheckReference.UnprocessedEntity =>
          UnprocessableEntity(Json.toJson(Failures.overpaymentNoLongerAvailable))
        case Scenarios.CheckReference.BadRequest =>
          BadRequest(Json.toJson(Failures.badRequestAsPerScenario))
        case Scenarios.CheckReference.Forbidden =>
          Forbidden(Json.toJson(Failures.forbiddenAsPerScenario))
        case Scenarios.CheckReference.InternalServerError =>
          InternalServerError("Internal Server Error as per scenario")
        case Scenarios.CheckReference.HappyPath =>
          Ok(Json.toJson(P800ReferenceCheckResult(
            reconciliationIdentifier = Some(ReconciliationIdentifier(123)),
            paymentNumber            = paymentNumber,
            payeNumber               = Some(PayeNumber("123PH123456")),
            taxDistrictNumber        = Some(TaxDistrictNumber(123)),
            paymentAmount            = BigDecimal("4321.09"),
            associatedPayableNumber  = AssociatedPayableNumber(123),
            customerAccountNumber    = CustomerAccountNumber("7654321"),
            currentOptimisticLock    = CurrentOptimisticLock(123)
          )))
        case Scenarios.CheckReference.HappyPathOptionalFields =>
          Ok(Json.toJson(P800ReferenceCheckResult(
            reconciliationIdentifier = None,
            paymentNumber            = paymentNumber,
            payeNumber               = None,
            taxDistrictNumber        = None,
            paymentAmount            = BigDecimal("4321.09"),
            associatedPayableNumber  = AssociatedPayableNumber(123),
            customerAccountNumber    = CustomerAccountNumber("7654321"),
            currentOptimisticLock    = CurrentOptimisticLock(123)
          )))
      }
    }

  /**
   * NPS Interface to Trace Individual
   */
  def traceIndividual(exactMatch: Boolean, returnRealName: Boolean): Action[TraceIndividualRequest] = actions.npsAction(parse.json[TraceIndividualRequest]) { request =>
    val r: TraceIndividualRequest = request.body
    val identifier: Nino = r.identifier
    require(exactMatch, "'exactMatch' has to be true")
    require(returnRealName, "'returnRealName' has to be true")
    if (!identifier.isValid) {
      logger.info(s"Not a valid identifier: [${identifier.toString}], returning BadRequest [${request.correlationId.toString}]")
      BadRequest(Json.toJson(Failures(Failure.invalidIdentifier(identifier))))
    } else {
      Scenarios.selectScenario(identifier)._2 match {
        case Scenarios.TraceIndividual.NotFound   => NotFound("")
        case Scenarios.TraceIndividual.BadRequest => BadRequest(Json.toJson(Failures.badRequestAsPerScenario))
        case Scenarios.TraceIndividual.InternalServerError =>
          InternalServerError("Internal Server Error as per scenario")
        case Scenarios.TraceIndividual.NameMatchingFailure =>
          Ok(Json.toJson(TraceIndividualResponse(List(TracedIndividual(
            identifier     = identifier, firstForename = Some("error"), secondForename = Some("error"), surname = Some("error")
          )))))
        case Scenarios.TraceIndividual.HappyPathOptionalFields =>
          Ok(Json.toJson(TraceIndividualResponse(List(TracedIndividual(
            identifier      = identifier,
            title           = None,
            firstForename   = None,
            secondForename  = None,
            surname         = None,
            sex             = None,
            addressType     = None,
            addressLine1    = None,
            addressLine2    = None,
            locality        = None,
            postalTown      = None,
            county          = None,
            addressPostcode = None,
            country         = None
          )))))
        case Scenarios.TraceIndividual.HappyPath =>
          Ok(Json.toJson(TraceIndividualResponse(List(TracedIndividual(
            identifier = identifier
          )))))
      }
    }
  }

  /**
   * NPS Interface to issue payable order. It's used in a Cheque journey.
   */
  def issuePayableOrder(identifier: Nino, paymentNumber: P800Reference): Action[IssuePayableOrderRequest] =
    actions.npsActionValidated(identifier, paymentNumber)(parse.json[IssuePayableOrderRequest]) { _ =>
      Scenarios.selectScenarioForEdhAndIssuePayableOrder(identifier)._2 match {
        case Scenarios.IssuePayableOrder.HappyPath =>
          Ok(Json.toJson(IssuePayableOrderResponse(
            identifier            = identifier,
            currentOptimisticLock = CurrentOptimisticLock(123)
          )))
        case Scenarios.IssuePayableOrder.RefundAlreadyTaken =>
          UnprocessableEntity(Json.toJson(Failures(
            Failure.overpaymentAlreadyClaimed
          )))
        case Scenarios.IssuePayableOrder.InternalServerError =>
          InternalServerError("Internal Server Error as per scenario")
      }
    }

  /**
   * NPS Interface to Suspend Overpayment. It's used in a Bank Transfer journey.
   */
  def suspendOverpayment(identifier: Nino): Action[SuspendOverpaymentRequest] =
    actions.npsActionValidated(identifier)(parse.json[SuspendOverpaymentRequest]) { _ =>
      Scenarios.selectScenarioForSuspendOverpayment(identifier) match {
        case Scenarios.SuspendOverpayment.HappyPath =>
          Ok(Json.toJson(SuspendOverpaymentResponse(
            identifier            = identifier,
            currentOptimisticLock = CurrentOptimisticLock(123)
          )))
        case Scenarios.SuspendOverpayment.InternalServerError =>
          InternalServerError(Json.toJson("reason" -> "Emulating errors for testing"))
      }
    }

  /**
   * NPS Interface to claim overpayment. It's used in a Bank Transfer journey.
   */
  def makeBacsRepayment(identifier: Nino): Action[MakeBacsRepaymentRequest] =
    actions.npsActionValidated(identifier)(parse.json[MakeBacsRepaymentRequest]) { implicit request =>
      Scenarios.selectScenarioForMakeBacsRepayment(identifier) match {
        case Scenarios.MakeBacsRepayment.HappyPath =>
          Ok(Json.toJson(MakeBacsRepaymentResponse(
            identifier            = identifier,
            currentOptimisticLock = request.body.currentOptimisticLock
          )))
        case Scenarios.MakeBacsRepayment.BadRequest =>
          BadRequest(Json.toJson(Failures.badRequestAsPerScenario))
        case Scenarios.MakeBacsRepayment.Forbidden =>
          Forbidden(Json.toJson(Failures.forbiddenAsPerScenario))
        case Scenarios.MakeBacsRepayment.RefundAlreadyTaken =>
          UnprocessableEntity(Json.toJson(Failures(
            Failure.overpaymentAlreadyClaimed
          )))
        case Scenarios.MakeBacsRepayment.Suspended =>
          UnprocessableEntity(Json.toJson(Failures(
            Failure.overpaymentSuspended
          )))
        case Scenarios.MakeBacsRepayment.InternalServerError =>
          InternalServerError("Internal Server Error as per scenario")
      }
    }

  private lazy val logger = Logger(this.getClass)
}
