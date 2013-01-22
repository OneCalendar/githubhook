package controllers

import play.api._
import play.api.mvc._
import model.Payload

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def deploy(path:String) = Action {request =>

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

    val string: String = request.body.asJson.getOrElse("").toString
    Json.parse[Payload](string)
  }
}