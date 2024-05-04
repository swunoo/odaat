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
    duration: Double,
    completedAt: LocalDate,
    deadline: LocalDate,
    priority: String,
    description: String
)


// Project entity (Project table in the database)
class ProjectTableDef(tag: Tag) extends Table[Project](tag, "project"){
  def title = column[String]("title", O.PrimaryKey, O.AutoInc)
  def duration = column[Double]("duration")
  def completedAt = column[LocalDate]("completed_at")
  def deadline = column[LocalDate]("deadline")
  def priority = column[String]("priority")
  def description = column[String]("description")

  override def * =
    (title, duration, completedAt, deadline, priority, description) <> (Project.tupled, Project.unapply)
}

// Repository layer for Project
class ProjectRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(
                                 implicit executionContext: ExecutionContext
) extends  HasDatabaseConfigProvider[JdbcProfile] {

  var projectRepo = TableQuery[ProjectTableDef]

  def add(project: Project): Future[String] = {
    dbConfig.db
      .run(projectRepo += project)
      .map(res => "Success")
      .recover {
        case ex: Exception => {
          val err = ex.getMessage
          printf(err)
          err
        }
      }
  }

  def delete(title: String): Future[Int] = {
    dbConfig.db.run(projectRepo.filter(_.title === title).delete)
  }

  def update(project: Project): Future[Int] = {
    dbConfig.db
      .run(projectRepo.filter(_.title === project.title)
            .map(p => (p.duration, p.completedAt, p.deadline, p.priority, p.description))
            .update(project.duration, project.completedAt, project.deadline, project.priority, project.description)
      )
  }

  def getAll: Future[Seq[Project]] = {
    dbConfig.db.run(projectRepo.result)
  }
}