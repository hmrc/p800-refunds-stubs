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

package uk.gov.hmrc.p800refundsstubs.services

import cats.data.OptionT
import com.google.inject.{Inject, Singleton}
import uk.gov.hmrc.p800refundsstubs.models.bankverification
import uk.gov.hmrc.p800refundsstubs.models.bankverification.{BankVerification, BankVerificationEntry, BankVerificationRequest, VerificationStatus}
import uk.gov.hmrc.p800refundsstubs.repo.BankVerificationRepo

import java.time.Instant
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BankVerificationService @Inject() (bankVerificationRepo: BankVerificationRepo)(implicit executionContext: ExecutionContext) {

  def insertData(bankVerificationRequest: BankVerificationRequest): Future[Unit] = {
    val statusToUpdateTo: VerificationStatus = bankVerificationRequest match {
      case BankVerificationRequest("MA000003B") => bankverification.VerificationStatus.UnSuccessful
      case _                                    => bankverification.VerificationStatus.Successful
    }
    bankVerificationRepo
      .insert(BankVerificationEntry(Instant.now(), BankVerification(bankVerificationRequest.identifier, statusToUpdateTo)))
      .map(_ => ())
  }

  def findData(bankVerificationRequest: BankVerificationRequest): OptionT[Future, BankVerification] = {
    bankVerificationRepo
      .findBankVerificationEntry(bankVerificationRequest.identifier)
      .map(_.bankVerification)
  }

}
