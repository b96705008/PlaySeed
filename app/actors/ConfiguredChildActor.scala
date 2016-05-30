package actors

import akka.actor._
import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import play.api.Configuration

/**
  * Created by roger19890107 on 5/30/16.
  */
object ConfiguredChildActor {
  case object GetChildConfig

  trait Factory {
    def apply(key: String): Actor
  }
}

class ConfiguredChildActor @Inject() (configuration: Configuration,
                                     @Assisted key: String) extends Actor {
  import ConfiguredChildActor._

  val config = configuration.getString("my.config").getOrElse("none - child")

  def receive = {
    case GetChildConfig =>
      sender() ! config
  }
}
