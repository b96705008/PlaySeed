package actors

import akka.actor._
import javax.inject._
import play.api.libs.concurrent.InjectedActorSupport

/**
  * Created by roger19890107 on 5/30/16.
  */
object ParentActor {
  case class GetChild(key: String)
}

class ParentActor @Inject() (childFactory: ConfiguredChildActor.Factory)
  extends Actor with InjectedActorSupport {

  import ParentActor._

  def receive = {
    case GetChild(key: String) =>
      val child: ActorRef = injectedChild(childFactory(key), key)
      sender() ! child
  }
}
