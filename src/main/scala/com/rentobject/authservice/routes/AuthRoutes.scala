package com.rentobject.authservice.routes

import cats.effect.Sync
import com.rentobject.authservice.models.Contracts.User
import com.rentobject.authservice.repository.UserRepository
import io.circe.generic.auto._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.Http4sDsl
import org.http4s.{HttpRoutes, Response}

object AuthRoutes {

  def routes[F[_]: Sync](Handler: UserRepository[F]): HttpRoutes[F] = {

    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case req @ POST -> Root / "test" =>
         val a: F[Response[F]] = req.decode[User]{ x => Ok(Handler.addUser(x))}
          a
    }
  }

}
