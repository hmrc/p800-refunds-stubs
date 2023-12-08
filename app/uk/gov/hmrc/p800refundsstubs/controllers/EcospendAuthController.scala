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
import play.api.mvc.{Action, ControllerComponents}
import uk.gov.hmrc.p800refundsstubs.models.EcospendErrorResponse
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.{Inject, Singleton}

@Singleton()
class EcospendAuthController @Inject() (cc: ControllerComponents) extends BackendController(cc) {

  val logger: Logger = Logger(this.getClass)

  val accessToken: Action[Map[String, Seq[String]]] = Action(parse.formUrlEncoded) { implicit request =>

    val grantType = request.body.get("grant_type")
    val clientId = request.body.get("client_id")
    val clientSecret = request.body.get("client_secret")
    val scope = request.body.get("scope")

    (grantType, clientId, clientSecret, scope) match {
      case (Some(Seq("client_credentials")), Some(Seq("stub-client-id")), Some(Seq("stub-client-secret")), Some(Seq("px01.ecospend.pis.sandbox"))) =>
        val accessToken = "1234567890"

        val responseBody = Json.obj(
          "access_token" -> accessToken,
          "expires_in" -> 300,
          "token_type" -> "client_credentials",
          "scope" -> "px01.ecospend.pis.sandbox"
        )

        logger.info(s"*** ECOSPEND STUB AUTH - stubbed auth request success, providing access token: $accessToken ***")

        Ok(responseBody)
      case (Some(Seq("client_credentials")), _, _, Some(Seq("px01.ecospend.pis.sandbox"))) =>
        val responseBody =
          EcospendErrorResponse("INVALID_CLIENT_CREDENTIALS", "Client credentials are missing or invalid", Json.obj())

        Unauthorized(Json.toJson(responseBody))
      case _ =>
        val responseBody =
          EcospendErrorResponse("INVALID_REQUEST", "Important data was missing from the request or invalid", Json.obj())

        BadRequest(Json.toJson(responseBody))
    }
  }
}
