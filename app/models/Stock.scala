package models

/**
  * Created by roger19890107 on 5/21/16.
  */
case class Stock(symbol: String, price: Double)

object Stock {

  import play.api.libs.json._

  implicit object StockFormat extends Format[Stock] {

    // JSON to Stock
    def reads(json: JsValue): JsResult[Stock] = {
      val symbol = (json \ "symbol").as[String]
      val price = (json \ "price").as[Double]
      JsSuccess(Stock(symbol, price))
    }

    // Stock to JSON
    def writes(s: Stock): JsValue = {
      val stockAsList = Seq("symbol" -> JsString(s.symbol),
                            "price" -> JsNumber(s.price))
      JsObject(stockAsList)
    }
  }
}
