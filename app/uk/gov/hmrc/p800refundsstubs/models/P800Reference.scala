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

import scala.util.{Failure, Success, Try}

final case class P800Reference(value: String) {
  val min: Int = 0
  val max: Int = 2147483646

  def isValid: Boolean = Try{
    val i = value.toInt
    i >= min && i <= max
  } match {
    case Success(r: Boolean) => r
    case Failure(_)          => false
  }

}

object P800Reference {
  implicit val format: Format[P800Reference] = Json.valueFormat[P800Reference]
  implicit val pathBindable: PathBindable[P800Reference] = ValueClassBinder.valueClassBinder(_.value)
}
