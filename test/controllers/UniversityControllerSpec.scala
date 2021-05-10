package controllers

import models.{Student, University}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{Injecting, WithApplication, _}
import repository.{StudentRepository, UniversityRepository}
import utils.JsonFormat._

import scala.concurrent.{ExecutionContext, Future}


class UniversityControllerSpec extends PlaySpec with MockitoSugar with GuiceOneAppPerTest {

  implicit val mockedRepo: UniversityRepository = mock[UniversityRepository]


  "UniversityController " should {

    "create a university" in new WithUniversityApplication() {
      val university = University("SIT", "Shivpuri", Some(1))
      when(mockedRepo.insert(university)) thenReturn Future.successful(1)
      val result = universityController.create().apply(FakeRequest().withBody(Json.toJson(university)))
      val resultAsString = contentAsString(result)
      //resultAsString mustBe """{"status":"success","data":{"id":1},"msg":"university has been created successfully."}"""
      resultAsString mustBe """{"id":1}"""
    }

    "update a university" in new WithUniversityApplication() {
      val updatedUniversity = University("SIT","Shivpuri MP",Some(1))
      when(mockedRepo.update(updatedUniversity)) thenReturn Future.successful(1)
      val result = universityController.update().apply(FakeRequest().withBody(Json.toJson(updatedUniversity)))
      val resultAsString = contentAsString(result)
      //resultAsString mustBe """{"status":"success","data":"{}","msg":"Employee has been updated successfully."}"""
      resultAsString mustBe """"{}""""
    }

    "get a university" in new WithUniversityApplication() {
      val university = University("SIT", "Shivpuri MP", Some(1))
      when(mockedRepo.getById(1)) thenReturn Future.successful(Some(university))
      val result = universityController.get(1).apply(FakeRequest())
      val resultAsString = contentAsString(result)
      //resultAsString mustBe """{"status":"success","data":{"name":"jaz","email":"jaz@bar.com","companyName":"ABC solution","position":"Senior Consultant","id":1},"msg":"Getting Employee successfully."}"""
      resultAsString mustBe """{"name":"SIT","location":"Shivpuri MP","id":1}"""
    }

    "delete a university" in new WithUniversityApplication() {
      when(mockedRepo.delete(1)) thenReturn Future.successful(1)
      val result = universityController.delete(1).apply(FakeRequest())
      val resultAsString = contentAsString(result)
      //resultAsString mustBe """{"status":"success","data":"{}","msg":"Employee has been deleted successfully."}"""
      resultAsString mustBe """"{}""""

    }

    "get all list" in new WithUniversityApplication() {
      val university = University("hcu", "Hyderabad",Some(1))
      when(mockedRepo.getAll()) thenReturn Future.successful(List(university))
      val result = universityController.list().apply(FakeRequest())
      val resultAsString = contentAsString(result)
      resultAsString mustBe """[{"name":"hcu","location":"Hyderabad","id":1}]"""
    }



  }

}

class WithUniversityApplication(implicit mockedRepo: UniversityRepository) extends WithApplication with Injecting {

  implicit val ec = inject[ExecutionContext]

  val messagesApi = inject[MessagesApi]

  val universityController: UniversityController =
    new UniversityController(
      stubControllerComponents(),
      mockedRepo
    )

}
