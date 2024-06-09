import controllers.{HomeController, ProjectController}
import models.Project
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar

import scala.concurrent.Future
import org.scalatestplus.play._
import play.api.libs.json.Json
import play.api.mvc._
import play.api.test.Helpers._
import play.api.test._
import service.ProjectService

import java.time.LocalDate

class ProjectControllerSpec extends PlaySpec with MockitoSugar {


 "MyController" should {

   "return a JSON response with all objects" in {
     val mockProjectService = mock[ProjectService]
     val mockDate = LocalDate.now()
     val controller = new ProjectController(Helpers.stubControllerComponents(), mockProjectService)
     val expectedResult = Seq(
       Project("Name",Some(10.0), None, Some(mockDate), "high", Some("Lorem"))
     )

     when(mockProjectService.getAll).thenReturn(Future.successful(expectedResult))

     val result: Future[Result] = controller.getAll().apply(FakeRequest())

     // Assert the response
     val jsonResponse = contentAsJson(result)
     jsonResponse mustBe Json.arr(
       Json.obj(
         "title" -> "Name",
         "duration" -> 10,
         "completedAt" -> null,
         "due" -> mockDate.toString,
         "priority" -> "high",
         "description" -> "Lorem"
       )
     )
   }
 }
  
}