package com.rentobject.authservice.settings

import pureconfig.ConfigReader.Result
import pureconfig._
import pureconfig.generic.auto._

trait Settings {


  case class authService(authService: authServiceConfig)
  case class authServiceConfig(httpPort: Int, httpHost: String)

  val applicationConfig: Result[authService] = ConfigSource.resources("applicationAuthService.conf").load[authService]

  val serverConf: authServiceConfig = applicationConfig match {
    case Right(config) => config.authService
    case Left(err) => throw new Exception("Failed to load config: " + err)
  }

}
object Settings extends Settings
