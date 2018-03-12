import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._

object EchoServer extends App {

  val websocketRoute =
    path("echo") {
      //handleWebSocketMessages(greeter)
    }

  val binding = Http().bindAndHandle(websocketRoute, "localhost", 8080)
  binding.onComplete {
    case Success(binding) ⇒
      val localAddress = binding.localAddress
      println(s"Server is listening on ${localAddress.getHostName}:${localAddress.getPort}")
    case Failure(e) ⇒
      println(s"Binding failed with ${e.getMessage}")
      system.terminate()
  }

}
