package org.jmotor.artifact.metadata.loader

import org.apache.ivy.core.IvyPatternHelper
import org.apache.maven.artifact.versioning.{ ArtifactVersion, DefaultArtifactVersion }
import org.asynchttpclient.util.HttpConstants.ResponseStatusCodes
import org.asynchttpclient.{ AsyncHttpClient, Realm }
import org.jmotor.artifact.exception.{ ArtifactMetadataLoadException, ArtifactNotFoundException }
import org.jmotor.artifact.http.RequestBuilder._
import org.jmotor.artifact.metadata.MetadataLoader
import org.jmotor.tools.http.AsyncHttpClientConversions._

import scala.collection.JavaConverters._
import scala.concurrent.{ ExecutionContext, Future }

/**
 * Component:
 * Description:
 * Date: 2018/2/8
 *
 * @author AI
 */
class IvyPatternsMetadataLoader(patterns: Seq[String], realm: Option[Realm])
  (implicit client: AsyncHttpClient, ec: ExecutionContext) extends MetadataLoader {

  private[this] lazy val regex = """<a(?:onclick="navi\(event\)")? href=":?([^/]*)/"(?: rel="nofollow")?>\1/</a>""".r

  override def getVersions(organization: String, artifactId: String, attrs: Map[String, String]): Future[Seq[ArtifactVersion]] = {
    val urls = patterns.map(pattern ⇒ getRevisionUrl(pattern, organization, artifactId, attrs)).collect {
      case Some(url) ⇒ url
    }
    val futures = urls.map { url ⇒
      client.prepareGet(url).ensure(realm).toFuture
    }
    Future.sequence(futures).map { responses ⇒
      if (responses.exists(_.getStatusCode == ResponseStatusCodes.OK_200)) {
        responses.collect {
          case r if r.getStatusCode == ResponseStatusCodes.OK_200 ⇒
            regex.findAllMatchIn(r.getResponseBody).map(_.group(1)).map { version ⇒
              new DefaultArtifactVersion(version)
            }
        }.flatten
      } else {
        if (responses.forall(_.getStatusCode == 404)) {
          throw ArtifactNotFoundException(organization, artifactId)
        } else {
          val errors = responses.map { response ⇒
            s"${response.getStatusCode} ${response.getStatusText}: ${response.getUri.toUrl}"
          } mkString "\n"
          throw ArtifactMetadataLoadException(errors)
        }
      }
    }
  }

  private def getRevisionUrl(pattern: String, organization: String, artifactId: String, attrs: Map[String, String]): Option[String] = {
    val tokens = attrs +
      (IvyPatternHelper.MODULE_KEY -> artifactId) +
      (IvyPatternHelper.ORGANISATION_KEY -> organization) +
      (IvyPatternHelper.ORGANISATION_KEY2 -> organization)
    val substituted = IvyPatternHelper.substituteTokens(pattern, tokens.asJava)
    if (IvyPatternHelper.getFirstToken(substituted) == IvyPatternHelper.REVISION_KEY) {
      Some(IvyPatternHelper.getTokenRoot(substituted))
    } else {
      None
    }
  }

}
