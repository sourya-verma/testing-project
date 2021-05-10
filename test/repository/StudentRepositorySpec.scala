package repository

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Helpers._
import play.api.test.{Injecting, WithApplication}

class StudentRepositorySpec extends PlaySpec with GuiceOneAppPerTest {

  import models._

  "Student repository" should {

    "get all rows" in new WithStudentRepository() {
      val result = await(studentRepo.getAll())
      result.length mustBe 3
      result.head.name mustBe "Bob"
    }

    "get single rows" in new WithStudentRepository() {
      val result = await(studentRepo.getById(1))
      result.isDefined mustBe true
      result.get.name mustBe "Bob"
    }

    "insert a row" in new WithStudentRepository() {
      val student = Student("raj", "raj@gmail.com",4,Some(2))
      val studentId = await(studentRepo.insert(student))
      studentId mustBe 4
    }

    "insert multiple rows" in new WithStudentRepository() {
      val list = List(Student("John Snow", "snow@gmail.com", 4, Some(3)))
      val result = studentRepo.insertAll(list)
      await(result) mustBe Seq(4)
    }

    "update a row" in new WithStudentRepository() {
      val result = await(studentRepo.update(Student("Bob", "bobby@gmail.com",2, Some(1))))
      result mustBe 1
    }

    "delete a row" in new WithStudentRepository() {
      val result = await(studentRepo.delete(1))
      result mustBe 1
    }

    "get Student Name and University Name" in new WithStudentRepository (){
      val result = await(studentRepo.getStudentNameWithUniversityName())
      result mustBe List(("Bob","PUNE University"), ("Rob","BHU"), ("Joe","HCU"))
    }

    "get University Name and No of student" in new WithStudentRepository (){
      val result = await(studentRepo.getUniversityNameAndNoOfStudents())
      result mustBe List(("PUNE University",1), ("HCU",1), ("BHU",1))
    }



  }


}

trait WithStudentRepository extends WithApplication with Injecting {

  val studentRepo = inject[StudentRepository]
}
