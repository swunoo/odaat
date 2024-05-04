package service

import com.google.inject.Inject
import models.{Project, ProjectRepository}

import scala.concurrent.Future

class ProjectService @Inject()(projectRepo: ProjectRepository){
  def add(project: Project): Future[String] = {
    projectRepo.add(project)
  }

  def delete(title: String): Future[Int] = {
    projectRepo.delete(title)
  }

  def update(project: Project): Future[Int] = {
    projectRepo.update(project)
  }

  def getAll: Future[Seq[Project]] = {
    projectRepo.getAll
  }
}
