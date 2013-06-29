package models

import com.novus.salat.dao._
import com.novus.salat._
import org.bson.types.ObjectId
import com.mongodb.casbah.MongoConnection
import models._
import com.mongodb.casbah.Imports._
import myApp._

case class Blog(_id: ObjectId = new ObjectId,
                title: String,
                body: String,
                slug: String,
                createdOn: Long)

case class Tags(tags: List[String])



//class BlogDAO[ObjectType <: AnyRef, ID <: Any](collectionName: String)(implicit mot: Manifest[models.Blog], mid: Manifest[org.bson.types.ObjectId], ctx: Context) extends SalatDAO[Blog, ObjectId](collection=DB("blog"))(mot, mid, ctx)

object BlogDAO extends SalatDAO[Blog, ObjectId](collection=DB("blog"))  {
  def list() = find(MongoDBObject.empty).toList


}
