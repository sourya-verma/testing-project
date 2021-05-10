package repository

import javax.inject.{Inject, Singleton}
import models.University
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

@Singleton()
class UniversityRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends UniversityTable with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  def insert(university: University): Future[Int] =
    db.run {
      universityTableQueryInc += university
    }

  def insertAll(universitys: List[University]): Future[Seq[Int]] =
    db.run {
      universityTableQueryInc ++= universitys
    }

  def update(university: University): Future[Int] =
    db.run {
      universityTableQuery.filter(_.id === university.id).update(university)
    }

  def delete(id: Int): Future[Int] =
    db.run {
      universityTableQuery.filter(_.id === id).delete
    }

  def getAll(): Future[List[University]] =
    db.run {
      universityTableQuery.to[List].result
    }

  def getById(universityId: Int): Future[Option[University]] =
    db.run {
      universityTableQuery.filter(_.id === universityId).result.headOption
    }

  def ddl = universityTableQuery.schema

}

private[repository] trait UniversityTable{
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  lazy protected val universityTableQuery = TableQuery[UniversityTable]
  lazy protected val universityTableQueryInc = universityTableQuery returning universityTableQuery.map(_.id)

  private[UniversityTable] class UniversityTable(tag: Tag) extends Table[University](tag, "university") {
    val id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    val name = column[String]("name")
    val location = column[String]("location")

    def * = (name,location, id.?) <> (University.tupled, University.unapply)
  }

}

