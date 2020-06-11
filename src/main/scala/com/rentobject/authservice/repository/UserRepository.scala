package com.rentobject.authservice.repository

import java.time.Instant
import java.util.UUID
import java.util.concurrent.Executors

import cats.Applicative
import cats.implicits._
import com.rentobject.authservice.models.Contracts.{User, UserId}
import io.getquill.{CassandraSyncContext, SnakeCase}

import scala.concurrent.ExecutionContext


trait UserRepository[F[_]] {
   def addUser(user: User): F[UserId]

}

object UserRepository {
  implicit def apply[F[_]](implicit ev: UserRepository[F]): UserRepository[F] = ev

  val ctx = new CassandraSyncContext(SnakeCase, "ctx")

  import ctx._
  implicit val ec: ExecutionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(4))
  def impl[F[_]: Applicative]: UserRepository[F] = new UserRepository[F] {

     def addUser(user: User): F[UserId] = {

      val users = quote {
        querySchema[User]("users")
      }

      val usr = user.copy(created_at = Some(Instant.now), user_id = Some(UUID.randomUUID))

      val query = quote {
        users.insert(lift(usr))
      }

       ctx.run(query)
       UserId(usr.user_id.get.toString).pure[F]

    }

  }
}

