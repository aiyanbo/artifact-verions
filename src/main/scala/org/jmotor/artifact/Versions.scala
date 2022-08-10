package org.jmotor.artifact

import java.util.regex.Pattern

import org.apache.maven.artifact.versioning.{ArtifactVersion, DefaultArtifactVersion}

/**
 * Component: Description: Date: 2018/2/8
 *
 * @author
 *   AI
 */
object Versions {

  final lazy val UNRELEASED: Seq[String] = Seq("pr", "m", "beta", "rc", "alpha", "snapshot", "snap")

  private[this] final lazy val jrePattern = s"jre\\d+".r.pattern

  private[this] final lazy val UnreleasedPatterns: Seq[Pattern] =
    Versions.UNRELEASED.map(q => s"$q[_-]?\\d+.*".r.pattern)

  def isReleaseVersion(version: ArtifactVersion): Boolean =
    Option(version.getQualifier) match {
      case None => true
      case Some(qualifier) =>
        val q = qualifier.toLowerCase
        !(Versions.UNRELEASED.contains(q) || UnreleasedPatterns.exists(_.matcher(q).matches()))
    }

  def latestRelease(versions: Seq[ArtifactVersion]): ArtifactVersion =
    versions.collect {
      case av if isReleaseVersion(av) =>
        Option(av.getQualifier).fold(av) {
          case q if isJreQualifier(q) => new DefaultArtifactVersion(av.toString.replace(q, ""))
          case _                      => av
        }
    }.max

  def isJreQualifier(qualifier: String): Boolean =
    jrePattern.matcher(qualifier).matches()

}
