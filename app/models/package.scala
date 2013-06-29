package models

import com.novus.salat._
import com.mongodb.casbah.MongoConnection
import play.api.Play.current
import play.api.Play
import com.mongodb.casbah.commons.Imports._
import com.mongodb.{MongoURI, WriteConcern}

package object myApp  {

  implicit val wc: WriteConcern = WriteConcern.NORMAL

  implicit val ctx = {
    val c = new Context() {
      val name ="play-context"
    }
    c.registerClassLoader(Play.classloader)
    c
  }

  object DB {
    val mongoUri = "localhost:27000"
    val connection = MongoConnection(mongoUri)("blog")
    def apply(collection: String) = connection(collection)
  }
}
