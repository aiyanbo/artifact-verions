import sbt._

object Dependencies {

  object Versions {
    val scala211 = "2.11.11"
    val scala212 = "2.12.4"
    val scalaTest = "3.0.5"
    val scalaLogging = "3.7.2"
  }

  object Compile {
    val scalaLogging: ModuleID = "com.typesafe.scala-logging" %% "scala-logging" % Versions.scalaLogging
  }

  object Test {
    val scalaTest: ModuleID = "org.scalatest" %% "scalatest" % Versions.scalaTest % "test"
  }

  import Compile._

  lazy val dependencies = Seq(scalaLogging, Test.scalaTest)

}
