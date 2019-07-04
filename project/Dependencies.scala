import sbt._

object Dependencies {

  object Versions {
    val ivy = "2.4.0"
    val scalaXml = "1.2.0"
    val scala212 = "2.12.8"
    val scala213 = "2.13.0"
    val scalatest = "3.0.8"
    val scala211 = "2.11.11"
    val scalaLibrary = "2.13.0"
    val mavenArtifact = "3.6.1"
    val searchMavenSdk = "1.0.1"
  }

  object Compile {
    val ivy = "org.apache.ivy" % "ivy" % Versions.ivy % "provided"
    val scalaXml = "org.scala-lang.modules" %% "scala-xml" % Versions.scalaXml
    val mavenArtifact = "org.apache.maven" % "maven-artifact" % Versions.mavenArtifact
    val searchMavenSdk = "org.jmotor.tools" %% "search-maven-org-scala-sdk" % Versions.searchMavenSdk
  }

  object Test {
    val scalaTest: ModuleID = "org.scalatest" %% "scalatest" % Versions.scalatest % "test"
  }

  import Compile._

  lazy val dependencies = Seq(ivy, scalaXml, searchMavenSdk, mavenArtifact, Test.scalaTest)

}
