import sbt._

object AppDependencies {

  private val bootstrapVersion = "8.6.0"
  private val hmrcMongoVersion = "1.9.0"

  val compile: Seq[ModuleID] = Seq(
    // format: OFF
    "uk.gov.hmrc" %% "bootstrap-backend-play-30" % bootstrapVersion,
    "uk.gov.hmrc.mongo" %% "hmrc-mongo-play-30" % hmrcMongoVersion,
    "org.typelevel" %% "cats-core" % "2.12.0",
    "com.beachape" %% "enumeratum-play-json" % "1.8.0"
  // format: ON
  )

  val test: Seq[ModuleID] = Seq(
    // format: OFF
    "uk.gov.hmrc"             %% "bootstrap-test-play-30"  % bootstrapVersion,
    "uk.gov.hmrc.mongo"       %% "hmrc-mongo-test-play-30" % hmrcMongoVersion,
    "org.scalatestplus.play"  %% "scalatestplus-play"      % "5.1.0",
  ).map(_ % Test)
  // format: ON
}
