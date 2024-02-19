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

package uk.gov.hmrc.p800refundsstubs.actions

import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.Results.BadRequest
import play.api.mvc._
import uk.gov.hmrc.p800refundsstubs.actions.CorrelationId.WithCorrelationId
import uk.gov.hmrc.p800refundsstubs.models.nps.{Failure, P800ReferenceCheckResultFailures}
import uk.gov.hmrc.p800refundsstubs.util.SafeEquals.EqualsOps

import java.util.Base64
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class Actions @Inject() (
    actionBuilder: DefaultActionBuilder
)(implicit ec: ExecutionContext) {
  self =>

  val default: ActionBuilder[Request, AnyContent] = actionBuilder

  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  val npsAction: ActionBuilder[NpsRequest, AnyContent] =
    default
      .andThen(correlationIdRefiner)
      .andThen(basicAuthRefiner())
      .andThen(ensureGovUkOriginatorId)
      .andThen(addCorrelationIdActionBuilder())

  def basicAuthRefiner(): ActionRefiner[NpsRequest, NpsRequest] = new ActionRefiner[NpsRequest, NpsRequest] {
    def forbidden(reason: String): Result = Results.Forbidden(Json.toJson(Failure(reason = s"Forbidden - $reason", code = "403.2")))

    override protected def refine[A](request: NpsRequest[A]): Future[Either[Result, NpsRequest[A]]] = {

      request.headers.get("Authorization") match {
        case Some(authHeader) if authHeader.startsWith("Basic ") =>
          decodeBasicAuth(authHeader) match {
            case Some((username, password)) if isValidUser(username, password) =>
              Future.successful(Right(request))
            case a =>
              logger.info(s"Forbidden, invalid credentials ${a.toString}")
              Future.successful(Left(forbidden("invalid credentials")))
          }
        case _ =>
          logger.info(s"Forbidden, missing Authorization header")
          Future.successful(Left(forbidden("missing Authorization header")))
      }
    }

    private def isValidUser(username: String, password: String): Boolean = {
      //testNpsUserName:testNpsPassword is dGVzdE5wc1VzZXJOYW1lOnRlc3ROcHNQYXNzd29yZA==
      username === "testNpsUserName" && password === "testNpsPassword"
    }

    private def decodeBasicAuth(authHeader: String): Option[(String, String)] = {
      val base64Credentials: String = authHeader.substring("Basic ".length).trim
      val credentials = new String(Base64.getDecoder.decode(base64Credentials))
      credentials.split(":", 2) match {
        case Array(username, password) => Some((username, password))
        case _                         => None
      }
    }

    override protected def executionContext: ExecutionContext = ec

    lazy val logger: Logger = Logger(this.getClass)
  }

  private lazy val correlationIdRefiner: ActionRefiner[Request, NpsRequest] = new ActionRefiner[Request, NpsRequest] {

    override protected def refine[A](request: Request[A]): Future[Either[Result, NpsRequest[A]]] = {
      val correlationId: Option[CorrelationId] = request.headers.headers.find(_._1.toLowerCase() === CorrelationId.key.toLowerCase()).map(_._2).map(CorrelationId.apply)

      correlationId.fold[Future[Either[Result, NpsRequest[A]]]] {
        val correlationId = CorrelationId.fresh()
        val message: String = s"Missing '${CorrelationId.key}' header."
        logger.error(message)
        Future.successful(Left[Result, NpsRequest[A]](BadRequest(message).withCorrelationId(correlationId)))
      } { correlationId: CorrelationId =>
        Future.successful(Right[Result, NpsRequest[A]](new NpsRequest(correlationId, request)))
      }
    }

    override protected def executionContext: ExecutionContext = ec

    lazy val logger: Logger = Logger(this.getClass)
  }

  private def addCorrelationIdActionBuilder[R[_]](): ActionFunction[R, R] = new ActionFunction[R, R] {
    override protected def executionContext: ExecutionContext = ec

    override def invokeBlock[A](request: R[A], block: R[A] => Future[Result]): Future[Result] = request match {
      case r: NpsRequest[_] => block(request).map(_.withCorrelationId(r.correlationId))
      case _                => block(request).map(_.withCorrelationId(CorrelationId.fresh()))
    }
  }

  private lazy val ensureGovUkOriginatorId = new ActionFilter[NpsRequest] {
    override protected def filter[A](request: NpsRequest[A]): Future[Option[Result]] = Future.successful{

      val `gov-uk-originator-id`: Option[String] = request.headers.headers.find(_._1.toLowerCase() === "gov-uk-originator-id").map(_._2)
      if (`gov-uk-originator-id`.exists(_ === "DA2_MRA_DIGITAL")) None else {
        Some(BadRequest(Json.toJson(P800ReferenceCheckResultFailures(
          failures = List(
            Failure(reason = s"Invalid or missing 'gov-uk-originator-id' ", code = "400.invalid-or-missing-gov-uk-originator-id")
          )
        ))))
      }
    }

    override protected def executionContext: ExecutionContext = ec
  }
}
