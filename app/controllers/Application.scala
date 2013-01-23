package controllers

import play.api._
import play.api.mvc._
import model.Payload
import play.api.data._
import play.api.data.Forms._
import com.codahale.jerkson.Json

object Application extends Controller {

  var lastPayload = ""

  case class GithubRequest(payload:String)

  def payload = Action {
    Ok(lastPayload)
  }

  def welcome = Action {
    Ok(views.html.welcome())
  }

  def log = Action {
    import sys.process._
    Ok("cat /root/compile_async.log".!!)
  }
  def deploy = Action {implicit request  =>

    val form = Form(mapping("payload" -> text)(GithubRequest.apply)(GithubRequest.unapply))

    lastPayload = form.bindFromRequest.get.payload

    val payload = Json.parse[Payload](lastPayload)

    if (payload.ref.contains("master") && payload.repository.url.contains("OneCalendar")) {
      import sys.process._
      try {
        val r: String = "/root/compile_async".!!
        Ok(r).as("plain/text")
      } catch {
        case e:Exception => InternalServerError(e.toString)
      }
    } else {
      PreconditionFailed
    }
  }
}