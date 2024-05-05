package models

import com.google.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.{Json, Reads}
import slick.jdbc.JdbcProfile

import java.time.LocalDate
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}

// Task model
case class Task (
  var id: Long,
  project: String,
  date: Option[LocalDate],
  duration: Option[Double],
  status: String,
  task: String
)

// Task entity (`task` table)
class TaskTableDef(tag: Tag) extends Table[Task](tag, "task"){
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def project = column[String]("project_title")
  def date = column[Option[LocalDate]]("date")
  def duration = column[Option[Double]]("duration")
  def status = column[String]("status")
  def task = column[String]("task")

  override def * =
    (id, project, date, duration, status, task) <> (Task.tupled, Task.unapply)
}

class TaskRepository @Inject()(
                              protected  val dbConfigProvider: DatabaseConfigProvider)(
                              implicit  executionContext: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile] {

  var taskRepo = TableQuery[TaskTableDef]

  def getAll: Future[Seq[Task]] = {
    dbConfig.db.run(taskRepo.result)
  }

  def get(project: Option[String], date: Option[String]): Future[Seq[Task]] = {

    val filteredQuery = (project, date) match {
      case (Some(project), Some(date)) => taskRepo.filter(row => row.project === project && row.date === LocalDate.parse(date))
      case (Some(project), None)     => taskRepo.filter(_.project === project)
      case (None, Some(date))     => taskRepo.filter(_.date === LocalDate.parse(date))
      case (None, None)         => taskRepo
    }

    dbConfig.db.run(filteredQuery.result)
  }

  def add(task: Task): Future[Long] = {
    dbConfig.db
      .run(taskRepo returning taskRepo.map(_.id) += task)
      .map { id => id }
  }

  def update(task: Task): Future[Int] = {
    dbConfig.db
      .run(taskRepo.filter(_.id === task.id)
            .map(t => (t.project, t.date, t.duration, t.status, t.task))
            .update(task.project, task.date, task.duration, task.status, task.task)
      )
  }

  def updateStatus(id: Long, status: String): Future[Int] = {
    dbConfig.db
      .run(taskRepo.filter(_.id === id)
        .map(t => (t.status))
        .update(status)
      )
  }

  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(taskRepo.filter(_.id === id).delete)
  }

}
