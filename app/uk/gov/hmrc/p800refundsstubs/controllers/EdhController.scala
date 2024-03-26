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

import play.api.libs.json.Json
import play.api.mvc._
import uk.gov.hmrc.p800refundsstubs.actions.Actions
import uk.gov.hmrc.p800refundsstubs.models.Nino
import uk.gov.hmrc.p800refundsstubs.models.edh._
import uk.gov.hmrc.p800refundsstubs.models.nps.Scenarios
import uk.gov.hmrc.p800refundsstubs.models.nps.Scenarios.GetBankDetailsRiskResultScenario
import uk.gov.hmrc.p800refundsstubs.util.SafeEquals.EqualsOps
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext
import scala.util.Try

@Singleton()
class EdhController @Inject() (
    cc:      ControllerComponents,
    actions: Actions
)(implicit ec: ExecutionContext) extends BackendController(cc) {

  /**
   * EDH Interface to start a risking case with case management
   */
  def notifyCaseManagement(clientUId: String): Action[CaseManagementRequest] = actions
    .edhAction
    .apply(
      parse
        .json[CaseManagementRequest]
        .validate(r => if (r.clientUId.value === clientUId) Right(r) else Left(BadRequest("'clientUId' has to match the path parameter")))
        .validate(r => if (r.clientUId.value.length <= 36) Right(r) else Left(BadRequest("'clientUId' has a max length of 36 characters")))
    )
    .apply { implicit request: Request[CaseManagementRequest] =>
      Scenarios.notifyRiskingExceptionEdhScenario(request.body.nino) match {
        case Scenarios.NotifyRiskingException.BadRequest =>
          BadRequest("BadRequest as per scenario")
        case Scenarios.NotifyRiskingException.Forbidden =>
          Forbidden("Forbidden as per scenario")
        case Scenarios.NotifyRiskingException.InternalServerError =>
          InternalServerError("Internal Server Error as per scenario")
        case Scenarios.NotifyRiskingException.HappyPath =>
          Ok
      }
    }

  def getBankDetailsRiskResult(claimId: ClaimId): Action[GetBankDetailsRiskResultRequest] = actions
    .edhAction
    .apply(
      parse
        .json[GetBankDetailsRiskResultRequest]
        .validate(r => claimId.validate.map(err => Left(BadRequest(err))).getOrElse(Right(r)))
        .validate(r => if (Try(UUID.fromString(r.header.transactionID.value)).isSuccess) Right(r) else Left(BadRequest("'transactionID' has to be valid UUID")))
        .validate(r => if (r.header.transactionID.value === claimId.value) Right(r) else Left(BadRequest("'claimId' has to be the same as transactionID")))
        .validate(r => if (r.header.requesterID.value === "Repayment Service") Right(r) else Left(BadRequest("'requesterID' has to be 'Repayment Service'")))
        .validate(r => if (r.header.serviceID.value === "P800") Right(r) else Left(BadRequest("'serviceID' has to be 'P800'")))
        .validate(r => r.validate.map(err => Left(BadRequest(s"'GetBankDetailsRiskResultRequest' is not valid, $err"))).getOrElse(Right(r)))
        .validate(r => if (r.paymentData.isDefined) Right(r) else Left(BadRequest("'paymentData' has to be defined")))
        .validate(r => if (r.riskData.size === 1) Right(r) else Left(BadRequest("'riskData' should have only one element")))
    )
    .apply{ implicit request =>
      val nino: Nino =
        request.body
          .riskData
          .headOption.getOrElse(throw new RuntimeException("Missing 'riskData'"))
          .person.getOrElse(throw new RuntimeException("Missing 'person'"))
          .nino

      Scenarios.selectScenarioForEdhAndIssuePayableOrder(nino)._1 match {
        case GetBankDetailsRiskResultScenario.HappyPath                        => Ok(Json.toJson(createSuccessResponseExample(NextAction.Pay)))
        case GetBankDetailsRiskResultScenario.DoNotPay                         => Ok(Json.toJson(createSuccessResponseExample(NextAction.DoNotPay)))
        case GetBankDetailsRiskResultScenario.SubmissionHasNotPassedValidation => BadRequest(Json.toJson(FailureResponse("Emulating SubmissionHasNotPassedValidation")))
        case GetBankDetailsRiskResultScenario.Forbidden                        => Forbidden(Json.toJson(FailureResponse("Emulating Unauthorised")))
        case GetBankDetailsRiskResultScenario.ResourceNotFound                 => NotFound(Json.toJson(FailureResponse("Emulating ResourceNotFound")))
        case GetBankDetailsRiskResultScenario.DesIssues                        => InternalServerError(Json.toJson(FailureResponse("Emulating DesIssues")))
        case GetBankDetailsRiskResultScenario.DependentSystemIssues            => ServiceUnavailable(Json.toJson(FailureResponse("Emulating DependentSystemIssues")))
      }
    }

  def createSuccessResponseExample(nextAction: NextAction): GetBankDetailsRiskResultResponse = {
    val header = Header(
      transactionID = TransactionID("TX1234567890"),
      requesterID   = RequesterID("REQ1234567890"),
      serviceID     = ServiceID("SVC1234567890")
    )

    val bankValidationResults = BankValidationResults(
      accountExists      = Some(Yes),
      nameMatches        = Some(No),
      addressMatches     = Some(Indeterminate),
      nonConsented       = Some(Inapplicable),
      subjectHasDeceased = Some(No)
    )

    val overallRiskResult = OverallRiskResult(
      ruleScore  = 75,
      nextAction = nextAction
    )

    val riskResults = nextAction match {
      case NextAction.Pay =>
        List(
          RuleResult(
            ruleId          = "Rule1",
            ruleInformation = Some("Additional information for Rule1"),
            ruleScore       = 80,
            nextAction      = NextAction.Pay
          ),
          RuleResult(
            ruleId          = "Rule2",
            ruleInformation = None,
            ruleScore       = 70,
            nextAction      = NextAction.Pay
          )
        )
      case NextAction.DoNotPay =>
        List(
          RuleResult(
            ruleId          = "Rule3",
            ruleInformation = Some("Additional information for Rule3"),
            ruleScore       = 60,
            nextAction      = NextAction.DoNotPay
          ),
          RuleResult(
            ruleId          = "Rule4",
            ruleInformation = Some("Additional information for Rule4"),
            ruleScore       = 50,
            nextAction      = NextAction.DoNotPay
          )
        )
    }

    GetBankDetailsRiskResultResponse(
      header                = header,
      bankValidationResults = Some(bankValidationResults),
      overallRiskResult     = overallRiskResult,
      riskResults           = Some(riskResults)
    )
  }

}

