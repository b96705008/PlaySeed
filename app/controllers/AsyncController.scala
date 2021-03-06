package controllers

import akka.actor.ActorSystem
import akka.actor._
import akka.pattern.ask
import javax.inject._

import actors.HelloActor
import actors.HelloActor.SayHello
import actors.ConfiguredActor._
import akka.routing.RoundRobinPool
import play.api.mvc._

import scala.concurrent.{Await, ExecutionContext, Future, Promise}
import scala.concurrent.duration._
import akka.util.Timeout

/**
 * This controller creates an `Action` that demonstrates how to write
 * simple asynchronous code in a controller. It uses a timer to
 * asynchronously delay sending a response for 1 second.
 *
 * @param actorSystem We need the `ActorSystem`'s `Scheduler` to
 * run code after a delay.
 * @param exec We need an `ExecutionContext` to execute our
 * asynchronous code.
 */
@Singleton
class AsyncController @Inject() (actorSystem: ActorSystem,
                                 @Named("configured-actor") configuredActor: ActorRef,
                                 @Named("parent-actor") parentActor: ActorRef)
                                (implicit exec: ExecutionContext) extends Controller {
  /**
   * Create an Action that returns a plain text message after a delay
   * of 1 second.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/message`.
   */
  def message = Action.async {
    getFutureMessage(1.second).map { msg => Ok(msg) }
  }

  private def getFutureMessage(delayTime: FiniteDuration): Future[String] = {
    val promise: Promise[String] = Promise[String]()
    actorSystem.scheduler.scheduleOnce(delayTime) { promise.success("Hi!") }
    promise.future
  }

  /**
    * Actor test
    */
  val helloActor = actorSystem.actorOf(HelloActor.props, "hello-actor")
  implicit val timeout = Timeout(5 seconds)

  def sayHello(name: String) = Action.async {
    (helloActor ? SayHello(name)).mapTo[String].map { message =>
      Ok(message)
    }
  }

  def getConfig = Action.async {
    (configuredActor ? GetConfig).mapTo[String].map { message =>
      Ok(message)
    }
  }

  import actors.ParentActor._
  import actors.ConfiguredChildActor._
  val rogerActor = Await.result[ActorRef](
    (parentActor ? GetChild("Roger")).mapTo[ActorRef], timeout.duration)

  def getChildConfig = Action.async {
    (rogerActor ? GetChildConfig).mapTo[String].map { message =>
      Ok(message)
    }
  }

  val router = actorSystem.actorOf(RoundRobinPool(5).props(Props[HelloActor]), "router")
  def helloRouter(name: String) = Action.async {
    (router ? SayHello(name)).mapTo[String].map { message =>
      Ok(message)
    }
  }
}
