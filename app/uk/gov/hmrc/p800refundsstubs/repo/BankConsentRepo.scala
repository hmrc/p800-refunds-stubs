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

package uk.gov.hmrc.p800refundsstubs.repo

import cats.data.OptionT
import com.google.inject.{Inject, Singleton}
import org.mongodb.scala.model._
import org.mongodb.scala.result.UpdateResult
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository
import uk.gov.hmrc.p800refundsstubs.models.bankconsent.BankConsentEntry

import java.util.UUID
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

@SuppressWarnings(Array("org.wartremover.warts.Any"))
@Singleton
final class BankConsentRepo @Inject() (
    mongoComponent: MongoComponent
)(implicit ec: ExecutionContext)
  extends PlayMongoRepository[BankConsentEntry](
    mongoComponent = mongoComponent,
    collectionName = "p800-refunds-stubs-bank-consent",
    domainFormat   = BankConsentEntry.format,
    indexes        = BankConsentRepo.indexes(30.minutes.toSeconds),
    replaceIndexes = true
  ) {

  def insert(bankConsentEntry: BankConsentEntry): Future[UpdateResult] =
    collection.replaceOne(
      filter      = Filters.eq("bankConsentEntry.bankConsent.id", bankConsentEntry.bankConsent.id),
      replacement = bankConsentEntry,
      options     = new ReplaceOptions().upsert(true)
    ).toFuture()

  def findBankConsentEntry(id: UUID): OptionT[Future, BankConsentEntry] =
    OptionT(collection.find(Filters.eq("bankConsent.id", id.toString)).headOption())

}

object BankConsentRepo {

  def indexes(cacheTtlInSeconds: Long): Seq[IndexModel] = Seq(
    IndexModel(
      keys         = Indexes.ascending("createdAt"),
      indexOptions = IndexOptions().expireAfter(cacheTtlInSeconds, TimeUnit.SECONDS).name("createdAtIdx")
    )
  )
}
