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
import play.api.mvc.{Action, ControllerComponents}
import uk.gov.hmrc.p800refundsstubs.models.{AmountInPence, IdentityVerificationRequest, IdentityVerificationResponse, IdentityVerified}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.Inject
import scala.concurrent.Future

class IdentityVerificationStubController @Inject() (cc: ControllerComponents) extends BackendController(cc) {

  val verifyIdentity: Action[IdentityVerificationRequest] = Action.async(parse.json[IdentityVerificationRequest]) { implicit request =>
    val response: IdentityVerificationResponse = request.body.nino.value match {
      case "MA000003A" => IdentityVerificationResponse(IdentityVerified(false), AmountInPence(12312))
      case _           => IdentityVerificationResponse(IdentityVerified(true), AmountInPence(123122))
    }
    Future.successful(Ok(Json.toJson(response)))
  }

}
