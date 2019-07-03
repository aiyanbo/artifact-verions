package org.jmotor.artifact.metadata

import org.apache.maven.artifact.versioning.DefaultArtifactVersion
import org.jmotor.artifact.Versions
import org.scalatest.FunSuite

/**
 *
 * @author AI
 *         2019-07-04
 */
class VersionsSpec extends FunSuite {

  test("test rc version") {
    assert(Versions.isReleaseVersion(new DefaultArtifactVersion("3.9.0")))
    assert(!Versions.isReleaseVersion(new DefaultArtifactVersion("3.9.0-rc-1")))
    assert(!Versions.isReleaseVersion(new DefaultArtifactVersion("2.0.0-RC2-1")))
  }

}
