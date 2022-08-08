import Dependencies.{Versions, _}
import org.jmotor.sbt.plugin.ComponentSorter

name := "artifact-versions"

organization := "org.jmotor.artifact"

scalaVersion := Versions.scala213

crossScalaVersions := Seq(Versions.scala212, Versions.scala213)

dependencyUpgradeModuleNames := Map(
  "scala-library" -> "scala",
  "undertow-.*" -> "undertow"
)

libraryDependencies ++= dependencies

dependencyUpgradeComponentSorter := ComponentSorter.ByAlphabetically

inThisBuild(List(
  sonatypeProfileName := "org.jmotor",
  homepage := Some(url("https://github.com/aiyanbo/artifact-verions")),
  licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
  developers := List(
    Developer(
      "aiyanbo",
      "Andy Ai",
      "yanbo.ai@gmail.com",
      url("https://aiyanbo.github.io/")
    )
  )
))
