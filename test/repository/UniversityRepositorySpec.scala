package repository

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Helpers._
import play.api.test.{Injecting, WithApplication}

class UniversityRepositorySpec extends PlaySpec with GuiceOneAppPerTest {

  import models._


  "University repository" should {

    "get all rows" in new WithUniversityRepository() {
      val result = await(universityRepo.getAll())
      result.length mustBe 3
      result.head.name mustBe "HCU"
    }

    "get single rows" in new WithUniversityRepository() {
      val result = await(universityRepo.getById(1))
      result.isDefined mustBe true
      result.get.name mustBe "HCU"
    }

    "insert a row" in new WithUniversityRepository() {
      val student = University("SIT", "Shivpuri",Some(2))
      val studentId = await(universityRepo.insert(student))
      studentId mustBe 4
    }

    "insert multiple rows" in new WithUniversityRepository() {
      val list = List(University("kU", "Kanpur", Some(3)))
      val result = universityRepo.insertAll(list)
      await(result) mustBe Seq(4)
    }

    "update a row" in new WithUniversityRepository() {
      val result = await(universityRepo.update(University("HCU j", "Hyderabad", Some(1))))
      result mustBe 1
    }

    "delete a row" in new WithUniversityRepository() {
      val result = await(universityRepo.delete(1))
      result mustBe 1
    }
  }


}

trait WithUniversityRepository extends WithApplication with Injecting {

  val universityRepo = inject[UniversityRepository]
}
