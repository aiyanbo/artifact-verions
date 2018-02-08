package org.jmotor.artifact.metadata.loader

import org.apache.maven.artifact.versioning.{ ArtifactVersion, DefaultArtifactVersion }
import org.asynchttpclient.AsyncHttpClient
import org.jmotor.artifact.metadata.MetadataLoader
import org.jmotor.tools.MavenSearchClient
import org.jmotor.tools.dto.MavenSearchRequest

import scala.concurrent.{ ExecutionContext, Future }
/**
 * Component:
 * Description:
 * Date: 2018/2/8
 *
 * @author AI
 */
class MavenSearchMetadataLoader(implicit httpClient: AsyncHttpClient, ec: ExecutionContext) extends MetadataLoader {

  private[this] lazy final val MAX_ROWS = 10
  private[this] lazy val client = MavenSearchClient(httpClient)

  override def getVersions(organization: String, artifactId: String): Future[Seq[ArtifactVersion]] = {
    val request = MavenSearchRequest(organization, artifactId, MAX_ROWS)
    client.search(request).map(_.map { artifact ⇒
      new DefaultArtifactVersion(artifact.v)
    })
  }

}
