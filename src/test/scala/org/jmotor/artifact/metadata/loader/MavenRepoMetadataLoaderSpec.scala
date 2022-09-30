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
class MavenRepoMetadataLoaderSpec extends AnyFunSuite with TestContext {
  private[this] val g = "org.jmotor"
  private[this] val a = "scala-utils_2.12"

  test("get versions") {
    val loader = new MavenRepoMetadataLoader("http://maven.aliyun.com/nexus/content/groups/public/", None)
    val future = loader.getVersions(g, a)
    val version = new DefaultArtifactVersion("1.0.2")
    val results = Await.result(future, 10.seconds)
    assert(results.max.compareTo(version) >= 0)
  }

  test("") {

  }

}
