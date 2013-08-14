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
import scala.io.Source

import laika.api.Transform
import laika.parse.markdown.Markdown
import java.io.{IOException, FileNotFoundException, File}

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

object BlogDAO  {
//  def test = {
////    val tmp = for (file <- new File("app/blogs").listFiles()) {
////      val contents = Source.fromFile(file).getLines().toList.head
////      val blog =  Blog("test", contents,"test")
////      println(blog)
////    }
////    println(tmp)
//
//    val test: List[Blog] = new File("app/blogs").listFiles().toList.map(f =>
//      Blog("test", Source.fromFile(f).getLines().toList.head, "test")
//    ).toList
////    val test1 = scala.io.Source.fromFile("app/blogs/bacon.md")
//  }
//  protected def processFile(file: File): Blog = {
//    val contents = Source.fromFile(file).getLines().toList.head
//
//    Blog("test", contents,"test")
//  }
//  def list() ={
//    new File("app/blogs").listFiles().toList.map(f =>
//      Blog("test", Source.fromFile(f).getLines().toList.head, "test")
//    ).toList
//  } //find(MongoDBObject.empty).sort(orderBy = MongoDBObject("_id" -> -1)).toList.map(_.blog)
//loop through all the files in the directory
  def list(): List[Blog] =   {
//    val blogs = for(file <- new File("app/blogs"))

//    val blogs: List[Blog] = for(file <- new File("app/blogs").listFiles().toList) {
//      if (validFormat(file))
//    } yield file
//    val fileContents: List[String] = new File("app/blogs").listFiles().map(f =>
//                                         io.Source.fromFile(f).mkString
//                                     ).toList
    val files =  new File("app/blogs").listFiles().toList
    val blogs = files.flatMap(parseBlog(_))
    blogs
  }
   //todo - exception handling
   protected def parseBlog(f: File): Option[Blog] = {
//      val reg = """([0-9A-Z_a-z-]+)(?=\.)""".r
//      val slug = reg.findFirstIn(f.getPath()).get
     def upperCase(word: String): String = {
       word.head.toUpper + word.tail
     }
     def convertToString(words:List[String]): String = words match {
       case h :: Nil => h
       case h :: tail => h + " " + convertToString(tail)
       
     }
     val name: String = f.getName()
     val slug: String = name.takeWhile(_!='.')

     val date = new DateTime(f.lastModified())
     try {
       val source = io.Source.fromFile(f)
       val contents = source.mkString
       source.close()

       val titleWords: Array[String] = slug.split("-").map(upperCase(_))
       val title: String = convertToString(titleWords.toList)
       val body  = Transform from Markdown to laika.render.HTML fromString contents toString()
       Some(Blog(title, body, slug, date))
     }
     catch  {
       case _: FileNotFoundException => {
         println("No such file " + name )
         None
       }
       case ex: IOException => {
         println(ex.printStackTrace())
         None
       }
     }

   }
  
  // def list(): List[Blog] = find(MongoDBObject.empty).sort(orderBy = MongoDBObject("_id" -> -1)).toList.map(_.blog)


  def bySlug(slug: String): Option[Blog] = {
     val file: File = new File("app/blogs/" + slug + ".md")
     parseBlog(file)
  
  }

  def updateBlog(b: Blog) = {
//    update(MongoDBObject("blog.slug"->b.slug),
//      MongoDBObject(
//        "$set" -> MongoDBObject(
//          ("blog" -> grater[Blog].asDBObject(b))
//        )
//      ),
//      upsert=true
//    )
    null
  }
}

