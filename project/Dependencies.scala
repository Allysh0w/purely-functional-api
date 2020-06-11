import sbt._

object Dependencies {

  private val pureConfigVersion = "0.12.3"
  private val Http4sVersion = "0.21.3"
  private val CirceVersion = "0.13.0"
  private val Specs2Version = "4.9.3"
  private val LogbackVersion = "1.2.3"
  private val QuillCassandraVersion = "3.5.1"

  val generalDependencies: Seq[ModuleID] = Seq(
    "com.github.pureconfig"   %% "pureconfig"   % pureConfigVersion,
    "org.http4s"      %% "http4s-blaze-server" % Http4sVersion,
    "org.http4s"      %% "http4s-blaze-client" % Http4sVersion,
    "org.http4s"      %% "http4s-circe"        % Http4sVersion,
    "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
    "io.circe"        %% "circe-generic"       % CirceVersion,
    "org.specs2"      %% "specs2-core"         % Specs2Version % "test",
    "ch.qos.logback"  %  "logback-classic"     % LogbackVersion
  )

  val quillDependencies = Seq(
    "io.getquill"     %% "quill-cassandra"     % QuillCassandraVersion
  )

}
