package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import models.Stock

/**
  * Created by roger19890107 on 5/21/16.
  */
@Singleton
class StockController @Inject() extends Controller {

  def getStock = Action {
    val stock = Stock("GOOG", 650.0)
    Ok(Json.toJson(stock))
  }

  def saveStock = Action { request =>
    val json = request.body.asJson.get
    val stock = json.as[Stock]
    println(stock)
    Ok
  }

}
