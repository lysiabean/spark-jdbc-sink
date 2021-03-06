val sparkVersion = "2.3.0"

assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", xs @ _*) => MergeStrategy.last
  case PathList("javax", "activation", xs @ _*) => MergeStrategy.last
  case PathList("org", "apache", xs @ _*) => MergeStrategy.first
  case PathList("com", "esotericsoftware", xs @ _*) => MergeStrategy.last
  case PathList("org", "aopalliance", xs @ _*) => MergeStrategy.last
  case "about.html" => MergeStrategy.rename
  case "git.properties" => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

lazy val root = (project in file("."))
  .settings(
    organization := "com.cognite.spark",
    name := "spark-jdbc-sink",
    assemblyJarName in assembly := "spark-jdbc-sink-with-dependencies.jar",
    version := "0.0.2",
    scalaVersion := "2.11.12",
    libraryDependencies ++= Seq(
      "org.wso2.carbon.metrics" % "org.wso2.carbon.metrics.jdbc.reporter" % "2.3.7",
      "org.apache.commons" % "commons-dbcp2" % "2.1.1",
      "org.apache.spark" %% "spark-core" % sparkVersion % "provided"
        exclude("org.glassfish.hk2.external", "javax.inject"),
      "org.apache.spark" %% "spark-sql" % sparkVersion % "provided"
        exclude("org.glassfish.hk2.external", "javax.inject"),
      "org.eclipse.jetty" % "jetty-servlet" % "9.3.20.v20170531" % "provided"
    ),
    publishTo :=
      Some("Sonatype Nexus Repository Manager" at
        "https://repository.dev.cognite.ai/repository/cognite/"),
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
    publishMavenStyle := true,
    crossScalaVersions := Seq("2.11.12"),
  )

// Don't include Scala in the assembly, we should use the version included in Spark instead
assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
