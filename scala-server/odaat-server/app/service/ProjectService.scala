package service

import com.google.inject.Inject
import models.{Project, ProjectRepository}

import scala.concurrent.Future

class ProjectService @Inject()(projectRepo: ProjectRepository){

  def getAll: Future[Seq[Project]] = {
    projectRepo.getAll
  }

  def getTitles: Future[Seq[String]] = {
    projectRepo.getTitles
  }

  def add(project: Project): Future[Int] = {
    projectRepo.add(project)
  }

  def update(project: Project): Future[Int] = {
    projectRepo.update(project)
  }

  def delete(title: String): Future[Int] = {
    projectRepo.delete(title)
  }
}
