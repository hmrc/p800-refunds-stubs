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

package uk.gov.hmrc.p800refundsstubs.models

import play.api.libs.json.{Format, Json}
import play.api.mvc.PathBindable
import uk.gov.hmrc.p800refundsstubs.util.ValueClassBinder

import scala.util.matching.Regex

final case class Nino(value: String) {
  def isValid: Boolean = Nino.regex.matches(value)
}

object Nino {
  implicit val format: Format[Nino] = Json.valueFormat[Nino]
  implicit val pathBindable: PathBindable[Nino] = ValueClassBinder.valueClassBinder(_.value)

  private val regex: Regex = """^(((?:[ACEHJLMOPRSWXY][A-CEGHJ-NPR-TW-Z]|B[A-CEHJ-NPR-TW-Z]|G[ACEGHJ-NPR-TW-Z]|[KT][A-CEGHJ-MPR-TW-Z]|N[A-CEGHJL-NPR-SW-Z]|Z[A-CEGHJ-NPR-TW-Y])[0-9]{6}[A-D]?)|([0-9]{2}[A-Z]{1}[0-9]{5}))$""".r
}
