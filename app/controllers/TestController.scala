package controllers

import javax.inject.{Inject, Singleton}
import java.nio.file._
import java.nio.file.Files
import java.nio.file.attribute.PosixFilePermissions

import scala.io.Source
import play.api.mvc._
import play.api.libs.json._
import java.io.FileInputStream

import models.EdgesDao
import play.api.Logger

import scala.concurrent.ExecutionContext


@Singleton
class TestController @Inject() (cc:MessagesControllerComponents,edgeDao: EdgesDao)
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
      //val filename = Paths.get(picture.filename).getFileName

      val filename = picture.filename

      //val newfile = new File(s"/home/cw/$filename")

      val fileWithPath:String = s"/home/cw/$filename"
      //Copy the upload file to /tmp/files directory
      //picture.ref.moveTo(Paths.get(s"/tmp/files/$filename"), replace = true)
      //picture.ref.moveTo(Paths.get(s"/tmp/files/$filename"), replace = true)
      picture.ref.moveTo(Paths.get(s"/home/cw/$filename"),replace = true)
      ///picture.ref.moveTo(newfile, replace = true)


      //change file permissions add read privileges to the file
      Files.setPosixFilePermissions(Paths.get(s"$fileWithPath"),
      PosixFilePermissions.fromString("rw-r--r--"))

      //newfile.set



      edgeDao.importDataFromfiles(fileWithPath)
      //read line from the upload file and insert into database;

/*
      val source = Source.fromFile(fileWithPath)
      for (line <- source.getLines())
        Logger.debug(line)
      source.close()
      */

/*
      val is = new FileInputStream(filePath)
      val channel = is.getChannel
      val buffer = ByteBuffer.allocate(1024)
      channel.read(buffer)

      //System.out.println(new String(buffer.array))
      Logger.debug(new String(buffer.array()))

      channel.close()
      is.close()

   */
/*
      val buf = new Array[Byte](4096)
      val fis = java.nio.file.Files.newInputStream(java.nio.file.Paths.get(s"/tmp/files/$filename"))

      // (1)
      val detector = new UniversalDetector

      // (2)
      var nread = 0
      nread = fis.read(buf)


      while (nread > 0 && !detector.isDone) {
        detector.handleData(buf, 0, nread)
        nread = fis.read(buf)
      }

      detector.dataEnd()

      val encoding: Option[String] = Option(detector.getDetectedCharset)


      detector.reset()
      /*
      while ( {
        ((nread = fis.read(buf)) > 0) && !detector.isDone
      }) detector.handleData(buf, 0, nread)
      // (3)
      */

      // (4)
      //val jsonVal: JsValue = JsString("hello")
      //Logger.debug(encoding.getOrElse("bad"))


      encoding match {
        case Some("UTF-8") =>
          Logger.debug("This is UTF-8 file")
        case None =>
          Logger.debug("Can't find file encoding")
      }
      */
      //Ok(jsonVal)

      val jsonVal: JsValue = Json.parse(s"""
      {
        "name" : "Hello"
      }
      """)
      Ok(jsonVal)


      //Ok(s"File uploaded ${encoding}")

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
