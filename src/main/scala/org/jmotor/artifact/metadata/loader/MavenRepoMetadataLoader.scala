package org.jmotor.artifact.metadata.loader

import java.net.URL
import java.nio.file.Paths

import org.apache.maven.artifact.versioning.{ ArtifactVersion, DefaultArtifactVersion }
import org.asynchttpclient.util.HttpConstants.ResponseStatusCodes
import org.asynchttpclient.{ AsyncHttpClient, Realm }
import org.jmotor.artifact.exception.ArtifactMetadataLoadException
import org.jmotor.artifact.http.RequestBuilder._
import org.jmotor.artifact.metadata.MetadataLoader
import org.jmotor.tools.http.AsyncHttpClientConversions._

import scala.concurrent.{ ExecutionContext, Future }
import scala.xml.XML

/**
 * Component:
 * Description:
 * Date: 2018/2/8
 *
 * @author AI
 */
class MavenRepoMetadataLoader(url: String, realm: Option[Realm])
  (implicit client: AsyncHttpClient, ec: ExecutionContext) extends MetadataLoader {

  private[this] lazy val (protocol, base) = {
    val u = new URL(url)
    u.getProtocol + "://" -> url.replace(s"${u.getProtocol}://", "")
  }

  override def getVersions(organization: String, artifactId: String): Future[Seq[ArtifactVersion]] = {
    client.prepareGet(
      protocol + Paths.get(base, organization.split('.').mkString("/"), artifactId, "maven-metadata.xml").toString).ensure(realm).toFuture.map {
        case r if r.getStatusCode == ResponseStatusCodes.OK_200 ⇒
          val xml = XML.load(r.getResponseBodyAsStream)
          xml \ "versioning" \ "versions" \ "version" map (node ⇒ new DefaultArtifactVersion(node.text))
        case r ⇒ throw ArtifactMetadataLoadException(s"${r.getStatusCode} ${r.getStatusText}: ${r.getUri.toUrl}")
      }
  }

}
