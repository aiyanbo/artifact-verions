import Dependencies.{Versions, _}
import org.jmotor.sbt.plugin.ComponentSorter
import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

name := "artifact-versions"

organization := "org.jmotor.artifact"

scalaVersion := Versions.scala212

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
