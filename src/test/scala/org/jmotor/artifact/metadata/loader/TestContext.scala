package org.jmotor.artifact.metadata.loader

import org.asynchttpclient.AsyncHttpClient

import scala.concurrent.ExecutionContext

/**
 * Component:
 * Description:
 * Date: 2018/2/8
 *
 * @author AI
 */
trait TestContext {

  implicit val client: AsyncHttpClient = {
    import org.asynchttpclient.Dsl._
    asyncHttpClient()
  }

  implicit val ec: ExecutionContext = ExecutionContext.Implicits.global

}
