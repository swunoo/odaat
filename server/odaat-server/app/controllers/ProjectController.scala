package controllers

import models.{Project, low}
import play.api.mvc._
import play.api.libs.json._
import service.ProjectService

import java.time.LocalDate
import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global

class ProjectController @Inject()(cc: ControllerComponents, projectService: ProjectService) extends AbstractController(cc){

  implicit  val projectFormat = Json.format[Project]

  // Typecast JSON to Project
  implicit val projectReads: Reads[Project] = Json.reads[Project]

  def getMock = Action {
    val project = new Project(
      "Title", 10.5, LocalDate.now(), LocalDate.now().plusDays(5), "low", "lorem")
    Ok(Json.toJson(project))
  }

  def getAll() = Action.async { implicit request: Request[AnyContent] =>
      projectService.getAll map { projects =>
        Ok(Json.toJson(projects))
      }
  }

  def add() = Action(parse.json) { request =>
    request.body.validate[Project] match {
        case JsSuccess(project, _) => {
          projectService.add(project)
          Ok(Json.toJson(project))
        }
        case JsError(errors) =>
          BadRequest("Invalid data")
      }
  }

  def update(title: String) = Action(parse.json) { request =>
    request.body.validate[Project] match {
      case JsSuccess(project, _) => {
        project.title = title
        projectService.update(project)
        Ok(Json.toJson(project))
      }
      case JsError(errors) =>
        BadRequest("Invalid data")
    }
  }

  def delete(title: String) = Action.async { implicit request: Request[AnyContent] =>
    projectService.delete(title) map { res =>
      Ok("Deleted")
    }
  }

}
