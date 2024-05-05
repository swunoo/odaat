package controllers

import models.Task
import play.api.mvc._
import play.api.libs.json._
import service.TaskService

import java.time.LocalDate
import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TaskController @Inject()(cc: ControllerComponents, taskService: TaskService) extends AbstractController(cc) {

  // Typecast Task to JSON
  implicit val taskFormat = Json.format[Task]

  // Typecast JSON to Task
  implicit val taskReads: Reads[Task] = Json.reads[Task]

  def getAll() = Action.async { request =>
    taskService.getAll map { tasks =>
      Ok(Json.toJson(tasks))
    }
  }

  def get(project: Option[String], date: Option[String]) = Action.async { request =>
    taskService.getByParams(project, date) map { tasks =>
      Ok(Json.toJson(tasks))
    }
  }

  def add() = Action(parse.json).async { request =>
    request.body.validate[Task] match {
      case JsSuccess(task, _) => {
        taskService.add(task).map {
          case 1 => Ok(Json.toJson(task))
          case _ => BadRequest("Invalid Data")
        }
      }
      case JsError(errors) =>
        println(errors)
        Future.successful(BadRequest("Invalid Data"))
    }
  }

  def update(id: Long) = Action(parse.json).async { request =>
    request.body.validate[Task] match {
      case JsSuccess(task, _) => {
        taskService.update(task, id).map {
          case 1 => Ok(Json.toJson(task))
          case _ => BadRequest("Update Failed")
        }
      }
      case JsError(errors) =>
        println(errors)
        Future.successful(BadRequest("Invalid Data"))
    }
  }

  def delete(id: Long) = Action.async { request =>
    taskService.delete(id) map { res =>
      Ok("Deleted")
    }
  }

}
