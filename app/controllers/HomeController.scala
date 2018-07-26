package controllers

import javax.inject._
import play.api.mvc._
import play.api.Logger
import play.api.libs.json._
import models.{Edge, EdgesDao}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents,
                               edgeDao: EdgesDao)(implicit assetsFinder: AssetsFinder)extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action {
    Ok(views.html.index())
  }

  def hello() = Action{
    val hello = edgeDao.OneRow()

    val hello1 = edgeDao.EdgesNodes()

    val hello2 = edgeDao.NodesWithEdges()

    //Logger.debug("Attempting risky calculation.")
    Logger.debug(s"$hello2")

    //json auto mapping
    implicit val residentWrites = Json.writes[Edge]
    
    //Ok(views.html.hello(hello))
    val json = Json.toJson(hello)
    Ok(json)
  }
}
