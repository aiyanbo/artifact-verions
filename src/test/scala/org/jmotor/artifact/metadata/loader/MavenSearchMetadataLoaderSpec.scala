package org.jmotor.artifact.metadata.loader

import org.apache.maven.artifact.versioning.DefaultArtifactVersion
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
class MavenSearchMetadataLoaderSpec extends AnyFunSuite with TestContext {
  private[this] val g = "com.google.guava"
  private[this] val a = "guava"

  test("get versions") {
    val loader = MavenSearchMetadataLoader()
    val future = loader.getVersions(g, a)
    val version = new DefaultArtifactVersion("24.0-jre")
    val results = Await.result(future, 10.seconds).filter(av ⇒ version.getQualifier.equals(av.getQualifier))
    assert(results.max.compareTo(version) >= 0)
  }

  test("sbt versions") {
    val loader = MavenSearchMetadataLoader()
    val future = loader.getVersions("org.scala-sbt", "sbt")
    val version = new DefaultArtifactVersion("1.1.0")
    val results = Await.result(future, 10.seconds)
    assert(results.max.compareTo(version) >= 0)
  }

}
