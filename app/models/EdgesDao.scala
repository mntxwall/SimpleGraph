package models

import anorm.SqlParser._
import anorm._
import javax.inject.Inject
import play.api.db.DBApi

case class Edge(id: Int, head: String, tail: String, weight: Int)

@javax.inject.Singleton
class EdgesDao @Inject()(dbApi: DBApi){

  private val db = dbApi.database("default")

  val rowParser: RowParser[Edge] = {
    get[Int]("id") ~
      get[String]("head_vertex") ~
      get[String]("tail_vertex") ~
      get[Int]("weight") map {
      case id~head~tail~weight => Edge(id, head, tail, weight)
    }
  }

  val rowParser1: RowParser[Edge] = Macro.parser[Edge]("id", "head_vertex", "tail_vertex", "weight")

  def OneRow(): List[Edge] = {

    db.withConnection { implicit c =>
      SQL("select * from edges").as(rowParser1.*)
    }
  }


  def EdgesNodes(): Seq[String] = {

    val headVertex: Seq[String] = db.withConnection{ implicit c =>
      SQL("SELECT DISTINCT(head_vertex) from edges").as(SqlParser.scalar[String].*)

    }

    val tailVertex: Seq[String] = db.withConnection{ implicit c =>
      SQL("SELECT DISTINCT(tail_vertex) from edges").as(SqlParser.scalar[String].*)

    }

    (headVertex ++ tailVertex).toSet.toSeq

  }
}
