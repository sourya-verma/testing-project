package utils



import models._
import play.api.libs.json.Json


object JsonFormat {


  implicit val studentFormat = Json.format[Student]
  implicit val universityFormat = Json.format[University]
  implicit val studentUniversityFormat = Json.format[StudentUniversity]
  implicit val universityStudentCountFormat = Json.format[UniversityStudentCount]

}


