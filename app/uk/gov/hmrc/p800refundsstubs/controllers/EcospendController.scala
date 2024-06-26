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
import uk.gov.hmrc.p800refundsstubs.models.{BankAccountSummaryResponse, BankAccountParty, BankPartyName, BankPartyFullLegalName}
import uk.gov.hmrc.p800refundsstubs.services.{BankConsentService, BankAccountService}
import uk.gov.hmrc.p800refundsstubs.util.SafeEquals.EqualsOps
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.annotation.nowarn
import scala.concurrent.{ExecutionContext, Future}

@Singleton()
class EcospendController @Inject() (
    cc:                 ControllerComponents,
    bankConsentService: BankConsentService,
    bankAccountService: BankAccountService
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
  private val developmentConsentIdHeaderKey: String = "consent-id"

  def accountSummary(@nowarn merchant_id: Option[String], @nowarn merchant_user_id: Option[String]): Action[AnyContent] = Action.async { implicit request =>
    performAccessTokenHeaderCheck {
      /**
       * MDTP doesn't accept for Http Headers with underscores.
       * This is why we're accepting extra header with hyphen instead so it can work on MDTP.
       * Client code will produce both headers.
       * Redundant header will be ignored on production like systems but it will make stubs working on MDTP.
       */
      val consentId: Option[UUID] =
        request.headers.headers.find(_._1.toLowerCase() === consentIdHeaderKey.toLowerCase()).map(_._2).map(UUID.fromString)
      val developmentConsentId: Option[UUID] =
        request.headers.headers.find(_._1.toLowerCase() === developmentConsentIdHeaderKey.toLowerCase()).map(_._2).map(UUID.fromString)

      consentId
        .orElse(developmentConsentId)
        .fold({
          logger.info("Missing consent_id and consent-id header. Responding with BadRequest")
          Future.successful(BadRequest(Json.toJson(EcospendData.badRequestErrorReponse)))
        }) { consentId: UUID =>
          bankAccountService
            .getAccountSummary(consentId)
            .foldF[Result](Future.successful(NoContent)) {
              bankAccountSummaryResponse: BankAccountSummaryResponse =>
                bankAccountSummaryResponse.value.headOption.map(_.bankId.getOrElse("")) match {
                  case Some("test-account-summary-bank-id-none") =>
                    Future.successful(Ok(Json.toJson(
                      bankAccountSummaryResponse.value.map(_.copy(bankId = None))
                    )))
                  case Some("test-account-summary-account-identification-none") =>
                    Future.successful(Ok(Json.toJson(
                      bankAccountSummaryResponse.value.map(_.copy(accountIdentification = None))
                    )))
                  case Some("test-account-summary-calculated-owner-name-none") =>
                    Future.successful(Ok(Json.toJson(
                      bankAccountSummaryResponse.value.map(_.copy(calculatedOwnerName = None))
                    )))
                  case Some("test-account-summary-display-name-none") =>
                    Future.successful(Ok(Json.toJson(
                      bankAccountSummaryResponse.value.map(_.copy(displayName = None))
                    )))
                  case Some("test-account-summary-parties-none") =>
                    Future.successful(Ok(Json.toJson(
                      bankAccountSummaryResponse.value.map(_.copy(parties = None))
                    )))
                  case Some("test-account-summary-parties-name-none") =>
                    Future.successful(Ok(Json.toJson(
                      bankAccountSummaryResponse.value.map(_.copy(parties = Some(List(
                        BankAccountParty(
                          name          = None,
                          fullLegalName = Some(BankPartyFullLegalName("Alice Crawford"))
                        ),
                        BankAccountParty(
                          name          = None,
                          fullLegalName = Some(BankPartyFullLegalName("Alice Crawford"))
                        )
                      ))))
                    )))
                  case Some("test-account-summary-parties-full-legal-name-none") =>
                    Future.successful(Ok(Json.toJson(
                      bankAccountSummaryResponse.value.map(_.copy(parties = Some(List(
                        BankAccountParty(
                          name          = Some(BankPartyName("Alice Crawford")),
                          fullLegalName = None
                        ),
                        BankAccountParty(
                          name          = Some(BankPartyName("Alice Crawford")),
                          fullLegalName = None
                        )
                      ))))
                    )))
                  case Some("test-account-summary-parties-all-name-none") =>
                    Future.successful(Ok(Json.toJson(
                      bankAccountSummaryResponse.value.map(_.copy(parties = Some(List(
                        BankAccountParty(
                          name          = None,
                          fullLegalName = None
                        ),
                        BankAccountParty(
                          name          = None,
                          fullLegalName = None
                        )
                      ))))
                    )))
                  case Some("test-account-summary-all-none") =>
                    Future.successful(Ok(Json.toJson(
                      bankAccountSummaryResponse.value.map(_.copy(
                        bankId                = None,
                        accountIdentification = None,
                        calculatedOwnerName   = None,
                        displayName           = None,
                        parties               = None
                      ))
                    )))
                  case _ =>
                    Future.successful(Ok(Json.toJson(bankAccountSummaryResponse)))
                }
            }
        }
    }
  }

}

