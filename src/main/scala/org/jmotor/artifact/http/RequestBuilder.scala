package org.jmotor.artifact.http

import org.asynchttpclient.{ BoundRequestBuilder, Realm }

/**
 * Component:
 * Description:
 * Date: 2018/2/8
 *
 * @author AI
 */
object RequestBuilder {

  implicit class BoundRequestBuilderSetting(builder: BoundRequestBuilder) {

    implicit def ensure(realm: Option[Realm]): BoundRequestBuilder = {
      builder.setFollowRedirect(true)
      realm.foreach(r ⇒ builder.setRealm(r))
      builder
    }

  }

}
