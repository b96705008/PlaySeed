package actors

import akka.actor._
import javax.inject._
import play.api.Configuration

/**
  * Created by roger19890107 on 5/29/16.
  */
object ConfiguredActor {
  case object GetConfig
}

class ConfiguredActor @Inject() (configuration: Configuration) extends Actor {
  import ConfiguredActor._

  val config = configuration.getString("my.config").getOrElse("none")

  def receive = {
    case GetConfig =>
      sender() ! config
  }
}
