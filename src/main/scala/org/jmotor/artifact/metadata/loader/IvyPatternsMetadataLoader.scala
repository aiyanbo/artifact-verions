package org.jmotor.artifact.metadata.loader

import okhttp3.Request
import org.apache.ivy.core.IvyPatternHelper
import org.apache.maven.artifact.versioning.{ArtifactVersion, DefaultArtifactVersion}
import org.jmotor.artifact.exception.{ArtifactMetadataLoadException, ArtifactNotFoundException}
import org.jmotor.artifact.http.OkHttpClients
import org.jmotor.artifact.metadata.MetadataLoader
import org.jmotor.tools.Conversions.OkHttpCallWrapper

import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future}

/**
 * Component: Description: Date: 2018/2/8
 *
 * @author
 * AI
 */
class IvyPatternsMetadataLoader(patterns: Seq[String],
                                credentials: Option[String])(implicit ec: ExecutionContext
                               ) extends MetadataLoader {

  private[this] lazy val regex = """<a(?:onclick="navi\(event\)")? href=":?([^/]*)/"(?: rel="nofollow")?>\1/</a>""".r
  private[this] val client = OkHttpClients.create(credentials)

  override def getVersions(
                            organization: String,
                            artifactId: String,
                            attrs: Map[String, String]
                          ): Future[Seq[ArtifactVersion]] = {
    val urls = patterns.map(pattern => getRevisionUrl(pattern, organization, artifactId, attrs)).collect {
      case Some(url) => url
    }
    val futures = urls.map { url =>
      val request = new Request.Builder().url(url).build()
      client.newCall(request).toFuture
    }
    Future.sequence(futures).map { responses =>
      if (responses.exists(_.isSuccessful)) {
        responses.collect {
          case r if r.isSuccessful =>
            regex.findAllMatchIn(r.body().string()).map(_.group(1)).map { version =>
              new DefaultArtifactVersion(version)
            }
        }.flatten
      } else {
        if (responses.forall(_.code() == 404)) {
          throw ArtifactNotFoundException(organization, artifactId)
        } else {
          val errors = responses.map { response =>
            s"${response.code()} : ${response.request().url()}"
          } mkString "\n"
          throw ArtifactMetadataLoadException(errors)
        }
      }
    }
  }

  private def getRevisionUrl(
                              pattern: String,
                              organization: String,
                              artifactId: String,
                              attrs: Map[String, String]
                            ): Option[String] = {
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
