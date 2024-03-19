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
import uk.gov.hmrc.p800refundsstubs.models.nps.Scenarios.ClaimOverpayment.ClaimOverpaymentScenario
import uk.gov.hmrc.p800refundsstubs.models.nps.Scenarios.IssuePayableOrder.IssuePayableOrderScenario
import uk.gov.hmrc.p800refundsstubs.models.nps.Scenarios.TraceIndividual.TraceIndividualScenario

object Scenarios {

  /**
   * It decodes a scenario for two API endpoints based on first digit in Nino.
   */
  def selectScenario(nino: Nino): (P800ReferenceCheckScenario, TraceIndividualScenario) = nino.value match {
    // format: OFF
    case s if "..0......".r.matches(s) => (CheckReference.NinoAndP800RefNotMatched, TraceIndividual.HappyPath)
    case s if "..1......".r.matches(s) => (CheckReference.RefundAlreadyTaken,       TraceIndividual.HappyPath)
    case s if "..2......".r.matches(s) => (CheckReference.UnprocessedEntity,        TraceIndividual.HappyPath)
    case s if "..3......".r.matches(s) => (CheckReference.BadRequest,               TraceIndividual.HappyPath)
    case s if "..4......".r.matches(s) => (CheckReference.Forbidden,                TraceIndividual.HappyPath)
    case s if "..5......".r.matches(s) => (CheckReference.InternalServerError,      TraceIndividual.HappyPath)
    case s if "..6......".r.matches(s) => (CheckReference.HappyPath,                TraceIndividual.NotFound)
    case s if "..7......".r.matches(s) => (CheckReference.HappyPath,                TraceIndividual.BadRequest)
    case s if "..8......".r.matches(s) => (CheckReference.HappyPath,                TraceIndividual.InternalServerError)
    case s if "..9......".r.matches(s) => (CheckReference.HappyPath,                TraceIndividual.HappyPath)
    // format: ON
  }

  /**
   * It decodes a scenario for IssuePayableOrder API based on the second digit in Nino.
   */
  def selectScenarioForIssuePayableOrder(nino: Nino): IssuePayableOrderScenario = nino.value match {
    // format: OFF
    case s if "...0.....".r.matches(s) => IssuePayableOrder.RefundAlreadyTaken
    case s if ".........".r.matches(s) => IssuePayableOrder.HappyPath
    // format: ON
  }

  /**
   * Decodes a scenario for ClaimOverpayment API based on the last digit of the Nino.
   */
  def selectScenarioForClaimOverpayment(nino: Nino): ClaimOverpaymentScenario = nino.value match {
    case s if ".......1.".r.matches(s) => ClaimOverpayment.BadRequest
    case s if ".......2.".r.matches(s) => ClaimOverpayment.Forbidden
    case s if ".......3.".r.matches(s) => ClaimOverpayment.InternalServerError
    case s if ".........".r.matches(s) => ClaimOverpayment.HappyPath
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
  }

  object ClaimOverpayment {
    sealed trait ClaimOverpaymentScenario

    case object BadRequest extends ClaimOverpaymentScenario

    case object Forbidden extends ClaimOverpaymentScenario

    case object InternalServerError extends ClaimOverpaymentScenario

    case object HappyPath extends ClaimOverpaymentScenario
  }

  object TraceIndividual {
    /**
     * Scenarios for Trace Individual API
     */
    sealed trait TraceIndividualScenario

    case object NotFound extends TraceIndividualScenario

    case object BadRequest extends TraceIndividualScenario

    case object InternalServerError extends TraceIndividualScenario

    case object HappyPath extends TraceIndividualScenario
  }

  object IssuePayableOrder {
    /**
     * Scenarios for Issue Payable Order API
     */
    sealed trait IssuePayableOrderScenario

    case object RefundAlreadyTaken extends IssuePayableOrderScenario

    case object HappyPath extends IssuePayableOrderScenario
  }

}
