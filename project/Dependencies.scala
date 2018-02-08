import sbt._

object Dependencies {

  object Versions {
    val ivy = "2.4.0"
    val scalaXml = "1.0.6"
    val scala211 = "2.11.11"
    val scala212 = "2.12.4"
    val scalaTest = "3.0.5"
    val scalaLogging = "3.7.2"
    val mavenArtifact = "3.5.2"
    val searchMavenSdk = "1.0.0-SNAPSHOT"
  }

  object Compile {
    val ivy = "org.apache.ivy" % "ivy" % Versions.ivy % "provided"
    val scalaXml = "org.scala-lang.modules" %% "scala-xml" % Versions.scalaXml
    val mavenArtifact = "org.apache.maven" % "maven-artifact" % Versions.mavenArtifact
    val searchMavenSdk = "org.jmotor.tools" %% "search-maven-org-scala-sdk" % Versions.searchMavenSdk
    val scalaLogging: ModuleID = "com.typesafe.scala-logging" %% "scala-logging" % Versions.scalaLogging
  }

  object Test {
    val scalaTest: ModuleID = "org.scalatest" %% "scalatest" % Versions.scalaTest % "test"
  }

  import Compile._

  lazy val dependencies = Seq(ivy, scalaXml, scalaLogging, searchMavenSdk, mavenArtifact, Test.scalaTest)

}
