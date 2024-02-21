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
import uk.gov.hmrc.p800refundsstubs.models.bankconsent.{BankConsentRequest, BankConsentResponse}
import uk.gov.hmrc.p800refundsstubs.models.bankverification.{BankVerification, BankVerificationRequest}
import uk.gov.hmrc.p800refundsstubs.models.{BankAccountSummaryResponse}
import uk.gov.hmrc.p800refundsstubs.services.{BankVerificationService, BankConsentService, BankAccountService}
import uk.gov.hmrc.p800refundsstubs.util.SafeEquals.EqualsOps
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton()
class EcospendController @Inject() (
    cc:                      ControllerComponents,
    bankVerificationService: BankVerificationService,
    bankConsentService:      BankConsentService,
    bankAccountService:      BankAccountService
)(implicit executionContext: ExecutionContext) extends BackendController(cc) {

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

  val notification: Action[BankVerificationRequest] = Action.async(parse.json[BankVerificationRequest]) { implicit request =>
    val bankVerificationRequest = request.body
    performAccessTokenHeaderCheck {
      bankVerificationService
        .findData(bankVerificationRequest)
        .foldF[Result](bankVerificationService.insertData(bankVerificationRequest).map(_ => PaymentRequired)){
          bankVerificationResult: BankVerification => Future.successful(Ok(Json.toJson(bankVerificationResult)))
        }
    }
  }

  val createConsent: Action[BankConsentRequest] = Action.async(parse.json[BankConsentRequest]) { implicit request =>
    val bankConsentRequest = request.body

    bankConsentRequest.bankId match {
      case "test-bad-request" =>
        Future.successful(BadRequest(Json.toJson(EcospendData.badRequestErrorReponse)))
      case "test-unauthorized-401" =>
        Future.successful(Unauthorized("Unauthorized"))
      case "test-server-error-500" =>
        Future.successful(InternalServerError(Json.toJson(EcospendData.internalServerErrorResponse)))
      case "test-server-error-502" =>
        Future.successful(BadGateway(Json.toJson(EcospendData.badGatewayErrorResponse)))
      case "test-server-error-503" =>
        Future.successful(ServiceUnavailable(Json.toJson(EcospendData.badRequestErrorReponse)))
      case _ =>
        performAccessTokenHeaderCheck {
          bankConsentService
            .insertData(bankConsentRequest)
            .map {
              bankConsentResponse: BankConsentResponse => Ok(Json.toJson(bankConsentResponse))
            }
        }
    }
  }

  private val consentIdHeaderKey: String = "consent_id"

  def accountSummary(merchant_id: Option[String], merchant_user_id: Option[String]): Action[AnyContent] = Action.async { implicit request =>
    performAccessTokenHeaderCheck {
      logger.info(s"Account summary: Query Parameters: merchant_id: [${merchant_id.toString}], merchant_user_id: [${merchant_user_id.toString}]")

      val consentId: Option[String] = request.headers.headers.find(_._1.toLowerCase() === consentIdHeaderKey.toLowerCase()).map(_._2)
      if (consentId.isEmpty)
        logger.error(s"Missing '${consentIdHeaderKey}' header")

      consentId
        .fold(Future.successful(BadRequest(Json.toJson(EcospendData.badRequestErrorReponse)))) { consentId: String =>
          bankAccountService
            .getAccountSummary(consentId)
            .foldF[Result](Future.successful(NoContent)) {
              bankAccountSummaryResponse: BankAccountSummaryResponse => Future.successful(Ok(Json.toJson(bankAccountSummaryResponse)))
            }
        }
    }
  }

}

