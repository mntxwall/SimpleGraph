package controllers

import javax.inject._
import play.api.mvc._
import play.api.Logger

import models.EdgesDao

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents,
                               edgedao: EdgesDao)(implicit assetsFinder: AssetsFinder)extends AbstractController(cc) {

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
    val hello = edgedao.OneRow()

    //Logger.debug("Attempting risky calculation.")
    Logger.debug(s"$hello")

    Ok(views.html.hello(hello))
  }
}
