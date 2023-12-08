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
import play.api.mvc._
import uk.gov.hmrc.p800refundsstubs.EcospendData
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton()
class EcospendController @Inject() (cc: ControllerComponents) extends BackendController(cc) {

  val logger: Logger = Logger(this.getClass)

  def banks(): Action[AnyContent] = Action.async { implicit request =>
    performAccessTokenHeaderCheck(Future.successful(Ok(EcospendData.getBanksResponseJson)))
  }

  private def performAccessTokenHeaderCheck(body: => Future[Result])(implicit request: Request[_]): Future[Result] = {
    val acceptedAccessToken = "1234567890"
    val expectedAuthHeaderContent = s"Bearer $acceptedAccessToken"

    request.headers.get("Authorization") match {
      case Some(token) if token.contains(expectedAuthHeaderContent) =>
        logger.info(s"*** ECOSPEND STUB AUTH - stubbed auth check success, accepted access token: $acceptedAccessToken ***")
        body
      case Some(rejectedAccessToken) =>
        logger.info(s"*** ECOSPEND STUB AUTH - stubbed auth check failed, rejected access token: $rejectedAccessToken ***")
        Future.successful(Unauthorized(Json.toJson(EcospendData.invalidAccessTokenCheckResponse)))
      case None =>
        logger.info(s"*** ECOSPEND STUB AUTH - stubbed auth check failed, no access token present in header ***")
        Future.successful(Unauthorized(Json.toJson(EcospendData.missingAccessTokenCheckResponse)))
    }
  }
}

