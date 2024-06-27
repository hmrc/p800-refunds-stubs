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

package uk.gov.hmrc.p800refundsstubs.controllers

import play.api.mvc._
import uk.gov.hmrc.p800refundsstubs.actions.Actions
import uk.gov.hmrc.p800refundsstubs.models.casemanagement._
import uk.gov.hmrc.p800refundsstubs.models.edh.BankAccountName
import uk.gov.hmrc.p800refundsstubs.models.nps.Scenarios
import uk.gov.hmrc.p800refundsstubs.util.SafeEquals.EqualsOps
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton()
class CaseManagementController @Inject() (
    cc:      ControllerComponents,
    actions: Actions
)(implicit ec: ExecutionContext) extends BackendController(cc) {

  /**
   * Case Management Interface to start a risking case with case management
   */
  def notifyCaseManagement(clientUId: String): Action[CaseManagementRequest] = actions
    .caseManagementAction
    .apply(
      parse
        .json[CaseManagementRequest]
        .validate(r => if (r.clientUId.value === clientUId) Right(r) else Left(BadRequest("'clientUId' has to match the path parameter")))
        .validate(r => if (r.clientUId.value.length <= 36) Right(r) else Left(BadRequest("'clientUId' has a max length of 36 characters")))
        .validate(r => if (BankAccountName.regex.matches(r.bankAccountName.value)) Right(r) else Left(BadRequest("'bankAccountName' failed to match regex")))
    )
    .apply { implicit request: Request[CaseManagementRequest] =>
      Scenarios.selectScenarioForNotifyRiskingException(request.body.nino) match {
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

}
