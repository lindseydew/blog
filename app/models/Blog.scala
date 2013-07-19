package models

import com.novus.salat.dao._
import com.novus.salat._
import org.bson.types.ObjectId
import com.mongodb.casbah.MongoConnection
import models._
import com.mongodb.casbah.Imports._
import myApp._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import Status._
case class BlogDB(_id: ObjectId = new ObjectId,
                 blog: Blog,
                 data: MetaData)

case class Blog (
                 title: String,
                 body: String,
                 slug: String,
                 createdOn: DateTime = DateTime.now()
                 )
case class MetaData (
                      status: String,
                      bodyOrig: String
                      )
 // def apply(title: String, body: String, slug: String) = new Blog(title, body, slug)

object BlogDAO extends SalatDAO[BlogDB, ObjectId](collection=DB("blogs"))  {
  def list() = find(MongoDBObject.empty).sort(orderBy = MongoDBObject("_id" -> -1)).toList.map(_.blog)

  def bySlug(slug: String) = findOne(MongoDBObject("blog.slug"->slug))

  def updateBlog(b: Blog) = {
    update(MongoDBObject("blog.slug"->b.slug),
      MongoDBObject(
        "$set" -> MongoDBObject(
          ("blog" -> grater[Blog].asDBObject(b))
        )
      ),
      upsert=true
    )
  }


}

