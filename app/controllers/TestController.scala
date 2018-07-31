package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc._

import scala.concurrent.ExecutionContext


@Singleton
class TestController @Inject() (cc:MessagesControllerComponents)
                               (implicit assetsFinder: AssetsFinder)
  extends MessagesAbstractController(cc){

  def index = Action {
    Ok(views.html.files())
  }

  def upload = Action(parse.multipartFormData) { implicit  request =>
    //Ok(views.html.files())
    Ok("File uploaded")
  }


 /* def hello = Action(parse.multipartFormData) { request =>
    request.body.file("picture").map { picture =>

      // only get the last part of the filename
      // otherwise someone can send a path like ../../home/foo/bar.txt to write to other files on the system
      //val filename = Paths.get(picture.filename).getFileName

      //picture.ref.moveTo(Paths.get(s"/tmp/picture/$filename"), replace = true)
      Ok("File uploaded")
    }.getOrElse {
      Redirect(routes.TestController.index).flashing(
        "error" -> "Missing file")
    }
  }*/


}
