package com.rentobject.authservice

import java.util.concurrent.Executors

import cats.effect.{ExitCode, IO, IOApp}
import com.rentobject.authservice.server.ServerAPI

import scala.concurrent.ExecutionContext
import cats.implicits._

object Main extends IOApp {

  implicit val ec: ExecutionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(4))

  def run(args: List[String]) =
    ServerAPI.stream[IO].compile.drain.as(ExitCode.Success)

}
