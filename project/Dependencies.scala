import sbt._

object Dependencies {

  object Versions {
    val ivy = "2.4.0"
    val scalaXml = "1.1.0"
    val scala211 = "2.11.11"
    val scala212 = "2.12.6"
    val scalaTest = "3.0.5"
    val mavenArtifact = "3.5.4"
    val searchMavenSdk = "1.0.0"
  }

  object Compile {
    val ivy = "org.apache.ivy" % "ivy" % Versions.ivy % "provided"
    val scalaXml = "org.scala-lang.modules" %% "scala-xml" % Versions.scalaXml
    val mavenArtifact = "org.apache.maven" % "maven-artifact" % Versions.mavenArtifact
    val searchMavenSdk = "org.jmotor.tools" %% "search-maven-org-scala-sdk" % Versions.searchMavenSdk
  }

  object Test {
    val scalaTest: ModuleID = "org.scalatest" %% "scalatest" % Versions.scalaTest % "test"
  }

  import Compile._

  lazy val dependencies = Seq(ivy, scalaXml, searchMavenSdk, mavenArtifact, Test.scalaTest)

}
