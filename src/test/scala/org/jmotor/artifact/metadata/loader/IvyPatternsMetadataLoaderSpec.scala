package org.jmotor.artifact.metadata.loader

import org.apache.maven.artifact.versioning.DefaultArtifactVersion
import org.jmotor.artifact.metadata.MetadataLoader
import org.scalatest._
import org.scalatest.funsuite.AnyFunSuite

import scala.concurrent.Await
import scala.concurrent.duration._

/**
 * Component:
 * Description:
 * Date: 2018/2/8
 *
 * @author AI
 */
class IvyPatternsMetadataLoaderSpec extends AnyFunSuite with TestContext {

  test("get normal versions") {
    val g = "com.typesafe"
    val a = "emoji_2.12"
    val patterns = Seq("https://repo.typesafe.com/typesafe/ivy-releases/[organization]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext]")
    val loader: MetadataLoader = new IvyPatternsMetadataLoader(patterns, None)
    val future = loader.getVersions(g, a)
    val results = Await.result(future, 10.seconds)
    val version = new DefaultArtifactVersion("1.1.0")
    assert(results.max.compareTo(version) >= 0)
  }

  //  test("get sbt plugin versions") {
  //    val g = "org.jetbrains"
  //    val a = "sbt-idea-shell"
  //    val patterns = Seq("https://dl.bintray.com/sbt/sbt-plugin-releases/[organisation]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext]")
  //    val loader: MetadataLoader = new IvyPatternsMetadataLoader(patterns, None)
  //    val future = loader.getVersions(g, a, Map("scalaVersion" -> "2.12", "sbtVersion" -> "1.0"))
  //    val results = Await.result(future, 10.seconds)
  //    val version = new DefaultArtifactVersion("2017.2")
  //    assert(results.max.compareTo(version) >= 0)
  //  }

}
