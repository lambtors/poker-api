package com.lambtors.poker_api.infrastructure.controller

import scala.io.StdIn

import play.api.libs.json.JsPath

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport

object HelloWorldServer extends App with PlayJsonSupport {
  implicit val system           = ActorSystem("poker-api-system")
  implicit val materializer     = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  case class PokerPostRequest(amountOfPlayers: Int)
  implicit val pokerPostRequestMarshaller = (JsPath \ "amount_of_players").read[Int].map(PokerPostRequest.apply)

  val routes =
    path("poker") {
      post {
        entity(as[PokerPostRequest]) { pokerPostRequest =>
          println(pokerPostRequest)
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
        }
      }
    }

  val bindingFuture = Http().bindAndHandle(routes, "localhost", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
}
