package org.jmotor.artifact.metadata.loader

import scala.concurrent.ExecutionContext

/**
 * Component:
 * Description:
 * Date: 2018/2/8
 *
 * @author AI
 */
trait TestContext {

  implicit val ec: ExecutionContext = ExecutionContext.Implicits.global

}
