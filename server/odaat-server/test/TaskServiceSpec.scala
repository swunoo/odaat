import models.{Project, ProjectRepository, Task, TaskRepository}
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import service.{ProjectService, TaskService}

import java.time.LocalDate
import scala.concurrent.Future

class TaskServiceSpec extends PlaySpec with MockitoSugar {

  "TaskService" should {

    "getAll#return all tasks" in {
      val expected = Future.successful(Seq(
        Task(1, "Project Title", Some(LocalDate.now()), None, "done", "Lorem Ipsum")
      ))

      val taskRepository = mock[TaskRepository]
      when(taskRepository.getAll).thenReturn(expected)

      val service = new TaskService(taskRepository)

      val actual = service.getAll
      actual mustBe expected
    }

    "getByParams#return tasks according to params" in {
      val expected = Future.successful(Seq(
        Task(1, "Project Title", Some(LocalDate.now()), None, "done", "Lorem Ipsum")
      ))

      val taskRepository = mock[TaskRepository]
      when(taskRepository.get(None, None)).thenReturn(expected)

      val service = new TaskService(taskRepository)

      val actual = service.getByParams(None, None)
      actual mustBe expected
    }

    "add#add tasks" in {
      val expected: Future[Long] = Future.successful(1)
      val mockTask =  Task(5, "Project Title", Some(LocalDate.now()), None, "done", "Lorem Ipsum")

      val taskRepository = mock[TaskRepository]
      when(taskRepository.add(mockTask)).thenReturn(expected)

      val service = new TaskService(taskRepository)

      service.add(mockTask) mustBe expected
    }

    "updateStatus#update task status" in {
      val expected = Future.successful(1)

      val taskRepository = mock[TaskRepository]
      when(taskRepository.updateStatus(1L, "done")).thenReturn(expected)

      val service = new TaskService(taskRepository)

      service.updateStatus(1L, "done") mustBe expected
    }

    "update#update tasks accordingly" in {
      val expected = Future.successful(1)
      val taskForService =  Task(5, "Project Title", Some(LocalDate.now()), None, "done", "Lorem Ipsum")

      val taskForRepo = Task(10, "Project Title", Some(LocalDate.now()), None, "done", "Lorem Ipsum")
      val taskRepository = mock[TaskRepository]
      when(taskRepository.update(taskForRepo)).thenReturn(expected)

      val service = new TaskService(taskRepository)

      service.update(taskForService, 10) mustBe expected
    }

    "delete#delete tasks" in {
      val expected = Future.successful(1)

      val taskRepository = mock[TaskRepository]
      when(taskRepository.delete(1)).thenReturn(expected)

      val service = new TaskService(taskRepository)

      service.delete(1) mustBe expected
    }

  }
}