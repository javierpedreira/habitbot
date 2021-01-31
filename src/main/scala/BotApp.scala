import canoe.api._
import canoe.syntax._
import cats.effect.{ExitCode, IO, IOApp}

object BotApp extends IOApp {

  val token: String = System.getenv("TELEGRAM_TOKEN")
  val url: String = "https://4f1fe551d6d1.ngrok.io"

  def run(args: List[String]): IO[ExitCode] =
    fs2.Stream
      .resource(TelegramClient.global[IO](token))
      .flatMap { implicit client =>
        fs2.Stream.resource(Bot.hook[IO](url)).flatMap(_.follow(echos))
      }
      .compile
      .drain
      .as(ExitCode.Success)


  def echos[F[_]: TelegramClient]: Scenario[F, Unit] =
    for {
      // Si se pasa el command(podemos escribir respuestas a comandos)
      // msg <- Scenario.expect(command("callback"))
      msg <- Scenario.expect(any)
      _   <- Scenario.eval(msg.chat.send(content = "PATATASSSS!!!!"))
    } yield ()

}
