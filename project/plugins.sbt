resolvers += MavenRepository("HMRC-open-artefacts-maven2", "https://open.artefacts.tax.service.gov.uk/maven2")
resolvers += Resolver.url("HMRC-open-artefacts-ivy2", url("https://open.artefacts.tax.service.gov.uk/ivy2"))(Resolver.ivyStylePatterns)
resolvers += Resolver.typesafeRepo("releases")

addSbtPlugin("uk.gov.hmrc"       %  "sbt-auto-build"        % "3.16.0")
addSbtPlugin("uk.gov.hmrc"       %  "sbt-distributables"    % "2.2.0")
addSbtPlugin("com.typesafe.play" %  "sbt-plugin"            % "2.8.21")
addSbtPlugin("org.scoverage"     %  "sbt-scoverage"         % "1.9.3")
addSbtPlugin("com.typesafe.sbt"  %  "sbt-gzip"              % "1.0.2")
addSbtPlugin("org.scalastyle"    %% "scalastyle-sbt-plugin" % "1.0.0")
addSbtPlugin("org.scalariform"   %% "sbt-scalariform"       % "1.8.3")
addSbtPlugin("org.wartremover"   %  "sbt-wartremover"       % "3.0.7")
addSbtPlugin("com.timushev.sbt"  %  "sbt-updates"           % "0.6.3")
addDependencyTreePlugin