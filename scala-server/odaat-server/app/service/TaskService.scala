package service

import com.google.inject.Inject
import models.{Task, TaskRepository}

import java.time.LocalDate
import scala.concurrent.Future
class TaskService @Inject()(taskRepo: TaskRepository) {

  def getAll: Future[Seq[Task]] = {
    taskRepo.getAll
  }

  def getByParams(project: Option[String], date: Option[String]): Future[Seq[Task]] = {
    taskRepo.get(project, date)
  }

  def add(task: Task): Future[Long] = {
    taskRepo.add(task)
  }

  def update(task: Task, id: Long): Future[Int] = {
    task.id = id
    taskRepo.update(task)
  }

  def updateStatus(id: Long, status: String): Future[Int] = {
    taskRepo.updateStatus(id, status)
  }

  def delete(id: Long): Future[Int] = {
    taskRepo.delete(id)
  }

}
