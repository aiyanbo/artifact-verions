import sbt._

object Dependencies {

  object Versions {
    val ivy = "2.5.0"
    val mavenArtifact = "3.8.6"
    val scala = "2.13.9"
    val scala211 = "2.11.11"
    val scala212 = "2.12.16"
    val scalaLibrary = "2.13.0"
    val scalaXml = "2.1.0"
    val scalatest = "3.2.14"
    val searchMavenOrgScalaSdk = "1.0.3"
  }

  object Compile {
    val ivy = "org.apache.ivy" % "ivy" % Versions.ivy % "provided"
    val scalaXml = "org.scala-lang.modules" %% "scala-xml" % Versions.scalaXml
    val mavenArtifact = "org.apache.maven" % "maven-artifact" % Versions.mavenArtifact
    val searchMavenSdk = "org.jmotor.tools" %% "search-maven-org-scala-sdk" % Versions.searchMavenOrgScalaSdk
  }

  object Test {
    val scalaTest: ModuleID = "org.scalatest" %% "scalatest" % Versions.scalatest % "test"
  }

  import Compile._

  lazy val dependencies = Seq(ivy, scalaXml, searchMavenSdk, mavenArtifact, Test.scalaTest)

}
