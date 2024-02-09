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

sealed trait P800ReferenceCheckScenario

object P800ReferenceCheckScenario {

  def selectScenario(nino: Nino): P800ReferenceCheckScenario = nino.value match {
    case s if "..0......".r.matches(s) => NinoAndP800RefNotMatched
    case s if "..1......".r.matches(s) => RefundAlreadyTaken
    case s if "..2......".r.matches(s) => UnprocessedEntity
    case s if "..3......".r.matches(s) => BadRequest
    case s if "..4......".r.matches(s) => Forbidden
    case s if "..5......".r.matches(s) => InternalServerError
    case _                             => HappyPath
  }

  case object NinoAndP800RefNotMatched extends P800ReferenceCheckScenario
  case object RefundAlreadyTaken extends P800ReferenceCheckScenario
  case object UnprocessedEntity extends P800ReferenceCheckScenario //Other unprocessed entity
  case object BadRequest extends P800ReferenceCheckScenario
  case object Forbidden extends P800ReferenceCheckScenario
  case object InternalServerError extends P800ReferenceCheckScenario
  case object HappyPath extends P800ReferenceCheckScenario
}

