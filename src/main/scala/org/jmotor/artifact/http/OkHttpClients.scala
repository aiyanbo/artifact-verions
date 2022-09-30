package org.jmotor.artifact.http

import okhttp3.{Authenticator, Dispatcher, OkHttpClient, Request, Response, Route}

/**
 * Component: Description: Date: 2018/2/8
 *
 * @author
 * AI
 */
object OkHttpClients {

  def create(credentials: Option[String]): OkHttpClient = {
    val dispatcher = new Dispatcher()
    val concurrent = 5000
    dispatcher.setMaxRequests(concurrent)
    dispatcher.setMaxRequestsPerHost(concurrent)
    new OkHttpClient.Builder().dispatcher(dispatcher).authenticator((_: Route, response: Response) => {
      credentials match {
        case None => response.request()
        case Some(v) => response.request().newBuilder().header("Authorization", v).build()
      }
    }).build()
  }

}
