package controllers

import models.{Student, University}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.i18n.{DefaultLangs, MessagesApi}
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{Injecting, WithApplication, _}
import repository.StudentRepository
import utils.JsonFormat._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Success


class StudentControllerSpec extends PlaySpec with MockitoSugar with GuiceOneAppPerTest {

  implicit val mockedRepo: StudentRepository = mock[StudentRepository]

  "StudentController " should {

    "create a student" in new WithStudentApplication() {
      val student = Student("abhi", "abhi@gmail.com", 2,Some(1))
      when(mockedRepo.insert(student)) thenReturn Future.successful(1)
      val result = studentController.create().apply(FakeRequest().withBody(Json.toJson(student)))
      val resultAsString = contentAsString(result)
      resultAsString mustBe """{"id":1}"""
    }

    "update a student" in new WithStudentApplication() {
      val updatedStudent = Student("abhi","abhik@gmail.com",2,Some(1))
      when(mockedRepo.update(updatedStudent)) thenReturn Future.successful(1)
      val result = studentController.update().apply(FakeRequest().withBody(Json.toJson(updatedStudent)))
      val resultAsString = contentAsString(result)
      resultAsString mustBe """"{}""""
    }

    "get a student" in new WithStudentApplication() {
      val student = Student("abhi", "abhi@outlook.com", 3, Some(1))
      when(mockedRepo.getById(1)) thenReturn Future.successful(Some(student))
      val result = studentController.get(1).apply(FakeRequest())
      val resultAsString = contentAsString(result)
      resultAsString mustBe """{"name":"abhi","email":"abhi@outlook.com","UID":3,"id":1}"""
    }

    "delete a student" in new WithStudentApplication() {
      when(mockedRepo.delete(1)) thenReturn Future.successful(1)
      val result = studentController.delete(1).apply(FakeRequest())
      val resultAsString = contentAsString(result)
      resultAsString mustBe """"{}""""
    }

    "get all list" in new WithStudentApplication() {
      val student = Student("abhi", "abhi@outlook.com", 3, Some(1))
      when(mockedRepo.getAll()) thenReturn Future.successful(List(student))
      val result = studentController.list().apply(FakeRequest())
      val resultAsString = contentAsString(result)
      resultAsString mustBe """[{"name":"abhi","email":"abhi@outlook.com","UID":3,"id":1}]"""
    }


    "get student name and university name" in new WithStudentApplication() {
      val list = List(("John Snow","HCU"),("Bob","BHU"))
      when(mockedRepo.getStudentNameWithUniversityName()) thenReturn Future.successful( list )
      val result = studentController.getStudentNameWithUniversityName().apply(FakeRequest())
      val resultAsString = contentAsString(result)
      resultAsString mustBe """[{"name":"John Snow","universityName":"HCU"},{"name":"Bob","universityName":"BHU"}]"""
    }

    "get University name and no of student" in new WithStudentApplication() {
      val list = List(("HCU",2))
      when(mockedRepo.getUniversityNameAndNoOfStudents()) thenReturn Future.successful( list)
      val result = studentController.getUniversityNameAndNoOfStudents().apply(FakeRequest())
      val resultAsString = contentAsString(result)
      resultAsString mustBe """[{"name":"HCU","count":2}]"""
    }



  }

}

class WithStudentApplication(implicit mockedRepo: StudentRepository) extends WithApplication with Injecting {

  implicit val ec = inject[ExecutionContext]

  val messagesApi = inject[MessagesApi]

  val studentController: StudentController =
    new StudentController(
      stubControllerComponents(),
      mockedRepo
    )


}
