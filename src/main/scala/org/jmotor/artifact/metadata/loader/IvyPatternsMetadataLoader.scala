package org.jmotor.artifact.metadata.loader

import java.util

import org.apache.ivy.core.IvyPatternHelper
import org.apache.maven.artifact.versioning.{ ArtifactVersion, DefaultArtifactVersion }
import org.asynchttpclient.util.HttpConstants.ResponseStatusCodes
import org.asynchttpclient.{ AsyncHttpClient, Realm }
import org.jmotor.artifact.http.RequestBuilder._
import org.jmotor.artifact.metadata.MetadataLoader
import org.jmotor.tools.http.AsyncHttpClientConversions._

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

  override def getVersions(organization: String, artifactId: String): Future[Seq[ArtifactVersion]] = {
    val urls = patterns.map(pattern ⇒ getRevisionUrl(pattern, organization, artifactId)).collect {
      case Some(url) ⇒ url
    }
    val futures = urls.map { url ⇒
      client.prepareGet(url).ensure(realm).toFuture
    }
    Future.sequence(futures).map { responses ⇒
      responses.collect {
        case r if r.getStatusCode == ResponseStatusCodes.OK_200 ⇒
          regex.findAllMatchIn(r.getResponseBody).map(_.group(1)).map { version ⇒
            new DefaultArtifactVersion(version)
          }
      } flatten
    }
  }

  private def getRevisionUrl(pattern: String, organization: String, artifactId: String): Option[String] = {
    val tokens = new util.HashMap[String, String]()
    tokens.put(IvyPatternHelper.ORGANISATION_KEY2, organization)
    tokens.put(IvyPatternHelper.MODULE_KEY, artifactId)
    val substituted = IvyPatternHelper.substituteTokens(pattern, tokens)
    if (IvyPatternHelper.getFirstToken(substituted) == IvyPatternHelper.REVISION_KEY) {
      Some(IvyPatternHelper.getTokenRoot(substituted))
    } else {
      None
    }
  }

}
