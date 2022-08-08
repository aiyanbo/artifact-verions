import Dependencies.{Versions, _}
import org.jmotor.sbt.plugin.ComponentSorter
import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

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

releasePublishArtifactsAction := PgpKeys.publishSigned.value

releaseCrossBuild := true

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  publishArtifacts,
  setNextVersion,
  commitNextVersion,
  pushChanges
)

credentials += Credentials(Path.userHome / ".sbt" / ".credentials")

//publishTo := sonatypePublishToBundle.value

//ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"


