package models

import com.novus.salat._
import com.mongodb.casbah.MongoConnection
import play.api.Play.current
import play.api.Play
import com.mongodb.casbah.commons.Imports._
import com.mongodb.{MongoURI, WriteConcern}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

package object myApp  {

  implicit val wc: WriteConcern = WriteConcern.NORMAL

  implicit val ctx = {
    val c = new Context() {
      val name ="play-context"
    }
    c.registerClassLoader(Play.classloader)
    c
  }

//  object DB {
//    val mongoUri  = System.getenv("mongoUri")
//    val connection = MongoConnection(mongoUri)("Lindseys-Blog")
//    connection.authenticate("admin","admin")
//    def apply(collection: String) = connection(collection)
//  }
  def formatTest(date: DateTime, pattern: String = "d MMMM, YYYY") = DateTimeFormat.forPattern(pattern).print(date)

  def stripBody(blog: String) = blog.take(400)

  object Status extends Enumeration {
    type Status = Value
    val Live, Pending, Deleted = Value
  }
}
