package com.lambtors.poker_api.infrastructure.controller

import scala.io.StdIn

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.stream.ActorMaterializer
import spray.json._
import DefaultJsonProtocol._

object HelloWorldServer extends App {
  implicit val system           = ActorSystem("poker-api-system")
  implicit val materializer     = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  case class PokerPostRequest(amountOfPlayers: Int)
  implicit val pokerPostRequestMarshaller = jsonFormat1(PokerPostRequest)

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
