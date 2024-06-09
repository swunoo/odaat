package models

import com.google.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.{Json, Reads}
import slick.jdbc.JdbcProfile

import java.time.LocalDate
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}

// Priority enum (currently not in use)
sealed trait ProjectPriority
case object low extends  ProjectPriority
case object medium extends ProjectPriority
case object high extends ProjectPriority

// Project model
case class Project(
    var title: String,
    duration: Option[Double],
    completedAt: Option[LocalDate],
    due: Option[LocalDate],
    priority: String,
    description: Option[String]
)


// Project entity (`project` table)
class ProjectTableDef(tag: Tag) extends Table[Project](tag, "project"){
  def title = column[String]("title", O.PrimaryKey)
  def duration = column[Option[Double]]("duration")
  def completedAt = column[Option[LocalDate]]("completed_at")
  def due = column[Option[LocalDate]]("due")
  def priority = column[String]("priority")
  def description = column[Option[String]]("description")

  override def * =
    (title, duration, completedAt, due, priority, description) <> (Project.tupled, Project.unapply)
}

// Repository layer for Project
class ProjectRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(
                                 implicit executionContext: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile] {

  var projectRepo = TableQuery[ProjectTableDef]

  def getAll: Future[Seq[Project]] = {
    dbConfig.db.run(projectRepo.result)
  }

  def getTitles: Future[Seq[String]] = {
    dbConfig.db.run(projectRepo.result).map(_.map(_.title))
  }

  def add(project: Project): Future[Int] = {
    dbConfig.db
      .run(projectRepo += project)
      .map(res => 1)
      .recover {
        case ex: Exception => {
          ex.printStackTrace()
          0
        }
      }
  }

  def update(project: Project): Future[Int] = {
    dbConfig.db
      .run(projectRepo.filter(_.title === project.title)
            .map(p => (p.duration, p.completedAt, p.due, p.priority, p.description))
            .update(project.duration, project.completedAt, project.due, project.priority, project.description)
      )
  }

  def delete(title: String): Future[Int] = {
    dbConfig.db.run(projectRepo.filter(_.title === title).delete)
  }
}