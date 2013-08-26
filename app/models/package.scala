package models

import play.api.Play.current
import play.api.Play
import com.mongodb.casbah.commons.Imports._
import com.mongodb.{MongoURI, WriteConcern}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

package object myApp  {

  implicit val wc: WriteConcern = WriteConcern.NORMAL

  def formatTest(date: DateTime, pattern: String = "d MMMM, YYYY") = DateTimeFormat.forPattern(pattern).print(date)

  def stripBody(blog: String) = blog.take(400)


}
