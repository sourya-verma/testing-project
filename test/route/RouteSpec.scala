package route

import models.{Student, University}
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._
import utils.JsonFormat._

class RouteSpec extends PlaySpec with GuiceOneAppPerSuite {

  "Routes" should {

    "get student list" in new WithApplication {
      val Some(result) = route(app, FakeRequest(GET, "/student/list"))
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """[{"name":"Bob","email":"bob@xyz.com","UID":3,"id":1},{"name":"Rob","email":"rob@abc.com","UID":2,"id":2},{"name":"Joe","email":"joe@xyz.com","UID":1,"id":3}]"""
    }

    "create  student" in new WithApplication() {
      val student = Student("john snow","john@gmail.com",2,Some(4))
      val Some(result) = route(app, FakeRequest(POST, "/student/create").withBody(Json.toJson(student)))
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """{"id":4}"""
    }

    "get student" in new WithApplication() {
      val Some(result) = route(app, FakeRequest(GET, "/student/get/1"))
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """{"name":"Bob","email":"bob@xyz.com","UID":3,"id":1}"""
    }

    "Get invalid student" in new WithApplication() {
      val Some(result) = route(app, FakeRequest(GET, "/student/get/156"))
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """"{}""""
    }

    "update student" in new WithApplication() {
      val student = Student("BOB", "bob@xyz.com", 1, Some(1))
      val Some(result) = route(app, FakeRequest(POST, "/student/update").withBody(Json.toJson(student)))
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """"{}""""
    }

    "delete student" in new WithApplication() {
      val Some(result) = route(app, FakeRequest(GET, "/student/delete/1"))
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """"{}""""
    }

    "get university list" in new WithApplication {
      val Some(result) = route(app, FakeRequest(GET, "/university/list"))
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """[{"name":"HCU","location":"Hyderabad","id":1},{"name":"BHU","location":"Banaras","id":2},{"name":"PUNE University","location":"Pune","id":3}]"""
    }

    "create university" in new WithApplication() {
      val university = University("sIT","shivpuri",Some(4))
      val Some(result) = route(app, FakeRequest(POST, "/university/create").withBody(Json.toJson(university)))
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """{"id":4}"""
    }

    "get university" in new WithApplication() {
      val Some(result) = route(app, FakeRequest(GET, "/university/get/1"))
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """{"name":"HCU","location":"Hyderabad","id":1}"""
    }

    "Get invalid university" in new WithApplication() {
      val Some(result) = route(app, FakeRequest(GET, "/university/get/156"))
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """"{}""""
    }

    "update university" in new WithApplication() {
      val university = University("HCU", "Gachibowli",Some(1))
      val Some(result) = route(app, FakeRequest(POST, "/university/update").withBody(Json.toJson(university)))
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """"{}""""
    }

    "delete university" in new WithApplication() {
      val Some(result) = route(app, FakeRequest(GET, "/university/delete/1"))
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """"{}""""
    }


  }

}
