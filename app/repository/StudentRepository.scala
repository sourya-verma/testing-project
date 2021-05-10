package repository

import javax.inject.{Inject, Singleton}
import models.Student
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

@Singleton()
class StudentRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends StudentTable with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  def insert(student: Student): Future[Int] =
    db.run {
      studentTableQueryInc += student
    }

  def insertAll(students: List[Student]): Future[Seq[Int]] =
    db.run {
      studentTableQueryInc ++= students
    }

  def update(student: Student): Future[Int] =
    db.run {
      studentTableQuery.filter(_.id === student.id).update(student)
    }

  def delete(id: Int): Future[Int] =
    db.run {
      studentTableQuery.filter(_.id === id).delete
    }

  def getAll(): Future[List[Student]] =
    db.run {
      studentTableQuery.to[List].result
    }

  def getById(studentId: Int): Future[Option[Student]] =
    db.run {
      studentTableQuery.filter(_.id === studentId).result.headOption
    }


  def getStudentNameWithUniversityName():Future[List[(String,String)]] = {
    db.run
    {
      (for {
        (s, u) <- studentTableQuery join universityTableQuery on (_.UId === _.id)
      } yield (s.name, u.name)).to[List].result
    }
  }

  def getUniversityNameAndNoOfStudents():Future[List[(String,Int)]] = {

    val qu = (for {
      (s, u) <- studentTableQuery join universityTableQuery on (_.UId === _.id)
    } yield (s, u)).groupBy(_._2.name)
    val qu2 = qu.map {
      case (uid, css) => (uid, css.map(_._1.id).length)
    }
    db.run(qu2.to[List].result)

  }


  def ddl = studentTableQuery.schema

}

private[repository] trait StudentTable extends UniversityTable {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  lazy protected val studentTableQuery = TableQuery[StudentTable]
  lazy protected val studentTableQueryInc = studentTableQuery returning studentTableQuery.map(_.id)

  private[StudentTable] class StudentTable(tag: Tag) extends Table[Student](tag, "student") {
    val id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    val name = column[String]("name")
    val email = column[String]("email")
    val UId = column[Int]("university_id")
    def universityID = foreignKey("_fk", UId, universityTableQuery)(_.id)

    def * = (name,email,UId, id.?) <> (Student.tupled, Student.unapply)

  }

}

