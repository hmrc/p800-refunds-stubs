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

import uk.gov.hmrc.p800refundsstubs.models.Nino
import uk.gov.hmrc.p800refundsstubs.models.nps.Scenarios.CheckReference.P800ReferenceCheckScenario
import uk.gov.hmrc.p800refundsstubs.models.nps.Scenarios.MakeBacsRepayment.MakeBacsRepaymentScenario
import uk.gov.hmrc.p800refundsstubs.models.nps.Scenarios.GetBankDetailsRiskResultScenario.GetBankDetailsRiskResultScenario
import uk.gov.hmrc.p800refundsstubs.models.nps.Scenarios.IssuePayableOrder.IssuePayableOrderScenario
import uk.gov.hmrc.p800refundsstubs.models.nps.Scenarios.TraceIndividual.TraceIndividualScenario
import uk.gov.hmrc.p800refundsstubs.models.nps.Scenarios.NotifyRiskingException.NotifyRiskingExceptionScenario

object Scenarios {

  /**
   * It decodes a scenario for two API endpoints based on first digit in Nino.
   */
  def selectScenario(nino: Nino): (P800ReferenceCheckScenario, TraceIndividualScenario) = nino.value match {
    // format: OFF
    case s if "NN99.....".r.matches(s) => (CheckReference.HappyPath,                TraceIndividual.NameMatchingFailure)
    case s if "..0......".r.matches(s) => (CheckReference.NinoAndP800RefNotMatched, TraceIndividual.HappyPath)
    case s if "..1......".r.matches(s) => (CheckReference.RefundAlreadyTaken,       TraceIndividual.HappyPath)
    case s if "..2......".r.matches(s) => (CheckReference.UnprocessedEntity,        TraceIndividual.HappyPath)
    case s if "..3......".r.matches(s) => (CheckReference.BadRequest,               TraceIndividual.HappyPath)
    case s if "..4......".r.matches(s) => (CheckReference.Forbidden,                TraceIndividual.HappyPath)
    case s if "..5......".r.matches(s) => (CheckReference.InternalServerError,      TraceIndividual.HappyPath)
    case s if "..6......".r.matches(s) => (CheckReference.HappyPath,                TraceIndividual.NotFound)
    case s if "..7......".r.matches(s) => (CheckReference.HappyPath,                TraceIndividual.BadRequest)
    case s if "..8......".r.matches(s) => (CheckReference.HappyPath,                TraceIndividual.InternalServerError)
    case s if "..9.0....".r.matches(s) => (CheckReference.HappyPathOptionalFields,  TraceIndividual.HappyPath)
    case s if "..9.1....".r.matches(s) => (CheckReference.HappyPath,  TraceIndividual.HappyPathOptionalFields)
    case s if "..9......".r.matches(s) => (CheckReference.HappyPath,                TraceIndividual.HappyPath)

    // format: ON
  }

  /**
   * It decodes a scenario for IssuePayableOrder API based on the second digit in Nino.
   */
  def selectScenarioForEdhAndIssuePayableOrder(nino: Nino): (GetBankDetailsRiskResultScenario, IssuePayableOrderScenario) = nino.value match {
    // format: OFF
    case s if "...0.....".r.matches(s) => (GetBankDetailsRiskResultScenario.HappyPath, IssuePayableOrder.RefundAlreadyTaken)
    case s if "...1.....".r.matches(s) => (GetBankDetailsRiskResultScenario.DoNotPay, IssuePayableOrder.HappyPath)
    case s if "...2.....".r.matches(s) => (GetBankDetailsRiskResultScenario.SubmissionHasNotPassedValidation, IssuePayableOrder.HappyPath)
    case s if "...3.....".r.matches(s) => (GetBankDetailsRiskResultScenario.Forbidden, IssuePayableOrder.HappyPath)
    case s if "...4.....".r.matches(s) => (GetBankDetailsRiskResultScenario.ResourceNotFound, IssuePayableOrder.HappyPath)
    case s if "...5.....".r.matches(s) => (GetBankDetailsRiskResultScenario.DesIssues, IssuePayableOrder.HappyPath)
    case s if "...6.....".r.matches(s) => (GetBankDetailsRiskResultScenario.DependentSystemIssues, IssuePayableOrder.HappyPath)
    case s if "...7.....".r.matches(s) => (GetBankDetailsRiskResultScenario.HappyPath, IssuePayableOrder.InternalServerError)
    case s if ".........".r.matches(s) => (GetBankDetailsRiskResultScenario.HappyPath, IssuePayableOrder.HappyPath)
    // format: ON
  }

  /**
   * It decodes a scenario for Suspend Overpayment API based on the fourth (4) digit in Nino.
   */
  def selectScenarioForSuspendOverpayment(nino: Nino): SuspendOverpayment.SuspendOverpaymentScenario = nino.value match {
    // format: OFF
    case s if ".....0...".r.matches(s) => SuspendOverpayment.InternalServerError
    case s if ".........".r.matches(s) => SuspendOverpayment.HappyPath
    // format: ON
  }

  /**
   * Decodes a scenario for ClaimOverpayment API based on the last digit of the Nino.
   */
  def selectScenarioForMakeBacsRepayment(nino: Nino): MakeBacsRepaymentScenario = nino.value match {
    case s if ".......1.".r.matches(s) => MakeBacsRepayment.BadRequest
    case s if ".......2.".r.matches(s) => MakeBacsRepayment.Forbidden
    case s if ".......3.".r.matches(s) => MakeBacsRepayment.InternalServerError
    case s if ".......4.".r.matches(s) => MakeBacsRepayment.RefundAlreadyTaken
    case s if ".......5.".r.matches(s) => MakeBacsRepayment.Suspended
    case s if ".........".r.matches(s) => MakeBacsRepayment.HappyPath
  }

  /**
   * Decodes a scenario for Notify Risking Exception
   */
  def selectScenarioForNotifyRiskingException(nino: Nino): NotifyRiskingExceptionScenario = nino.value match {
    case s if "......1..".r.matches(s) => NotifyRiskingException.BadRequest
    case s if "......2..".r.matches(s) => NotifyRiskingException.Forbidden
    case s if "......3..".r.matches(s) => NotifyRiskingException.InternalServerError
    case s if ".........".r.matches(s) => NotifyRiskingException.HappyPath
  }

  object NotifyRiskingException {
    sealed trait NotifyRiskingExceptionScenario

    case object BadRequest extends NotifyRiskingExceptionScenario

    case object Forbidden extends NotifyRiskingExceptionScenario

    case object InternalServerError extends NotifyRiskingExceptionScenario

    case object HappyPath extends NotifyRiskingExceptionScenario
  }

  object CheckReference {
    /**
     * Scenarios for P800 Check Reference API
     */
    sealed trait P800ReferenceCheckScenario

    case object NinoAndP800RefNotMatched extends P800ReferenceCheckScenario

    case object RefundAlreadyTaken extends P800ReferenceCheckScenario

    case object UnprocessedEntity extends P800ReferenceCheckScenario //Other unprocessed entity

    case object BadRequest extends P800ReferenceCheckScenario

    case object Forbidden extends P800ReferenceCheckScenario

    case object InternalServerError extends P800ReferenceCheckScenario

    case object HappyPath extends P800ReferenceCheckScenario

    /**
     * Make reconciliationIdentifier, taxDistrictNumber, payeNumber optional
     * OPS-12156
     */
    case object HappyPathOptionalFields extends P800ReferenceCheckScenario

  }

  object MakeBacsRepayment {
    sealed trait MakeBacsRepaymentScenario

    case object BadRequest extends MakeBacsRepaymentScenario

    case object Forbidden extends MakeBacsRepaymentScenario

    case object InternalServerError extends MakeBacsRepaymentScenario

    case object RefundAlreadyTaken extends MakeBacsRepaymentScenario

    case object Suspended extends MakeBacsRepaymentScenario

    case object HappyPath extends MakeBacsRepaymentScenario
  }

  object TraceIndividual {
    /**
     * Scenarios for Trace Individual API
     */
    sealed trait TraceIndividualScenario

    case object NotFound extends TraceIndividualScenario

    case object BadRequest extends TraceIndividualScenario

    case object InternalServerError extends TraceIndividualScenario

    case object NameMatchingFailure extends TraceIndividualScenario

    case object HappyPath extends TraceIndividualScenario

    case object HappyPathOptionalFields extends TraceIndividualScenario

  }

  object IssuePayableOrder {
    /**
     * Scenarios for Issue Payable Order API
     */
    sealed trait IssuePayableOrderScenario

    case object RefundAlreadyTaken extends IssuePayableOrderScenario

    case object HappyPath extends IssuePayableOrderScenario

    case object InternalServerError extends IssuePayableOrderScenario
  }

  object SuspendOverpayment {
    /**
     * Scenarios for Suspend Overpayment API
     */
    sealed trait SuspendOverpaymentScenario

    case object HappyPath extends SuspendOverpaymentScenario

    case object InternalServerError extends SuspendOverpaymentScenario
  }

  object GetBankDetailsRiskResultScenario {
    /**
     * Scenarios for Get Bank Details Risk Result API
     */
    sealed trait GetBankDetailsRiskResultScenario

    /**
     * 200 Response with nextAction = Pay
     */
    case object HappyPath extends GetBankDetailsRiskResultScenario

    /**
     * 200 Response with nextAction = Do Not Pay
     */
    case object DoNotPay extends GetBankDetailsRiskResultScenario

    case object SubmissionHasNotPassedValidation extends GetBankDetailsRiskResultScenario

    case object Forbidden extends GetBankDetailsRiskResultScenario
    /**
     * The remote endpoint has indicated that no data can be found
     */
    case object ResourceNotFound extends GetBankDetailsRiskResultScenario

    /**
     * DES is currently experiencing problems that require live service intervention
     */
    case object DesIssues extends GetBankDetailsRiskResultScenario

    /**
     * Dependent systems are currently not responding
     */
    case object DependentSystemIssues extends GetBankDetailsRiskResultScenario
  }
}
