import canoe.api._
import canoe.syntax._
import cats.effect.{ExitCode, IO, IOApp}

object BotApp extends IOApp {

  val token: String = System.getenv("TELEGRAM_TOKEN")
  val url: String = s"https://${System.getenv("NGROK_ID")}.ngrok.io"

  def run(args: List[String]): IO[ExitCode] =
    fs2.Stream
      .resource(TelegramClient.global[IO](token))
      .flatMap { implicit client =>
        fs2.Stream.resource(Bot.hook[IO](url)).flatMap(_.follow(echos))
      }
      .compile
      .drain
      .as(ExitCode.Success)

  val activities = List("1", "2", "3")
  def echos[F[_]: TelegramClient]: Scenario[F, Unit] =
    for {
      msg <- Scenario.expect(any)
      _   <- Scenario.eval(msg.chat.send(content = Activities("Comer", "Dormir", "Leer").toString))
    } yield ()

}
