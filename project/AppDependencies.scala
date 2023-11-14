import play.core.PlayVersion
import play.sbt.PlayImport._
import sbt.Keys.libraryDependencies
import sbt._

object AppDependencies {

  private val bootstrapVersion = "7.23.0"
  private val hmrcMongoVersion = "1.4.0"

  val compile = Seq(
    "uk.gov.hmrc" %% "bootstrap-backend-play-28" % bootstrapVersion,
    "uk.gov.hmrc.mongo" %% "hmrc-mongo-play-28" % hmrcMongoVersion,
    "org.typelevel" %% "cats-core" % "2.10.0"
  )

  val test = Seq(
    "uk.gov.hmrc" %% "bootstrap-test-play-28" % bootstrapVersion % "test, it",
    "uk.gov.hmrc.mongo" %% "hmrc-mongo-test-play-28" % hmrcMongoVersion % Test,
    "org.scalatest" %% "scalatest" % "3.2.17" % Test,
    "com.vladsch.flexmark" % "flexmark-all" % "0.62.2" % Test,
    "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test,
  )
}
