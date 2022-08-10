package org.jmotor.artifact.metadata.loader

import org.apache.maven.artifact.versioning.{ArtifactVersion, DefaultArtifactVersion}
import org.asynchttpclient.AsyncHttpClient
import org.jmotor.artifact.exception.ArtifactNotFoundException
import org.jmotor.artifact.metadata.MetadataLoader
import org.jmotor.tools.MavenSearchClient
import org.jmotor.tools.dto.MavenSearchRequest

import scala.concurrent.{ExecutionContext, Future}

/**
 * Component: Description: Date: 2018/2/8
 *
 * @author
 *   AI
 */
class MavenSearchMetadataLoader(maxRows: Int, client: MavenSearchClient)(implicit ec: ExecutionContext)
    extends MetadataLoader {

  override def getVersions(
    organization: String,
    artifactId: String,
    attrs: Map[String, String]
  ): Future[Seq[ArtifactVersion]] = {
    val request = MavenSearchRequest(organization, artifactId, maxRows)
    client.search(request).map {
      case artifacts if artifacts.isEmpty => throw ArtifactNotFoundException(organization, artifactId)
      case artifacts                      => artifacts.map(a => new DefaultArtifactVersion(a.v))
    }
  }

}

object MavenSearchMetadataLoader {

  def apply()(implicit ec: ExecutionContext): MavenSearchMetadataLoader = {
    val maxRows = 10
    new MavenSearchMetadataLoader(maxRows, MavenSearchClient())
  }

  def apply(maxRows: Int)(implicit ec: ExecutionContext): MavenSearchMetadataLoader =
    new MavenSearchMetadataLoader(maxRows, MavenSearchClient())

  def apply(maxRows: Int, client: MavenSearchClient)(implicit ec: ExecutionContext): MavenSearchMetadataLoader =
    new MavenSearchMetadataLoader(maxRows, client)

  def apply(maxRows: Int, client: AsyncHttpClient)(implicit ec: ExecutionContext): MavenSearchMetadataLoader =
    new MavenSearchMetadataLoader(maxRows, MavenSearchClient(client))

}
