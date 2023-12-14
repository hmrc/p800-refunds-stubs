import sbt._

object AppDependencies {

  private val bootstrapVersion = "8.2.0"
  private val hmrcMongoVersion = "1.6.0"

  val compile: Seq[ModuleID] = Seq(
    // format: OFF
    "uk.gov.hmrc"       %% "bootstrap-backend-play-28" % bootstrapVersion,
    "uk.gov.hmrc.mongo" %% "hmrc-mongo-play-28"        % hmrcMongoVersion,
    "org.typelevel"     %% "cats-core"                 % "2.10.0",
    "com.beachape"      %% "enumeratum-play-json"      % "1.7.3"
  // format: ON
  )

  val test: Seq[ModuleID] = Seq(
    // format: OFF
    "uk.gov.hmrc"             %% "bootstrap-test-play-28"  % bootstrapVersion % "test, it",
    "uk.gov.hmrc.mongo"       %% "hmrc-mongo-test-play-28" % hmrcMongoVersion % Test,
    "org.scalatest"           %% "scalatest"               % "3.2.17"         % Test,
    "com.vladsch.flexmark"    %  "flexmark-all"            % "0.62.2"         % Test,
    "org.scalatestplus.play"  %% "scalatestplus-play"      % "5.1.0"          % Test,
  // format: ON
  )
}
