package filters

import javax.inject._

import akka.stream.Materializer
import com.google.inject.Inject
import play.api.Logger

import scala.concurrent.{ExecutionContext, Future}
import play.api.mvc._

/**
  * Created by roger19890107 on 5/29/16.
  */
class LoggingFilter @Inject() (implicit val mat: Materializer, ec: ExecutionContext) extends Filter {
  override def apply(nextFilter: (RequestHeader) => Future[Result])
                    (requestHeader: RequestHeader): Future[Result] = {
    val startTime = System.currentTimeMillis()

    nextFilter(requestHeader).map { result =>
      val endTime = System.currentTimeMillis()
      val requestTime = endTime - startTime

      Logger.info(s"${requestHeader.method} ${requestHeader.uri} took ${requestTime} and returned ${result.header.status}")

      result.withHeaders("Request-Time" -> requestTime.toString)
    }
  }
}
