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

import play.api.libs.json.Json
import uk.gov.hmrc.p800refundsstubs.models.Nino
import uk.gov.hmrc.p800refundsstubs.testsupport.UnitSpec

class TraceIndividualResponseSpec extends UnitSpec {

  "serialize" in {

    val t = TraceIndividualResponse(List(TracedIndividual(
      identifier = Nino("AB999999C")
    )))

    Json.toJson(t) shouldBe Json.parse(
      //language=JSON
      """{
        |  "traceIndividualResponse" : [{
        |    "identifier" : "AB999999C",
        |    "firstForename" : "Alice",
        |    "secondForename" : "B",
        |    "surname" : "Crawford",
        |    "dateOfBirth" : "1978-01-02",
        |    "addressType" : "RESIDENTIAL",
        |    "addressLine1" : "Flat 1 Rose House",
        |    "addressLine2" : "Worthing",
        |    "addressPostcode" : "BN12 4XL",
        |    "country" : "ENGLAND"
        |  }]
        |}""".stripMargin
    )
  }

}
