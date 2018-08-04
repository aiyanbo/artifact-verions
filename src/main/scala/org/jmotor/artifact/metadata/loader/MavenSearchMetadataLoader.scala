package org.jmotor.artifact.metadata.loader

import org.apache.maven.artifact.versioning.{ ArtifactVersion, DefaultArtifactVersion }
import org.asynchttpclient.AsyncHttpClient
import org.jmotor.artifact.exception.ArtifactNotFoundException
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
class MavenSearchMetadataLoader(maxRows: Int = 10)(implicit httpClient: AsyncHttpClient, ec: ExecutionContext) extends MetadataLoader {

  private[this] lazy val client = MavenSearchClient(httpClient)

  override def getVersions(organization: String, artifactId: String, attrs: Map[String, String]): Future[Seq[ArtifactVersion]] = {
    val request = MavenSearchRequest(organization, artifactId, maxRows)
    client.search(request).map {
      case artifacts if artifacts.isEmpty ⇒ throw ArtifactNotFoundException(organization, artifactId)
      case artifacts                      ⇒ artifacts.map(a ⇒ new DefaultArtifactVersion(a.v))
    }
  }

}
