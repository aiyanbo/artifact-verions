package org.jmotor.artifact.metadata.loader

import okhttp3.{Authenticator, Credentials, Dispatcher, OkHttpClient, Request, Response, Route}
import org.apache.maven.artifact.versioning.{ArtifactVersion, DefaultArtifactVersion}
import org.jmotor.artifact.exception.{ArtifactMetadataLoadException, ArtifactNotFoundException}
import org.jmotor.artifact.http.OkHttpClients
import org.jmotor.artifact.metadata.MetadataLoader
import org.jmotor.tools.Conversions._

import java.net.URL
import java.nio.file.Paths
import scala.concurrent.{ExecutionContext, Future}
import scala.xml.XML

/**
 * Component: Description: Date: 2018/2/8
 *
 * @author
 * AI
 */
class MavenRepoMetadataLoader(url: String, credentials: Option[String])(implicit ec: ExecutionContext)
  extends MetadataLoader {

  private[this] val client: OkHttpClient = OkHttpClients.create(credentials)

  private[this] lazy val (protocol, base) = {
    val u = new URL(url)
    u.getProtocol + "://" -> url.replace(s"${u.getProtocol}://", "")
  }

  override def getVersions(
                            organization: String,
                            artifactId: String,
                            attrs: Map[String, String]
                          ): Future[Seq[ArtifactVersion]] = {
    val location = protocol + Paths.get(base, organization.split('.').mkString("/"), artifactId, "maven-metadata.xml").toString
    val request = new Request.Builder().url(location).build()
    client.newCall(request)
      .toFuture
      .map {
        case r if r.isSuccessful =>
          val xml = XML.load(r.body().byteStream())
          xml \ "versioning" \ "versions" \ "version" map (node => new DefaultArtifactVersion(node.text))
        case r if r.code() == 404 => throw ArtifactNotFoundException(organization, artifactId)
        case r => throw ArtifactMetadataLoadException(s"${r.code()} ${r.body().string()}: ${location}")
      }
  }

}
