package controllers

import models.Project
import service.ProjectService

import play.api.mvc._
import play.api.libs.json._
import java.time.LocalDate
import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ProjectController @Inject()(cc: ControllerComponents, projectService: ProjectService) extends AbstractController(cc){

  // Typecase Project to JSON
  implicit val projectFormat = Json.format[Project]

  // Typecast JSON to Project
  implicit val projectReads: Reads[Project] = Json.reads[Project]

  def getAll() = Action.async { request =>
      projectService.getAll map { projects =>
        Ok(Json.toJson(projects))
      }
  }

  def getTitles() = Action.async { request =>
    projectService.getTitles map { titles =>
      Ok(Json.toJson(titles))
    }
  }

  def add() = Action(parse.json).async { request =>
    request.body.validate[Project] match {
        case JsSuccess(project, _) => {
          projectService.add(project).map {
            case 1 => Ok(Json.toJson(project))
            case _ => BadRequest("Invalid Data")
          }
        }
        case JsError(errors) =>
          println(errors)
          Future.successful(BadRequest("Invalid Data"))
      }
  }

  def update() = Action(parse.json).async { request =>
    request.body.validate[Project] match {
      case JsSuccess(project, _) => {
        projectService.update(project).map {
          case 1 => Ok(Json.toJson(project))
          case _ => BadRequest("Update Failed")
        }
      }
      case JsError(errors) =>
        println(errors)
        Future.successful(BadRequest("Invalid Data"))
    }
  }

  def delete(title: String) = Action.async { request =>
    projectService.delete(title) map {
      case 1 => Ok("Deleted")
      case _ => BadRequest("Delete Failed")
    }
  }

}
