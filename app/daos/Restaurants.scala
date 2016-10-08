package daos

import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import play.api.Play
import slick.lifted.TableQuery
import models.RestaurantTableDef
import scala.concurrent.Future
import models.CompleteRestaurant
import slick.driver.PostgresDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global



object Restaurants {
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  val restaurants = TableQuery[RestaurantTableDef]
  
  def list:Future[Seq[CompleteRestaurant]] = {
    dbConfig.db.run(restaurants.result)
  }
  def getById(id:Long):Future[Option[CompleteRestaurant]] = {
    dbConfig.db.run(restaurants.filter(_.id === id).result.headOption)
  }
  def save(restaurant:CompleteRestaurant):Future[String] = {
    dbConfig.db.run(restaurants += restaurant).map(res => "Restaurant saved").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }
  def update(restaurant:CompleteRestaurant):Future[Int] = {
    dbConfig.db.run(restaurants.filter(_.id === restaurant.id).update(restaurant))
  }
   
  def delete(id:Long):Future[Int] = {
    dbConfig.db.run(restaurants.filter(_.id === id).delete)
  }
}