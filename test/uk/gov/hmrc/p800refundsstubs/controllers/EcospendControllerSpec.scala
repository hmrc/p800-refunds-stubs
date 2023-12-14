package uk.gov.hmrc.p800refundsstubs.controllers

import play.api.http.HeaderNames.CONTENT_TYPE
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.p800refundsstubs.models.bankverification._
import uk.gov.hmrc.p800refundsstubs.testsupport.ItSpec

class EcospendControllerSpec extends ItSpec {

  private val ecospendController = app.injector.instanceOf[EcospendController]

  private def fakeRequest(identifier: String): FakeRequest[BankVerificationRequest] = FakeRequest()
    .withBody(BankVerificationRequest(identifier))
    .withHeaders(CONTENT_TYPE -> JSON)

  private def prepMongo(identifier: String, verificationStatus: VerificationStatus) =
    bankVerificationRepo
      .insert(BankVerificationEntry(java.time.Instant.now(), BankVerification(identifier, verificationStatus)))
      .futureValue

  "POST /notification" - {
    "return 404 if no entry in mongo" in {
      val result = ecospendController.notification(fakeRequest("AB123456A"))
      status(result) shouldBe 404
    }

    "return Successful when that is the value in mongo" in {
      val identifier: String = "LM001014C"
      prepMongo(identifier, VerificationStatus.Successful)
      val result = ecospendController.notification(fakeRequest(identifier))
      status(result) shouldBe 200
      //language=JSON
      val expectedJsonResponse = """{"identifier":"LM001014C","verificationStatus":"Successful"}"""
      contentAsString(result) shouldBe expectedJsonResponse
    }

    "return UnSuccessful when that is the value in mongo" in {
      val identifier: String = "AB123456B"
      prepMongo(identifier, VerificationStatus.UnSuccessful)
      val result = ecospendController.notification(fakeRequest(identifier))
      status(result) shouldBe 200
      //language=JSON
      val expectedJsonResponse = """{"identifier":"AB123456B","verificationStatus":"UnSuccessful"}"""
      contentAsString(result) shouldBe expectedJsonResponse
    }

    "update to a status when called a second time by the frontend" in {
      val identifier: String = "AB123456C"
      val firstCall = ecospendController.notification(fakeRequest(identifier))
      status(firstCall) shouldBe 404
      val secondCall = ecospendController.notification(fakeRequest(identifier))
      status(secondCall) shouldBe 200
      //language=JSON
      val expectedJsonResponse = """{"identifier":"AB123456C","verificationStatus":"UnSuccessful"}"""
      contentAsString(secondCall) shouldBe expectedJsonResponse
    }
  }
}
