package com.rentobject.authservice.server

import cats.data.Kleisli
import cats.effect.{ConcurrentEffect, ContextShift, ExitCode, Timer}
import com.rentobject.authservice.repository.UserRepository
import com.rentobject.authservice.routes.AuthRoutes
import fs2.Stream
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.{HttpRoutes, Request, Response}
import org.http4s.server.Router
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger
import cats.implicits._
import scala.concurrent.ExecutionContext

object ServerAPI {


  def stream[F[_]: ConcurrentEffect](implicit T: Timer[F], C: ContextShift[F], ec: ExecutionContext): Stream[F, Nothing] = {
    for {
      client <- BlazeClientBuilder[F](ec).stream
      createAccount = UserRepository.impl[F]
      httpApp: Kleisli[F, Request[F], Response[F]] = (

          AuthRoutes.routes[F](createAccount)
        ).orNotFound

      // With Middlewares in place
      finalHttpApp = Logger.httpApp(false, false)(httpApp)

      exitCode <- BlazeServerBuilder[F]
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(finalHttpApp)
        .serve
    } yield exitCode
  }.drain
}



//  def serverStream[F[_]: ConcurrentEffect](routes: HttpRoutes[F])
//                                          (implicit T: Timer[F],
//                                           C: ContextShift[F],
//                                           ec: ExecutionContext): fs2.Stream[F, ExitCode] = {
//
//    val httpRoutes = Router[F](
//      "/" -> routes
//    ).orNotFound
//
//    val httpApp = Logger.httpApp(logHeaders = true, logBody = true)(httpRoutes)
//
//    BlazeServerBuilder[F]
//      .bindHttp(8080, "0.0.0.0")
//      .withHttpApp(httpApp)
//      .serve
//  }





//}
