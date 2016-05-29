package actors

import akka.actor.{Actor, Props}

/**
  * Created by roger19890107 on 5/29/16.
  */
object HelloActor {
  def props = Props[HelloActor]

  case class SayHello(name: String)
}

class HelloActor extends Actor {
  import HelloActor._

  def receive = {
    case SayHello(name: String) =>
      sender ! "hello, " + name
  }
}
