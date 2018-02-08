package org.jmotor.artifact.metadata

import org.apache.maven.artifact.versioning.ArtifactVersion

import scala.concurrent.Future

/**
 * Component:
 * Description:
 * Date: 2018/2/8
 *
 * @author AI
 */
trait MetadataLoader {

  def getVersions(organization: String, artifactId: String): Future[Seq[ArtifactVersion]]

}
