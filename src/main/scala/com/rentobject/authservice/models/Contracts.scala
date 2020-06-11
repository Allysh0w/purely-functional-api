package com.rentobject.authservice.models


import java.time.Instant
import java.util.UUID

import cats.effect.IO
import com.datastax.driver.core.LocalDate
import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, HCursor}
import io.getquill.context.cassandra.Udt
import org.http4s.EntityDecoder
import org.http4s.circe._

import scala.util.Try

object Contracts {

  case class Address(street: String,
                         city: String,
                         state: String,
                         neighborhood: String,
                         zip_code: Int,
                         number: String,
                         reference_point: String) extends Udt


  case class User(user_id: Option[UUID],
                  first_name: String,
                  last_name: String,
                  nickname: String,
                  email: String,
                  cpf: String,
                  phone_number: List[String],
                  birth_date: LocalDate,
                  user_password: String,
                  created_at: Option[Instant],
                  enabled: Boolean,
                  profile_image_url: String,
                  address: List[Address])

  object Address {

  implicit val addressDecoder = new Decoder[Address] {
    override def apply(c: HCursor): Result[Address] =
      for {
        street <- c.get[String]("street")
        city <- c.get[String]("city")
        state <- c.get[String]("state")
        neighborhood <- c.get[String]("neighborhood")
        zip_code <- c.get[Int]("zip_code")
        number <- c.get[String]("number")
        reference_point <- c.get[String]("reference_point")
      } yield Address(
        street,
        city,
        state,
        neighborhood,
        zip_code,
        number,
        reference_point)
  }
    implicit val aDecoder: EntityDecoder[IO, Address] = jsonOf[IO, Address]

  }

  object User {
    implicit val en = Encoder.encodeString.contramap[LocalDate](_.toString)
    implicit val b = Decoder.decodeString.emapTry{ st =>
      val r = st.split("-")
      Try(LocalDate.fromYearMonthDay(r(0).toInt, r(1).toInt, r(2).toInt))
    }
    implicit val userDecoder: Decoder[User] = new Decoder[User] {
      override def apply(c: HCursor): Decoder.Result[User] =
        for {
          user_id <- c.get[Option[UUID]]("user_id")
          //user_id <- c.getOrElse[UUID]("user_id")(UUID.randomUUID())
          first_name <- c.get[String]("first_name")
          last_name <- c.get[String]("last_name")
          nickname <- c.get[String]("nickname")
          email <- c.get[String]("email")
          cpf <- c.get[String]("cpf")
          phone_number <- c.get[List[String]]("phone_number")
          birth_date <- c.get[LocalDate]("birth_date")(b)
          user_password <- c.get[String]("user_password")
          created_at <- c.get[Option[Instant]]("created_at")
          enabled <- c.get[Boolean]("enabled")
          profile_image_url <- c.get[String]("profile_image_url")
          address <- c.get[List[Address]]("address")
        } yield User(user_id,
          first_name,
          last_name,
          nickname,
          email,
          cpf,
          phone_number,
          birth_date,
          user_password,
          created_at,
          enabled,
          profile_image_url,
          address)
    }

    implicit val personEntityDecoder: EntityDecoder[IO, User] = jsonOf[IO, User]
  }

  case class UserId(user_id: String)

  case class LoginByEmail(email: String, password: String)


}
