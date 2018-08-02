package controllers

import javax.inject.{Inject, Singleton}
import java.nio.file.Paths
import play.api.mvc._
import org.mozilla.universalchardet.UniversalDetector

import scala.concurrent.ExecutionContext


@Singleton
class TestController @Inject() (cc:MessagesControllerComponents)
                               (implicit assetsFinder: AssetsFinder)
  extends MessagesAbstractController(cc){

  def index = Action { implicit  request =>
    Ok(views.html.files())
  }


  def upload = Action(parse.multipartFormData) { implicit  request =>
    //Ok(views.html.files())

    request.body.file("picture").map { picture =>

      // only get the last part of the filename
      // otherwise someone can send a path like ../../home/foo/bar.txt to write to other files on the system
      val filename = Paths.get(picture.filename).getFileName

      picture.ref.moveTo(Paths.get(s"/tmp/files/$filename"), replace = true)


      val buf = new Array[Byte](4096)
      val fis = java.nio.file.Files.newInputStream(java.nio.file.Paths.get(s"/tmp/files/$filename"))

      // (1)
      val detector = new UniversalDetector

      // (2)
      var nread = 0
      nread = fis.read(buf)

      while (nread > 0 && !detector.isDone){

      }

      /*
      while ( {
        ((nread = fis.read(buf)) > 0) && !detector.isDone
      }) detector.handleData(buf, 0, nread)
      // (3)
      */
      detector.dataEnd()

      // (4)
      val encoding = detector.getDetectedCharset


      Ok("File uploaded")
    }.getOrElse {
      Ok("Upload Faild")
    }
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
