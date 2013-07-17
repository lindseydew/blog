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


case class BlogDB(_id: ObjectId = new ObjectId,
                 blog: Blog)

case class Blog (
                 title: String,
                 body: String,
                 slug: String,
                 createdOn: DateTime = DateTime.now()
                 )  {
 // def apply(title: String, body: String, slug: String) = new Blog(title, body, slug)

}

case class Tags(tags: List[String])



//class BlogDAO[ObjectType <: AnyRef, ID <: Any](collectionName: String)(implicit mot: Manifest[models.Blog], mid: Manifest[org.bson.types.ObjectId], ctx: Context) extends SalatDAO[Blog, ObjectId](collection=DB("blog"))(mot, mid, ctx)

object BlogDAO extends SalatDAO[BlogDB, ObjectId](collection=DB("blogs"))  {
  def list() = find(MongoDBObject.empty).toList.map(_.blog)
  //todo - exception handling
  def bySlug(slug: String) = findOne(MongoDBObject("blog.slug"->slug)).get.blog

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

