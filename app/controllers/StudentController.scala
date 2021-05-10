package controllers

import com.google.inject.Inject
import models.{Student, StudentUniversity, UniversityStudentCount}
import org.slf4j.LoggerFactory
import play.api.Logger
import play.api.i18n._
import play.api.libs.json.Json._
import play.api.libs.json.{JsError, JsObject, JsValue, Json}
import play.api.mvc._
import repository.{StudentRepository, UniversityRepository}
import utils.Constants
import utils.JsonFormat._

import scala.concurrent.{ExecutionContext, Future}

class StudentController @Inject()(
                                    cc: ControllerComponents,
                                    studentRepository: StudentRepository
                                  )(implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  import Constants._

  def list: Action[AnyContent] =
    Action.async {
      studentRepository.getAll().map { res =>
        Ok(Json.toJson(res)).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }

  def create: Action[JsValue] = {
    println("Requesting for Adding the Record......")
    Action.async(parse.json) {
      request =>
        println("request is : "+request.body)
      request.body.validate[Student].fold(
        error => Future.successful(BadRequest(JsError.toJson(error)).withHeaders("Access-Control-Allow-Origin" -> "*")),
          { student =>
            studentRepository.insert(student).map { createdStudentId =>
              Ok(Json.toJson(Map("id" -> createdStudentId))).withHeaders("Access-Control-Allow-Origin" -> "*")
            }

      })

    }
  }


  def delete(studentId: Int): Action[AnyContent] =
    Action.async { _ =>
      studentRepository.delete(studentId).map { _ =>
        Ok(Json.toJson("{}")).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }

  def get(studentId: Int): Action[AnyContent] =
    Action.async { _ =>
      studentRepository.getById(studentId).map { studentOpt =>
        studentOpt.fold(Ok(Json.toJson("{}")))(student => Ok(
          Json.toJson(student))).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }

  def update: Action[JsValue] = {
    println("Requesting for Updating the Record......")
    Action.async(parse.json) { request =>
      println("request is : "+request.body)
      request.body.validate[Student].fold(
        error =>
          Future.successful(BadRequest(JsError.toJson(error)).withHeaders("Access-Control-Allow-Origin" -> "*")),
        { student =>
        studentRepository.update(student).map { _ => Ok(Json.toJson("{}")).withHeaders("Access-Control-Allow-Origin" -> "*") }
        }
      )
    }
  }

  def getStudentNameWithUniversityName(): Action[AnyContent] =
    Action.async { _ =>
      studentRepository.getStudentNameWithUniversityName().map {
        res =>
          val ans = for(i<-res)yield (StudentUniversity(i._1,i._2))
          Ok(Json.toJson(ans))
      }
    }

  def getUniversityNameAndNoOfStudents(): Action[AnyContent] =
    Action.async { _ =>
      studentRepository.getUniversityNameAndNoOfStudents().map {
        res =>
          val ans = for(i<-res)yield (UniversityStudentCount(i._1,i._2))
          Ok(Json.toJson(ans))
      }
    }






}

