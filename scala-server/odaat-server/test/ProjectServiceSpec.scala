import models.{Project, ProjectRepository}
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import service.ProjectService

import java.time.LocalDate
import scala.concurrent.Future

class ProjectServiceSpec extends PlaySpec with MockitoSugar {

  "ProjectService" should {

    "getAll#return all projects" in {
      val expected = Future.successful(Seq(
        Project("Name",Some(10.0), None, Some(LocalDate.now()), "high", Some("Lorem"))
      ))

      val projRepository = mock[ProjectRepository]
      when(projRepository.getAll).thenReturn(expected)

      val service = new ProjectService(projRepository)

      val actual = service.getAll
      actual mustBe expected
    }

    "getTitles#return all titles" in {
      val expected = Future.successful(Seq(
        "Title 1"
      ))

      val projRepository = mock[ProjectRepository]
      when(projRepository.getTitles).thenReturn(expected)

      val service = new ProjectService(projRepository)

      val actual = service.getTitles
      actual mustBe expected
    }

    "add#return 1" in {
      val expected = Future.successful(1)
      val mockProject = Project("Name",Some(10.0), None, Some(LocalDate.now()), "high", Some("Lorem"))

      val projRepository = mock[ProjectRepository]
      when(projRepository.add(mockProject)).thenReturn(expected)

      val service = new ProjectService(projRepository)

      val actual = service.add(mockProject)
      actual mustBe expected
    }

    "update#return 1" in {
      val expected = Future.successful(1)
      val mockProject = Project("Name",Some(10.0), None, Some(LocalDate.now()), "high", Some("Lorem"))

      val projRepository = mock[ProjectRepository]
      when(projRepository.update(mockProject)).thenReturn(expected)

      val service = new ProjectService(projRepository)

      val actual = service.update(mockProject)
      actual mustBe expected
    }

    "delete#return 1" in {
      val expected = Future.successful(1)

      val projRepository = mock[ProjectRepository]
      when(projRepository.delete("mock title")).thenReturn(expected)

      val service = new ProjectService(projRepository)

      val actual = service.delete("mock title")
      actual mustBe expected
    }

  }
}