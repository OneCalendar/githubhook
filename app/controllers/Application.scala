package controllers

import play.api.mvc._
import model.Payload

object Application extends Controller {

  var lastPayload = ""

  def show = Action {
    Ok(lastPayload)
  }
  def deploy = Action {request =>

    val payload:Payload = payload2json(request)

    if (payload.ref.contains("master") && payload.repository.url.contains("OneCalendar")) {
      import sys.process._
      try {
        val r: String = "/root/compile".!!
        Ok(r).as("plain/text")
      } catch {
        case e:Exception => InternalServerError(e.toString)
      }
    } else {
      PreconditionFailed
    }
  }

  def payload2json(request: Request[AnyContent]) : Payload = {
    import com.codahale.jerkson.Json
    import model.Payload

    lastPayload = request.body.asJson.getOrElse("").toString

    Json.parse[Payload](lastPayload)
  }
}