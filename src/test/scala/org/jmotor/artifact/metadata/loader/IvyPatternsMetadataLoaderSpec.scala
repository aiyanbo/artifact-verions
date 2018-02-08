package org.jmotor.artifact.metadata.loader

import org.apache.maven.artifact.versioning.DefaultArtifactVersion
import org.scalatest.FunSuite

import scala.concurrent.Await
import scala.concurrent.duration._

/**
 * Component:
 * Description:
 * Date: 2018/2/8
 *
 * @author AI
 */
class IvyPatternsMetadataLoaderSpec extends FunSuite with TestContext {
  private[this] val g = "com.typesafe"
  private[this] val a = "emoji_2.12"

  test("get versions") {
    val patterns = Seq("https://repo.typesafe.com/typesafe/ivy-releases/[organization]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext]")
    val loader = new IvyPatternsMetadataLoader(patterns, None)
    val future = loader.getVersions(g, a)
    val results = Await.result(future, 10.seconds)
    val version = new DefaultArtifactVersion("1.1.0")
    assert(results.max.compareTo(version) >= 0)
  }

}
