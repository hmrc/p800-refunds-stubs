import scala.collection.immutable.Seq

val strictBuilding: SettingKey[Boolean] = StrictBuilding.strictBuilding //defining here so it can be set before running sbt like `sbt 'set Global / strictBuilding := true' ...`
StrictBuilding.strictBuildingSetting

lazy val microservice = Project("p800-refunds-stubs", file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(
    majorVersion        := 0,
    scalaVersion        := "2.13.12",
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test,
    Compile / doc / scalacOptions := Seq(), // This will allow warnings in `doc` task
    Test / doc / scalacOptions := Seq(), // This will allow warnings in `doc` task
    scalacOptions ++= ScalaCompilerFlags.scalaCompilerOptions,
    scalacOptions ++= {
      if (StrictBuilding.strictBuilding.value) ScalaCompilerFlags.strictScalaCompilerOptions else Nil
    },
    pipelineStages := Seq(gzip),
    Compile / scalacOptions -= "utf8"
  )
  .settings(
      routesImport ++= Seq(
          "uk.gov.hmrc.p800refundsstubs.models.Nino",
          "uk.gov.hmrc.p800refundsstubs.models.edh.ClaimId",
          "uk.gov.hmrc.p800refundsstubs.models.P800Reference"
      ))
  .settings(resolvers += Resolver.jcenterRepo)
  .settings(CodeCoverageSettings.settings: _*)
  .settings(commands ++= SbtCommands.commands)
  .settings(ScalariformSettings.scalariformSettings: _*)
  .settings(SbtUpdatesSettings.sbtUpdatesSettings: _*)
  .settings(CodeCoverageSettings.settings: _*)
  .settings(WartRemoverSettings.wartRemoverSettings)
  .settings(PlayKeys.playDefaultPort := 10151)
